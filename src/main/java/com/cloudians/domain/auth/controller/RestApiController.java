package com.cloudians.domain.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cloudians.domain.auth.dto.request.UserAuthRequest;
import com.cloudians.domain.auth.repository.UserAuthRepository;
import com.cloudians.domain.auth.service.PrincipalDetails;

@RestController
public class RestApiController {

	@Autowired
	private UserAuthRepository userAuthRepository;

	private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	
	@PostMapping("token")
	public String token() {
		return "<h1>home</h1>";
	}

	@GetMapping("/admin")
	public String admin() {
		return "admin";
		
	}
	
	@GetMapping("/users")
	@ResponseBody
	public String users(Authentication authentication) {
		PrincipalDetails prin = (PrincipalDetails) authentication.getPrincipal();
		System.out.println(authentication);
		System.out.println(prin.getUsername());
		return "users";
		
	}
	
	
	@PostMapping("join")
	public String join(@RequestBody UserAuthRequest user) {
		try {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setStatus(1);
			userAuthRepository.save(user);
			return "회원가입완료";
		} catch (Exception e) {
			e.printStackTrace(); // 예외 발생 시 로그 출력
			return "회원가입 실패: " + e.getMessage();
		}
	}

}
