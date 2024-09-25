package com.cloudians.domain.publicdiary.repository.diary;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.diary.SearchCondition;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicDiaryRepository {
    void save(PublicDiary publicDiary);

    void delete(PublicDiary publicDiary);

    Optional<PublicDiary> findById(Long publicDiaryId);

    boolean existsByPersonalDiaryId(Long personalDiaryId);

    List<PublicDiary> searchByTypeAndKeywordOrderByTimestampDesc(SearchCondition condition, Long cursor, Long count);

    List<PublicDiary> publicDiariesOrderByCreatedAtDescWithTop3Diaries(Long cursor, Long count);

    Optional<PublicDiary> findByPersonalDiaryId(Long personalDiaryId);
}
