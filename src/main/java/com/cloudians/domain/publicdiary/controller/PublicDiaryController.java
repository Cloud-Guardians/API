package com.cloudians.domain.publicdiary.controller;

import com.cloudians.domain.publicdiary.dto.response.PublicDiaryCreateResponse;
import com.cloudians.domain.publicdiary.service.PublicDiaryService;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/public-diaries")
@RestController
@RequiredArgsConstructor
public class PublicDiaryController {
    private final PublicDiaryService publicDiaryService;

    @PostMapping()
    public ResponseEntity<Message> createPersonalDiary(@RequestParam String userEmail,
                                                       @RequestParam Long personalDiaryId) {
        PublicDiaryCreateResponse response = publicDiaryService.createPublicDiary(userEmail, personalDiaryId);
        Message message = new Message(response, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }
}
