package com.amx.jax.ui.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;

@Configuration
@EnableHazelcastHttpSession
@ConditionalOnExpression(com.amx.jax.ui.WebApplication.USE_HAZELCAST)
public class HazelcastConfiguration {

	@Value("${spring.session.hazelcast.server}")
	private String server;

	@Bean
	public ClientConfig clientConfig() throws Exception {
		ClientConfig clientConfig = new ClientConfig();
		final ClientNetworkConfig networkConfig = new ClientNetworkConfig();
		networkConfig.addAddress(server);
		clientConfig.setNetworkConfig(networkConfig);
		clientConfig.setInstanceName("spring:session:sessions");
		return clientConfig;
	}

	@Bean
	public HazelcastInstance getHazelcastClientInstance(ClientConfig clientConfig) throws IOException {
		HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
		return hazelcastInstance;
	}

}
