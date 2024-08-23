//package com.cloudians.domain.auth.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.cloudians.domain.auth.dto.request.UserAuthRequest;
//
//// CRUD 함수 JpaRepository가 들고 있음
//// @Repository 어노테이션 없어도 IoC 가능 < JpaRepository 상속했기 때문에 자동 빈 등록
//public interface UserAuthRepository extends JpaRepository<UserAuthRequest, String> {
//
//    Optional<UserAuthRequest> findByUserEmail(String userEmail);
//	}