package com.cloudians.user.service;

import com.cloudians.user.dto.request.UserRequest;
import com.cloudians.user.dto.response.UserResponse;


public interface UserService {

	 // 사용자 정보 조회
	UserResponse findByEmail(String userEmail);
	
	// 사용자 정보 변경
	UserResponse updateUser(String userEmail, UserRequest userRequest);
	
	// 사용자 프로필 변경
	
	// 사용자 프로필 조회
	 
}
