package com.amx.jax.postman.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.cache.ContactsCache;
import com.amx.jax.postman.model.TGMessage;
import com.amx.utils.ArgUtil;

public class TelegramBot extends TelegramLongPollingBot {

	private static Logger LOGGER = LoggerService.getLogger(TelegramBot.class);

	public String botUsername;
	public String botToken;
	public ContactsCache contactsCache;
	private TGMessage.Channel channel;

	public TelegramBot botUsername(String botUsernamem) {
		this.botUsername = botUsernamem;
		return this;
	}

	public TelegramBot botToken(String botToken) {
		this.botToken = botToken;
		return this;
	}

	public TelegramBot contactsCache(ContactsCache contactsCache) {
		this.contactsCache = contactsCache;
		return this;
	}

	public TelegramBot channel(TGMessage.Channel channel) {
		this.channel = channel;
		return this;
	}

	@Override
	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			LOGGER.info("TG : {} {}", update.getMessage().getChatId(), update.getMessage().getText());

			SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(update.getMessage().getChatId())
					.setText("Reply:" + update.getMessage().getText());

			if (update.getMessage().getText().equals("/start")
					|| update.getMessage().getText().equals("/link")) {

				message.setText("You send /start");

				// create keyboard
				ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
				message.setReplyMarkup(replyKeyboardMarkup);
				replyKeyboardMarkup.setSelective(true);
				replyKeyboardMarkup.setResizeKeyboard(true);
				replyKeyboardMarkup.setOneTimeKeyboard(true);

				// new list
				List<KeyboardRow> keyboard = new ArrayList<>();

				// first keyboard line
				KeyboardRow keyboardFirstRow = new KeyboardRow();
				KeyboardButton keyboardButton = new KeyboardButton();
				keyboardButton.setText("Share your number >").setRequestContact(true);
				keyboardFirstRow.add(keyboardButton);
				// add array to list
				keyboard.add(keyboardFirstRow);
				// add list to our keyboard
				replyKeyboardMarkup.setKeyboard(keyboard);

			} else if (update.getMessage().getText().equals("/mycontact")) {
				message.setText(update.getMessage().getContact().getPhoneNumber());
			}

			try {
				execute(message); // Call method to send the message
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}

		if (ArgUtil.is(update.getMessage().getContact())) {
			LOGGER.info("TG : {} {}", update.getMessage().getChatId(), update.getMessage().getContact());
			contactsCache.put(channel.name() + "#" + update.getMessage().getContact().getPhoneNumber(),
					update.getMessage().getChatId());
		}
	}

	public void send(String phone, String text) {
		Long chatId = contactsCache.get(channel.name() + "#" + phone);
		SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
				.setChatId(chatId)
				.setText(text);
		try {
			execute(message); // Call method to send the message
		} catch (TelegramApiException e) {
			e.printStackTrace();
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