package com.spring.core.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "email can't be blank")
    @Email(message = "email should be filled with valid format")
    private String email;

    @NotBlank(message = "password can't be blank")
    @Size(min = 8, message = "password length must be greater than 8 characters")
    private String password;
}
