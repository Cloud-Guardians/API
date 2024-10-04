package com.cloudians.domain.user.service;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudians.domain.user.dto.request.UserProfileRequest;
import com.cloudians.domain.user.dto.request.UserRequest;
import com.cloudians.domain.user.dto.response.UserResponse;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;
import com.cloudians.global.service.FirebaseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final FirebaseService firebaseService;
    @Autowired
    private final UserRepository userRepository;

    public User findUserByUserOrThrow(User user) {
        return userRepository.findByUserEmail(user.getUserEmail())
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
      
    }

    // user info
    public UserResponse userInfo(User user) {
        return UserResponse.fromUser(findUserByUserOrThrow(user));
    }

    // 프로필 제외한 개인 정보 변경
    // 이름, 성별, 생년월일, 생시
    public UserResponse updateUser(User user, UserRequest request) {
        System.out.println("조회부터 할게염.");
        User optionalUser = findUserByUserOrThrow(user);
        User editedUser = user.edit(request);
        return UserResponse.fromUser(editedUser);
    }

    // user profile
    public UserResponse updateProfile(User user, MultipartFile file, String editedNickname) {
        User optionalUser = findUserByUserOrThrow(user);
        UserProfileRequest request = new UserProfileRequest();
        
        if(file != null)  {
            String domain = "profile";
            firebaseService.uploadFile(file, user.getUserEmail(), domain, domain);
            String url = firebaseService.getFileUrl(user.getUserEmail(), domain, domain);
            request.setProfileUrl(url);
        } 
        
        if(editedNickname != null && checkDuplicatedNickname(editedNickname)) {
               request.setNickname(editedNickname);
        }
        User editedUser = user.profileEdit(request, user);
        return UserResponse.fromUser(editedUser);
    }

    private boolean checkDuplicatedNickname(String nickname) {
	return userRepository.existsByNickname(nickname);
    }
    
    
    // user profile delete
    public UserResponse deleteUserProfile(User user) {
        User optionalUser = findUserByUserOrThrow(user);
        String domain = "profile";
        firebaseService.deleteFileUrl(user.getUserEmail(), domain, domain);
        UserProfileRequest request = new UserProfileRequest(null, user.getNickname());
        User editedUser = user.profileEdit(request, user);
        return UserResponse.fromUser(editedUser);
    }

    // user profile & nickname search
    public Map<String, String> getProfileAndNickname(User user) {
        User optionalUser = findUserByUserOrThrow(user);
        String profileUrl = user.getProfileUrl();
        String nickname = user.getNickname();
        Map<String, String> params = new HashMap<>();
        params.put("profileUrl", profileUrl);
        params.put("nickname", nickname);
        return params;
    }

    // user withdraw
    public boolean unregisterUser(User user) {
        User optionalUser = findUserByUserOrThrow(user);
        userRepository.delete(user);
        return true;
    }



}