package com.spring.core.service;

import com.spring.core.dto.user.*;

public interface UserService {
    RegisterResponse register(RegisterRequest request);
    TokenResponse login(LoginRequest request);
}
