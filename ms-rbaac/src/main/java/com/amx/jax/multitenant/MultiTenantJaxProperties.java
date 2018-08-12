package com.amx.jax.multitenant;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "multitenancy")
public class MultiTenantJaxProperties {

	private List<DataSourceProperties> dataSourcesProps;
	// Getters and Setters	

	public List<DataSourceProperties> getDataSourcesProps() {
		return dataSourcesProps;
	}

	public void setDataSourcesProps(List<DataSourceProperties> dataSourcesProps) {
		this.dataSourcesProps = dataSourcesProps;
	}

	public static class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {

		public String getTenantId() {
			return tenantId;
		}

		public void setTenantId(String tenantId) {
			this.tenantId = tenantId;
		}

		private String tenantId;
		// Getters and Setters
	}
}
