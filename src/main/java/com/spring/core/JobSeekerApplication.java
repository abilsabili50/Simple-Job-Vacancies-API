package com.spring.core;

import com.spring.core.setup.DatabaseSeeder;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.TimeZone;

@SpringBootApplication
@EnableMongoAuditing
public class JobSeekerApplication {

	public static void main(String[] args) {
		DatabaseSeeder seeder = SpringApplication
				.run(JobSeekerApplication.class, args)
				.getBean(DatabaseSeeder.class);

		seeder.adminSetup();
	}

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));
	}

}
