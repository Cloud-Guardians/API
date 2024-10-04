package com.cloudians.domain.home.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class WhisperQuestion {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "whisper_question_id")
    Long id;

    private String content;

    private LocalDate date;


}