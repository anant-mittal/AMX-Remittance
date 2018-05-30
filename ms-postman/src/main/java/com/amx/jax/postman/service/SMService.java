package com.amx.jax.postman.service;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.AppParam;
import com.amx.jax.dict.Tenant;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.rest.RestService;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.amx.utils.JsonPath;
import com.amx.utils.JsonUtil;
import com.amx.utils.MapBuilder;

@Component
@TenantScoped
public class SMService {

	private static Logger LOGGER = LoggerService.getLogger(SMService.class);

	@Value("${msg91.auth.key}")
	private String authKey;
	@Value("${msg91.remote.url}")
	private String remoteUrl;
	@Value("${msg91.sender.id}")
	private String senderId;
	@Value("${msg91.route}")
	private String route;

	@TenantValue("${sms.username}")
	private String username;

	@TenantValue("${sms.password}")
	private String password;

	@TenantValue("${sms.secret}")
	private String secret;

	@Autowired
	RestService restService;

	public static JsonPath messagePath = new JsonPath("sms/[0]/message");
	public static JsonPath toPath = new JsonPath("sms/[0]/to/[0]");

	public SMS sendSMS(SMS sms) {

		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:START", "sendSMS");
		}

		String phone = sms.getTo().get(0);

		if (phone != null && phone.length() == 10) {
			// HttpResponse<String> response = Unirest.post(remoteUrl).header("authkey",
			// authKey)
			// .header("content-type", "application/json")
			// .body(String.format(
			// "{ \"sender\": \"%s\", \"route\": \"%s\", \"country\": \"91\", \"sms\": [ {
			// \"message\": \"%s\", \"to\": [ \"%s\" ] } ] }",
			// senderId, route, sms.toText(), sms.getTo().get(0)))
			// .asString();

			Map<String, Object> map = MapBuilder.map().put("sender", senderId).put("route", route).put("country", "91")
					.put(messagePath, sms.toText()).put(toPath, sms.getTo().get(0)).toMap();
			LOGGER.info("SMS Preparing   " + map);
			String response = restService.ajax(remoteUrl).header("authkey", authKey)
					.header("content-type", "application/json").post(

							JsonUtil.toJson(map)

					).asString();

			LOGGER.info("SMS Sent   " + response);
		} else if (phone != null && phone.length() == 8) {
			Tenant tnt = TenantContextHolder.currentSite();
			if (tnt == Tenant.BHR) {
				String response = restService.ajax("http://ems.kalaam-telecom.com/SendSms.aspx")
						.queryParam("User", username).queryParam("passwd", password)
						.queryParam("mobilenumber", "973" + phone).queryParam("message", sms.toText())
						.queryParam("sid", secret).queryParam("mtype", "N").queryParam("DR", "Y").get().asString();
				LOGGER.info("SMS Sent   " + response);
			} else if (tnt == Tenant.KWT) {
				String response = restService
						.ajax("https://applications2.almullagroup.com/Login_Enhanced/LoginEnhancedServlet")
						.field("destination_mobile", "965" + phone).field("message_to_send", sms.toText()).postForm()
						.asString();
				LOGGER.info("SMS Sent   " + response);
			}
		}

		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:END", "sendSMS");
		}

		return sms;
	}
}
