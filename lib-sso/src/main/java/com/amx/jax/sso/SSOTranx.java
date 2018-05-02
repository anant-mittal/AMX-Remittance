package com.amx.jax.sso;

import java.io.Serializable;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.cache.TransactionModel;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.sso.SSOTranx.SSOModel;

@Component
public class SSOTranx extends TransactionModel<SSOModel> {

	private Logger LOGGER = LoggerService.getLogger(SSOTranx.class);

	public static class SSOModel implements Serializable {
		private static final long serialVersionUID = -2178734153442648084L;

	}

	public static enum Action {
		INIT, SET, COMMIT
	}

	@Override
	public SSOModel init() {
		SSOModel msg = new SSOModel();
		this.save(msg);
		return msg;
	}

	public SSOModel setMessage(String text) {
		SSOModel msg = this.get();
		this.save(msg);
		return msg;
	}

	@Override
	public SSOModel commit() {
		return this.get();
	}

	public void sendOTP() {
		LOGGER.info("Trax ID={}", this.getTranxId());
	}

}
