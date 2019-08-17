package com.amx.service_provider;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.WebApplicationContext;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableAutoConfiguration(exclude = { 
//		DataSourceAutoConfiguration.class,
//		JpaRepositoriesAutoConfiguration.class,
//		DataSourceTransactionManagerAutoConfiguration.class,
//		JndiConnectionFactoryAutoConfiguration.class,
//		DataSourceAutoConfiguration.class,
//		HibernateJpaAutoConfiguration.class,
//		JpaRepositoriesAutoConfiguration.class,
//		DataSourceTransactionManagerAutoConfiguration.class})
//@ComponentScan(basePackages = { "com.amx.jax.dbmodel.webservice" })
//@ComponentScan(basePackages = { "com.amx.jax" })
//@EnableAsync(proxyTargetClass = true)
//@EnableScheduling
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
//@EnableAspectJAutoProxy(proxyTargetClass = true)
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//@Component
//@SpringBootApplication
@ComponentScan(basePackages = { "com.amx.service_provider.repository.webservice" })
@EnableAsync(proxyTargetClass = true)
//@EnableScheduling
//@EntityScan("com.amx.jax.dbmodel.webservice")
//@EnableJpaRepositories("com.amx.jax.broker.repository")
public class MsServiceProviderConfig
{
	
}
