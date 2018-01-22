package com.amx.jax.logger;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

import com.amx.jax.logger.model.ApplicationLog;
import com.amx.jax.logger.model.CustomerLog;

/**
 * Spring Configurations.
 */
//@Configuration
public class ApplicationConfig {

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * Populate ApplicationLog collection with sample data.
	 *
	 */
	@Bean
	public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {

		Jackson2RepositoryPopulatorFactoryBean factoryBean = new Jackson2RepositoryPopulatorFactoryBean();
		factoryBean.setResources(new Resource[] { new ClassPathResource("app-log.json") });
		return factoryBean;
	}

	/**
	 * Clean up after execution by dropping used test db instance.
	 *
	 * @throws Exception
	 */
	@PreDestroy
	void dropTestDB() throws Exception {
		mongoTemplate.dropCollection(ApplicationLog.class);
		mongoTemplate.dropCollection(CustomerLog.class);
	}

}