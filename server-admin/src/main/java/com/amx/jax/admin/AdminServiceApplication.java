package com.amx.jax.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.def.MockParamBuilder;
import com.amx.jax.def.MockParamBuilder.MockParam;
import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;

@SpringBootApplication
@ComponentScan("com.amx.jax")
public class AdminServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AdminServiceApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AdminServiceApplication.class);
	}

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public com.amx.jax.amxlib.model.JaxMetaInfo JaxMetaInfo() {
		com.amx.jax.amxlib.model.JaxMetaInfo metaInfo = new com.amx.jax.amxlib.model.JaxMetaInfo();
		return metaInfo;
	}

	@Bean
	public MockParam metaInfo() {
		return new MockParamBuilder().name("meta-info").description("meta-info").defaultValue(
				"{\"countryId\":91,\"customerId\":5218,\"companyId\":1,\"channel\":\"ONLINE\" , \"countryBranchId\":\"78\", \"tenant\":\"KWT2\"}")
				.parameterType(MockParamBuilder.MockParamType.HEADER).required(true).build();
	}

}
