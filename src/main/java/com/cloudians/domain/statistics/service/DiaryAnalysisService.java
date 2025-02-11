package com.cloudians.domain.statistics.service;

import com.cloudians.domain.home.entity.SenderType;
import com.cloudians.domain.home.repository.WhisperMessageRepositoryImpl;
import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryException;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryExceptionType;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryEmotionRepository;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryRepository;
import com.cloudians.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static java.util.Map.Entry.comparingByValue;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryAnalysisService {

    private final PersonalDiaryRepository diaryRepository;
    private final PersonalDiaryEmotionRepository emotionRepository;
    private final WhisperMessageRepositoryImpl whisperRepository;


    protected long getWhisperCount(User user, LocalDate startDay, LocalDate endDay) {
        LocalDateTime start = startDay.atStartOfDay();
        LocalDateTime end = endDay.atTime(LocalTime.MAX);
        return whisperRepository.countByTimestampBetweenAndUser(user, SenderType.USER, start, end);
    }

    protected List<PersonalDiary> getPersonalDiaryList(User user, LocalDate startOfMonth, LocalDate endOfMonth) {
        return diaryRepository.findPersonalDiaryByUserAndDateBetweenOrderByDate(user, startOfMonth, endOfMonth)
                .orElseThrow(()-> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
    }


    protected PersonalDiaryEmotion findEmotionByUserAndDate(User user, LocalDate date) {
        PersonalDiaryEmotion emotion = emotionRepository.findPersonalDiaryEmotionByUserAndDate(user, date);
        return emotion;
    }
    protected PersonalDiary findDiaryByIdAndUser(User user, Long personalDiaryId) {
        return diaryRepository.findById(personalDiaryId)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));

    }
}
