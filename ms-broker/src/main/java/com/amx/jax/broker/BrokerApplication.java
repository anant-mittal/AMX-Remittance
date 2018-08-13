package com.amx.jax.broker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The Class RbaacServiceApplication.
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.amx.jax" })
@EnableAsync(proxyTargetClass = true)
public class BrokerApplication {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(BrokerApplication.class, args);
	}

}
