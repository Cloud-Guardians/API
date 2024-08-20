package com.cloudians.domain.personaldiary.service;

import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryCreateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionCreateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryEmotionUpdateRequest;
import com.cloudians.domain.personaldiary.dto.request.PersonalDiaryUpdateRequest;
import com.cloudians.domain.personaldiary.dto.response.*;
import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.PersonalDiaryEmotion;
import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import com.cloudians.domain.personaldiary.entity.analysis.FiveElementCharacter;
import com.cloudians.domain.personaldiary.entity.analysis.HarmonyTip;
import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryException;
import com.cloudians.domain.personaldiary.exception.PersonalDiaryExceptionType;
import com.cloudians.domain.personaldiary.repository.*;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;
import com.cloudians.global.exception.FirebaseException;
import com.cloudians.global.exception.JsonException;
import com.cloudians.global.exception.JsonExceptionType;
import com.cloudians.global.service.FirebaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonalDiaryService {

    private final ChatGptService chatGptService;
    private final FirebaseService firebaseService;

    private final UserRepository userRepository;
    private final PersonalDiaryRepository personalDiaryRepository;
    private final PersonalDiaryEmotionRepository personalDiaryEmotionRepository;
    private final PersonalDiaryAnalysisRepository personalDiaryAnalysisRepository;
    private final FiveElementCharacterRepository fiveElementCharacterRepository;
    private final FiveElementRepository fiveElementRepository;
    private final HarmonyTipRepository harmonyTipRepository;

    private Map<String, PersonalDiaryEmotion> tempEmotions = new HashMap<>();
    public static String DOMAIN = "diary";
    public static String FILE_NAME_FORMAT = "YYYYMMdd";
    public static String ACTIVITY_TAG1 = "#활동적";
    public static String ACTIVITY_TAG2 = "#차분한";
    public static String ACTIVITY_TAG3 = "#창의적";
    public static String ANSWER_START_REGEX = ": ";
    public static String NEW_LINE_REGEX = "\n";
    public static int EMOTION_DIVISOR = 10;


    public PersonalDiaryEmotionCreateResponse createTempSelfEmotions(PersonalDiaryEmotionCreateRequest request, String userEmail) {
        User user = findUserByUserEmail(userEmail);
        //감정 수치 검증
        validateEmotionsValue(request);
        PersonalDiaryEmotion personalDiaryEmotion = request.toEntity(user);
        //감정들 임시저장소에 저장
        tempEmotions.put(user.getUserEmail(), personalDiaryEmotion);

        return PersonalDiaryEmotionCreateResponse.of(personalDiaryEmotion, user);
    }


    public PersonalDiaryEmotionUpdateResponse editSelfEmotions(PersonalDiaryEmotionUpdateRequest request, Long emotionId, String userEmail) {
        User user = findUserByUserEmail(userEmail);
        // 수정할 감정이 있는지 확인
        PersonalDiaryEmotion emotions = personalDiaryEmotionRepository.findByIdAndUser(emotionId, user).orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
        //수정
        PersonalDiaryEmotion editedEmotions = emotions.edit(request);

        return PersonalDiaryEmotionUpdateResponse.of(editedEmotions);
    }

    public PersonalDiaryCreateResponse createPersonalDiary(PersonalDiaryCreateRequest request, String userEmail, MultipartFile file) {
        User user = findUserByUserEmail(userEmail);
        //감정 수치 가져오기
        PersonalDiaryEmotion emotions = getTempEmotion(user.getUserEmail());
        //없으면 예외처리
        validateEmotionsExistenceAndThrow(emotions);
        //이미 해당날짜에 일기 썼는지 확인
        validateDuplicateDateDiaryAndThrow(user, emotions);
        //감정들 저장 및 임시저장소에서 삭제
        personalDiaryEmotionRepository.save(emotions);
        removeTempEmotion(user.getUserEmail());
        //사진 업로드
        String photoUrl = uploadPhoto(userEmail, file, emotions.getDate());
        PersonalDiary personalDiary = request.toEntity(user, emotions, photoUrl);
        //일기 저장
        PersonalDiary savedPersonalDiary = personalDiaryRepository.save(personalDiary);
        analyzePersonalDiary(user, savedPersonalDiary);

        return PersonalDiaryCreateResponse.of(savedPersonalDiary, user, emotions);
    }

    public PersonalDiaryResponse getPersonalDiary(String userEmail, Long personalDiaryId) {
        User user = findUserByUserEmail(userEmail);

        PersonalDiary personalDiary = getPersonalDiaryOrThrow(personalDiaryId, user);
        return PersonalDiaryResponse.of(personalDiary);
    }

    public PersonalDiaryResponse editPersonalDiary(PersonalDiaryUpdateRequest request, Long personalDiaryId, String userEmail, MultipartFile file) {
        User user = findUserByUserEmail(userEmail);
        // 수정할 일기가 있는지 확인
        PersonalDiary personalDiary = getPersonalDiaryOrThrow(personalDiaryId, user);
        //파일이 있으면 새로운 사진으로 수정 및 기존 사진 삭제, 파일이 없으면 그대로
        String updatedPhotoUrl = personalDiary.getPhotoUrl();
        if (file != null) {
            updatedPhotoUrl = updateDiaryPhoto(userEmail, file, personalDiary);
        }
        PersonalDiary editedPersonalDiary = personalDiary.edit(request, updatedPhotoUrl);
        analyzeEditedPersonalDiary(user, editedPersonalDiary);

        return PersonalDiaryResponse.of(editedPersonalDiary);
    }

    private String updateDiaryPhoto(String userEmail, MultipartFile file, PersonalDiary personalDiary) {
        String fileName = getFileName(personalDiary.getDate());
        try {
            firebaseService.getFileUrl(userEmail, DOMAIN, fileName);
            firebaseService.deleteFileUrl(userEmail, DOMAIN, fileName);
        } catch (FirebaseException ignored) {
        }
        return uploadPhoto(userEmail, file, personalDiary.getDate());
    }

    public void analyzeEditedPersonalDiary(User user, PersonalDiary editedPersonalDiary) {

        String[] analysisResults = analyzeDiaryWithChatGPT(editedPersonalDiary, user);
        FiveElement element = getElementOrThrow(analysisResults);
        List<String> characters = getElementCharacters(element);
        String harmonyTipsJson = getHarmonyTipsJson();

        PersonalDiaryAnalysis personalDiaryAnalysis = getPersonalDiaryAnalysisOrThrow(editedPersonalDiary.getPersonalDiaryAnalysis().getId());
        personalDiaryAnalysis.edit(user, editedPersonalDiary, element, characters, harmonyTipsJson, analysisResults);
    }

    public void deletePersonalDiary(String userEmail, Long personalDiaryId) {
        User user = findUserByUserEmail(userEmail);
        PersonalDiary personalDiary = getPersonalDiaryOrThrow(personalDiaryId, user);

        deletePhotoIfExists(userEmail, personalDiary);
        personalDiaryRepository.delete(personalDiary);
        personalDiaryEmotionRepository.delete(personalDiary.getEmotion());
        personalDiaryAnalysisRepository.delete(personalDiary.getPersonalDiaryAnalysis());
    }


    public PersonalDiaryAnalyzeResponse getAnalyze(String userEmail, Long personalDiaryId) {
        User user = findUserByUserEmail(userEmail);
        PersonalDiary personalDiary = getPersonalDiaryOrThrow(personalDiaryId, user);
        PersonalDiaryAnalysis personalDiaryAnalysis = getPersonalDiaryAnalysisOrThrow(personalDiary.getPersonalDiaryAnalysis().getId());

        return PersonalDiaryAnalyzeResponse.of(personalDiaryAnalysis, user);
    }

    public void deletePersonalDiaryPhoto(String userEmail, Long personalDiaryId) {
        User user = findUserByUserEmail(userEmail);
        PersonalDiary personalDiary = getPersonalDiaryOrThrow(personalDiaryId, user);

        deletePhotoIfExists(userEmail, personalDiary);
        personalDiary.deletePhotoUrl();
    }

    private void analyzePersonalDiary(User user, PersonalDiary personalDiary) {

        String[] analysisResults = analyzeDiaryWithChatGPT(personalDiary, user);
        FiveElement element = getElementOrThrow(analysisResults);
        List<String> characters = getElementCharacters(element);
        String harmonyTipsJson = getHarmonyTipsJson();

        PersonalDiaryAnalysis personalDiaryAnalysis = PersonalDiaryAnalysis.createPersonalDiaryAnalysis(user, personalDiary, element, characters, harmonyTipsJson, analysisResults);
        personalDiaryAnalysisRepository.save(personalDiaryAnalysis);
        personalDiary.linkPersonalDiaryAnalysis(personalDiaryAnalysis);
    }

    private PersonalDiaryAnalysis getPersonalDiaryAnalysisOrThrow(Long diaryAnalysisId) {
        return personalDiaryAnalysisRepository.findById(diaryAnalysisId).orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY_ANALYSIS));
    }

    private FiveElement getElementOrThrow(String[] analysisResults) {
        return fiveElementRepository.findByName(analysisResults[0]).orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.COUDNT_FOUND_ELEMENT));
    }

    private String[] analyzeDiaryWithChatGPT(PersonalDiary personalDiary, User user) {
        String answer = chatGptService.askQuestion(personalDiary, user);
        System.out.println("answer = " + answer);
        String[] lines = answer.split(NEW_LINE_REGEX);

        return new String[]{extractValue(lines, 0),  // elementName
                extractValue(lines, 1),  // fortuneDetail
                extractValue(lines, 2)   // advice
        };
    }

    private String extractValue(String[] lines, int index) {
        if (lines.length <= index || !lines[index].contains(ANSWER_START_REGEX)) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.WRONG_CHATGPT_ANSWER_FORMAT);
        }
        return lines[index].split(ANSWER_START_REGEX)[1].trim();
    }

    private List<String> getElementCharacters(FiveElement element) {
        return fiveElementCharacterRepository.findRandomCharactersByElementId(element.getId()).stream().map(FiveElementCharacter::getCharacter).collect(Collectors.toList());
    }

    private String getHarmonyTipsJson() {
        List<HarmonyTip> harmonyTips = harmonyTipRepository.findRandomTipsByTags(ACTIVITY_TAG1, ACTIVITY_TAG2, ACTIVITY_TAG3);
        List<HarmonyTipsResponse> harmonyTipsResponse = harmonyTips.stream().map(HarmonyTipsResponse::of).collect(Collectors.toList());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(harmonyTipsResponse);
        } catch (JsonProcessingException e) {
            throw new JsonException(JsonExceptionType.INVALID_JSON_FORMAT);
        }
    }

    private void deletePhotoIfExists(String userEmail, PersonalDiary personalDiary) {
        validateIfPhotoExistsOrThrow(personalDiary);
        if (personalDiary.getPhotoUrl() != null) {
            firebaseService.deleteFileUrl(userEmail, DOMAIN, getFileName(personalDiary.getDate()));
        }
    }

    private void validateIfPhotoExistsOrThrow(PersonalDiary personalDiary) {
        if (personalDiary.getPhotoUrl() == null) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY_PHOTO);
        }
    }

    private String uploadPhoto(String userEmail, MultipartFile file, LocalDate diaryDate) {
        return file != null ? firebaseService.uploadFile(file, userEmail, getFileName(diaryDate), DOMAIN) : null;
    }

    private String getFileName(LocalDate diaryDate) {
        return diaryDate.format(DateTimeFormatter.ofPattern(FILE_NAME_FORMAT));
    }

    private PersonalDiary getPersonalDiaryOrThrow(Long personalDiaryId, User user) {
        return personalDiaryRepository.findByIdAndUser(personalDiaryId, user).orElseThrow(() -> new PersonalDiaryException(PersonalDiaryExceptionType.NON_EXIST_PERSONAL_DIARY));
    }

    private void validateEmotionsValue(PersonalDiaryEmotionCreateRequest request) {
        List<Integer> emotions = Arrays.asList(request.getJoy(), request.getSadness(), request.getAnger(), request.getAnxiety(), request.getBoredom());
        emotions.forEach(this::validateEmotionValue);
    }

    private void validateEmotionValue(int emotion) {
        if (emotion % EMOTION_DIVISOR != 0) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.EMOTION_VALUE_WRONG_INPUT);
        }
    }

    private PersonalDiaryEmotion getTempEmotion(String userEmail) {
        return tempEmotions.get(userEmail);
    }

    private void removeTempEmotion(String userEmail) {
        tempEmotions.remove(userEmail);
    }


    private void validateEmotionsExistenceAndThrow(PersonalDiaryEmotion emotions) {
        if (emotions == null) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.NO_EMOTION_DATA);
        }
    }

    private void validateDuplicateDateDiaryAndThrow(User user, PersonalDiaryEmotion emotions) {
        boolean isExist = personalDiaryRepository.existsPersonalDiaryByUserAndDate(user, emotions.getDate());
        if (isExist) {
            throw new PersonalDiaryException(PersonalDiaryExceptionType.PERSONAL_DIARY_ALREADY_EXIST);
        }
    }

    private User findUserByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail).orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
    }
}