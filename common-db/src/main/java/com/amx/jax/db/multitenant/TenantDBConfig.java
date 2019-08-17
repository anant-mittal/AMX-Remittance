package com.amx.jax.db.multitenant;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@Component
@TenantScoped
public class TenantDBConfig {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@TenantValue("${spring.datasource.driver-class-name}")
	String dataSourceDriverClassName;
	@TenantValue("${spring.datasource.url}")
	String dataSourceUrl;
	@TenantValue("${spring.datasource.username}")
	String dataSourceUsername;
	@TenantValue("${spring.datasource.password}")
	String dataSourcePassword;

	public String getDataSourceDriverClassName() {
		return dataSourceDriverClassName;
	}

	public String getDataSourceUrl() {
		return dataSourceUrl;
	}

	public String getDataSourceUsername() {
		return dataSourceUsername;
	}

	public String getDataSourcePassword() {
		return dataSourcePassword;
	}

	DataSource dataSource;

	public DataSource getDataSource() {
		if (dataSource == null) {
			LOGGER.info("dataSource is NULL So creating One");
			DataSourceBuilder factory = DataSourceBuilder.create().url(getDataSourceUrl())
					.username(getDataSourceUsername()).password(getDataSourcePassword())
					.driverClassName(getDataSourceDriverClassName());
			org.apache.tomcat.jdbc.pool.DataSource tomcatDataSource = (org.apache.tomcat.jdbc.pool.DataSource) factory
					.build();
			tomcatDataSource.setTestOnBorrow(true);
			tomcatDataSource.setValidationQuery("select 1 from dual");
			tomcatDataSource.setTestWhileIdle(true);
			dataSource = tomcatDataSource;
			LOGGER.info("dataSource was NULL So created One");
		}
		return dataSource;
	}

}
