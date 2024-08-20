package com.cloudians.domain.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudians.domain.auth.dto.request.UserAuthRequest;
import com.cloudians.domain.auth.repository.UserAuthRepository;
import com.cloudians.domain.auth.service.PrincipalDetails;
import com.cloudians.domain.auth.service.PrincipalDetailsService;
import com.cloudians.domain.auth.service.UserService;

@Controller // View return
public class AuthController {

	
	@Autowired
	private UserAuthRepository UserAuthRepository;

	@Autowired
	private PrincipalDetailsService principalDetailsService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/test/login2")
	public @ResponseBody String testLogin2(@RequestParam String userEmail, @RequestParam String password) {// DI(의존성 주입)
		UserDetails user = principalDetailsService.loadUserByUsername(userEmail);
//		PrincipalDetails userDetails = new PrincipalDetails(username, password);
		PrincipalDetails userDetails = (PrincipalDetails) user;
		// PrincipalDetails를 UserDetails implements 했기 때문에 Principal로 받을 수 있음 (같은 타입)
		System.out.println("/test/login ============");
//		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//		System.out.println("authentication: "+principalDetails.getUser());

		System.out.println("userDetails: " + userDetails.getUser());
		return "세션 정보 확인하기 "+ userDetails.getUser();
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin(Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth) { // DI(의존성 주입)
		System.out.println("/test/ouath/login ============");
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		
		// 일반 로그인 시 authentication에
		System.out.println("authentication: " + oauth2User.getAttributes());
		System.out.println("oauth2User: " + oauth.getAttributes());
		return "OAuth 세션 정보 확인하기";
	}

	// 객체 및 어노테이션 사용이 가능하다

	// OAuth 로그인을 해도 PrincipalDetails
	// 일반 로그인을 해도 PrincipalDetails
	@GetMapping("/user")
	@ResponseBody
	public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		if (principalDetails == null) {
	        return "No user details available";
	    }
		System.out.println("principalDetails: " + principalDetails.getUser());
		return "user";
	}

	/* @GetMapping("/admin")
	@ResponseBody
	public String admin() {
		return "admin";
	} */

	// 스프링 시큐리티 해당 주소로 감 - SecurityConfig 파일 생성 후 작동 x
	
	@GetMapping("/loginForm")
	@ResponseBody
	public String loginForm() {
		return "회원가입이 완료되었음 ";
	}
	
	@PostMapping("/signup")
	public String join(UserAuthRequest user) {
		System.out.println(user);
		user.setStatus(1);
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		UserAuthRepository.save(user); // 회원가입 잘됨 비밀번호: 1234 = 시큐리티로 로그인 불가함 이유는 패스워드 암호화가 안 돼
		return "redirect:/loginForm";
	}


	
}
