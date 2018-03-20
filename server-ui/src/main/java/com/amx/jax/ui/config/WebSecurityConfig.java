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
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				// Register Calls
				.and().authorizeRequests().antMatchers("/register/**").permitAll()
				// Home Pages Calls
				.and().authorizeRequests().antMatchers("/home/**").permitAll()
				// Publics Calls
				.and().authorizeRequests().antMatchers("/pub/**").permitAll()
				// Login Calls
				.and().authorizeRequests().antMatchers("/login/**").permitAll()
				// API Calls
				.and().authorizeRequests().antMatchers("/api/**").authenticated()
				// App Pages
				.and().authorizeRequests().antMatchers("/app/**").authenticated().and().authorizeRequests()
				.antMatchers("/.**").authenticated()
				// Login Formas
				.and().formLogin().loginPage("/login").permitAll().failureUrl("/login?error").permitAll()
				// Logiut Pages
				.and().logout().permitAll().logoutSuccessUrl("/login?logout").deleteCookies("JSESSIONID")
				.invalidateHttpSession(true).permitAll().and().exceptionHandling().accessDeniedPage("/403").and().csrf()
				.disable().headers().disable();
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
