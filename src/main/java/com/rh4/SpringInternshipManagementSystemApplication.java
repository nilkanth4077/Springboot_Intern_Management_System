package com.rh4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.rh4")
public class SpringInternshipManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringInternshipManagementSystemApplication.class, args);
	}

}
