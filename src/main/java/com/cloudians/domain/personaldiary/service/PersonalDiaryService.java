package com.cloudians.domain.personaldiary.service;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionCreateRequest;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryEmotionCreateResponse;
import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryException;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryExceptionType;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryEmotionRepository;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonalDiaryService {
    private final PersonalDiaryRepository personalDiaryRepository;
    private final PersonalDiaryEmotionRepository personalDiaryEmotionRepository;

    public PersonalDiaryEmotionCreateResponse createSelfEmotions(PersonalDiaryEmotionCreateRequest request) {
        List<Integer> emotions = Arrays.asList(
                request.getJoy(),
                request.getSadness(),
                request.getAnger(),
                request.getAnxiety(),
                request.getBoredom()
        );
        emotions.forEach(this::validateEmotionValue);

        PersonalDiaryEmotion personalDiaryEmotion = request.toEntity();
        PersonalDiaryEmotion savedEmotions = personalDiaryEmotionRepository.save(personalDiaryEmotion);

        return PersonalDiaryEmotionCreateResponse.of(savedEmotions);
    }

    private void validateEmotionValue(int emotion) {
        if (emotion % 10 != 0) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.WRONG_INPUT);
        }
    }
}
