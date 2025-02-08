package com.cloudians.domain.personaldiary.entity.analysis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Getter
@NoArgsConstructor
public class FiveElement {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "element_id")
    private Long id;

    private String name;

    private String elementPhotoName;

    private String fiveElementsPhotoName;

    private String plusElement;

    private String minusElement;
}