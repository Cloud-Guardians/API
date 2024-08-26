package com.cloudians.domain.user.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Data
@NoArgsConstructor
public class UserLockRequest {
    
    private String userEmail;
    private String passcode;
    private boolean status;

    @Builder
    public UserLockRequest (String userEmail, String passcode, boolean status) {
	this.userEmail=userEmail;
	this.passcode=passcode;
	this.status = status;
    }

}
