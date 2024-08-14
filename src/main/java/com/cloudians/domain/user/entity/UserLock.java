package com.cloudians.domain.user.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.cloudians.domain.user.dto.response.UserLockResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserLock {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "lock_id")
    private Long id;
    
    @Column(name="user_email")
    private String userEmail;
    
    @Column(name="passcode")
    private String passcode;
    
    @Column(name="status")
    private Boolean status;
    
    public UserLockResponse toDto() {
	 return  UserLockResponse.builder()
		 .userEmail(this.userEmail)
			.passcode(this.passcode)
			.status(this.status)
			.build();
    }
    

}
