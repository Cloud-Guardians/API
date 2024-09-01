package com.cloudians.domain.auth.service;


import java.util.Random;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.cloudians.domain.auth.dto.request.LoginRequest;
import com.cloudians.domain.auth.dto.request.SignupRequest;
import com.cloudians.domain.auth.dto.response.SignupResponse;
import com.cloudians.domain.auth.util.JwtProcessor;
import com.cloudians.domain.user.entity.SignupType;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final JwtProcessor jwtProcessor;
    private final BcryptService bcryptService;
    
    
    public String getUserEmail(String token) {
	return jwtProcessor.verifyAuthTokenOrThrow(token).getUserEmail();
    }
    
    public SignupResponse signup(SignupRequest request){
        String encodedPassword = bcryptService.encodeBcrypt(request.getPassword());
       //TODO: 닉네임 랜덤 생성 필요
        String nickname = uniqueNickname();

        User user = request.toUser(encodedPassword, nickname);
        user.setSignupType(SignupType.REGULAR);
        userRepository.save(user);

        return SignupResponse.from(user);
    }

    public String login(@Valid LoginRequest request) {
        User user = getUserOrThrow(request);

        validatePassword(request, user);
        return jwtProcessor.createAccessToken(user.getUserEmail());
    }

    private void validatePassword(LoginRequest request, User user) {
        boolean isMatchingPassword = bcryptService.matchBcrypt(request.getPassword(), user.getPassword());
        // plain: getPassword
        if(!isMatchingPassword){
            throw new UserException(UserExceptionType.WRONG_PASSWORD);
        }
    }

    private User getUserOrThrow(LoginRequest request) {
        User user = userRepository.findByUserEmail(request.getUserEmail())
                .orElseThrow(() -> new UserException((UserExceptionType.USER_NOT_FOUND)));
        return user;
    }

    private static String randomNickname(){
        String[] adjectives = {"배고픈", "행복한", "똑똑한", "즐거운", "강한", "빠른", "재치있는",
                "충성스러운", "멋진", "훌륭한", "즐거운", "아름다운", "기쁜", "사랑스러운", "행복한",
                "환상적인", "놀라운", "훌륭한", "매력적인", "긍정적인", "빛나는", "희망찬", "용감한",
                "따뜻한", "신나는", "친절한", "든든한", "감동적인", "뛰어난", "성실한", "창의적인", "자랑스러운", "유쾌한"};

        String[] nouns = {"사자", "호랑이", "독수리", "상어", "판다", "여우", "늑대", "용", "곰",
                "매", "강아지", "고양이", "토끼", "햄스터", "앵무새", "거북이", "고슴도치", "물고기",
                "말", "돌고래", "펭귄", "코알라", "기린", "수달", "코끼리", "펭귄", "기니피그", "용"};

        Random random = new Random();

        // 랜덤 형용사
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];
        String randomNum = String.valueOf(random.nextInt(999));

        return adjective + " " + noun + randomNum;
    }

    private String uniqueNickname(){
        String nickname;
        do{
            nickname = randomNickname();
        }while (userRepository.findByNickname(nickname).isPresent());
        return nickname;
    }
}
