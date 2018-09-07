package com.amx.jax.postman.service;

import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.postman.model.Message;

@Component
public class WhatsAppService {

	public static String WHATS_MESSAGES = "WHATS_MESSAGES";

	@Autowired
	RedissonClient redisson;

	@Async(ExecutorConfig.EXECUTER_DIAMOND)
	public Message send(Message message) {
		RQueue<Message> queue = redisson.getQueue(WHATS_MESSAGES);
		queue.add(message);
		return message;
	}

	public Message poll() {
		RQueue<Message> queue = redisson.getQueue(WHATS_MESSAGES);
		Message message = queue.poll();
		return message == null ? new Message() : message;
	}
}
