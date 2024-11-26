package com.daengdaeng_eodiga.project.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotiResponseDto {
    private int notificationId;
    private String eventType;
    private String content;
    private String createdDate;
    private String createdTime;

    @Builder
    public NotiResponseDto(int notificationId, String eventType, String content, String createdDate, String createdTime) {
        this.notificationId = notificationId;
        this.eventType = eventType;
        this.content = content;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }
}
