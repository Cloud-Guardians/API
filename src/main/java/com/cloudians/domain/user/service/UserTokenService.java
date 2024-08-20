package com.cloudians.domain.user.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.cloudians.domain.user.repository.UserTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserTokenService {
    
    private final UserTokenRepository tokenRepository;

}
