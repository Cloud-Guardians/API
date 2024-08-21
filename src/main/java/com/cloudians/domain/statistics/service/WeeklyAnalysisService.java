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
import com.cloudians.domain.home.repository.WhisperMessageJpaRepository;
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
import com.cloudians.domain.statistics.entity.WeeklyAnalysis;
import com.cloudians.domain.statistics.repository.WeeklyAnalysisRepository;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WeeklyAnalysisService {
    private final WeeklyAnalysisRepository weeklyRepository;
    private final PersonalDiaryRepository diaryRepository;
    private final PersonalDiaryEmotionRepository emotionRepository;
    private final UserRepository userRepository;
    private final WhisperMessageJpaRepository whisperJpaRepository;
    private final PersonalDiaryAnalysisRepository personalDiaryAnalysisRepository;
    private final WhisperMessageRepositoryImpl whisperRepository;
    private final FiveElementCharacterRepository fiveElementCharacterRepository;
    private final FiveElementRepository fiveElementRepository;

    private User findUserByUserEmail(String userEmail) {
   	User user = userRepository.findByUserEmail(userEmail)
   		.orElseThrow(()-> new UserException(UserExceptionType.USER_NOT_FOUND));
   	return user;
       }
       
    // personalDiaryList 가져오기
    private List<PersonalDiary> getDiaryList(String userEmail){
	User user = findUserByUserEmail(userEmail);
	List<PersonalDiary> list = diaryRepository.findListByUser(user)
		.orElseThrow(()->  new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
	return list;
    }
    
    // 몇째주인지확인하기 
    private int getWeek(int date) {
	int week = 0;
	if(date <=7) {
	    week = 1;
	} else if(date>7 && date<=14) {
	    week = 2;
	}else if(date>14 && date <=21) {
	    week = 3;
	}else if(date>22) {
	    week = 4;
	}
	return week;
    }
    // yearMonthWeek 2024081
    public List<PersonalDiary> getWeeklyDiaryList(String userEmail, String yearMonth, String week){
 	List<PersonalDiary> totalList = getDiaryList(userEmail);
 	List<PersonalDiary> weeklyList = new ArrayList<>();
 	for(PersonalDiary diary : totalList) {
 	 String diaryMonth =  diary.getDate().toString().split("-")[0]+diary.getDate().toString().split("-")[1];
 	 String day = diary.getDate().toString().split("-")[2];
 	 String weekly = String.valueOf(getWeek(Integer.parseInt(day)));
 	 System.out.println(diaryMonth);
 	 if(yearMonth.equals(diaryMonth) && weekly.equals(week)) {     
 	    weeklyList.add(diary);
 	 }
 	}
 	return weeklyList;
     }
    private List<WhisperMessage> getWhisperList(String userEmail){
	User user = findUserByUserEmail(userEmail);
 	List<WhisperMessage> list = whisperRepository.findListByUser(user);
 	return list;
     }
    public List<WhisperMessage> getWeeklyWhisperList(String userEmail, String yearMonth, String week){
 	List<WhisperMessage> totalList = getWhisperList(userEmail);
 	List<WhisperMessage> weeklyList = new ArrayList<>();
 	
 	for(WhisperMessage message : totalList) {
 	    System.out.println(message.getTimestamp());
 	 String whisperMonth =  message.getTimestamp().toString().split("-")[0]+message.getTimestamp().toString().split("-")[1];
 	 String day = message.getTimestamp().toString().split("-")[2].substring(0,2);
 	 String weekly = String.valueOf(getWeek(Integer.parseInt(day)));
 	 System.out.println(whisperMonth);
 	 if(yearMonth.equals(whisperMonth) && weekly.equals(week)) {     
 	    weeklyList.add(message);
 	 }
 	}
 	return weeklyList;
     }
    
    // 주간 위스퍼 답변 수 
    public int getWeeklyDiaryAndAnswer(String userEmail, String yearMonth, String week) {
	List<WhisperMessage> whisperList = getWeeklyWhisperList(userEmail, yearMonth, week);
	 int totalWhisper = whisperList.size();
	 return totalWhisper;
    }
    
    // 주간 평균 감정
    // 월간 평균 기운 가져오기 
    public  List<Map.Entry<Object, Long>> getWeeklyMostElement(String userEmail, String yearMonth, String week) {
System.out.println("service start");
	List<PersonalDiary> diaryList = getWeeklyDiaryList(userEmail,yearMonth,week);
	System.out.println("list start:"+diaryList.toString());
	List<String> elementList = new ArrayList<>();
	for(PersonalDiary diary : diaryList) {
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
	 
	 if(frequencyMap.size()<3) {
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
  public Map<String, Object> getWeeklyAnalysis(String userEmail, String yearMonth, String week){
      List<PersonalDiary> diaryList = getWeeklyDiaryList(userEmail, yearMonth, week);
      User user = findUserByUserEmail(userEmail);
      List<Map.Entry<Object, Long>> elementList =  getWeeklyMostElement(userEmail, yearMonth, week);
      String most = elementList.get(0).getKey().toString();
      FiveElement mostElement = fiveElementRepository.findByName(most).get();
      List<FiveElementCharacter> mostElementChar = fiveElementCharacterRepository.findRandomCharactersByElementId(mostElement.getId());
      WeeklyAnalysis anal = getOrCreateWeeklyAnalysis(userEmail, yearMonth, String.valueOf(week));
      for(PersonalDiary diary : diaryList) {
	  PersonalDiaryEmotion emotion = emotionRepository.findPersonalDiaryEmotionByUserAndDate(user, diary.getDate());
	  anal.setTotalDiary(anal.getTotalDiary()+1);
	  anal.setTotalAnswer(getWeeklyDiaryAndAnswer(userEmail,yearMonth,week));
	  anal.setWeeklyHappy(anal.getWeeklyHappy()+emotion.getJoy());
	  anal.setWeeklySad(anal.getWeeklySad()+emotion.getSadness());
	  anal.setWeeklyAngry(anal.getWeeklyAngry()+emotion.getAnger());
	  anal.setWeeklyUneasy(anal.getWeeklyUneasy()+emotion.getAnxiety());
	  anal.setWeeklyBoring(anal.getWeeklyBoring()+emotion.getBoredom());
      }
      weeklyRepository.save(anal);
      Map<String,Object> map = new HashMap<>();
      map.put("response",anal.toDto());
      map.put("list",mostElementChar);
      
      return map;
  }
  
  private PersonalDiaryEmotion findEmotionByUserAndDate(String userEmail, LocalDate date) {
	User user = findUserByUserEmail(userEmail);
	PersonalDiaryEmotion emotion = emotionRepository.findPersonalDiaryEmotionByUserAndDate(user, date);
	return emotion;
  }

  private WeeklyAnalysis getOrCreateWeeklyAnalysis(String userEmail, String yearMonth, String week) {
	User user = findUserByUserEmail(userEmail);
	    return weeklyRepository.findByUserEmailAndWeeklyDate(userEmail, yearMonth+week)
	        .orElseGet(() -> {
	            WeeklyAnalysis newAnalysis = new WeeklyAnalysis();
	            newAnalysis.setUserEmail(userEmail);
	            newAnalysis.setWeeklyDate(yearMonth+week);
	            newAnalysis.setTotalDiary(0);
	            newAnalysis.setTotalAnswer(0);
	            // Initialize other fields with default values if needed
	            return weeklyRepository.save(newAnalysis);
	        });}
  
    // 다음 주 예측 및 조언
    
  
    
}
