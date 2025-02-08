package com.cloudians.domain.statistics.service;

import com.cloudians.domain.home.entity.WhisperMessage;
import com.cloudians.domain.home.repository.WhisperMessageRepositoryImpl;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryCreateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryResponse;
import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryException;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryExceptionType;
import com.cloudians.domain.personaldiary.repository.*;
import com.cloudians.domain.statistics.entity.WeeklyAnalysis;
import com.cloudians.domain.statistics.exception.AnalysisException;
import com.cloudians.domain.statistics.exception.AnalysisExceptionType;
import com.cloudians.domain.statistics.repository.WeeklyAnalysisRepository;
import com.cloudians.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WeeklyAnalysisService {
    private final WeeklyAnalysisRepository weeklyRepository;
    private final PersonalDiaryRepository diaryRepository;
    private final PersonalDiaryEmotionRepository emotionRepository;
    private final PersonalDiaryAnalysisRepository personalDiaryAnalysisRepository;
    private final WhisperMessageRepositoryImpl whisperRepository;



    // 다이어리 삭제 시
    @Transactional
    public void deleteDiaryEntry(User user, Long personalDiaryId) {
        PersonalDiary diary = getPersonalDiaryOrThrow(personalDiaryId);

        String yearMonth = diary.getDate().toString().substring(0,7).replace("-", "");
        int week = getWeek(diary.getDate());
        String date = yearMonth+Integer.toString(week);

        WeeklyAnalysis analysis = getWeeklyAnalysisOrThrow(user, date);
        PersonalDiaryEmotion emotion = findEmotionByUserAndDate(user, diary.getDate());
        analysis.setTotalDiary(analysis.getTotalDiary() - 1);
        analysis.subtractAnalysisEmotion(emotion);

        weeklyRepository.save(analysis);
    }

    @Transactional
    public void updateDiaryEntry(User user, PersonalDiaryResponse response) {;

        PersonalDiaryEmotion emotion = findEmotionByUserAndDate(user, response.getDate());
        String yearMonth = response.getDate().toString().substring(0,7).replace("-", "");
        int week = getWeek(response.getDate());
        String date = yearMonth+Integer.toString(week);

        WeeklyAnalysis analysis = getWeeklyAnalysisOrThrow(user, date);

        analysis.setTotalDiary(analysis.getTotalDiary() + 1);

        analysis.addAnalysisEmotion(emotion);
        weeklyRepository.save(analysis);
    }


    @Transactional
    public void addDiaryEntry(User user, PersonalDiaryCreateResponse diary) {
        String yearMonth = diary.getDate().toString().substring(0,7).replace("-", "");
        int week = getWeek(diary.getDate());
        String date  = yearMonth+Integer.toString(week);
        System.out.println(yearMonth);
        WeeklyAnalysis analysis = weeklyRepository.findByUserAndWeeklyDate(user, date)
                .orElseGet(()-> {
                    WeeklyAnalysis newAnalysis = new WeeklyAnalysis();
                    newAnalysis.setUser(user);
                    newAnalysis.setWeeklyDate(date);
                    return weeklyRepository.save(newAnalysis);
                });
        analysis.setTotalDiary(analysis.getTotalDiary() + 1);
        PersonalDiaryEmotion emotion = findEmotionByUserAndDate(user, diary.getDate());
        analysis.addAnalysisEmotion(emotion);
        weeklyRepository.save(analysis);
    }

    // 주간 통계 업데이트
    public Map<String, Object> getWeeklyAnalysis(User user, String yearMonth, String week) {
        List<PersonalDiary> diaryList = getWeeklyDiaryList(user, yearMonth, week);
        List<Map.Entry<Object, Long>> elementList = getWeeklyMostElement(user, diaryList);
        WeeklyAnalysis anal = getWeeklyAnalysisOrThrow(user, yearMonth+week);

        Map<String, Object> map = new HashMap<>();
        map.put("response", anal.toDto());
        map.put("list", elementList);

        return map;
    }

    // yyyy-MM
    public int getWeek(LocalDate date) {
        return date.get(WeekFields.ISO.weekOfMonth());
    }

    private List<WhisperMessage> getWhisperList(User user) {
        List<WhisperMessage> list = whisperRepository.findListByUser(user);
        return list;
    }

    private List<WhisperMessage> getWeeklyWhisperList(User user, String yearMonth, String week) {
        List<WhisperMessage> totalList = getWhisperList(user);
        List<WhisperMessage> weeklyList = new ArrayList<>();

        for (WhisperMessage message : totalList) {
            System.out.println(message.getTimestamp());
            String whisperMonth = message.getTimestamp().toString().split("-")[0] + message.getTimestamp().toString().split("-")[1];
            String day = message.getTimestamp().toString().split("-")[2].substring(0, 2);
            String weekly = null;
            // rhcu
            System.out.println(whisperMonth);
            if (yearMonth.equals(whisperMonth) && weekly.equals(week)) {
                weeklyList.add(message);
            }
        }
        return weeklyList;
    }

    // 주간 위스퍼 답변 수 
    private int getWeeklyDiaryAndAnswer(User user, String yearMonth, String week) {
        List<WhisperMessage> whisperList = getWeeklyWhisperList(user, yearMonth, week);
        int totalWhisper = whisperList.size();
        return totalWhisper;
    }

    // 주간 평균 감정
    private List<Map.Entry<Object, Long>> getWeeklyMostElement(User user, List<PersonalDiary> diaryList) {

        List<String> elementList = new ArrayList<>();
        for (PersonalDiary diary : diaryList) {
            PersonalDiaryAnalysis analysis = personalDiaryAnalysisRepository.findByPersonalDiaryId(diary.getId()).get();
            elementList.add(analysis.getFiveElement().getName());
        }

        Map<Object, Long> frequencyMap = elementList.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        Optional<Map.Entry<Object, Long>> mostFrequent = frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        Map.Entry<Object, Long> entry = mostFrequent
                .orElseThrow(() -> new AnalysisException(AnalysisExceptionType.ELEMENT_LIST_NOT_FOUND));

        if (frequencyMap.size() < 3) {
            List<Map.Entry<Object, Long>> list = frequencyMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(frequencyMap.size())
                    .collect(Collectors.toList());
            return list;
        }

        List<Map.Entry<Object, Long>> list = frequencyMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .collect(Collectors.toList());
        return list;
    }


    private PersonalDiaryEmotion findEmotionByUserAndDate(User user, LocalDate date) {
        PersonalDiaryEmotion emotion = emotionRepository.findPersonalDiaryEmotionByUserAndDate(user, date);
        return emotion;
    }

    // 특정 주 다이어리 가져오기
    private List<PersonalDiary> getWeeklyDiaryList(User user, String yearMonth, String week) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = yearMonth+"01";
        LocalDate baseDate = LocalDate.parse(dateString, formatter);

        int weekNumber = Integer.parseInt(week);
        LocalDate startOfWeek = baseDate.with(DayOfWeek.MONDAY)
                .plusWeeks(weekNumber - 1);
        LocalDate endOfWeek = startOfWeek.with(DayOfWeek.SUNDAY);
        System.out.println(startOfWeek);
        System.out.println(endOfWeek);
        List<PersonalDiary> diaryList = diaryRepository.findPersonalDiaryByUserAndDateBetweenOrderByDate(user, startOfWeek, endOfWeek)
                .orElseThrow(()-> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
        System.out.println(diaryList.toString()+": getWeeklyDiaryList");
        return diaryList;
    }

    private PersonalDiary getPersonalDiaryOrThrow(Long personalDiaryId) {
        return diaryRepository.findById(personalDiaryId)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
    }

    private WeeklyAnalysis getWeeklyAnalysisOrThrow(User user, String weeklyDate) {
        return weeklyRepository.findByUserAndWeeklyDate(user, weeklyDate)
                .orElseThrow(()-> new AnalysisException(AnalysisExceptionType.WEEKLY_ANALYSIS_NOT_FOUND));
    }

    // 다음 주 예측 및 조언


}
