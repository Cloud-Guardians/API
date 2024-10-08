package com.cloudians.domain.user.controller;

import java.io.IOException;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;
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

import com.cloudians.domain.auth.controller.AuthUser;
import com.cloudians.domain.user.dto.request.FcmNotificationRequest;
import com.cloudians.domain.user.dto.request.NotificationRequest;
import com.cloudians.domain.user.dto.response.NotificationResponse;
import com.cloudians.domain.user.entity.NotificationType;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.service.FcmNotificationService;
import com.cloudians.global.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class FcmNotificationController {

    private final FcmNotificationService fcmService;
    
    private ResponseEntity<Message> successMessage (Object object){
	    Message message = new Message(object,null,HttpStatus.OK.value());
	    return ResponseEntity.status(HttpStatus.OK).body(message);
	}
	
    public ResponseEntity<Message> errorMessage (Exception e){
	    Message errorMessage = new Message(e, HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}
   
    @PostMapping("/v1/fcm/send")
    public String pushMessage(@RequestBody FcmNotificationRequest request) throws IOException{
	System.out.println("pushMessage:"+request.getToken());
	log.debug("[+] send to push message");
	try {
	    int result = fcmService.sendMessageTo(request);
            System.out.println(request.getBody());
	    return successMessage(request.getBody()).toString();
	}catch(Exception e) {
	    return e.toString();
	}
    }
    
    
    @PostMapping("/v1/fcm/sendDaily")
    public ResponseEntity<Message> pushDailyMessage(@AuthUser User user) throws IOException{
	log.debug("[+] send to push message");
	    return successMessage(user);}
    
    @GetMapping("/v1/fcm/token")
    public String tokenTest() throws IOException{
	String token = fcmService.getAccessToken();
	return token;
    }
    

	// 커뮤니티 알림 생성/삭제
	
	// 커뮤니티 알림 조회
	

    // 위스퍼 알림 시간 등록
	@PostMapping("/whisper-notification")
	ResponseEntity<Message> insertWhisperNotification(@AuthUser User user, @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time){
	    NotificationResponse response =    fcmService.insertHomeNotification(user,NotificationType.WHISPER,time);
	    return successMessage(response);
	}
	
	// 위스퍼 알림 삭제
    	@DeleteMapping("/whisper-notification")
    	ResponseEntity<Message> deleteWhisperNotification(@AuthUser User user){
    	fcmService.deleteHomeNotification(user,NotificationType.WHISPER);
    	return successMessage("done");
    	}
    	
    	// 위스퍼 알림 수정
	@PutMapping("/whisper-notification")
    	ResponseEntity<Message> changeWhisperNotification(@AuthUser User user, @RequestParam NotificationRequest request){
    	    NotificationResponse response = fcmService.editHomeNotification(user, request, NotificationType.WHISPER);
    	    return successMessage(response);
    	}
	
	// 다이어리 알림 토글
    	@PutMapping("/whisper-notification-toggle")
    	ResponseEntity<Message> changeWhisperNotificationToggle(@AuthUser User user){
    	    fcmService.toggleHomeNotification(user,NotificationType.WHISPER);
    	    return successMessage("done");
    	}
    	
	
    
	// 다이어리 알림 추가
	@PostMapping("/diary-notification")
	ResponseEntity<Message> insertDiaryNotification(@AuthUser User user, @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time){
	    NotificationResponse response =    fcmService.insertHomeNotification(user,NotificationType.DIARY,time);
	    return successMessage(response);
	}
	
	// 다이어리 알림 삭제
    	@DeleteMapping("/diary-notification")
    	ResponseEntity<Message> deleteDiaryNotification(@AuthUser User user){
    	fcmService.deleteHomeNotification(user,NotificationType.DIARY);
    	return successMessage("done");
    	}
	
	// 다이어리 알림 수정
    	@PutMapping("/diary-notification")
    	ResponseEntity<Message> changeDiaryNotification(@AuthUser User user, @RequestParam NotificationRequest request){
    	NotificationResponse response = fcmService.editHomeNotification(user, request, NotificationType.DIARY);
	    return successMessage(response);
    	}
    	
    	// 다이어리 알림 토글
    	@PutMapping("/diary-notification-toggle")
    	ResponseEntity<Message> changeDiaryNotificationToggle(@AuthUser User user){
    	    fcmService.toggleHomeNotification(user,NotificationType.DIARY);
    	    return successMessage("done");
    	}


    
}
