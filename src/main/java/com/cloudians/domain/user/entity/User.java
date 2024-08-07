package com.cloudians.domain.user.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.cloudians.domain.user.dto.response.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
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
	
	public User(String userEmail, String name, String nickname, String password, char gender, String calendarType,
			Date birthdate, String birthTime, String profileUrl) {
		this.userEmail=userEmail;
		this.name=name;
		this.nickname=nickname;
		this.password=password;
		this.gender=gender;
		this.profileUrl=profileUrl;
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
                .profileUrl(this.profileUrl)
                .calendarType(this.calendarType)
                .birthdate(this.birthdate)
                .birthTime(this.birthTime)
                .build();
    }
//    
    
}
