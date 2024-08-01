package com.cloudians.user.dto.response;

import java.sql.Date;

import com.cloudians.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private String userEmail;
	private String name;
	private String nickname;
	private char gender;
	private String calendarType;
	private Date birthdate;
	private String birthTime;
	private String profileUrl;
	
	private int totalReportCount;
	private int status;
	
	private String accessToken;
	private String refreshToken;
	private String fcmToken;
	
	@Builder
	private UserResponse(User user) {
		this.userEmail=user.getUserEmail();
		this.name=user.getName();
		this.nickname=user.getNickname();
		this.gender=user.getGender();
		this.calendarType=user.getCalendarType();
		this.birthdate=user.getBirthdate();
		this.birthTime=user.getBirthTime();
		this.profileUrl=user.getProfileUrl();
	}
	
}
