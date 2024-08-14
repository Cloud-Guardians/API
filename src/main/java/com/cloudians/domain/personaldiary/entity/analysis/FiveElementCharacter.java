package com.cloudians.domain.personaldiary.entity.analysis;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class FiveElementCharacter {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "char_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "element_id")
    private FiveElement element;

    private String character;
}
