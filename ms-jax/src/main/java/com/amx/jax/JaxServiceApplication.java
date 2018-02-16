package com.amx.jax;

import javax.servlet.ServletContextListener;
import javax.validation.constraints.NotNull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amx.jax.postman.client.PostManContextListener;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.amx.jax" })
public class JaxServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaxServiceApplication.class, args);
	}

	@NotNull
	@Bean
	ServletListenerRegistrationBean<ServletContextListener> myServletListener() {
		ServletListenerRegistrationBean<ServletContextListener> srb = new ServletListenerRegistrationBean<>();
		srb.setListener(new PostManContextListener());
		return srb;
	}

	// @Autowired
	// @Qualifier("dataSourcesJax")
	// Map<String, DataSource> result;
	//
	// private void initDBCredentails() {
	//
	//
	//
	// DataSourceBuilder factory =
	// DataSourceBuilder.create().url(dsProperties.getUrl())
	// .username(dsProperties.getUsername()).password(dsProperties.getPassword())
	// .driverClassName("oracle");
	// result.put(dsProperties.getTenantId(), factory.build());
	// }
}
