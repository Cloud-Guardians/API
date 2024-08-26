package com.cloudians.domain.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.repository.UserRepository;

@Service
public class UserAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional(readOnly = true)
    public User findUserByUsername(String userEmail) {
        // Optional을 반환하는 메서드에 대해 orElseThrow 사용
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("회원 찾기 실패"));
    }

    @Transactional
    public boolean registerUser(User user) {
        // 이메일로 사용자 찾기
        User existingUser = userRepository.findByUserEmail(user.getUserEmail()).orElse(null);

        // 이미 존재하는 사용자라면 회원가입 실패
        if (existingUser != null) {
            return false; // 사용자 이미 존재
        }

        // 비밀번호 해싱
        String rawPassword = user.getPassword(); // 원문 비밀번호
        String encPassword = encoder.encode(rawPassword); // 해시된 비밀번호
        user.setPassword(encPassword);

        // 역할 설정
        user.setStatus(1);

        try {
            userRepository.save(user);
            return true; // 성공적으로 저장됨
        } catch (Exception e) {
            // 로그를 남기거나 예외 처리를 할 수 있음
            return false; // 회원가입 실패
        }
    }

    @Transactional
    public int joinUser(User user) {
        String rawPassword = user.getPassword(); // 1234 원문
        String encPassword = encoder.encode(rawPassword); // 해쉬
        user.setPassword(encPassword);
        user.setStatus(1);
        try {
            userRepository.save(user);
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    @Transactional
    public void updateUser(User user) {
        // 수정시에는 영속성 컨텍스트 User 오브젝트를 영속화시키고, 영속화된 User 오브젝트를 수정
        // select를 해서 User 오브젝트를 DB로부터 가져오는 이유는 영속화를 하기 위해서!!
        // 영속화된 오브젝트를 변경하면 자동으로 DB에 update문을 날려주거든요.
        User persistence = userRepository.findByUserEmail(user.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원 찾기 실패"));

        // Validate 체크 => oauth 필드에 값이 없으면 수정 가능
        if (persistence.getSignupType() == 0) {  // 예를 들어, 0이 OAuth가 설정되지 않았음을 의미한다고 가정
            String rawPassword = user.getPassword();
            String encPassword = encoder.encode(rawPassword);
            persistence.setPassword(encPassword);
            persistence.setNickname(user.getNickname());
        }

        // 회원수정 함수 종료시 = 서비스 종료 = 트랜잭션 종료 = commit 이 자동으로 됩니다.
        // 영속화된 persistence 객체의 변화가 감지되면 더티체킹이 되어 update문을 날려줍니다.
    }
}