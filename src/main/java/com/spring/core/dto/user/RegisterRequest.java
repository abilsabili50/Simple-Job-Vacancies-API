package com.spring.core.dto.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "email can't be blank")
    @Email(message = "email should be filled with valid format")
    private String email;

    @NotBlank(message = "password can't be blank")
    @Size(min = 8, message = "password length must be greater than 8 characters")
    private String password;

    @NotBlank(message = "name can't be blank")
    private String name;

    @NotNull(message = "age can't be blank")
    @Min(value = 0, message = "age can't be filled with negative values")
    private Integer age;

    @NotNull(message = "experience can't be blank")
    @Min(value = 0, message = "experience can't be filled with negative values")
    private Integer experience;
}
