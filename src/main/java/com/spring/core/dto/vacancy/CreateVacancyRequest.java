package com.spring.core.dto.vacancy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateVacancyRequest {
    @NotBlank(message = "vacancy's name can't be blank")
    private String name;

    @NotBlank(message = "description can't be blank")
    private String description;

    @NotNull(message = "maximum age can't be blank")
    private Integer maxAge;

    @NotNull(message = "minimum experience can't be blank")
    private Integer minExp;

    @NotNull(message = "salary can't be blank")
    private Long salary;

    @NotBlank(message = "publish date can't be blank")
    private String publishDate;

    @NotBlank(message = "expiration date can't be blank")
    private String expiryDate;
}
