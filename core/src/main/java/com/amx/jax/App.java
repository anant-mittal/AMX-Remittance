package com.amx.jax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */

@SpringBootApplication
/*@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.amx.spservice.client.*"))
*/
@ComponentScan(basePackages= {"com.amx.jax"})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class App 
{
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
}
