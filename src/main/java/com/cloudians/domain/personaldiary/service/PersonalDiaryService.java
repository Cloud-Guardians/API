package com.cloudians.domain.personaldiary.service;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryCreateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionRequest;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryCreateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryEmotionResponse;
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


    public PersonalDiaryEmotionResponse createTempSelfEmotions(PersonalDiaryEmotionRequest request, String userEmail) {
        User user = findUserByUserEmail(userEmail);

        validateEmotionsValue(request);

        PersonalDiaryEmotion personalDiaryEmotion = request.toEntity(user);
        tempEmotions.put(user.getUserEmail(), personalDiaryEmotion);

        return PersonalDiaryEmotionResponse.of(personalDiaryEmotion, user);
    }

    private void validateEmotionsValue(PersonalDiaryEmotionRequest request) {
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
            throw new PersonalDiaryException(PersonalDiaryExceptionType.WRONG_INPUT);
        }
    }

    public PersonalDiaryEmotion getTempEmotion(String userEmail) {
        return tempEmotions.get(userEmail);
    }

    public void removeTempEmotion(String userEmail) {
        tempEmotions.remove(userEmail);
    }

    public PersonalDiaryCreateResponse createPersonalDiary(PersonalDiaryCreateRequest request, String userEmail) {
        User user = findUserByUserEmail(userEmail);

        PersonalDiaryEmotion emotions = getTempEmotion(user.getUserEmail());
        validateEmotionsExistenceAndThrow(emotions);
        validateDuplicateDateDiaryAndThrow(user, emotions);

        personalDiaryEmotionRepository.save(emotions);
        removeTempEmotion(user.getUserEmail());
        PersonalDiary personalDiary = request.toEntity(user, emotions);
        PersonalDiary savedPersonalDiary = personalDiaryRepository.save(personalDiary);

        return PersonalDiaryCreateResponse.of(savedPersonalDiary, user, emotions);
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

    public PersonalDiaryEmotionResponse editSelfEmotions(PersonalDiaryEmotionRequest request, Long emotionId, String userEmail) {
        User user = findUserByUserEmail(userEmail);
        // 수정할 감정이 있는지 확인
        PersonalDiaryEmotion emotions = personalDiaryEmotionRepository.findByIdAndUser(emotionId, user)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
        validateEmotionsValue(request);
        //수정
        PersonalDiaryEmotion editedEmotions = emotions.edit(request);

        return PersonalDiaryEmotionResponse.of(editedEmotions, user);
    }
}
