package com.cloudians.domain.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudians.domain.auth.entity.UserToken;
import com.cloudians.domain.auth.repository.UserTokenRepository;
import com.cloudians.domain.publicdiary.dto.response.comment.PublicDiaryCommentResponse;
import com.cloudians.domain.publicdiary.dto.response.diary.PublicDiaryResponse;
import com.cloudians.domain.publicdiary.dto.response.diary.PublicDiaryThumbnailResponse;
import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.exception.PublicDiaryException;
import com.cloudians.domain.publicdiary.exception.PublicDiaryExceptionType;
import com.cloudians.domain.publicdiary.repository.comment.PublicDiaryCommentJpaRepository;
import com.cloudians.domain.publicdiary.repository.diary.PublicDiaryJpaRepository;
import com.cloudians.domain.publicdiary.repository.diary.PublicDiaryRepositoryImpl;
import com.cloudians.domain.publicdiary.service.PublicDiaryService;
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
public class UserService{
	
	private final FirebaseService firebaseService;
	private final UserRepository userRepository;
	private final UserTokenRepository userTokenRepository;
	private final PublicDiaryJpaRepository publicRepository;
	private final PublicDiaryCommentJpaRepository commentRepository;
	private final PublicDiaryRepositoryImpl diaryRepositoryImpl;
	private final PublicDiaryService publicDiaryService;

	public User findUserByEmailOrThrow(String userEmail) {
	    Optional<User> us = userRepository.findByUserEmail(userEmail);
	   User user = userRepository.findByUserEmail(userEmail)
			 .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
	   return user;
	}
    
	// login test
	public void userLogin(String userEmail, String password, String fcmToken) {
	        User user = findUserByEmailOrThrow(userEmail);
	        if (!user.getPassword().equals(password)) {
	            throw new UserException(UserExceptionType.USER_NOT_FOUND);
	        }
	        // FCM 토큰을 데이터베이스에 저장
	        UserToken userToken = new UserToken();
	        userToken.setUserEmail(userEmail);
	        userToken.setTokenType("fcm");
	        userToken.setTokenValue(fcmToken);
	        userTokenRepository.save(userToken);

	    }
	    

    
    // user info
    public UserResponse userInfo(String userEmail) {
	User user = findUserByEmailOrThrow(userEmail);
	return user.toDto();
    }
	

	// 프로필 제외한 개인 정보 변경
	public UserResponse updateUser(String userEmail, UserRequest userRequest) {
		User user = findUserByEmailOrThrow(userEmail);
			 
	    if (userRequest.getName() != null) {
	        user.setName(userRequest.getName());
	    }
	    if (userRequest.getNickname() != null) {
	        user.setNickname(userRequest.getNickname());
	    }
	   if(userRequest.getBirthdate()!=null) {
		   user.setBirthdate(userRequest.getBirthdate());
	   }
	   if(userRequest.getBirthTime()!=null) {
		   user.setBirthTime(userRequest.getBirthTime());
	   }
	   if(userRequest.getCalendarType()!=null) {
		   user.setCalendarType(userRequest.getCalendarType());
	   }
	    User updatedUser = userRepository.save(user);
	    System.out.println(updatedUser.getName());
	    return updatedUser.toDto();
	}
	
	// user profile 
	public Map<String, String> updateProfile(String userEmail, MultipartFile file) {
	    User user = findUserByEmailOrThrow(userEmail);
		    String domain = "profile";
			firebaseService.uploadFile(file, userEmail,domain,domain);
			   user.setProfileUrl(firebaseService.getFileUrl(userEmail,domain,domain));
			   User updatedUser = userRepository.save(user);
			   Map<String, String> map = new HashMap<>();
			   map.put("profile",updatedUser.getProfileUrl());
			   map.put("nickname",updatedUser.getNickname());
			   return map;
	}
	
	// user profile delete 
	public UserResponse deleteUserProfile(String userEmail) {
	    User user = findUserByEmailOrThrow(userEmail);
	    String domain = "profile";
		firebaseService.deleteFileUrl(userEmail,domain,domain);
		user.setProfileUrl(null);
		  User updatedUser = userRepository.save(user);		  
		  return updatedUser.toDto();
	}
	
	// user profile & nickname search
	public Map<String, String> getProfileAndNickname(String userEmail){
	    User user = findUserByEmailOrThrow(userEmail);
	    String profileUrl = user.getProfileUrl();
	    String nickname = user.getNickname();
	    Map<String, String> params = new HashMap<>();
	    params.put("profileUrl",profileUrl);
	    params.put("nickname",nickname);
	    return params;
	}
	
	// user withdraw
	public boolean unregisterUser(String userEmail) {
	    User user = findUserByEmailOrThrow(userEmail);
	    userRepository.delete(user);
	    return true;
	}

	// 유저 작성글 조회 
	public List<PublicDiaryThumbnailResponse> getPublicDiaries(String userEmail){
	    User author = findUserByEmailOrThrow(userEmail);
	    List<PublicDiary> entityList = publicRepository.findListByAuthor(author);
	   List<PublicDiaryThumbnailResponse> list = publicDiaryService.getPublicDiaryThumbnailResponses(entityList);
	    return  list;
	}
	

	// 유저 댓글 조회 
	public List<Map<String,Object>> getPublicComments(String userEmail){
	    User author = findUserByEmailOrThrow(userEmail);
	   List<PublicDiaryComment> entityList = commentRepository.findListByAuthor(author);
	   List<Map<String,Object>> list = new ArrayList<>();
	   
	   for(PublicDiaryComment comment : entityList) {
	       Map<String, Object> map = new HashMap<>();
	       map.put("comment",PublicDiaryCommentResponse.of(comment));
	       PublicDiary diary = diaryRepositoryImpl.findByPublicDiaryComment(comment)
		       .orElseThrow(()-> new PublicDiaryException(PublicDiaryExceptionType.PUBLIC_DIARY_LIST_NOT_FOUND));
	       map.put("diary",PublicDiaryResponse.of(diary,author));
	       list.add(map);
	   }
	   return list;
	}
	


}