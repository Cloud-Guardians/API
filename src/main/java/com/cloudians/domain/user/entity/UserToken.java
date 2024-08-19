package com.cloudians.domain.user.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class UserToken {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="token_id")
    private Long tokenId;
    
    @Column(name="user_email")
    private String userEmail;
    
    @Column(name="token_type")
    private String tokenType;
    
    @Column(name="token_value")
    private String tokenValue;
    
    @Column(name="created_at")
    private Date createdAt;
    
    @Column(name="updated_at")
    private Date updatedAt;
    

}
