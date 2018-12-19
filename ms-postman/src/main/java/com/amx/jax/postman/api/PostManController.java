package com.amx.jax.postman.api;

import java.util.List;
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
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.Language;
import com.amx.jax.postman.PostManConfig;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.ExceptionReport;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.SupportEmail;
import com.amx.jax.postman.model.TemplatesMX;
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
	 * @param template the template
	 * @param data     the data
	 * @param fileName the file name
	 * @param fileType the file type
	 * @return the file
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = PostManUrls.PROCESS_TEMPLATE, method = RequestMethod.POST)
	public AmxApiResponse<File, Object> processTemplate(@RequestParam TemplatesMX template,
			@RequestParam(required = false) String data, @RequestParam(required = false) String fileName,
			@RequestParam(required = false) File.Type fileType) {

		Language lang = getLang();
		File file = new File();
		file.setLang(lang);

		file.setITemplate(template);
		file.setType(fileType);
		file.setModel(JsonUtil.fromJson(data, Map.class));
		return postManService.processTemplate(file);

	}

	/**
	 * Process template file.
	 *
	 * @param file the file
	 * @return the file
	 */
	@RequestMapping(value = PostManUrls.PROCESS_TEMPLATE_FILE, method = RequestMethod.POST)
	public File processTemplateFile(@RequestBody File file) {
		Language lang = getLang();
		file.setLang(lang);
		return postManService.processTemplate(file).getResult();
	}

	/**
	 * Send SMS.
	 *
	 * @param sms   the sms
	 * @param async the async
	 * @return the sms
	 * @throws PostManException the post man exception
	 */
	@RequestMapping(value = PostManUrls.SEND_SMS, method = RequestMethod.POST)
	public AmxApiResponse<SMS, Object> sendSMS(@RequestBody SMS sms,
			@RequestParam(required = false, defaultValue = "false") Boolean async) throws PostManException {

		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:START", "sendSMS");
		}

		Language lang = getLang();

		if (sms.getLang() == null) {
			sms.setLang(lang);
		}

		if (async == true) {
			return postManService.sendSMSAsync(sms);
		} else {
			return postManService.sendSMS(sms);
		}
	}

	@RequestMapping(value = PostManUrls.SEND_SMS, method = RequestMethod.GET)
	public AmxApiResponse<SMS, Object> sendSMSGet(@RequestParam String to, @RequestParam String message)
			throws PostManException {
		SMS sms = new SMS();
		sms.addTo(to);
		sms.setMessage(message);
		return postManService.sendSMSAsync(sms);
	}

	@RequestMapping(value = PostManUrls.SEND_EMAIL, method = RequestMethod.POST)
	public AmxApiResponse<Email, Object> sendEmail(@RequestBody Email email,
			@RequestParam(required = false, defaultValue = "false") Boolean async) throws PostManException {

		Language lang = getLang();

		if (email.getLang() == null) {
			email.setLang(lang);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SEND_EMAIL   {} ", JsonUtil.toJson(email));
		}

		if (async == true) {
			return postManService.sendEmailAsync(email);
		} else {
			return postManService.sendEmail(email);
		}
	}

	@RequestMapping(value = PostManUrls.SEND_EMAIL_BULK, method = RequestMethod.POST)
	public AmxApiResponse<Email, Object> sendEmailBulk(@RequestBody List<Email> emailList) throws PostManException {
		return postManService.sendEmailBulk(emailList);
	}

	/**
	 * Send email.
	 *
	 * @param email the email
	 * @return the email
	 * @throws PostManException the post man exception
	 */
	@RequestMapping(value = PostManUrls.SEND_EMAIL_SUPPORT, method = RequestMethod.POST)
	public AmxApiResponse<Email, Object> sendEmail(@RequestBody SupportEmail email) throws PostManException {
		Language lang = getLang();
		if (email.getLang() == null) {
			email.setLang(lang);
		}
		return postManService.sendEmailToSupprt(email);
	}

	/**
	 * Notify slack.
	 *
	 * @param msg the msg
	 * @return the message
	 * @throws PostManException the post man exception
	 */
	@RequestMapping(value = PostManUrls.NOTIFY_SLACK, method = RequestMethod.POST)
	public AmxApiResponse<Notipy, Object> notifySlack(@RequestBody Notipy msg) throws PostManException {
		return postManService.notifySlack(msg);
	}

	/**
	 * Notify slack.
	 *
	 * @param eMsg      the e msg
	 * @param title     the title
	 * @param appname   the appname
	 * @param exception the exception
	 * @return the exception
	 * @throws PostManException the post man exception
	 */
	@RequestMapping(value = PostManUrls.NOTIFY_SLACK_EXCEP, method = RequestMethod.POST)
	public AmxApiResponse<ExceptionReport, Object> notifySlack(@RequestBody Exception eMsg,
			@RequestParam(required = false) String title, @RequestParam(required = false) String appname,
			@RequestParam(required = false) String exception) throws PostManException {
		return postManService.notifyException(appname, title, exception, new ExceptionReport(eMsg));
	}

	@RequestMapping(value = PostManUrls.NOTIFY_SLACK_EXCEP_REPORT, method = RequestMethod.POST)
	public AmxApiResponse<ExceptionReport, Object> notifySlackReport(@RequestBody ExceptionReport eMsg,
			@RequestParam(required = false) String title, @RequestParam(required = false) String appname,
			@RequestParam(required = false) String exception) throws PostManException {
		if (eMsg.getEmail() != null) {
			postManService.sendEmail(eMsg.getEmail());
		}
		return postManService.notifyException(appname, title, exception, eMsg);
	}

}
