package com.pwl.his;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HolidayInformationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HolidayInformationServiceApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
