//package com.cloudians.global.config;
//
//import com.cloudians.domain.auth.service.PrincipalDetailsService;
//import com.cloudians.domain.auth.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Autowired
//    private PrincipalDetailsService principalDetailsService; // 사용자 세부 정보 서비스
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        final String bearerToken = request.getHeader("Authorization");
//
//        String username = null;
//        String jwtToken = null;
//
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            jwtToken = bearerToken.substring(7);
//            username = jwtUtil.extractEmail(jwtToken);
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            if (jwtUtil.validateToken(jwtToken)) {
//                UserDetails userDetails = principalDetailsService.loadUserByUsername(username);
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        chain.doFilter(request, response);
//    }
//}
