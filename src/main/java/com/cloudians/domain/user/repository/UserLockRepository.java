package com.cloudians.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.entity.UserLock;


public interface UserLockRepository extends JpaRepository<UserLock, Long> {
    
    boolean existsByUser(User user);
    Optional<UserLock> findByUser(User user);
    Optional<UserLock> findByUserAndPasscode(User user, String passcode);

}
