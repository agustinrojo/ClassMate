package com.classmate.comment_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
/**
 * Main application class for the Comment Service.
 */
@EnableFeignClients
@SpringBootApplication
public class CommentServiceApplication {

	/**
	 * Main method to run the Spring Boot application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CommentServiceApplication.class, args);
	}

}
