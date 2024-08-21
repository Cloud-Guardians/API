package com.cloudians.domain.publicdiary.service;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryException;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryExceptionType;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryRepository;
import com.cloudians.domain.publicdiary.dto.response.PublicDiaryCreateResponse;
import com.cloudians.domain.publicdiary.entity.PublicDiary;
import com.cloudians.domain.publicdiary.exception.PublicDiaryException;
import com.cloudians.domain.publicdiary.exception.PublicDiaryExceptionType;
import com.cloudians.domain.publicdiary.repository.PublicDiaryRepository;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PublicDiaryService {
    private final PublicDiaryRepository publicDiaryRepository;
    private final PersonalDiaryRepository personalDiaryRepository;

    private final UserRepository userRepository;

    public PublicDiaryCreateResponse createPublicDiary(String userEmail, Long personalDiaryId) {
        User user = findUserByUserEmail(userEmail);
        PersonalDiary personalDiary = getPersonalDiaryOrThrow(personalDiaryId, user);
        validateIfPublicDiaryExists(personalDiaryId);

        PublicDiary publicDiary = PublicDiary.createPublicDiary(user, personalDiary);
        publicDiaryRepository.save(publicDiary);

        return PublicDiaryCreateResponse.of(publicDiary, user);
    }

    public void deletePublicDiary(String userEmail, Long publicDiaryId) {
        User user = findUserByUserEmail(userEmail);
        PublicDiary publicDiary = getPublicDiaryOrThrow(publicDiaryId, user);

        publicDiaryRepository.delete(publicDiary);
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
}
