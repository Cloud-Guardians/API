package com.cloudians.global.filter;

import com.cloudians.domain.admin.exception.AdminException;
import com.cloudians.domain.admin.exception.AdminExceptionType;
import com.cloudians.domain.auth.util.JwtProcessor;
import com.cloudians.domain.user.entity.User;
import com.cloudians.domain.user.entity.UserStatus;
import com.cloudians.domain.user.exception.UserException;
import com.cloudians.domain.user.exception.UserExceptionType;
import com.cloudians.global.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 나중에 firebase 필터랑 순서 정해서 하기 1번 필터
@Component
@RequiredArgsConstructor     // OncePerRequestFilter: 필터 처리 한 번 함
public class AuthFilter extends OncePerRequestFilter {

    private final JwtProcessor jwtProcessor;

    private static final String AUTH_API = "/api/auth";

    private static final String AUTH_LOGOUT_API = "/api/auth/logout";

    private static final String ADMIN = "/api/admin";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (shouldSkipFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            User loginUser = getAuthenticatedUserFromToken(request);
            request.setAttribute("user", loginUser);

            if (isAdminFilter(request) && !isUserAdmin(loginUser)) {
                throw new UserException(UserExceptionType.FORBIDDEN_ACCESS);
            }

            filterChain.doFilter(request, response);
        } catch (UserException e) {
            handleException(response, e);
        }
    }

    // 관리자 경로
    private boolean isAdminFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith(ADMIN);
    }

    // 유저가 관리자인지 검증
    private boolean isUserAdmin(User user) {
       UserStatus ADMIN = UserStatus.ADMIN;
        return ADMIN.equals(user.getStatus());
    }

    // /api/auth는 로그인해야 돼서 필터 처리 안 함
    private boolean shouldSkipFilter(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        // request에서 uri 구분
        if (requestUri.startsWith(AUTH_API) && !requestUri.equals(AUTH_LOGOUT_API))
            return true;
        return false;
    }

    private User getAuthenticatedUserFromToken(HttpServletRequest request) {
        String accessToken = getAccessToken(request);
        User user = jwtProcessor.verifyAuthTokenOrThrow(accessToken);
        return user;
    }

    private String getAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UserException(UserExceptionType.NULL_TOKEN);
        }
        return authorization.substring(7);
    }

    private void handleException(HttpServletResponse response, UserException e) throws IOException {
        response.setStatus(e.getExceptionType().getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Message message = new Message(
                e.getExceptionType().getErrorMessage(),
                e.getExceptionType().getStatusCode()
        );
        response.getWriter().write(new ObjectMapper().writeValueAsString(message));
    }
}