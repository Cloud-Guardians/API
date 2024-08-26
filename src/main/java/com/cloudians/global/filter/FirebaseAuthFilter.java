//package com.cloudians.global.filter;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.cloudians.domain.user.service.UserService;
//import com.cloudians.global.Message;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.auth.FirebaseToken;
//
//@Component
//public class FirebaseAuthFilter extends OncePerRequestFilter {
//
//    private UserService userService;
//    private FirebaseAuth fireAuth;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//	String uid = "c51e4662168a7a006bf6082dcd7a16ba5ff3fb0b";
//	System.out.println("Authorization이 시작됩니다.");
//        String token = request.getHeader("Authorization");
//        System.out.println("token 등록 완료:"+token);
//
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7); // Remove "Bearer " prefix
//            System.out.println("Bearer 제거:"+token);
//            try {
//        	System.out.println("id token을 넣겠습니다.");
//                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
//                System.out.println("decodedToken 생성 완료"+decodedToken);
//                request.setAttribute("user", decodedToken);
//                System.out.println(request.getAttribute("user"));
//            } catch (FirebaseAuthException e) {
//        	System.out.println(e);
//                response.getWriter().write((new Message(e,HttpStatus.UNAUTHORIZED.value())).toString());
//                return;
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}