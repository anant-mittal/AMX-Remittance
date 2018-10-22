package com.amx.jax.adapter;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.amx.jax")
public class SWAdapterService implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("Adapter Started");
	}
	
	
}