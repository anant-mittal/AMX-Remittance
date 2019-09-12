package com.amx.jax.ui.config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.amx.jax.http.ApiRequestConfig;
import com.amx.jax.http.RequestType;

/**
 * The Class WebSecurityConfig.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements ApiRequestConfig {

	/** The custom auth provider. */
	@Autowired
	CustomerAuthProvider customAuthProvider;

	/** The login url entry. */
	@Autowired
	WebLoginUrlEntry loginUrlEntry;

	public static final Pattern pattern = Pattern.compile("^\\/(register|home|login|pub).*$");
	public static final Pattern secured_pattern = Pattern.compile("^\\/(app|api).*$");

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter#configure(org.springframework.security.config.
	 * annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.headers().frameOptions().disable();
		http.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				// Register Calls
				.and().authorizeRequests().antMatchers("/register/**").permitAll()
				// Home Pages Calls
				.and().authorizeRequests().antMatchers("/home/**").permitAll()
				// Publics Calls
				.and().authorizeRequests().antMatchers("/pub/**").permitAll()
				.and().authorizeRequests().antMatchers("/p/**").permitAll()
				// Login Calls
				.and().authorizeRequests().antMatchers("/login/**").permitAll()
				// API Calls
				.and().authorizeRequests().antMatchers("/api/**").authenticated()
				// App Pages
				.and().authorizeRequests().antMatchers("/app/**").authenticated()
				.and().authorizeRequests().antMatchers("/a/**").authenticated().and().authorizeRequests()
				.antMatchers("/.**").authenticated()
				// Login Formas
				.and().formLogin().loginPage("/login").permitAll().failureUrl("/login?error").permitAll()
				// Logiut Pages
				.and().logout().permitAll().logoutSuccessUrl("/login?logout").deleteCookies("JSESSIONID")
				.invalidateHttpSession(true).permitAll().and().exceptionHandling().accessDeniedPage("/403").and().csrf()
				.disable().headers().disable();

		http.exceptionHandling().authenticationEntryPoint(loginUrlEntry);
	}

	public static boolean isPublicUrl(String url) {
		Matcher matcher = pattern.matcher(url);
		return matcher.find();
	}

	public static boolean isSecuredUrl(String url) {
		Matcher matcher = secured_pattern.matcher(url);
		return matcher.find();
	}

	/**
	 * Configure global.
	 *
	 * @param auth the auth
	 * @throws Exception the exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customAuthProvider).inMemoryAuthentication().withUser("user").password("password")
				.roles("USER");
	}

	public static final Pattern SILENT = Pattern.compile("^\\/(resources|static|css|js|images|apple-app-site-association|.well-known|favicon.ico).*$");
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.config.annotation.web.configuration.
	 * WebSecurityConfigurerAdapter#configure(org.springframework.security.config.
	 * annotation.web.builders.WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
				"/apple-app-site-association", "/.well-known/apple-app-site-association", "/favicon.ico");
	}

	@Override
	public RequestType from(HttpServletRequest req, RequestType reqType) {
		if (RequestType.DEFAULT.equals(reqType)) {
			Matcher matcher = SILENT.matcher(req.getRequestURI());
			if(matcher.find()) {
				return RequestType.NO_TRACK_PING;
			}
		}
		return reqType;
	}

//	@Bean
//	public CookieSerializer cookieSerializer() {
//		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
//		serializer.setCookieName("JSESSIONID");
//		serializer.setDomainName("local-kwt.amxremit.com");
//		serializer.setCookiePath("/");
//		//serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
//		serializer.setCookieMaxAge(10); // Set the cookie max age in seconds, e.g. 10 seconds
//		return serializer;
//	}

}
