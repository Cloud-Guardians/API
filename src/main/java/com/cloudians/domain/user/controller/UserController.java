package com.cloudians.domain.user.controller;

import java.util.Map;

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

import com.cloudians.domain.user.dto.request.UserLockRequest;
import com.cloudians.domain.user.dto.request.UserRequest;
import com.cloudians.domain.user.dto.response.UserLockResponse;
import com.cloudians.domain.user.dto.response.UserResponse;
import com.cloudians.domain.user.service.UserLockService;
import com.cloudians.domain.user.service.UserService;
import com.cloudians.global.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final UserLockService userLockService;



	public ResponseEntity<Message> errorMessage (Exception e){
	    Message errorMessage = new Message(e, HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}
	private ResponseEntity<Message> successMessage (Object object){
	    Message message = new Message(object,null,HttpStatus.OK.value());
	    return ResponseEntity.status(HttpStatus.OK).body(message);
	}

	// 프로필 조회
	 @GetMapping("/profile")
	    public ResponseEntity<Message> userProfile(@RequestParam("userEmail") String userEmail) {
	     Map<String, String> params = userService.getProfileAndNickname(userEmail);
	     return successMessage(params);
	    }


	// 프로필 변경
		@PutMapping("/profile")
		public ResponseEntity<Message> userProfileUpdate (@RequestParam("userEmail") String userEmail,
				@RequestParam("file") MultipartFile file) {// 사용자 정보를 업데이트하는 서비스 호출
		   UserResponse user = userService.updateProfile(userEmail,file);
		   return successMessage(user.getProfileUrl());
		}

	// 프로필 삭제
		@DeleteMapping("/profile")
		public ResponseEntity<Message> userProfileDelete (@RequestParam("userEmail") String userEmail) throws Exception{
			UserResponse user = userService.deleteUserProfile(userEmail);
			return successMessage(user);
		}

	// 내 정보 조회
	@GetMapping("/user-info")
	public ResponseEntity<Message> userInfo(@RequestParam("userEmail") String userEmail){
	    log.info("조회 시작.");
	    UserResponse user = userService.userInfo(userEmail);
	    return successMessage(user);
	}

	// 내 정보 수정
	@PutMapping("/user-info")
	public ResponseEntity<Message> userInfoUpdate(@RequestParam("userEmail") String userEmail,
            @RequestBody UserRequest userRequest){// 사용자 정보를 업데이트하는 서비스 호출
       System.out.println("controller - modify");
		UserResponse updatedUser = userService.updateUser(userEmail, userRequest);
        return successMessage(updatedUser);
	}

	// 앱 잠금 설정 생성
	@PostMapping("/user-lock")
	ResponseEntity<Message> userLockUpdate(@RequestBody UserLockRequest request){
	UserLockResponse response= userLockService.addNewLock(request);
	return successMessage(response);
	}


	// 앱 실행 시 잠금 설정 화면
	@GetMapping("/user-lock")
	ResponseEntity<Message> userLockCheck(@RequestParam("userEmail") String userEmail, String insertCode){
	   boolean isChecked = userLockService.checkLock(userEmail, insertCode);
	   return successMessage(isChecked);
	}

	// 앱 잠금삭제 & 비활
	@DeleteMapping("/user-lock")
	ResponseEntity<Message> userLockDelete(@RequestParam("userEmail") String userEmail, String insertCode){
	  userLockService.deleteLock(userEmail, insertCode);
	  return successMessage("done");
	}

	// 앱 잠금 번호 변경
//		@PutMapping("user-lock")
//		ResponseEntity<Message> userLockChange(@RequestParam("userEmail") String userEmail, String insertCode){
//		    UserLockResponse user = userLockService.changeLock(userEmail,insertCode);
//		    return successMessage(user);
//
//		}

	// 앱 잠금 비활성화
	@PutMapping("/user-lock-toggle")
	ResponseEntity<Message> userLockToggle(@RequestParam("userEmail") String userEmail){
		  userLockService.toggleLock(userEmail);
		 return successMessage("done");
		}





	// 유저 작성글 조회

	// 유저 댓글 조회




//	 커뮤니티 알림 생성/삭제
//
//	 커뮤니티 알림 조회
//
//
//	 홈 알림 호출
//
//	 홈 알림 등록
//
//
//	 홈 알림 삭제
//
//	 홈 알림 수정




}