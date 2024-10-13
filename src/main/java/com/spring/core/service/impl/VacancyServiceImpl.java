package com.spring.core.service.impl;

import com.spring.core.dto.vacancy.CreateVacancyRequest;
import com.spring.core.dto.vacancy.UpdateVacancyRequest;
import com.spring.core.dto.vacancy.VacancyResponse;
import com.spring.core.entity.ERole;
import com.spring.core.entity.User;
import com.spring.core.entity.Vacancy;
import com.spring.core.repository.UserRepository;
import com.spring.core.repository.VacancyRepository;
import com.spring.core.service.VacancyService;
import com.spring.core.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;

    @Override
    @Transactional
    public VacancyResponse create(CreateVacancyRequest request) {
        if(vacancyRepository.existsByName(request.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vacancy's name already registered");
        }

        Vacancy vacancy = Vacancy.builder()
                .name(request.getName())
                .description(request.getDescription())
                .maxAge(request.getMaxAge())
                .minExp(request.getMinExp())
                .publishDate(Formatter.stringToDate(request.getPublishDate()))
                .expiryDate(Formatter.stringToDate(request.getExpiryDate()))
                .salary(request.getSalary())
                .build();

        vacancyRepository.save(vacancy);

        return VacancyResponse.builder()
                .id(vacancy.getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .salary(String.valueOf(vacancy.getSalary()))
                .maxAge(vacancy.getMaxAge())
                .minExp(vacancy.getMinExp())
                .publishDate(Formatter.dateToString(vacancy.getPublishDate()))
                .expiryDate(Formatter.dateToString(vacancy.getExpiryDate()))
                .build();
    }

    @Override
    public List<VacancyResponse> getAllVacanciesRegisteredUser(String email, Boolean qualifier) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("invalid credentials"));

        List<Vacancy> vacancies;

        if(user.getRole().equals(ERole.SEEKER)){
            vacancies = (qualifier != null && qualifier) ? vacancyRepository.findAllSpecificVacancies(Formatter.dateToDate(new Date()), user.getAge(), user.getExperience())
            : vacancyRepository.findAllActiveVacancies(new Date());
        } else {
            vacancies = vacancyRepository.findAll();
        }

        return vacancies.stream().map(vacancy -> VacancyResponse.builder()
                .id(vacancy.getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .maxAge(vacancy.getMaxAge())
                .minExp(vacancy.getMinExp())
                .salary(vacancy.getSalary().toString())
                .publishDate(Formatter.dateToString(vacancy.getPublishDate()))
                .expiryDate(Formatter.dateToString(vacancy.getExpiryDate()))
                .build()).toList();
    }

    @Override
    public List<VacancyResponse> getAllVacanciesUnregisteredUser() {
        List<Vacancy> allActiveVacancies = vacancyRepository.findAllActiveVacancies(Formatter.dateToDate(new Date()));

        return allActiveVacancies.stream().map(vacancy -> VacancyResponse.builder()
                .id(vacancy.getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .maxAge(vacancy.getMaxAge())
                .minExp(vacancy.getMinExp())
                .salary(vacancy.getSalary().toString())
                .publishDate(Formatter.dateToString(vacancy.getPublishDate()))
                .expiryDate(Formatter.dateToString(vacancy.getExpiryDate()))
                .build()).toList();
    }

    @Override
    public VacancyResponse getVacancyById(String id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("invalid credentials"));

        Vacancy vacancy = vacancyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "vacancy not found"));

        if(user.getRole().equals(ERole.SEEKER) && vacancy.getExpiryDate().before(new Date())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "vacancy not found");
        }

        return VacancyResponse.builder()
                .id(vacancy.getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .maxAge(vacancy.getMaxAge())
                .minExp(vacancy.getMinExp())
                .salary(vacancy.getSalary().toString())
                .publishDate(Formatter.dateToString(vacancy.getPublishDate()))
                .expiryDate(Formatter.dateToString(vacancy.getExpiryDate()))
                .build();
    }

    @Override
    public VacancyResponse updateVacancyById(UpdateVacancyRequest request) {
        Vacancy vacancy = vacancyRepository.findById(request.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "vacancy not found"));

        vacancy.setName(Optional.ofNullable(request.getName()).orElse(vacancy.getName()));
        vacancy.setDescription(Optional.ofNullable(request.getDescription()).orElse(vacancy.getDescription()));
        vacancy.setSalary(Optional.ofNullable(request.getSalary()).orElse(vacancy.getSalary()));
        vacancy.setMaxAge(Optional.ofNullable(request.getMaxAge()).orElse(vacancy.getMaxAge()));
        vacancy.setMinExp(Optional.ofNullable(request.getMinExp()).orElse(vacancy.getMinExp()));
        vacancy.setPublishDate(Optional.ofNullable(request.getPublishDate())
                .map(Formatter::stringToDate).orElse(vacancy.getPublishDate()));
        vacancy.setExpiryDate(Optional.ofNullable(request.getExpiryDate())
                .map(Formatter::stringToDate).orElse(vacancy.getExpiryDate()));

        vacancyRepository.save(vacancy);

        return VacancyResponse.builder()
                .id(vacancy.getId())
                .name(vacancy.getName())
                .description(vacancy.getDescription())
                .maxAge(vacancy.getMaxAge())
                .minExp(vacancy.getMinExp())
                .salary(vacancy.getSalary().toString())
                .publishDate(Formatter.dateToString(vacancy.getPublishDate()))
                .expiryDate(Formatter.dateToString(vacancy.getExpiryDate()))
                .build();
    }

    @Override
    public void deleteVacancyById(String id) {
        Vacancy vacancy = vacancyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "vacancy not found"));

        vacancyRepository.delete(vacancy);
    }
}
