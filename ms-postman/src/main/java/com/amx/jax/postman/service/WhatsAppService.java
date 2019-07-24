package com.amx.jax.postman.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.amx.jax.AppContextUtil;
import com.amx.jax.api.ListRequestModel;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.dict.ContactType;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.postman.PostManConfig;
import com.amx.jax.postman.audit.PMGaugeEvent;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.ArgUtil;
import com.amx.utils.CollectionUtil;
import com.amx.utils.Constants;
import com.amx.utils.MapBuilder;

@Component
public class WhatsAppService {

	public static String WHATS_MESSAGES = "WHATS_MESSAGES";

	@Autowired
	RedissonClient redisson;

	@Autowired
	ApiWhaService apiWhaService;

	@Autowired
	private TunnelService tunnelService;

	@Autowired
	private PostManConfig postManConfig;

	@Autowired
	private FileService fileService;

	@Autowired
	private AuditService auditService;

	private RBlockingQueue<WAMessage> getQueue(BigDecimal queueId) {
		if (ArgUtil.isEmpty(queueId) || queueId.equals(BigDecimal.ZERO)) {
			return redisson.getBlockingQueue(WHATS_MESSAGES + "_" + AppContextUtil.getTenant());
		}
		return redisson.getBlockingQueue(WHATS_MESSAGES + "_" + queueId + "_" + AppContextUtil.getTenant());
	}

	public WAMessage resolveTemplate(WAMessage waMessage) {
		if (ArgUtil.isEmpty(waMessage.getMessage()) && waMessage.getTemplate() != null) {
			Context context = new Context(postManConfig.getLocal(waMessage));
			context.setVariables(waMessage.getModel());

			File file = new File();
			file.setTemplate(waMessage.getTemplate());
			file.setModel(waMessage.getModel());
			file.setLang(waMessage.getLang());

			waMessage.setMessage(fileService.create(file, ContactType.WHATSAPP).getContent());
		}
		return waMessage;
	}

	@Async(ExecutorConfig.EXECUTER_DIAMOND)
	public WAMessage send(WAMessage message) {
		message.setId(AppContextUtil.getTraceId());
		message = resolveTemplate(message);

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
		message = resolveTemplate(message);
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

	@Async(ExecutorConfig.EXECUTER_DIAMOND)
	public void onMessage(ListRequestModel<Map<String, String>> data, BigDecimal queueId) {
		List<Map<String, String>> messages = data.getValues();
		for (Map<String, String> map : messages) {
			UserInboxEvent userInboxEvent = new UserInboxEvent();
			userInboxEvent.setWaChannel(WAMessage.Channel.DEFAULT);
			userInboxEvent.setQueue(queueId);
			userInboxEvent.setFrom(ArgUtil.parseAsString(map.get("from"), Constants.BLANK));
			userInboxEvent.setTo(ArgUtil.parseAsString(map.get("to"), Constants.BLANK));
			userInboxEvent.setMessage(ArgUtil.parseAsString(map.get("text"), Constants.BLANK));

			PMGaugeEvent pMGaugeEvent = new PMGaugeEvent(PMGaugeEvent.Type.ON_WHATSAPP);
			pMGaugeEvent.setTo(CollectionUtil.getList(userInboxEvent.getFrom()));
			pMGaugeEvent.setMessage(userInboxEvent.getMessage());
			pMGaugeEvent.setResult(Result.DONE);

			try {
				tunnelService.task(userInboxEvent);
				auditService.gauge(pMGaugeEvent.set(Result.DONE));
			} catch (Exception e) {
				auditService.excep(pMGaugeEvent.set(Result.DONE), e);
			}

		}
	}
}
