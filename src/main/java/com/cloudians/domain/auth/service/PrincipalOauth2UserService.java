package com.cloudians.domain.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cloudians.domain.auth.dto.request.UserAuthRequest;
import com.cloudians.domain.auth.repository.UserAuthRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private UserAuthRepository userAuthRepository; // 인스턴스 주입

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
        System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());

        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("getAttributes: " + oauth2User.getAttributes());

        String userEmail = oauth2User.getAttribute("email");
        String password = bCryptPasswordEncoder.encode("1234"); // 하드코딩된 패스워드는 보안에 주의
        String name = oauth2User.getAttribute("name");
        
        // userAuthRepository 인스턴스를 사용하여 메서드 호출
        UserAuthRequest userEntity = userAuthRepository.findByUserEmail(userEmail).orElse(null);

        if (userEntity == null) {
            System.out.println("구글 로그인이 최초입니다.");
            userEntity = UserAuthRequest.builder()
                    .userEmail(userEmail)
                    .password(password)
                    .name(name)
                    .status(1)
                    .build();
            userAuthRepository.save(userEntity); // 인스턴스를 통해 저장
        } else {
            System.out.println("구글 로그인을 한 적이 있습니다.");
        }

        // PrincipalDetails는 정의되어 있어야 함
        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}

