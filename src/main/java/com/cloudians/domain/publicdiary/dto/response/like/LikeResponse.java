package com.cloudians.domain.publicdiary.dto.response.like;

import com.cloudians.domain.publicdiary.dto.response.UserProfileResponse;
import com.cloudians.domain.publicdiary.entity.like.LikeLink;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LikeResponse {
    private Long likeId;
    private UserProfileResponse user;
    boolean hasLike;

    @Builder
    private LikeResponse(Long likeId, UserProfileResponse user, boolean hasLike) {
        this.likeId = likeId;
        this.user = user;
        this.hasLike = hasLike;
    }

    public static LikeResponse of(LikeLink likeLink, boolean hasLike) {
        return LikeResponse.builder()
                .likeId(likeLink.getId())
                .user(UserProfileResponse.from(likeLink.getUser()))
                .hasLike(hasLike)
                .build();
    }
}