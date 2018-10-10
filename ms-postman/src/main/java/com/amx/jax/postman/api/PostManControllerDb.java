package com.amx.jax.postman.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.service.PostManServiceImpl;
import com.amx.utils.ArgUtil;

@RestController
@RequestMapping(PostManUrls.SEND_EMAIL_DB)
@Deprecated
public class PostManControllerDb {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManControllerDb.class);

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

	/**
	 * @param tnt
	 * @param lang
	 * @param to
	 * @param customer
	 * @param amount
	 * @param amount
	 * @return
	 */
	@RequestMapping(value = "send", method = RequestMethod.GET)
	public Map<String, Object> sendEmailGet(@RequestParam Tenant tnt, @RequestParam(required = false) Language language,
			@RequestParam String to, @RequestParam String customer, @RequestParam String amount,
			@RequestParam String loyaltypoints, @RequestParam String refno, @RequestParam String date,
			@RequestParam(required = false) String languageid, @RequestParam TemplatesMX template)
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

		email.setITemplate(template);
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
	 * @param tnt
	 * @param lang
	 * @param to
	 * @param customer
	 * @param amount
	 * @param amount
	 * @return
	 */
	@RequestMapping(value = "civilexpire", method = RequestMethod.GET)
	public Map<String, Object> sendCivilExpiryEmailGet(@RequestParam Tenant tnt,
			@RequestParam(required = false) Language language, @RequestParam String to, @RequestParam String customer,
			@RequestParam String date, @RequestParam(required = false) String languageid,
			@RequestParam TemplatesMX template) throws PostManException {

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> modeldata = new HashMap<String, Object>();
		modeldata.put("to", to);
		modeldata.put("customer", customer);
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

		if (template == TemplatesMX.CIVILID_EXPIRY) {
			email.setSubject("Civil ID Expiry Reminder"); // Given by Umesh
		} else {
			email.setSubject("Civil ID has been expired"); // Given by Umesh
		}

		email.setITemplate(template);
		postManService.sendEmailAsync(email);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> data = new HashMap<String, String>();
		data.put("msg", String.format("Email is send to %s", customer));
		map.put("data", data);
		map.put("error", null);
		map.put("meta", null);
		map.put("responseCode", "SUCCESS");
		map.put("responseMessage", "Email is successfully sent.");
		return map;
	}

}
