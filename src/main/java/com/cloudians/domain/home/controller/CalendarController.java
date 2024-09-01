package com.cloudians.domain.home.controller;

import com.cloudians.domain.auth.controller.AuthUser;
import com.cloudians.domain.home.dto.response.CalendarResponse;
import com.cloudians.domain.home.service.CalendarService;
import com.cloudians.domain.personaldiary.dto.response.PersonalDiaryResponse;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/home/calendar")
@RestController
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("/{date}")
    public ResponseEntity<Message> getDiariesInMonth(@AuthUser User user,
                                                     @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CalendarResponse> response = calendarService.getDiariesInThreeMonths(user, date);
        Message message = new Message(response, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }

    @GetMapping("/day/{date}")
    public ResponseEntity<Message> getPersonalDiary(@AuthUser User user,
                                                    @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        PersonalDiaryResponse response = calendarService.getPersonalDiary(user, date);
        Message message = new Message(response, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }
}