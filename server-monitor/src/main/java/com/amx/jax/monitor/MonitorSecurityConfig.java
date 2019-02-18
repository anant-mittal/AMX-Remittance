package com.amx.jax.monitor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@ConditionalOnProperty("app.security")
public class MonitorSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.formLogin()
				.loginPage("/login.html")
				.loginProcessingUrl("/login")
				.permitAll();
		http
				.logout().logoutUrl("/logout");
		http
				.csrf().disable();
		http
				.authorizeRequests()
				.antMatchers("/login.html", "/**/*.css", "/img/**", "/third-party/**","api/applications**")
				.permitAll();
		http
				.authorizeRequests()
				.antMatchers("/**")
				.authenticated();
		http.httpBasic();
	}
}
