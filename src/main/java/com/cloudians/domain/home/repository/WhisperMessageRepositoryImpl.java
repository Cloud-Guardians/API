package com.cloudians.domain.home.repository;

import com.cloudians.domain.home.entity.SenderType;
import com.cloudians.domain.home.entity.WhisperMessage;
import com.cloudians.domain.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.cloudians.domain.home.entity.QWhisperMessage.whisperMessage;

@Repository
@RequiredArgsConstructor
public class WhisperMessageRepositoryImpl implements WhisperMessageRepository {

    private final WhisperMessageJpaRepository whisperMessageJpaRepository;
    private final JPAQueryFactory q;

    @Override
    public void save(WhisperMessage whisperMessage) {
        whisperMessageJpaRepository.save(whisperMessage);
    }

    @Override
    public boolean existsByUserAndSenderAndTimestampBetween(User user, SenderType sender, LocalDateTime questionDateTime, LocalDateTime twentyFourHoursLater) {
        return q.selectFrom(whisperMessage)
                .where(whisperMessage.user.eq(user)
                        .and(whisperMessage.sender.eq(sender)
                                .and(whisperMessage.timestamp.between(questionDateTime, twentyFourHoursLater))))
                .fetchFirst() != null;
    }

    @Override
    public List<WhisperMessage> findByUserOrderByTimeStampDesc(User user, Long cursor, Long count) {
        return q.selectFrom(whisperMessage)
                .where(whisperMessage.user.eq(user)
                        .and(getLt(cursor)))
                .limit(count + 1)
                .orderBy(whisperMessage.timestamp.desc())
                .fetch();
    }

    @Override
    public List<WhisperMessage> findBySearchKeywordOrderByTimeStampDesc(User user, Long cursor, Long count, String keyword) {
        return q.selectFrom(whisperMessage)
                .where(whisperMessage.user.eq(user)
                        .and(whisperMessage.message.containsIgnoreCase(keyword))
                        .and(getLt(cursor))).limit(count + 1)
                .orderBy(whisperMessage.timestamp.desc())
                .fetch();
    }

    @Override
    public List<WhisperMessage> findByDateOrderByTimestampDesc(User user, Long cursor, Long count, LocalDate date) {
        return q.selectFrom(whisperMessage)
                .where(whisperMessage.user.eq(user)
                        .and(whisperMessage.timestamp.between(date.atStartOfDay(), date.plusDays(1).atStartOfDay()))
                        .and(getLt(cursor)))
                .limit(count + 1)
                .orderBy(whisperMessage.timestamp.desc())
                .fetch();
    }

    @Override
    public List<WhisperMessage> findListByUser(User user) {
        return q.selectFrom(whisperMessage)
                .where(whisperMessage.user.eq(user))
                .fetch();
    }

    @Override
    public List<WhisperMessage> findByUserAndSender(User user, SenderType sender) {
        return whisperMessageJpaRepository.findByUserAndSender(user, sender);
    }

    private BooleanExpression getLt(Long cursor) {
        return cursor == null ? null : whisperMessage.id.lt(cursor);
    }
}
