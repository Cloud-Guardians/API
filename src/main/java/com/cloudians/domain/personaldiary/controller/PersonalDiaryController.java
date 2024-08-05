package com.cloudians.domain.personaldiary.controller;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryCreateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionCreateRequest;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryCreateResponse;
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

    // 자가 감정 측정 생성
    @PostMapping("/self-emotions")
    public ResponseEntity<Message> createSelfEmotions(@RequestParam String userEmail,
                                                      @Valid @RequestBody PersonalDiaryEmotionCreateRequest request) {

        PersonalDiaryEmotionCreateResponse response = personalDiaryService.createTempSelfEmotions(request, userEmail);
        Message message = new Message(response, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    //     일기 내용 작성
    @PostMapping()
    public ResponseEntity<Message> createPersonalDiary(@RequestParam String userEmail,
                                                       @Valid @RequestBody PersonalDiaryCreateRequest request) {

        PersonalDiaryCreateResponse response = personalDiaryService.createPersonalDiary(request, userEmail);
        Message message = new Message(response, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }
}
