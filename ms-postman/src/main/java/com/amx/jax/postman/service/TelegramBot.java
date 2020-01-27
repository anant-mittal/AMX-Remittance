package com.amx.jax.postman.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.AuditActor;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.audit.PMGaugeEvent;
import com.amx.jax.postman.cache.ContactsCache;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.TGMessage;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.ArgUtil;
import com.amx.utils.CollectionUtil;
import com.amx.utils.UniqueID;

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

	@Autowired
	private TunnelService tunnelService;

	@Autowired
	private AuditService auditService;

	public TelegramBot set(TunnelService tunnelService, AuditService auditService) {
		this.tunnelService = tunnelService;
		this.auditService = auditService;
		return this;
	}

	@Override
	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text
		if (update.hasMessage() && update.getMessage().hasText()) {
			LOGGER.info("TG TEXT: {} {}", update.getMessage().getChatId(), update.getMessage().getText());

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
				executeWithCatch(message);
			} else if (update.getMessage().getText().equals("/mycontact")) {
				message.setText(update.getMessage().getContact().getPhoneNumber());
				executeWithCatch(message);
			} else {
				onMessageLocal(update);
			}

		} else {
			LOGGER.info("TG NOTEXT : {}", update.getMessage().getChatId());
		}
		if (ArgUtil.is(update.getMessage().getContact())) {
			if (ArgUtil.is(update.getMessage().getFrom().getId())) {
				update.getMessage().getFrom().getId().equals(update.getMessage().getContact().getUserID());
				LOGGER.info("TG CONTACT : {}", update.getMessage().getChatId());
				String phoneKey = channel.name() + "#" + update.getMessage().getContact().getPhoneNumber();
				// Cache ChatId against PhoneNumber
				contactsCache.put(phoneKey,
						ArgUtil.parseAsString(update.getMessage().getChatId()));
				// Cache PhoneNumber against userid
				contactsCache.put("_" + update.getMessage().getFrom().getId(),
						update.getMessage().getContact().getPhoneNumber());
				send(phoneKey, "You have successfully subscribed to updates");
			}
		}
	}

	private void onMessageLocal(Update update) {
		String sessionId = AppContextUtil.getSessionId(false, UniqueID.generateSessionId());
		AppContextUtil.setSessionId(sessionId);
		AppContextUtil.getTraceId(true, true);
		AppContextUtil.resetTraceTime();
		AppContextUtil.init();
		PMGaugeEvent pMGaugeEvent = new PMGaugeEvent(PMGaugeEvent.Type.ON_TG);
		try {

			String from = contactsCache.get("_" + update.getMessage().getFrom().getId());

			UserInboxEvent userInboxEvent = new UserInboxEvent();
			userInboxEvent.setWaChannel(WAMessage.Channel.DEFAULT);
			userInboxEvent.setQueue(BigDecimal.ZERO);
			userInboxEvent.setFrom(from);
			userInboxEvent.setMessage(update.getMessage().getText());

			AppContextUtil.setActorId(new AuditActor(AuditActor.ActorType.W, from));
			pMGaugeEvent.setTo(CollectionUtil.getList(userInboxEvent.getFrom()));
			pMGaugeEvent.setMessage(userInboxEvent.getMessage());
			pMGaugeEvent.setResult(Result.DONE);

			tunnelService.task(userInboxEvent);
			auditService.gauge(pMGaugeEvent.set(Result.DONE));

		} catch (Exception e) {
			auditService.excep(pMGaugeEvent.set(Result.DONE), e);
		} finally {
			AppContextUtil.clear();
		}
	}

	public void send(String phone, String text) {
		String chatId = contactsCache.get(channel.name() + "#" + phone);
		if (ArgUtil.is(chatId)) {
			SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
					.setChatId(chatId)
					.setText(text);
			executeWithCatch(message);
		}
	}

	private void executeWithCatch(SendMessage message) {
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