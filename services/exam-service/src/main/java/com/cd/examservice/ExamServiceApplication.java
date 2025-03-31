package com.cd.examservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ExamServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamServiceApplication.class, args);
	}


	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}
}
