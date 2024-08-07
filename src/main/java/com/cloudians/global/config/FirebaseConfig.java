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
	        if (FirebaseApp.getApps().isEmpty()) {
	            try {
	                String fileUrl = "https://firebasestorage.googleapis.com/v0/b/cloudians-photo.appspot.com/o/key%2Fcloudians-photo-key.json?alt=media&token=f80216aa-07c3-49a3-97d8-c1c1127c9bb1";
	                URL url = new URL(fileUrl);
	                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	                connection.setRequestMethod("GET");
	                InputStream inputStream = connection.getInputStream();
	                GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
	               

	                
	                FirebaseOptions options = new FirebaseOptions.Builder()
	                    .setCredentials(credentials)
	                    .build();
	                FirebaseApp.initializeApp(options);	                
	                log.info("Firebase initialized successfully.");
	                inputStream.close();                
	            } catch (IOException e) {
	                log.error("Error initializing Firebase: ", e);
	            }
	        } else {
	            log.info("Firebase is already initialized.");
	        }
	    }

}
