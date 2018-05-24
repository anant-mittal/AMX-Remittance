package com.amx.jax.scheduler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.PostManService;
import com.amx.jax.scheduler.config.SchedulerConfig;
import com.amx.jax.scheduler.task.RateAlertTask;
import com.amx.jax.scheduler.task.trigger.RateAlertTrigger;
import com.amx.jax.AppConfig;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.client.config.JaxConfig;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = { "com.amx.jax" })
@PropertySource("classpath:application-lib.properties")
public class ServerSchedulerApplication implements SchedulingConfigurer {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	JaxConfig jaxConfig;

	@Autowired
	SchedulerConfig schedulerConfig;

	public static void main(String[] args) {
		SpringApplication.run(ServerSchedulerApplication.class, args);
	}

	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		logger.info("In configureTasks");
		taskRegistrar.setScheduler(threadPoolTaskScheduler());
		for (Tenant tenant : schedulerConfig.getTenants()) {
			logger.info("Configuring Rate alert task, for tenant " + tenant);
			RateAlertTask rateAlertTask = rateAlertTask();
			rateAlertTask.setTenant(tenant);
			ScheduledFuture<?> future = threadPoolTaskScheduler().schedule(rateAlertTask, rateAlertTrigger());
			rateAlertTrigger().setFuture(future);
		}
		logger.info("End of configureTasks");
	}

	@Bean(destroyMethod = "shutdown")
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(Integer.valueOf(10));
		threadPoolTaskScheduler.setThreadNamePrefix("ThreadPool-Task-Scheduler");
		return threadPoolTaskScheduler;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		return restTemplate;
	}

	@Bean("base_url")
	public URL baseUrl() throws MalformedURLException {
		return new URL(jaxConfig.getSpServiceUrl());
	}

	@Bean
	public JaxConfig jaxConfig() {
		return new JaxConfig();
	}

	@Bean
	@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public com.amx.jax.amxlib.model.JaxMetaInfo JaxMetaInfo() {
		com.amx.jax.amxlib.model.JaxMetaInfo metaInfo = new com.amx.jax.amxlib.model.JaxMetaInfo();
		return metaInfo;
	}

	@Bean
	@Scope("prototype")
	public RateAlertTask rateAlertTask() {
		return new RateAlertTask();
	}

	@Bean
	public RateAlertTrigger rateAlertTrigger() {
		return new RateAlertTrigger();
	}

}
