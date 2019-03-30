package com.amx.jax.radar.snap;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.bot.InBoxListener;
import com.amx.jax.dict.ContactType;
import com.amx.jax.postman.events.UserInboxEvent;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.radar.jobs.customer.OracleViewDocument;
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
	public WAMessage charResponse(@RequestBody UserInboxEvent event) throws NumberParseException {
		return inBoxListener.onMessageResponse(event);
	}

	@ResponseBody
	@RequestMapping(value = "snap/api/chat/response/send", method = RequestMethod.POST)
	public WAMessage charReply(@RequestBody UserInboxEvent event) {
		return inBoxListener.onMessageReply(event);
	}

	@ResponseBody
	@RequestMapping(value = "snap/api/verifylink/send", method = RequestMethod.POST)
	public OracleViewDocument charReply(@RequestParam ContactType type, @RequestParam BigDecimal customerId) {
		return snapDocumentRepository.createCustomerVerificationLink(type, customerId);
	}

}
