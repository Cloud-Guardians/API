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
public class FiveElement {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "element_id")
    private Long id;

    private String name;

    private String elementPhotoUrl;

    private String fiveElementsPhotoUrl;

    private String plusElement;

    private String minusElement;
}
