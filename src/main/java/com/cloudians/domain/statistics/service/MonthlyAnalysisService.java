package com.cloudians.domain.statistics.service;


import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryUpdateRequest;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryCreateResponse;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryResponse;
import com.cloudians.domain.statistics.repository.MonthlyAnalysisRepositoryImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cloudians.domain.home.entity.WhisperMessage;
import com.cloudians.domain.home.repository.WhisperMessageRepositoryImpl;
import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import com.cloudians.domain.personaldiary.entity.analysis.FiveElementCharacter;
import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryException;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryExceptionType;
import com.cloudians.domain.personaldiary.repository.FiveElementCharacterRepository;
import com.cloudians.domain.personaldiary.repository.FiveElementRepository;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryAnalysisRepository;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryEmotionRepository;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryRepository;
import com.cloudians.domain.statistics.dto.response.MonthlyAnalysisResponse;
import com.cloudians.domain.statistics.entity.MonthlyAnalysis;
import com.cloudians.domain.statistics.exception.AnalysisException;
import com.cloudians.domain.statistics.exception.AnalysisExceptionType;
import com.cloudians.domain.statistics.repository.CollectionRepository;
import com.cloudians.domain.statistics.repository.MonthlyAnalysisJPARepository;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import static com.cloudians.domain.statistics.exception.AnalysisExceptionType.MONTHLY_ANALYSIS_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@CacheConfig(cacheNames = "monthlyAnalysis")
public class MonthlyAnalysisService {


    private final MonthlyAnalysisJPARepository monthlyAnalysisJPARepository;
    private final MonthlyAnalysisRepositoryImpl monthlyAnalysisRepository;
    private final PersonalDiaryRepository diaryRepository;
    private final FiveElementRepository fiveElementRepository;
    private final PersonalDiaryEmotionRepository emotionRepository;
    private final FiveElementCharacterRepository fiveElementCharacterRepository;
    private final WhisperMessageRepositoryImpl whisperRepository;
    private final PersonalDiaryAnalysisRepository personalDiaryAnalysisRepository;


    @Transactional
    public void deleteDiaryEntry(User user, Long personalDiaryId) {
        PersonalDiary diary = findDiaryByIdAndUser(user, personalDiaryId);
        String year = getYearMonth(diary.getDate()).get("year");
        String month = getYearMonth(diary.getDate()).get("month");
        String date = year + month;
        MonthlyAnalysis anal = getOrCreateMonthlyAnalysis(user, date);
        PersonalDiaryEmotion emotion = findEmotionByUserAndDate(user, diary.getDate());
        anal.setTotalDiary(anal.getTotalDiary() - 1);
        anal.subtractAnalysisEmotion(emotion);
        monthlyAnalysisJPARepository.save(anal);
    }

    @Transactional
    public void updateDiaryEntry(User user, PersonalDiaryResponse response) {;
        String yearMonth = response.getDate().toString().substring(0,7).replace("-", "");
        PersonalDiaryEmotion emotion = findEmotionByUserAndDate(user, response.getDate());
        MonthlyAnalysis analysis = getOrCreateMonthlyAnalysis(user, yearMonth);
        analysis.setTotalDiary(analysis.getTotalDiary() + 1);
        analysis.addAnalysisEmotion(emotion);
        monthlyAnalysisJPARepository.save(analysis);
    }


    @Transactional
    public void addDiaryEntry(User user, PersonalDiaryCreateResponse diary) {
        String yearMonth = diary.getDate().toString().substring(0,7).replace("-", "");
        System.out.println(yearMonth);
        MonthlyAnalysis analysis = monthlyAnalysisRepository.findByUserAndMonthlyDate(user, yearMonth)
                .orElseGet(()-> {
                    MonthlyAnalysis newAnalysis = new MonthlyAnalysis();
                    newAnalysis.setUser(user);
                    newAnalysis.setMonthlyDate(yearMonth);
                    return monthlyAnalysisJPARepository.save(newAnalysis);
                });
        analysis.setTotalDiary(analysis.getTotalDiary() + 1);
        PersonalDiaryEmotion emotion = findEmotionByUserAndDate(user, diary.getDate());
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
    public Map<String, Object> getMonthlyReport(User user, String year, String month) {
        List<Map.Entry<Object, Long>> elementList = getMonthlyMostElement(user, year, month);
        Map.Entry<Object, Long> maxEntry = elementList.stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new RuntimeException("List is empty"));

        String maxElement = maxEntry.getKey().toString();
        FiveElement max = fiveElementRepository.findByName(maxElement).get();
        List<FiveElementCharacter> maxList = fiveElementCharacterRepository.findRandomCharactersByElementId(max.getId());


        Map.Entry<Object, Long> minEntry = elementList.stream()
                .min(Map.Entry.comparingByValue())
                .orElseThrow(() -> new RuntimeException("List is empty"));

        String minElement = minEntry.getKey().toString();
        FiveElement min = fiveElementRepository.findByName(maxElement).get();
        Map<String, Object> map = new HashMap<>();
        map.put("max", max);
        map.put("min", min);
        return map;
        // 제일 많은 기운과 제일 적은 기운에 대한 음양오행 내역과 특징 가져오기


    }


    // 위스퍼 대답 수 누적
    public void addWhisperEntry(User user, String yearMonth) {
        MonthlyAnalysis anal = getOrCreateMonthlyAnalysis(user, yearMonth);
        anal.setTotalAnswer(anal.getTotalAnswer() + 1);
        monthlyAnalysisJPARepository.save(anal);
    }



    // 월간 평균 기운 가져오기
    private List<Map.Entry<Object, Long>> getMonthlyMostElement(User user, String year, String month) {

        List<PersonalDiary> diaryList = getMonthlyDiaryList(user, year, month);
        System.out.println("list start:" + diaryList.toString());

        List<String> elementList = new ArrayList<>();
        for (PersonalDiary diary : diaryList) {
            PersonalDiaryAnalysis analysis = personalDiaryAnalysisRepository.findByPersonalDiaryId(diary.getId()).get();
            elementList.add(analysis.getFiveElement().getName());
        }


        Map<Object, Long> frequencyMap = elementList.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        System.out.println(frequencyMap.toString());
        Optional<Map.Entry<Object, Long>> mostFrequent = frequencyMap.entrySet().stream()
                .max(Map.Entry.comparingByValue());
        System.out.println(mostFrequent.toString());
        Map.Entry<Object, Long> entry = mostFrequent.get();

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


    public String getMonth() {
        String year = Integer.toString(LocalDate.now().getYear());
        String todayMonth = String.format("%02d", LocalDate.now().getMonthValue());
        System.out.println("이번 달은" + todayMonth + "입니다.");
        return year + todayMonth;
    }

    private String getYear() {
        return Integer.toString(LocalDate.now().getYear());
    }


    private List<WhisperMessage> getWhisperList(User user) {
        List<WhisperMessage> list = whisperRepository.findListByUser(user);
        return list;
    }

    private List<WhisperMessage> getMonthlyWhisperList(User user, String yearMonth) {
        List<WhisperMessage> totalList = getWhisperList(user);
        System.out.println("여긴가?" + totalList.toString());
        List<WhisperMessage> monthlyList = new ArrayList<>();
        for (WhisperMessage message : totalList) {
            String diaryMonth = message.getTimestamp().toString().split("-")[0] + message.getTimestamp().toString().split("-")[1];
            System.out.println(diaryMonth);
            if (yearMonth.equals(diaryMonth)) {
                monthlyList.add(message);
            }
        }
        return monthlyList;
    }

    // 이번 달
    private List<PersonalDiary> getThisMonthlyDiaryList(User user) {
       return getMonthlyDiaryList(user, getYear() ,getMonth());
    }


    private PersonalDiaryEmotion findEmotionByUserAndDate(User user, LocalDate date) {
        PersonalDiaryEmotion emotion = emotionRepository.findPersonalDiaryEmotionByUserAndDate(user, date);
        return emotion;
    }

    private PersonalDiary findDiaryByIdAndUser(User user, Long personalDiaryId) {
        PersonalDiary diary = diaryRepository.findById(personalDiaryId)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
        return diary;
    }

    // 월간 통계 가지고 오는데 없으면 새로 만들기
    private MonthlyAnalysis getOrCreateMonthlyAnalysis(User user, String yearMonth) {
        return monthlyAnalysisJPARepository.findByUserAndMonthlyDate(user, yearMonth)
                .orElseGet(() -> {
                    System.out.println("통계 내역이 없어 새로 생성합니다.");
                    MonthlyAnalysis newAnalysis = new MonthlyAnalysis();
                    newAnalysis.setUser(user);
                    newAnalysis.setMonthlyDate(yearMonth);
                    newAnalysis.setTotalDiary(0);
                    newAnalysis.setTotalAnswer(0);
                    return monthlyAnalysisJPARepository.save(newAnalysis);
                });
    }

    // 날짜로 통계 찾기
    private MonthlyAnalysis findAnalysisByUserAndMonthlyDate(User user, String date) {
        MonthlyAnalysis analysis = monthlyAnalysisJPARepository.findByUserAndMonthlyDate(user, date)
                .orElseThrow(() -> new AnalysisException(MONTHLY_ANALYSIS_NOT_FOUND));
        return analysis;
    }


    // 특정 달 다이어리 가져오기
    private List<PersonalDiary> getMonthlyDiaryList(User user, String year, String month) {
        System.out.println("헨뇨");
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        LocalDate startOfMonth = LocalDate.of(yearInt, monthInt, 1);
        System.out.println(startOfMonth+"달");
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        List<PersonalDiary> diaryList = diaryRepository.findPersonalDiaryByUserAndDateBetweenOrderByDate(user, startOfMonth, endOfMonth)
                .orElseThrow(()-> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
        return diaryList;
    }

    private Map<String, String> getYearMonth(LocalDate date) {
        String year = date.toString().split("-")[0];
        String month = date.toString().split("-")[1];

        Map<String, String> map = new HashMap<>();
        map.put("year", year);
        map.put("month", month);
        return map;
    }

}
