package com.cloudians.domain.publicdiary.dto.response.like;

import com.cloudians.domain.publicdiary.entity.like.LikeLink;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LikeResponse {
    private Long likeId;
    private String nickname;
    boolean hasLike;

    @Builder
    private LikeResponse(Long likeId, String nickname, boolean hasLike) {
        this.likeId = likeId;
        this.nickname = nickname;
        this.hasLike = hasLike;
    }

    public static LikeResponse of(LikeLink likeLink, boolean hasLike) {
        return LikeResponse.builder()
                .likeId(likeLink.getId())
                .nickname(likeLink.getUser().getNickname())
                .hasLike(hasLike)
                .build();
    }
}
