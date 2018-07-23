package com.amx.jax.placeorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.amx.jax"})
@EnableJpaRepositories(basePackages = {"com.amx.jax.placeorder"})
@EntityScan("com.amx.jax.dbmodel")
@EnableAsync

public class PlaceOrderApplication 
{
    public static void main( String[] args )
    {
		SpringApplication.run(PlaceOrderApplication.class, args);
    }
}
