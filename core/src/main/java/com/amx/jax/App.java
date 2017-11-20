package com.amx.jax;

import org.springframework.boot.SpringApplication;

/**
 * Hello world!
 *
 */

//@SpringBootApplication
///*@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.amx.spservice.client.*"))
//*/
//@ComponentScan(basePackages= {"com.amx.jax"})
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class App 
{
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
}
