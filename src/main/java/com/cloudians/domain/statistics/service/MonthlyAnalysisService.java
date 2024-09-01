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
import com.cloudians.domain.statistics.dto.response.CollectionResponse;
import com.cloudians.domain.statistics.dto.response.MonthlyAnalysisResponse;
import com.cloudians.domain.statistics.entity.Collection;
import com.cloudians.domain.statistics.entity.MonthlyAnalysis;
import com.cloudians.domain.statistics.exception.AnalysisException;
import com.cloudians.domain.statistics.exception.AnalysisExceptionType;
import com.cloudians.domain.statistics.repository.CollectionRepository;
import com.cloudians.domain.statistics.repository.MonthlyAnalysisJPARepository;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
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


    private PersonalDiaryEmotion findEmotionByUserAndDate(String userEmail, LocalDate date) {
        User user = findUserByUserEmail(userEmail);
        PersonalDiaryEmotion emotion = emotionRepository.findPersonalDiaryEmotionByUserAndDate(user, date);
        return emotion;
    }

    private PersonalDiary findDiaryByIdAndUser(String userEmail, Long personalDiaryId) {
        User user = findUserByUserEmail(userEmail);
        PersonalDiary diary = diaryRepository.findById(personalDiaryId)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
        return diary;

    }

    private MonthlyAnalysis getOrCreateMonthlyAnalysis(String userEmail, String yearMonth) {
        User user = findUserByUserEmail(userEmail);
        return monthlyRepository.findByUserEmailAndMonthlyDate(userEmail, yearMonth)
                .orElseGet(() -> {
                    MonthlyAnalysis newAnalysis = new MonthlyAnalysis();
                    newAnalysis.setUserEmail(userEmail);
                    newAnalysis.setMonthlyDate(yearMonth);
                    newAnalysis.setTotalDiary(0);
                    newAnalysis.setTotalAnswer(0);
                    // Initialize other fields with default values if needed
                    return monthlyRepository.save(newAnalysis);
                });
    }

    private MonthlyAnalysis findAnalysisByuserEmailAndMonthlyDate(String userEmail, String date) {
        MonthlyAnalysis analysis = monthlyRepository.findByUserEmailAndMonthlyDate(userEmail, date)
                .orElseThrow(() -> new AnalysisException(AnalysisExceptionType.MONTHLY_ANALYSIS_NOT_FOUND));
        return analysis;
    }

    // 월간 통계 제공
    public MonthlyAnalysisResponse getMonthlyAnalysis(String userEmail, String yearMonth) {

        if (monthlyRepository.findByUserEmailAndMonthlyDate(userEmail, yearMonth).isEmpty()) {
            MonthlyAnalysis newAnalysis = updateMonthlyAnalysis(userEmail, yearMonth);
            return newAnalysis.toDto();
        } else {
            System.out.println("있지롱");
            MonthlyAnalysis analysis = findAnalysisByuserEmailAndMonthlyDate(userEmail, yearMonth);
            return analysis.toDto();
        }
    }


    // 월간 통계 없을 시
    public MonthlyAnalysis updateMonthlyAnalysis(String userEmail, String yearMonth) {
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
        List<WhisperMessage> whisperList = getMonthlyWhisperList(userEmail, yearMonth);
        // 월간 일기 수와 5 emotion 누적
        List<PersonalDiary> diaryList = getMonthlyDiaryList(userEmail, yearMonth);
        for (PersonalDiary diary : diaryList) {
            totalDiary++;
            PersonalDiaryEmotion emotion = findEmotionByUserAndDate(userEmail, diary.getDate());
            monthlyJoy += emotion.getJoy();
            monthlySadness += emotion.getSadness();
            monthlyAnxiety += emotion.getAnxiety();
            monthlyBoredom += emotion.getBoredom();
        }

        List<Map.Entry<Object, Long>> list = getMonthlyMostElement(userEmail, yearMonth);
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
        analysis.setUserEmail(userEmail);
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

        return analysis;

    }


    // 월간 통계 데이터 있다고 가정하고 5개의 감정과 일기 수 누적
    public void addDiaryEntry(String userEmail, LocalDate date) {
        String yearMonth = getMonth();
        MonthlyAnalysis anal = getOrCreateMonthlyAnalysis(userEmail, yearMonth);
        PersonalDiaryEmotion emotion = findEmotionByUserAndDate(userEmail, date);

        anal.setTotalDiary(anal.getTotalDiary() + 1);
        anal.setMonthlyJoy(anal.getMonthlyJoy() + emotion.getJoy());
        anal.setMonthlySadness(anal.getMonthlySadness() + emotion.getSadness());
        anal.setMonthlyAnger(anal.getMonthlyAnger() + emotion.getAnger());
        anal.setMonthlyAnxiety(anal.getMonthlyAnxiety() + emotion.getAnxiety());
        anal.setMonthlyBoredom(anal.getMonthlyBoredom() + emotion.getBoredom());

        monthlyRepository.save(anal);

    }

    // 다이어리 삭제 시
    public void deleteDiaryEntry(String userEmail, Long personalDiaryId) {
        PersonalDiary diary = findDiaryByIdAndUser(userEmail, personalDiaryId);
        String year = diary.getDate().toString().split("-")[0];
        String month = diary.getDate().toString().split("-")[1];
        String date = year + month;
        MonthlyAnalysis anal = getOrCreateMonthlyAnalysis(userEmail, date);
        PersonalDiaryEmotion emotion = findEmotionByUserAndDate(userEmail, diary.getDate());


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
    public void addWhisperEntry(String userEmail, String yearMonth) {

        MonthlyAnalysis anal = getOrCreateMonthlyAnalysis(userEmail, yearMonth);
        anal.setTotalAnswer(anal.getTotalAnswer() + 1);
        monthlyRepository.save(anal);
    }

    // 월간 정리
    public Map<String, Object> getMonthlyReport(String userEmail, String yearMonth) {
        List<Map.Entry<Object, Long>> elementList = getMonthlyMostElement(userEmail, yearMonth);
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
    public List<Map.Entry<Object, Long>> getMonthlyMostElement(String userEmail, String yearMonth) {
        System.out.println("service start");
        List<PersonalDiary> diaryList = getMonthlyDiaryList(userEmail, yearMonth);
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

    private User findUserByUserEmail(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
        return user;
    }

    // personalDiaryList 가져오기
    private List<PersonalDiary> getDiaryList(String userEmail) {
        User user = findUserByUserEmail(userEmail);
        List<PersonalDiary> list = diaryRepository.findListByUser(user)
                .orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
        return list;
    }


    private List<WhisperMessage> getWhisperList(String userEmail) {
        User user = findUserByUserEmail(userEmail);
        List<WhisperMessage> list = whisperRepository.findListByUser(user);
        return list;
    }

    public List<WhisperMessage> getMonthlyWhisperList(String userEmail, String yearMonth) {
        List<WhisperMessage> totalList = getWhisperList(userEmail);
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
    private List<PersonalDiary> getThisMonthlyDiaryList(String userEmail) {
        List<PersonalDiary> totalList = getDiaryList(userEmail);
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
    public List<PersonalDiary> getMonthlyDiaryList(String userEmail, String yearMonth) {
        List<PersonalDiary> totalList = getDiaryList(userEmail);
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
    public void addCollection(String userEmail, LocalDate date) {
    }

    // 월간 콜렉션
    public List<CollectionResponse> getMonthlyCollection(String userEmail, String yearMonth) {
        List<Collection> totalCollectionList = collectionRepository.findListByUserEmail(userEmail);
        List<CollectionResponse> collectionList = new ArrayList<>();

        for (Collection col : totalCollectionList) {
            String colDate = col.getDate().toString().split("-")[0] + col.getDate().toString().split("-")[1];
            if (colDate.equals(yearMonth)) {
                collectionList.add(col.toDto());
            }
        }
        return collectionList;

    }


}
