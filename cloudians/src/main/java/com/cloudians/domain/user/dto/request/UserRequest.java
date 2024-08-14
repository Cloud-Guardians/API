package com.cloudians.domain.user.dto.request;

import java.sql.Date;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
public class UserRequest {

	private String userEmail;
	private String password;
	private String name;
	private String profileUrl;
	private String nickname;
	private char gender;
	private String calendarType;
	private Date birthdate;
	private String birthTime;
	
	public UserRequest() {
    }
}
