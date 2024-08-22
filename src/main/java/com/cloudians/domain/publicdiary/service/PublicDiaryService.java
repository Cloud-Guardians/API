package com.cloudians.domain.publicdiary.service;

import com.cloudians.domain.home.dto.response.GeneralPaginatedResponse;
import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryException;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryExceptionType;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryRepository;
import com.cloudians.domain.publicdiary.dto.response.PublicDiaryResponse;
import com.cloudians.domain.publicdiary.dto.response.PublicDiaryThumbnailResponse;
import com.cloudians.domain.publicdiary.entity.comment.PublicDiaryComment;
import com.cloudians.domain.publicdiary.entity.diary.PublicDiary;
import com.cloudians.domain.publicdiary.entity.diary.SearchCondition;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PublicDiaryService {
    private final PublicDiaryRepositoryImpl publicDiaryRepository;
    private final PublicDiaryCommentRepositoryImpl publicDiaryCommentRepository;
    private final PersonalDiaryRepository personalDiaryRepository;

    private final UserRepository userRepository;

    public PublicDiaryResponse createPublicDiary(String userEmail, Long personalDiaryId) {
        User user = findUserByUserEmail(userEmail);
        PersonalDiary personalDiary = getPersonalDiaryOrThrow(personalDiaryId, user);
        validateIfPublicDiaryExists(personalDiaryId);

        PublicDiary publicDiary = PublicDiary.createPublicDiary(user, personalDiary);
        publicDiaryRepository.save(publicDiary);

        return PublicDiaryResponse.of(publicDiary, user);
    }

    public GeneralPaginatedResponse<PublicDiaryThumbnailResponse> getPublicDiariesByKeyword(String userEmail, Long cursor, Long count, String searchType, String keyword) {
        findUserByUserEmail(userEmail);
        SearchCondition condition = SearchCondition.of(searchType, keyword);
        List<PublicDiary> searchedPublicDiaries = publicDiaryRepository.searchByTypeAndKeywordOrderByTimestampDesc(condition, cursor, count);
        List<PublicDiaryThumbnailResponse> thumbnailResponses = getPublicDiaryThumbnailResponses(searchedPublicDiaries);

        return GeneralPaginatedResponse.of(searchedPublicDiaries, count, PublicDiary::getId,
                diary -> thumbnailResponses.get(searchedPublicDiaries.indexOf(diary))
        );
    }

    public GeneralPaginatedResponse<PublicDiaryThumbnailResponse> getAllPublicDiaries(String userEmail, Long cursor, Long count) {
        findUserByUserEmail(userEmail);
        List<PublicDiary> publicDiaries = publicDiaryRepository.publicDiariesOrderByCreatedAtDescWithTop3Diaries(cursor, count);
        List<PublicDiaryThumbnailResponse> thumbnailResponses = getPublicDiaryThumbnailResponses(publicDiaries);

        return GeneralPaginatedResponse.of(publicDiaries, count, PublicDiary::getId,
                diary -> thumbnailResponses.get(publicDiaries.indexOf(diary))
        );
    }

    private List<PublicDiaryThumbnailResponse> getPublicDiaryThumbnailResponses(List<PublicDiary> publicDiaries) {
        return publicDiaries.stream()
                .map(publicDiary -> {
                    Long commentsCount = getCommentsCount(publicDiary);
                    return PublicDiaryThumbnailResponse.of(publicDiary, commentsCount);
                })
                .collect(Collectors.toList());
    }

    private Long getCommentsCount(PublicDiary publicDiary) {
        return publicDiaryCommentRepository.getPublicDiaryCommentsCount(publicDiary);
    }

    public PublicDiaryResponse getPublicDiary(String userEmail, Long publicDiaryId) {
        User user = findUserByUserEmail(userEmail);

        PublicDiary publicDiary = findByIdOrThrow(publicDiaryId);
        publicDiary.updateView(publicDiary.getViews());
        return PublicDiaryResponse.of(publicDiary, user);
    }

    public void deletePublicDiary(String userEmail, Long publicDiaryId) {
        User user = findUserByUserEmail(userEmail);
        PublicDiary publicDiary = getPublicDiaryOrThrow(publicDiaryId, user);

        publicDiaryRepository.delete(publicDiary);
        List<PublicDiaryComment> commentsInPublicDiary = publicDiaryCommentRepository.findByPublicDiary(publicDiary);
        publicDiaryCommentRepository.deleteAll(commentsInPublicDiary);
    }

    private void validateIfPublicDiaryExists(Long personalDiaryId) {
        if (publicDiaryRepository.existsByPersonalDiaryId(personalDiaryId)) {
            throw new PublicDiaryException(PublicDiaryExceptionType.PUBLIC_DIARY_ALREADY_EXIST);
        }
    }

    private PersonalDiary getPersonalDiaryOrThrow(Long personalDiaryId, User user) {
        return personalDiaryRepository.findByIdAndUser(personalDiaryId, user)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
    }

    private PublicDiary getPublicDiaryOrThrow(Long publicDiaryId, User user) {
        return publicDiaryRepository.findByIdAndUser(publicDiaryId, user)
                .orElseThrow(() -> new PublicDiaryException(PublicDiaryExceptionType.NON_EXIST_PUBLIC_DIARY));
    }

    private User findUserByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }

    private PublicDiary findByIdOrThrow(Long publicDiaryId) {
        return publicDiaryRepository.findById(publicDiaryId)
                .orElseThrow(() -> new PublicDiaryException(PublicDiaryExceptionType.NON_EXIST_PUBLIC_DIARY));
    }
}
