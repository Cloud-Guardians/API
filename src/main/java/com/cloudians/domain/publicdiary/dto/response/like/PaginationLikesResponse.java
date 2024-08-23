package com.cloudians.domain.publicdiary.dto.response.like;

import com.cloudians.domain.publicdiary.entity.like.LikeLink;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PaginationLikesResponse {
    private Long likeId;

    private String nickname;

    private String profileUrl;

    @Builder
    private PaginationLikesResponse(Long likeId, String nickname, String profileUrl) {
        this.likeId = likeId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }

    public static PaginationLikesResponse from(LikeLink likeLink) {
        return PaginationLikesResponse.builder()
                .likeId(likeLink.getId())
                .nickname(likeLink.getUser().getNickname())
                .profileUrl(likeLink.getUser().getProfileUrl())
                .build();
    }
}
