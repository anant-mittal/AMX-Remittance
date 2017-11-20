package com.amx.jax;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.amx.jax.services.OracleCustomerService;
import com.amx.jax.services.KwServiceFactory;
import com.amx.jax.userservice.service.UserService;

@SpringBootApplication
@ComponentScan(basePackages= {"com.amx.jax"})
public class JaxServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaxServiceApplication.class, args);
	}
	
}
