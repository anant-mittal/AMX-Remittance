package com.amx.jax.postman;

import javax.servlet.ServletRequestListener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import com.amx.jax.postman.model.Langs;
import com.bootloaderjs.ArgUtil;

@SpringBootApplication
@ComponentScan("com.amx.jax")
@EnableAsync(proxyTargetClass = true)
public class PostManApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostManApplication.class, args);
	}

	@Value("${jax.lang.code}")
	private String defaultLangCode;

	@Bean
	Langs defaultLang() {
		return (Langs) ArgUtil.parseAsEnum(defaultLangCode, Langs.DEFAULT);
	}

	@Bean
	ServletListenerRegistrationBean<ServletRequestListener> myServletRequestListener() {
		ServletListenerRegistrationBean<ServletRequestListener> srb = new ServletListenerRegistrationBean<>();
		srb.setListener(new WebRequestListener());
		return srb;
	}

	// @Bean
	// public SpringTemplateEngine customTemplateEngine(SpringTemplateEngine
	// templateEngine) {
	// SpringTemplateEngine customTemplateEngine = new SpringTemplateEngine();
	// customTemplateEngine.setEnableSpringELCompiler(true);
	// for (ITemplateResolver iterable_element :
	// templateEngine.getTemplateResolvers()) {
	// customTemplateEngine.setTemplateResolver(iterable_element);
	// }
	// customTemplateEngine.addDialect(new HelloDialect());
	// return customTemplateEngine;
	// }
}
