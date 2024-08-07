package com.cloudians.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudians.domain.user.entity.UserLock;


public interface UserLockRepository extends JpaRepository<UserLock, Long> {
    
    Optional<UserLock> findByUserEmail(String userEmail);

}
