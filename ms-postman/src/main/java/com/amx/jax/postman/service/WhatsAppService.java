package com.amx.jax.postman.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.postman.model.Message;
import com.amx.utils.ArgUtil;
import com.amx.utils.MapBuilder;

@Component
public class WhatsAppService {

	public static String WHATS_MESSAGES = "WHATS_MESSAGES";

	@Autowired
	RedissonClient redisson;

	private RBlockingQueue<Message> getQueue(BigDecimal queueId) {
		if (ArgUtil.isEmpty(queueId) || queueId.equals(BigDecimal.ZERO)) {
			return redisson.getBlockingQueue(WHATS_MESSAGES);
		}
		return redisson.getBlockingQueue(WHATS_MESSAGES + "_" + queueId);
	}

	@Async(ExecutorConfig.EXECUTER_DIAMOND)
	public Message send(Message message) {
		RBlockingQueue<Message> queue = getQueue(BigDecimal.ZERO);
		queue.add(message);
		return message;
	}

	@Async(ExecutorConfig.EXECUTER_DIAMOND)
	public Message send(Message message, BigDecimal queueId) {
		RBlockingQueue<Message> queue = getQueue(queueId);
		queue.add(message);
		return message;
	}

	public Message poll(BigDecimal queueId) throws InterruptedException {
		RBlockingQueue<Message> queue = getQueue(queueId);
		Message message = queue.poll(1, TimeUnit.MINUTES);
		if (message == null) {
			RBlockingQueue<Message> queue2 = getQueue(queueId.add(BigDecimal.ONE));
			message = queue2.poll(2, TimeUnit.SECONDS);
		}
		return message == null ? new Message() : message;
	}

	public Map<String, Object> status(BigDecimal queueId) throws InterruptedException {
		RBlockingQueue<Message> queue = getQueue(queueId);
		return MapBuilder.map().put("qName", queue.getName()).put("qSize", queue.size())
				.put("nextMessage", queue.peek()).build();

	}
}
