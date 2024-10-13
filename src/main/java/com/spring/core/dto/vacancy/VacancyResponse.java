package com.spring.core.dto.vacancy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VacancyResponse {
    private String id;
    private String name;
    private String description;
    private Integer maxAge;
    private Integer minExp;
    private String salary;
    private String publishDate;
    private String expiryDate;
}
