package com.cloudians.domain.auth.service;

import com.cloudians.domain.auth.dto.request.*;
import com.cloudians.domain.auth.dto.response.FindPwResponse;
import com.cloudians.domain.auth.dto.response.LoginResponse;
import com.cloudians.domain.auth.entity.UserToken;
import com.cloudians.domain.auth.repository.UserTokenRepository;
import com.cloudians.domain.auth.util.JwtProcessor;
import com.cloudians.domain.auth.dto.response.SignupResponse;
import com.cloudians.domain.user.entity.SignupType;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;

    private final UserTokenRepository userTokenRepository;

    private final JwtProcessor jwtProcessor;

    private final JavaMailSender mailSender;

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
    public void checkIfUserExists(SignupRequest request) {
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
        //TODO: 더 나은 로직 찾기
        User user = jwtProcessor.verifyAuthTokenOrThrow(request.getRefreshToken().substring(7));

        validateBlackListToken(request.getRefreshToken()); // 블랙리스트에 있는 토큰인지
        return jwtProcessor.createAccessToken(user.getUserEmail());
    }

    // 유효 기간 남은 토큰 블랙리스트
    public void logout(User loggedInUser, String refreshToken) {
        //TODO: 더 나은 로직 찾기
        User originalUser = jwtProcessor.verifyAuthTokenOrThrow(refreshToken.substring(7));

        validateTokenOwner(loggedInUser, originalUser);
        validateBlackListToken(refreshToken);

        UserToken userToken = UserToken.blackListFrom(loggedInUser, refreshToken);
        userTokenRepository.save(userToken);
    }

    // 비밀번호를 임시 비밀번호로 변경
    public FindPwResponse updatePassword(FindPwRequest request) {
        userEmailExist(request);

        String tmpPassword = getTmpPassword();
        String encodedPassword = bcryptService.encodeBcrypt(tmpPassword);

        User user = userRepository.findByUserEmail(request.getUserEmail())
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        user.setPassword(encodedPassword);
        userRepository.save(user);

        FindPwResponse findPwResponse = FindPwResponse.from(user, tmpPassword);

        sendMail(findPwResponse);

        return findPwResponse;
    }

    public void resetPassword(ResetPwRequest request) {
        User user = userRepository.findByUserEmail(request.getUserEmail())
                .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));

        if (!bcryptService.matchBcrypt(request.getOldPassword(), user.getPassword())) {
            throw new UserException(UserExceptionType.WRONG_PASSWORD);
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new UserException(UserExceptionType.PASSWORD_DO_NOT_MATCH);
        }

        String resetPassword = bcryptService.encodeBcrypt(request.getNewPassword());

        user.setPassword(resetPassword);
        userRepository.save(user);
    }

    private void sendMail(FindPwResponse response) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(response.getTo());
        mailMessage.setSubject(response.getSubject());
        mailMessage.setText(response.getMessage());
        mailMessage.setFrom(response.getFrom());
        mailMessage.setReplyTo(response.getFrom());

        mailSender.send(mailMessage);
    }

    private User userEmailExist(FindPwRequest request) {
        return userRepository.findByUserEmail(request.getUserEmail())
                .orElseThrow(() -> new UserException((UserExceptionType.USER_NOT_FOUND)));
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

    private void validateTokenOwner(User loggedInUser, User originalUser) {
        if (!loggedInUser.getUserEmail().equals(originalUser.getUserEmail())) {
            throw new UserException(UserExceptionType.USER_NOT_MATCHED);
        }
    }

    private void validateBlackListToken(String refreshToken) {
        boolean isBlackListed = userTokenRepository.existsByTokenValue(refreshToken);
        if (isBlackListed) {
            throw new UserException(UserExceptionType.ALREADY_LOGOUT_TOKEN);
        }
    }

    // tmpPassword 랜덤으로
    private String getTmpPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        int rndAllCharactersLength = rndAllCharacters.length;
        for (int i = 0; i < 15; i++) {
            stringBuilder.append(rndAllCharacters[random.nextInt(rndAllCharactersLength)]);
        }

        String randomPassword = stringBuilder.toString();

        // 최소 8자리에 대문자, 소문자, 숫자, 특수문자 각 1개 이상 포함
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,}";
        if (!Pattern.matches(pattern, randomPassword)) {
            return getTmpPassword();
        }
        return randomPassword;
    }

    private static final char[] rndAllCharacters = new char[]{
            // 숫자
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',

            // 대문자
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',

            // 소문자
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',

            // 특수 기호
            '@', '$', '!', '%', '*', '?', '&'
    };
}



