package com.cloudians.domain.user.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudians.domain.user.dto.request.UserLockRequest;
import com.cloudians.domain.user.dto.response.UserLockResponse;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.entity.UserLock;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserLockRepository;
import com.cloudians.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserLockService {
    
    	private final UserRepository userRepository;
    	private final UserLockRepository userLockRepository;
    
    private UserLock findUserLockByUser(User user) {
	UserLock userLock = userLockRepository.findByUser(user)
		.orElseThrow(()-> new UserException(UserExceptionType. USER_LOCK_NOT_FOUND));
	return userLock;
    }
 
    // lock 입력 화면
    public boolean checkLock(User user, String insertCode) {
	UserLock userLock = findUserLockByUser(user);
	if(insertCode == null) throw new UserException(UserExceptionType. USER_LOCK_NOT_NULL);
	if(insertCode.length()<4) throw  new UserException(UserExceptionType. USER_LOCK_LENGTH_INVALID);
	if(userLock.getPasscode().equals(insertCode)) {
	    return true;
	} else throw  new UserException(UserExceptionType.LOCK_PASSWORD_INCORRECT);
    }
   
    
    // lock 등록
    public UserLockResponse addNewLock(UserLockRequest request) {
	User user = userRepository.findByUserEmail(request.getUserEmail())
		.orElseThrow(()-> new UserException(UserExceptionType.USER_NOT_FOUND));
	if(userLockRepository.existsByUser(user)) {
	    UserLock userLock = new UserLock(user, request.getPasscode(), true);
	    return UserLockResponse.fromUserLock(userLock);
	} else throw new UserException(UserExceptionType.USER_LOCK_DEACTIVATION_FAILED);
    }
    
    // lock 삭제
    public void deleteLock(User user, String insertCode) {
	UserLock userLock = userLockRepository.findByUserAndPasscode(user,insertCode)
		.orElseThrow(()-> new UserException(UserExceptionType.USER_LOCK_DEACTIVATION_FAILED));
	userLockRepository.delete(userLock);
    }
    

    // lock 수정
    public UserLockResponse changeLock(User user, String beforePass, String afterPass) {

	UserLock userLock = userLockRepository.findByUserAndPasscode(user,beforePass)
		.orElseThrow(()-> new UserException(UserExceptionType.USER_LOCK_DEACTIVATION_FAILED));
	userLock.edit(user, afterPass);
	return userLock.toDto();
    }
    
    // lock toggle on, off 
    public boolean toggleLock(User user) {
	UserLock userLock = findUserLockByUser(user);
	userLock.toggle(user);
	return userLock.getStatus();
    }
   
  
}
