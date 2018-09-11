package com.amx.jax.postman.api;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.audit.PMGaugeEvent;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.Message.Status;
import com.amx.jax.postman.service.WhatsAppService;
import com.amx.utils.ArgUtil;

@RestController
public class WhatsAppController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WhatsAppController.class);
	private static final long MESSAGE_TIMEOUT = 10 * 60 * 1000;

	@Autowired
	WhatsAppService whatsAppService;

	@Autowired
	AuditService auditService;

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
	public Message pollWhatsApp(BigDecimal q) throws PostManException, InterruptedException {
		return whatsAppService.poll(ArgUtil.parseAsBigDecimal(q, BigDecimal.ZERO));
	}

	@RequestMapping(value = PostManUrls.WHATS_APP_STATS, method = RequestMethod.GET)
	public Map<String, Object> statsWhatsApp(BigDecimal q) throws PostManException, InterruptedException {
		return whatsAppService.status(ArgUtil.parseAsBigDecimal(q, BigDecimal.ZERO));
	}

	@RequestMapping(value = PostManUrls.WHATS_APP_RESEND, method = RequestMethod.POST)
	public Message resendWhatsApp(@RequestBody Message msg, @RequestParam BigDecimal q) throws PostManException {
		long ageOfMessage = System.currentTimeMillis() - msg.getTimestamp();

		if (ageOfMessage < MESSAGE_TIMEOUT) {
			whatsAppService.send(msg, ArgUtil.parseAsBigDecimal(q, BigDecimal.ZERO));
		} else {
			msg.setStatus(Status.FAILED);
			return statusWhatsApp(msg, "TIMEOUT");
		}
		return msg;
	}

	@RequestMapping(value = PostManUrls.WHATS_APP_STATUS, method = RequestMethod.POST)
	public Message statusWhatsApp(@RequestBody Message msg, @RequestParam(required = false) String reason)
			throws PostManException {
		PMGaugeEvent pMGaugeEvent = new PMGaugeEvent(PMGaugeEvent.Type.SEND_WHATSAPP);
		pMGaugeEvent.setTo(msg.getTo());
		pMGaugeEvent.setMessage(msg.getMessage());
		if (msg.getStatus() == Message.Status.SENT) {
			pMGaugeEvent.setResult(Result.DONE);
		} else {
			pMGaugeEvent.setResponseText(reason);
			pMGaugeEvent.setResult(Result.FAIL);
		}
		auditService.log(pMGaugeEvent);
		return msg;
	}

}
