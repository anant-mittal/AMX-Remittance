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

import com.amx.jax.AppParam;
import com.amx.jax.dict.Language;
import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.PostManConfig;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.ExceptionReport;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.SupportEmail;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.postman.service.PostManServiceImpl;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

/**
 * The Class PostManController.
 */
@RestController
public class PostManController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PostManController.class);

	/** The post man service. */
	@Autowired
	private PostManServiceImpl postManService;

	/** The request. */
	@Autowired
	private HttpServletRequest request;

	/** The post man config. */
	@Autowired
	private PostManConfig postManConfig;

	/**
	 * Gets the lang.
	 *
	 * @return the lang
	 */
	public Language getLang() {
		String langString = request.getParameter(PostManServiceImpl.PARAM_LANG);// localeResolver.resolveLocale(request).toString();
		Language lang = (Language) ArgUtil.parseAsEnum(langString, postManConfig.getTenantLang());
		return lang;
	}

	/**
	 * Process template.
	 *
	 * @param template
	 *            the template
	 * @param data
	 *            the data
	 * @param fileName
	 *            the file name
	 * @param fileType
	 *            the file type
	 * @return the file
	 */
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

	/**
	 * Process template file.
	 *
	 * @param file
	 *            the file
	 * @return the file
	 */
	@RequestMapping(value = PostManUrls.PROCESS_TEMPLATE_FILE, method = RequestMethod.POST)
	public File processTemplateFile(@RequestBody File file) {
		Language lang = getLang();
		file.setLang(lang);
		try {
			file = postManService.processTemplate(file);
		} catch (Exception e) {
			LOGGER.error(" Template Error {}", file.getModel(), e);
		}

		LOGGER.info(" FILE {} : {} . {}", file.getTemplate(), file.getName(), file.getType());

		return file;
	}

	/**
	 * Send SMS.
	 *
	 * @param sms
	 *            the sms
	 * @param async
	 *            the async
	 * @return the sms
	 * @throws PostManException
	 *             the post man exception
	 */
	@RequestMapping(value = PostManUrls.SEND_SMS, method = RequestMethod.POST)
	public SMS sendSMS(@RequestBody SMS sms, @RequestParam(required = false, defaultValue = "false") Boolean async)
			throws PostManException {

		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:START", "sendSMS");
		}

		Language lang = getLang();

		if (sms.getLang() == null) {
			sms.setLang(lang);
		}

		if (async == true) {
			postManService.sendSMSAsync(sms);
		} else {
			postManService.sendSMS(sms);
		}
		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:END", "sendSMS");
		}
		return sms;
	}

	/**
	 * Send email get.
	 *
	 * @param tnt
	 *            the tnt
	 * @param language
	 *            the language
	 * @param to
	 *            the to
	 * @param customer
	 *            the customer
	 * @param amount
	 *            the amount
	 * @param loyaltypoints
	 *            the loyaltypoints
	 * @param refno
	 *            the refno
	 * @param date
	 *            the date
	 * @param languageid
	 *            the languageid
	 * @param template
	 *            the template
	 * @return the map
	 * @throws PostManException
	 *             the post man exception
	 */
	@Deprecated
	@RequestMapping(value = PostManUrls.SEND_EMAIL, method = RequestMethod.GET)
	public Map<String, Object> sendEmailGet(@RequestParam Tenant tnt, @RequestParam(required = false) Language language,
			@RequestParam String to, @RequestParam String customer, @RequestParam String amount,
			@RequestParam String loyaltypoints, @RequestParam String refno, @RequestParam String date,
			@RequestParam(required = false) String languageid, @RequestParam Templates template)
			throws PostManException {

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", to);
		modeldata.put("customer", customer);
		modeldata.put("amount", amount);
		modeldata.put("loyaltypoints", loyaltypoints);
		modeldata.put("refno", refno);
		modeldata.put("date", date);
		wrapper.put("data", modeldata);

		Email email = new Email();

		if ("2".equals(languageid)) {
			email.setLang(Language.AR);
			modeldata.put("languageid", Language.AR);
		} else {
			email.setLang(Language.EN);
			modeldata.put("languageid", Language.EN);
		}
		email.setModel(wrapper);
		email.addTo(to);
		email.setHtml(true);
		email.setSubject("Feedback Email"); // Given by Umesh

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

	/**
	 * Send email.
	 *
	 * @param email
	 *            the email
	 * @param async
	 *            the async
	 * @return the email
	 * @throws PostManException
	 *             the post man exception
	 */
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

	/**
	 * Send email.
	 *
	 * @param email
	 *            the email
	 * @return the email
	 * @throws PostManException
	 *             the post man exception
	 */
	@RequestMapping(value = PostManUrls.SEND_EMAIL_SUPPORT, method = RequestMethod.POST)
	public Email sendEmail(@RequestBody SupportEmail email) throws PostManException {
		Language lang = getLang();
		if (email.getLang() == null) {
			email.setLang(lang);
		}
		postManService.sendEmailToSupprt(email);
		return email;
	}

	/**
	 * Notify slack.
	 *
	 * @param msg
	 *            the msg
	 * @return the message
	 * @throws PostManException
	 *             the post man exception
	 */
	@RequestMapping(value = PostManUrls.NOTIFY_SLACK, method = RequestMethod.POST)
	public Message notifySlack(@RequestBody Notipy msg) throws PostManException {
		postManService.notifySlack(msg);
		return msg;
	}

	/**
	 * Notify slack.
	 *
	 * @param eMsg
	 *            the e msg
	 * @param title
	 *            the title
	 * @param appname
	 *            the appname
	 * @param exception
	 *            the exception
	 * @return the exception
	 * @throws PostManException
	 *             the post man exception
	 */
	@RequestMapping(value = PostManUrls.NOTIFY_SLACK_EXCEP, method = RequestMethod.POST)
	public Exception notifySlack(@RequestBody Exception eMsg, @RequestParam(required = false) String title,
			@RequestParam(required = false) String appname, @RequestParam(required = false) String exception)
			throws PostManException {
		postManService.notifyException(appname, title, exception, new ExceptionReport(eMsg));
		return eMsg;
	}

	@RequestMapping(value = PostManUrls.NOTIFY_SLACK_EXCEP_REPORT, method = RequestMethod.POST)
	public Exception notifySlackReport(@RequestBody ExceptionReport eMsg, @RequestParam(required = false) String title,
			@RequestParam(required = false) String appname, @RequestParam(required = false) String exception)
			throws PostManException {
		postManService.notifyException(appname, title, exception, eMsg);
		if (eMsg.getEmail() != null) {
			postManService.sendEmail(eMsg.getEmail());
		}
		return eMsg;
	}

}
