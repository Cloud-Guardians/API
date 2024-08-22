package com.cloudians.domain.publicdiary.controller;

import com.cloudians.domain.home.dto.response.GeneralPaginatedResponse;
import com.cloudians.domain.publicdiary.dto.response.PublicDiaryResponse;
import com.cloudians.domain.publicdiary.dto.response.PublicDiaryThumbnailResponse;
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
    public ResponseEntity<Message> createPublicDiary(@RequestParam String userEmail, @RequestParam Long personalDiaryId) {
        PublicDiaryResponse response = publicDiaryService.createPublicDiary(userEmail, personalDiaryId);
        Message message = new Message(response, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping()
    public ResponseEntity<Message> getPublicDiaries(@RequestParam String userEmail, @RequestParam(required = false) Long cursor, @RequestParam(defaultValue = "10") Long count, @RequestParam(required = false) String searchType, @RequestParam(required = false) String keyword) {
        GeneralPaginatedResponse<PublicDiaryThumbnailResponse> response;
        // keyword 검색
        if (keyword != null && !keyword.isEmpty()) {
            response = publicDiaryService.getPublicDiariesByKeyword(userEmail, cursor, count, searchType, keyword);
            return createGetMessagesResponseEntity(response);
        }

        // 전체 게시글 조회
        response = publicDiaryService.getAllPublicDiaries(userEmail, cursor, count);
        return createGetMessagesResponseEntity(response);
    }

    @GetMapping("/{public-diary-id}")
    public ResponseEntity<Message> getPublicDiary(@RequestParam String userEmail, @PathVariable("public-diary-id") Long publicDiaryId) {
        PublicDiaryResponse response = publicDiaryService.getPublicDiary(userEmail, publicDiaryId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @DeleteMapping("/{public-diary-id}")
    public ResponseEntity<Message> deletePublicDiary(@RequestParam String userEmail, @PathVariable("public-diary-id") Long publicDiaryId) {
        publicDiaryService.deletePublicDiary(userEmail, publicDiaryId);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    private ResponseEntity<Message> createGetMessagesResponseEntity(GeneralPaginatedResponse<PublicDiaryThumbnailResponse> response) {
        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
