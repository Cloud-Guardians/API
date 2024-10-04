package com.cloudians.domain.statistics.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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

@Service
@RequiredArgsConstructor
@Transactional
public class MonthlyAnalysisService {
    private final MonthlyAnalysisJPARepository monthlyRepository;
    private final PersonalDiaryRepository diaryRepository;
    private final FiveElementRepository fiveElementRepository;
    private final PersonalDiaryEmotionRepository emotionRepository;
    private final FiveElementCharacterRepository fiveElementCharacterRepository;
    private final WhisperMessageRepositoryImpl whisperRepository;
    private final UserRepository userRepository;
    private final PersonalDiaryAnalysisRepository personalDiaryAnalysisRepository;
    private final CollectionRepository collectionRepository;


    private PersonalDiaryEmotion findEmotionByUserAndDate(User user, LocalDate date) {
        PersonalDiaryEmotion emotion = emotionRepository.findPersonalDiaryEmotionByUserAndDate(user, date);
        return emotion;
    }

    private PersonalDiary findDiaryByIdAndUser(User user, Long personalDiaryId) {
        PersonalDiary diary = diaryRepository.findById(personalDiaryId)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
        return diary;
    }

    private MonthlyAnalysis getOrCreateMonthlyAnalysis(User user, String yearMonth) {
        return monthlyRepository.findByUserAndMonthlyDate(user, yearMonth)
                .orElseGet(() -> {
                    MonthlyAnalysis newAnalysis = new MonthlyAnalysis();
                    newAnalysis.setUser(user);
                    newAnalysis.setMonthlyDate(yearMonth);
                    newAnalysis.setTotalDiary(0);
                    newAnalysis.setTotalAnswer(0);
                    // Initialize other fields with default values if needed
                    return monthlyRepository.save(newAnalysis);
                });
    }

    private MonthlyAnalysis findAnalysisByUserAndMonthlyDate(User user, String date) {
        MonthlyAnalysis analysis = monthlyRepository.findByUserAndMonthlyDate(user, date)
                .orElseThrow(() -> new AnalysisException(AnalysisExceptionType.MONTHLY_ANALYSIS_NOT_FOUND));
        return analysis;
    }

    // 월간 통계 제공
    public MonthlyAnalysisResponse getMonthlyAnalysis(User user, String yearMonth) {

        if (monthlyRepository.findByUserAndMonthlyDate(user, yearMonth).isEmpty()) {
            MonthlyAnalysisResponse newAnalysis = updateMonthlyAnalysis(user, yearMonth);
            return newAnalysis;
        } else {
            System.out.println("있지롱");
            MonthlyAnalysis analysis = findAnalysisByUserAndMonthlyDate(user, yearMonth);
            return analysis.toDto();
        }
    }


    // 월간 통계 없을 시
    public MonthlyAnalysisResponse updateMonthlyAnalysis(User user, String yearMonth) {
        MonthlyAnalysis analysis = new MonthlyAnalysis();
        int totalDiary = 0;
        int monthlyJoy = 0;
        int monthlySadness = 0;
        int monthlyAnger = 0;
        int monthlyAnxiety = 0;
        int monthlyBoredom = 0;
        String most1 = "";
        String most2 = "";
        String most3 = "";
        List<WhisperMessage> whisperList = getMonthlyWhisperList(user, yearMonth);
        // 월간 일기 수와 5 emotion 누적
        List<PersonalDiary> diaryList = getMonthlyDiaryList(user, yearMonth);
        for (PersonalDiary diary : diaryList) {
            totalDiary++;
            PersonalDiaryEmotion emotion = findEmotionByUserAndDate(user, diary.getDate());
            monthlyJoy += emotion.getJoy();
            monthlySadness += emotion.getSadness();
            monthlyAnxiety += emotion.getAnxiety();
            monthlyBoredom += emotion.getBoredom();
        }

        List<Map.Entry<Object, Long>> list = getMonthlyMostElement(user, yearMonth);
        if (list.size() == 3) {
            most1 = list.get(0).getKey().toString() + "=" + list.get(0).getValue().toString();
            most2 = list.get(1).getKey().toString() + "=" + list.get(1).getValue().toString();
            most3 = list.get(2).getKey().toString() + "=" + list.get(2).getValue().toString();
        } else if (list.size() == 2) {
            most1 = list.get(0).getKey().toString() + "=" + list.get(0).getValue().toString();
            most2 = list.get(1).getKey().toString() + "=" + list.get(1).getValue().toString();
        } else {
            most1 = list.get(0).getKey().toString() + "=" + list.get(0).getValue().toString();
        }
        analysis.setUser(user);
        analysis.setMonthlyDate(yearMonth);
        analysis.setTotalDiary(totalDiary);
        analysis.setTotalAnswer(whisperList.size());
        analysis.setMonthlyJoy(monthlyJoy);
        analysis.setMonthlySadness(monthlySadness);
        analysis.setMonthlyAnger(monthlyAnger);
        analysis.setMonthlyAnxiety(monthlyAnxiety);
        analysis.setMonthlyBoredom(monthlyBoredom);
        analysis.setMonthlyElement(list.get(0).getKey().toString());
        analysis.setMostElementTop3(most1 + "," + most2 + "," + most3);


        monthlyRepository.save(analysis);

        return MonthlyAnalysisResponse.of(analysis);

    }


    // 월간 통계 데이터 있다고 가정하고 5개의 감정과 일기 수 누적
    public void addDiaryEntry(User user, LocalDate date) {
        String yearMonth = getMonth();
        MonthlyAnalysis anal = getOrCreateMonthlyAnalysis(user, yearMonth);
        PersonalDiaryEmotion emotion = findEmotionByUserAndDate(user, date);

        anal.setTotalDiary(anal.getTotalDiary() + 1);
        anal.setMonthlyJoy(anal.getMonthlyJoy() + emotion.getJoy());
        anal.setMonthlySadness(anal.getMonthlySadness() + emotion.getSadness());
        anal.setMonthlyAnger(anal.getMonthlyAnger() + emotion.getAnger());
        anal.setMonthlyAnxiety(anal.getMonthlyAnxiety() + emotion.getAnxiety());
        anal.setMonthlyBoredom(anal.getMonthlyBoredom() + emotion.getBoredom());

        monthlyRepository.save(anal);

    }

    // 다이어리 삭제 시
    public void deleteDiaryEntry(User user, Long personalDiaryId) {
        PersonalDiary diary = findDiaryByIdAndUser(user, personalDiaryId);
        String year = diary.getDate().toString().split("-")[0];
        String month = diary.getDate().toString().split("-")[1];
        String date = year + month;
        MonthlyAnalysis anal = getOrCreateMonthlyAnalysis(user, date);
        PersonalDiaryEmotion emotion = findEmotionByUserAndDate(user, diary.getDate());


        anal.setTotalDiary(anal.getTotalDiary() - 1);
        anal.setMonthlyJoy(anal.getMonthlyJoy() - emotion.getJoy());
        anal.setMonthlySadness(anal.getMonthlySadness() - emotion.getSadness());
        anal.setMonthlyAnger(anal.getMonthlyAnger() - emotion.getAnger());
        anal.setMonthlyAnxiety(anal.getMonthlyAnxiety() - emotion.getAnxiety());
        anal.setMonthlyBoredom(anal.getMonthlyBoredom() - emotion.getBoredom());

        monthlyRepository.save(anal);

    }

    // 일기 수정
//    public void modifyDiaryEntry(String userEmail, PersonalDiary diary, PersonalDiaryResponse response) {
//   	String year = diary.getDate().toString().split("-")[0];
//   	String month = diary.getDate().toString().split("-")[1];
//   	String date = year+month;
//   	MonthlyAnalysis anal = findAnalysisByuserEmailAndMonthlyDate(userEmail, date);
// 
//   	PersonalDiaryEmotion emotion = findEmotionByUserAndDate(userEmail,diary.getDate());
//   	
//
//   	anal.setMonthlyHappy(anal.getMonthlyHappy()-emotion.getJoy()+request.getJoy());
//   	anal.setMonthlySad(anal.getMonthlySad()-emotion.getSadness()+request.getSadness());
//   	anal.setMonthlyAngry(anal.getMonthlyHappy()-emotion.getAnger()+request.getAnger());
//   	anal.setMonthlyUneasy(anal.getMonthlyHappy()-emotion.getAnxiety()+request.getAnxiety());
//   	anal.setMonthlyBoring(anal.getMonthlyHappy()-emotion.getBoredom()+request.getBoredom());
//   	
//   	monthlyRepository.save(anal);
//   	
//       }


    // 위스퍼 대답 수 누적
    public void addWhisperEntry(User user, String yearMonth) {

        MonthlyAnalysis anal = getOrCreateMonthlyAnalysis(user, yearMonth);
        anal.setTotalAnswer(anal.getTotalAnswer() + 1);
        monthlyRepository.save(anal);
    }

    // 월간 정리
    public Map<String, Object> getMonthlyReport(User user, String yearMonth) {
        List<Map.Entry<Object, Long>> elementList = getMonthlyMostElement(user, yearMonth);
        Map.Entry<Object, Long> maxEntry = elementList.stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new RuntimeException("List is empty"));

        String maxElement = maxEntry.getKey().toString();
        System.out.println("max:" + maxElement);
        FiveElement max = fiveElementRepository.findByName(maxElement).get();
        List<FiveElementCharacter> maxList = fiveElementCharacterRepository.findRandomCharactersByElementId(max.getId());


        Map.Entry<Object, Long> minEntry = elementList.stream()
                .min(Map.Entry.comparingByValue())
                .orElseThrow(() -> new RuntimeException("List is empty"));

        String minElement = minEntry.getKey().toString();
        System.out.println("min:" + minElement);
        FiveElement min = fiveElementRepository.findByName(maxElement).get();
        Map<String, Object> map = new HashMap<>();
        map.put("max", max);
        map.put("min", min);
        System.out.println("service 이상 무");
        return map;
        // 제일 많은 기운과 제일 적은 기운에 대한 음양오행 내역과 특징 가져오기


    }

    // 월간 평균 기운 가져오기
    public List<Map.Entry<Object, Long>> getMonthlyMostElement(User user, String yearMonth) {
        System.out.println("service start");
        List<PersonalDiary> diaryList = getMonthlyDiaryList(user, yearMonth);
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

    // 기준 날짜 : 사용자가 들어간 날짜 기점으로 해당 달을 얻어야 함
    public String getMonth() {
        String year = Integer.toString(LocalDate.now().getYear());
        String todayMonth = String.format("%02d", LocalDate.now().getMonthValue());
        System.out.println("이번 달은" + todayMonth + "입니다.");
        return year + todayMonth;
    }

  
    // personalDiaryList 가져오기
    private List<PersonalDiary> getDiaryList(User user) {
        List<PersonalDiary> list = diaryRepository.findListByUser(user)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
        return list;
    }


    private List<WhisperMessage> getWhisperList(User user) {
        List<WhisperMessage> list = whisperRepository.findListByUser(user);
        return list;
    }

    public List<WhisperMessage> getMonthlyWhisperList(User user, String yearMonth) {
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
        List<PersonalDiary> totalList = getDiaryList(user);
        String thisMonth = getMonth();
        List<PersonalDiary> monthlyList = new ArrayList<>();
        for (PersonalDiary diary : totalList) {
            String diaryMonth = diary.getDate().toString().split("-")[1];
            if (thisMonth.equals(diaryMonth)) {
                monthlyList.add(diary);
                System.out.println("diary created at: " + diary.getCreatedAt());
            }
        }
        return monthlyList;
    }

    // yearMonth 202408 
    public List<PersonalDiary> getMonthlyDiaryList(User user, String yearMonth) {
        List<PersonalDiary> totalList = getDiaryList(user);
        System.out.println("여긴가?" + totalList.toString());
        List<PersonalDiary> monthlyList = new ArrayList<>();
        for (PersonalDiary diary : totalList) {
            String diaryMonth = diary.getDate().toString().split("-")[0] + diary.getDate().toString().split("-")[1];
            System.out.println(diaryMonth);
            if (yearMonth.equals(diaryMonth)) {
                monthlyList.add(diary);
            }
        }
        return monthlyList;
    }

    // 일기 작성 시 콜렉션 리스트 추가 
//    public void addCollection(String userEmail, LocalDate date) {
//    }

    // 월간 콜렉션
//    public List<CollectionResponse> getMonthlyCollection(User user, String yearMonth) {
//        List<Collection> totalCollectionList = collectionRepository.findListByUser(user);
//        List<CollectionResponse> collectionList = new ArrayList<>();
//
//        for (Collection col : totalCollectionList) {
//            String colDate = col.getDate().toString().split("-")[0] + col.getDate().toString().split("-")[1];
//            if (colDate.equals(yearMonth)) {
//                collectionList.add(col.toDto());
//            }
//        }
//        return collectionList;
//
//    }


}
