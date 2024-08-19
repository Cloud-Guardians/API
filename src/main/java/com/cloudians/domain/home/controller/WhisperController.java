package com.cloudians.domain.home.controller;

import com.cloudians.domain.home.dto.request.WhisperMessageRequest;
import com.cloudians.domain.home.dto.response.GeneralPaginatedResponse;
import com.cloudians.domain.home.dto.response.WhisperMessageResponse;
import com.cloudians.domain.home.dto.response.WhisperResponse;
import com.cloudians.domain.home.service.WhisperService;
import com.cloudians.global.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/home/whisper")
@RestController
@RequiredArgsConstructor
public class WhisperController {
    private final WhisperService whisperService;

    @PostMapping("/answer")
    public ResponseEntity<Message> createAnswer(@RequestParam String userEmail,
                                                @RequestBody @Valid WhisperMessageRequest request) {
        WhisperResponse response = whisperService.createAnswer(userEmail, request);
        Message message = new Message(response, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }

    @GetMapping()
    public ResponseEntity<Message> getRecentMessages(@RequestParam String userEmail,
                                                     @RequestParam(required = false) Long cursor,
                                                     @RequestParam(defaultValue = "10") Long count) {
        GeneralPaginatedResponse<WhisperMessageResponse> response = whisperService.getRecentMessages(userEmail, cursor, count);
        Message message = new Message(response, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }
}
