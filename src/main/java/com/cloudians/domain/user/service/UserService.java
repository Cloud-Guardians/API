package com.cloudians.domain.user.service;

import com.cloudians.domain.user.dto.request.UserRequest;
import com.cloudians.domain.user.dto.response.UserResponse;


public interface UserService {

	 // 사용자 정보 조회
	UserResponse findByEmail(String userEmail);

	UserResponse findByEmail2(String userEmail);

	// 사용자 정보 변경
	UserResponse updateUser(String userEmail, UserRequest userRequest);
	
	// 사용자 프로필 변경
	
	// 사용자 프로필 조회
	 
}
