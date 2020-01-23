package com.amx.jax.postman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.amx.jax.postman.service.TelegramBot;

/**
 * The Class PostManApplication.
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.amx.jax" })
@EnableAsync(proxyTargetClass = true)
public class PostManApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(PostManApplication.class, args);
	}

}
