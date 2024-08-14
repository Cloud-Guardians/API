package com.cloudians.domain.user.dto.request;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserLockRequest {
    
    private String userEmail;
    private String passcode;
    private boolean status;
    

}
