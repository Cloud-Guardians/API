package com.cloudians.domain.personaldiary.entity.analysis;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FiveElement {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "element_id")
    private Long id;

    private String name;

    private String elementPhotoUrl;

    private String plusElement;

    private String minusElement;
}
