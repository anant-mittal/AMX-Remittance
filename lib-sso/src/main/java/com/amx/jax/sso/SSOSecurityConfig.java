package com.amx.jax.sso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.amx.jax.sso.client.SSOAuthProvider;
import com.amx.jax.sso.client.SSOLoginUrlEntry;

@Configuration
@EnableWebSecurity
public class SSOSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	SSOAuthProvider customAuthProvider;

	@Autowired
	SSOLoginUrlEntry loginUrlEntry;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				// Publics Calls
				.and().authorizeRequests().antMatchers("/pub/**").permitAll()
				// Login Calls
				.and().authorizeRequests().antMatchers("/sso/**").permitAll()
				// API Calls
				.and().authorizeRequests().antMatchers("/api/**").authenticated()
				// App Pages
				.and().authorizeRequests().antMatchers("/app/**").authenticated().and().authorizeRequests()
				.antMatchers("/.**").authenticated()
				// Login Forms
				.and().formLogin().loginPage(SSOConstants.APP_LOGIN_URL_CHECK).successHandler(successHandler())
				.permitAll().failureUrl("/sso/login?error").permitAll()
				// Logout Pages
				.and().logout().permitAll().logoutSuccessUrl("/sso/login?logout").deleteCookies("JSESSIONID")
				.invalidateHttpSession(true).permitAll().and().exceptionHandling().accessDeniedPage("/403").and().csrf()
				.disable().headers().disable();
		http.exceptionHandling().authenticationEntryPoint(loginUrlEntry);
	}

	@Bean
	public AuthenticationSuccessHandler successHandler() {
		SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
		handler.setUseReferer(true);
		return handler;
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
