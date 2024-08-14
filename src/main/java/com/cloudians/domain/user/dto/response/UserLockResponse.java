package com.cloudians.domain.user.dto.response;

import com.cloudians.domain.user.entity.UserLock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLockResponse {
    private String userEmail;
    private String passcode;
    private boolean status;
    
    public static UserLockResponse fromUserLock(UserLock userLock) {
	return UserLockResponse.builder()
		.userEmail(userLock.getUserEmail())
		.passcode(userLock.getPasscode())
		.status(userLock.getStatus())
		.build();
    }

}
