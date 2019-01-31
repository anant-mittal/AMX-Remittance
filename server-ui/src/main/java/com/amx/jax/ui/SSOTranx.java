package com.amx.jax.ui;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.TransactionModel;
import com.amx.jax.ui.SSOTranx.SSOModel;

@Component
public class SSOTranx extends TransactionModel<SSOModel> {

	public static class SSOModel implements Serializable {
		private static final long serialVersionUID = -2178734153442648084L;

	}

	@Override
	public SSOModel init() {
		return this.save(getDefault());
	}

	@Override
	public SSOModel getDefault() {
		SSOModel sSOModel = new SSOModel();
		return sSOModel;
	}

}
