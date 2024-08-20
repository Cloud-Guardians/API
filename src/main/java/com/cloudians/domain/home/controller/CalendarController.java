package com.cloudians.domain.home.controller;

import com.cloudians.domain.home.dto.response.CalendarResponse;
import com.cloudians.domain.home.service.CalendarService;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/home/calendar")
@RestController
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("/{date}")
    public ResponseEntity<Message> getDiariesInMonth(@RequestParam String userEmail,
                                                     @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CalendarResponse> response = calendarService.getDiariesInMonth(userEmail, date);
        Message message = new Message(response, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }
}
