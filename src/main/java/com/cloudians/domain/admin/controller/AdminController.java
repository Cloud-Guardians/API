package com.cloudians.domain.admin.controller;

import com.cloudians.domain.admin.dto.request.ProcessReportsRequest;
import com.cloudians.domain.admin.dto.request.UpdateReportRequest;
import com.cloudians.domain.admin.dto.response.comment.AdminReportCommentResponse;
import com.cloudians.domain.admin.dto.response.diary.AdminReportResponse;
import com.cloudians.domain.admin.dto.response.diary.ReportedDiaryResponse;
import com.cloudians.domain.admin.service.AdminService;
import com.cloudians.domain.auth.controller.AuthUser;
import com.cloudians.domain.publicdiary.entity.report.ReportStatus;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 신고 게시글 목록 조회 (+삭제된 게시글과 반려된 게시글 구분 조회)
    @GetMapping("/public-diaries")
    public ResponseEntity<Message> getReports(@RequestParam ReportStatus status, @AuthUser User user) {
        List<AdminReportResponse> response = adminService.getAllReports(status, user);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 신고 게시글 상세 조회
    @GetMapping("/public-diary/{public-diary-report-id}")
    public ResponseEntity<Message> getReport(@PathVariable("public-diary-report-id") Long publicDiaryReportId) {
        AdminReportResponse response = adminService.getReport(publicDiaryReportId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 신고당한 게시글 조회
    @GetMapping("reported-diary/{public-diary-id}")
    public ResponseEntity<Message> getReportedDiary(@PathVariable("public-diary-id") Long publicDiaryId) {
        ReportedDiaryResponse response = adminService.getReportedDiary(publicDiaryId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 게시글 중복 신고 목록 조회
    @GetMapping("/public-diary/{public-diary-id}/duplicate")
    public ResponseEntity<Message> getDuplicateReports(@PathVariable("public-diary-id") Long publicDiaryId) {
        List<AdminReportResponse> response = adminService.getDuplicateReports(publicDiaryId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 신고 댓글 목록 조회(+삭제된 댓글 반려된 댓글 구분 조회)
    @GetMapping("/comments")
    public ResponseEntity<Message> getReportComments(@RequestParam ReportStatus status) {
        List<AdminReportCommentResponse> response = adminService.getAllComments(status);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 신고된 댓글 상세 조회
    @GetMapping("/comment/{public-diary-comment-report-id}")
    public ResponseEntity<Message> getCommentReport(@PathVariable("public-diary-comment-report-id") Long commentReportId) {
        AdminReportCommentResponse response = adminService.getComment(commentReportId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 댓글 중복 신고 목록 조회
    @GetMapping("/comments/{comment-id}/duplicate")
    public ResponseEntity<Message> getDuplicateCommentsReport(@PathVariable("comment-id") Long commentId) {
        List<AdminReportCommentResponse> response = adminService.getDuplicateCommentsReport(commentId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 신고 댓글 + 게시글 상세 조회
    @GetMapping("/comments/{comment-id}")
    public ResponseEntity<Message> getReportedComment(@PathVariable("comment-id") Long commentId) {
        AdminReportCommentResponse response = adminService.getReportedComment(commentId);

        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 단일 신고 - 게시글 반려 및 삭제 처리
    @PostMapping("public-diary/reject/{report-id}")
    public ResponseEntity<Message> rejectReport(@PathVariable("report-id") Long reportId, @RequestBody ProcessReportsRequest request){//, @RequestBody RejectRequest request) {
        adminService.editReport(reportId, request);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 단일 신고 - 댓글 반려 처리
    @PostMapping("comment/reject/{comment-id}")
    public ResponseEntity<Message> rejectCommentReport(@PathVariable("comment-id") Long commentId, @RequestBody ProcessReportsRequest request){//, @RequestBody RejectRequest request) {
        adminService.editCommentReport(commentId, request);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 게시글 신고 다중 처리
    @PostMapping("/public-diaries/bulk-update")
    public ResponseEntity<Message> updateReport(@RequestBody UpdateReportRequest updateReportRequest) {
        adminService.updateReports(updateReportRequest);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    // 댓글 신고 다중 처리
    @PostMapping("/comments/bulk-update")
    public ResponseEntity<Message> updateCommentReport(@RequestBody UpdateReportRequest updateReportRequest) {
        adminService.updateCommentsReport(updateReportRequest);

        Message message = new Message(null, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }
}
