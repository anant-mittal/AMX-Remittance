package com.amx.jax;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amx.jax.multitenant.MultiTenantJaxProperties.DataSourceProperties;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages= {"com.amx.jax"})
public class JaxServiceApplication  {
	

	public static void main(String[] args) {
		SpringApplication.run(JaxServiceApplication.class, args);
	}
	
//	@Autowired
//	@Qualifier("dataSourcesJax")
//	Map<String, DataSource> result;
//	
//	private void initDBCredentails() {
//		
//		
//		
//			DataSourceBuilder factory = DataSourceBuilder.create().url(dsProperties.getUrl())
//					.username(dsProperties.getUsername()).password(dsProperties.getPassword())
//					.driverClassName("oracle");
//			result.put(dsProperties.getTenantId(), factory.build());
//	}
}
