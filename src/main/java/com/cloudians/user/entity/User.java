package com.cloudians.user.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.cloudians.user.dto.response.UserResponse;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Builder
public class User {

	@Id
	@Column(name = "user_email")
	private String userEmail;
	
	private String name;
	
	@Column(unique = true)
	private String nickname;
	
	private String password; 
	private char gender;
	
	@Column(name = "profile_url")
	private String profileUrl;

	
	@Column(name = "calendar_type")
	private String calendarType;
	
	private Date birthdate;
	
	@Column(name = "birth_time")
	private String birthTime;
	
	@Column(name = "total_report_count")
	private int totalReportCount;
	
	private int status;
	
	@Builder
	private User(String userEmail, String name, String nickname, String password, char gender, String calendarType,
			Date birthdate, String birthTime) {
		this.userEmail=userEmail;
		this.name=name;
		this.nickname=nickname;
		this.password=password;
		this.gender=gender;
		this.calendarType=calendarType;
		this.birthdate=birthdate;
		this.birthTime=birthTime;
	}
	
	// Convert User entity to UserResponse DTO
    public UserResponse toDto() {
        return UserResponse.builder()
                .userEmail(this.userEmail)
                .name(this.name)
                .nickname(this.nickname)
                .gender(this.gender)
                .calendarType(this.calendarType)
                .birthdate(this.birthdate)
                .birthTime(this.birthTime)
                .build();
    }
}
