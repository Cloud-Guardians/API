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
    

    
    public static NotificationResponse of(Notification notification) {
	return NotificationResponse.builder()
		.notificationDiaryTime(notification.getNotificationDiaryTime())
		.notificationType(notification.getNotificationType())
		.notificationStatus(notification.isNotificationStatus())
		.build();
    }
    
}
