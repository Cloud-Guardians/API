package com.cloudians.domain.user.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.cloudians.domain.user.dto.response.UserLockResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserLock {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "lock_id")
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_email")
    private User user;
    
    @Column(name="passcode")
    private String passcode;
    
    @Column(name="status")
    private Boolean status;
    
    public UserLock(User user, String passcode, boolean status) {
	this.user=user;
	this.passcode=passcode;
	this.status=status;
    }
  
    public UserLock edit(User user, String afterCode) {
	this.user=user;
	this.passcode=afterCode;
	this.status=true;
	return this;
    }
    
    public UserLock toggle(User user) {
	this.user=user;
	this.status=!status;
	return this;
    }
    
    public UserLockResponse toDto() {
	 return  UserLockResponse.builder()
		 .userEmail(this.user.getUserEmail())
			.passcode(this.passcode)
			.status(this.status)
			.build();
    }
    

}
