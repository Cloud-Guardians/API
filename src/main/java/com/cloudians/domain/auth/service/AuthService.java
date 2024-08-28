package com.cloudians.domain.auth.service;

import com.cloudians.domain.auth.dto.request.TokenRefreshRequest;
import com.cloudians.domain.auth.dto.response.LoginResponse;
import com.cloudians.domain.auth.util.JwtProcessor;
import com.cloudians.domain.auth.dto.request.LoginRequest;
import com.cloudians.domain.auth.dto.request.SignupRequest;
import com.cloudians.domain.auth.dto.response.SignupResponse;
import com.cloudians.domain.user.entity.SignupType;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;

    private final JwtProcessor jwtProcessor;

    private final BcryptService bcryptService;

    // 회원가입
    public SignupResponse signup(SignupRequest request) {
        checkIfUserExists(request);
        String encodedPassword = bcryptService.encodeBcrypt(request.getPassword());
        String nickname = uniqueNickname();
        User user = request.toUser(encodedPassword, nickname);
        user.setSignupType(SignupType.REGULAR);
        userRepository.save(user);
        return SignupResponse.from(user);
    }

    // 회원가입 중복 여부
    private void checkIfUserExists(SignupRequest request) {
        if (userRepository.findByUserEmail(request.getUserEmail()).isPresent()) {
            throw new UserException(UserExceptionType.USER_ALREADY_EXIST);
        }
    }

    // 로그인
    public LoginResponse login(LoginRequest request) {
        User user = getUserOrThrow(request);
        validatePassword(request, user);
        String accessToken = jwtProcessor.createAccessToken(user.getUserEmail());
        String refreshToken = jwtProcessor.createRefreshToken(user.getUserEmail());
        return LoginResponse.of(accessToken, refreshToken);
    }

    // 들어온 토큰 검증
    public String refreshAccessToken(@Valid TokenRefreshRequest request) {
        User user = jwtProcessor.verifyAuthTokenOrThrow(request.getRefreshToken());
        return jwtProcessor.createAccessToken(user.getUserEmail());
    }

    // 암호화된 비밀번호과 매칭
    private void validatePassword(LoginRequest request, User user) {
        boolean isMatchingPassword = bcryptService.matchBcrypt(request.getPassword(), user.getPassword());
        // plain: getPassword
        if (!isMatchingPassword) {
            throw new UserException(UserExceptionType.WRONG_PASSWORD);
        }
    }

    // 아이디 존재 여부
    private User getUserOrThrow(LoginRequest request) {
        return userRepository.findByUserEmail(request.getUserEmail())
                .orElseThrow(() -> new UserException((UserExceptionType.USER_NOT_FOUND)));
    }

    // 회원가입 시 랜덤 닉네임
    private static String randomNickname() {
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

    // 같은 닉네임 부여 시 새로 리턴
    private String uniqueNickname() {
        String nickname;
        do {
            nickname = randomNickname();
        } while (userRepository.findByNickname(nickname).isPresent());
        return nickname;
    }
}


