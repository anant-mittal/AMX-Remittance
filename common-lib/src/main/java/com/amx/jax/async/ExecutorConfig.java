package com.amx.jax.async;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ExecutorConfig extends AsyncConfigurerSupport {

	public static final String DEFAULT = "silverExecutor";
	public static final String EXECUTER_BRONZE = "bronzeExecutor";

	public static final String EXECUTER_WORKER = "workerExecutor";
	public static final String EXECUTER_GOLD = "goldExecutor";
	public static final String EXECUTER_PLATINUM = "platinumExecutor";
	public static final String EXECUTER_DIAMOND = "diamondExecutor";
	public static final String EXECUTER_TASK = "task";

	@Override
	@Bean
	public Executor getAsyncExecutor() {
		ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
		executor.setCorePoolSize(5);
		executor.setThreadNamePrefix(DEFAULT + "-");
		executor.initialize();
		return executor;
	}

	@Bean(name = EXECUTER_WORKER)
	public TaskExecutor taskExecutorWorker() {
		ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
		executor.setCorePoolSize(200);
		executor.setThreadNamePrefix(EXECUTER_GOLD + "-");
		executor.initialize();
		return executor;
	}

	@Bean(name = EXECUTER_GOLD)
	public TaskExecutor taskExecutorGold() {
		ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
		executor.setCorePoolSize(100);
		executor.setThreadNamePrefix(EXECUTER_GOLD + "-");
		executor.initialize();
		return executor;
	}

	@Bean(name = EXECUTER_PLATINUM)
	public TaskExecutor taskExecutorPlatinum() {
		ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
		executor.setCorePoolSize(100);
		executor.setThreadNamePrefix(EXECUTER_PLATINUM + "-");
		executor.initialize();
		return executor;
	}

	@Bean(name = EXECUTER_DIAMOND)
	public TaskExecutor taskExecutorDiamond() {
		ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
		executor.setCorePoolSize(100);
		executor.setThreadNamePrefix(EXECUTER_DIAMOND + "-");
		executor.initialize();
		return executor;
	}

	@Bean(name = EXECUTER_BRONZE)
	public TaskExecutor taskExecutorBronze() {
		ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
		executor.setCorePoolSize(20);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(200);
		executor.setAllowCoreThreadTimeOut(true);
		executor.setKeepAliveSeconds(120);
		executor.setThreadNamePrefix(EXECUTER_BRONZE + "-");
		executor.initialize();
		return executor;
	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(10);
		threadPoolTaskScheduler.setThreadNamePrefix(EXECUTER_TASK + "-");
		return threadPoolTaskScheduler;
	}
}
