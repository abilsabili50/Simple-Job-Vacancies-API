package com.spring.core.service;

import com.spring.core.dto.vacancy.CreateVacancyRequest;
import com.spring.core.dto.vacancy.UpdateVacancyRequest;
import com.spring.core.dto.vacancy.VacancyResponse;

import java.util.List;

public interface VacancyService {
    VacancyResponse create(CreateVacancyRequest request);
    List<VacancyResponse> getAllVacanciesRegisteredUser(String email, Boolean qualifier);
    List<VacancyResponse> getAllVacanciesUnregisteredUser();
    VacancyResponse getVacancyById(String id, String email);
    VacancyResponse updateVacancyById(UpdateVacancyRequest request);
    void deleteVacancyById(String id);
}
