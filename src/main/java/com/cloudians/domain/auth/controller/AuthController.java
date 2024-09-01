package com.cloudians.domain.auth.controller;


import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudians.domain.auth.dto.request.LoginRequest;
import com.cloudians.domain.auth.dto.request.SignupRequest;
import com.cloudians.domain.auth.dto.response.SignupResponse;
import com.cloudians.domain.auth.service.AuthService;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.global.Message;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;



    @PostMapping("/signup")
    public ResponseEntity<Message> signup(@Valid @RequestBody SignupRequest request) {
        // valid 붙여야지만 예외 처리 가능함
        SignupResponse response = authService.signup(request);
        Message message = new Message(response, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    @GetMapping("/resource")
    public ResponseEntity<String> getResource(@RequestHeader("Authorization") String authorizationHeader) {
        String jwtToken = authorizationHeader.replace("Bearer ", "");
        try {
            String email = authService.getUserEmail(jwtToken);
            // Do something with the user
            return ResponseEntity.ok("Resource accessed by " +email);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<Message> login(@Valid @RequestBody LoginRequest request) {
        // valid 붙여야지만 예외 처리 가능함
        String token = authService.login(request);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        Message message = new Message(token, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(message);
    }

}
