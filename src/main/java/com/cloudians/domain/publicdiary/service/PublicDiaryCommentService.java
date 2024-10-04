package com.cloudians.domain.publicdiary.service;

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
import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.like.PublicDiaryCommentLikeLink;
import com.cloudians.domain.publicdiary.entity.report.PublicDiaryCommentReport;
import com.cloudians.domain.publicdiary.exception.PublicDiaryException;
import com.cloudians.domain.publicdiary.exception.PublicDiaryExceptionType;
import com.cloudians.domain.publicdiary.repository.comment.PublicDiaryCommentRepository;
import com.cloudians.domain.publicdiary.repository.diary.PublicDiaryRepository;
import com.cloudians.domain.publicdiary.repository.like.PublicDiaryCommentLikeLinkRepository;
import com.cloudians.domain.publicdiary.repository.report.PublicDiaryCommentReportRepository;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PublicDiaryCommentService {
    private final PublicDiaryCommentRepository publicDiaryCommentRepository;
    private final PublicDiaryRepository publicDiaryRepository;
    private final PublicDiaryCommentLikeLinkRepository publicDiaryCommentLikeLinkRepository;
    private final PublicDiaryCommentReportRepository publicDiaryCommentReportRepository;

    public PublicDiaryCommentResponse writeComment(User author, Long publicDiaryId, WritePublicDiaryCommentRequest request) {

        PublicDiary publicDiary = findPublicDiaryByIdOrThrow(publicDiaryId);
        PublicDiaryComment publicDiaryComment = request.toEntity(publicDiary, author);
        publicDiaryCommentRepository.save(publicDiaryComment);

        return PublicDiaryCommentResponse.of(publicDiaryComment);
    }

    public GeneralPaginatedResponse<PublicDiaryCommentResponse> getAllComments(Long publicDiaryId, Long cursor, Long count) {
        findPublicDiaryByIdOrThrow(publicDiaryId);

        List<PublicDiaryComment> comments = publicDiaryCommentRepository.findCommentsOrderByCreatedAtAsc(publicDiaryId, cursor, count);
        return GeneralPaginatedResponse.of(comments, count, PublicDiaryComment::getId, PublicDiaryCommentResponse::of);
    }

    public PublicDiaryCommentResponse editComment(User user, Long publicDiaryId, Long publicDiaryCommentId, EditPublicDiaryCommentRequest request) {
        findPublicDiaryByIdOrThrow(publicDiaryId);
        PublicDiaryComment publicDiaryComment = getPublicDiaryCommentOrThrow(publicDiaryCommentId);
        validateSameUser(publicDiaryComment.getAuthor(), user);


        PublicDiaryComment editedComment = publicDiaryComment.edit(request);

        return PublicDiaryCommentResponse.of(editedComment);
    }

    public void deleteComment(User user, Long publicDiaryId, Long publicDiaryCommentId) {
        findPublicDiaryByIdOrThrow(publicDiaryId);
        PublicDiaryComment publicDiaryComment = getPublicDiaryCommentOrThrow(publicDiaryCommentId);
        validateSameUser(publicDiaryComment.getAuthor(), user);

        publicDiaryCommentRepository.deleteChildComments(publicDiaryComment.getId());
        publicDiaryCommentRepository.delete(publicDiaryComment);
    }

    // 대댓글
    public ChildCommentResponse writeChildComment(User user, Long publicDiaryId, Long parentCommentId, WriteChildCommentRequest request) {
        PublicDiary publicDiary = findPublicDiaryByIdOrThrow(publicDiaryId);

        PublicDiaryComment parentComment = getPublicDiaryCommentOrThrow(parentCommentId);
        validateIsParentComment(parentComment);

        PublicDiaryComment childComment = request.toEntity(publicDiary, user, parentComment.getId());
        publicDiaryCommentRepository.save(childComment);

        return ChildCommentResponse.from(childComment);
    }

    public GeneralPaginatedResponse<ChildCommentResponse> getAllChildComments(Long cursor, Long count, Long publicDiaryId, Long parentCommentId) {
        findPublicDiaryByIdOrThrow(publicDiaryId);

        PublicDiaryComment parentComment = getPublicDiaryCommentOrThrow(parentCommentId);
        validateIsParentComment(parentComment);

        List<PublicDiaryComment> comments = publicDiaryCommentRepository.findChildCommentsOrderByAsc(cursor, count, parentCommentId);
        return GeneralPaginatedResponse.of(comments, count, PublicDiaryComment::getId, ChildCommentResponse::from);
    }

    public ChildCommentResponse editChildComment(User user, Long publicDiaryId, Long parentCommentId, Long childCommentId, EditPublicDiaryCommentRequest request) {
        findPublicDiaryByIdOrThrow(publicDiaryId);

        PublicDiaryComment parentComment = getPublicDiaryCommentOrThrow(parentCommentId);
        validateIsParentComment(parentComment);

        PublicDiaryComment childComment = getPublicDiaryCommentOrThrow(childCommentId);
        validateIsChildComment(childComment);
        validateSameUser(childComment.getAuthor(), user);

        PublicDiaryComment editedComment = childComment.edit(request);

        return ChildCommentResponse.from(editedComment);
    }

    public void deleteChildComment(User user, Long publicDiaryId, Long parentCommentId, Long childCommentId) {
        findPublicDiaryByIdOrThrow(publicDiaryId);

        PublicDiaryComment parentComment = getPublicDiaryCommentOrThrow(parentCommentId);
        validateIsParentComment(parentComment);

        PublicDiaryComment childComment = getPublicDiaryCommentOrThrow(childCommentId);
        validateIsChildComment(childComment);
        validateSameUser(childComment.getAuthor(), user);

        publicDiaryCommentRepository.delete(childComment);
    }

    public LikeResponse toggleLike(User user, Long publicDiaryId, Long publicDiaryCommentId) {
        findPublicDiaryByIdOrThrow(publicDiaryId);

        PublicDiaryComment publicDiaryComment = getPublicDiaryCommentOrThrow(publicDiaryCommentId);
        Optional<PublicDiaryCommentLikeLink> existingLike = publicDiaryCommentLikeLinkRepository.findByPublicDiaryCommentAndUser(publicDiaryComment, user);
        if (existingLike.isPresent()) {
            publicDiaryCommentLikeLinkRepository.delete(existingLike.get());
            publicDiaryComment.decreaseLikeCount();
            return LikeResponse.of(existingLike.get(), false);
        } else {
            PublicDiaryCommentLikeLink publicDiaryCommentLikeLink = PublicDiaryCommentLikeLink.toEntity(publicDiaryComment, user);
            publicDiaryCommentLikeLinkRepository.save(publicDiaryCommentLikeLink);
            publicDiaryComment.increaseLikeCount();
            return LikeResponse.of(publicDiaryCommentLikeLink, true);
        }
    }

    public GeneralPaginatedResponse<PaginationLikesResponse> countLikes(Long cursor, Long count, Long publicDiaryId, Long publicDiaryCommentId) {
        findPublicDiaryByIdOrThrow(publicDiaryId);
        getPublicDiaryCommentOrThrow(publicDiaryCommentId);

        List<PublicDiaryCommentLikeLink> likes = publicDiaryCommentLikeLinkRepository.findPublicDiaryCommentLikesOrderByDesc(cursor, count, publicDiaryCommentId);
        return GeneralPaginatedResponse.of(likes, count, PublicDiaryCommentLikeLink::getId, PaginationLikesResponse::from);
    }

    private void validateIsParentComment(PublicDiaryComment parentComment) {
        if (parentComment.getParentCommentId() != null) {
            throw new PublicDiaryException(PublicDiaryExceptionType.BAD_REQUEST);
        }
    }

    private PublicDiaryComment getPublicDiaryCommentOrThrow(Long publicDiaryCommentId) {
        return publicDiaryCommentRepository.findById(publicDiaryCommentId)
                .orElseThrow(() -> new PublicDiaryException(PublicDiaryExceptionType.NON_EXIST_PUBLIC_DIARY_COMMENT));
    }

    private PublicDiary findPublicDiaryByIdOrThrow(Long publicDiaryId) {
        return publicDiaryRepository.findById(publicDiaryId)
                .orElseThrow(() -> new PublicDiaryException(PublicDiaryExceptionType.NON_EXIST_PUBLIC_DIARY));
    }

    private void validateIsChildComment(PublicDiaryComment childComment) {
        if (childComment.getParentCommentId() == null) {
            throw new PublicDiaryException(PublicDiaryExceptionType.BAD_REQUEST);
        }
    }

    public PublicDiaryCommentReportResponse reportPublicDiaryComment(User reporter, Long publicDiaryId, Long publicDiaryCommentId, ReportRequest request) {
        findPublicDiaryByIdOrThrow(publicDiaryId);
        PublicDiaryComment reportedComment = getPublicDiaryCommentOrThrow(publicDiaryCommentId);

        validateSelfReport(reportedComment, reporter);
        validateDuplicateReport(reporter, reportedComment);

        PublicDiaryCommentReport publicDiaryCommentReport = request.toCommentReport(reporter, reportedComment);
        publicDiaryCommentReportRepository.save(publicDiaryCommentReport);

        return PublicDiaryCommentReportResponse.of(publicDiaryCommentReport, reporter, reportedComment);
    }

    private void validateDuplicateReport(User reporter, PublicDiaryComment reportedComment) {
        if (publicDiaryCommentReportRepository.existsByReporterAndReportedComment(reporter, reportedComment)) {
            throw new PublicDiaryException(PublicDiaryExceptionType.ALREADY_REPORT);
        }
    }

    private boolean isSameUser(User reporter, PublicDiaryComment reportedComment) {
        return reportedComment.getAuthor().getUserEmail().equals(reporter.getUserEmail());
    }

    private void validateSelfReport(PublicDiaryComment reportedComment, User reporter) {
        if (isSameUser(reporter, reportedComment)) {
            throw new PublicDiaryException(PublicDiaryExceptionType.SELF_REPORT);
        }
    }

    private void validateSameUser(User originalUser, User loggedInUser) {
        if (!loggedInUser.equals(originalUser)) {
            throw new UserException((UserExceptionType.UNAUTHORIZED_ACCESS));
        }
    }
}