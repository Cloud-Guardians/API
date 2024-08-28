package com.cloudians.domain.user.dto.response;

import java.time.LocalDate;

import com.cloudians.domain.user.entity.UserStatus;
import com.cloudians.domain.user.entity.BirthTimeType;
import com.cloudians.domain.user.entity.CalendarType;
import com.cloudians.domain.user.entity.SignupType;
import com.cloudians.domain.user.entity.User;

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
	private SignupType signupType;
	private String name;
	private String nickname;
	private char gender;
	private CalendarType calendarType;
	private LocalDate birthdate;
	private BirthTimeType birthTime;
	private String profileUrl;

	private int totalReportCount;
	private UserStatus status;

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
