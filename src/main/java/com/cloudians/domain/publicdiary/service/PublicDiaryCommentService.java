package com.cloudians.domain.publicdiary.service;

import com.cloudians.domain.home.dto.response.GeneralPaginatedResponse;
import com.cloudians.domain.publicdiary.dto.request.EditPublicDiaryCommentRequest;
import com.cloudians.domain.publicdiary.dto.request.WritePublicDiaryCommentRequest;
import com.cloudians.domain.publicdiary.dto.response.PublicDiaryCommentResponse;
import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.exception.PublicDiaryException;
import com.cloudians.domain.publicdiary.exception.PublicDiaryExceptionType;
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

@Service
@RequiredArgsConstructor
@Transactional
public class PublicDiaryCommentService {
    private final PublicDiaryCommentRepositoryImpl publicDiaryCommentRepository;
    private final PublicDiaryRepositoryImpl publicDiaryRepository;

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

        List<PublicDiaryComment> comments = publicDiaryCommentRepository.findCommentsOrderByCreatedAtDesc(publicDiaryId, cursor, count);
        return GeneralPaginatedResponse.of(comments, count, PublicDiaryComment::getId, PublicDiaryCommentResponse::of);
    }

    public PublicDiaryCommentResponse editComment(String userEmail, Long publicDiaryId, Long publicDiaryCommentId, EditPublicDiaryCommentRequest request) {
        User user = findUserByUserEmail(userEmail);

        findPublicDiaryByIdOrThrow(publicDiaryId);
        PublicDiaryComment publicDiaryComment = findPublicDiaryCommentByIdOrThrow(publicDiaryCommentId, user);

        PublicDiaryComment editedComment = publicDiaryComment.edit(request);
        return PublicDiaryCommentResponse.of(editedComment);
    }

    public void deleteComment(String userEmail, Long publicDiaryId, Long publicDiaryCommentId) {
        User user = findUserByUserEmail(userEmail);

        findPublicDiaryByIdOrThrow(publicDiaryId);
        PublicDiaryComment publicDiaryComment = findPublicDiaryCommentByIdOrThrow(publicDiaryCommentId, user);

        publicDiaryCommentRepository.delete(publicDiaryComment);
    }

    private PublicDiaryComment findPublicDiaryCommentByIdOrThrow(Long publicDiaryCommentId, User user) {
        return publicDiaryCommentRepository.findByIdAndUser(publicDiaryCommentId, user)
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
}
