package com.amx.jax.branch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@ComponentScan(basePackages = { "com.amx.jax" })
@EnableAsync(proxyTargetClass = true)
public class BranchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BranchApplication.class, args);
	}

}
