package com.cloudians.domain.auth.entity;

import com.cloudians.domain.user.entity.User;
import com.cloudians.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_token")
@Data
@NoArgsConstructor
public class UserToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", nullable = false)
    private Long tokenId;
    
    @Column(name = "user_email", nullable = false, length = 30)
    private String userEmail;

    @Column(name = "token_type", length = 10)
    private String tokenType;

    @Column(name = "token_value", length = 300)
    private String tokenValue;

    @Builder
    private UserToken(String userEmail, String tokenType, String tokenValue) {
        this.userEmail = userEmail;
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
    }

    public static UserToken blackListFrom(User user, String refreshToken) {
        return UserToken.builder()
                .userEmail(user.getUserEmail())
                .tokenType("refresh")
                .tokenValue(refreshToken)
                .build();
    }
}