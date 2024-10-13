package com.spring.core.setup;

import com.spring.core.entity.*;
import com.spring.core.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder {
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String ADMIN_EMAIL = "admin@admin.com";

    @Transactional
    public void adminSetup(){
        userRepository.deleteAll();
        vacanciesSetup();

        User admin = User.builder()
                .email(ADMIN_EMAIL)
                .password(passwordEncoder.encode("admin123"))
                .age(35)
                .experience(5)
                .role(ERole.ADMIN)
                .name("admin")
                .build();

        userRepository.save(admin);
    }

    @Transactional
    protected void vacanciesSetup(){
        vacancyRepository.deleteAll();

        vacancyRepository.saveAll(vacancies);
    }

    // list vacancies
    private List<Vacancy> vacancies = Arrays.asList(
            Vacancy.builder()
                    .name("Junior Developer")
                    .description("Entry level software developer position.")
                    .minExp(1)
                    .maxAge(25)
                    .salary(5000000L)
                    .publishDate(new Date())
                    .expiryDate(Date.from(Instant.now().plus(30, ChronoUnit.DAYS))) // expire setelah 30 hari
                    .build(),

            Vacancy.builder()
                    .name("Junior Tester")
                    .description("Entry level software tester position.")
                    .minExp(1)
                    .maxAge(25)
                    .salary(4500000L)
                    .publishDate(Date.from(Instant.now().minus(60, ChronoUnit.DAYS))) // tidak aktif (lebih dari 60 hari yang lalu)
                    .expiryDate(Date.from(Instant.now().minus(30, ChronoUnit.DAYS))) // expire 30 hari yang lalu
                    .build(),

            Vacancy.builder()
                    .name("Mid-Level Developer")
                    .description("Software developer position.")
                    .minExp(2)
                    .maxAge(30)
                    .salary(7000000L)
                    .publishDate(new Date())
                    .expiryDate(Date.from(Instant.now().plus(30, ChronoUnit.DAYS))) // expire setelah 30 hari
                    .build(),

            Vacancy.builder()
                    .name("Mid-Level Tester")
                    .description("Mid-level software tester position.")
                    .minExp(2)
                    .maxAge(30)
                    .salary(6000000L)
                    .publishDate(Date.from(Instant.now().minus(60, ChronoUnit.DAYS))) // tidak aktif (lebih dari 60 hari yang lalu)
                    .expiryDate(Date.from(Instant.now().minus(30, ChronoUnit.DAYS))) // expire 30 hari yang lalu
                    .build(),

            Vacancy.builder()
                    .name("Senior Developer")
                    .description("Senior software developer position.")
                    .minExp(5)
                    .maxAge(25)
                    .salary(12000000L)
                    .publishDate(new Date())
                    .expiryDate(Date.from(Instant.now().plus(30, ChronoUnit.DAYS))) // expire setelah 30 hari
                    .build(),

            Vacancy.builder()
                    .name("Senior Tester")
                    .description("Senior software tester position.")
                    .minExp(5)
                    .maxAge(25)
                    .salary(10000000L)
                    .publishDate(Date.from(Instant.now().minus(60, ChronoUnit.DAYS))) // tidak aktif (lebih dari 60 hari yang lalu)
                    .expiryDate(Date.from(Instant.now().minus(30, ChronoUnit.DAYS))) // expire 30 hari yang lalu
                    .build()
    );
}
