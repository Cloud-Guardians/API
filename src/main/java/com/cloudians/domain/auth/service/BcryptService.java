package com.cloudians.domain.auth.service;

import com.password4j.Password;
import org.springframework.stereotype.Service;

@Service
public class BcryptService {

    public String encodeBcrypt(String plainPassword) {
        return Password.hash(plainPassword).withBcrypt().getResult();
    }

    public boolean matchBcrypt(String plainPassword, String HashedPassword){
        return Password.check(plainPassword, HashedPassword).withBcrypt();
    }
}
