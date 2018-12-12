package com.amx.jax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.swagger.MockParamBuilder;
import com.amx.jax.swagger.MockParamBuilder.MockParam;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.amx.jax" })
@EnableAsync
public class JaxServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaxServiceApplication.class, args);
	}

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public JaxMetaInfo JaxMetaInfo() {
		JaxMetaInfo metaInfo = new JaxMetaInfo();
		return metaInfo;
	}

	@Bean
	public MockParam metaInfo() {
		return new MockParamBuilder().name("meta-info").description("meta-info")
				.defaultValue("{\"countryId\":91,\"customerId\":5218,\"companyId\":1,\"channel\":\"ONLINE\" , "
						+ "\"countryBranchId\":\"78\", \"tenant\":\"KWT\",\"languageId\":1,\"employeeId\":265 }")
				.parameterType(MockParamBuilder.MockParamType.HEADER).required(true).build();
	}

}
