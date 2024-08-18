package com.cloudians.global.exception;

import com.cloudians.global.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Message> handleBaseEx(BaseException exception) {
        Message message = new Message(
                exception.getExceptionType().getErrorMessage(),
                exception.getExceptionType().getStatusCode()
        );

        return new ResponseEntity<>(
                message,
                exception.getExceptionType().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Message> handleValidationExceptions(MethodArgumentNotValidException exception) {

        // @Valid에서 넘어온 에러메세지 추출
        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();

        // 에러메세지를 담은 응답 메세지 형성
        Message message = new Message(errorMessage, 400);
        return new ResponseEntity<>(
                message,
                HttpStatus.BAD_REQUEST
        );
    }
}