package com.amx.jax.mcq;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.AppConfig;

@Service
public class MCQ {

	private Logger LOGGER = LoggerFactory.getLogger(MCQ.class);
	private Object lock = new Object();

	@Autowired(required = false)
	RedissonClient redisson;

	@Autowired
	AppConfig appConfig;

	boolean leader;

	private boolean isPrivateLeader(String key, long timeinMilliSeconds) {
		RMapCache<String, String> map = redisson
				.getMapCache("MCQFLAGS");

		String previousLeader = map.putIfAbsent(key, appConfig.getSpringAppName(), timeinMilliSeconds,
				TimeUnit.MILLISECONDS);
		LOGGER.info("Leader:{} for key:{}", previousLeader, key);
		if (previousLeader == null) {
			return true;
		}

		if (appConfig.getSpringAppName().equalsIgnoreCase(previousLeader)) {
			return true;
		}
		return false;
	}

	public boolean claimLeaderShip(String key, long timeinMilliSeconds) {

		if (redisson == null) {
			return false;
		}

		return isPrivateLeader(key, timeinMilliSeconds);
	}

	public <T> boolean claimLeaderShip(Class<T> class1, long timeinMilliSeconds) {
		return this.claimLeaderShip(class1.getName(), timeinMilliSeconds);
	}

	public boolean resignLeaderShip(String key) {
		RMapCache<String, String> map = redisson
				.getMapCache("MCQFLAGS");
		return map.remove(key, appConfig.getSpringAppName());
	}

	public <T> boolean resignLeaderShip(Class<T> class1) {
		return this.resignLeaderShip(class1.getName());
	}
}
