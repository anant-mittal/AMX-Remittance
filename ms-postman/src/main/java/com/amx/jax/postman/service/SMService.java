package com.amx.jax.postman.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Tenant;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
@TenantScoped
public class SMService {

	private Logger logger = Logger.getLogger(SMService.class);

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

	public SMS sendSMS(SMS sms) throws UnirestException {

		String phone = sms.getTo().get(0);

		if (phone != null && phone.length() == 10) {
			HttpResponse<String> response = Unirest.post(remoteUrl).header("authkey", authKey)
					.header("content-type", "application/json")
					.body(String.format(
							"{ \"sender\": \"%s\", \"route\": \"%s\", \"country\": \"91\", \"sms\": [ { \"message\": \"%s\", \"to\": [ \"%s\" ] } ] }",
							senderId, route, sms.toText(), sms.getTo().get(0)))
					.asString();
			logger.info("SMS Sent   " + response.getBody());
		} else if (phone != null && phone.length() == 8) {
			Tenant tnt = TenantContextHolder.currentSite();
			if (tnt == Tenant.BHR) {
				HttpResponse<String> response = Unirest.get("http://ems.kalaam-telecom.com/SendSms.aspx")
						.queryString("User", username).queryString("passwd", password)
						.queryString("mobilenumber", "973" + phone).queryString("message", sms.toText())
						.queryString("sid", secret).queryString("mtype", "N").queryString("DR", "Y").asString();
				logger.info("SMS Sent   " + response.getBody());
			} else if (tnt == Tenant.KWT) {
				HttpResponse<String> response = Unirest
						.post("https://applications2.almullagroup.com/Login_Enhanced/LoginEnhancedServlet")
						// .header("content-type", "application/json")
						.field("destination_mobile", "965" + phone).field("message_to_send", sms.toText()).asString();
				logger.info("SMS Sent   " + response.getBody());
			}
		}

		return sms;
	}
}
