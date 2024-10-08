package com.cloudians.domain.statistics.service;

import com.cloudians.domain.home.entity.WhisperMessage;
import com.cloudians.domain.home.repository.WhisperMessageRepositoryImpl;
import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import com.cloudians.domain.personaldiary.entity.analysis.FiveElementCharacter;
import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryException;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryExceptionType;
import com.cloudians.domain.personaldiary.repository.*;
import com.cloudians.domain.statistics.entity.WeeklyAnalysis;
import com.cloudians.domain.statistics.exception.AnalysisException;
import com.cloudians.domain.statistics.exception.AnalysisExceptionType;
import com.cloudians.domain.statistics.repository.WeeklyAnalysisRepository;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WeeklyAnalysisService {
    private final WeeklyAnalysisRepository weeklyRepository;
    private final PersonalDiaryRepository diaryRepository;
    private final PersonalDiaryEmotionRepository emotionRepository;
    private final UserRepository userRepository;
    private boolean updated = false;
    private final PersonalDiaryAnalysisRepository personalDiaryAnalysisRepository;
    private final WhisperMessageRepositoryImpl whisperRepository;
    private final FiveElementCharacterRepository fiveElementCharacterRepository;
    private final FiveElementRepository fiveElementRepository;


    // personalDiaryList 가져오기
    private List<PersonalDiary> getDiaryList(User user) {
        List<PersonalDiary> list = diaryRepository.findListByUser(user)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
        return list;
    }

    // 몇째주인지확인하기 
    private int getWeek(int date) {
        int week = 0;
        if (date <= 7) {
            week = 1;
        } else if (date > 7 && date <= 14) {
            week = 2;
        } else if (date > 14 && date <= 21) {
            week = 3;
        } else if (date > 22) {
            week = 4;
        }
        return week;
    }

    // yearMonthWeek 2024081
    public List<PersonalDiary> getWeeklyDiaryList(User user, String yearMonth, String week) {
        List<PersonalDiary> totalList = getDiaryList(user);
        List<PersonalDiary> weeklyList = new ArrayList<>();
        for (PersonalDiary diary : totalList) {
            String diaryMonth = diary.getDate().toString().split("-")[0] + diary.getDate().toString().split("-")[1];
            String day = diary.getDate().toString().split("-")[2];
            String weekly = String.valueOf(getWeek(Integer.parseInt(day)));
            System.out.println(diaryMonth);
            if (yearMonth.equals(diaryMonth) && weekly.equals(week)) {
                weeklyList.add(diary);
            }
        }
        if (weeklyList.isEmpty()) {
            throw new AnalysisException(AnalysisExceptionType.WEEKLY_ANALYSIS_NOT_FOUND);
        }
        return weeklyList;
    }

    private List<WhisperMessage> getWhisperList(User user) {
        List<WhisperMessage> list = whisperRepository.findListByUser(user);
        return list;
    }

    public List<WhisperMessage> getWeeklyWhisperList(User user, String yearMonth, String week) {
        List<WhisperMessage> totalList = getWhisperList(user);
        List<WhisperMessage> weeklyList = new ArrayList<>();

        for (WhisperMessage message : totalList) {
            System.out.println(message.getTimestamp());
            String whisperMonth = message.getTimestamp().toString().split("-")[0] + message.getTimestamp().toString().split("-")[1];
            String day = message.getTimestamp().toString().split("-")[2].substring(0, 2);
            String weekly = String.valueOf(getWeek(Integer.parseInt(day)));
            System.out.println(whisperMonth);
            if (yearMonth.equals(whisperMonth) && weekly.equals(week)) {
                weeklyList.add(message);
            }
        }
        return weeklyList;
    }

    // 주간 위스퍼 답변 수 
    public int getWeeklyDiaryAndAnswer(User user, String yearMonth, String week) {
        List<WhisperMessage> whisperList = getWeeklyWhisperList(user, yearMonth, week);
        int totalWhisper = whisperList.size();
        return totalWhisper;
    }

    // 주간 평균 감정
    // 월간 평균 기운 가져오기 
    public List<Map.Entry<Object, Long>> getWeeklyMostElement(User user, String yearMonth, String week) {
        System.out.println("service start");
        List<PersonalDiary> diaryList = getWeeklyDiaryList(user, yearMonth, week);
        System.out.println("list start:" + diaryList.toString());

        List<String> elementList = new ArrayList<>();

        for (PersonalDiary diary : diaryList) {
            PersonalDiaryAnalysis analysis = personalDiaryAnalysisRepository.findByPersonalDiaryId(diary.getId())
                    .orElseThrow(() -> new AnalysisException(AnalysisExceptionType.ANALYSIS_NOT_FOUND));
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

    // 주간 통계 업데이트
    public Map<String, Object> getWeeklyAnalysis(User user, String yearMonth, String week) {
        List<PersonalDiary> diaryList = getWeeklyDiaryList(user, yearMonth, week);
        List<Map.Entry<Object, Long>> elementList = getWeeklyMostElement(user, yearMonth, week);
        String most = elementList.get(0).getKey().toString();
        FiveElement mostElement = fiveElementRepository.findByName(most).get();
        // List<FiveElementCharacter>로 보내면 Jackson 직렬화 때문에 오류 나서 이렇게 코드 짠 건데 나중에 복습하기
        List<String> mostElementChar = fiveElementCharacterRepository.findRandomCharactersByElementId(mostElement.getId()).stream().map(FiveElementCharacter::getCharacteristic).collect(Collectors.toList());
        ;
        WeeklyAnalysis anal = getOrCreateWeeklyAnalysis(user, yearMonth, String.valueOf(week));
        for (PersonalDiary diary : diaryList) {
            PersonalDiaryEmotion emotion = emotionRepository.findPersonalDiaryEmotionByUserAndDate(user, diary.getDate());
            // 기존 값을 더하지 않고 주간 통계를 새롭게 계산
            anal.setTotalDiary(diaryList.size());
            anal.setTotalAnswer(getWeeklyDiaryAndAnswer(user, yearMonth, week));
            anal.setWeeklyJoy(diaryList.stream().mapToInt(d -> findEmotionByUserAndDate(user, d.getDate()).getJoy()).sum());
            anal.setWeeklySadness(diaryList.stream().mapToInt(d -> findEmotionByUserAndDate(user, d.getDate()).getSadness()).sum());
            anal.setWeeklyAnger(diaryList.stream().mapToInt(d -> findEmotionByUserAndDate(user, d.getDate()).getAnger()).sum());
            anal.setWeeklyAnxiety(diaryList.stream().mapToInt(d -> findEmotionByUserAndDate(user, d.getDate()).getAnxiety()).sum());
            anal.setWeeklyBoredom(diaryList.stream().mapToInt(d -> findEmotionByUserAndDate(user, d.getDate()).getBoredom()).sum());
        }

        weeklyRepository.save(anal);
        Map<String, Object> map = new HashMap<>();
        map.put("response", anal.toDto());
        map.put("list", mostElementChar);

        return map;
    }

    private PersonalDiaryEmotion findEmotionByUserAndDate(User user, LocalDate date) {
        PersonalDiaryEmotion emotion = emotionRepository.findPersonalDiaryEmotionByUserAndDate(user, date);
        return emotion;
    }

    private WeeklyAnalysis getOrCreateWeeklyAnalysis(User user, String yearMonth, String week) {
        return weeklyRepository.findByUserAndWeeklyDate(user, yearMonth + week)
                .orElseGet(() -> {
                    WeeklyAnalysis newAnalysis = new WeeklyAnalysis();
                    newAnalysis.setUser(user);
                    newAnalysis.setWeeklyDate(yearMonth + week);
                    newAnalysis.setTotalDiary(0);
                    newAnalysis.setTotalAnswer(0);
                    // Initialize other fields with default values if needed
                    return weeklyRepository.save(newAnalysis);
                });
    }

    // 다음 주 예측 및 조언


}
