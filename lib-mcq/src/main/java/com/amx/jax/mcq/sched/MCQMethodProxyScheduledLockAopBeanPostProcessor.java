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
package com.amx.jax.mcq.sched;

import org.springframework.aop.support.AbstractPointcutAdvisor;

import net.javacrumbs.shedlock.core.DefaultLockManager;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.spring.internal.SpringLockConfigurationExtractor;

public class MCQMethodProxyScheduledLockAopBeanPostProcessor extends AProxyScheduledLockAopBeanPostProcessor {
	private static final long serialVersionUID = -7982258433616761914L;

	public MCQMethodProxyScheduledLockAopBeanPostProcessor(String defaultLockAtMostFor, String defaultLockAtLeastFor) {
		super(defaultLockAtMostFor, defaultLockAtLeastFor);
	}

	@Override
	protected AbstractPointcutAdvisor getAdvisor(LockProvider lockProvider) {
		SpringLockConfigurationExtractor lockConfigurationExtractor = new SpringLockConfigurationExtractor(
				defaultLockAtMostForDuration(), defaultLockAtLeastForDuration(), getResolver());
		return new MCQMethodPointcutAdvisor(new DefaultLockManager(lockProvider, lockConfigurationExtractor));
	}
}
