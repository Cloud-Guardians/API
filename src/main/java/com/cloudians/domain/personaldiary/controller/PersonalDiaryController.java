package com.cloudians.domain.personaldiary.controller;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryCreateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionCreateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionUpdateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryUpdateRequest;
import com.cloudians.domain.personaldiary.dto.response.*;
import com.cloudians.domain.personaldiary.service.PersonalDiaryService;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                                                       @RequestPart @Valid PersonalDiaryCreateRequest request,
                                                       @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        PersonalDiaryCreateResponse response = personalDiaryService.createPersonalDiary(request, userEmail, file);

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
                                                     @RequestPart @Valid PersonalDiaryUpdateRequest request,
                                                     @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {
        PersonalDiaryResponse response = personalDiaryService.editPersonalDiary(request, personalDiaryId, userEmail, file);
        Message message = new Message(response, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }


    // 일기 삭제(자가 감정, 일기 분석, 사진 같이 삭제됨)
    @DeleteMapping("/{personal-diary-id}")
    public ResponseEntity<Message> deletePersonalDiary(@RequestParam String userEmail,
                                                       @PathVariable("personal-diary-id") Long personalDiaryId) {
        personalDiaryService.deletePersonalDiary(userEmail, personalDiaryId);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    //분석 조회
    @GetMapping("/{personal-diary-id}/analyses")
    public ResponseEntity<Message> getAnalyze(@RequestParam String userEmail,
                                              @PathVariable("personal-diary-id") Long personalDiaryId) {
        PersonalDiaryAnalyzeResponse response = personalDiaryService.getAnalyze(userEmail, personalDiaryId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    //일기 사진 삭제
    @DeleteMapping("/{personal-diary-id}/photos")
    public ResponseEntity<Message> deletePersonalDiaryPhoto(@RequestParam String userEmail,
                                                            @PathVariable("personal-diary-id") Long personalDiaryId) {
        personalDiaryService.deletePersonalDiaryPhoto(userEmail, personalDiaryId);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }
}
