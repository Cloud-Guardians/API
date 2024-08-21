package com.cloudians.domain.publicdiary.repository;

import com.cloudians.domain.publicdiary.entity.PublicDiary;
import com.cloudians.domain.publicdiary.entity.SearchCondition;
import com.cloudians.domain.publicdiary.entity.SearchType;
import com.cloudians.domain.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cloudians.domain.home.entity.QWhisperMessage.whisperMessage;
import static com.cloudians.domain.publicdiary.entity.QPublicDiary.publicDiary;

@Repository
@RequiredArgsConstructor
public class PublicDiaryRepositoryImpl {
    private final PublicDiaryJpaRepository publicDiaryJpaRepository;
    private final JPAQueryFactory q;

    public static int TOP_DIARIES_SIZE = 3;

    public void save(PublicDiary publicDiary) {
        publicDiaryJpaRepository.save(publicDiary);
    }

    public void delete(PublicDiary publicDiary) {
        publicDiaryJpaRepository.delete(publicDiary);
    }

    public boolean existsByPersonalDiaryId(Long personalDiaryId) {
        return publicDiaryJpaRepository.existsByPersonalDiaryId(personalDiaryId);
    }

    public Optional<PublicDiary> findByIdAndUser(Long publicDiaryId, User user) {
        return publicDiaryJpaRepository.findByIdAndUser(publicDiaryId, user);
    }

    public List<PublicDiary> searchByTypeAndKeywordOrderByTimestampDesc(SearchCondition condition, Long cursor, Long count) {
        return q.selectFrom(publicDiary)
                .where(isSearchable(condition.getType(), condition.getContent())
                        .and(getLt(cursor)))
                .limit(count + 1)
                .orderBy(publicDiary.createdAt.desc())
                .fetch();
    }

    public List<PublicDiary> findPublicDiariesOrderByCreatedAtDescWithTop3Diaries(Long cursor, Long count) {
        List<PublicDiary> top3Diaries = findTop3DiariesByLikes(cursor);
        List<PublicDiary> otherDiaries = getLeftPublicDiariesOrderByDesc(cursor, count, top3Diaries);

        List<PublicDiary> result = new ArrayList<>(top3Diaries);
        result.addAll(otherDiaries);

        return result;
    }

    private List<PublicDiary> getLeftPublicDiariesOrderByDesc(Long cursor, Long count, List<PublicDiary> top3Diaries) {
        if (top3Diaries.size() == TOP_DIARIES_SIZE) {
            List<Long> top3DiaryIds = top3Diaries.stream()
                    .map(PublicDiary::getId)
                    .collect(Collectors.toList());

            return q.selectFrom(publicDiary)
                    .where(getLt(cursor)
                            .and(publicDiary.id.notIn(top3DiaryIds)))
                    .orderBy(publicDiary.createdAt.desc())
                    .limit(count - 2)  // top3Diaries가 3개이므로 (count + 1 - 3)은 count - 2와 동일
                    .fetch();
        }
        return Collections.emptyList();
    }

    private List<PublicDiary> findTop3DiariesByLikes(Long cursor) {
        return q.selectFrom(publicDiary)
                .where(getLt(cursor))
                .orderBy(publicDiary.likes.desc(), publicDiary.createdAt.desc())
                .limit(3)
                .fetch();
    }

    private BooleanExpression getLt(Long cursor) {
        return cursor == null ? null : whisperMessage.id.lt(cursor);
    }

    BooleanExpression isSearchable(SearchType sType, String content) {
        if (sType == SearchType.TITLE) {
            return containsTitle(content);
        }
        if (sType == SearchType.AUTHOR) {
            return containsAuthor(content);
        }
        if (sType == SearchType.CONTENT) {
            return containsContent(content);
        }
        return containsTitle(content).or(containsContent(content));
    }

    private BooleanExpression containsContent(String content) {
        return publicDiary.personalDiary.content.containsIgnoreCase(content);
    }

    private BooleanExpression containsAuthor(String content) {
        return publicDiary.user.nickname.eq(content);
    }

    private BooleanExpression containsTitle(String content) {
        return publicDiary.personalDiary.title.containsIgnoreCase(content);
    }
}
