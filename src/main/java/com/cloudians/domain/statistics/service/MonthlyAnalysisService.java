package com.cloudians.domain.statistics.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.transaction.Transactional;

import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryCreateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryResponse;
import com.cloudians.domain.statistics.dto.response.MonthlyAnalysisResponse;
import com.cloudians.domain.statistics.repository.MonthlyAnalysisRepositoryImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import com.cloudians.domain.statistics.entity.MonthlyAnalysis;
import com.cloudians.domain.statistics.exception.AnalysisException;
import com.cloudians.domain.statistics.repository.MonthlyAnalysisJPARepository;
import com.cloudians.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

import static com.cloudians.domain.statistics.exception.AnalysisExceptionType.MONTHLY_ANALYSIS_NOT_FOUND;
import static java.util.Map.Entry.comparingByValue;

@Service
@RequiredArgsConstructor
@Transactional
@CacheConfig(cacheNames = "monthlyAnalysis")
public class MonthlyAnalysisService {


    private final MonthlyAnalysisJPARepository monthlyAnalysisJPARepository;
    private final MonthlyAnalysisRepositoryImpl monthlyAnalysisRepository;

    private final ElementAnalysisService elementAnalysisService;
    private final DiaryAnalysisService diaryAnalysisService;

    @CacheEvict(key = "#user.userEmail + #yearMonth")
    @Transactional
    public void deleteDiaryEntry(User user, Long personalDiaryId) {
        PersonalDiary diary = diaryAnalysisService.findDiaryByIdAndUser(user, personalDiaryId);
        String year = getYearMonthMap(diary.getDate()).get("year");
        String month = getYearMonthMap(diary.getDate()).get("month");
        String yearMonth = year + month;
        MonthlyAnalysis anal = getOrCreateMonthlyAnalysis(user, yearMonth);
        PersonalDiaryEmotion emotion = diaryAnalysisService.findEmotionByUserAndDate(user, diary.getDate());
        anal.subtractAnalysisEmotion(emotion);
        monthlyAnalysisJPARepository.save(anal);
    }

    @CachePut(key = "#user.userEmail + #yearMonth")
    @Transactional
    public void updateDiaryEntry(User user, LocalDate date) {
        String yearMonth = getYearMonth(date);
        PersonalDiaryEmotion emotion = diaryAnalysisService.findEmotionByUserAndDate(user, date);
        MonthlyAnalysis analysis = getOrCreateMonthlyAnalysis(user, yearMonth);
        analysis.addAnalysisEmotion(emotion);
        monthlyAnalysisJPARepository.save(analysis);
    }

    @Transactional
    public void addDiaryEntry(User user, LocalDate date) {
        String yearMonth = getYearMonth(date);
        MonthlyAnalysis analysis = monthlyAnalysisRepository.findByUserAndMonthlyDate(user, yearMonth)
                .orElseGet(()-> {
                    MonthlyAnalysis newAnalysis = MonthlyAnalysis.builder()
                            .user(user)
                            .monthlyDate(yearMonth)
                            .totalDiary(0)
                            .totalAnswer(0)
                            .build();
                    return monthlyAnalysisJPARepository.save(newAnalysis);
                });
        PersonalDiaryEmotion emotion = diaryAnalysisService.findEmotionByUserAndDate(user, date);
        analysis.addAnalysisEmotion(emotion);
        monthlyAnalysisJPARepository.save(analysis);
    }

    // 월간 통계 제공
    @Cacheable(key = "#user.userEmail + #yearMonth")
    public MonthlyAnalysis getMonthlyAnalysis(User user, String yearMonth) {
        return monthlyAnalysisJPARepository.findByUserAndMonthlyDate(user, yearMonth)
                .orElseThrow(() -> new AnalysisException(MONTHLY_ANALYSIS_NOT_FOUND));
    }

    // 월간 정리
    public Map<String, Object> getMonthlyReport(User user, MonthlyAnalysis analysis) {

        String yearMonth = analysis.getMonthlyDate();
        Map<String, LocalDate> dateMap = convertToLocalDate(yearMonth);
        LocalDate start = dateMap.get("start");
        LocalDate end = dateMap.get("end");

        monthlyWhisperUpdate(user, start, end, analysis);

        List<PersonalDiary> diaryList = getMonthlyDiaryList(user, start, end);
        List<Map.Entry<String, Long>> elementList = getMonthlyMostElement(user, diaryList);

        FiveElement max = elementAnalysisService.getElementCharacter(elementList, "MAX");
        FiveElement min = elementAnalysisService.getElementCharacter(elementList, "MIN");

        List<String> maxCharacters = elementAnalysisService.getCharactersList(max);
        List<String> minCharacters = elementAnalysisService.getCharactersList(min);

       MonthlyAnalysisResponse response = MonthlyAnalysisResponse.of(analysis, max);

        Map<String, Object> elementMap = new HashMap<>();
        elementMap.put("response",response);
        elementMap.put("max", max);
        elementMap.put("min", min);
        elementMap.put("maxCharacters", maxCharacters);
        elementMap.put("minCharacters", minCharacters);
        return elementMap;
    }

    // 월간 평균 기운 가져오기
    private List<Map.Entry<String, Long>> getMonthlyMostElement(User user, List<PersonalDiary> diaryList) {
        List<String> elementList = elementAnalysisService.getElementList(diaryList);
        return elementAnalysisService.getMostFrequentElement(elementList);
    }

    private String getYearMonth(LocalDate date) {
        return date.toString().substring(0,7).replace("-", "");
    }

    private Map<String, LocalDate> convertToLocalDate(String yearMonth) {
        yearMonth = yearMonth+"01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startOfMonth = LocalDate.parse(yearMonth, formatter);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        Map<String, LocalDate> dateMap = new HashMap<>();
        dateMap.put("start", startOfMonth);
        dateMap.put("end", endOfMonth);
        return dateMap;
    }

    private void monthlyWhisperUpdate(User user, LocalDate start, LocalDate end, MonthlyAnalysis analysis) {
       long count = diaryAnalysisService.getWhisperCount(user, start, end);

       analysis.updateWhisperCount((int) count);
       monthlyAnalysisJPARepository.save(analysis);
    }


    // 월간 통계 가지고 오는데 없으면 새로 만들기
    private MonthlyAnalysis getOrCreateMonthlyAnalysis(User user, String yearMonth) {
        return monthlyAnalysisJPARepository.findByUserAndMonthlyDate(user, yearMonth)
                .orElseGet(() -> {
                    System.out.println("통계 내역이 없어 새로 생성합니다.");
                    MonthlyAnalysis newAnalysis = MonthlyAnalysis.builder()
                            .user(user)
                            .monthlyDate(yearMonth)
                            .totalAnswer(0)
                            .totalDiary(0)
                            .build();
                    return monthlyAnalysisJPARepository.save(newAnalysis);
                });
    }


    // 특정 달 다이어리 가져오기
    private List<PersonalDiary> getMonthlyDiaryList(User user, LocalDate start, LocalDate end) {
        return diaryAnalysisService.getPersonalDiaryList(user, start, end);
    }

    private Map<String, String> getYearMonthMap(LocalDate date) {
        String year = date.toString().split("-")[0];
        String month = date.toString().split("-")[1];

        Map<String, String> map = new HashMap<>();
        map.put("year", year);
        map.put("month", month);
        return map;
    }

}
