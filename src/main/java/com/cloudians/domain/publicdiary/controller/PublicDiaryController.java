package com.cloudians.domain.publicdiary.controller;

import com.cloudians.domain.auth.controller.AuthUser;
import com.cloudians.domain.home.dto.response.GeneralPaginatedResponse;
import com.cloudians.domain.publicdiary.dto.request.ReportRequest;
import com.cloudians.domain.publicdiary.dto.response.diary.PublicDiaryResponse;
import com.cloudians.domain.publicdiary.dto.response.diary.PublicDiaryThumbnailResponse;
import com.cloudians.domain.publicdiary.dto.response.like.LikeResponse;
import com.cloudians.domain.publicdiary.dto.response.like.PaginationLikesResponse;
import com.cloudians.domain.publicdiary.dto.response.report.PublicDiaryReportResponse;
import com.cloudians.domain.publicdiary.service.PublicDiaryService;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/public-diaries")
@RestController
@RequiredArgsConstructor
public class PublicDiaryController {
    private final PublicDiaryService publicDiaryService;

    @PostMapping()
    public ResponseEntity<Message> createPublicDiary(@AuthUser User user,
                                                     @RequestParam Long personalDiaryId) {
        PublicDiaryResponse response = publicDiaryService.createPublicDiary(user, personalDiaryId);
        Message message = new Message(response, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping()
    public ResponseEntity<Message> getPublicDiaries(@RequestParam(required = false) Long cursor,
                                                    @RequestParam(defaultValue = "10") Long count,
                                                    @RequestParam(required = false) String searchType,
                                                    @RequestParam(required = false) String keyword) {
        GeneralPaginatedResponse<PublicDiaryThumbnailResponse> response;
        // keyword 검색
        if (keyword != null && !keyword.isEmpty()) {
            response = publicDiaryService.getPublicDiariesByKeyword(cursor, count, searchType, keyword);
            return createGetMessagesResponseEntity(response);
        }

        // 전체 게시글 조회
        response = publicDiaryService.getAllPublicDiaries(cursor, count);
        return createGetMessagesResponseEntity(response);
    }

    @GetMapping("/{public-diary-id}")
    public ResponseEntity<Message> getPublicDiary(@PathVariable("public-diary-id") Long publicDiaryId) {
        PublicDiaryResponse response = publicDiaryService.getPublicDiary(publicDiaryId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @DeleteMapping("/{public-diary-id}")
    public ResponseEntity<Message> deletePublicDiary(@AuthUser User user,
                                                     @PathVariable("public-diary-id") Long publicDiaryId) {
        publicDiaryService.deletePublicDiary(user, publicDiaryId);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // 좋아요
    @PostMapping("/{public-diary-id}/likes")
    public ResponseEntity<Message> toggleLike(@AuthUser User user,
                                              @PathVariable("public-diary-id") Long publicDiaryId) {
        LikeResponse response = publicDiaryService.toggleLike(user, publicDiaryId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @GetMapping("/{public-diary-id}/likes")
    public ResponseEntity<Message> countLikes(@RequestParam(required = false) Long cursor,
                                              @RequestParam(defaultValue = "10") Long count,
                                              @PathVariable("public-diary-id") Long publicDiaryId) {
        GeneralPaginatedResponse<PaginationLikesResponse> response = publicDiaryService.countLikes(cursor, count, publicDiaryId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    //신고
    @PostMapping("/{public-diary-id}/reports")
    public ResponseEntity<Message> reportPublicDiary(@AuthUser User user,
                                                     @PathVariable("public-diary-id") Long publicDiaryId,
                                                     @RequestBody @Valid ReportRequest request) {
        PublicDiaryReportResponse response = publicDiaryService.reportPublicDiary(user, publicDiaryId, request);

        Message message = new Message(response, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    private ResponseEntity<Message> createGetMessagesResponseEntity(GeneralPaginatedResponse<PublicDiaryThumbnailResponse> response) {
        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
