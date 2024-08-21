package com.cloudians.domain.publicdiary.controller;

import com.cloudians.domain.publicdiary.dto.response.PublicDiaryCreateResponse;
import com.cloudians.domain.publicdiary.service.PublicDiaryService;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/public-diaries")
@RestController
@RequiredArgsConstructor
public class PublicDiaryController {
    private final PublicDiaryService publicDiaryService;

    @PostMapping()
    public ResponseEntity<Message> createPublicDiary(@RequestParam String userEmail,
                                                     @RequestParam Long personalDiaryId) {
        PublicDiaryCreateResponse response = publicDiaryService.createPublicDiary(userEmail, personalDiaryId);
        Message message = new Message(response, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    @DeleteMapping("/{public-diary-id}")
    public ResponseEntity<Message> deletePublicDiary(@RequestParam String userEmail,
                                                     @PathVariable("public-diary-id") Long publicDiaryId) {
        publicDiaryService.deletePublicDiary(userEmail, publicDiaryId);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }
}
