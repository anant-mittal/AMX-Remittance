package com.amx.jax.mcq;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;

public class MCQLockProvider implements LockProvider {

	private MCQ mcq;

	public MCQLockProvider(MCQ mcq) {
		this.mcq = mcq;
	}

	@Override
	public Optional<SimpleLock> lock(LockConfiguration lockConfiguration) {

		Expiration maxExpiration = getExpiration(lockConfiguration.getLockAtMostUntil());
		Expiration minExpiration = getExpiration(lockConfiguration.getLockAtLeastUntil());

		Candidate candidate = new Candidate().fixedDelay(minExpiration.getExpirationTimeInMilliseconds())
				.maxAge(maxExpiration.getExpirationTimeInMilliseconds()).queue(lockConfiguration.getName());

		if (this.mcq.lead(candidate)) {
			return Optional.of(new MCQLock(mcq, candidate));
		} else {
			return Optional.empty();
		}
	}

	private static Expiration getExpiration(Instant until) {
		return Expiration.from(getMsUntil(until), TimeUnit.MILLISECONDS);
	}

	private static long getMsUntil(Instant until) {
		return Duration.between(Instant.now(), until).toMillis();
	}

	private static final class MCQLock implements SimpleLock {

		private Candidate candidate;
		private MCQ mcq;

		public MCQLock(MCQ mcq, Candidate candidate) {
			this.mcq = mcq;
			this.candidate = candidate;
		}

		@Override
		public void unlock() {
			this.mcq.resign(candidate);
		}

	}
}
