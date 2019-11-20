package com.amx.jax.postman.service;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.PostManConfig;
import com.amx.jax.postman.audit.PMGaugeEvent;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.rest.RestService;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

@Component
public class ApiWhaService {

	private static Logger LOGGER = LoggerService.getLogger(ApiWhaService.class);

	@Value("${apiwha.api.key}")
	String apiWhaKey;

	@Autowired
	private RestService restService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private FileService fileService;
	@Autowired
	private TunnelService tunnelService;

	@Autowired
	private PostManConfig postManConfig;

	public WAMessage sendWAMessage(WAMessage message) {

		String to = null;
		PMGaugeEvent pMGaugeEvent = new PMGaugeEvent(PMGaugeEvent.Type.SEND_WHATSAPP);
		try {
			to = message.getTo() != null ? message.getTo().get(0) : null;
			if (message.getTemplate() != null) {
				Context context = new Context(postManConfig.getLocal(message));
				context.setVariables(message.getModel());
				File file = new File();
				file.setTemplate(message.getTemplate());
				file.setModel(message.getModel());
				file.setLang(message.getLang());
				message.setMessage(fileService.create(file).getContent());
			}

			if (ArgUtil.isEmpty(to)) {
				auditService.gauge(pMGaugeEvent.set(message).result(Result.REJECTED));
			} else {
				String responseText = restService.ajax("http://panel.apiwha.com/send_message.php")
						.field("apikey", apiWhaKey).field("number",
								message.getTo())
						.postForm().asString();
				auditService.gauge(pMGaugeEvent.responseText(responseText).set(message));
			}
		} catch (Exception e) {
			auditService.excep(pMGaugeEvent.set(message).result(Result.ERROR), e);
		}
		return message;
	}

	public void onMessage(Map<String, Object> dataMap) {
		String event = ArgUtil.parseAsString(dataMap.get("event"), Constants.BLANK);
		if ("INBOX".equals(event)) {
			UserInboxEvent userInboxEvent = new UserInboxEvent();
			userInboxEvent.setWaChannel(WAMessage.Channel.APIWHA);
			userInboxEvent.setFrom(ArgUtil.parseAsString(dataMap.get("from"), Constants.BLANK));
			userInboxEvent.setTo(ArgUtil.parseAsString(dataMap.get("to"), Constants.BLANK));
			userInboxEvent.setMessage(ArgUtil.parseAsString(dataMap.get("text"), Constants.BLANK));
			tunnelService.task(userInboxEvent);
		}
	}

}
