package com.amx.jax.postman.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.amx.jax.logger.AuditService;
import com.amx.jax.postman.cache.ContactsCache;
import com.amx.jax.postman.model.TGMessage;
import com.amx.jax.tunnel.TunnelService;

@Component
public class TelegramService {

	@Autowired
	private ContactsCache contactsCache;

	private TelegramBot defaultTelegramBot;

	@Autowired
	private TunnelService tunnelService;

	@Autowired
	private AuditService auditService;

	@PostConstruct
	public void init() {
		ApiContextInitializer.init();
		TelegramBotsApi botsApi = new TelegramBotsApi();
		try {
			defaultTelegramBot = new TelegramBot().botUsername("almullabot")
					.botToken("1060793944:AAEqLlJK0tuHVw55bCtq4Ph-ZU59rLrUcEY")
					.contactsCache(contactsCache)
					.channel(TGMessage.Channel.DEFAULT)
					.set(tunnelService, auditService);
			botsApi.registerBot(defaultTelegramBot);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	public TGMessage send(TGMessage message) {
		for (String to : message.getTo()) {
			defaultTelegramBot.send(to, message.getMessage());
		}
		return message;
	}
}
