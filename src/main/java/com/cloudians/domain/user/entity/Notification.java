package com.cloudians.domain.user.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.cloudians.domain.user.dto.request.NotificationRequest;
import com.cloudians.domain.user.dto.response.NotificationResponse;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="notification_id")
    private Long notificationId;
    
    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;
    
    @Column(name="notification_diary_time")
    private  LocalTime notificationDiaryTime;
    
    @Column(name="notification_type")
    private String notificationType;
    
    @Column(name="notification_content")
    private  String notificationContent;
    
    @Column(name="notification_status")
    private boolean notificationStatus;
    
    @Column(name="notification_is_read")
    private boolean notificationIsRead;
    
    @Builder
    public Notification(Long notificationId, User user, LocalTime notificationDiaryTime, String notificationType, String notificationContent, boolean notificationStatus, boolean notificationIsRead) {
	this.notificationId=notificationId;
	this.user=user;
	this.notificationDiaryTime=notificationDiaryTime;
	this.notificationType=notificationType;
	this.notificationContent=notificationContent;
	this.notificationStatus=notificationStatus;
	this.notificationIsRead=notificationIsRead;
    }
    
    public Notification diaryTimeEdit(NotificationRequest request) {
	if(request.getNotificationDiaryTime()!=null) {
	    this.notificationDiaryTime=request.getNotificationDiaryTime();
	}
	return this;
    }
    
    public Notification toggle(User user) {
	this.user=user;
	this.notificationStatus=!notificationStatus;
	return this;
    }
    
    public NotificationResponse toDto() {
	return NotificationResponse.builder()
		.notificationId(this.notificationId)
		.userEmail(this.user.getUserEmail())
		.notificationDiaryTime(this.notificationDiaryTime)
		.notificationType(this.notificationType)
		.notificationContent(this.notificationContent)
		.notificationStatus(this.notificationStatus)
		.notificationIsRead(this.notificationIsRead)
		.build();
    }
    
 

}
