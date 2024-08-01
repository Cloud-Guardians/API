package com.cloudians.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudians.user.dto.request.UserRequest;
import com.cloudians.user.dto.response.UserResponse;
import com.cloudians.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/test")
	public ResponseEntity<String> testEndpoint() {
	    return new ResponseEntity<>("Test endpoint working", HttpStatus.OK);
	}
	// 프로필 생성
	

	// 프로필 조회
	
	// 프로필 수정
	
	
	// 내 정보 조회
	@GetMapping("/user-info")
	public ResponseEntity<?> userInfo(@RequestParam String userEmail){
		try {
		UserResponse userInfo = userService.findByEmail(userEmail);
			return new ResponseEntity<>(userInfo,HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
		return new ResponseEntity<>(e,HttpStatus.BAD_REQUEST);
		}
		
	}
	
	// 내 정보 수정
	@PutMapping("/user-info")
	public ResponseEntity<UserResponse> userInfoUpdate(@RequestParam String userEmail,
            @RequestBody UserRequest userRequest){// 사용자 정보를 업데이트하는 서비스 호출
       System.out.println("controller - modify");
		UserResponse updatedUser = userService.updateUser(userEmail, userRequest);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
	}
	
	
	
	// 유저 작성글 조회
	
	// 유저 댓글 조회
	
	
	
	// 앱 잠금 설정 생성/삭제
	
	// 앱 실행 시 잠금 설정 화면
	
	
	// 오류 제보
	

	
	// 커뮤니티 알림 생성/삭제
	
	// 커뮤니티 알림 조회
	
	
	// 홈 알림 생성/삭제
	
	// 홈 알림 수정
	
	

}
