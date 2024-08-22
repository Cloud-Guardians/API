package com.cloudians.domain.auth.controller;

import java.util.Map;

import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudians.domain.auth.dto.request.LoginRequest;
import com.cloudians.domain.auth.dto.request.UserAuthRequest;
import com.cloudians.domain.auth.dto.response.LoginResponse;
import com.cloudians.domain.auth.repository.UserAuthRepository;
import com.cloudians.domain.auth.service.AuthTokenService;
import com.cloudians.domain.auth.service.PrincipalDetails;

@RestController
@RequestMapping("/auth")
public class RestApiController {
	
	@Autowired
    private AuthTokenService authTokenService;
	
	@Autowired
	private UserAuthRepository userAuthRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	 
	@PostMapping("signup")
	public String join(@RequestBody UserAuthRequest user) {
		try {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setStatus(1);
			userAuthRepository.save(user);
			return "회원가입 완료";
		} catch (Exception e) {
			e.printStackTrace(); // 예외 발생 시 로그 출력
			return "회원가입 실패: " + e.getMessage();
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> generalLogin(@RequestBody LoginRequest loginRequest) {
	    try {
	        Map<String, String> tokens = authTokenService.login(loginRequest.getEmail(), loginRequest.getPassword());
	        
	        // 성공적으로 로그인한 경우 토큰을 포함한 응답 반환
	        LoginResponse loginResponse = new LoginResponse(tokens.get("accessToken"), tokens.get("refreshToken"));
	        return ResponseEntity.ok(loginResponse);
	    } catch (InvalidCredentialsException e) {
	        // 예외 발생 시 401 Unauthorized 응답 반환
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 오류 메시지 없이 상태 코드만 반환
	    }
	}

	
	@GetMapping("login/google")
	public String testOAuthLogin(Authentication authentication,
	        @AuthenticationPrincipal OAuth2User oauth) { // DI(의존성 주입)
	    System.out.println("/api/auth/login/google ============");
	    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
	    
	    String oauthEmail = principalDetails.getOAuthEmail(); // OAuth2 이메일

	    UserAuthRequest userEntity = userAuthRepository.findByUserEmail(oauthEmail).orElse(null);
	    if (userEntity == null) {
	        // DB에 사용자 저장
	        userEntity = UserAuthRequest.builder()
	                .userEmail(oauthEmail)
	                .status(1) // 기본 상태
	                .build();
	        userAuthRepository.save(userEntity);
	        System.out.println("새로운 사용자를 DB에 저장했습니다.");
	    } else {
	        System.out.println("기존 사용자가 DB에 존재합니다.");
	    }
	    System.out.println("oauth2User: " + oauth.getAttributes());
	    return "redirect:/home"; // templates/signup.html로 이동
	}
	
	@GetMapping("login/google/redirect")
	public String OauthLoginSuccess(Authentication authentication) {
	    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		
		String userEmail = principalDetails.getUser().getUserEmail();
	    
	    return "redirect:/home";
	}
}
