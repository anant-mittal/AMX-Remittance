package com.amx.jax.postman;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import com.amx.jax.postman.model.Langs;
import com.amx.utils.ArgUtil;

@SpringBootApplication
@ComponentScan(basePackages = { "com.amx.jax", "com.bootloaderjs" })
@EnableAsync(proxyTargetClass = true)
public class PostManApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostManApplication.class, args);
	}

	@Value("${jax.lang.code}")
	private String defaultLangCode;

	@Bean
	Langs defaultLang() {
		return (Langs) ArgUtil.parseAsEnum(defaultLangCode, Langs.DEFAULT);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		return restTemplate;
	}
}
