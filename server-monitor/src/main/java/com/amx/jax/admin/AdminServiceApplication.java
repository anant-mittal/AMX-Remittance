package com.amx.jax.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import com.amx.jax.def.MockParamBuilder;
import com.amx.jax.def.MockParamBuilder.MockParam;

import de.codecentric.boot.admin.config.EnableAdminServer;

@ServletComponentScan
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("com.amx.jax")
@EnableAsync
@EnableCaching
@EnableAdminServer
public class AdminServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AdminServiceApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AdminServiceApplication.class);
	}

	@Bean
	public MockParam metaInfo() {
		return new MockParamBuilder().name("meta-info").description("meta-info").defaultValue(
				"{\"countryId\":91,\"customerId\":5218,\"companyId\":1,\"channel\":\"ONLINE\" , \"countryBranchId\":\"78\", \"tenant\":\"KWT2\"}")
				.parameterType(MockParamBuilder.MockParamType.HEADER).required(true).build();
	}

}
