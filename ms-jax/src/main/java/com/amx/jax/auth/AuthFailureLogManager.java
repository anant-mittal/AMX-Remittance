package com.amx.jax.auth;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.AppContextUtil;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.dbmodel.auth.AuthFailureLog;
import com.amx.jax.error.JaxError;
import com.amx.jax.repository.auth.AuthFailureLogRepository;
import com.amx.jax.util.JaxContextUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthFailureLogManager {

	@Autowired
	AuthFailureLogRepository authFailureLogRepository;
	@Autowired
	JaxTenantProperties jaxTenantProperties;

	private static final Logger log = LoggerFactory.getLogger(AuthFailureLogManager.class);

	public void logAuthFailureEvent(AbstractJaxException ex) {
		String identityInt = null;
		String clientIp = AppContextUtil.getUserClient().getIp();
		Object requestModel = JaxContextUtil.getRequestModel();
		if (requestModel instanceof CustomerModel) {
			CustomerModel customerModel = (CustomerModel) requestModel;
			String civilId = customerModel.getLoginId();
			identityInt = civilId;
		}
		log.debug("Failed auth attempt ip {}, civil id {}", clientIp, identityInt);
		AuthFailureLog authFailureLog = new AuthFailureLog();
		authFailureLog.setEventDate(new Date());
		authFailureLog.setEventName(JaxEvent.getAuthLogEvent(JaxContextUtil.getJaxEvent()));
		authFailureLog.setIdentityInt(identityInt);
		authFailureLog.setIpAddress(clientIp);
		authFailureLogRepository.save(authFailureLog);
	}

	public void validateAuthFailure() {
		Calendar cal = Calendar.getInstance();
		String clientIp = AppContextUtil.getUserClient().getIp();
		if (jaxTenantProperties.getAuthFailLogInterval() != null && jaxTenantProperties.getAuthFailLogAttemps() != null) {
			cal.add(Calendar.SECOND, jaxTenantProperties.getAuthFailLogInterval() * -1);
			Long authFailRecordcnt = authFailureLogRepository.getFailedRecordCountForIp(clientIp, cal.getTime());
			if (authFailRecordcnt > jaxTenantProperties.getAuthFailLogAttemps()) {
				log.info("IP {} is blocked by jax", clientIp);
				throw new GlobalException(JaxError.CLIENT_IP_BLOCKED,
						"IP address " + clientIp + " is blocked due to frequent failed attempts, please try after sometime");
			}

		}
	}

}
