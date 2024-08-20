package com.cloudians.domain.auth.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cloudians.domain.auth.dto.request.UserAuthRequest;

import lombok.Data;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인 진행
// 로그인 진행이 완료되면 시큐리티 세션을 만들어 준다 (Security ContextHolder)
// 오브젝트 => Authentication 타입 객체 
// Authentication 안에 User 정보가 있어야 함
// USer오브젝트 타입 => UserDetails 타입 객체


//Security Session에 저장되는 객체가 Authentication => 객체 안에 유저 정보 저장될 때 UserDetails(PrincipalDetails)

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private UserAuthRequest user; // 컴포지션(has-a)
	private Map<String, Object> attributes;
	
	
	// 일반 로그인 
	public PrincipalDetails(UserAuthRequest user) {
		this.user = user;
	}
	
//	public PrincipalDetails(String username, String password) {
//		Securityuser user = new Securityuser();
//		user.setUsername(username);
//		user.setPassword(password);
//		PrincipalDetails prin = new PrincipalDetails(user);
//		
//		
//	}
	//OAuth 로그인
	public PrincipalDetails(UserAuthRequest user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}
	
	// 해당 유저의 권한을 리턴하는 곳
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    Collection<GrantedAuthority> authorities = new ArrayList<>();

	    // status에 따른 권한 설정
	    switch (user.getStatus()) {
	        case 1:
	            authorities.add(new SimpleGrantedAuthority("USER")); // "ROLE_" 접두사 없이 USER 추가
	            break;
	        case 2:
	            authorities.add(new SimpleGrantedAuthority("ADMIN")); // "ROLE_" 접두사 없이 ADMIN 추가
	            break;
	        default:
	            authorities.add(new SimpleGrantedAuthority("USER")); // 기본값
	            break;
	    }

	    return authorities; // 수정: role 변수를 사용할 필요 없음
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		// 우리 사이트 일 년 사용 안 하면 휴면 계정 처리
		// user.getLoginDate();
		// 현재 시간 - 로그인 시간 => 일 년 초과 시 return false;
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
		// sub는 곧 id
	}

	
}
