package com.cloudians.global.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudians.global.service.FirebaseService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@RestController
@RequestMapping("/api/users")
public class FirebaseController {
	
	@Autowired
	private FirebaseService firebaseService;
	
	@GetMapping("/file")
	public String aaa() throws FirebaseAuthException {
	    
	    FirebaseAuth mAuth = FirebaseAuth.getInstance();
	    String uid = "c51e4662168a7a006bf6082dcd7a16ba5ff3fb0b";
	  return FirebaseAuth.getInstance().createCustomToken(uid);
	}
	@GetMapping("/token")
	public String tokenTest() throws FirebaseAuthException{
		FirebaseAuth mAuth = FirebaseAuth.getInstance();
		String uid = "pieeJ3r044hygNP7nqbVTu7LLk13";
		try {
			String customToken = FirebaseAuth.getInstance().createCustomToken(uid);
			
		} catch(Exception e) {
			return e.toString();
		}
		return null;
	}
	@PostMapping("/files")
	public String uploadFile(@RequestParam("file") MultipartFile file, String nameFile) throws IOException, FirebaseAuthException {
		if(file.isEmpty()) return "is empty";
		return firebaseService.uploadFiles(file, nameFile);
	}
//	@GetMapping("/files")
//	public String getFileUrl(String fileName)throws Exception {
//		
//	}
	@GetMapping("/files")
	public String viewFile(String fileName) throws IOException, FirebaseAuthException {
		System.out.println(fileName+"은 들어온다.");
		FirebaseAuth auth = FirebaseAuth.getInstance();
		String idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjFkYmUwNmI1ZDdjMmE3YzA0NDU2MzA2MWZmMGZlYTM3NzQwYjg2YmMiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vY2xvdWRpYW5zLXBob3RvIiwiYXVkIjoiY2xvdWRpYW5zLXBob3RvIiwiYXV0aF90aW1lIjoxNzIyNjAyMzM5LCJ1c2VyX2lkIjoiYzUxZTQ2NjIxNjhhN2EwMDZiZjYwODJkY2Q3YTE2YmE1ZmYzZmIwYiIsInN1YiI6ImM1MWU0NjYyMTY4YTdhMDA2YmY2MDgyZGNkN2ExNmJhNWZmM2ZiMGIiLCJpYXQiOjE3MjI2MDIzMzksImV4cCI6MTcyMjYwNTkzOSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6e30sInNpZ25faW5fcHJvdmlkZXIiOiJjdXN0b20ifX0.EaAFl4zWo9IMZB2oPMKBt6CizouHrpQRAH8ur7W_-5y8gqauFiKPAw8wWUz_xuE2z_HA8Sbo-xI7HCvYZWxmtcgzqNwInmpoDs7yYHdcHodewARC2EwysQxQ79rplyQS3lcF8emsIf9RrcHXLLlVA2Vv5ghTOvXV64AcITgROTrZuhsXEYDDtRhFflLUB2Bx7YHRWWguiVgwlX9N20MZJgpNFDHMM1GhQrL75z3tA0YjBE41-D9n5M46mPmag9Tg01lfI3T9vEZReMz6_m1I7pQavqGa4IYDYPv9TZcegbcKC7H2ShMAd_agNhdwF1Qun-qqeeFKony0rwnuew5z1g";
		FirebaseToken token = auth.verifyIdToken(idToken);
		if(token != null) {
			System.out.println("일단 토큰은 있음.");
			String url = firebaseService.getFileUrl(fileName);
            return url;
		}
		return token.getClaims().toString();
	}
	


}
