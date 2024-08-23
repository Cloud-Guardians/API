package com.cloudians.domain.publicdiary.controller;

import com.cloudians.domain.home.dto.response.GeneralPaginatedResponse;
import com.cloudians.domain.publicdiary.dto.request.EditPublicDiaryCommentRequest;
import com.cloudians.domain.publicdiary.dto.request.ReportRequest;
import com.cloudians.domain.publicdiary.dto.request.WriteChildCommentRequest;
import com.cloudians.domain.publicdiary.dto.request.WritePublicDiaryCommentRequest;
import com.cloudians.domain.publicdiary.dto.response.*;
import com.cloudians.domain.publicdiary.service.PublicDiaryCommentService;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/public-diaries/{public-diary-id}/comments")
@RestController
@RequiredArgsConstructor
public class PublicDiaryCommentController {
    private final PublicDiaryCommentService publicDiaryCommentService;

    @PostMapping()
    public ResponseEntity<Message> write(@RequestParam String userEmail,
                                         @PathVariable("public-diary-id") Long publicDiaryId,
                                         @RequestBody @Valid WritePublicDiaryCommentRequest request) {

        PublicDiaryCommentResponse response = publicDiaryCommentService.writeComment(userEmail, publicDiaryId, request);

        Message message = new Message(response, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    @GetMapping()
    public ResponseEntity<Message> getAllComments(@RequestParam String userEmail,
                                                  @PathVariable("public-diary-id") Long publicDiaryId,
                                                  @RequestParam(required = false) Long cursor,
                                                  @RequestParam(defaultValue = "20") Long count) {

        GeneralPaginatedResponse<PublicDiaryCommentResponse> response = publicDiaryCommentService.getAllComments(publicDiaryId, cursor, count);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @PutMapping("/{public-diary-comment-id}")
    public ResponseEntity<Message> editComment(@RequestParam String userEmail,
                                               @PathVariable("public-diary-id") Long publicDiaryId,
                                               @PathVariable("public-diary-comment-id") Long publicDiaryCommentId,
                                               @RequestBody EditPublicDiaryCommentRequest request) {
        PublicDiaryCommentResponse response = publicDiaryCommentService.editComment(userEmail, publicDiaryId, publicDiaryCommentId, request);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @DeleteMapping("/{public-diary-comment-id}")
    public ResponseEntity<Message> deleteComment(@RequestParam String userEmail,
                                                 @PathVariable("public-diary-id") Long publicDiaryId,
                                                 @PathVariable("public-diary-comment-id") Long publicDiaryCommentId) {
        publicDiaryCommentService.deleteComment(userEmail, publicDiaryId, publicDiaryCommentId);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 대댓글
    @PostMapping("{parent-comment-id}")
    public ResponseEntity<Message> writeChildComment(@RequestParam String userEmail,
                                                     @PathVariable("public-diary-id") Long publicDiaryId,
                                                     @PathVariable("parent-comment-id") Long parentCommentId,
                                                     @RequestBody @Valid WriteChildCommentRequest request) {

        ChildCommentResponse response = publicDiaryCommentService.writeChildComment(userEmail, publicDiaryId, parentCommentId, request);

        Message message = new Message(response, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    @GetMapping("{parent-comment-id}")
    public ResponseEntity<Message> getAllChildComments(@RequestParam(required = false) Long cursor,
                                                       @RequestParam(defaultValue = "20") Long count,
                                                       @PathVariable("public-diary-id") Long publicDiaryId,
                                                       @PathVariable("parent-comment-id") Long parentCommentId) {

        GeneralPaginatedResponse<ChildCommentResponse> response = publicDiaryCommentService.getAllChildComments(cursor, count, publicDiaryId, parentCommentId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @PutMapping("/{parent-comment-id}/{child-comment-id}")
    public ResponseEntity<Message> editChildComment(@RequestParam String userEmail,
                                                    @PathVariable("public-diary-id") Long publicDiaryId,
                                                    @PathVariable("parent-comment-id") Long parentCommentId,
                                                    @PathVariable("child-comment-id") Long childCommentId,
                                                    @RequestBody EditPublicDiaryCommentRequest request) {

        ChildCommentResponse response = publicDiaryCommentService.editChildComment(userEmail, publicDiaryId, parentCommentId, childCommentId, request);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @DeleteMapping("/{parent-comment-id}/{child-comment-id}")
    public ResponseEntity<Message> deleteChildComment(@RequestParam String userEmail,
                                                      @PathVariable("public-diary-id") Long publicDiaryId,
                                                      @PathVariable("parent-comment-id") Long parentCommentId,
                                                      @PathVariable("child-comment-id") Long childCommentId) {

        publicDiaryCommentService.deleteChildComment(userEmail, publicDiaryId, parentCommentId, childCommentId);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @PostMapping("/{public-diary-comment-id}/likes")
    public ResponseEntity<Message> toggleLike(@RequestParam String userEmail,
                                              @PathVariable("public-diary-id") Long publicDiaryId,
                                              @PathVariable("public-diary-comment-id") Long publicDiaryCommentId) {
        LikeResponse response = publicDiaryCommentService.toggleLike(userEmail, publicDiaryId, publicDiaryCommentId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @GetMapping("/{public-diary-comment-id}/likes")
    public ResponseEntity<Message> countLikes(@RequestParam(required = false) Long cursor,
                                              @RequestParam(defaultValue = "10") Long count,
                                              @PathVariable("public-diary-id") Long publicDiaryId,
                                              @PathVariable("public-diary-comment-id") Long publicDiaryCommentId) {
        GeneralPaginatedResponse<PaginationLikesResponse> response = publicDiaryCommentService.countLikes(cursor, count, publicDiaryId, publicDiaryCommentId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @PostMapping("/{public-diary-comment-id}/reports")
    public ResponseEntity<Message> reportPublicDiaryComment(@RequestParam String userEmail,
                                                            @PathVariable("public-diary-id") Long publicDiaryId,
                                                            @PathVariable("public-diary-comment-id") Long publicDiaryCommentId,
                                                            @RequestBody @Valid ReportRequest request) {
        PublicDiaryCommentReportResponse response = publicDiaryCommentService.reportPublicDiaryComment(userEmail, publicDiaryId, publicDiaryCommentId, request);

        Message message = new Message(response, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }
}
