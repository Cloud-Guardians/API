package com.cloudians.domain.publicdiary.repository;

import com.cloudians.domain.publicdiary.entity.PublicDiary;
import com.cloudians.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublicDiaryRepository extends JpaRepository<PublicDiary, Long> {
    boolean existsByPersonalDiaryId(Long personalDiaryId);

    Optional<PublicDiary> findByIdAndUser(Long publicDiaryId, User user);
}
