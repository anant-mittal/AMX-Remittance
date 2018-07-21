package com.amx.jax.admin.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.scope.TenantContext;


/**
 * The Class AuthLibContext.
 */
@Service
public class AuthLibContext extends TenantContext<AuthLib> {

	/**
	 * The Interface AuthLib.
	 */
	public interface AuthLib {

		/**
		 * To next auth state.
		 *
		 * @param authState
		 *            the auth state
		 * @return the auth state
		 */
		public AuthState toNextAuthState(AuthState authState);

		/**
		 * Gets the next auth step.
		 *
		 * @param authState
		 *            the auth state
		 * @return the next auth step
		 */
		public AuthState.AuthStep getNextAuthStep(AuthState authState);

	}

	/**
	 * Instantiates a new auth lib context.
	 *
	 * @param libs
	 *            the libs
	 */
	@Autowired
	public AuthLibContext(List<AuthLib> libs) {
		super(libs);
	}

}
