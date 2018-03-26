package com.amx.jax.session;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
// @EnableRedissonHttpSession
@ConditionalOnProperty("app.cache.session")
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedissonHttpSessionConfig extends AbstractHttpSessionApplicationInitializer {

}