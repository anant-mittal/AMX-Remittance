package com.amx.jax.postman.service;

import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.amx.jax.AppParam;
import com.amx.jax.dict.Tenant;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.rest.RestService;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonPath;
import com.amx.utils.JsonUtil;
import com.amx.utils.MapBuilder;
import com.amx.utils.Utils;

/**
 * The Class SMService.
 */
@Component
@TenantScoped
public class SMService {

	/** The logger. */
	private static Logger LOGGER = LoggerService.getLogger(SMService.class);

	/** The auth key. */
	@Value("${msg91.auth.key}")
	private String authKey;

	/** The remote url. */
	@Value("${msg91.remote.url}")
	private String remoteUrl;

	/** The sender id. */
	@Value("${msg91.sender.id}")
	private String senderId;

	/** The route. */
	@Value("${msg91.route}")
	private String route;

	/** The username. */
	@TenantValue("${sms.username}")
	private String username;

	/** The password. */
	@TenantValue("${sms.password}")
	private String password;

	/** The secret. */
	@TenantValue("${sms.secret}")
	private String secret;

	/** The rest service. */
	@Autowired
	RestService restService;

	/** The audit service. */
	@Autowired
	AuditService auditService;

	/** The slack service. */
	@Autowired
	SlackService slackService;

	/** The template service. */
	@Autowired
	private TemplateService templateService;

	/** The message path. */
	public static JsonPath messagePath = new JsonPath("sms/[0]/message");

	/** The to path. */
	public static JsonPath toPath = new JsonPath("sms/[0]/to/[0]");

	/** The Constant EMPTY_TO. */
	public static final String EMPTY_TO = "EMPTY_TO";

	/** The Constant EXCEPTION. */
	public static final String EXCEPTION = "EXECPTION";

	/**
	 * Send SMS.
	 *
	 * @param sms
	 *            the sms
	 * @return the sms
	 */
	public SMS sendSMS(SMS sms) {
		String to = null;
		PMGaugeEvent pMGaugeEvent = new PMGaugeEvent(PMGaugeEvent.Type.SEND_SMS);
		try {
			LOGGER.info("Sending {} SMS to {}", sms.getTemplate(), Utils.commaConcat(sms.getTo()));

			to = sms.getTo() != null ? sms.getTo().get(0) : null;

			if (sms.getTemplate() != null) {
				Context context = new Context(new Locale(sms.getLang().toString()));
				context.setVariables(sms.getModel());
				sms.setMessage(templateService.processHtml(sms.getTemplate(), context));
			}

			if (ArgUtil.isEmpty(to)) {
				throw new PostManException(PostManException.ErrorCode.NO_RECIPIENT_DEFINED);
			}

			this.doSendSMS(sms);
			auditService.gauge(pMGaugeEvent.set(sms));
		} catch (PostManException e) {
			auditService.gauge(pMGaugeEvent.set(sms).set(Result.FAIL));
		} catch (Exception e) {
			auditService.excep(pMGaugeEvent.set(sms), LOGGER, e);
			slackService.sendException(to, e);
		}
		return sms;

	}

	/**
	 * Do send SMS.
	 *
	 * @param sms
	 *            the sms
	 * @return the sms
	 */
	private SMS doSendSMS(SMS sms) {

		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:START", "sendSMS");
		}

		String phone = sms.getTo().get(0);

		if (phone != null && phone.length() == 10) {

			Map<String, Object> map = MapBuilder.map().put("sender", senderId).put("route", route).put("country", "91")
					.put(messagePath, sms.toText()).put(toPath, sms.getTo().get(0)).toMap();
			LOGGER.info("SMS Preparing   " + map);
			String response = restService.ajax(remoteUrl).header("authkey", authKey)
					.header("content-type", "application/json").post(JsonUtil.toJson(map)).asString();

		} else if (phone != null && phone.length() == 8) {
			Tenant tnt = TenantContextHolder.currentSite();

			if (tnt == Tenant.BHR) {

				String response = restService.ajax("http://ems.kalaam-telecom.com/SendSms.aspx")
						.queryParam("User", username).queryParam("passwd", password)
						.queryParam("mobilenumber", "973" + phone).queryParam("message", sms.toText())
						.queryParam("sid", secret).queryParam("mtype", "N").queryParam("DR", "Y").get().asString();

			} else if (tnt == Tenant.KWT) {

				String response = restService
						.ajax("https://applications2.almullagroup.com/Login_Enhanced/LoginEnhancedServlet")
						.field("destination_mobile", "965" + phone).field("message_to_send", sms.toText()).postForm()
						.asString();
			} else {
				throw new PostManException(PostManException.ErrorCode.NO_TENANT_DEFINED);
			}

		} else {
			throw new PostManException(PostManException.ErrorCode.NO_TENANT_DEFINED);
		}

		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:END", "sendSMS");
		}

		return sms;
	}
}
