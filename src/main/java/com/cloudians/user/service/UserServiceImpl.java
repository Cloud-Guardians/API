package com.cloudians.user.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cloudians.user.dto.request.UserRequest;
import com.cloudians.user.dto.response.UserResponse;
import com.cloudians.user.entity.User;
import com.cloudians.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{
	
	
	private final UserRepository userRepository;

	@Override
	public UserResponse findByEmail(String userEmail) {
		Optional<User> user = userRepository.findByUserEmail(userEmail);
		if(user.isEmpty()) return null;
		return UserResponse.builder().user(user.get()).build();
	}

	@Override
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

}
