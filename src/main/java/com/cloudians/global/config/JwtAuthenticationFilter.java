package com.cloudians.global.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cloudians.domain.auth.dto.request.PrincipalDetails;
import com.cloudians.domain.auth.dto.request.UserAuthRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// login 요청해서 username, password 전송하면 post
// UsernamePasswordAuthenticationFilter 동작함.


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/auth/login");
        this.authenticationManager = authenticationManager;
    }

    // login 요청 시 로그인 시도를 위해 실행되는 함수
    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 진입");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Method: " + request.getMethod());

        // 1. username, password 받기
        try {
//			BufferedReader br = request.getReader();
//			
//			String input = null;
//			while((input = br.readLine()) != null){
//				System.out.println(input);
//			}
            ObjectMapper om = new ObjectMapper();
            UserAuthRequest user = om.readValue(request.getInputStream(), UserAuthRequest.class);
            System.out.println("입력된 비밀번호: " + user.getPassword());
            System.out.println(user);

            // 강제 로그인
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getPassword());

            // PrincipalDetailsSerivce의 loadUserByUsername() 함수 실행된 후 정상이면 authentication 리턴됨
            // DB의 userEmail, password와 일치
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);
            // 내 로그인 정보가 authentication에 담김

            // 로그인 성공함
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUsername()); // 로그인 정상적으로 됨
            // authentication 객체가 세션 영역에 저장됨 => 로그인이 됨
            System.out.println("1---------------------");
            System.out.println("인증 성공: " + authentication.getName());
            // authentication 객체가 session 영역에 저장해야 하고 그 방법이 return 해 주면 됨
            // 리턴의 이유는 권한 관리를 security가 대신 해 주기 때문에 편하려고 하는 것 굳이 jwt 토큰 쓰며 세션 만들 필요 없음 권한 처리 때문임
            return authentication;
        } catch (Exception e) {
            System.out.println("loadUserByUsername 예외 발생: " + e.getMessage());
        }

        return null;
    }

    // attemptAuthentication 실행 인증 정상적으로 되면 successfulAuthentication 함수 실행됨
    // JWT 토큰 만들어서 request 요청한 사용자에게 JWT 토큰 response
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨: 인증 완료");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // Hash 암호 방식
        String jwtToken = JWT.create()
                .withSubject("cos token") // token name
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10))) // 10분 후 만료
                .withClaim("userEmail", principalDetails.getUser().getUserEmail()) // 사용자 이메일 클레임
                .sign(Algorithm.HMAC512("cos")); // 비밀 키 사용

        System.out.println("Generated JWT Token: " + jwtToken);

        response.addHeader("Authorization", "Bearer " + jwtToken); // 응답 헤더에 JWT 추가
        // super.successfulAuthentication(request, response, chain, authResult);
    }


}