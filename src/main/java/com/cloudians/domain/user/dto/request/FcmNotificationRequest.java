package com.cloudians.domain.user.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Getter
@ToString
@NoArgsConstructor
public class FcmNotificationRequest {
    // 기기가 발급받은 FCM 토큰 .
    private String token;

    //디바이스 기기로 전송하려는 푸시 메시지의 제목
    private String title;

    // 디바이스 기기로 전송하려는 푸시 메시지의 내용
    private String body;

    @Builder
    public FcmNotificationRequest(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body =body;
    }
    
    
    public FcmNotificationRequest sendForDiary(String token) {
	FcmNotificationRequest request = new FcmNotificationRequest();
	request.setToken(token);
	request.setTitle(" 님, 잊으신 것 없나요?");
	request.setBody("하루를 마무리하며 일기를 작성해 보세요!");
	return request;
    }
    
    public FcmNotificationRequest sendForWhisper(String token) {
  	FcmNotificationRequest request = new FcmNotificationRequest();
  	request.setToken(token);
  	request.setTitle(" 님, 잊으신 것 없나요?");
  	request.setBody("하루를 마무리하며 일기를 작성해 보세요!");
  	return request;
      }
}
