package com.example.chat.errorHandling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
    @ExceptionHandler(BusinessNotFound.class)
    public ResponseEntity<String> BusinessNotFoundHandler(BusinessNotFound businessNotFound) {
        return new ResponseEntity<String>(businessNotFound.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> BusinessNotFoundHandler(UserNotFound userNotFound) {
        return new ResponseEntity<String>(userNotFound.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> RuntimeExceptionHandler(RuntimeException runtimeException) {
        return new ResponseEntity<String>(runtimeException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> BadCredentialsExceptionHandler(BadCredentialsException badCredentialsException) {
        return new ResponseEntity<String>(badCredentialsException.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> UsernameNotFoundExceptionHandler(UsernameNotFoundException usernameNotFoundException) {
        return new ResponseEntity<String>(usernameNotFoundException.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        LinkedHashMap<String, String> validationErrors = new LinkedHashMap<>();
        for (ObjectError error: errors) {
            validationErrors.put(((FieldError)error).getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }
}