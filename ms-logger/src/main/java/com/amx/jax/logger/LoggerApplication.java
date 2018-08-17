package com.amx.jax.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The Class LoggerApplication.
 */
@SpringBootApplication
@ComponentScan("com.amx.jax")
@EnableAsync(proxyTargetClass = true)
public class LoggerApplication {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(LoggerApplication.class, args);
	}

}