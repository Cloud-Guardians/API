package com.cloudians.domain.user.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudians.domain.user.dto.request.UserRequest;
import com.cloudians.domain.user.dto.response.UserResponse;
import com.cloudians.domain.user.entity.BirthTimeType;
import com.cloudians.domain.user.entity.CalendarType;
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

    public User findUserByEmailOrThrow(String userEmail) {
        Optional<User> us = userRepository.findByUserEmail(userEmail);
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
        return user;
    }

    // user info
    public UserResponse userInfo(String userEmail) {
        User user = findUserByEmailOrThrow(userEmail);
        return UserResponse.fromUser(user);
    }

    // 프로필 제외한 개인 정보 변경
    // 이름, 성별, 생년월일, 생시
    public UserResponse updateUser(String userEmail, UserRequest userRequest) {
        System.out.println("조회부터 할게염.");
        User user = findUserByEmailOrThrow(userEmail);

        if (userRequest.getName() != null) {
            user.setName(userRequest.getName());
        }
//	    if (userRequest.getNickname() != null) {
//	        user.setNickname(userRequest.getNickname());
//	    }
        if (userRequest.getGender() != '\0') {
            user.setGender(userRequest.getGender());
        }
        if (userRequest.getBirthdate() != null) {
            user.setBirthdate(userRequest.getBirthdate());
        }
        if (userRequest.getBirthTime() != null) {
            user.setBirthTime(BirthTimeType.from(userRequest.getBirthTime()));
        }
        if (userRequest.getCalendarType() != null) {
            user.setCalendarType(CalendarType.from(userRequest.getCalendarType()));
        }

        User updatedUser = userRepository.save(user);
        System.out.println(updatedUser.getName());
        return UserResponse.fromUser(user);
    }

    // user profile
    public UserResponse updateProfile(String userEmail, MultipartFile file) {
        User user = findUserByEmailOrThrow(userEmail);
        String domain = "profile";
        firebaseService.uploadFile(file, userEmail, domain, domain);
        user.setProfileUrl(firebaseService.getFileUrl(userEmail, domain, domain));
        User updatedUser = userRepository.save(user);
        return UserResponse.fromUser(user);

    }

    // user profile delete
    public UserResponse deleteUserProfile(String userEmail) {
        User user = findUserByEmailOrThrow(userEmail);
        String domain = "profile";
        firebaseService.deleteFileUrl(userEmail, domain, domain);
        user.setProfileUrl(null);
        User updatedUser = userRepository.save(user);
        return UserResponse.fromUser(user);
    }

    // user profile & nickname search
    public Map<String, String> getProfileAndNickname(String userEmail) {
        User user = findUserByEmailOrThrow(userEmail);
        String profileUrl = user.getProfileUrl();
        String nickname = user.getNickname();
        Map<String, String> params = new HashMap<>();
        params.put("profileUrl", profileUrl);
        params.put("nickname", nickname);
        return params;
    }

    // user withdraw
    public boolean unregisterUser(String userEmail) {
        User user = findUserByEmailOrThrow(userEmail);
        userRepository.delete(user);
        return true;
    }

    // 유저 작성글 조회

    // 유저 댓글 조회


}