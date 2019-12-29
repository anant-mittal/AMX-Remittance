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

			/**
			 * Set Connection Pool Properties
			 */

			// Initial Pool Size : default 10
			if (dsProperties.getInitialSize() != null) {
				tomcatDataSource.setInitialSize(dsProperties.getInitialSize());
			}

			// Maximum Active Connections Ever : default 100
			if (dsProperties.getMaxActive() != null) {
				tomcatDataSource.setMaxActive(dsProperties.getMaxActive());
			}

			// Maximum Idle Connections Ever : default 100
			if (dsProperties.getMaxIdle() != null) {
				tomcatDataSource.setMaxIdle(dsProperties.getMaxIdle());
			}

			// Minimum Idle Connections Ever : default 10
			if (dsProperties.getMinIdle() != null) {
				tomcatDataSource.setMinIdle(dsProperties.getMinIdle());
			}

			tomcatDataSource.setTestOnBorrow(true);
			tomcatDataSource.setValidationQuery("select 1 from dual");
			tomcatDataSource.setTestWhileIdle(true);
			result.put(dsProperties.getTenantId(), tomcatDataSource);

		}
		return result;
	}
}
