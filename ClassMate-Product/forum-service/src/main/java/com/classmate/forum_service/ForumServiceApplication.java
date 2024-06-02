package com.classmate.forum_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main class for the Forum Service application.
 * This class bootstraps the Spring Boot application and enables Feign clients for inter-service communication.
 */
@EnableFeignClients
@SpringBootApplication
public class ForumServiceApplication {

	/**
	 * The entry point of the Forum Service application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ForumServiceApplication.class, args);
	}
}
