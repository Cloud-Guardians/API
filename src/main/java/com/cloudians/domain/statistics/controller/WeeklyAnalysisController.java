package com.cloudians.domain.statistics.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudians.domain.auth.controller.AuthUser;
import com.cloudians.domain.personaldiary.service.PersonalDiaryService;
import com.cloudians.domain.statistics.service.WeeklyAnalysisService;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/statistics/weekly")
@RequiredArgsConstructor
public class WeeklyAnalysisController {
    
    private final WeeklyAnalysisService weeklyService;
    
    @GetMapping("/{year}/{month}/{week}")
    public ResponseEntity<Message> getMonth(@AuthUser User user, @PathVariable("year") String year, @PathVariable("month") String month, @PathVariable("week") String week){
	String yearMonth = year+month;
	Map<String, Object> thisMonth = weeklyService.getWeeklyAnalysis(user,yearMonth,week);
	    Message message = new Message(thisMonth, HttpStatus.OK.value());
	return ResponseEntity.status(HttpStatus.OK).body(message);
	
    }

}
