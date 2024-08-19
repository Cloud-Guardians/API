package com.cloudians.global.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FirebaseConfig {
	
    
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
	System.out.println("현재 firebaseApp의 상태는?");
        if (FirebaseApp.getApps().isEmpty()) {
            System.out.println("firebaseApp가 존재하지 않기 때문에 생성해드리겠습니다.");
            try (InputStream inputStream = new FileInputStream("src/main/resources/cloudians-photo-key.json")) {
                GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(credentials)
                        .build();
                return FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                log.error("Error initializing Firebase: ", e);
                throw e;
            }
        } else {
            System.out.println(!FirebaseApp.getInstance().toString().isEmpty());
            System.out.println("firebaseApp가 정상적으로 존재합니다.");
            return FirebaseApp.getInstance();
        }
    }

}
