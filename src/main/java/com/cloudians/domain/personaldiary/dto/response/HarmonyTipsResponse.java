package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.personaldiary.entity.analysis.HarmonyTip;
import lombok.Builder;
import lombok.Getter;

@Getter
public class HarmonyTipsResponse {
    private String activityTag;

    private String activityPhotoUrl;

    private String activityTitle;

    @Builder
    private HarmonyTipsResponse(String activityTag, String activityPhotoUrl, String activityTitle) {
        this.activityTag = activityTag;
        this.activityPhotoUrl = activityPhotoUrl;
        this.activityTitle = activityTitle;
    }

    public static HarmonyTipsResponse of(HarmonyTip harmonyTip) {
        return HarmonyTipsResponse.builder()
                .activityTag(harmonyTip.getActivityTag())
                .activityPhotoUrl(harmonyTip.getActivityPhotoUrl())
                .activityTitle(harmonyTip.getActivityTitle())
                .build();
    }
}
