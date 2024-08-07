package com.cloudians.global.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	    Message errorMessage = new Message(e, HttpStatus.BAD_REQUEST.value());
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
	
	@GetMapping("/token")
	public ResponseEntity<Message> tokenTest() throws FirebaseAuthException{
		String uid = "pieeJ3r044hygNP7nqbVTu7LLk13";
		try {
			String customToken = FirebaseAuth.getInstance().createCustomToken(uid);
			return successMessage(customToken);
		} catch(Exception e) {
		    return errorMessage(e);
		}

	}
	
	@PostMapping("/files")
	public ResponseEntity<Message> uploadFile(@RequestParam("file") MultipartFile file, String nameFile) throws IOException, FirebaseAuthException {
	    try {
		String fileUrl = firebaseService.uploadFiles(file, nameFile);
		return successMessage(fileUrl);
	    } catch(Exception e) {
		 return errorMessage(e);
	    }
	}

	@GetMapping("/files")
	public ResponseEntity<Message> viewFile(String fileName) throws IOException, FirebaseAuthException {
	    try {
		System.out.println(fileName+"은 들어온다.");
		String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFkYmUwNmI1ZDdjMmE3YzA0NDU2MzA2MWZmMGZlYTM3NzQwYjg2YmMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vY2xvdWRpYW5zLXBob3RvIiwiYXVkIjoiY2xvdWRpYW5zLXBob3RvIiwiYXV0aF90aW1lIjoxNzIyNjAyMzM5LCJ1c2VyX2lkIjoiYzUxZTQ2NjIxNjhhN2EwMDZiZjYwODJkY2Q3YTE2YmE1ZmYzZmIwYiIsInN1YiI6ImM1MWU0NjYyMTY4YTdhMDA2YmY2MDgyZGNkN2ExNmJhNWZmM2ZiMGIiLCJpYXQiOjE3MjI2MDIzMzksImV4cCI6MTcyMjYwNTkzOSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6e30sInNpZ25faW5fcHJvdmlkZXIiOiJjdXN0b20ifX0.EaAFl4zWo9IMZB2oPMKBt6CizouHrpQRAH8ur7W_-5y8gqauFiKPAw8wWUz_xuE2z_HA8Sbo-xI7HCvYZWxmtcgzqNwInmpoDs7yYHdcHodewARC2EwysQxQ79rplyQS3lcF8emsIf9RrcHXLLlVA2Vv5ghTOvXV64AcITgROTrZuhsXEYDDtRhFflLUB2Bx7YHRWWguiVgwlX9N20MZJgpNFDHMM1GhQrL75z3tA0YjBE41-D9n5M46mPmag9Tg01lfI3T9vEZReMz6_m1I7pQavqGa4IYDYPv9TZcegbcKC7H2ShMAd_agNhdwF1Qun-qqeeFKony0rwnuew5z1g";
		FirebaseToken token = fireAuth.verifyIdToken(idToken);
		System.out.println("일단 토큰은 있음.");
		String url = firebaseService.getFileUrl(fileName);
		return successMessage(url);
	    } catch(Exception e) {
		 return errorMessage(e);
	    }
	}
	


}
