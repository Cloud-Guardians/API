package com.cloudians.domain.user.dto.response;

import java.sql.Date;

import com.cloudians.domain.user.entity.UserToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTokenResponse {

    private Long tokenId;

    private String userEmail;
    
    private String tokenType;

    private String tokenValue;

    private Date createdAt;

    private Date updatedAt;
    
    public static UserTokenResponse fromUserToken(UserToken token) {
	return UserTokenResponse.builder()
		.tokenId(token.getTokenId())
		.userEmail(token.getUserEmail())
		.tokenType(token.getTokenType())
		.tokenValue(token.getTokenValue())
		.createdAt(token.getCreatedAt())
		.updatedAt(token.getUpdatedAt())
		.build();
    }
}
