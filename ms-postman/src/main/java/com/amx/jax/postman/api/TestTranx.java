package com.amx.jax.postman.api;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.TransactionModel;
import com.amx.jax.postman.model.Message;

@Component
public class TestTranx extends TransactionModel<Message> {

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

}
