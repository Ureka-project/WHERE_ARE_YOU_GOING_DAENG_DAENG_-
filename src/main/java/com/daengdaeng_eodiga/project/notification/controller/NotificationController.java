package com.daengdaeng_eodiga.project.notification.controller;

import com.daengdaeng_eodiga.project.Global.Security.config.CustomOAuth2User;
import com.daengdaeng_eodiga.project.Global.dto.ApiResponse;
import com.daengdaeng_eodiga.project.notification.dto.NotiResponseDto;
import com.daengdaeng_eodiga.project.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotiResponseDto>>> fetchUnreadNotifications(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        int userId = customOAuth2User.getUserDTO().getUserid();
        List<NotiResponseDto> response = notificationService.fetchUnreadNotifications(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<String>> updateNotificationAsRead(@PathVariable int notificationId) {
        notificationService.updateNotificationAsRead(notificationId);
        return ResponseEntity.ok(ApiResponse.success("notification read successfully"));
    }
}