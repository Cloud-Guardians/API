package com.cloudians.domain.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudians.domain.user.dto.request.UserRequest;
import com.cloudians.domain.user.dto.response.UserResponse;
import com.cloudians.domain.user.service.UserService;
import com.cloudians.global.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/test")
	public ResponseEntity<Message> testEndpoint() {
	    String result = "Test endpoint working";
	    Message message = new Message(result,null,HttpStatus.OK.value());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}


	// 프로필 조회 
	 @GetMapping("/profile")
	    public ResponseEntity<Message> userProfile(@RequestParam String userEmail) {
	        try {
	            System.out.println(userEmail);
	            UserResponse user = userService.findByEmail(userEmail);
	            Map<String, String> param = new HashMap<>();
	            param.put("profile", user.getProfileUrl());
	            param.put("nickname", user.getNickname());
	            Message message = new Message(param, HttpStatus.OK.value());
	            return ResponseEntity.status(HttpStatus.OK).body(message);
	        } catch (Exception e) {
	            e.printStackTrace();
	            Message errorMessage = new Message("An error occurred while fetching the user profile.", HttpStatus.BAD_REQUEST.value());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	        }
	    }
	
	
	// 프로필 등록 혹은 삭제
		@PutMapping("/profile")
		public ResponseEntity<Message> userProfileUpdate (@RequestParam String userEmail,
				@RequestParam("file") MultipartFile file) throws Exception {// 사용자 정보를 업데이트하는 서비스 호출
	       System.out.println("controller - profile - modify");
	       UserResponse user = userService.findByEmail(userEmail);
	       
	       if(user != null) {
	    	   userService.updateUserProfile(userEmail,file);
	    	Message message = new Message(user,null, HttpStatus.OK.value());
	            return ResponseEntity.status(HttpStatus.OK).body(message);
	       }
	       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	
		
		@DeleteMapping("/profile")
		public ResponseEntity<?> userProfileDelete (@RequestParam String userEmail) throws Exception{
			UserResponse user = userService.findByEmail(userEmail);
		       
		       if(user != null) {
		    	   userService.deleteUserProfile(userEmail);
		    	Message message = new Message(user,null, HttpStatus.OK.value());
		            return ResponseEntity.status(HttpStatus.OK).body(message);
		       }
		       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
		}
		
		
	// 회원 가입
		  @PostMapping("/join")
		    public ResponseEntity<Message> userJoin(@RequestBody UserRequest userRequest) {
		        try {
		            System.out.println(userRequest.getUserEmail());
		            UserResponse newUser = userService.userSignup(userRequest);
		            Message message = new Message(newUser,null, HttpStatus.OK.value());
		            return ResponseEntity.status(HttpStatus.OK).body(message);
		        } catch (Exception e) {
		            e.printStackTrace();
		            Message errorMessage = new Message("An error occurred while fetching the user profile.", HttpStatus.BAD_REQUEST.value());
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		
		        }
		    }
	
	// 내 정보 조회
	@GetMapping("/user-info")
	public ResponseEntity<Message> userInfo(@RequestParam String userEmail){
		try {
			System.out.println(userEmail);
		UserResponse userInfo = userService.findByEmail(userEmail);
		Message message = new Message(userInfo,null, HttpStatus.OK.value());
	            return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch(Exception e) {
		    e.printStackTrace();
	            Message errorMessage = new Message(e, HttpStatus.BAD_REQUEST.value());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		}
		
	}
	
	// 내 정보 수정
	@PutMapping("/user-info")
	public ResponseEntity<Message> userInfoUpdate(@RequestParam String userEmail,
            @RequestBody UserRequest userRequest){// 사용자 정보를 업데이트하는 서비스 호출
       System.out.println("controller - modify");
		UserResponse updatedUser = userService.updateUser(userEmail, userRequest);
        if (updatedUser == null) {
            Message message = new Message(updatedUser,null, HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK).body(message);
        }
        Message errorMessage = new Message("An error occurred while fetching the user profile.", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
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