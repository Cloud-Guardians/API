package com.cloudians.domain.statistics.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import com.cloudians.domain.personaldiary.service.PersonalDiaryService;
import com.cloudians.domain.statistics.dto.response.CollectionResponse;
import com.cloudians.domain.statistics.dto.response.MonthlyAnalysisResponse;
import com.cloudians.domain.statistics.service.MonthlyAnalysisService;
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
    public ResponseEntity<Message> monthlyReport(@RequestParam("userEmail") String userEmail, @PathVariable("year") String year, @PathVariable("month") String month){	
	String yearMonth = year+month;
	MonthlyAnalysisResponse response = monthlyService.getMonthlyAnalysis(userEmail, yearMonth);
	Map<String, Object> map = monthlyService.getMonthlyReport(userEmail,yearMonth);
	FiveElement max = (FiveElement)map.get("max");
	System.out.println("컨트롤러에서:"+max.toString());
	FiveElement min = (FiveElement)map.get("min");
	List<String> maxChar = personalDiaryService.getElementCharacters(max);
	System.out.println(maxChar.toString());
	List<String> minChar = personalDiaryService.getElementCharacters(min);
	
	Map<String, Object> result = new HashMap<>();
	result.put("monthlyAnalysis",response);
	result.put("maxCharacter",maxChar);
	result.put("minCharacter",minChar);
	Message message = new Message(result, HttpStatus.OK.value());
	return ResponseEntity.status(HttpStatus.OK).body(message);
	
	
    }
    
    @GetMapping("/collection")
    public ResponseEntity<Message> monthlyCollection(@RequestParam String userEmail, @RequestParam String yearMonth){
	List<CollectionResponse> collection = monthlyService.getMonthlyCollection(userEmail, yearMonth);
	Message message = new Message(collection, HttpStatus.OK.value());
	return ResponseEntity.status(HttpStatus.OK).body(message);
	
    }
   
    

    

}
