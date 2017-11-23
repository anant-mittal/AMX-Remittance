package com.amx.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.amx.ui.config.Properties;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.amx")
public class WebApplication {

	@Autowired
	private Properties props;

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}

	@Bean("base_url")
	public URL baseUrl() throws MalformedURLException {
		return new URL(props.getJaxServiceUrl());
	}
}
