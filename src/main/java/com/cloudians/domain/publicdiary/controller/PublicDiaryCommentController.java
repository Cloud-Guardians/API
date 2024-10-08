package com.cloudians.domain.publicdiary.controller;

import com.cloudians.domain.auth.controller.AuthUser;
import com.cloudians.domain.home.dto.response.GeneralPaginatedResponse;
import com.cloudians.domain.publicdiary.dto.request.ReportRequest;
import com.cloudians.domain.publicdiary.dto.request.comment.EditPublicDiaryCommentRequest;
import com.cloudians.domain.publicdiary.dto.request.comment.WriteChildCommentRequest;
import com.cloudians.domain.publicdiary.dto.request.comment.WritePublicDiaryCommentRequest;
import com.cloudians.domain.publicdiary.dto.response.comment.ChildCommentResponse;
import com.cloudians.domain.publicdiary.dto.response.comment.PublicDiaryCommentResponse;
import com.cloudians.domain.publicdiary.dto.response.like.LikeResponse;
import com.cloudians.domain.publicdiary.dto.response.like.PaginationLikesResponse;
import com.cloudians.domain.publicdiary.dto.response.report.PublicDiaryCommentReportResponse;
import com.cloudians.domain.publicdiary.service.PublicDiaryCommentService;
import com.cloudians.domain.user.entity.User;
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
    public ResponseEntity<Message> write(@AuthUser User user,
                                         @PathVariable("public-diary-id") Long publicDiaryId,
                                         @RequestBody @Valid WritePublicDiaryCommentRequest request) {

        PublicDiaryCommentResponse response = publicDiaryCommentService.writeComment(user, publicDiaryId, request);

        Message message = new Message(response, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    @GetMapping()
    public ResponseEntity<Message> getAllComments(@PathVariable("public-diary-id") Long publicDiaryId,
                                                  @RequestParam(required = false) Long cursor,
                                                  @RequestParam(defaultValue = "10") Long count) {

        GeneralPaginatedResponse<PublicDiaryCommentResponse> response = publicDiaryCommentService.getAllComments(publicDiaryId, cursor, count);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @PutMapping("/{public-diary-comment-id}")
    public ResponseEntity<Message> editComment(@AuthUser User user,
                                               @PathVariable("public-diary-id") Long publicDiaryId,
                                               @PathVariable("public-diary-comment-id") Long publicDiaryCommentId,
                                               @RequestBody EditPublicDiaryCommentRequest request) {
        PublicDiaryCommentResponse response = publicDiaryCommentService.editComment(user, publicDiaryId, publicDiaryCommentId, request);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @DeleteMapping("/{public-diary-comment-id}")
    public ResponseEntity<Message> deleteComment(@AuthUser User user,
                                                 @PathVariable("public-diary-id") Long publicDiaryId,
                                                 @PathVariable("public-diary-comment-id") Long publicDiaryCommentId) {
        publicDiaryCommentService.deleteComment(user, publicDiaryId, publicDiaryCommentId);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 대댓글
    @PostMapping("{parent-comment-id}")
    public ResponseEntity<Message> writeChildComment(@AuthUser User user,
                                                     @PathVariable("public-diary-id") Long publicDiaryId,
                                                     @PathVariable("parent-comment-id") Long parentCommentId,
                                                     @RequestBody @Valid WriteChildCommentRequest request) {

        ChildCommentResponse response = publicDiaryCommentService.writeChildComment(user, publicDiaryId, parentCommentId, request);

        Message message = new Message(response, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    @GetMapping("{parent-comment-id}")
    public ResponseEntity<Message> getAllChildComments(@RequestParam(required = false) Long cursor,
                                                       @RequestParam(defaultValue = "10") Long count,
                                                       @PathVariable("public-diary-id") Long publicDiaryId,
                                                       @PathVariable("parent-comment-id") Long parentCommentId) {

        GeneralPaginatedResponse<ChildCommentResponse> response = publicDiaryCommentService.getAllChildComments(cursor, count, publicDiaryId, parentCommentId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @PutMapping("/{parent-comment-id}/{child-comment-id}")
    public ResponseEntity<Message> editChildComment(@AuthUser User user,
                                                    @PathVariable("public-diary-id") Long publicDiaryId,
                                                    @PathVariable("parent-comment-id") Long parentCommentId,
                                                    @PathVariable("child-comment-id") Long childCommentId,
                                                    @RequestBody EditPublicDiaryCommentRequest request) {

        ChildCommentResponse response = publicDiaryCommentService.editChildComment(user, publicDiaryId, parentCommentId, childCommentId, request);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @DeleteMapping("/{parent-comment-id}/{child-comment-id}")
    public ResponseEntity<Message> deleteChildComment(@AuthUser User user,
                                                      @PathVariable("public-diary-id") Long publicDiaryId,
                                                      @PathVariable("parent-comment-id") Long parentCommentId,
                                                      @PathVariable("child-comment-id") Long childCommentId) {

        publicDiaryCommentService.deleteChildComment(user, publicDiaryId, parentCommentId, childCommentId);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @PostMapping("/{public-diary-comment-id}/likes")
    public ResponseEntity<Message> toggleLike(@AuthUser User user,
                                              @PathVariable("public-diary-id") Long publicDiaryId,
                                              @PathVariable("public-diary-comment-id") Long publicDiaryCommentId) {
        LikeResponse response = publicDiaryCommentService.toggleLike(user, publicDiaryId, publicDiaryCommentId);

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
    public ResponseEntity<Message> reportPublicDiaryComment(@AuthUser User user,
                                                            @PathVariable("public-diary-id") Long publicDiaryId,
                                                            @PathVariable("public-diary-comment-id") Long publicDiaryCommentId,
                                                            @RequestBody @Valid ReportRequest request) {
        PublicDiaryCommentReportResponse response = publicDiaryCommentService.reportPublicDiaryComment(user, publicDiaryId, publicDiaryCommentId, request);

        Message message = new Message(response, HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }
}
