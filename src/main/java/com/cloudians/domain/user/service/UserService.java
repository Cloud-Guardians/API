package com.cloudians.domain.user.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudians.domain.user.dto.request.UserRequest;
import com.cloudians.domain.user.dto.response.UserResponse;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.entity.UserToken;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;
import com.cloudians.domain.user.repository.UserTokenRepository;
import com.cloudians.global.service.FirebaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService{
	
	private final FirebaseService firebaseService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	private final UserRepository userRepository;
	private final UserTokenRepository userTokenRepository;

	public User findUserByEmailOrThrow(String userEmail) {
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
	    

    //signup_type
    public UserResponse userSignup(UserRequest userRequest) {
	Optional<User> user = userRepository.findByUserEmail(userRequest.getUserEmail());
	User join = new User();
	if(user.isEmpty()) {
	    join.setUserEmail(userRequest.getUserEmail());
	    join.setNickname(userRequest.getNickname());
	    join.setName(userRequest.getName());
	    join.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
	    join.setGender(userRequest.getGender());
	    join.setCalendarType(userRequest.getCalendarType());
	    join.setBirthdate(userRequest.getBirthdate());
	    join.setBirthTime(userRequest.getBirthTime());
	    User NewUser = userRepository.save(join);
	    return NewUser.toDto();
	} else return null;
    }
    
    // user info
    public UserResponse userInfo(String userEmail) {
	User user = findUserByEmailOrThrow(userEmail);
	return user.toDto();
    }
	

	// 프로필 제외한 개인 정보 변경
	public UserResponse updateUser(String userEmail, UserRequest userRequest) {
		System.out.println("조회부터 할게염.");
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
	public UserResponse updateProfile(String userEmail, MultipartFile file) {
	    User user = findUserByEmailOrThrow(userEmail);
		    String domain = "profile";
			firebaseService.uploadFile(file, userEmail,domain,domain);
			   user.setProfileUrl(firebaseService.getFileUrl(userEmail,domain,domain));
			   User updatedUser = userRepository.save(user);
			   return updatedUser.toDto();

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
	


}