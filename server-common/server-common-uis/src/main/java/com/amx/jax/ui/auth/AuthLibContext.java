package com.amx.jax.ui.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.AuthState;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.ui.UIConstants.Features;
import com.amx.jax.ui.auth.AuthLibContext.AuthLib;

/**
 * The Class AuthLibContext.
 */
@Service
public class AuthLibContext extends TenantContext<AuthLib> {

	private static final long serialVersionUID = -6363240793649510261L;

	/**
	 * The Interface AuthLib.
	 */
	public interface AuthLib {

		/**
		 * To next auth state.
		 *
		 * @param authState the auth state
		 * @return the auth state
		 */
		public AuthState toNextAuthState(AuthState authState);

		/**
		 * Gets the next auth step.
		 *
		 * @param authState the auth state
		 * @return the next auth step
		 */
		public AuthState.AuthStep getNextAuthStep(AuthState authState);

		public CustomerFlags checkUserMeta(AuthState authState, CustomerFlags customerFlags);

		public CustomerFlags checkModule(AuthState authState, CustomerFlags customerFlags, Features feature);

		public default boolean hasFeature(AuthState authState, CustomerFlags customerFlags, Features feature) {
			return true;
		};

		public default List<Features> filterFeatures(AuthState authState, CustomerFlags customerFlags,
				List<Features> features) {
			List<Features> newfeatures = new ArrayList<Features>();
			for (int i = 0; i < features.size(); i++) {
				if (this.hasFeature(authState, customerFlags, features.get(i))) {
					newfeatures.add(features.get(i));
				}
			}
			return newfeatures;
		};

	}

	/**
	 * Instantiates a new auth lib context.
	 *
	 * @param libs the libs
	 */
	@Autowired
	public AuthLibContext(List<AuthLib> libs) {
		super(libs);
	}

}
