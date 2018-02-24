package com.amx.jax.postman.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Langs;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.postman.service.PostManServiceImpl;
import com.bootloaderjs.ArgUtil;
import com.bootloaderjs.JsonUtil;

@RestController
public class PostManController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManController.class);

	@Autowired
	private PostManServiceImpl postManService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private Langs defaultLang;

	public Langs getLang() {
		String langString = request.getParameter(PostManServiceImpl.PARAM_LANG);// localeResolver.resolveLocale(request).toString();
		Langs lang = (Langs) ArgUtil.parseAsEnum(langString, defaultLang);
		return lang;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = PostManUrls.PROCESS_TEMPLATE, method = RequestMethod.POST)
	public File processTemplate(@RequestParam Templates template, @RequestParam(required = false) String data,
			@RequestParam(required = false) String fileName, @RequestParam(required = false) File.Type fileType) {

		Langs lang = getLang();
		File file = new File();
		file.setLang(lang);

		try {
			file.setTemplate(template);
			file.setType(fileType);
			file.setModel(JsonUtil.fromJson(data, Map.class));
			file = postManService.processTemplate(file);
		} catch (Exception e) {
			LOGGER.error(" Template Error {}", data, e);
		}

		LOGGER.info(" FILE {} : {} . {}", template, fileName, fileType);

		return file;
	}

	@RequestMapping(value = PostManUrls.SEND_SMS, method = RequestMethod.POST)
	public SMS sendSMS(@RequestBody SMS sms, @RequestParam(required = false, defaultValue = "false") Boolean async)
			throws PostManException {

		Langs lang = getLang();

		if (sms.getLang() == null) {
			sms.setLang(lang);
		}

		if (async == true) {
			postManService.sendSMSAsync(sms);
		} else {
			postManService.sendSMS(sms);
		}
		return sms;
	}

	@RequestMapping(value = PostManUrls.SEND_EMAIL, method = RequestMethod.POST)
	public Email sendEmail(@RequestBody Email email,
			@RequestParam(required = false, defaultValue = "false") Boolean async) throws PostManException {

		Langs lang = getLang();

		if (email.getLang() == null) {
			email.setLang(lang);
		}

		if (async == true) {
			postManService.sendEmailAsync(email);
		} else {
			postManService.sendEmail(email);
		}
		return email;
	}

	@RequestMapping(value = PostManUrls.NOTIFY_SLACK, method = RequestMethod.POST)
	public Message notifySlack(@RequestBody Notipy msg) throws PostManException {
		postManService.notifySlack(msg);
		return msg;
	}

	@RequestMapping(value = PostManUrls.NOTIFY_SLACK_EXCEP, method = RequestMethod.POST)
	public Exception notifySlack(@RequestBody Exception eMsg, @RequestParam(required = false) String title)
			throws PostManException {
		postManService.notifyException(title, eMsg);
		return eMsg;
	}
}
