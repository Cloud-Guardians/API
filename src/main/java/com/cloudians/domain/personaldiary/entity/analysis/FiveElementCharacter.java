package com.cloudians.domain.personaldiary.entity.analysis;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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