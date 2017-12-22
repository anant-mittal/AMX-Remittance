package com.amx.jax.ui.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@EnableRedisHttpSession
@ConditionalOnExpression(com.amx.jax.ui.WebApplication.USE_REDIS)
public class CacheRedisConfiguration extends AbstractHttpSessionApplicationInitializer {
//	@Bean
//	public LettuceConnectionFactory connectionFactory() {
//		return new LettuceConnectionFactory(); // <2>
//	}
}
