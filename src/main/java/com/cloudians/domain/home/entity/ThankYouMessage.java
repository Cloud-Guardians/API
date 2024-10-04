package com.cloudians.domain.home.entity;

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
public class ThankYouMessage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "thank_you_message_id")
    Long id;

    private String content;
}