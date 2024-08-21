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
    
    private UserLock findUserLockByUserEmail(String userEmail) {
	UserLock userLock = userLockRepository.findByUserEmail(userEmail)
		.orElseThrow(()-> new UserException(UserExceptionType. USER_LOCK_NOT_FOUND));
	return userLock;
    }
    private User findUseByUserEmail(String userEmail) {
	User user = userRepository.findByUserEmail(userEmail)
		.orElseThrow(()-> new UserException(UserExceptionType. USER_NOT_FOUND));
	return user;
    }
    // lock 입력 화면
    public boolean checkLock(String userEmail, String insertCode) {
	UserLock userLock = findUserLockByUserEmail(userEmail);
	if(insertCode == null) throw new UserException(UserExceptionType. USER_LOCK_NOT_NULL);
	if(insertCode.length()<4) throw  new UserException(UserExceptionType. USER_LOCK_LENGTH_INVALID);
	if(userLock.getPasscode().equals(insertCode)) {
	    return true;
	} else throw  new UserException(UserExceptionType.LOCK_PASSWORD_INCORRECT);
    }
   
    
    // lock 등록
    public UserLockResponse addNewLock(UserLockRequest request) {
	User user = findUseByUserEmail(request.getUserEmail());
	    UserLock userLock = new UserLock();
	     userLock.setUserEmail(request.getUserEmail());
		   userLock.setPasscode(request.getPasscode());
		   userLock.setStatus(true);
		   userLockRepository.save(userLock);
		   return userLock.toDto();
    }
    
    // lock 삭제
    public void deleteLock(String userEmail, String insertCode) {
	UserLock userLock = findUserLockByUserEmail(userEmail);
	if(userLock.getPasscode().equals(insertCode)) {
	userLockRepository.delete(userLock);}
	else throw new UserException(UserExceptionType.USER_LOCK_DEACTIVATION_FAILED);
    }
    

    // lock 수정
    public UserLockResponse changeLock(String userEmail, String insertCode) {
	UserLock userLock = findUserLockByUserEmail(userEmail);
	userLock.setPasscode(insertCode);
	userLockRepository.save(userLock);
	return userLock.toDto();
    }
    
    // lock toggle on, off 
    public boolean toggleLock(String userEmail) {
	UserLock userLock = findUserLockByUserEmail(userEmail);
	userLock.setStatus(!userLock.getStatus());
	userLockRepository.save(userLock);
	return userLock.getStatus();
    }
   
  
}
