package com.cloudians.domain.auth.dto.request;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user")
public class UserAuthRequest {
	@Id // primary key
    @Column(name = "user_email") // 기본 키 필드
	private String userEmail;
	private String password;
	private int signupType;
	private String name;
    @Column(name = "profile_url")
	private String profileUrl;
	private String nickname;
	private char gender;
    @Column(name = "calendar_type")
	private String calendarType;
	private Date birthdate;
	@Column(name = "birth_time")
	private String birthTime;
    @Column(name = "total_report_count")
	private int totalReportCount = 0;
	@Column(nullable = false)
	private int status = 1; // roles

	 public List<String> getStatusList() {
	        if (this.status > 0) {
	            // status를 String으로 변환 후 ','로 분할
	            return Arrays.asList(String.valueOf(this.status).split(","));
	        }
	        return new ArrayList<>();
	    }
	
	@Builder
	public UserAuthRequest(String userEmail, String password, int signupType, String name, String profileUrl,
			String nickname, char gender, /*String calendarType*/ Date birthdate, String birthTime, int totalReportCount,
			int status) {
		super();
		this.userEmail = userEmail;
		this.password = password;
		this.signupType = signupType;
		this.name = name;
		this.profileUrl = profileUrl;
		this.nickname = nickname;
		this.gender = gender;
		// this.calendarType = calendarType;
		this.birthdate = birthdate;
		this.birthTime = birthTime;
		this.totalReportCount = totalReportCount;
		this.status = status;
	}

	
}

