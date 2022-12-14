package com.amx.jax.ui.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Tenant;
import com.amx.jax.model.AuthState;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.scope.TenantSpecific;
import com.amx.jax.ui.UIConstants.Features;
import com.amx.jax.ui.auth.AuthLibContext.AuthLib;
import com.amx.jax.ui.session.UserDeviceBean;

/**
 * The Class AuthLibKWT.
 */
@Component
@TenantSpecific({ Tenant.KWT,Tenant.KWT2})
public class AuthLibKWT implements AuthLib {

	@Autowired
	private UserDeviceBean userDevice;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.auth.AuthLibContext.AuthLib#toNextAuthState(com.amx.jax.ui.
	 * auth.AuthState)
	 */
	@Override
	public AuthState toNextAuthState(AuthState authState) {
		authState.cStep = getNextAuthStep(authState);
		return authState;
	}

	/**
	 * Check login step.
	 *
	 * @param authState the auth state
	 * @return the auth state. auth step
	 */
	private AuthState.AuthStep checkLoginStep(AuthState authState) {
		if (authState.cStep == null) {
			return AuthState.AuthStep.USERPASS;
		}
		switch (authState.cStep) {
		case DEVICEPASS:
			return AuthState.AuthStep.COMPLETED;
		case USERPASS:
			return AuthState.AuthStep.SECQUES;
		case USERPASS_SINGLE:
			return AuthState.AuthStep.COMPLETED;
		case SECQUES:
			return AuthState.AuthStep.COMPLETED;
		default:
			return AuthState.AuthStep.USERPASS;
		}
	}

	/**
	 * Check reset step.
	 *
	 * @param authState the auth state
	 * @return the auth state. auth step
	 */
	private AuthState.AuthStep checkResetStep(AuthState authState) {
		if (authState.cStep == null) {
			return AuthState.AuthStep.IDVALID;
		}
		switch (authState.cStep) {
		case IDVALID:
			return AuthState.AuthStep.MOTPVFY;
		case MOTPVFY:
			return AuthState.AuthStep.SECQUES;
		case SECQUES:
			return AuthState.AuthStep.CREDS_SET;
		case CREDS_SET:
			return AuthState.AuthStep.COMPLETED;
		default:
			return authState.cStep;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.ui.auth.AuthLibContext.AuthLib#getNextAuthStep(com.amx.jax.ui.
	 * auth.AuthState)
	 */
	public AuthState.AuthStep getNextAuthStep(AuthState authState) {
		if (authState.flow == AuthState.AuthFlow.LOGIN) {
			return checkLoginStep(authState);
		} else if (authState.flow == AuthState.AuthFlow.RESET_PASS) {
			return checkResetStep(authState);
		} else if (authState.flow == AuthState.AuthFlow.ACTIVATION) {
			return checkActivationStep(authState);
		} else if (authState.flow == AuthState.AuthFlow.REGISTRATION) {
			return checkRegStep(authState);
		} else if (authState.flow == AuthState.AuthFlow.PERMS) {
			return authState.cStep;
		}
		return authState.cStep;
	}

	/**
	 * Check reg step.
	 *
	 * @param authState the auth state
	 * @return the auth state. auth step
	 */
	private AuthState.AuthStep checkRegStep(AuthState authState) {
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
			return AuthState.AuthStep.CREDS_SET;
		case CAPTION_SET:
			return AuthState.AuthStep.CREDS_SET;
		case CREDS_SET:
			return AuthState.AuthStep.COMPLETED;
		default:
			return authState.cStep;
		}
	}

	/**
	 * Check activation step.
	 *
	 * @param authState the auth state
	 * @return the auth state. auth step
	 */
	private AuthState.AuthStep checkActivationStep(AuthState authState) {
		if (authState.cStep == null) {
			return AuthState.AuthStep.IDVALID;
		}
		switch (authState.cStep) {
		case IDVALID:
			return AuthState.AuthStep.MOTPVFY;
		case MOTPVFY:
			return AuthState.AuthStep.CREDS_SET;
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
	}

	@Override
	public CustomerFlags checkUserMeta(AuthState authState, CustomerFlags customerFlags) {
		AuthPermUtil.checkAnnualIncomeExpiry(authState, customerFlags);
		return customerFlags;
	}

	@Override
	public CustomerFlags checkModule(AuthState authState, CustomerFlags customerFlags, Features feature) {
		switch (feature) {
		case DASHBOARD:
			AuthPermUtil.checkEmailUpdate(authState, customerFlags);
			//AuthPermUtil.checkInsuranceUpdate(authState, customerFlags, userDevice.getUserDevice());
			break;
		case REMIT:
		case BENE_UPDATE:
		case FXORDER:
			AuthPermUtil.checkEmailUpdate(authState, customerFlags);
			AuthPermUtil.checkIdProofExpiry(authState, customerFlags);
			AuthPermUtil.checkSQASetup(authState, customerFlags);
			AuthPermUtil.checkSQA(authState, customerFlags);
			//AuthPermUtil.checkInsuranceUpdate(authState, customerFlags, userDevice.getUserDevice());
			break;
		case SQA_UPDATE:
			AuthPermUtil.checkEmailUpdate(authState, customerFlags);
			AuthPermUtil.checkSQASetup(authState, customerFlags);
			//AuthPermUtil.checkInsuranceUpdate(authState, customerFlags, userDevice.getUserDevice());
			break;
		default:
			AuthPermUtil.checkEmailUpdate(authState, customerFlags);
			//AuthPermUtil.checkInsuranceUpdate(authState, customerFlags, userDevice.getUserDevice());
			break;
		}

		return customerFlags;
	}

	public boolean hasFeature(AuthState authState, CustomerFlags customerFlags, Features feature) {
		switch (feature) {
		case INSURANCE:
			return customerFlags.getIsInsuranceActive();
		default:
			return true;
		}
	}

}
