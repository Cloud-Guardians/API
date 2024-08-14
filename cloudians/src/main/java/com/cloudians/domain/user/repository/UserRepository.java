package com.cloudians.domain.user.repository;

import java.util.Optional;

import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
	
	// 내 정보 조회
	Optional<User> findByUserEmail(String userEmail);
	


}
