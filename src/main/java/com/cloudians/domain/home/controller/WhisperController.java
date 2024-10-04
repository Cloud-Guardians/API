package com.cloudians.domain.home.controller;

import com.cloudians.domain.auth.controller.AuthUser;
import com.cloudians.domain.home.dto.request.WhisperMessageRequest;
import com.cloudians.domain.home.dto.response.GeneralPaginatedResponse;
import com.cloudians.domain.home.dto.response.WhisperMessageResponse;
import com.cloudians.domain.home.dto.response.WhisperResponse;
import com.cloudians.domain.home.service.WhisperService;
import com.cloudians.domain.user.entity.User;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RequestMapping("/home/whisper")
@RestController
@RequiredArgsConstructor
public class WhisperController {
    private final WhisperService whisperService;

    @PostMapping("/answer")
    public ResponseEntity<Message> createAnswer(@AuthUser User user,
                                                @RequestBody @Valid WhisperMessageRequest request) {
        WhisperResponse response = whisperService.createAnswer(user, request);
        Message message = new Message(response, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    @GetMapping()
    public ResponseEntity<Message> getMessages(@AuthUser User user,
                                               @RequestParam(required = false) Long cursor,
                                               @RequestParam(defaultValue = "10") Long count,
                                               @RequestParam(required = false) String keyword,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        GeneralPaginatedResponse<WhisperMessageResponse> response;
        // keyword 검색 처리
        if (keyword != null && !keyword.isEmpty()) {
            response = whisperService.getMessagesByKeyword(user, cursor, count, keyword);
            return createGetMessagesResponseEntity(response);
        }
        // 날짜 검색 처리
        if (date != null) {
            response = whisperService.getMessagesByDate(user, cursor, count, date);
            return createGetMessagesResponseEntity(response);
        }
        // 기본 최근 메시지 조회
        response = whisperService.getRecentMessages(user, cursor, count);
        return createGetMessagesResponseEntity(response);
    }

    private ResponseEntity<Message> createGetMessagesResponseEntity(GeneralPaginatedResponse<WhisperMessageResponse> response) {
        Message message = new Message(response, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}