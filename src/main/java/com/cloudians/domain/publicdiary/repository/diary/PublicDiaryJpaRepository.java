package com.cloudians.domain.publicdiary.repository.diary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.user.entity.User;

public interface PublicDiaryJpaRepository extends JpaRepository<PublicDiary, Long> {
    boolean existsByPersonalDiaryId(Long personalDiaryId);
    Optional<PublicDiary> findByIdAndAuthor(Long publicDiaryId, User author);
    List<PublicDiary> findListByAuthor(User author);
}