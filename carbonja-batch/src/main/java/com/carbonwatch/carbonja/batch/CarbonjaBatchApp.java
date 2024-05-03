package com.carbonwatch.carbonja.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@ComponentScan(basePackages = { "com.carbonwatch.carbonja" })
@EnableRetry
public class CarbonjaBatchApp {

	private static final Logger logger = LoggerFactory.getLogger(CarbonjaBatchApp.class);
	
	public static void main(final String[] args) {

		logger.info("Start Carbonbja springbatch application");

		final int exitCode = SpringApplication.exit(SpringApplication.run(CarbonjaBatchApp.class, args));
		System.exit(exitCode);
	} 

}