package com.cloudians.domain.personaldiary.service;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryCreateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionCreateRequest;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryCreateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryEmotionCreateResponse;
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

        List<Integer> emotions = Arrays.asList(
                request.getJoy(),
                request.getSadness(),
                request.getAnger(),
                request.getAnxiety(),
                request.getBoredom()
        );
        emotions.forEach(this::validateEmotionValue);

        PersonalDiaryEmotion personalDiaryEmotion = request.toEntity(user);
        tempEmotions.put(user.getUserEmail(), personalDiaryEmotion);

//        PersonalDiaryEmotion savedEmotions = personalDiaryEmotionRepository.save(personalDiaryEmotion);

        return PersonalDiaryEmotionCreateResponse.of(personalDiaryEmotion, user);
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
        if (emotions == null) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.NO_EMOTION_DATA);
        }
        boolean isExist = personalDiaryRepository.existsPersonalDiaryByUserAndDate(user, emotions.getDate());
        if (isExist) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.PERSONAL_DIARY_ALREADY_EXIST);
        }
        personalDiaryEmotionRepository.save(emotions);
        removeTempEmotion(user.getUserEmail());
        PersonalDiary personalDiary = request.toEntity(user, emotions);
        PersonalDiary savedPersonalDiary = personalDiaryRepository.save(personalDiary);
        return PersonalDiaryCreateResponse.of(savedPersonalDiary, user, emotions);
    }

    private User findUserByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(
                        () -> new UserException(UserExceptionType.USER_NOT_FOUND)
                );
    }
}
