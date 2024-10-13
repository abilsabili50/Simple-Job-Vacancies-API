package com.spring.core.repository;

import com.spring.core.entity.Vacancy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VacancyRepository extends MongoRepository<Vacancy, String> {
    boolean existsByName(String name);

    @Query("{ 'expiry_date' : {$gt : ?0}, 'max_age' : {$gte : ?1}, 'min_exp' : {$lte : ?2}}")
    List<Vacancy> findAllSpecificVacancies(Date date, Integer maxAge, Integer minExp);

    @Query("{ 'expiry_date' : {$gt : ?0}}")
    List<Vacancy> findAllActiveVacancies(Date date);
}
