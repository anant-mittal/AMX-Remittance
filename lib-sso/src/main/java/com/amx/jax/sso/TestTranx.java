package com.amx.jax.sso;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.cache.TransactionModel;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.sso.TestTranx.SSOModel;

@Component
public class TestTranx extends TransactionModel<SSOModel> {

	public static class SSOModel {
		
	}

	private Logger LOGGER = LoggerService.getLogger(TestTranx.class);

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
