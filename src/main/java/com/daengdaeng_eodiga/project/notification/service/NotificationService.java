package com.daengdaeng_eodiga.project.notification.service;

import com.daengdaeng_eodiga.project.Global.exception.NotificationNotFoundException;
import com.daengdaeng_eodiga.project.Global.exception.PushTokenIsExistsException;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.notification.controller.Publisher;
import com.daengdaeng_eodiga.project.notification.dto.FcmRequestDto;
import com.daengdaeng_eodiga.project.notification.dto.NotiResponseDto;
import com.daengdaeng_eodiga.project.notification.entity.Notification;
import com.daengdaeng_eodiga.project.notification.entity.PushToken;
import com.daengdaeng_eodiga.project.notification.enums.NotificationTopic;
import com.daengdaeng_eodiga.project.notification.enums.PushType;
import com.daengdaeng_eodiga.project.notification.repository.NotificationRepository;
import com.daengdaeng_eodiga.project.notification.repository.PushTokenRepository;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final CommonCodeService commonCodeService;
    private final UserService userService;
    private final PushTokenRepository pushTokenRepository;
    private final PushTokenService pushTokenService;
    private final Publisher publisher;

    public List<NotiResponseDto> fetchUnreadNotifications(int userId) {

        List<Notification> unreadNotifications = notificationRepository.findByUser_UserIdAndReadingFalseOrderByCreatedAtDesc(userId);
        List<NotiResponseDto> notificationDtos = unreadNotifications.stream()
                .map(notification -> NotiResponseDto.builder()
                        .notificationId(notification.getNotificationId())
                        .eventType(commonCodeService.getCommonCodeName(notification.getType()))
                        .content(notification.getContent())
                        .createdDate(notification.getCreatedAt().toLocalDate().toString())
                        .createdTime(notification.getCreatedAt().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .build())
                .collect(Collectors.toList());

        return notificationDtos;
    }

    public void updateNotificationAsRead(int notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);
        notification.setReading(true);
    }

    public void savePushToken(int userId, String token, String type) {
        User user = userService.findUser(userId);
        commonCodeService.isCommonCode(type);
        isTokenExists(user, token, type);
        PushToken pushToken = PushToken.builder().user(user).token(token).pushType(type).build();
        pushTokenRepository.save(pushToken);
    }

    public void isTokenExists(User user, String token, String type) {
        if(!pushTokenRepository.findByTokenAndUserAndPushType(token, user, type).isEmpty()){
            throw new PushTokenIsExistsException();
        }
    }

    public List<PushToken> findPushTokenByUser(User user) {
        return pushTokenRepository.findByUser(user);
    }

    public void cancelPush(int userId) {
        User user = userService.findUser(userId);
        pushTokenRepository.deleteByUser(user);
    }

    public Map isNotificationConsent(int userId) {

        if(pushTokenRepository.findByUser_UserId(userId).isEmpty()){
            return Map.of("isNotificationConsent", false);
        }
        return Map.of("isNotificationConsent", true);
    }

    public FcmRequestDto createFcmRequest(List<String> token, List<Integer> userId, PushType type, String petName, String placeName, String eventName, String region) {
        return FcmRequestDto.builder()
                .token(token)
                .userId(userId)
                .type(type)
                .petName(petName)
                .placeName(placeName)
                .eventName(eventName)
                .region(region)
                .build();
    }

    @Async
    public void sendOwnerNotification(Integer ownerUserId, Integer pastOwnerUserId, String region ) {
        List<Integer> users = new ArrayList<>();
        if(ownerUserId != null){
            users.add(ownerUserId);
        }
        if(pastOwnerUserId != null){
            users.add(pastOwnerUserId);
        }
        List<PushToken> pushTokens = pushTokenService.fetchOwnerPushTokens(users,"PUSH_TYP_01");
        Map<Integer,List<String>> ownerTokens = new HashMap<>();
        pushTokens.forEach(pushToken -> {
            int userId = pushToken.getUser().getUserId();
            List<String> tokens = ownerTokens.getOrDefault(userId, new ArrayList<>());
            tokens.add(pushToken.getToken());
            ownerTokens.put(userId, tokens);
        });
        if(ownerUserId != null&&ownerTokens.get(ownerUserId)!=null){
            FcmRequestDto ownerFcmRequest = createFcmRequest(ownerTokens.get(ownerUserId), List.of(ownerUserId), PushType.OWNER, null, null, null,region);
            publisher.publish(NotificationTopic.FCM, ownerFcmRequest);
        }
        if(pastOwnerUserId != null&&ownerTokens.get(pastOwnerUserId)!=null){
            FcmRequestDto pastOwnerFcmRequest = createFcmRequest(ownerTokens.get(pastOwnerUserId), List.of(pastOwnerUserId), PushType.PAST_OWNER, null, null, null,region);
            publisher.publish(NotificationTopic.FCM, pastOwnerFcmRequest);
        }

    }
}