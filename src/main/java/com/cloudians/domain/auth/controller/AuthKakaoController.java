package com.cloudians.domain.auth.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cloudians.domain.auth.dto.request.KakaoProfile;
import com.cloudians.domain.auth.dto.request.OAuthToken;
import com.cloudians.domain.auth.dto.request.UserAuthRequest;
import com.cloudians.domain.auth.repository.UserAuthRepository;
import com.cloudians.domain.auth.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthKakaoController {

	@Value("${cos.key}")
	private String cosKey;
	
	@Autowired
	private UserAuthRepository UserAuthRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;
	

	// kakaotalk
		@GetMapping("/auth/kakao/callback")
		@ResponseBody
		public String kakaoCallback(String code) {// 데이터 리턴해 주는 컨트롤러 함수
			// POST 방식으로 key=value 데이터 요청 (카카오로)
			// HttpsURLConnection url, Retrofit2, OkHttp, RestTemplate
			
			
			RestTemplate rt = new RestTemplate();
			
			// HttpHeader 오브젝트 생성
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			
			// HttpBody 오브젝트 생성
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("grant_type", "authorization_code");
			params.add("client_id", "14c33822389d00f9b9603b670bdca72e");
			params.add("redirect_uri", "http://localhost:9090/auth/kakao/callback");
			params.add("code", code);
			
			// HttpHeader, HttpBody를 하나의 오브젝트에 담기
			HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
					new HttpEntity<>(params, headers);
			
			// Http 요청 Post 방식, Response 변수의 응답 받음 
			ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",	
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
					);
			
			// Gson, Json Simple, ObjectMapper
			ObjectMapper objectMapper = new ObjectMapper();
			OAuthToken oauthToken = null;
			try {
				oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			}catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			System.out.println("카카오 엑세스 토큰: "+oauthToken.getAccess_token());
			
			RestTemplate rt2 = new RestTemplate();
			
			// HttpHeader 오브젝트 생성
			HttpHeaders headers2 = new HttpHeaders();
			headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token());
			headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			
			// HttpHeader, HttpBody를 하나의 오브젝트에 담기
			HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
					new HttpEntity<>(headers2);
			
			// Http 요청 Post 방식, Response 변수의 응답 받음 
			ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",	
				HttpMethod.POST,
				kakaoProfileRequest,
				String.class
					);
			
			System.out.println(response2.getBody());
			
			ObjectMapper objectMapper2 = new ObjectMapper();
			KakaoProfile kakaoProfile = null;
			try {
				kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			}catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			//User 오브젝트: username, password, email
			System.out.println("카카오 아이디: "+kakaoProfile.getId());
			System.out.println("카카오 이메일: "+kakaoProfile.getKakao_account().getEmail());
			
			
			System.out.println("카카오 유저 네임: "+kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
//			UUID garbagePassword = UUID.randomUUID();
//			System.out.println("패스워드: "+garbagePassword);
			UserAuthRequest kakaoUser = UserAuthRequest.builder()
			.userEmail(kakaoProfile.getKakao_account().getEmail())
			.name("테스트")
			.status(1)
			.password("1234")
			.build();
			
			Optional<UserAuthRequest> originUser = UserAuthRepository.findByUserEmail(kakaoUser.getUserEmail());
			
			if(originUser == null) {
				userService.joinUser(kakaoUser);
			}
			
			// 로그인 처리
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUserEmail(), cosKey));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			return "redirect:/";
		}
	
}
