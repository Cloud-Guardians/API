package com.cloudians.domain.personaldiary.service;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryCreateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionCreateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionUpdateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryUpdateRequest;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryCreateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryEmotionCreateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryEmotionUpdateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryResponse;
import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryException;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryExceptionType;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryEmotionRepository;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryRepository;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonalDiaryService {
    private final PersonalDiaryRepository personalDiaryRepository;
    private final PersonalDiaryEmotionRepository personalDiaryEmotionRepository;
    private final UserRepository userRepository;

    private Map<String, PersonalDiaryEmotion> tempEmotions = new HashMap<>();

    public PersonalDiaryEmotionCreateResponse createTempSelfEmotions(PersonalDiaryEmotionCreateRequest request, String userEmail) {
        User user = findUserByUserEmail(userEmail);
        //감정 수치 검증
        validateEmotionsValue(request);
        PersonalDiaryEmotion personalDiaryEmotion = request.toEntity(user);
        //감정들 임시저장소에 저장
        tempEmotions.put(user.getUserEmail(), personalDiaryEmotion);

        return PersonalDiaryEmotionCreateResponse.of(personalDiaryEmotion, user);
    }


    public PersonalDiaryEmotionUpdateResponse editSelfEmotions(PersonalDiaryEmotionUpdateRequest request, Long emotionId, String userEmail) {
        User user = findUserByUserEmail(userEmail);
        // 수정할 감정이 있는지 확인
        PersonalDiaryEmotion emotions = personalDiaryEmotionRepository.findByIdAndUser(emotionId, user)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
        //수정
        PersonalDiaryEmotion editedEmotions = emotions.edit(request);

        return PersonalDiaryEmotionUpdateResponse.of(editedEmotions);
    }

    public PersonalDiaryCreateResponse createPersonalDiary(PersonalDiaryCreateRequest request, String userEmail) {
        User user = findUserByUserEmail(userEmail);
        //감정 수치 가져오기
        PersonalDiaryEmotion emotions = getTempEmotion(user.getUserEmail());
        //없으면 예외처리
        validateEmotionsExistenceAndThrow(emotions);
        //이미 해당날짜에 일기 썼는지 확인
        validateDuplicateDateDiaryAndThrow(user, emotions);

        personalDiaryEmotionRepository.save(emotions);
        //감정들 임시저장소에서 삭제
        removeTempEmotion(user.getUserEmail());

        PersonalDiary personalDiary = request.toEntity(user, emotions);
        PersonalDiary savedPersonalDiary = personalDiaryRepository.save(personalDiary);

        return PersonalDiaryCreateResponse.of(savedPersonalDiary, user, emotions);
    }

    public PersonalDiaryResponse getPersonalDiary(String userEmail, Long personalDiaryId) {
        User user = findUserByUserEmail(userEmail);

        PersonalDiary personalDiary = getPersonalDiaryOrThrow(personalDiaryId, user);
        return PersonalDiaryResponse.of(personalDiary);
    }

    public PersonalDiaryResponse editPersonalDiary(PersonalDiaryUpdateRequest request, Long personalDiaryId, String userEmail) {
        User user = findUserByUserEmail(userEmail);
        // 수정할 일기가 있는지 확인
        PersonalDiary personalDiary = getPersonalDiaryOrThrow(personalDiaryId, user);

        PersonalDiary editedPersonalDiary = personalDiary.edit(request);

        return PersonalDiaryResponse.of(editedPersonalDiary);
    }

    public void deletePersonalDiary(String userEmail, Long personalDiaryId) {
        User user = findUserByUserEmail(userEmail);

        PersonalDiary personalDiary = getPersonalDiaryOrThrow(personalDiaryId, user);

        personalDiaryRepository.delete(personalDiary);
        personalDiaryEmotionRepository.delete(personalDiary.getEmotion());
    }

    private PersonalDiary getPersonalDiaryOrThrow(Long personalDiaryId, User user) {
        return personalDiaryRepository.findByIdAndUser(personalDiaryId, user)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
    }

    private void validateEmotionsValue(PersonalDiaryEmotionCreateRequest request) {
        List<Integer> emotions = Arrays.asList(
                request.getJoy(),
                request.getSadness(),
                request.getAnger(),
                request.getAnxiety(),
                request.getBoredom()
        );
        emotions.forEach(this::validateEmotionValue);
    }

    private void validateEmotionValue(int emotion) {
        if (emotion % 10 != 0) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.EMOTION_VALUE_WRONG_INPUT);
        }
    }

    private PersonalDiaryEmotion getTempEmotion(String userEmail) {
        return tempEmotions.get(userEmail);
    }

    private void removeTempEmotion(String userEmail) {
        tempEmotions.remove(userEmail);
    }


    private void validateEmotionsExistenceAndThrow(PersonalDiaryEmotion emotions) {
        if (emotions == null) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.NO_EMOTION_DATA);
        }
    }

    private void validateDuplicateDateDiaryAndThrow(User user, PersonalDiaryEmotion emotions) {
        boolean isExist = personalDiaryRepository.existsPersonalDiaryByUserAndDate(user, emotions.getDate());
        if (isExist) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.PERSONAL_DIARY_ALREADY_EXIST);
        }
    }

    private User findUserByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }

}