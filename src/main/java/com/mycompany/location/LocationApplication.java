package com.mycompany.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LocationApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(LocationApplication.class);

	public static void main(String[] args) {
		LOGGER.trace("Trace Log");
		LOGGER.debug("Debug Log");
		LOGGER.info("Info Log");
		LOGGER.warn("Warn Log");
		LOGGER.error("Error Log");
		SpringApplication.run(LocationApplication.class, args);
	}

}
