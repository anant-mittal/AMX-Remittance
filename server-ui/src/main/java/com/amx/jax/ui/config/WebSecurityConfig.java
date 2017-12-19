package com.amx.jax.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomerAuthProvider customAuthProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.headers().frameOptions().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and().authorizeRequests()
				.antMatchers("/register/**").permitAll().and().authorizeRequests().antMatchers("/home/**").permitAll()
				.and().authorizeRequests().antMatchers("/pub/**").permitAll().and().authorizeRequests()
				.antMatchers("/login/**").permitAll().and().authorizeRequests().antMatchers("/api/**").authenticated()
				.and().authorizeRequests().antMatchers("/app/**").authenticated().and().formLogin().loginPage("/login")
				.permitAll().failureUrl("/login?error").permitAll().and().logout().permitAll()
				.logoutSuccessUrl("/login?logout").permitAll().and().exceptionHandling().accessDeniedPage("/403").and()
				.csrf().disable().headers().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthProvider).inMemoryAuthentication().withUser("user").password("password")
				.roles("USER");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}
}
