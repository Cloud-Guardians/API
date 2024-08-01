package com.cloudians.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudians.user.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	
	// 내 정보 조회
	Optional<User> findByUserEmail(String userEmail);


}
