package com.amx.jax.ui.config;

import java.io.IOException;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.session.config.EnableRedissonHttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@EnableRedissonHttpSession
@ConditionalOnExpression(com.amx.jax.ui.WebApplication.USE_REDIS)
public class CacheRedisConfiguration extends AbstractHttpSessionApplicationInitializer {
	// @Bean
	// public LettuceConnectionFactory connectionFactory() {
	// return new LettuceConnectionFactory(); // <2>
	// }

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private String port;

	@Bean(destroyMethod = "shutdown")
	public RedissonClient redisson() throws IOException {
		Config config = new Config();
		// config.useClusterServers().addNodeAddress("redis://10.28.42.83:6379");
		config.useSingleServer().setAddress("redis://" + host + ":" + port);
		return Redisson.create(config);
	}
}
