package com.spring.core.exception;

import com.spring.core.dto.WebResponse;
import com.spring.core.util.StringsUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.DecodingException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // handle data binding validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<Object>> methodArgumentNotValidException(MethodArgumentNotValidException exception){

        // Extract field errors
        List<String> errorMessages = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(StringsUtil::formatFieldError)
                .collect(Collectors.toList());

        // Join errors into a single message
        String formattedErrorMessage = String.join("; ", errorMessages);

        WebResponse<Object> errResponse = WebResponse
                .<Object>builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(formattedErrorMessage)
                .data(null)
                .build();

        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
    }

    // handle invalid path variable
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<WebResponse<Object>> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception){

        WebResponse<Object> errResponse = WebResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message("invalid value: " + exception.getValue())
                .data(null)
                .build();

        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
    }

    // handle invalid request param
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<WebResponse<Object>> missingServletRequestParameterException(MissingServletRequestParameterException exception){

        WebResponse<Object> response = WebResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message("Required parameter is missing: " + exception.getParameterName())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // handle invalid request header
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<WebResponse<Object>> handleMissingRequestHeader(MissingRequestHeaderException ex) {

        WebResponse<Object> response = WebResponse.<Object>builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message("Required header is missing: " + ex.getHeaderName())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // handle unsupported content-type
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<WebResponse<Object>> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex) {
        WebResponse<Object> response = WebResponse.builder()
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString())
                .message("Content-Type is not supported: " + ex.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    // handle malformed json format
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<WebResponse<Object>> handleMessageNotReadable(
            HttpMessageNotReadableException ex) {
        WebResponse<Object> response = WebResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message("Malformed JSON request: " + ex.getMessage())
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // handle invalid credentials
    @ExceptionHandler(value = { BadCredentialsException.class })
    public ResponseEntity<WebResponse<Object>> badCredentialsException(BadCredentialsException exception) {

        WebResponse<Object> errResponse = WebResponse
                .<Object>builder()
                .status(HttpStatus.UNAUTHORIZED.toString())
                .message("bad credentials")
                .data(null)
                .build();

        return new ResponseEntity<>(errResponse, HttpStatus.UNAUTHORIZED);
    }

    // handle unregistered email
    @ExceptionHandler(value = { UsernameNotFoundException.class })
    public ResponseEntity<WebResponse<Object>> usernameNotFoundException(UsernameNotFoundException exception){

        WebResponse<Object> errResponse = WebResponse
                .<Object>builder()
                .status(HttpStatus.UNAUTHORIZED.toString())
                .message("bad credentials")
                .data(null)
                .build();

        return new ResponseEntity<>(errResponse, HttpStatus.NOT_FOUND);
    }

    // handle forbidden access
    @ExceptionHandler(value = { AccessDeniedException.class })
    public ResponseEntity<WebResponse<Object>> accessDeniedException(AccessDeniedException exception) {

        WebResponse<Object> errResponse = WebResponse
                .<Object>builder()
                .status(HttpStatus.FORBIDDEN.toString())
                .message("unauthorized user")
                .data(null)
                .build();

        return new ResponseEntity<>(errResponse, HttpStatus.FORBIDDEN);
    }

    // handle expired jwt token
    @ExceptionHandler(value = { ExpiredJwtException.class })
    public ResponseEntity<WebResponse<Object>> expiredJwtException(ExpiredJwtException exception) {

        WebResponse<Object> errResponse = WebResponse
                .<Object>builder()
                .status(HttpStatus.UNAUTHORIZED.toString())
                .message("expired credentials")
                .data(null)
                .build();

        return new ResponseEntity<>(errResponse, HttpStatus.UNAUTHORIZED);
    }

    // handle invalid token's signature
    @ExceptionHandler(value = { java.security.SignatureException.class })
    public ResponseEntity<WebResponse<Object>> handleJavaSecuritySignatureException(java.security.SignatureException exception) {

        WebResponse<Object> errResponse = WebResponse
                .<Object>builder()
                .status(HttpStatus.UNAUTHORIZED.toString())
                .message("Invalid credentials")
                .data(null)
                .build();

        return new ResponseEntity<>(errResponse, HttpStatus.UNAUTHORIZED);
    }

    // handle invalid token's signature
    @ExceptionHandler(value = { io.jsonwebtoken.security.SignatureException.class })
    public ResponseEntity<WebResponse<Object>> handleJwtSignatureException(io.jsonwebtoken.security.SignatureException exception) {

        WebResponse<Object> errResponse = WebResponse
                .<Object>builder()
                .status(HttpStatus.UNAUTHORIZED.toString())
                .message("Invalid credentials")
                .data(null)
                .build();

        return new ResponseEntity<>(errResponse, HttpStatus.UNAUTHORIZED);
    }

    // handle invalid token's format
    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<WebResponse<Object>> decodingException(DecodingException exception){

        WebResponse<Object> response = WebResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message("invalid token")
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // handle common error
    @ExceptionHandler(value = { ResponseStatusException.class })
    public ResponseEntity<WebResponse<Object>> applicationException(ResponseStatusException exception) {

        WebResponse<Object> errResponse = WebResponse
                .<Object>builder()
                .status(exception.getStatusCode().toString())
                .message(exception.getReason())
                .data(null)
                .build();

        return new ResponseEntity<>(errResponse, exception.getStatusCode());
    }

    // handle server error
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<WebResponse<Object>> allException(Exception exception) {
        // Get the cause if it exists (for dev stage purpose only)
        Throwable cause = exception.getCause();
        String causeMessage = (cause != null) ? cause.getMessage() : exception.getMessage();

        WebResponse<Object> errResponse = WebResponse
                .<Object>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message(causeMessage)
                .data(null)
                .build();

        return new ResponseEntity<>(errResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
