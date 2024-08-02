package com.cloudians.config;

import java.io.FileInputStream;
import java.io.IOException;

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
	                FileInputStream serviceAccount = new FileInputStream("/Users/orcor/Downloads/cloudians-photo-key.json");

	                FirebaseOptions options = new FirebaseOptions.Builder()
	                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
	                    .build();
	                
	               
	                FirebaseApp.initializeApp(options);
	                
	                log.info("Firebase initialized successfully.");
	            } catch (IOException e) {
	                log.error("Error initializing Firebase: ", e);
	            }
	        } else {
	            log.info("Firebase is already initialized.");
	            
	        }
	    }
	
	

}
