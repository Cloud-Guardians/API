package com.cloudians.domain.user.service;



import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cloudians.domain.auth.entity.UserToken;
import com.cloudians.domain.auth.repository.UserTokenRepository;
import com.cloudians.domain.user.dto.request.FcmNotificationRequest;
import com.cloudians.domain.user.dto.request.NotificationRequest;
import com.cloudians.domain.user.dto.response.FcmNotificationResponse;
import com.cloudians.domain.user.dto.response.NotificationResponse;
import com.cloudians.domain.user.entity.Notification;
import com.cloudians.domain.user.entity.NotificationType;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.NotificationException;
import com.cloudians.domain.user.exception.NotificationExceptionType;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.NotificationRepository;
import com.cloudians.domain.user.repository.UserRepository;
import com.cloudians.global.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FcmNotificationService {

    public final String firebaseConfigPath = "cloudians-photo-key.json";
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository; 
    private final TaskScheduler taskScheduler; 
    private  boolean updated = false;
    
	
 // uesr home notification insert O
 	public NotificationResponse insertHomeNotification(User user, NotificationType type, LocalTime time) {
 	    System.out.println("일단 들어옴");
 	    // time 형식 21:00:00 .. 이렇게 해야 댐
 	    Notification notification = new Notification();
 	    notification.setUser(user);
 	    notification.setNotificationDiaryTime(time);
 	    notification.setNotificationType(type.toString());
 	    notification.setNotificationStatus(true);
 	    notification.setNotificationIsRead(true);   
 	    notificationRepository.save(notification);
 	    return notification.toDto();	}
    
    
 	private Notification findNotificationByUserEmailAndType(User user, NotificationType type) {
 	    Notification notification = notificationRepository.findByUserAndNotificationType(user, type.toString())
 		    .orElseThrow(()-> new NotificationException(NotificationExceptionType.NOTIFICATION_NOT_FOUND));
 	    return notification;
 	}
	
	// user home notification time change O
	public NotificationResponse editHomeNotification(User user, NotificationRequest request, NotificationType type) {
	    Notification notification = findNotificationByUserAndType(user, type);
	    notification.diaryTimeEdit(request);
	    return NotificationResponse.of(notification);
	}
	
	 // home toggle on, off 
	    public boolean toggleHomeNotification(User user,NotificationType type) {
		  Notification notification = findNotificationByUserEmailAndType(user, type);
		  notification.toggle(user);
			    return true;    
	    }
	    
	// user home notification time delete
	public void deleteHomeNotification(User user, NotificationType type) {
	    Notification notification = findNotificationByUserEmailAndType(user, type);
	    notificationRepository.delete(notification);
	}
    // whisper&diary
	
	
// 매일  12:00에 자동으로 실행시켜 줌
 @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
public void sendHomeNotificationToAll() {
    List<Notification> notifications = notificationRepository.findAll();
    for(Notification notification:notifications) {
	if(notification.getNotificationType().equals(NotificationType.DIARY.toString())) {
	    sendHomeNotification(notification,NotificationType.DIARY);
	} else if(notification.getNotificationType().equals(NotificationType.WHISPER.toString())) {
	    sendHomeNotification(notification,NotificationType.WHISPER);
	}
    }
}


    public void sendHomeNotification(Notification notification, NotificationType type) {
	// notification entity에서 값을 받아와 가지고, 알림 타입과 알림 상태가 일치하면 지정 알림 시간을 등록하여, 알림을 전송 

	
	  
	String hours = notification.getNotificationDiaryTime().toString().split(":")[0];
	String minutes = notification.getNotificationDiaryTime().toString().split(":")[1];
	String cronText = minutes+" "+hours+" * * * *";

	// cronText : 0 0 1 2 * *로 나타나고, 매일 정각 열두시를 의미
	
	String accessToken = "";
	
	FcmNotificationRequest request = new FcmNotificationRequest();
	if(type==NotificationType.DIARY && notification.isNotificationStatus()) {
	    request.sendForDiary(accessToken);
	} else if(type==NotificationType.WHISPER && notification.isNotificationStatus()) {
	    request.sendForWhisper(accessToken);
	}

	taskScheduler.schedule(() -> {
            try {
        	log.info("ing");
                sendMessageTo(request);
                log.info(request.getTitle()+":"+request.getBody());
            } catch (Exception e) {
                Message message = new Message(e,NotificationExceptionType. NOTIFICATION_SEND_FAILURE.getStatusCode());
              System.out.println("error:"+message);
            }
        }, new CronTrigger(cronText));
	System.out.println("Scheduled task with cron: " + cronText);
	
    }
    
    // 댓글과 좋아요 알림  ... 수정중 
//    public void communityNotificationPush(String userEmail, NotificationType type) {
//	List<Notification> notifications = (List<Notification>)param.get("notification");
//	Notification notification = new Notification();
//	for(Notification not : notifications) {
//	    if(not.getNotificationType().equals(type.toString()) && not.isNotificationStatus()) {
//		notification = not;
//	    }
//	}
//	
//	
//    }
    
    /**
     * 푸시 메시지 처리를 수행하는 비즈니스 로직
     * 매개변수 이름 / 설명
     * @param fcmSendDto 모바일에서 전달받은 Object
     * 메서드가 반환하는 값의 유형과 의미 설명
     * @return 성공(1), 실패(0)
     */
    public int sendMessageTo(FcmNotificationRequest request) throws IOException {
	// 내부 메소드 makeMessage를 통해 request에 든 데이터로 메시지 내용 문자화
        String message = makeMessage(request);
        
        // RestEmplate : 스프링에서 http 요청 수행하고 응답 처리할 때 사용
        // 			  http 요청 생성시켜서 메소드(get, post, delete, put)을 사용해 서버로부터 전송하여 데이터를 조회하거나 전송, 리소스 수정 및 삭제 때 이용함
        //			  서버로부터 받은 응답을 Java 객체로 매핑해 사용할 수 있음
        // 			  FCM(Firebase Cloud Messaging) api를 호출해서 메시지 전송하기 위해 사용함
        RestTemplate restTemplate = new RestTemplate();
        
       /**
         * 추가된 사항 : RestTemplate 이용중 클라이언트의 한글 깨짐 증상에 대한 수정
         * @refernece : https://stackoverflow.com/questions/29392422/how-can-i-tell-resttemplate-to-post-with-utf-8-encoding
         */
        
        // 메시지로 변환 : 인코딩 설정 
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        // HttpHeaders : Content-Type/application/json 으로 설정하여 'Authorization' 헤더에 Bearer 토큰을 추가해 인증하려고 만듦
        HttpHeaders headers = new HttpHeaders();
        // json type  
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 인증 토큰
        headers.set("Authorization", "Bearer " + getAccessToken());
        System.out.println("headers:"+headers);
        // HttpEntity : 메시지 본문에 헤더를 포함시켜 http 요청 구성  -> restTemplate의 exchange 메소드에 전달될 예정
        HttpEntity entity = new HttpEntity<>(message, headers);
        System.out.println("entity:"+entity);
        //  FCM API URL로, 메시지를 전송할 fcm api의 엔드포인트 Url 
        String API_URL = "https://fcm.googleapis.com/v1/projects/cloudians-photo/messages:send";
        
        // RestTemplate's exchange 메드를 이용해 http post요청 수행해서 받은 응답을 Json으로 가져오니까 String으로 처리해서 가져옴 
        ResponseEntity response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
        
        System.out.println("response:"+response);
        // 응답 상태 코드 콘솔에 출력하기 
        System.out.println(response.getStatusCode());

        // 응답 상태 코드 200일 때 1, 아닐 때 0 반환
        return response.getStatusCode() == HttpStatus.OK ? 1 : 0;
    }

    
    
    // 토큰 얻기
    /**
     * Firebase Admin SDK의 비공개 키를 참조하여 Bearer 토큰을 발급 받습니다.
     *
     * @return Bearer token
     */
    public String getAccessToken() throws IOException {
	
	// inputStream을 이용하여 인증에 성공한 GoogleCredentials 객체를 생성 (Admin 인증 정보 처리할 때 사용됨)
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        System.out.println(googleCredentials.toString());
        // 토큰 만료 시 갱신
       googleCredentials.refreshIfExpired();
        
        // 토큰값 반환
        return googleCredentials.getAccessToken().getTokenValue();
    }

    // 메시지 만드는 메소드라서 String 형태로 반환된다. 
    /**
     * FCM 전송 정보를 기반으로 메시지를 구성합니다. (Object -> String)
     *
     * @param FcmNotificationRequest UserNotificationRequest
     * @return String
     */
    private String makeMessage(FcmNotificationRequest request) throws JsonProcessingException {
	// Jackson의 ObjectMapper를 생성함 (why? JSON 문자열을 java 객체로 바꾸려면 Jackson으로 바꿔 줘야 해서 ..
	// Jacson은 Json 데이터를 java 객체로 바꾸거나 반대로 할 때 사용하는 라이브러리
	// Json -> java 역직렬화, java -> Json 직렬화
        ObjectMapper om = new ObjectMapper();
        
        //  보내야 되는 userNotificationDto를 빌더로 만들어 객체 생성
        FcmNotificationResponse response = FcmNotificationResponse.builder()
                .message(FcmNotificationResponse.Message.builder()
                	// 메시지를 수신하는 사용자의 토큰
                        .token(request.getToken())
                        .notification(FcmNotificationResponse.Notification.builder()
                                .title(request.getTitle())
                                .body(request.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();
        // false : 메시지가 실제로 전송됨 , true: 메시지가 실제로 전송되지 않음
        System.out.println("makeMessage:"+request.getToken());
        System.out.println("body:"+request.getBody());
        // 자바 객체인 UserNotificationResponse를 Json 문자열로 직렬화해서 반환
        System.out.println(om.toString());
        return om.writeValueAsString(response);
    }
    
   
    private Notification findNotificationByUserAndType(User user, NotificationType type) {
	return  notificationRepository.findByUserAndNotificationType(user, type.toString())
		.orElseThrow(()-> new NotificationException(NotificationExceptionType.NOTIFICATION_NOT_FOUND));
    }
	
	private User findUserByUserEmail(User user) {
	   return userRepository.findByUserEmail(user.getUserEmail())
	                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
	}
    
}
