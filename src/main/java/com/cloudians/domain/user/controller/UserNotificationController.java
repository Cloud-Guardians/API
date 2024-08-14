package com.cloudians.domain.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudians.domain.user.service.UserNotificationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/fcm")
public class UserNotificationController {

    @Autowired
    private final UserNotificationService fcmService;

    public UserNotificationController(UserNotificationService fcmService) {
        this.fcmService = fcmService;
    }

}
