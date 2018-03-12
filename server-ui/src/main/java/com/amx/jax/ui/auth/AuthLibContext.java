package com.amx.jax.ui.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.scope.TenantContext;
import com.amx.jax.ui.auth.AuthLibContext.AuthLib;
import com.amx.jax.ui.session.GuestSession.AuthFlow;
import com.amx.jax.ui.session.GuestSession.AuthStep;

@Service
public class AuthLibContext extends TenantContext<AuthLib> {

	public interface AuthLib {
		public AuthStep getNextAuthStep(AuthFlow authFlow, AuthStep authStep);

	}

	@Autowired
	public AuthLibContext(List<AuthLib> libs) {
		super(libs);
	}

}
