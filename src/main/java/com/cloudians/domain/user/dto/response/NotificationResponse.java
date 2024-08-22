package com.cloudians.domain.user.dto.response;

import java.time.LocalTime;

import com.cloudians.domain.user.entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private Long notificationId;
    private String userEmail;
    private LocalTime notificationDiaryTime;
    private String notificationType;
    private String notificationContent;
    private boolean notificationStatus;
    private boolean notificationIsRead;
    
    public static NotificationResponse fromNotification(Notification notification) {
	return NotificationResponse.builder()
		.notificationId(notification.getNotificationId())
		.userEmail(notification.getUserEmail())
		.notificationDiaryTime(notification.getNotificationDiaryTime())
		.notificationType(notification.getNotificationType())
		.notificationContent(notification.getNotificationContent())
		.notificationStatus(notification.isNotificationStatus())
		.notificationIsRead(notification.isNotificationIsRead())
		.build();
    }
    
}
