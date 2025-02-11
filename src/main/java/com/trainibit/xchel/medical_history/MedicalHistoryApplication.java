package com.trainibit.xchel.medical_history;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MedicalHistoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalHistoryApplication.class, args);
	}

}
