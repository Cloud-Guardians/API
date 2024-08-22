package com.cloudians.domain.auth.controller;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudians.domain.auth.service.KakaoOAuthService;

@RequestMapping("/auth")
@RestController
public class AuthKakaoController {

	private final KakaoOAuthService kakaoOAuthService;

    public AuthKakaoController(KakaoOAuthService kakaoOAuthService) {
        this.kakaoOAuthService = kakaoOAuthService;
    }

    @GetMapping("/kakao/callback")
    public String kakaoCallback(String code) {
        // 카카오 액세스 토큰 요청
    	OAuth2User user = kakaoOAuthService.handleKakaoLogin(code);
        if (user == null) {
            System.out.println("로그인 실패: 사용자 정보를 가져오지 못했습니다."); // 오류 처리
        } 
        return "http://localhost:9090/api/auth/kakao/callback"; // 리디렉션 URL
    }
	
	
/*
    @Value("${cos.key}")
    private String cosKey;
    
    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // kakaotalk
    @GetMapping("/kakao/callback")
    public String kakaoCallback(String code) { // 데이터 리턴해 주는 컨트롤러 함수
        
        // 1단계: 카카오로 토큰 요청
        RestTemplate rt = new RestTemplate();
        
        // HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        
        // HttpBody 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "14c33822389d00f9b9603b670bdca72e");
        params.add("redirect_uri", "http://localhost:9090/api/auth/kakao/callback");
        params.add("code", code);
        
        System.out.println("요청 URI: https://kauth.kakao.com/oauth/token");
        System.out.println("요청 헤더: " + headers);
        System.out.println("요청 파라미터: " + params);
        
        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        
        // POST 방식으로 토큰 요청
        ResponseEntity<String> response = rt.exchange(
            "https://kauth.kakao.com/oauth/token",    
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );
        
        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        System.out.println("카카오 엑세스 토큰: " + oauthToken.getAccess_token());
        
        // 2단계: 사용자 정보 요청
        RestTemplate rt2 = new RestTemplate();
        
        // HttpHeader 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        
        // HttpHeader를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);
        
        // POST 방식으로 사용자 정보 요청
        ResponseEntity<String> response2 = rt2.exchange(
            "https://kapi.kakao.com/v2/user/me",    
            HttpMethod.POST,
            kakaoProfileRequest,
            String.class
        );
        
        System.out.println(response2.getBody());
        
        // JSON 파싱
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 사용자 정보 출력
        System.out.println("카카오 아이디: " + kakaoProfile.getId());
        System.out.println("카카오 이메일: " + kakaoProfile.getKakao_account().getEmail());
        
        // 회원가입 처리
        UserAuthRequest kakaoUser = UserAuthRequest.builder()
            .userEmail(kakaoProfile.getKakao_account().getEmail())
            .name("테스트")
            .status(1)
            .password("1234") // 가짜 비밀번호
            .build();
        
        Optional<UserAuthRequest> originUser = userAuthRepository.findByUserEmail(kakaoUser.getUserEmail());

        if (!originUser.isPresent()) {
            userService.joinUser(kakaoUser);
        }
        
        // 로그인 처리
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(kakaoUser.getUserEmail(), "비밀번호") // 실제 비밀번호 사용
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);        
        
        return "redirect:/"; // 리디렉션
    }
    */
	

	 
	}

	