package com.cloudians.domain.user.controller;

import com.cloudians.domain.user.dto.request.UserRequest;
import com.cloudians.domain.user.dto.response.UserResponse;
import com.cloudians.domain.user.service.UserService;
import com.cloudians.global.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return new ResponseEntity<>("Test endpoint working", HttpStatus.OK);
    }
    // 프로필 생성


    // 프로필 조회 (firebase storage 설정 이후 정하기)
    @GetMapping("/profile")
    public ResponseEntity<?> userProfile(@RequestParam String userEmail) {
        return null;

    }

    // 프로필 수정 (firebase storage 설정 이후 정하기)
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> userProfileUpdate(@RequestParam String userEmail,
                                                          @RequestBody UserRequest userRequest) {// 사용자 정보를 업데이트하는 서비스 호출
        System.out.println("controller - profile - modify");
        return null;
    }

    // 회원 가입

    // 내 정보 조회
//    @GetMapping("/user-info")
//    public ResponseEntity<?> userInfo(@RequestParam String userEmail) {
//        try {
//            System.out.println(userEmail);
//            UserResponse userInfo = userService.findByEmail(userEmail);
//            return new ResponseEntity<>(userInfo, HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
//        }
//
//    }

    @GetMapping("/user-info")
    public ResponseEntity<Message> userInfo2(@RequestParam String userEmail) {
            UserResponse userInfo = userService.findByEmail2(userEmail);

            Message message = new Message(userInfo, HttpStatus.OK.value());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
    }

    // 내 정보 수정
    @PutMapping("/user-info")
    public ResponseEntity<UserResponse> userInfoUpdate(@RequestParam String userEmail,
                                                       @RequestBody UserRequest userRequest) {// 사용자 정보를 업데이트하는 서비스 호출
        System.out.println("controller - modify");
        UserResponse updatedUser = userService.updateUser(userEmail, userRequest);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }


// 유저 작성글 조회

// 유저 댓글 조회


// 앱 잠금 설정 생성/삭제

// 앱 실행 시 잠금 설정 화면


// 오류 제보


// 커뮤니티 알림 생성/삭제

// 커뮤니티 알림 조회


// 홈 알림 생성/삭제

// 홈 알림 수정


}
