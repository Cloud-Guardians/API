package com.cloudians.domain.auth.controller;

import com.cloudians.domain.auth.dto.request.LoginRequest;
import com.cloudians.domain.auth.dto.request.SignupRequest;
import com.cloudians.domain.auth.dto.request.TokenRefreshRequest;
import com.cloudians.domain.auth.dto.response.LoginResponse;
import com.cloudians.domain.auth.dto.response.SignupResponse;
import com.cloudians.domain.auth.service.AuthService;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


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

    @PostMapping("/login")
    public ResponseEntity<Message> login(@Valid @RequestBody LoginRequest request) {
        // valid 붙여야지만 예외 처리 가능함
        LoginResponse response = authService.login(request);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Token", "Bearer " + response.getAccessToken());
        headers.set("Refresh-Token", "Bearer " + response.getRefreshToken());

        Message message = new Message(null, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(message);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Message> refreshAccessToken(@Valid @RequestBody TokenRefreshRequest request) {
        String accessToken = authService.refreshAccessToken(request);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Token", "Bearer " + accessToken);

        Message message = new Message( null, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(message);
    }

}