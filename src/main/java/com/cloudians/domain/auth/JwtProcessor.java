package com.cloudians.domain.auth;

import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProcessor {

    // @Value("${jwt.secret.key}")
    private final String SECRET_KEY = "dfhiwehdkfsjdxifsnfaklansdfiwenfsdklfnkelnfklneiofdf";

    public static final String USER_EMAIL_KEY = "userEmail";
    // cmd shift enter ;
    public static final int ACCESS_TOKEN_EXP = 60 * 60 * 1000;

    private final UserRepository userRepository;

    public String createAccessToken(String userEmail) {

        Claims claims = createClaims(userEmail);

        Date expiresIn = createExpiresIn();
        Key signingKey = createSigningKey();

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiresIn)
                .signWith(signingKey)
                .compact();
        // option cmd m
    }

    public User verifyAuthTokenOrThrow(String accessToken) {
        try {
            Jws<Claims> claimJws = parseToClaimsJws(accessToken);
            Claims claims = claimJws.getBody();
            String userEmail = String.valueOf(claims.get(USER_EMAIL_KEY)).toString();
            return userRepository.findByUserEmail(userEmail)
                    .orElseThrow(() -> new UserException(UserExceptionType.USER_NOT_FOUND));
        } catch (ExpiredJwtException expiredJwtException) {

            throw new UserException(UserExceptionType.TOKEN_EXPIRED);
        }catch(JwtException jwtException){

            throw new UserException(UserExceptionType.TOKEN_INVALID);
        }
    }

    private Jws<Claims> parseToClaimsJws(String accessToken) {

        Key signingKey = createSigningKey();
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(accessToken);
    }

    private Claims createClaims(String userEmail) {
        Claims claims = Jwts.claims();
        // OPTION CMD V = 변수로 만들어 줌
        claims.put(USER_EMAIL_KEY, userEmail);
        return claims;
    }

    private Date createExpiresIn() {
        long currentDateTime = new Date().getTime();
        Date ExpiresIn = new Date(currentDateTime + ACCESS_TOKEN_EXP);
        return new Date(currentDateTime + ACCESS_TOKEN_EXP);
    }

    private Key createSigningKey() {
        byte[] secretKeyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

}
