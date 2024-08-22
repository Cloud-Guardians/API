package com.cloudians.domain.publicdiary.service;

import com.cloudians.domain.home.dto.response.GeneralPaginatedResponse;
import com.cloudians.domain.publicdiary.dto.request.EditPublicDiaryCommentRequest;
import com.cloudians.domain.publicdiary.dto.request.WriteChildCommentRequest;
import com.cloudians.domain.publicdiary.dto.request.WritePublicDiaryCommentRequest;
import com.cloudians.domain.publicdiary.dto.response.ChildCommentResponse;
import com.cloudians.domain.publicdiary.dto.response.LikeResponse;
import com.cloudians.domain.publicdiary.dto.response.PaginationLikesResponse;
import com.cloudians.domain.publicdiary.dto.response.PublicDiaryCommentResponse;
import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.like.PublicDiaryCommentLikeLink;
import com.cloudians.domain.publicdiary.exception.PublicDiaryException;
import com.cloudians.domain.publicdiary.exception.PublicDiaryExceptionType;
import com.cloudians.domain.publicdiary.repository.PublicDiaryCommentLikeLinkRepositoryImpl;
import com.cloudians.domain.publicdiary.repository.PublicDiaryCommentRepositoryImpl;
import com.cloudians.domain.publicdiary.repository.PublicDiaryRepositoryImpl;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PublicDiaryCommentService {
    private final PublicDiaryCommentRepositoryImpl publicDiaryCommentRepository;
    private final PublicDiaryRepositoryImpl publicDiaryRepository;
    private final PublicDiaryCommentLikeLinkRepositoryImpl publicDiaryCommentLikeLinkRepository;

    private final UserRepository userRepository;

    public PublicDiaryCommentResponse writeComment(String userEmail, Long publicDiaryId, WritePublicDiaryCommentRequest request) {
        User author = findUserByUserEmail(userEmail);

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

    public PublicDiaryCommentResponse editComment(String userEmail, Long publicDiaryId, Long publicDiaryCommentId, EditPublicDiaryCommentRequest request) {
        User user = findUserByUserEmail(userEmail);

        findPublicDiaryByIdOrThrow(publicDiaryId);
        PublicDiaryComment publicDiaryComment = findPublicDiaryCommentByIdAndUserOrThrow(publicDiaryCommentId, user);

        PublicDiaryComment editedComment = publicDiaryComment.edit(request);
        return PublicDiaryCommentResponse.of(editedComment);
    }

    public void deleteComment(String userEmail, Long publicDiaryId, Long publicDiaryCommentId) {
        User user = findUserByUserEmail(userEmail);

        findPublicDiaryByIdOrThrow(publicDiaryId);
        PublicDiaryComment publicDiaryComment = findPublicDiaryCommentByIdAndUserOrThrow(publicDiaryCommentId, user);

        publicDiaryCommentRepository.deleteChildComments(publicDiaryComment.getId());
        publicDiaryCommentRepository.delete(publicDiaryComment);
    }

    // 대댓글
    public ChildCommentResponse writeChildComment(String userEmail, Long publicDiaryId, Long parentCommentId, WriteChildCommentRequest request) {
        User user = findUserByUserEmail(userEmail);
        PublicDiary publicDiary = findPublicDiaryByIdOrThrow(publicDiaryId);

        PublicDiaryComment parentComment = findPublicDiaryCommentByIdOrThrow(parentCommentId);
        validateIsParentComment(parentComment);

        PublicDiaryComment childComment = request.toEntity(publicDiary, user, parentComment.getId());
        publicDiaryCommentRepository.save(childComment);

        return ChildCommentResponse.of(childComment);
    }

    public GeneralPaginatedResponse<ChildCommentResponse> getAllChildComments(Long cursor, Long count, Long publicDiaryId, Long parentCommentId) {
        findPublicDiaryByIdOrThrow(publicDiaryId);

        PublicDiaryComment parentComment = findPublicDiaryCommentByIdOrThrow(parentCommentId);
        validateIsParentComment(parentComment);

        List<PublicDiaryComment> comments = publicDiaryCommentRepository.findChildCommentsOrderByAsc(cursor, count, parentCommentId);
        return GeneralPaginatedResponse.of(comments, count, PublicDiaryComment::getId, ChildCommentResponse::of);
    }

    public ChildCommentResponse editChildComment(String userEmail, Long publicDiaryId, Long parentCommentId, Long childCommentId, EditPublicDiaryCommentRequest request) {
        User user = findUserByUserEmail(userEmail);
        findPublicDiaryByIdOrThrow(publicDiaryId);

        PublicDiaryComment parentComment = findPublicDiaryCommentByIdOrThrow(parentCommentId);
        validateIsParentComment(parentComment);

        PublicDiaryComment childComment = findPublicDiaryCommentByIdOrThrow(childCommentId);
        validateIsChildComment(childComment);
        validateSameUser(user, childComment);

        PublicDiaryComment editedComment = childComment.edit(request);

        return ChildCommentResponse.of(editedComment);
    }

    public void deleteChildComment(String userEmail, Long publicDiaryId, Long parentCommentId, Long childCommentId) {
        User user = findUserByUserEmail(userEmail);

        findPublicDiaryByIdOrThrow(publicDiaryId);

        PublicDiaryComment parentComment = findPublicDiaryCommentByIdOrThrow(parentCommentId);
        validateIsParentComment(parentComment);

        PublicDiaryComment childComment = findPublicDiaryCommentByIdOrThrow(childCommentId);
        validateIsChildComment(childComment);
        validateSameUser(user, childComment);

        publicDiaryCommentRepository.delete(childComment);
    }

    public LikeResponse toggleLike(String userEmail, Long publicDiaryId, Long publicDiaryCommentId) {
        User user = findUserByUserEmail(userEmail);
        findPublicDiaryByIdOrThrow(publicDiaryId);

        PublicDiaryComment publicDiaryComment = findPublicDiaryCommentByIdOrThrow(publicDiaryCommentId);
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
        findPublicDiaryCommentByIdOrThrow(publicDiaryCommentId);

        List<PublicDiaryCommentLikeLink> likes = publicDiaryCommentLikeLinkRepository.findPublicDiaryCommentLikesOrderByDesc(cursor, count, publicDiaryCommentId);
        return GeneralPaginatedResponse.of(likes, count, PublicDiaryCommentLikeLink::getId, PaginationLikesResponse::from);
    }

    private void validateIsParentComment(PublicDiaryComment parentComment) {
        if (parentComment.getParentCommentId() != null) {
            throw new PublicDiaryException(PublicDiaryExceptionType.BAD_REQUEST);
        }
    }

    private PublicDiaryComment findPublicDiaryCommentByIdAndUserOrThrow(Long publicDiaryCommentId, User user) {
        return publicDiaryCommentRepository.findByIdAndUser(publicDiaryCommentId, user)
                .orElseThrow(() -> new PublicDiaryException(PublicDiaryExceptionType.NON_EXIST_PUBLIC_DIARY_COMMENT));
    }

    private PublicDiaryComment findPublicDiaryCommentByIdOrThrow(Long publicDiaryCommentId) {
        return publicDiaryCommentRepository.findById(publicDiaryCommentId)
                .orElseThrow(() -> new PublicDiaryException(PublicDiaryExceptionType.NON_EXIST_PUBLIC_DIARY_COMMENT));
    }

    private PublicDiary findPublicDiaryByIdOrThrow(Long publicDiaryId) {
        return publicDiaryRepository.findById(publicDiaryId)
                .orElseThrow(() -> new PublicDiaryException(PublicDiaryExceptionType.NON_EXIST_PUBLIC_DIARY));
    }

    private User findUserByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }

    private void validateSameUser(User user, PublicDiaryComment childComment) {
        if (childComment.getAuthor() != user) {
            throw new PublicDiaryException(PublicDiaryExceptionType.FORBIDDEN_USER);
        }
    }

    private void validateIsChildComment(PublicDiaryComment childComment) {
        if (childComment.getParentCommentId() == null) {
            throw new PublicDiaryException(PublicDiaryExceptionType.BAD_REQUEST);
        }
    }
}
