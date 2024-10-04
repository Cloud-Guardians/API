package com.cloudians.domain.personaldiary.dto.response;

import com.cloudians.domain.personaldiary.entity.analysis.HarmonyTip;
import lombok.Builder;
import lombok.Getter;

@Getter
public class HarmonyTipsResponse {
    private String activityTag;

    private String activityPhotoName;

    private String activityTitle;

    @Builder
    private HarmonyTipsResponse(String activityTag, String activityPhotoName, String activityTitle) {
        this.activityTag = activityTag;
        this.activityPhotoName = activityPhotoName;
        this.activityTitle = activityTitle;
    }

    public static HarmonyTipsResponse of(HarmonyTip harmonyTip) {
        return HarmonyTipsResponse.builder()
                .activityTag(harmonyTip.getActivityTag())
                .activityPhotoName(harmonyTip.getActivityPhotoName())
                .activityTitle(harmonyTip.getActivityTitle())
                .build();
    }
}