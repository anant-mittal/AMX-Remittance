package com.amx.jax.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amx.jax.adapter.service.CardReaderService;

@SpringBootApplication
public class SpringBootConsoleApplication implements ApplicationRunner {

	@Autowired
	private CardReaderService helloService;

	public static void main(String[] args) throws Exception {

		// disabled banner, don't want to see the spring logo
		SpringApplication app = new SpringApplication(SpringBootConsoleApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);

		// SpringApplication.run(SpringBootConsoleApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (args.getSourceArgs().length > 0) {
			System.out.println(helloService.getMessage(args.getSourceArgs()[0].toString()));
		} else {
			System.out.println(helloService.getMessage());
		}
		// exit(0);
	}
}