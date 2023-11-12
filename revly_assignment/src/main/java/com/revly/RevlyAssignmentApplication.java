package com.revly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RevlyAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(RevlyAssignmentApplication.class, args);
	}

}
