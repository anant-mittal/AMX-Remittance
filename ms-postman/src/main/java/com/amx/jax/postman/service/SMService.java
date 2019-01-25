package com.amx.jax.postman.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.PostManConfig;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.audit.PMGaugeEvent;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.rest.RestQuery;
import com.amx.jax.rest.RestService;
import com.amx.jax.rest.RestService.Ajax.RestMethod;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.amx.utils.ArgUtil;
import com.amx.utils.CryptoUtil;
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

	@TenantValue("${sms.request.method}")
	private RestMethod smsReqType;

	@TenantValue("${sms.request.url}")
	private String smsReqUrl;

	@TenantValue("${sms.request.query}")
	private String smsReqQuery;

	@TenantValue("${sms.request.fields}")
	private RestQuery smsReqFields;

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
	private RestService restService;

	/** The audit service. */
	@Autowired
	private AuditService auditService;

	/** The slack service. */
	@Autowired
	private SlackService slackService;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private PostManConfig postManConfig;

	@Autowired
	private ContactCleanerService contactService;

	@Autowired
	private FileService fileService;

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
	 * @param sms the sms
	 * @return the sms
	 */
	public SMS sendSMS(SMS sms) {
		String to = null;
		PMGaugeEvent pMGaugeEvent = new PMGaugeEvent(PMGaugeEvent.Type.SEND_SMS);
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Sending {} SMS to {}", sms.getTemplate(), Utils.commaConcat(sms.getTo()));
			}

			to = sms.getTo() != null ? sms.getTo().get(0) : null;

			if (sms.getTemplate() != null) {
				Context context = new Context(postManConfig.getLocal(sms));
				context.setVariables(sms.getModel());
				
				File file = new File();
				file.setTemplate(sms.getTemplate());
				file.setModel(sms.getModel());
				file.setLang(sms.getLang());
				
				sms.setMessage(fileService.create(file).getContent() 
						//templateService.processHtml(sms.getITemplate(), context)
						);
			}

			if (ArgUtil.isEmpty(to)) {
				throw new PostManException(PostManException.ErrorCode.NO_RECIPIENT_DEFINED);
			}

			String responseText = this.doSendSMS(sms);
			auditService.gauge(pMGaugeEvent.set(sms, responseText));
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
	 * @param sms the sms
	 * @return the sms
	 * @throws UnsupportedEncodingException
	 */
	public String doSendSMS(SMS sms) throws UnsupportedEncodingException {

		String phone = contactService.getMobile(sms.getTo().get(0));

		if (!appConfig.isProdMode() && !ArgUtil.isEmpty(sms.getITemplate())
				&& !ArgUtil.isEmpty(sms.getITemplate().getChannel())) {
			Notipy msg = new Notipy();
			msg.setAuthor(String.format("%s = %s", sms.getTo().get(0), phone));
			msg.setMessage(sms.toText());
			msg.setChannel(sms.getITemplate().getChannel());
			msg.addField("TEMPLATE", sms.getITemplate().toString());
			msg.setColor("#" + CryptoUtil.toHex(6, sms.getITemplate().toString()));
			slackService.sendNotification(msg);
		}

		if (!appConfig.isProdMode() && (phone != null && phone.length() == 10)) {

			Map<String, Object> map = MapBuilder.map().put("sender", senderId).put("route", route).put("country", "91")
					.put(messagePath,
							URLEncoder.encode(sms.toText(), "UTF-8"))
					.put(toPath, phone.trim()).toMap();
			return restService.ajax(remoteUrl).header("authkey", authKey).header("content-type", "application/json")
					.post(JsonUtil.toJson(map)).asString();

		} else if (phone != null) {

			Map<String, String> params = new HashMap<String, String>();
			params.put("mobile", phone.trim());
			params.put("text", sms.toText());
			params.put("username", username);
			params.put("password", password);
			params.put("secret", secret);
			params.put("traceid", AppContextUtil.getTraceId());
			return restService.ajax(this.smsReqUrl).build(smsReqType, smsReqQuery, smsReqFields, params).asString();
		} else {
			throw new PostManException(PostManException.ErrorCode.NO_TENANT_DEFINED);
		}

	}
}
