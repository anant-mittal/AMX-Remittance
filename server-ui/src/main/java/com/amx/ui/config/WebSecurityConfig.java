package com.amx.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomerAuthProvider customAuthProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http.headers().frameOptions().disable();
		http.headers().disable();
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/register/**").permitAll();
		http.authorizeRequests().antMatchers("/app/**").authenticated();
		http.formLogin().loginPage("/login").failureUrl("/login?error").permitAll().and().logout()
				.logoutSuccessUrl("/login?logout").permitAll().and().exceptionHandling().accessDeniedPage("/403");
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
