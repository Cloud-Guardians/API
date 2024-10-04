package com.cloudians.domain.home.entity;

import com.cloudians.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
public class WhisperMessage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "whisper_message_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    private String message;

    private LocalDateTime timestamp;

    @Enumerated(STRING)
    private SenderType sender;

    @Builder
    private WhisperMessage(User user, String message, LocalDateTime timestamp, SenderType sender) {
        this.user = user;
        this.message = message;
        this.timestamp = timestamp;
        this.sender = sender;
    }

    public static WhisperMessage createChatMessage(User user, String message, LocalDateTime timestamp, SenderType sender) {
        return WhisperMessage.builder()
                .user(user)
                .message(message)
                .timestamp(timestamp)
                .sender(sender)
                .build();
    }
}