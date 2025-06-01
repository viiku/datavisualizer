//package com.viiku.datavisualizer.common.exception;
//
//import com.viiku.datavisualizer.model.dtos.payload.response.ApiErrorResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@ControllerAdvice
//public class ErrorHandler {
//
//    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
//        log.error("Caught Exception", ex);
//        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
//                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .message("An expected error occurred")
//                .build();
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
//        log.error("Caught Illegal Argument Exception", ex);
//        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
//                .status(HttpStatus.BAD_REQUEST.value())
//                .message(ex.getMessage())
//                .build();
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(IllegalStateException.class)
//    public ResponseEntity<ApiErrorResponse> handleIllegalStateException(IllegalStateException ex) {
//        log.error("Caught Illegal State Exception", ex);
//        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
//                .status(HttpStatus.CONFLICT.value())
//                .message(ex.getMessage())
//                .build();
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
//    }

//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
//        log.error("Caught Bad Credentials Exception", ex);
//        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
//                .status(HttpStatus.UNAUTHORIZED.value())
//                .message("Incorrect Username or Password")
//                .build();
//
//        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
//    }
//}
