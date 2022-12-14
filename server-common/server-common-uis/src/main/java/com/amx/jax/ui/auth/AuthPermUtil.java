package com.amx.jax.ui.auth;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.AuthState;
import com.amx.jax.model.UserDevice;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.config.UIServerError;
import com.amx.utils.ArgUtil;

public class AuthPermUtil {

	public static void checkIdProofExpiryRequired(AuthState authState, CustomerFlags customerFlags) {
		if (ArgUtil.nullAsFalse(customerFlags.getIdProofRequired())) {
			authState.setnStep(AuthState.AuthStep.PROFILE_UPDATE);
			throw new GlobalException(JaxError.EKYC_REQUIRED, "You KYC has expired, kindly Upload again");
		}
	}

	public static void checkIdProofExpiry(AuthState authState, CustomerFlags customerFlags) {
		if (ArgUtil.nullAsFalse(customerFlags.getIdProofRequired())) {
			authState.setnStep(AuthState.AuthStep.PROFILE_UPDATE);
			throw new GlobalException(JaxError.KYC_EXPIRED, "You KYC has expired, kindly Visit branch");
		}
	}

	public static void checkIdProofVerified(AuthState authState, CustomerFlags customerFlags) {
		if (ArgUtil.nullAsFalse(customerFlags.getIdProofVerificationPending())) {
			authState.setnStep(AuthState.AuthStep.PROFILE_UPDATE);
			throw new GlobalException(JaxError.KYC_VERIFICATION_PENDING, "You KYC verification is Pending");
		}
	}

	public static void checkAnnualIncomeExpiry(AuthState authState, CustomerFlags customerFlags) {
		if (ArgUtil.nullAsFalse(customerFlags.getAnnualIncomeExpired())) {
			authState.setnStep(AuthState.AuthStep.PROFILE_UPDATE);
			throw new GlobalException(JaxError.INCOME_UPDATE_REQUIRED, "Kindly update Annual Income");
		}
	}

	public static void checkSQA(AuthState authState, CustomerFlags customerFlags) {
		if (!authState.isValidSecQues()) {
			authState.setnStep(AuthState.AuthStep.SECQUES);
			throw new GlobalException(JaxError.SQA_REQUIRED, "Security Answer is required");
		}
	}

	public static void checkSQASetup(AuthState authState, CustomerFlags customerFlags) {
		if (!ArgUtil.nullAsFalse(customerFlags.getSecurityQuestionDone())) {
			authState.setnStep(AuthState.AuthStep.SECQ_SET);
			throw new GlobalException(JaxError.SQA_SETUP_REQUIRED, "Security QA setup is required");
		}
	}

	public static void checkEmailUpdate(AuthState authState, CustomerFlags customerFlags) {
		if (ArgUtil.nullAsFalse(customerFlags.getIsEmailMissing())) {
			authState.setnStep(AuthState.AuthStep.PROFILE_UPDATE);
			throw new UIServerError(OWAStatusStatusCodes.REDIRECT_MODULE, "Email update is required")
					.redirectUrl("/app/myaccount/contact?tab=email");
		}
	}

	public static void checkMobileUpdate(AuthState authState, CustomerFlags customerFlags) {
		if (ArgUtil.nullAsFalse(customerFlags.getIsMobileMissing())) {
			authState.setnStep(AuthState.AuthStep.PROFILE_UPDATE);
			throw new UIServerError(OWAStatusStatusCodes.REDIRECT_MODULE, "Mobile update is required")
					.redirectUrl("/app/myaccount/contact?tab=mobile");
		}
	}

	public static void checkInsuranceUpdate(AuthState authState, CustomerFlags customerFlags, UserDevice userDevice) {
		if (ArgUtil.isEmpty(userDevice) || !userDevice.isMobile()) {
			authState.setnStep(AuthState.AuthStep.PROFILE_UPDATE);
			if (ArgUtil.nullAsFalse(customerFlags.getIsForceUpdateInsuranceRequired())) {
				throw new UIServerError(OWAStatusStatusCodes.REDIRECT_MODULE, "Insurance update is required")
						.redirectUrl("/app/myaccount/insurance?update=required");
			}
		}
	}

}
