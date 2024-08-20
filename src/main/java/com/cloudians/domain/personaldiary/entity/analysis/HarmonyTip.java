package com.cloudians.domain.personaldiary.entity.analysis;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class HarmonyTip {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "harmony_id")
    private Long id;

    private String activityTag;

    private String activityPhotoUrl;

    private String activityTitle;

}