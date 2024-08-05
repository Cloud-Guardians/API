package com.cloudians.domain.personaldiary.controller;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionCreateRequest;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryEmotionCreateResponse;
import com.cloudians.domain.personaldiary.service.PersonalDiaryService;
import com.cloudians.domain.user.service.UserService;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/diaries")
@RestController
@RequiredArgsConstructor
public class PersonalDiaryController {
    private final PersonalDiaryService personalDiaryService;
    private final UserService userService;

    // 자가 감정 측정 생성
    @PostMapping("/self-emotions")
    public ResponseEntity<Message> createSelfEmotions(@RequestParam String userEmail,
                                                      @Valid @RequestBody PersonalDiaryEmotionCreateRequest request) {
        userService.findByEmail(userEmail);

        PersonalDiaryEmotionCreateResponse response = personalDiaryService.createSelfEmotions(request);
        Message message = new Message(response, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }
}
