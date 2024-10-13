package com.spring.core.dto.vacancy;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UpdateVacancyRequest {
    private String name;
    private String description;
    private Integer maxAge;
    private Integer minExp;
    private Long salary;
    private String publishDate;
    private String expiryDate;

    @JsonIgnore
    private String id;
}
