package com.amx.jax;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.amx.jax.services.CustomerService;
import com.amx.jax.services.KwServiceFactory;
import com.amx.jax.userservice.service.UserService;

@SpringBootApplication
/*@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.amx.spservice.client.*"))
*/
@ComponentScan(basePackages= {"com.amx.jax"})
public class JaxServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaxServiceApplication.class, args);
	}
	
	@Autowired
	UserService userService;
	
	@Autowired
	CustomerService custService;
	
	@Autowired
	KwServiceFactory kwserviceFactory;
	
	@PostConstruct
	public void init(){
		System.out.println("JaxServiceApplication init method calling");
		
		kwserviceFactory.setCustService(custService);
		kwserviceFactory.setUserService(userService);
		
		System.out.println("JaxServiceApplication init method called");
	}
}
