package com.mycompany.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LocationApplication {

	private static final Logger LOGGER =LoggerFactory.getLogger(LocationApplication.class);

	public static void main(String[] args) {
		LOGGER.info("info");
		SpringApplication.run(LocationApplication.class, args);
	}

}
