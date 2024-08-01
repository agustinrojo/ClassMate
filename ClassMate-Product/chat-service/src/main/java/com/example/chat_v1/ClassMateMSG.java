package com.example.chat_v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ClassMateMSG {

	public static void main(String[] args) {
		SpringApplication.run(ClassMateMSG.class, args);
	}

}
