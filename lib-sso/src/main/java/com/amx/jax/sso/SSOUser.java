package com.amx.jax.sso;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.utils.ArgUtil;
import com.amx.utils.Random;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SSOUser implements Serializable {

	private static final long serialVersionUID = 5846957265338654300L;

	private boolean authDone = false;
	private String tranxId;
	private EmployeeDetailsDTO userDetails = null;

	private String selfSAC;
	private String partnerSAC;

	public boolean isAuthDone() {
		return authDone;
	}

	public void setAuthDone(boolean authDone) {
		this.authDone = authDone;
	}

	public String getTranxId() {
		return tranxId;
	}

	public void setTranxId(String tranxId) {
		this.tranxId = tranxId;
	}

	public String ssoTranxId() {
		String tranxId = AppContextUtil.getTranxId();
		if (ArgUtil.isEmpty(tranxId) && !ArgUtil.isEmpty(this.getTranxId())) {
			tranxId = this.getTranxId();
			AppContextUtil.setTranxId(tranxId);
		} else {
			tranxId = AppContextUtil.getTranxId(true);
			this.setTranxId(tranxId);
		}
		return tranxId;
	}

	public void setUserDetails(EmployeeDetailsDTO userDetails) {
		this.userDetails = userDetails;
	}

	public EmployeeDetailsDTO getUserDetails() {
		return userDetails;
	}

	public String getSelfSAC() {
		return selfSAC;
	}

	public void setSelfSAC(String selfSAC) {
		this.selfSAC = selfSAC;
	}

	public String getPartnerSAC() {
		return partnerSAC;
	}

	public void setPartnerSAC(String partnerSAC) {
		this.partnerSAC = partnerSAC;
	}

	public void generateSAC() {
		this.selfSAC = Random.randomAlpha(3);
		this.partnerSAC = Random.randomAlpha(3);
	}

}
