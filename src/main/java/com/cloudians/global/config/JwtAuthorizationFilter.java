package com.cloudians.global.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cloudians.domain.auth.dto.request.PrincipalDetails;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

// 시큐리티가 filter 가지고 있는데 그중 BasicAuthenticationFilter가 있음
// 권한이나 인증 필요한 특정 주소 요청 시 위 필터를 무조건 타게 돼 있음
// 권한 및 인증 필요한 주소가 아니면 이 필터 안 탐
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository; // 필드 초기화
    }

    // 인증이나 권한 필요한 주소 요청 시 해달 필터 타게 됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소 요청");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader: " + jwtHeader);


        // JWT 토큰 검증해 정상적인 사용자인지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰 검증해 확인
        String jwtToken = request.getHeader("Authorization").replace("Bearer", "").trim();
        String userEmail = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("userEmail").asString();

        // 정상 작동 시
        if (userEmail != null) {
            System.out.println("username 정상 ");
            Optional<User> optionalUserAuthRequest = userRepository.findByUserEmail(userEmail);

            User user = optionalUserAuthRequest.orElse(null); // 값이 없으면 null 반환

            System.out.println("userEntity: " + user);
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            System.out.println("자고 시포요 " + principalDetails.getUsername());
            // JWT 토큰 서명을 통해서 서명이 정상이면 authentication 객체 생성
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근해 authentication 객체 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }


}