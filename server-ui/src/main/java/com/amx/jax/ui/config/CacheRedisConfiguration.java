package com.amx.jax.ui.config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.LocalCachedMapOptions.EvictionPolicy;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.session.config.EnableRedissonHttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@EnableRedissonHttpSession
@ConditionalOnExpression(com.amx.jax.ui.WebApplication.USE_REDIS)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class CacheRedisConfiguration extends AbstractHttpSessionApplicationInitializer {
	// @Bean
	// public LettuceConnectionFactory connectionFactory() {
	// return new LettuceConnectionFactory(); // <2>
	// }

	@Autowired
	private RedisProperties redisProperties;

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private String port;

	@Bean(destroyMethod = "shutdown")
	public RedissonClient redisson() throws IOException {
		// Config config = new Config();
		// config.useClusterServers().addNodeAddress("redis://10.28.42.83:6379");
		// config.useSingleServer().setAddress("redis://" + host + ":" + port);
		// return Redisson.create(config);

		Config config = new Config();
		// sentinel
		if (redisProperties.getSentinel() != null) {
			SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
			sentinelServersConfig.setMasterName(redisProperties.getSentinel().getMaster());
			redisProperties.getSentinel().getNodes();
			sentinelServersConfig.addSentinelAddress(redisProperties.getSentinel().getNodes().split(","));
			sentinelServersConfig.setDatabase(redisProperties.getDatabase());
			if (redisProperties.getPassword() != null) {
				sentinelServersConfig.setPassword(redisProperties.getPassword());
			}
		} else { // single server
			SingleServerConfig singleServerConfig = config.useSingleServer();
			// format as redis://127.0.0.1:7181 or rediss://127.0.0.1:7181 for SSL
			String schema = redisProperties.isSsl() ? "rediss://" : "redis://";
			singleServerConfig.setAddress(schema + redisProperties.getHost() + ":" + redisProperties.getPort());
			singleServerConfig.setDatabase(redisProperties.getDatabase());
			if (redisProperties.getPassword() != null) {
				singleServerConfig.setPassword(redisProperties.getPassword());
			}
		}
		config.setCodec(new org.redisson.codec.FstCodec());
		return Redisson.create(config);
	}
}
