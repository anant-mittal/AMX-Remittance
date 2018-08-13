package com.amx.jax.broker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.amx.jax.service.BrokerService;

/**
 * The Class RbaacServiceApplication.
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.amx.jax" })
@EnableAsync(proxyTargetClass = true)
@EnableScheduling
@EntityScan("com.amx.jax")
@EnableJpaRepositories("com.amx.jax.service.repository")
public class BrokerApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(BrokerApplication.class, args);

//		BrokerService broker_service = new BrokerService();
//		
//		broker_service.pushNewEventNotifications();

	}

}
