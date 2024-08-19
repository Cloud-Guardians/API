package com.cloudians.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cloudians.domain.user.entity.UserToken;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken,Long> {
    Optional<List<UserToken>> findByUserEmail(String userEmail);
}
