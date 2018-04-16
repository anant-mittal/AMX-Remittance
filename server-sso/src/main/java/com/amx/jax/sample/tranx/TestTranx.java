package com.amx.jax.sample.tranx;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.cache.TransactionModel;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.model.Message;

@Component
public class TestTranx extends TransactionModel<Message> implements OTPAware {

	private Logger LOGGER = LoggerService.getLogger(TestTranx.class);

	public static enum Action {
		INIT, SET, COMMIT
	}

	@Override
	public Message init() {
		Message msg = new Message();
		this.save(msg);
		return msg;
	}

	public Message setMessage(String text) {
		Message msg = this.get();
		msg.setMessage(text);
		this.save(msg);
		return msg;
	}

	@Override
	public Message commit() {
		return this.get();
	}

	@Override
	public void sendOTP() {
		LOGGER.info("Trax ID={}", this.getTranxId());
	}

}
