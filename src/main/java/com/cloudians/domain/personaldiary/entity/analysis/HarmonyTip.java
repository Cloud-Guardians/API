package com.cloudians.domain.personaldiary.entity.analysis;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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
