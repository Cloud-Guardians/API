package com.cloudians.global.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FirebaseConfig {
	
	 @PostConstruct
	    public void initialize() {
	        if (FirebaseApp.getApps().isEmpty()) { // FirebaseApp이 초기화되어 있는지 확인
	            try {
	                // Firebase 인증 파일의 URL
	                String fileUrl = "https://firebasestorage.googleapis.com/v0/b/cloudians-photo.appspot.com/o/key%2Fcloudians-photo-key.json?alt=media&token=f80216aa-07c3-49a3-97d8-c1c1127c9bb1";
	                
	                // URL 객체를 생성
	                URL url = new URL(fileUrl);
	                
	                // HttpURLConnection 객체를 생성
	                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	                connection.setRequestMethod("GET");
	                
	                // InputStream을 얻어 JSON 데이터를 읽습니다
	                InputStream inputStream = connection.getInputStream();
	                
	                // GoogleCredentials로부터 인증을 생성
	                GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
	                
	                FirebaseOptions options = new FirebaseOptions.Builder()
	                    .setCredentials(credentials)
	                    .build();
	                
	                // Firebase 초기화
	                FirebaseApp.initializeApp(options);
	                
	                log.info("Firebase initialized successfully.");
	                
	                // InputStream 닫기
	                inputStream.close();
	                
	            } catch (IOException e) {
	                log.error("Error initializing Firebase: ", e);
	            }
	        } else {
	            log.info("Firebase is already initialized.");
	        }
	    }

}
