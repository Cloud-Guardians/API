package com.cloudians.domain.admin.service;

import com.cloudians.domain.admin.dto.request.ProcessReportsRequest;
import com.cloudians.domain.admin.dto.request.UpdateReportRequest;
import com.cloudians.domain.admin.dto.response.comment.AdminReportCommentResponse;
import com.cloudians.domain.admin.dto.response.diary.AdminReportResponse;
import com.cloudians.domain.admin.dto.response.diary.ReportedDiaryResponse;
import com.cloudians.domain.admin.exception.AdminException;
import com.cloudians.domain.admin.exception.AdminExceptionType;
import com.cloudians.domain.admin.repository.AdminPublicDiaryReportJpaRepository;
import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryCommentReport;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryReport;
import com.cloudians.domain.publicdiary.entity.report.ReportStatus;
import com.cloudians.domain.publicdiary.repository.comment.PublicDiaryCommentRepositoryImpl;
import com.cloudians.domain.publicdiary.repository.diary.PublicDiaryRepositoryImpl;
import com.cloudians.domain.publicdiary.repository.report.PublicDiaryCommentReportJpaRepository;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cloudians.domain.publicdiary.entity.report.ReportStatus.DISMISS;
import static com.cloudians.domain.user.entity.UserStatus.ADMIN;


@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final AdminPublicDiaryReportJpaRepository publicDiaryReportRepository;

    private final PublicDiaryCommentReportJpaRepository publicDiaryCommentReportRepository;

    private final UserRepository userRepository;

    private final PublicDiaryRepositoryImpl publicDiaryRepository;

    private final PublicDiaryCommentRepositoryImpl publicDiaryCommentRepository;

    public List<AdminReportResponse> getAllReports(ReportStatus status, User user) {
        checkAdminStatus(user.getUserEmail());

        List<PublicDiaryReport> response = publicDiaryReportRepository.findByStatus(status);

        if (response.isEmpty()) {
            throw new AdminException(AdminExceptionType.STATUS_NOT_FOUND);
        }

        return response.stream()
                .map(report -> AdminReportResponse.of(report, report.getReporter()))
                .collect(Collectors.toList());
    }

    public AdminReportResponse getReport(Long publicDiaryReportId) {
        PublicDiaryReport response = getPublicDiaryReport(publicDiaryReportId);

        response.changeReadStatus(true);

        return AdminReportResponse.of(response, response.getReporter());
    }

    public ReportedDiaryResponse getReportedDiary(Long publicDiaryId) {
        PublicDiary response = getPublicDiary(publicDiaryId);

        return ReportedDiaryResponse.from(response);
    }

    public List<AdminReportResponse> getDuplicateReports(Long publicDiaryId) {
        PublicDiary reportedDiary = getPublicDiary(publicDiaryId);

        List<PublicDiaryReport> response = publicDiaryReportRepository.findByReportedDiary(reportedDiary);

        if (response.size() == 1) {
            throw new AdminException(AdminExceptionType.NOT_ENOUGH_REPORTS);
        }

        if (response.isEmpty()) {
            throw new AdminException(AdminExceptionType.NO_REPORTS_FOR_PUBLIC_DIARY);
        }

        return response.stream()
                .sorted(Comparator.comparingLong(PublicDiaryReport::getId))
                .map(report -> AdminReportResponse.of(report, report.getReporter()))
                .collect(Collectors.toList());
    }

    public List<AdminReportCommentResponse> getAllComments(ReportStatus status, User user) {
        checkAdminStatus(user.getUserEmail());

        List<PublicDiaryCommentReport> response = publicDiaryCommentReportRepository.findByStatus(status);

        if (response.isEmpty()) {
            throw new AdminException(AdminExceptionType.STATUS_NOT_FOUND);
        }

        return response.stream()
                .map(commentReport -> AdminReportCommentResponse.of(commentReport, commentReport.getReporter()))
                .collect(Collectors.toList());
    }

    public List<AdminReportCommentResponse> getDuplicateCommentsReport(Long commentId) {
        PublicDiaryComment reportedComment = getPublicDiaryComment(commentId);

        List<PublicDiaryCommentReport> response = publicDiaryCommentReportRepository.findByReportedComment(reportedComment);

        if (response.size() == 1) {
            throw new AdminException(AdminExceptionType.NOT_ENOUGH_REPORTS);
        }

        if (response.isEmpty()) {
            throw new AdminException(AdminExceptionType.NO_REPORTS_FOR_COMMENT);
        }

        return response.stream()
                .sorted(Comparator.comparingLong(PublicDiaryCommentReport::getId))
                .map(commentReport -> AdminReportCommentResponse.of(commentReport, commentReport.getReporter()))
                .collect(Collectors.toList());
    }

    public AdminReportCommentResponse getComment(Long commentReportId) {
        PublicDiaryCommentReport response = getPublicDiaryCommentReport(commentReportId);

        response.changeReadStatus(true);

        return AdminReportCommentResponse.of(response, response.getReporter());
    }

    public void checkAdminStatus(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        if (!ADMIN.equals(user.getStatus())) {
            throw new AdminException(AdminExceptionType.UNAUTHORIZED_ACCESS);
        }
    }

    public AdminReportCommentResponse getReportedComment(Long commentId) {
        PublicDiaryCommentReport response = getPublicDiaryCommentReport(commentId);

        return AdminReportCommentResponse.of(response, response.getReporter());
    }

    public void editReport(Long reportId, ProcessReportsRequest request) {
        PublicDiaryReport report = getPublicDiaryReport(reportId);
        User reportedUser = report.getReportedDiary().getAuthor();
        PublicDiary diary = report.getReportedDiary();
        String action = request.getAction();

        if ("delete".equals(action)) {
            reportedUser.setTotalReportCount(reportedUser.getTotalReportCount() + 1);
            publicDiaryRepository.delete(diary);
            publicDiaryReportRepository.delete(report);

        } else if ("update".equals(action)) {
            PublicDiaryReport response = getPublicDiaryReport(reportId);
            response.changeStatus(DISMISS);
            response.changeReadStatus(true);
        } else throw new AdminException(AdminExceptionType.INVALID_ACTION);
    }

    public void editCommentReport(Long reportId, ProcessReportsRequest request) {
        PublicDiaryCommentReport report = getPublicDiaryCommentReport(reportId);
        User reportedUser = report.getReportedComment().getAuthor();
        PublicDiaryComment comment = report.getReportedComment();
        String action = request.getAction();

        if ("delete".equals(action)) {
            reportedUser.setTotalReportCount(reportedUser.getTotalReportCount() + 1);
            publicDiaryCommentRepository.delete(comment);
            publicDiaryCommentReportRepository.delete(report);

        } else if ("update".equals(action)) {
            PublicDiaryCommentReport response = getPublicDiaryCommentReport(reportId);
            response.changeStatus(DISMISS);
            response.changeReadStatus(true);
        } else throw new AdminException(AdminExceptionType.INVALID_ACTION);
    }

    public void updateReports(UpdateReportRequest request) {
        String action = request.getAction();

        for (Long reportId : request.getReportId()) {
            ProcessReportsRequest processRequest = new ProcessReportsRequest(action);

            editReport(reportId, processRequest);
        }
    }

    public void updateCommentsReport(UpdateReportRequest request) {
        String action = request.getAction();

        for (Long reportId : request.getReportId()) {
            ProcessReportsRequest processRequest = new ProcessReportsRequest(action);

            editCommentReport(reportId, processRequest);
        }
    }

    private PublicDiaryReport getPublicDiaryReport(Long publicDiaryReportId) {
        return publicDiaryReportRepository.findById(publicDiaryReportId)
                .orElseThrow(() -> new AdminException(AdminExceptionType.REPORT_NOT_FOUND));
    }

    private PublicDiaryCommentReport getPublicDiaryCommentReport(Long publicDiaryCommentReportId) {
        return publicDiaryCommentReportRepository.findById(publicDiaryCommentReportId)
                .orElseThrow(() -> new AdminException(AdminExceptionType.REPORT_NOT_FOUND));
    }

    private PublicDiary getPublicDiary(Long publicDiaryId) {
        return publicDiaryRepository.findById(publicDiaryId)
                .orElseThrow(() -> new AdminException(AdminExceptionType.NON_EXIST_REPORTED_DIARY));
    }

    private PublicDiaryComment getPublicDiaryComment(Long publicDiaryCommentId) {
        return publicDiaryCommentRepository.findById(publicDiaryCommentId)
                .orElseThrow(() -> new AdminException(AdminExceptionType.NON_EXIST_REPORTED_COMMENT));
    }
}
