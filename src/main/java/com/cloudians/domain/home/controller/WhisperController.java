package com.cloudians.domain.home.controller;

import com.cloudians.domain.home.dto.request.WhisperMessageRequest;
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

    @PostMapping("/{whisper-question-id}/answer")
    public ResponseEntity<Message> createAnswer(@RequestParam String userEmail,
                                                @PathVariable("whisper-question-id") Long whisperQuestionId,
                                                @RequestBody @Valid WhisperMessageRequest request) {
        WhisperResponse response = whisperService.createAnswer(userEmail, whisperQuestionId, request);
        Message message = new Message(response, HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(message);
    }
}
