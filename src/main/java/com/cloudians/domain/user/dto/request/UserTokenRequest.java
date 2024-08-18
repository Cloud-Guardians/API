package com.cloudians.domain.user.dto.request;

import java.sql.Date;

import com.cloudians.domain.user.dto.response.UserTokenResponse;
import com.cloudians.domain.user.entity.UserToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTokenRequest {
    private Long tokenId;

    private String userEmail;
    
    private String tokenType;

    private String tokenValue;

    private Date createdAt;

    private Date updatedAt;
    
    public static UserTokenRequest fromUserToken(UserToken token) {
  	return UserTokenRequest.builder()
  		.tokenId(token.getTokenId())
  		.userEmail(token.getUserEmail())
  		.tokenType(token.getTokenType())
  		.tokenValue(token.getTokenValue())
  		.createdAt(token.getCreatedAt())
  		.updatedAt(token.getUpdatedAt())
  		.build();
      }
}
