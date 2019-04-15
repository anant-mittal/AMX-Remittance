package com.amx.jax.ui.auth;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.AuthState;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.utils.ArgUtil;

public class AuthPermUtil {

	public static void checkIdProofExpiryRequired(AuthState authState, CustomerFlags customerFlags) {
		if (ArgUtil.nullAsFalse(customerFlags.getIdProofRequired())) {
			throw new GlobalException(JaxError.EKYC_REQUIRED, "You KYC has expired, kindly Upload again");
		}
	}

	public static void checkIdProofExpiry(AuthState authState, CustomerFlags customerFlags) {
		if (ArgUtil.nullAsFalse(customerFlags.getIdProofRequired())) {
			throw new GlobalException(JaxError.KYC_EXPIRED, "You KYC has expired, kindly Visit branch");
		}
	}

	public static void checkIdProofVerified(AuthState authState, CustomerFlags customerFlags) {
		if (ArgUtil.nullAsFalse(customerFlags.getIdProofVerificationPending())) {
			throw new GlobalException(JaxError.KYC_VERIFICATION_PENDING, "You KYC verification is Pending");
		}
	}

	public static void checkAnnualIncomeExpiry(AuthState authState, CustomerFlags customerFlags) {
		if (ArgUtil.nullAsFalse(customerFlags.getAnnualIncomeExpired())) {
			throw new GlobalException(JaxError.INCOME_UPDATE_REQUIRED, "Kindly update Annual Income");
		}
	}

	public static void checkSQA(AuthState authState, CustomerFlags customerFlags, Object meta) {
		if (!authState.isValidSecQues()) {
			GlobalException globalException = new GlobalException(JaxError.SQA_REQUIRED, "Sec QA setup is required");
			globalException.setMeta(meta);
			throw globalException;
		}
	}

	public static void checkSQASetup(AuthState authState, CustomerFlags customerFlags) {
		if (!ArgUtil.nullAsFalse(customerFlags.getSecurityQuestionDone())) {
			throw new GlobalException(JaxError.SQA_SETUP_REQUIRED, "Sec QA setup is required");
		}
	}

}
