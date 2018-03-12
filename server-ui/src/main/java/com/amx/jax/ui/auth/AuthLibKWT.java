package com.amx.jax.ui.auth;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.Tenant;
import com.amx.jax.scope.TenantSpecific;
import com.amx.jax.ui.auth.AuthLibContext.AuthLib;
import com.amx.jax.ui.session.GuestSession.AuthFlow;
import com.amx.jax.ui.session.GuestSession.AuthStep;

@Component
@TenantSpecific(Tenant.KWT)
public class AuthLibKWT implements AuthLib {

	@Override
	public AuthStep getNextAuthStep(AuthFlow authFlow, AuthStep authStep) {
		if (authFlow == AuthFlow.LOGIN) {
			switch (authStep) {
			case USERPASS:
				return AuthStep.SECQUES;
			case SECQUES:
				return AuthStep.COMPLETED;
			default:
				return AuthStep.USERPASS;
			}
		} else if (authFlow == AuthFlow.RESET_PASS) {
			switch (authStep) {
			case IDVALID:
				return AuthStep.DOTPVFY;
			case DOTPVFY:
				return AuthStep.COMPLETED;
			default:
				return authStep;
			}
		} else if (authFlow == AuthFlow.ACTIVATION) {
			switch (authStep) {
			case IDVALID:
				return AuthStep.MOTPVFY;
			case MOTPVFY:
				return AuthStep.COMPLETED;
			default:
				return authStep;
			}
		}
		return authStep;
	}

}
