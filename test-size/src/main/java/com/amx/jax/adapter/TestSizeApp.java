package com.amx.jax.adapter;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.SitePreferenceAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.amx.jax")
@EnableAutoConfiguration(exclude = {
		
		JmxAutoConfiguration.class

		, WebSocketAutoConfiguration.class

		, SitePreferenceAutoConfiguration.class

		, SpringApplicationAdminJmxAutoConfiguration.class

		, ValidationAutoConfiguration.class

})
public class TestSizeApp {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(TestSizeApp.class)
				.run(args);

	}

}