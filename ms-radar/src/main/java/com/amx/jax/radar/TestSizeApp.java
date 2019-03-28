package com.amx.jax.radar;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.mobile.SitePreferenceAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.scope.ThreadScoped;

@SpringBootApplication
@ComponentScan(value = "com.amx.jax")
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {
		JmxAutoConfiguration.class, WebSocketAutoConfiguration.class, SitePreferenceAutoConfiguration.class,
		SpringApplicationAdminJmxAutoConfiguration.class, ValidationAutoConfiguration.class
})
public class TestSizeApp {

	public static final String ENABLE_JOBS = "true";
	public static final String ENABLE_JOBS_DEBUG = "false";

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = new SpringApplicationBuilder(TestSizeApp.class)
				.run(args);
	}

	@Bean
	@ThreadScoped
	public JaxMetaInfo jaxMetaInfo() {
		return new JaxMetaInfo();
	}

}
