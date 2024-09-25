package com.cloudians.domain.personaldiary.entity.analysis;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class HarmonyTip {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "harmony_id")
    private Long id;

    private String activityTag;

    private String activityPhotoName;

    private String activityTitle;

}