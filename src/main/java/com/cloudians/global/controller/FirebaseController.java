package com.cloudians.global.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudians.global.Message;
import com.cloudians.global.service.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@RestController
@RequestMapping("/api/global")
public class FirebaseController {
	
	@Autowired
	private FirebaseService firebaseService;
	private FirebaseAuth fireAuth = FirebaseAuth.getInstance();
	
	public ResponseEntity<Message> errorMessage (Exception e){
	    System.out.println(e);
	    
	    Message errorMessage = new Message(e.toString(), HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
	}
	
	public ResponseEntity<Message> successMessage (Object object){
	    Message message = new Message(object,null,HttpStatus.OK.value());
	    return ResponseEntity.status(HttpStatus.OK).body(message);
	}
	
	@GetMapping("/testFiles")
	public ResponseEntity<Message> testFiles() throws FirebaseAuthException {
	    String uid = "c51e4662168a7a006bf6082dcd7a16ba5ff3fb0b";
	  return successMessage(uid);
	}
	
	
	// custom Token
	@GetMapping("/token")
	public ResponseEntity<Message> tokenTest() throws FirebaseAuthException{
		String uid = "c51e4662168a7a006bf6082dcd7a16ba5ff3fb0b";
		try {
			String customToken = FirebaseAuth.getInstance().createCustomToken(uid);
			return successMessage(customToken);
		} catch(Exception e) {
		    return errorMessage(e);
		}

	}
	
	// uploadFile
	@PostMapping("/files")
	public ResponseEntity<Message> uploadFile(@RequestParam("file") MultipartFile file, String nameFile) throws IOException, FirebaseAuthException {
	    try {
		String userEmail = "dencoding@naver.com";
		String domain = "profile";
		String fileUrl = firebaseService.uploadFile(file, userEmail, nameFile, domain);
		return successMessage(fileUrl);
	    } catch(Exception e) {
		 return errorMessage(e);
	    }
	}
	

	@GetMapping("/files")
	public ResponseEntity<Message> viewFile(@RequestParam String fileName){
	    System.out.println(fileName+"은 들어온다.");
		String idToken = "eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL2lkZW50aXR5dG9vbGtpdC5nb29nbGVhcGlzLmNvbS9nb29nbGUuaWRlbnRpdHkuaWRlbnRpdHl0b29sa2l0LnYxLklkZW50aXR5VG9vbGtpdCIsImV4cCI6MTcyMzAwNDA5MywiaWF0IjoxNzIzMDAwNDkzLCJpc3MiOiJmaXJlYmFzZS1hZG1pbnNkay1wbW85bkBjbG91ZGlhbnMtcGhvdG8uaWFtLmdzZXJ2aWNlYWNjb3VudC5jb20iLCJzdWIiOiJmaXJlYmFzZS1hZG1pbnNkay1wbW85bkBjbG91ZGlhbnMtcGhvdG8uaWFtLmdzZXJ2aWNlYWNjb3VudC5jb20iLCJ1aWQiOiJwaWVlSjNyMDQ0aHlnTlA3bnFiVlR1N0xMazEzIn0.jGKN8ORbbfuQpgWtZcKdT4pM8Sn55YNm-G_y6XHwXNmng2SuiPgOoNQEBUEaPgIy7dwFcwQrqHIqvwobyxF3xGuL8MRWHr8_a0vlbT6hzX5LGk00_tkXVCoCOrnBEY4rkxzy8fxpfZ2TclcfrBMdCyeLgM8ttXhUY3pOrw41FCAJE4bBxVYI79KYoOcLRahHl-hmdUvEeTRC0Dl0-OAPXtmTW26xJzT8yJ-NQUVTL74UZ-d8niHHk5tfNIHmvMr_cySpmwlj-xELCPy2zVrV2KLGgya7HNSQ1hP20uYUbiSbioaOMhkuHpBAj-WWOYGBc9ZZz99kbZC6AuxluhZVmA";
	    try {
		FirebaseToken token = fireAuth.verifyIdToken(idToken);
		System.out.println("일단 토큰은 있음.");
		String url = firebaseService.getFileUrl(fileName);
		return successMessage(url);
	    } catch(Exception e) {
		 return errorMessage(e);
	    }
	}
	


}
