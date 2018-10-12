
package com.amx.jax.ui.config;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * The Class CustomerAuthProvider.
 */
@Component
public class CustomerAuthProvider implements AuthenticationProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String username = auth.getName();
		String password = null;
		if (auth.getCredentials() != null) {
			password = auth.getCredentials().toString();
		}
		return new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.authentication.AuthenticationProvider#supports(
	 * java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}

}
