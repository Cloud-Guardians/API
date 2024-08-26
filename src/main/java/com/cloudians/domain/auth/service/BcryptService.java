package com.cloudians.domain.auth.service;

import org.springframework.stereotype.Service;

import com.password4j.Password;

@Service
public class BcryptService {

    public String encodeBcrypt(String plainPassword) {
        return Password.hash(plainPassword).withBcrypt().getResult();
    }

    public boolean matchBcrypt(String plainPassword, String HashedPassword){
        return Password.check(plainPassword, HashedPassword).withBcrypt();
    }
}