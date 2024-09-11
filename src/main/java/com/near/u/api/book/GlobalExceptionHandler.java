package com.near.u.api.book;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BookWasNotBorrowedException.class})
    public ResponseEntity<ExceptionResponse> bookWasNotBorrowedExceptionHandler(BookWasNotBorrowedException e){
        return createResponseEntity(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler({BookNotFoundException.class})
    public ResponseEntity<ExceptionResponse> bookNotFoundExceptionHandler(BookNotFoundException e){
        return createResponseEntity(HttpStatus.NO_CONTENT, e);
    }

    @ExceptionHandler({BookIsAlreadyBorrowedException.class})
    public ResponseEntity<ExceptionResponse> bookIsAlreadyBorrowedExceptionHandler(BookIsAlreadyBorrowedException e){
        return createResponseEntity(HttpStatus.BAD_REQUEST, e);
    }

    private ResponseEntity createResponseEntity(HttpStatus httpStatus, RuntimeException e){
        return ResponseEntity
                .status(httpStatus)
                .body(
                        ExceptionResponse.builder()
                                .statusCode(httpStatus.value())
                                .message(e.getMessage())
                                .build()
                );
    }

}
