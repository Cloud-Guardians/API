package com.cloudians.domain.statistics.service;


import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryCreateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryResponse;
import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import com.cloudians.domain.statistics.dto.response.WeeklyAnalysisResponse;
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

@Service
@RequiredArgsConstructor
@Transactional
public class WeeklyAnalysisService {
    private final WeeklyAnalysisRepository weeklyRepository;

    private final ElementAnalysisService elementAnalysisService;
    private final DiaryAnalysisService diaryAnalysisService;


    // 다이어리 삭제 시
    @Transactional
    public void deleteDiaryEntry(User user, Long personalDiaryId) {
        PersonalDiary diary = diaryAnalysisService.findDiaryByIdAndUser(user, personalDiaryId);

        String yearMonth = diary.getDate().toString().substring(0,7).replace("-", "");
        String week = getWeek(diary.getDate());


        WeeklyAnalysis analysis = getWeeklyAnalysisOrThrow(user, yearMonth,week);
        PersonalDiaryEmotion emotion = diaryAnalysisService.findEmotionByUserAndDate(user, diary.getDate());
        analysis.updateDiaryCount(-1);
        analysis.subtractAnalysisEmotion(emotion);

        weeklyRepository.save(analysis);
    }

    @Transactional
    public void updateDiaryEntry(User user, LocalDate date) {;

        PersonalDiaryEmotion emotion = diaryAnalysisService.findEmotionByUserAndDate(user, date);

        String yearMonth = date.toString().substring(0,7).replace("-", "");
        String week = getWeek(date);

        WeeklyAnalysis analysis = getWeeklyAnalysisOrThrow(user, yearMonth, week);

       analysis.updateDiaryCount(1);
        analysis.addAnalysisEmotion(emotion);
        weeklyRepository.save(analysis);
    }


    @Transactional
    public void addDiaryEntry(User user, LocalDate date) {
        String yearMonth = date.toString().substring(0,7).replace("-", "");
        String week = getWeek(date);
        String weeklyDate  = yearMonth+week;


        WeeklyAnalysis analysis = weeklyRepository.findByUserAndWeeklyDate(user, weeklyDate)
                .orElseGet(()-> {
                    WeeklyAnalysis newAnalysis = WeeklyAnalysis.builder()
                            .user(user)
                            .weeklyDate(weeklyDate)
                            .totalDiary(0)
                            .totalAnswer(0)
                            .build();
                    return weeklyRepository.save(newAnalysis);
                });
        analysis.updateWhisperCount(1);
        PersonalDiaryEmotion emotion = diaryAnalysisService.findEmotionByUserAndDate(user, date);
        analysis.addAnalysisEmotion(emotion);
        weeklyRepository.save(analysis);
    }

    // 주간 통계 업데이트
    public Map<String, Object> getWeeklyAnalysis(User user, String year, String month, String week) {
        weeklyWhisperUpdate(user, year, month, week);

        List<PersonalDiary> diaryList = getWeeklyDiaryList(user, year, month, week);
        List<String> elementList = elementAnalysisService.getElementList(diaryList);

        List<Map.Entry<String, Long>> frequentElement = elementAnalysisService.getMostFrequentElement(elementList);
        FiveElement mostElement = elementAnalysisService.getElementCharacter(frequentElement, "MAX");
        List<String> mostElementCharacters = elementAnalysisService.getCharactersList(mostElement);

        String yearMonth = year+month;
        WeeklyAnalysis anal = getWeeklyAnalysisOrThrow(user, yearMonth, week);
        WeeklyAnalysisResponse response = WeeklyAnalysisResponse.of(anal);

        Map<String, Object> map = new HashMap<>();
        map.put("response", response);
        map.put("list", elementList);
        map.put("mostElement", mostElementCharacters);
        return map;
    }

    // yyyy-MM
    public String getWeek(LocalDate date) {
        int week = date.get(WeekFields.ISO.weekOfMonth());
        return Integer.toString(week);
    }

//    // 주간 평균 감정
//    private List<Map.Entry<Object, Long>> getWeeklyMostElement(User user, List<PersonalDiary> diaryList) {
//        List<String> elementList = new ArrayList<>();
//        for (PersonalDiary diary : diaryList) {
//            PersonalDiaryAnalysis analysis = personalDiaryAnalysisRepository.findByPersonalDiaryId(diary.getId()).get();
//            elementList.add(analysis.getFiveElement().getName());
//        }
//
//        Map<Object, Long> frequencyMap = elementList.stream()
//                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
//
//        Optional<Map.Entry<Object, Long>> mostFrequent = frequencyMap.entrySet().stream()
//                .max(Map.Entry.comparingByValue());
//
//        Map.Entry<Object, Long> entry = mostFrequent
//                .orElseThrow(() -> new AnalysisException(AnalysisExceptionType.ELEMENT_LIST_NOT_FOUND));
//
//        if (frequencyMap.size() < 3) {
//            List<Map.Entry<Object, Long>> list = frequencyMap.entrySet().stream()
//                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                    .limit(frequencyMap.size())
//                    .collect(Collectors.toList());
//            return list;
//        }
//
//        List<Map.Entry<Object, Long>> list = frequencyMap.entrySet().stream()
//                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
//                .limit(3)
//                .collect(Collectors.toList());
//        return list;
//    }

    private Map<String, LocalDate> convertToLocalDate(String year, String month, String week) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = year+month+"01";
        LocalDate baseDate = LocalDate.parse(dateString, formatter);

        int weekNumber = Integer.parseInt(week);
        LocalDate startOfWeek = baseDate.with(DayOfWeek.MONDAY)
                .plusWeeks(weekNumber - 1);
        LocalDate endOfWeek = startOfWeek.with(DayOfWeek.SUNDAY);

        Map<String, LocalDate> dateMap = new HashMap<>();
        dateMap.put("start", startOfWeek);
        dateMap.put("end", endOfWeek);
        return dateMap;
    }

    private void weeklyWhisperUpdate(User user, String year, String month, String week) {
        Map<String, LocalDate> dateMap = convertToLocalDate(year, month, week);

        LocalDate start = dateMap.get("start");
        LocalDate end = dateMap.get("end");

        String yearMonth = year + month;

        long count = diaryAnalysisService.getWhisperCount(user, start, end);
        WeeklyAnalysis analysis = getWeeklyAnalysisOrThrow(user, yearMonth,week);
        analysis.updateWhisperCount((int) count);
        weeklyRepository.save(analysis);
    }

    // 특정 주 다이어리 가져오기
    private List<PersonalDiary> getWeeklyDiaryList(User user, String year, String month, String week) {


        Map<String, LocalDate> dateMap = convertToLocalDate(year, month, week);

        LocalDate startOfWeek = dateMap.get("start");
        LocalDate endOfWeek = dateMap.get("end");

        return diaryAnalysisService.getPersonalDiaryList(user, startOfWeek, endOfWeek);
    }

    private WeeklyAnalysis getWeeklyAnalysisOrThrow(User user, String yearMonth, String week) {
        String weeklyDate = yearMonth+week;
        return weeklyRepository.findByUserAndWeeklyDate(user, weeklyDate)
                .orElseThrow(()-> new AnalysisException(AnalysisExceptionType.WEEKLY_ANALYSIS_NOT_FOUND));
    }

    // 다음 주 예측 및 조언


}
