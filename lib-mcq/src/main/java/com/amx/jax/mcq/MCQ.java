package com.amx.jax.mcq;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.AppConfig;
import com.amx.jax.tunnel.TunnelEventXchange;

@Service
public class MCQ {

	private Logger LOGGER = LoggerFactory.getLogger(MCQ.class);

	@Autowired(required = false)
	RedissonClient redisson;

	@Autowired
	AppConfig appConfig;

	public boolean amILeader() {
		if (redisson == null) {
			return false;
		}

		RMapCache<String, String> map = redisson
				.getMapCache("MCQFLAGS");

		map.put("amILeader", appConfig.getSpringAppName());

		return false;
	}
}
