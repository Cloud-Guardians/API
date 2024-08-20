package com.cloudians.domain.user.controller;

import java.io.IOException;
import java.sql.Time;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudians.domain.user.dto.request.FcmNotificationRequest;
import com.cloudians.domain.user.dto.response.NotificationResponse;
import com.cloudians.domain.user.service.FcmNotificationService;
import com.cloudians.global.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
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
    public ResponseEntity<Message> pushDailyMessage(@RequestParam String userEmail) throws IOException{
	log.debug("[+] send to push message");
	    fcmService.sendHomeNotification(userEmail);
	    return successMessage(userEmail);}
    
    @GetMapping("/v1/fcm/token")
    public String tokenTest() throws IOException{
	String token = fcmService.getAccessToken();
	return token;
    }
    

	// 커뮤니티 알림 생성/삭제
	
	// 커뮤니티 알림 조회
	
	
	// 홈 알림 등록 시 자동으로 .. 진행되게 해야겠네
    
	@PostMapping("/home-notification")
	ResponseEntity<Message> insertHomeNotification(@RequestParam String userEmail, Time time){
	    NotificationResponse response =    fcmService.insertHomeNotification(userEmail,(Time)time);
	    fcmService.sendHomeNotification(userEmail);
	    return successMessage(response);
	}
	
	// 홈 알림 삭제
    	@DeleteMapping("/home-notification")
    	ResponseEntity<Message> deleteHomeNotification(@RequestParam String userEmail){
    	fcmService.deleteHomeNotification(userEmail);
    	return successMessage("done");
    	}
	
	// 홈 알림 수정
    	@PutMapping("/home-notification")
    	ResponseEntity<Message> changeHomeNotification(@RequestParam String userEmail, Time changeTime){
    	    fcmService.changeHomeNotification(userEmail, changeTime);
    	    return successMessage(userEmail+changeTime);
    	}
    	
    	// 홈 알림 토글
    	@PutMapping("/home-notification-toggle")
    	ResponseEntity<Message> changeHomeNotificationToggle(@RequestParam String userEmail){
    	    fcmService.toggleHomeNotification(userEmail);
    	    return successMessage(userEmail);
    	}


    
}
