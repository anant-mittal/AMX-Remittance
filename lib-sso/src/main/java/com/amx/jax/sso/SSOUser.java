package com.amx.jax.sso;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.utils.ArgUtil;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SSOUser implements Serializable {

	private static final long serialVersionUID = 5846957265338654300L;

	private boolean authDone = false;
	private String tranxId;

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
		String tranxId;
		if (!ArgUtil.isEmpty(this.getTranxId())) {
			tranxId = this.getTranxId();
			AppContextUtil.setTranxId(tranxId);
		} else {
			tranxId = AppContextUtil.getTranxId(true);
			this.setTranxId(tranxId);
		}
		return tranxId;
	}

}
