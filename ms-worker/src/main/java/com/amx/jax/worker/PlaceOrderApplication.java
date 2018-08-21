package com.amx.jax.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.amx.jax"})
@EnableAsync
public class PlaceOrderApplication 
{
    public static void main( String[] args )
    {
		SpringApplication.run(PlaceOrderApplication.class, args);
    }
}
