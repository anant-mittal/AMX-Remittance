package com.amx.jax.mcq.sched;

import java.util.Arrays;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import com.amx.jax.logger.LoggerService;

import net.javacrumbs.shedlock.core.LockManager;
import net.javacrumbs.shedlock.core.LockableRunnable;
import net.javacrumbs.shedlock.core.SchedulerLock;

public class MCQMethodPointcutAdvisor extends AbstractPointcutAdvisor {

	private static final long serialVersionUID = -603390456242461316L;
	private static final Logger LOGGER = LoggerService.getLogger(MCQMethodPointcutAdvisor.class);
	private final Pointcut pointcut = AnnotationMatchingPointcut.forMethodAnnotation(SchedulerLock.class);
	private final Advice advice;

	MCQMethodPointcutAdvisor(LockManager lockManager) {
		this.advice = new LockMethodInterceptor(lockManager);
	}

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	@Override
	public Advice getAdvice() {
		return this.advice;
	}

	public class LockMethodInterceptor implements MethodInterceptor {

		private final LockManager lockManager;

		private LockMethodInterceptor(LockManager lockManager) {
			this.lockManager = lockManager;
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {

			System.out.println("Method name : "
					+ invocation.getMethod().getName());
			System.out.println("Method arguments : "
					+ Arrays.toString(invocation.getArguments()));

			System.out.println("HijackAroundMethod : Before method hijacked!");

			try {
				Object[] arguments = invocation.getArguments();
				if (arguments.length >= 1 && arguments[0] instanceof Runnable) {
					arguments[0] = new LockableRunnable((Runnable) arguments[0], lockManager);
				} else {
					LOGGER.warn("Task scheduler first argument should be Runnable");
				}
				return invocation.proceed();
			} catch (IllegalArgumentException e) {
				System.out.println("HijackAroundMethod : Throw exception hijacked!");
				throw e;
			}
		}

	}

}
