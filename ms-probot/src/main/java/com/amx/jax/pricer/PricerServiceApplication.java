package com.amx.jax.pricer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = { "com.amx.jax" })
@EnableAsync(proxyTargetClass = true)
@EnableCaching
public class PricerServiceApplication {

	public static void main(String[] args) {
		//ConfigurableApplicationContext context = SpringApplication.run(PricerServiceApplication.class, args);
		SpringApplication.run(PricerServiceApplication.class, args);
		//context.getBean(RemitPriceManager.class).
	}
}
