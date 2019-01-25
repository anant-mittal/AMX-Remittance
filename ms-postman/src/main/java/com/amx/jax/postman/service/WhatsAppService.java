package com.amx.jax.postman.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.postman.model.WAMessage;
import com.amx.utils.ArgUtil;
import com.amx.utils.MapBuilder;

@Component
public class WhatsAppService {

	public static String WHATS_MESSAGES = "WHATS_MESSAGES";

	@Autowired
	RedissonClient redisson;

	@Autowired
	ApiWhaService apiWhaService;

	private RBlockingQueue<WAMessage> getQueue(BigDecimal queueId) {
		if (ArgUtil.isEmpty(queueId) || queueId.equals(BigDecimal.ZERO)) {
			return redisson.getBlockingQueue(WHATS_MESSAGES);
		}
		return redisson.getBlockingQueue(WHATS_MESSAGES + "_" + queueId);
	}

	@Async(ExecutorConfig.EXECUTER_DIAMOND)
	public WAMessage send(WAMessage message) {
		message.setId(AppContextUtil.getTraceId());
		if (WAMessage.Channel.APIWHA == message.getChannel()) {
			apiWhaService.sendWAMessage(message);
		} else {
			RBlockingQueue<WAMessage> queue = getQueue(BigDecimal.ZERO);
			queue.add(message);
		}
		return message;
	}

	@Async(ExecutorConfig.EXECUTER_DIAMOND)
	public WAMessage send(WAMessage message, BigDecimal queueId) {
		RBlockingQueue<WAMessage> queue = getQueue(queueId);
		queue.add(message);
		return message;
	}

	public WAMessage poll(BigDecimal queueId) throws InterruptedException {
		RBlockingQueue<WAMessage> queue = getQueue(queueId);
		WAMessage message = queue.poll(1, TimeUnit.MINUTES);
		if (message == null) {
			RBlockingQueue<WAMessage> queue2 = getQueue(queueId.add(BigDecimal.ONE));
			message = queue2.poll(2, TimeUnit.SECONDS);
		}
		return message == null ? new WAMessage() : message;
	}

	public Map<String, Object> status(BigDecimal queueId) throws InterruptedException {
		RBlockingQueue<WAMessage> queue = getQueue(queueId);
		return MapBuilder.map().put("qName", queue.getName()).put("qSize", queue.size())
				.put("nextMessage", queue.peek()).build();

	}
}
