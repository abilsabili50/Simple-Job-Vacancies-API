package com.spring.core.controller;

import com.spring.core.dto.WebResponse;
import com.spring.core.dto.user.LoginRequest;
import com.spring.core.dto.user.RegisterRequest;
import com.spring.core.dto.user.RegisterResponse;
import com.spring.core.dto.user.TokenResponse;
import com.spring.core.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/register"
    )
    public ResponseEntity<WebResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request){
        RegisterResponse registerResponse = userService.register(request);

        WebResponse<RegisterResponse> response = WebResponse.<RegisterResponse>builder()
                .status("success")
                .message("user registered successfully")
                .data(registerResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/login"
    )
    public ResponseEntity<WebResponse<TokenResponse>> login(@RequestBody @Valid LoginRequest request){
        TokenResponse tokenResponse = userService.login(request);

        WebResponse<TokenResponse> response = WebResponse.<TokenResponse>builder()
                .status("success")
                .message("login success")
                .data(tokenResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
