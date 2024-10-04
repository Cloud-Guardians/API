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

import com.cloudians.domain.auth.controller.AuthUser;
import com.cloudians.domain.user.dto.request.UserLockRequest;
import com.cloudians.domain.user.dto.request.UserRequest;
import com.cloudians.domain.user.dto.response.UserLockResponse;
import com.cloudians.domain.user.dto.response.UserResponse;
import com.cloudians.domain.user.entity.User;
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
	    public ResponseEntity<Message> getProfileAndNickname(@AuthUser User user) {
	     Map<String, String> params = userService.getProfileAndNickname(user);
	     return successMessage(params);
	    }


	// 프로필 변경
		@PutMapping("/profile")
		public ResponseEntity<Message> editProfileAndNickname (@AuthUser User user,
				@RequestParam(value="file", required=false) MultipartFile file, @RequestParam(required=false) String editedNickname) {// 사용자 정보를 업데이트하는 서비스 호출
		   UserResponse response = userService.updateProfile(user,file, editedNickname);
		   return successMessage(response);
		}

	// 프로필 삭제
		@DeleteMapping("/profile")
		public ResponseEntity<Message> userProfileDelete (@AuthUser User user) throws Exception{
			UserResponse response = userService.deleteUserProfile(user);
			return successMessage(response);
		}

	// 내 정보 조회
	@GetMapping("/user-info")
	public ResponseEntity<Message> userInfo(@AuthUser User user){
	    log.info("조회 시작.");
	    UserResponse response = userService.userInfo(user);
	    return successMessage(user);
	}

	// 내 정보 수정
	@PutMapping("/user-info")
	public ResponseEntity<Message> userInfoUpdate(@AuthUser User user,
            @RequestBody UserRequest request){// 사용자 정보를 업데이트하는 서비스 호출
       System.out.println("controller - modify");
		UserResponse response = userService.updateUser(user, request);
        return successMessage(response);
	}

	// 앱 잠금 설정 생성
	@PostMapping("/user-lock")
	ResponseEntity<Message> userLockUpdate(@RequestBody UserLockRequest request){
	UserLockResponse response= userLockService.addNewLock(request);
	return successMessage(response);
	}


	// 앱 실행 시 잠금 설정 화면
	@GetMapping("/user-lock")
	ResponseEntity<Message> userLockCheck(@AuthUser User user, String insertCode){
	   boolean isChecked = userLockService.checkLock(user, insertCode);
	   return successMessage(isChecked);
	}

	// 앱 잠금삭제 & 비활
	@DeleteMapping("/user-lock")
	ResponseEntity<Message> userLockDelete(@AuthUser User user, String insertCode){
	  userLockService.deleteLock(user, insertCode);
	  return successMessage("done");
	}

	// 앱 잠금 번호 변경
		@PutMapping("user-lock")
		ResponseEntity<Message> userLockChange(@AuthUser User user, String beforePass, String afterPass){
		    UserLockResponse response = userLockService.changeLock(user,beforePass, afterPass);
		    return successMessage(response);

		}

	// 앱 잠금 비활성화
	@PutMapping("/user-lock-toggle")
	ResponseEntity<Message> userLockToggle(@AuthUser User user){
		  userLockService.toggleLock(user);
		 return successMessage("done");
		}



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