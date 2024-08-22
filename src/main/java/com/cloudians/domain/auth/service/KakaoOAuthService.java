package com.cloudians.domain.auth.service;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cloudians.domain.auth.dto.request.OAuthToken;
import com.cloudians.domain.auth.dto.request.UserAuthRequest;
import com.cloudians.domain.auth.repository.UserAuthRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class KakaoOAuthService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Autowired
    private AuthTokenService authTokenService;
    
    public OAuth2User handleKakaoLogin(String code) {
        String accessToken = requestAccessToken(code);
        if (accessToken == null) {
            System.err.println("Access Token을 가져오지 못했습니다."); // 로그 추가
            return null; // Access Token이 없으면 null 반환
        }

        OAuth2User oauth2User = fetchKakaoUserInfo(accessToken);
        if (oauth2User == null) {
            System.err.println("사용자 정보를 가져오지 못했습니다."); // 로그 추가
            return null; // 사용자 정보가 없으면 null 반환
        }

        // DB에서 사용자 검색
        String userEmail = oauth2User.getAttribute("email");
        if (userEmail == null) {
            System.err.println("사용자 이메일이 null입니다."); // 로그 추가
            return null; // 이메일이 없으면 null 반환
        }

        UserAuthRequest userEntity = userAuthRepository.findByUserEmail(userEmail).orElse(null);
        if (userEntity == null) {
            // 새로운 사용자 등록
            userEntity = UserAuthRequest.builder()
                    .userEmail(userEmail)
                    .password(bCryptPasswordEncoder.encode("1234")) // 기본 비밀번호 해시화
                    .status(1)
                    .build();
            userAuthRepository.save(userEntity);
        }

        // JWT 토큰 발급
        String jwtToken = generateJwtToken(userEntity);
        
        String refreshToken = authTokenService.generateRefreshToken(userEntity.getUserEmail());
        
        authTokenService.saveRefreshToken(userEntity.getUserEmail(), refreshToken);

        
        // JWT 토큰을 응답 헤더에 추가
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.addHeader("Authorization", "Bearer " + jwtToken);
        }

        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }

    private String requestAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "14c33822389d00f9b9603b670bdca72e");
        params.add("redirect_uri", "http://localhost:9090/api/auth/kakao/callback");
        params.add("code", code);
        params.add("scope", "account_email");

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        
        ResponseEntity<String> response;
        try {
            response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // JSON 파싱
                ObjectMapper objectMapper = new ObjectMapper();
                OAuthToken oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
                System.out.println("사용자 액세스 토큰: "+oauthToken);
                return oauthToken.getAccess_token(); // 토큰 반환
                
            } else {
                // 실패한 경우 로그 출력
                System.err.println("Kakao API 호출 실패: " + response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            // 예외 발생 시 로그 출력
            e.printStackTrace();
            return null; // null 반환
        }
    }


    private OAuth2User fetchKakaoUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me", 
                HttpMethod.GET, 
                entity, 
                String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> userInfo = objectMapper.readValue(responseBody, Map.class); // Map으로 변환

                // 사용자 정보에서 이메일 추출
                String email = (String) ((Map) userInfo.get("kakao_account")).get("email");
                userInfo.put("email", email); // Map에 이메일 추가

                return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), userInfo, "id");
            } else {
                System.err.println("Kakao API 호출 실패: " + response.getStatusCode());
                return null;
            }
        } catch (HttpClientErrorException e) {
            System.err.println("Kakao API 호출 시 오류 발생: " + e.getStatusCode());
            System.err.println("응답 본문: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private String generateJwtToken(UserAuthRequest userEntity) {
        String jwtToken = JWT.create()
                .withSubject("jwt token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10))) // 10분 후 만료
                .withClaim("userEmail", userEntity.getUserEmail())
                .sign(Algorithm.HMAC512("jwt"));
        
        System.out.println("생성된 JWT 토큰: " + jwtToken); // JWT 토큰 출력
        
        return jwtToken;


    }
}
