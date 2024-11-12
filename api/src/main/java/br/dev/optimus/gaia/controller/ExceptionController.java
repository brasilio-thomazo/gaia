package br.dev.optimus.gaia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.dev.optimus.gaia.exception.DuplicateRecordException;
import br.dev.optimus.gaia.exception.InvalidCredentialsException;
import br.dev.optimus.gaia.exception.PasswordMismatchException;
import br.dev.optimus.gaia.exception.RecordNotFoundException;
import br.dev.optimus.gaia.exception.RequiredExcepetion;

@ControllerAdvice
public class ExceptionController {
    public class Response {
        private String message;
        private List<String> errors;
        private HttpStatus status;
        private long timestamp;

        public Response(String message, HttpStatus status) {
            this.message = message;
            this.status = status;
            this.timestamp = System.currentTimeMillis();
        }

        public Response(String message, HttpStatus status, List<String> errors) {
            this(message, status);
            this.errors = errors;
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getMessage() {
            return message;
        }

        public int getStatus() {
            return status.value();
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Response> notFound(RecordNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response(e.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<Response> duplicate(DuplicateRecordException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new Response(e.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Response> passwordMismatch(PasswordMismatchException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new Response(e.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Response> invalidCredentials(InvalidCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new Response(e.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Response> notFound(UsernameNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new Response(e.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> validation(MethodArgumentNotValidException e) {
        var errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + "," + error.getDefaultMessage()).toList();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response(null, HttpStatus.BAD_REQUEST, errors));
    }

    @ExceptionHandler(RequiredExcepetion.class)
    public ResponseEntity<Response> required(RequiredExcepetion e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new Response(e.getMessage(), HttpStatus.BAD_REQUEST));
    }
}
