package com.spring.core.controller;

import com.spring.core.dto.WebResponse;
import com.spring.core.dto.vacancy.CreateVacancyRequest;
import com.spring.core.dto.vacancy.UpdateVacancyRequest;
import com.spring.core.dto.vacancy.VacancyResponse;
import com.spring.core.service.VacancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vacancies")
public class VacancyController {
    private final VacancyService vacancyService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = ""
    )
    public ResponseEntity<WebResponse<VacancyResponse>> createVacancy(@RequestBody @Valid CreateVacancyRequest request){
        VacancyResponse vacancyResponse = vacancyService.create(request);

        WebResponse<VacancyResponse> response = WebResponse.<VacancyResponse>builder()
                .status("success")
                .message("vacancy created successfully")
                .data(vacancyResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/dashboard"
    )
    public ResponseEntity<WebResponse<List<VacancyResponse>>> getAllActiveVacancies(){
        List<VacancyResponse> allActiveVacancies = vacancyService.getAllVacanciesUnregisteredUser();

        WebResponse<List<VacancyResponse>> response = WebResponse.<List<VacancyResponse>>builder()
                .status("success")
                .message("vacancy retrieved successfully")
                .data(allActiveVacancies)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SEEKER')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = ""
    )
    public ResponseEntity<WebResponse<List<VacancyResponse>>> getAllQualifiedVacancies(@RequestParam(required = false) Boolean qualifier, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<VacancyResponse> allQualifiedVacancies = vacancyService.getAllVacanciesRegisteredUser(userDetails.getUsername(), qualifier);

        WebResponse<List<VacancyResponse>> response = WebResponse.<List<VacancyResponse>>builder()
                .status("success")
                .message("vacancy retrieved successfully")
                .data(allQualifiedVacancies)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SEEKER')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/{vacancyId}"
    )
    public ResponseEntity<WebResponse<VacancyResponse>> getVacancyById(@PathVariable String vacancyId, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        VacancyResponse vacancyResponse = vacancyService.getVacancyById(vacancyId, userDetails.getUsername());
        WebResponse<VacancyResponse> response = WebResponse.<VacancyResponse>builder()
                .status("success")
                .message("vacancy retrieved successfully")
                .data(vacancyResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/{vacancyId}"
    )
    public ResponseEntity<WebResponse<VacancyResponse>> updateVacancy(@PathVariable String vacancyId, @RequestBody @Valid UpdateVacancyRequest request){
        request.setId(vacancyId);

        VacancyResponse vacancyResponse = vacancyService.updateVacancyById(request);
        WebResponse<VacancyResponse> response = WebResponse.<VacancyResponse>builder()
                .status("success")
                .message("vacancy updated successfully")
                .data(vacancyResponse)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/{vacancyId}"
    )
    public ResponseEntity<WebResponse<Object>> deleteVacancy(@PathVariable String vacancyId){
        vacancyService.deleteVacancyById(vacancyId);

        WebResponse<Object> response = WebResponse.builder()
                .status("success")
                .message("vacancy deleted successfully")
                .data(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
