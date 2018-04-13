package com.amx.jax.postman.api;

import java.util.HashMap;
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

import com.amx.jax.dict.Language;
import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.PostManConfig;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.SupportEmail;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.postman.service.PostManServiceImpl;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

@RestController
public class PostManController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManController.class);

	@Autowired
	private PostManServiceImpl postManService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PostManConfig postManConfig;

	public Language getLang() {
		String langString = request.getParameter(PostManServiceImpl.PARAM_LANG);// localeResolver.resolveLocale(request).toString();
		Language lang = (Language) ArgUtil.parseAsEnum(langString, postManConfig.getTenantLang());
		return lang;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = PostManUrls.PROCESS_TEMPLATE, method = RequestMethod.POST)
	public File processTemplate(@RequestParam Templates template, @RequestParam(required = false) String data,
			@RequestParam(required = false) String fileName, @RequestParam(required = false) File.Type fileType) {

		Language lang = getLang();
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

		Language lang = getLang();

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

	/**
	 * @param tnt
	 * @param lang
	 * @param to
	 * @param customer
	 * @param amount
	 * @param amount
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = PostManUrls.SEND_EMAIL, method = RequestMethod.GET)
	public Map<String, Object> sendEmailGet(@RequestParam Tenant tnt, @RequestParam(required = false) Language language,
			@RequestParam String to, @RequestParam String customer, @RequestParam String amount,
			@RequestParam String loyaltypoints, @RequestParam String refno, @RequestParam String date,
			@RequestParam(required = false) String languageid, @RequestParam Templates template)
			throws PostManException {
		Email email = new Email();

		if ("2".equals(languageid)) {
			email.setLang(Language.AR);
		} else {
			email.setLang(Language.EN);
		}

		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", to);
		modeldata.put("customer", customer);
		modeldata.put("amount", amount);
		modeldata.put("loyaltypoints", loyaltypoints);
		modeldata.put("refno", refno);
		modeldata.put("date", date);
		modeldata.put("languageid", languageid);

		email.setModel(modeldata);
		email.addTo(to);

		email.setTemplate(template);
		postManService.sendEmailAsync(email);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> data = new HashMap<String, String>();
		data.put("msg", String.format("Email is send to %s and Ref NO. is %s", customer, refno));
		map.put("data", data);
		map.put("error", null);
		map.put("meta", null);
		map.put("responseCode", "SUCCESS");
		map.put("responseMessage", "Email is successfully sent.");
		return map;
	}

	@RequestMapping(value = PostManUrls.SEND_EMAIL, method = RequestMethod.POST)
	public Email sendEmail(@RequestBody Email email,
			@RequestParam(required = false, defaultValue = "false") Boolean async) throws PostManException {

		Language lang = getLang();

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

	@RequestMapping(value = PostManUrls.SEND_EMAIL_SUPPORT, method = RequestMethod.POST)
	public Email sendEmail(@RequestBody SupportEmail email) throws PostManException {
		Language lang = getLang();
		if (email.getLang() == null) {
			email.setLang(lang);
		}
		postManService.sendEmailToSupprt(email);
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
