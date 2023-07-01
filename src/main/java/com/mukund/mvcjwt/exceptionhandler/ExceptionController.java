package com.mukund.mvcjwt.exceptionhandler;

import java.time.ZonedDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
                return ResponseEntity.status(status).body(new CustomErrorResponse(status.toString(),
                                ((ServletWebRequest) request).getRequest().getRequestURI(), ZonedDateTime.now(),
                                "Invalid Field Values",
                                ex.getAllErrors().stream().map(error -> error.getDefaultMessage()).toList()));
        }

        @Override
        protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
                return ResponseEntity.status(status).body(new CustomErrorResponse(status.toString(),
                                ((ServletWebRequest) request).getRequest().getRequestURI(), ZonedDateTime.now(),
                                ex.getLocalizedMessage(), null));
        }

        @ExceptionHandler({ ResourceNotFoundException.class })
        public ResponseEntity<CustomErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatusCode.valueOf(404))
                                .headers(headers -> headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON)).body(
                                                new CustomErrorResponse(HttpStatusCode.valueOf(404).toString(),
                                                                request.getRequestURI(),
                                                                ZonedDateTime.now(),
                                                                ex.getMessage(),
                                                                null));
        }

        @ExceptionHandler({ Exception.class })
        public ResponseEntity<CustomErrorResponse> handleException(Exception ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatusCode.valueOf(500))
                                .headers(headers -> headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON)).body(
                                                new CustomErrorResponse(HttpStatusCode.valueOf(500).toString(),
                                                                request.getRequestURI(),
                                                                ZonedDateTime.now(),
                                                                ex.getLocalizedMessage(),
                                                                null));
        }

        @ExceptionHandler({ AccessDeniedException.class })
        public ResponseEntity<CustomErrorResponse> handleException(AccessDeniedException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatusCode.valueOf(403))
                                .headers(headers -> headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON)).body(
                                                new CustomErrorResponse(HttpStatusCode.valueOf(403).toString(),
                                                                request.getRequestURI(),
                                                                ZonedDateTime.now(),
                                                                ex.getLocalizedMessage(),
                                                                null));
        }

        @ExceptionHandler({ BadCredentialsException.class })
        public ResponseEntity<CustomErrorResponse> handleBadCredentials(BadCredentialsException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatusCode.valueOf(400))
                                .headers(headers -> headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON)).body(
                                                new CustomErrorResponse(HttpStatusCode.valueOf(400).toString(),
                                                                request.getRequestURI(),
                                                                ZonedDateTime.now(),
                                                                ex.getLocalizedMessage(),
                                                                null));
        }

}