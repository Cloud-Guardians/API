package com.cloudians.domain.statistics.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cloudians.domain.statistics.entity.MonthlyAnalysis;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloudians.domain.auth.controller.AuthUser;
import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import com.cloudians.domain.personaldiary.service.PersonalDiaryService;
import com.cloudians.domain.statistics.dto.response.MonthlyAnalysisResponse;
import com.cloudians.domain.statistics.service.MonthlyAnalysisService;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class MonthlyAnalysisController {
    
    private final MonthlyAnalysisService monthlyService;



	@GetMapping("/monthly/{year}/{month}")
	public ResponseEntity<Message> getMonthlyReport(
			@AuthUser User user,
			@PathVariable("year") String year,
			@PathVariable("month") String month) {

		String yearMonth = year + month;

		// monthlyAnalysis 결과
		MonthlyAnalysis analysis = monthlyService.getMonthlyAnalysis(user, yearMonth);
		Map<String, Object> response = monthlyService.getMonthlyReport(user, analysis);

		Message message = new Message(response, HttpStatus.OK.value());
		return ResponseEntity.ok(message);
	}


}
