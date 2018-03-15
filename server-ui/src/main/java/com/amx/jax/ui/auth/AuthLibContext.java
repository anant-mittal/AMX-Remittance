package com.amx.jax.ui.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.scope.TenantContext;
import com.amx.jax.ui.auth.AuthLibContext.AuthLib;

@Service
public class AuthLibContext extends TenantContext<AuthLib> {

	public interface AuthLib {
		public AuthState toNextAuthState(AuthState authState);

	}

	@Autowired
	public AuthLibContext(List<AuthLib> libs) {
		super(libs);
	}

}
