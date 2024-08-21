package com.cloudians.domain.user.dto.request;

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
public class NotificationRequest {
    private Long notificationId;
    private String userEmail;
    private LocalTime notificationDiaryTime;
    private String notificationType;
    private String notificationContent;
    private boolean notificationStatus;
    private boolean notificationIsRead;
    
    public static NotificationRequest fromNotification(Notification notification) {
 	return NotificationRequest.builder()
 		.notificationId(notification.getNotificationId())
 		.userEmail(notification.getUserEmail())
 		.notificationDiaryTime(notification.getNotificationDiaryTime())
 		.notificationType(notification.getNotificationType())
 		.notificationContent(notification.getNotificationContent())
 		.notificationStatus(notification.isNotificationStatus())
 		.notificationIsRead(notification.isNotificationIsRead())
 		.build();
     }
    
    public Notification toEntity(NotificationRequest notification) {
	return Notification.builder()
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
