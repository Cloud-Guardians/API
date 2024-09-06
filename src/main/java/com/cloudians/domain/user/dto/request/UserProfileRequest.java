package com.cloudians.domain.user.dto.request;

import com.cloudians.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserProfileRequest {


    private String profileUrl;
    private String nickname;
    
    public UserProfileRequest() {
	
    }
    public UserProfileRequest profileEdit(User user, String profileUrl) {
	this.profileUrl = profileUrl;
	this.nickname = user.getNickname();
	return this;
    }
    

    public UserProfileRequest nicknameEdit(User user, String editedNickname) {
	this.profileUrl = user.getProfileUrl();
	this.nickname = editedNickname;
	return this;
    }

    
    
}
