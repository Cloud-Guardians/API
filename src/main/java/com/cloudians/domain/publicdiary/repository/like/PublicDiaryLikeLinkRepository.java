package com.cloudians.domain.publicdiary.repository.like;

import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.like.PublicDiaryLikeLink;
import com.cloudians.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicDiaryLikeLinkRepository {

    void save(PublicDiaryLikeLink publicDiaryLikeLink);

    void delete(PublicDiaryLikeLink publicDiaryLikeLink);

    Optional<PublicDiaryLikeLink> findByPublicDiaryAndUser(PublicDiary publicDiary, User user);

    List<PublicDiaryLikeLink> findPublicDiaryLikesOrderByDesc(Long cursor, Long count, Long publicDiaryId);
}
