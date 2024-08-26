package com.cloudians.domain.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudians.domain.publicdiary.dto.response.comment.PublicDiaryCommentResponse;
import com.cloudians.domain.publicdiary.dto.response.diary.PublicDiaryThumbnailResponse;
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
	
	@GetMapping("/test")
	public ResponseEntity<Message> testEndpoint() {
	    String result = "Test endpoint working";
	    return successMessage(result);
	}
	
	@PostMapping("/login")
	    public ResponseEntity<Map<String, String>> loginTest(@RequestBody Map<String, Object> request) {
	        // 로그인 로직을 여기에 추가합니다.
	        boolean loginSuccessful = true; // 실제 로그인 로직을 구현하여 결과를 기반으로 설정해야 합니다.
	        Map<String, Object> userRequestMap = (Map<String, Object>) request.get("userRequest");
	        String userEmail = (String) userRequestMap.get("userEmail");
	        String password = (String) userRequestMap.get("password");
	        String fcmToken = (String) request.get("fcmToken");
	        
	        System.out.println("request:"+request.toString());
	        System.out.println(userEmail);
	        System.out.println(password);
	        if (loginSuccessful) {
	            try {
	                // FCM 토큰 발급
	        	System.out.println("user 조회 시작");
	        	User user = userService.findUserByEmailOrThrow(userEmail);

	                userService.userLogin(user.getUserEmail(),user.getPassword(),fcmToken);
	                // 콘솔에 토큰을 출력
	                System.out.println("Generated FCM Token: " + fcmToken);

	                // 클라이언트에게 FCM 토큰을 반환
	                Map<String, String> response = new HashMap<>();
	                response.put("fcmToken", fcmToken);
	                return ResponseEntity.ok(response);
	            } catch (Exception e) {
	                e.printStackTrace();
	                return ResponseEntity.status(500).body(Map.of("error", "Failed to generate FCM token"));
	            }
	        } else {
	            // 로그인 실패 시
	            return ResponseEntity.status(401).body(Map.of("error", "Login failed!"));
	        }
	    }

	
	// 탈퇴
	@GetMapping("/withdraw")
	public ResponseEntity<Message> userWithdraw(@RequestParam("userEmail") String userEmail){
	    boolean result = userService.unregisterUser(userEmail);
	    return successMessage(result);
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
		   Map<String, String> user = userService.updateProfile(userEmail,file);
		   return successMessage(user);
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
	    UserResponse user = userService.userInfo(userEmail);
	    return successMessage(user);
	}
	
	// 내 정보 수정
	@PutMapping("/user-info")
	public ResponseEntity<Message> userInfoUpdate(@RequestParam("userEmail") String userEmail,
            @RequestBody UserRequest userRequest){// 사용자 정보를 업데이트하는 서비스 호출
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
		@PutMapping("user-lock")
		ResponseEntity<Message> userLockChange(@RequestParam("userEmail") String userEmail, String beforePass, String afterPass){
		    UserLockResponse user = userLockService.changeLock(userEmail,beforePass,afterPass);
		    return successMessage(user);
		    
		}
	
	// 앱 잠금 비활성화
	@PutMapping("/user-lock-toggle")
	ResponseEntity<Message> userLockToggle(@RequestParam("userEmail") String userEmail){
		  userLockService.toggleLock(userEmail);
		 return successMessage("done");
		}
	
	
	// 유저 작성글 조회
	@GetMapping("/{userEmail}/publicDiaries")
	ResponseEntity<Message> getUserPublicDiaries(@PathVariable("userEmail") String userEmail){
	    List<PublicDiaryThumbnailResponse> list = userService.getPublicDiaries(userEmail);
	    return successMessage(list);
	}
	
	
	// 유저 댓글 조회
	@GetMapping("/{userEmail}/comments")
	ResponseEntity<Message> getUserComments(@PathVariable("userEmail") String userEmail){
	    List<Map<String,Object>> list = userService.getPublicComments(userEmail);
	    return successMessage(list);
	}
	
	

	
	

}