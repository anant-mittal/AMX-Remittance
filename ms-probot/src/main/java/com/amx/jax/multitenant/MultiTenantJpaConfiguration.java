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

import com.amx.jax.multitenant.MultiTenantJaxProperties.DataSourceProperties;

@Configuration
@EnableConfigurationProperties({ MultiTenantJaxProperties.class, JpaProperties.class })

public class MultiTenantJpaConfiguration {

	@Autowired
	MultiTenantJaxProperties multiTenantJaxProperties;

	@Bean(name = "dataSourcesJax")
	public Map<String, DataSource> dataSourcesJax() {
		Map<String, DataSource> result = new HashMap<>();
		for (DataSourceProperties dsProperties : this.multiTenantJaxProperties.getDataSourcesProps()) {
			DataSourceBuilder factory = DataSourceBuilder.create().url(dsProperties.getUrl())
					.username(dsProperties.getUsername()).password(dsProperties.getPassword())
					.driverClassName(dsProperties.getDriverClassName());

			org.apache.tomcat.jdbc.pool.DataSource tomcatDataSource = (org.apache.tomcat.jdbc.pool.DataSource) factory
					.build();

			 tomcatDataSource.setTestOnBorrow(true);
			 tomcatDataSource.setValidationQuery("select 1 from dual");
			 tomcatDataSource.setTestWhileIdle(true);
			 result.put(dsProperties.getTenantId(), tomcatDataSource);

//			com.zaxxer.hikari.HikariDataSource hikariDataSource = (com.zaxxer.hikari.HikariDataSource) factory.build();
//			hikariDataSource.setConnectionTestQuery("select 1 from dual");
//			result.put(dsProperties.getTenantId(), hikariDataSource);

		}
		return result;
	}
}
