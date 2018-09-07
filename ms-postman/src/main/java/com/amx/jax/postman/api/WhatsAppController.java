package com.amx.jax.postman.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.service.WhatsAppService;

@RestController
public class WhatsAppController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WhatsAppController.class);

	@Autowired
	WhatsAppService whatsAppService;

	@RequestMapping(value = PostManUrls.WHATS_APP_SEND, method = RequestMethod.POST)
	public Message sendWhatsApp(@RequestBody Message msg) throws PostManException {
		whatsAppService.send(msg);
		return msg;
	}

	@RequestMapping(value = PostManUrls.WHATS_APP_SEND, method = RequestMethod.GET)
	public Message sendWhatsAppGet(@RequestParam String to, @RequestParam String message) throws PostManException {
		Message msg = new Message();
		msg.addTo(to);
		msg.setMessage(message);
		whatsAppService.send(msg);
		return msg;
	}

	@RequestMapping(value = PostManUrls.WHATS_APP_SEND, method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Message sendWhatsAppForm(@RequestParam String to, @RequestParam String message) throws PostManException {
		Message msg = new Message();
		msg.addTo(to);
		msg.setMessage(message);
		whatsAppService.send(msg);
		return msg;
	}

	@RequestMapping(value = PostManUrls.WHATS_APP_POLL, method = RequestMethod.GET)
	public Message pollWhatsApp() throws PostManException {
		return whatsAppService.poll();
	}

}
