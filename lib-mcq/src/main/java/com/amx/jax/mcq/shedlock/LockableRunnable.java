/**
 * Copyright 2009-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.amx.jax.mcq.shedlock;

import static java.util.Objects.requireNonNull;

import com.amx.jax.mcq.Candidate;
import com.amx.jax.mcq.MCQLocker;

/**
 * Executes wrapped runnable
 */
public class LockableRunnable implements Runnable {
	private final Runnable task;

	private final Candidate lock;
	private final MCQLocker locker;

	public LockableRunnable(Runnable task, MCQLocker locker, Candidate lock) {
		this.task = requireNonNull(task);
		this.lock = requireNonNull(lock);
		this.locker = requireNonNull(locker);
	}

	@Override
	public void run() {
		locker.executeWithLock(task, lock);
	}
}
