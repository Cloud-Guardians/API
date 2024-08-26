package com.cloudians.domain.publicdiary.dto.response.like;

import com.cloudians.domain.publicdiary.dto.response.UserProfileResponse;
import com.cloudians.domain.publicdiary.entity.like.LikeLink;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PaginationLikesResponse {
    private Long likeId;

    private UserProfileResponse user;

    private String profileUrl;

    @Builder
    private PaginationLikesResponse(Long likeId, UserProfileResponse user, String profileUrl) {
        this.likeId = likeId;
        this.user = user;
        this.profileUrl = profileUrl;
    }

    public static PaginationLikesResponse from(LikeLink likeLink) {
        return PaginationLikesResponse.builder()
                .likeId(likeLink.getId())
                .user(UserProfileResponse.from(likeLink.getUser()))
                .profileUrl(likeLink.getUser().getProfileUrl())
                .build();
    }
}