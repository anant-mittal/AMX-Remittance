package com.amx.jax.ui.auth;

import org.springframework.stereotype.Component;

import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantSpecific;
import com.amx.jax.ui.auth.AuthLibContext.AuthLib;

@Component
@TenantSpecific({ Tenant.KWT, Tenant.BHR, Tenant.KWT2 })
public class AuthLibKWT implements AuthLib {

	@Override
	public AuthState toNextAuthState(AuthState authState) {
		authState.cStep = getNextAuthStep(authState);
		return authState;
	}

	public AuthState.AuthStep getNextAuthStep(AuthState authState) {
		if (authState.flow == AuthState.AuthFlow.LOGIN) {
			if (authState.cStep == null) {
				return AuthState.AuthStep.USERPASS;
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
				if (authState.isPresentEmail()) {
					return AuthState.AuthStep.SECQ_SET;
				} else {
					return AuthState.AuthStep.DATA_VERIFY;
				}
			case DATA_VERIFY:
				return AuthState.AuthStep.SECQ_SET;
			case SECQ_SET:
				return AuthState.AuthStep.CAPTION_SET;
			case CAPTION_SET:
				return AuthState.AuthStep.CREDS_SET;
			case CREDS_SET:
				return AuthState.AuthStep.COMPLETED;
			default:
				return authState.cStep;
			}
		} else if (authState.flow == AuthState.AuthFlow.REGISTRATION) {
			if (authState.cStep == null) {
				return AuthState.AuthStep.IDVALID;
			}
			switch (authState.cStep) {
			case IDVALID:
				return AuthState.AuthStep.DOTPVFY;
			case DOTPVFY:
				return AuthState.AuthStep.SAVE_HOME;
			case SAVE_HOME:
				return AuthState.AuthStep.SECQ_SET;
			case SECQ_SET:
				return AuthState.AuthStep.CAPTION_SET;
			case CAPTION_SET:
				return AuthState.AuthStep.CREDS_SET;
			case CREDS_SET:
				return AuthState.AuthStep.COMPLETED;
			default:
				return authState.cStep;
			}
		}
		return authState.cStep;
	}

}
