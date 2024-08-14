package com.cloudians.domain.user.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudians.domain.user.dto.request.UserRequest;
import com.cloudians.domain.user.dto.response.UserResponse;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.repository.UserRepository;
import com.cloudians.global.service.FirebaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService{
	@Autowired
	private FirebaseService firebaseService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
	@Autowired
	private UserRepository userRepository;
	
    public UserService(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
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
	
	public UserResponse findByEmail(String userEmail) {
		Optional<User> user = userRepository.findByUserEmail(userEmail);
		if(user.isEmpty()) return null;
		UserResponse userResponse = user.get().toDto();
		return userResponse;
	}


	public UserResponse updateUser(String userEmail, UserRequest userRequest) {
		System.out.println("조회부터 할게염.");
		Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
		System.out.println(optionalUser.toString()+"조회 완료.");
		if (optionalUser.isEmpty()) {
	        return null;
	    }
		
		User user = optionalUser.get();
		System.out.println(user.getUserEmail()+"변환 완뇨.");
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
	
	public UserResponse updateUserProfile(String userEmail, MultipartFile file) throws Exception{
		Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
		System.out.println(optionalUser.toString()+"조회 완료.");
		if (optionalUser.isEmpty()) {
	        return null;
	    }
		User user = optionalUser.get();
		// user 가져왔으니까 MultipartFile file 을 Storage에 추가하고, 추가한 Url을 user's profile에 넣어야됨.
		if(file!=null) {
			firebaseService.uploadFiles(file, user.getNickname().toString()+"userProfile");
			String url = "https://firebasestorage.googleapis.com/v0/b/cloudians-photo.appspot.com/o/"+user.getNickname().toString()+"userProfile?alt=media";
			   user.setProfileUrl(url);
			   User updatedUser = userRepository.save(user);
			   return updatedUser.toDto();
		   } else {
			   String url = "https://firebaseStorage.googleapis.com/v0/b/cloudians-photo.appspot.com/o/noneProfile?alt=media";
			   user.setProfileUrl(url);
			   User updatedUser = userRepository.save(user);
			   return updatedUser.toDto();
		   }
	}
	
	public UserResponse deleteUserProfile(String userEmail) throws Exception{
		Optional<User> optionalUser = userRepository.findByUserEmail(userEmail);
		System.out.println(optionalUser.toString()+"조회 완료.");
		if (optionalUser.isEmpty()) {
	        return null;
	    }
		User user = optionalUser.get();
		firebaseService.deleteFileUrl(user.getProfileUrl());
		user.setProfileUrl(null);
		  User updatedUser = userRepository.save(user);
		  
		  return updatedUser.toDto();
	}

}