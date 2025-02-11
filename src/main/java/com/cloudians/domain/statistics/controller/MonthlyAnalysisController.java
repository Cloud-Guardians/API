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
    private final PersonalDiaryService personalDiaryService;
    
    @GetMapping("/test")
    public ResponseEntity<Message> getMonth(){
	Object thisMonth = monthlyService.getMonth();
	String userEmail = "a@a.com";
	    Message message = new Message(userEmail, HttpStatus.OK.value());
	return ResponseEntity.status(HttpStatus.OK).body(message);
	
    }


	@GetMapping("/monthly/{year}/{month}")
	public ResponseEntity<Message> getMonthlyReport(
			@AuthUser User user,
			@PathVariable("year") String year,
			@PathVariable("month") String month) {

		String yearMonth = year + month;
		MonthlyAnalysis analysis = monthlyService.getMonthlyAnalysis(user, yearMonth);
		MonthlyAnalysisResponse analysisResponse = MonthlyAnalysisResponse.of(analysis);


		Map<String, Object> elementAnalysis = monthlyService.getMonthlyReport(user, year, month);
		FiveElement maxElement = (FiveElement) elementAnalysis.get("max");
		FiveElement minElement = (FiveElement) elementAnalysis.get("min");

		List<String> maxCharacteristics = personalDiaryService.getElementCharacters(maxElement);
		List<String> minCharacteristics = personalDiaryService.getElementCharacters(minElement);

		Map<String, Object> response = new HashMap<>();
		response.put("monthlyAnalysis", analysisResponse);
		response.put("maxElementCharacteristics", maxCharacteristics);
		response.put("minElementCharacteristics", minCharacteristics);
		response.put("dominantElement", maxElement);
		response.put("recessiveElement", minElement);

		Message message = new Message(response, HttpStatus.OK.value());
		return ResponseEntity.ok(message);
	}


}
