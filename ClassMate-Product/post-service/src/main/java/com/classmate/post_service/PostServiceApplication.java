package com.classmate.post_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main application class for the Post Service.
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class PostServiceApplication {

	/**
	 * Main method to run the Spring Boot application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(PostServiceApplication.class, args);
	}
}
