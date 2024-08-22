package com.cloudians.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.cloudians.domain.auth.entity.UserToken;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    // 사용자 이메일로 토큰을 찾는 메서드 추가
    Optional<UserToken> findByUserEmail(String userEmail);

	Optional<UserToken> findByUserEmailAndTokenType(String email, String tokenType);

	void deleteByUserEmailAndTokenType(String userEmail, String string);
}