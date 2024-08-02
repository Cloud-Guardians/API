package com.cloudians.user.dto.response;

import java.sql.Date;

import com.cloudians.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
	

	public static UserResponse fromUser(User user)  {
		return UserResponse.builder()
                .userEmail(user.getUserEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .calendarType(user.getCalendarType())
                .birthdate(user.getBirthdate())
                .birthTime(user.getBirthTime())
                .profileUrl(user.getProfileUrl())
                .build();
	}
	
	
//	 public static UserResponse create(User user) {
//	        return new UserResponse(user);
//	    }
	
}
