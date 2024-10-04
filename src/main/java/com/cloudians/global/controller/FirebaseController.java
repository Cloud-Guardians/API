package com.cloudians.global.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.cloudians.domain.user.dto.response.UserResponse;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.service.FcmNotificationService;
import com.cloudians.domain.user.service.UserService;
import com.cloudians.global.Message;
import com.cloudians.global.service.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/global")
public class FirebaseController {

    private static final String FIREBASE_API_URL = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=AIzaSyCRa6O8ERHxL_9CmWJeJyUKcxMgDxH65-A";
	private final FirebaseService firebaseService;


	public ResponseEntity<Message> errorMessage (Exception e){
	    System.out.println(e);

	    Message errorMessage = new Message(e.toString(), HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.OK).body(errorMessage);
	}

	public ResponseEntity<Message> successMessage (Object object){
	    Message message = new Message(object,null,HttpStatus.OK.value());
	    return ResponseEntity.status(HttpStatus.OK).body(message);
	}



	// id Token
	@GetMapping("/token")
	public ResponseEntity<Message> tokenTest() throws FirebaseAuthException{
		String uid = "c51e4662168a7a006bf6082dcd7a16ba5ff3fb0b";

		try {
			String customToken = FirebaseAuth.getInstance().createCustomToken(uid);
		Map<String, Object> firebaseRequest = new HashMap<>();
		firebaseRequest.put("token",customToken);
		firebaseRequest.put("returnSecureToken",true);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Map<String,Object>> entity = new HttpEntity<>(firebaseRequest,headers);

		ResponseEntity<Map> response = restTemplate.exchange(
			FIREBASE_API_URL,
			HttpMethod.POST,
			entity,
			Map.class);

		Map<String,String> responseBody = new HashMap<>();
		if(response.getBody() != null) {
		    String idToken = (String) response.getBody().get("idToken");
		    responseBody.put("idToken",idToken);
		    return successMessage(idToken);
		}
		return successMessage("none");
		} catch(Exception e) {
		    return errorMessage(e);
		}

	}

	@DeleteMapping("/delete")
	public ResponseEntity<Message> deleteFile(@RequestParam String fileName) throws Exception{
	    System.out.println("Start");
	    firebaseService.deleteFileUrl("dencodin@naver.com","profile",fileName);
	    String message = "done";
		   return successMessage(message);
	}

	@GetMapping("/get")
	public ResponseEntity<Message> getFile(@RequestParam String fileName) throws Exception {
	    System.out.println("Start");
	    String userEmail = "dencoding@naver.com";
	    String message=  firebaseService.getFileUrl(userEmail,"profile",fileName);
		   return successMessage(message);
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
	        try {
		System.out.println("일단 토큰은 있음.");
		 String url = firebaseService.getFileUrl("dencoding","profile",fileName);
		URL url2 = new URL(url);
		return successMessage(url.toString());
	    } catch(Exception e) {

		 return errorMessage(e);
	    }

	}





}
