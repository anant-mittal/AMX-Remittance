package com.amx.jax.postman.api;

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
import com.amx.jax.postman.service.PostManServiceImpl;
import com.bootloaderjs.JsonUtil;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class PostManController {

	@Autowired
	private PostManServiceImpl postManService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManController.class);

	@RequestMapping(value = PostManUrls.PROCESS_TEMPLATE, method = RequestMethod.POST)
	public File processTemplate(@RequestParam String template, @RequestParam(required = false) String data,
			@RequestParam(required = false) String fileName) {

		@SuppressWarnings("unchecked")
		File file = postManService.processTemplate(template, JsonUtil.fromJson(data, Map.class), fileName);
		return file;// file.getContent();

	}

	@RequestMapping(value = PostManUrls.SEND_SMS, method = RequestMethod.POST)
	public SMS sendSMS(@RequestBody SMS sms) throws UnirestException {
		postManService.sendSMS(sms);
		return sms;
	}

	@RequestMapping(value = PostManUrls.SEND_EMAIL, method = RequestMethod.POST)
	public Email sendEmail(@RequestBody Email email) {
		postManService.sendEmail(email);
		return email;
	}

	@RequestMapping(value = PostManUrls.NOTIFY_SLACK, method = RequestMethod.POST)
	public Message notifySlack(@RequestBody Message msg) throws UnirestException {
		postManService.notifySlack(msg);
		return msg;
	}

}
