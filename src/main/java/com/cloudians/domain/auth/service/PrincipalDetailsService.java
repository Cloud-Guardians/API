package com.cloudians.domain.auth.service;

import com.cloudians.domain.auth.dto.request.PrincipalDetails;
import com.cloudians.domain.auth.dto.request.UserAuthRequest;
import com.cloudians.domain.auth.repository.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login")
// login 요청이 오면 자동으로 UesrDetailsService 타입으로 IoC로 되어 있는 loadUserByUsername 함수가 실행

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserAuthRepository UserAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService의 loadIserByUsername()");
        UserAuthRequest userEntity = UserAuthRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + userEmail));
        if (userEntity.getStatus() != 1) { // 1이 활성 상태라면
            throw new DisabledException("사용자가 비활성화되었습니다.");
        }
        System.out.println("userEntity: " + userEntity);
        System.out.println("저장된 비밀번호: " + userEntity.getPassword());
        // UserAuthRequest 객체를 기반으로 PrincipalDetails 객체 생성
        return new PrincipalDetails(userEntity);
    }
}