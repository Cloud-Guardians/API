package com.cloudians.domain.user.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    
    @Column(name="user_email")
    private String userEmail;
    
    @Column(name="notification_diary_time")
    private  Time notificationDiaryTime;
    
    @Column(name="notification_type")
    private int notificationType;
    
    @Column(name="notification_content")
    private  String notificationContent;
    
    @Column(name="notification_status")
    private boolean notificationStatus;
    
    @Column(name="notification_is_read")
    private boolean notificationIsRead;
    
    @Builder
    public Notification(Long notificationId, String userEmail, Time notificationDiaryTime, int notificationType, String notificationContent, boolean notificationStatus, boolean notificationIsRead) {
	this.notificationId=notificationId;
	this.userEmail=userEmail;
	this.notificationDiaryTime=notificationDiaryTime;
	this.notificationType=notificationType;
	this.notificationContent=notificationContent;
	this.notificationStatus=notificationStatus;
	this.notificationIsRead=notificationIsRead;
    }
    
    public NotificationResponse toDto() {
	return NotificationResponse.builder()
		.notificationId(this.notificationId)
		.userEmail(this.userEmail)
		.notificationDiaryTime(this.notificationDiaryTime)
		.notificationType(this.notificationType)
		.notificationContent(this.notificationContent)
		.notificationStatus(this.notificationStatus)
		.notificationIsRead(this.notificationIsRead)
		.build();
    }
    
 

}
