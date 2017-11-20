package com.amx.jax.multitenant;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amx.jax.multitenant.MultiTenantJaxProperties.DataSourceProperties;

@Configuration
@EnableConfigurationProperties({ MultiTenantJaxProperties.class, JpaProperties.class })

public class MultiTenantJpaConfiguration {
	
	@Autowired
	MultiTenantJaxProperties multiTenantDvdRentalProperties;
	
	@Bean(name = "dataSourcesJax" )
	public Map<String, DataSource> dataSourcesDvdRental() {
	     Map<String, DataSource> result = new HashMap<>();
	     for (DataSourceProperties dsProperties : this.multiTenantDvdRentalProperties.getDataSourcesProps()) {
	       DataSourceBuilder factory = DataSourceBuilder
	         .create()
	         .url(dsProperties.getUrl())
	         .username(dsProperties.getUsername())
	         .password(dsProperties.getPassword())
	         .driverClassName(dsProperties.getDriverClassName());
	       result.put(dsProperties.getTenantId(), factory.build());
	     }
	     return result;
	   }
}
