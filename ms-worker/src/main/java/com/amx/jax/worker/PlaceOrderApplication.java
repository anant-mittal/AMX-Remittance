package com.amx.jax.worker;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amx.jax.amxlib.model.JaxMetaInfo;


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
    
	/**
	 * Jax meta info.
	 *
	 * @return the jax meta info
	 */
	@Bean
	@Scope(value = "thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public JaxMetaInfo jaxMetaInfo() {
		return new com.amx.jax.amxlib.model.JaxMetaInfo();
	}
	
	@Bean
	public CustomScopeConfigurer customScopeConfigurer() {
		CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer(); 
		org.springframework.beans.factory.config.Scope customScope = new ThreadScope();
		customScopeConfigurer.addScope("thread", customScope);
		return customScopeConfigurer;
	}
}
