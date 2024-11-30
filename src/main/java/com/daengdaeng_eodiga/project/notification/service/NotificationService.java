package com.daengdaeng_eodiga.project.notification.service;

import com.daengdaeng_eodiga.project.Global.exception.NotificationNotFoundException;
import com.daengdaeng_eodiga.project.Global.exception.PushTokenIsExistsException;
import com.daengdaeng_eodiga.project.common.service.CommonCodeService;
import com.daengdaeng_eodiga.project.notification.dto.NotiResponseDto;
import com.daengdaeng_eodiga.project.notification.entity.Notification;
import com.daengdaeng_eodiga.project.notification.entity.PushToken;
import com.daengdaeng_eodiga.project.notification.repository.NotificationRepository;
import com.daengdaeng_eodiga.project.notification.repository.PushTokenRepository;
import com.daengdaeng_eodiga.project.user.entity.User;
import com.daengdaeng_eodiga.project.user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final CommonCodeService commonCodeService;
    private final UserService userService;
    private final PushTokenRepository pushTokenRepository;

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
}