package com.amx.jax.constant;

import com.amx.jax.notification.alert.ApplicationCreationFailureAlert;
import com.amx.jax.notification.alert.BankBranchSearchFailureAlert;
import com.amx.jax.notification.alert.FingerPrintLoginIncorrectAttempt;
import com.amx.jax.notification.alert.IAlert;
import com.amx.jax.notification.alert.RemittanceCreationFailureAlert;

/**
 * @author Prashant - Represents api flow defination
 */
public enum JaxEvent {

	CREATE_APPLICATION(ApplicationCreationFailureAlert.class), CREATE_REMITTANCE(RemittanceCreationFailureAlert.class),
	BANK_BRANCH_SEARCH(BankBranchSearchFailureAlert.class), FINGERPRINT_LOGIN_INCORRECT_ATTEMPT(FingerPrintLoginIncorrectAttempt.class),
	LOGIN;

	Class<? extends IAlert> alertBean;

	private JaxEvent(Class<? extends IAlert> alertBean) {
		this.alertBean = alertBean;
	}
	
	private JaxEvent() {

	}

	public Class<? extends IAlert> getAlertBean() {
		return alertBean;
	}

	public void setAlertBean(Class<? extends IAlert> alertBean) {
		this.alertBean = alertBean;
	}

}
