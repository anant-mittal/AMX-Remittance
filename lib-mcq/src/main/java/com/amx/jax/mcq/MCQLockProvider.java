package com.amx.jax.mcq;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.types.Expiration;

import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;

public class MCQLockProvider implements LockProvider {

	private MCQLocker mcq;

	public static Candidate getCandidate(LockConfiguration lockConfiguration) {
		long maxExpiration = Math.max(0,
				getExpiration(lockConfiguration.getLockAtMostUntil()).getExpirationTimeInMilliseconds());
		long minExpiration = Math.max(1000,
				getExpiration(lockConfiguration.getLockAtLeastUntil()).getExpirationTimeInMilliseconds());

		if (maxExpiration == 0L) {
			maxExpiration = minExpiration + 120000;
		}

		Candidate candidate = new Candidate()
				.fixedDelay(Math.min(minExpiration, maxExpiration))
				.maxAge(Math.max(minExpiration, maxExpiration))
				.queue(lockConfiguration.getName());

		return candidate;
	}

	@Override
	public Optional<SimpleLock> lock(LockConfiguration lockConfiguration) {
		Candidate candidate = getCandidate(lockConfiguration);
		if (this.mcq.lead(candidate)) {
			return Optional.of(new MCQLock(mcq, candidate));
		} else {
			return Optional.empty();
		}
	}

	public static Expiration getExpiration(Instant until) {
		return Expiration.from(getMsUntil(until), TimeUnit.MILLISECONDS);
	}

	public static long getMsUntil(Instant until) {
		return Duration.between(Instant.now(), until).toMillis();
	}

	private static final class MCQLock implements SimpleLock {

		private Candidate candidate;
		private MCQLocker mcq;

		public MCQLock(MCQLocker mcq, Candidate candidate) {
			this.mcq = mcq;
			this.candidate = candidate;
		}

		@Override
		public void unlock() {
			this.mcq.resign(candidate);
		}

	}
}
