package com.amx.jax.postman.api;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.postman.service.PostManServiceImpl;
import com.bootloaderjs.JsonUtil;
import com.mashape.unirest.http.exceptions.UnirestException;

@RestController
public class PostManController {

	@Autowired
	private PostManServiceImpl postManService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManController.class);

	@SuppressWarnings("unchecked")
	@RequestMapping(value = PostManUrls.PROCESS_TEMPLATE, method = RequestMethod.POST)
	public File processTemplate(@RequestParam Templates template, @RequestParam(required = false) String data,
			@RequestParam(required = false) String fileName, @RequestParam(required = false) File.Type fileType) {

		File file = null;
		try {
			file = postManService.processTemplate(template, JsonUtil.fromJson(data, Map.class), fileType);
		} catch (Exception e) {
			LOGGER.error(" Template Error {}", data, e);
		}

		LOGGER.info(" FILE {} : {} . {}", template, fileName, fileType);

		return file;
	}

	@RequestMapping(value = PostManUrls.SEND_SMS, method = RequestMethod.POST)
	public SMS sendSMS(@RequestBody SMS sms) throws UnirestException {
		postManService.sendSMS(sms);
		return sms;
	}

	@RequestMapping(value = PostManUrls.SEND_EMAIL, method = RequestMethod.POST)
	public Email sendEmail(@RequestBody Email email) throws UnirestException {
		postManService.sendEmail(email);
		return email;
	}

	@RequestMapping(value = PostManUrls.NOTIFY_SLACK, method = RequestMethod.POST)
	public Message notifySlack(@RequestBody Message msg) throws UnirestException {
		postManService.notifySlack(msg);
		return msg;
	}

	@RequestMapping(value = PostManUrls.NOTIFY_SLACK_EXCEP, method = RequestMethod.POST)
	public Exception notifySlack(@RequestBody Exception eMsg, @RequestParam(required = false) String title)
			throws UnirestException {
		postManService.notifyException(title, eMsg);
		return eMsg;
	}
}
