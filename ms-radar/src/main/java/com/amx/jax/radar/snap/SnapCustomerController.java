package com.amx.jax.radar.snap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.bot.InBoxListener;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.radar.service.SnapDocumentRepository;
import com.google.i18n.phonenumbers.NumberParseException;

@Controller
public class SnapCustomerController {

	@Autowired
	InBoxListener inBoxListener;

	@Autowired
	SnapDocumentRepository snapDocumentRepository;

	@ResponseBody
	@RequestMapping(value = "snap/api/chat/response/get", method = RequestMethod.POST)
	public Message charResponse(@RequestBody UserInboxEvent event) throws NumberParseException {
		return inBoxListener.onMessageResponse(event);
	}

	@ResponseBody
	@RequestMapping(value = "snap/api/chat/response/send", method = RequestMethod.POST)
	public Message charReply(@RequestBody UserInboxEvent event) {
		return inBoxListener.onMessageReply(event);
	}

}
