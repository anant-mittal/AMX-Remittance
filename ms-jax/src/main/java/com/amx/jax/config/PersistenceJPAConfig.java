package com.amx.jax.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.multitenant.MultiTenantConnectionProviderImpl;

@Configuration
public class PersistenceJPAConfig {

	@Autowired
	private JpaProperties jpaProperties;
	
	@Autowired
	private MultiTenantConnectionProviderImpl multiTenantConnectionProviderImpl;

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
			MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
			CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl) {
		Map<String, Object> properties = new HashMap<>();
		properties.putAll(jpaProperties.getHibernateProperties(dataSource));
		properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
		properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
		properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);
		properties.put("hibernate.show_sql", "false");
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.amx.jax");
		em.setJpaVendorAdapter(jpaVendorAdapter());
		em.setJpaPropertyMap(properties);
		return em;
	}
	
	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public JdbcTemplate jdbcTemplate( ) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(multiTenantConnectionProviderImpl.getDataSource());
		return jdbcTemplate;
	}
}
