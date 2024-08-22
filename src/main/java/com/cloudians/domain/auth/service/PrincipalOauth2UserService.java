package com.cloudians.domain.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cloudians.domain.auth.dto.request.PrincipalDetails;
import com.cloudians.domain.auth.dto.request.UserAuthRequest;
import com.cloudians.domain.auth.repository.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String userEmail = oauth2User.getAttribute("email");

        // DB에서 사용자 검색
        UserAuthRequest userEntity = userAuthRepository.findByUserEmail(userEmail).orElse(null);

        System.out.println("Retrieved user email: " + userEmail);

        System.out.println("Attempting to log in via OAuth2 for user: " + userEmail);


        if (userEntity == null) {
            // 새로운 사용자 등록 (비밀번호 필드 없음)
            userEntity = UserAuthRequest.builder()
                    .userEmail(userEmail)
                    .status(1)
                    .build();
            userAuthRepository.save(userEntity);
        }

        // JWT 토큰 발급
        String jwtToken = JWT.create()
                .withSubject("cos token") // 토큰 이름
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10))) // 10분 후 만료
                .withClaim("userEmail", userEntity.getUserEmail()) // 사용자 이메일 클레임
                .sign(Algorithm.HMAC512("cos")); // 비밀 키 사용

        System.out.println("JWT Token: " + jwtToken);

        // JWT 토큰을 응답 헤더에 추가
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (response != null) {
            response.addHeader("Authorization", "Bearer " + jwtToken);
        }
        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}