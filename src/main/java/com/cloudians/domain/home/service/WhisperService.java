package com.cloudians.domain.home.service;

import com.cloudians.domain.home.dto.request.WhisperMessageRequest;
import com.cloudians.domain.home.dto.response.GeneralPaginatedResponse;
import com.cloudians.domain.home.dto.response.WhisperMessageResponse;
import com.cloudians.domain.home.dto.response.WhisperResponse;
import com.cloudians.domain.home.entity.SenderType;
import com.cloudians.domain.home.entity.ThankYouMessage;
import com.cloudians.domain.home.entity.WhisperMessage;
import com.cloudians.domain.home.entity.WhisperQuestion;
import com.cloudians.domain.home.exception.WhisperException;
import com.cloudians.domain.home.exception.WhisperExceptionType;
import com.cloudians.domain.home.repository.ThankYouMessageRepository;
import com.cloudians.domain.home.repository.WhisperMessageRepository;
import com.cloudians.domain.home.repository.WhisperQuestionRepository;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class WhisperService {
    private final WhisperQuestionRepository whisperQuestionRepository;
    private final ThankYouMessageRepository thankYouMessageRepository;
    private final WhisperMessageRepository whisperMessageRepository;

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void createWhisperQuestion() {
        log.info("스케쥴러 실행");

        LocalDate today = LocalDate.now();
        List<User> users = userRepository.findAll();

        for (User user : users) {
            log.info("유저에게 오늘의 질문 보내기: " + user.getUserEmail() + ", Date: " + today);
            sendQuestion(user, today);
        }
    }

    public void
    sendQuestion(User user, LocalDate today) {
        WhisperQuestion whisperQuestion = getQuestionIfExistsOrThrow(today);
        String content = whisperQuestion.getContent();
        WhisperMessage questionOfToday = WhisperMessage.createChatMessage(user, content, LocalDateTime.now(), SenderType.SYSTEM);
        whisperMessageRepository.save(questionOfToday);
    }

    public WhisperResponse createAnswer(User user, WhisperMessageRequest request) {

        LocalDateTime questionDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime twentyFourHoursLater = questionDateTime.plusHours(24);
        validateAnswerTime(user, questionDateTime, twentyFourHoursLater);

        WhisperMessageResponse userChatMessageResponse = getUserChatMessageResponse(request, user);
        WhisperMessageResponse systemChatMessageResponse = getSystemChatMessageResponse(user);

        return WhisperResponse.of(userChatMessageResponse, systemChatMessageResponse);
    }

    public GeneralPaginatedResponse<WhisperMessageResponse> getRecentMessages(User user, Long cursor, Long count) {

        List<WhisperMessage> messages = whisperMessageRepository.findByUserOrderByTimeStampDesc(user, cursor, count);
        return GeneralPaginatedResponse.of(messages, count, WhisperMessage::getId, WhisperMessageResponse::of);
    }

    public GeneralPaginatedResponse<WhisperMessageResponse> getMessagesByKeyword(User user, Long cursor, Long count, String keyword) {

        List<WhisperMessage> messages = whisperMessageRepository.findBySearchKeywordOrderByTimeStampDesc(user, cursor, count, keyword);
        return GeneralPaginatedResponse.of(messages, count, WhisperMessage::getId, WhisperMessageResponse::of);
    }

    public GeneralPaginatedResponse<WhisperMessageResponse> getMessagesByDate(User user, Long cursor, Long count, LocalDate date) {

        List<WhisperMessage> messages = whisperMessageRepository.findByDateOrderByTimestampDesc(user, cursor, count, date);
        return GeneralPaginatedResponse.of(messages, count, WhisperMessage::getId, WhisperMessageResponse::of);
    }

    private void validateAnswerTime(User user, LocalDateTime questionDateTime, LocalDateTime twentyFourHoursLater) {
        if (whisperMessageRepository.existsByUserAndSenderAndTimestampBetween(user, SenderType.USER, questionDateTime, twentyFourHoursLater)) {
            throw new WhisperException(WhisperExceptionType.ALREADY_EXIST_MESSAGE);
        }
    }

    private WhisperMessageResponse getSystemChatMessageResponse(User user) {
        List<ThankYouMessage> thankYouMessages = thankYouMessageRepository.findAll();

        String systemContent = getRandomSystemContent(thankYouMessages);
        WhisperMessage systemChatMessage = WhisperMessage.createChatMessage(user, systemContent, LocalDateTime.now().plusSeconds(1), SenderType.SYSTEM);
        whisperMessageRepository.save(systemChatMessage);
        return WhisperMessageResponse.of(systemChatMessage);
    }

    private String getRandomSystemContent(List<ThankYouMessage> thankYouMessages) {
        Random random = new Random();
        return thankYouMessages.get(random.nextInt(thankYouMessages.size())).getContent();
    }

    private WhisperMessageResponse getUserChatMessageResponse(WhisperMessageRequest request, User user) {
        WhisperMessage userChatMessage = request.toEntity(user, request.getContent(), LocalDateTime.now(), SenderType.USER);
        whisperMessageRepository.save(userChatMessage);
        return WhisperMessageResponse.of(userChatMessage);
    }

    private WhisperQuestion getQuestionIfExistsOrThrow(LocalDate today) {
        return whisperQuestionRepository.findByDate(today)
                .orElseThrow(() -> new WhisperException(WhisperExceptionType.NON_EXIST_WHISPER_QUESTION));
    }
}