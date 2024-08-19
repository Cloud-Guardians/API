package com.cloudians.domain.home.repository;

import com.cloudians.domain.home.entity.SenderType;
import com.cloudians.domain.home.entity.WhisperMessage;
import com.cloudians.domain.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.cloudians.domain.home.entity.QWhisperMessage.whisperMessage;

@Repository
@RequiredArgsConstructor
public class WhisperMessageRepositoryImpl {
    private final WhisperMessageJpaRepository whisperMessageJpaRepository;
    private final JPAQueryFactory q;

    public void save(WhisperMessage whisperMessage) {
        whisperMessageJpaRepository.save(whisperMessage);
    }

    public boolean existsByUserAndSenderAndTimestampBetween(User user, SenderType sender, LocalDateTime questionDateTime, LocalDateTime twentyFourHoursLater) {
        return q.selectFrom(whisperMessage)
                .where(whisperMessage.user.eq(user)
                        .and(whisperMessage.sender.eq(sender)
                                .and(whisperMessage.timestamp.between(questionDateTime, twentyFourHoursLater))))
                .fetchFirst() != null;
    }

    public List<WhisperMessage> findByUserOrderByTimeStampDesc(User user, Long cursor, Long count) {
        return q.selectFrom(whisperMessage)
                .where(whisperMessage.user.eq(user))
                .where(getLt(cursor)).limit(count + 1)
                .orderBy(whisperMessage.timestamp.desc())
                .fetch();
    }

    private BooleanExpression getLt(Long cursor) {
        return cursor == null ? null : whisperMessage.id.lt(cursor);
    }
}
