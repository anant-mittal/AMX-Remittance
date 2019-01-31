package com.amx.jax.ui;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.TransactionModel;
import com.amx.jax.ui.SSOTranx2.SSOModel2;

@Component
public class SSOTranx2 extends TransactionModel<SSOModel2> {

	public static class SSOModel2 implements Serializable {
		private static final long serialVersionUID = -2178734153442648084L;

	}

	@Override
	public SSOModel2 init() {
		return this.save(getDefault());
	}

	@Override
	public SSOModel2 getDefault() {
		SSOModel2 sSOModel = new SSOModel2();
		return sSOModel;
	}

}
