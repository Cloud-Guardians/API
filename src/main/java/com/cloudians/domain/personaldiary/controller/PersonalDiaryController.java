package com.cloudians.domain.personaldiary.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryCreateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionCreateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionUpdateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryUpdateRequest;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryCreateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryEmotionCreateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryEmotionUpdateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryResponse;
import com.cloudians.domain.personaldiary.service.PersonalDiaryService;
import com.cloudians.global.Message;

import lombok.RequiredArgsConstructor;

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

    // 자가 감정 측정 수정
    @PutMapping("/self-emotions/{emotion-id}")
    public ResponseEntity<Message> editSelfEmotions(@RequestParam String userEmail,
                                                    @PathVariable("emotion-id") Long emotionId,
                                                    @Valid @RequestBody PersonalDiaryEmotionUpdateRequest request) {
        PersonalDiaryEmotionUpdateResponse response = personalDiaryService.editSelfEmotions(request, emotionId, userEmail);
        Message message = new Message(response, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 일기 내용 생성
    @PostMapping()
    public ResponseEntity<Message> createPersonalDiary(@RequestParam String userEmail,
                                                       @Valid @RequestBody PersonalDiaryCreateRequest request) {

        PersonalDiaryCreateResponse response = personalDiaryService.createPersonalDiary(request, userEmail);
        Message message = new Message(response, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    // 일기 내용 조회
    @GetMapping("/{personal-diary-id}")
    public ResponseEntity<Message> getPersonalDiary(@RequestParam String userEmail,
                                                    @PathVariable("personal-diary-id") Long personalDiaryId) {
        PersonalDiaryResponse response = personalDiaryService.getPersonalDiary(userEmail, personalDiaryId);
        Message message = new Message(response, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 일기 내용 수정
    @PutMapping("/{personal-diary-id}")
    public ResponseEntity<Message> editPersonalDiary(@RequestParam String userEmail,
                                                     @PathVariable("personal-diary-id") Long personalDiaryId,
                                                     @Valid @RequestBody PersonalDiaryUpdateRequest request) {
        PersonalDiaryResponse response = personalDiaryService.editPersonalDiary(request, personalDiaryId, userEmail);
        Message message = new Message(response, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }


    // 자가 감정 및 일기 삭제
    @DeleteMapping("/{personal-diary-id}")
    public ResponseEntity<Message> deletePersonalDiary(@RequestParam String userEmail,
                                                       @PathVariable("personal-diary-id") Long personalDiaryId) {
        personalDiaryService.deletePersonalDiary(userEmail, personalDiaryId);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }
}