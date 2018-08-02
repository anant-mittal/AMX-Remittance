package com.amx.jax.postman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * The Class PostManApplication.
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.amx.jax", "com.bootloaderjs" })
@EnableAsync(proxyTargetClass = true)
public class PostManApplication {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(PostManApplication.class, args);
	}

	/**
	 * Rest template.
	 *
	 * @param builder
	 *            the builder
	 * @return the rest template
	 */
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		return restTemplate;
	}
}
