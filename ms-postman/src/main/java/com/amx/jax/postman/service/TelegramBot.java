package com.amx.jax.postman.service;

import org.slf4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.amx.jax.logger.LoggerService;

public class TelegramBot extends TelegramLongPollingBot {

	private static Logger LOGGER = LoggerService.getLogger(TelegramBot.class);

	public String botUsername;
	public String botToken;

	public TelegramBot(String botUsernamem, String botToken) {
		super();
		this.botUsername = botUsernamem;
		this.botToken = botToken;
	}

	@Override
	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			LOGGER.info("TG : {} {}", update.getMessage().getChatId(), update.getMessage().getText());
			SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(update.getMessage().getChatId())
					.setText("Reply:" + update.getMessage().getText());
			try {
				execute(message); // Call method to send the message
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getBotUsername() {
		return botUsername;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}
}