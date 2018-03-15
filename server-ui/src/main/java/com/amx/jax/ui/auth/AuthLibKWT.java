package com.amx.jax.ui.auth;

import org.springframework.stereotype.Component;

import com.amx.jax.scope.Tenant;
import com.amx.jax.scope.TenantSpecific;
import com.amx.jax.ui.auth.AuthLibContext.AuthLib;

@Component
@TenantSpecific(Tenant.KWT)
public class AuthLibKWT implements AuthLib {

	@Override
	public AuthState toNextAuthState(AuthState authState) {
		authState.cStep = toNextAuthStep(authState);
		return authState;
	}

	public AuthState.AuthStep toNextAuthStep(AuthState authState) {
		if (authState.flow == AuthState.AuthFlow.LOGIN) {
			if (authState.cStep == null) {
				return AuthState.AuthStep.IDVALID;
			}
			switch (authState.cStep) {
			case USERPASS:
				return AuthState.AuthStep.SECQUES;
			case SECQUES:
				return AuthState.AuthStep.COMPLETED;
			default:
				return AuthState.AuthStep.USERPASS;
			}
		} else if (authState.flow == AuthState.AuthFlow.RESET_PASS) {
			if (authState.cStep == null) {
				return AuthState.AuthStep.IDVALID;
			}
			switch (authState.cStep) {
			case IDVALID:
				return AuthState.AuthStep.DOTPVFY;
			case DOTPVFY:
				return AuthState.AuthStep.COMPLETED;
			default:
				return authState.cStep;
			}
		} else if (authState.flow == AuthState.AuthFlow.ACTIVATION) {
			if (authState.cStep == null) {
				return AuthState.AuthStep.IDVALID;
			}
			switch (authState.cStep) {
			case IDVALID:
				return AuthState.AuthStep.MOTPVFY;
			case MOTPVFY:
				return AuthState.AuthStep.COMPLETED;
			default:
				return authState.cStep;
			}
		}
		return authState.cStep;
	}

}
