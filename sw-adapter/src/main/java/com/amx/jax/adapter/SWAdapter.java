package com.amx.jax.adapter;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.amx.jax")
public class SWAdapter implements ApplicationRunner {

	public static void main(String[] args) throws Exception {

		// disabled banner, don't want to see the spring logo
		SpringApplication app = new SpringApplication(SWAdapter.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);

		// SpringApplication.run(SpringBootConsoleApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("Adapter....");
		// exit(0);
	}
}