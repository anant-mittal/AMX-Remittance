package com.amx.jax.sso;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SSOUser implements Serializable {

	private static final long serialVersionUID = 5846957265338654300L;

	private boolean authDone = false;

	public boolean isAuthDone() {
		return authDone;
	}

	public void setAuthDone(boolean authDone) {
		this.authDone = authDone;
	}

}
