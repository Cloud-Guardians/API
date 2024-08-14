package com.cloudians.domain.user.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloudians.domain.user.dto.request.UserLockRequest;
import com.cloudians.domain.user.dto.response.UserLockResponse;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.entity.UserLock;
import com.cloudians.domain.user.repository.UserLockRepository;
import com.cloudians.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserLockService {
    @Autowired
	private UserRepository userRepository;
    @Autowired
    	private UserLockRepository userLockRepository;
    
    public UserLock findByEmail(String userEmail) {
	Optional<UserLock> userLock = userLockRepository.findByUserEmail(userEmail);
	return userLock.get();
    }
    
    // lock 입력 화면
    public boolean checkLock(String userEmail, String insertCode) {
	Optional<UserLock> userLock = userLockRepository.findByUserEmail(userEmail);
	if(userLock.get().getPasscode().equals(insertCode)) {
	    return true;
	} return false;
    }
   
    
    // lock 등록
    public UserLockResponse addLock(String userEmail, String passcode) {
	Optional<User> user = userRepository.findByUserEmail(userEmail);
	if(!user.isEmpty()) {
	    Optional<UserLock> userLock = userLockRepository.findByUserEmail(userEmail);
	    UserLock newUserLock = new UserLock();
	    if(userLock.isEmpty()) {
		newUserLock.setUserEmail(userEmail);
		newUserLock.setPasscode(passcode);
		newUserLock.setStatus(true);
		userLockRepository.save(newUserLock);
		return newUserLock.toDto();
	    } else return null;
	} return null;
    }
    
    // lock 삭제
    public void deleteLock(UserLock userLockRequest) {
	Optional<UserLock> userLock = userLockRepository.findByUserEmail(userLockRequest.getUserEmail());
	userLockRepository.delete(userLock.get());
    }
    

    // lock 수정
   
  
}
