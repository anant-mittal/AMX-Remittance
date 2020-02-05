package com.amx.jax.auth;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.AppContextUtil;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.dbmodel.auth.AuthFailureLog;
import com.amx.jax.dbmodel.auth.BlockedIPAdress;
import com.amx.jax.dbmodel.auth.IPBlockedReasoncode;
import com.amx.jax.error.JaxError;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.repository.auth.AuthFailureLogRepository;
import com.amx.jax.repository.auth.BlockedIPAdressRepository;
import com.amx.jax.util.AmxDBConstants;
import com.amx.jax.util.JaxContextUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthFailureLogManager {

	@Autowired
	AuthFailureLogRepository authFailureLogRepository;
	@Autowired
	JaxTenantProperties jaxTenantProperties;
	@Autowired
	BlockedIPAdressRepository blockedIPAdressRepository;
	@Autowired
	PostManService postManService;

	private static final Logger log = LoggerFactory.getLogger(AuthFailureLogManager.class);

	@Transactional
	public void logAuthFailureEvent(AbstractJaxException ex) {
		if (!isApplicable(ex)) {
			return;
		}
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
		blockIpAddress(clientIp, IPBlockedReasoncode.AUTH_FAILURE_EXCEEDED);
	}

	private boolean isApplicable(AbstractJaxException ex) {
		if (JaxError.CLIENT_IP_BLOCKED.equals(ex.getError())) {
			return false;
		}
		return true;
	}

	private void blockIpAddress(String clientIp, IPBlockedReasoncode reasonCode) {
		if (jaxTenantProperties.getAuthFailBlocktime() != null && jaxTenantProperties.getAuthFailLogAttemps() != null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, jaxTenantProperties.getAuthFailBlocktime() * -1);
			Long authFailRecordcnt = authFailureLogRepository.getFailedRecordCountForIp(clientIp, cal.getTime());
			if (authFailRecordcnt > jaxTenantProperties.getAuthFailLogAttemps()) {
				Date unblockDate = getLastUnblockDate(clientIp);
				// if unblocked today then check again no of count again
				if (unblockDate != null) {
					authFailRecordcnt = authFailureLogRepository.getFailedRecordCountForIp(clientIp, unblockDate);
					if (authFailRecordcnt <= jaxTenantProperties.getAuthFailLogAttemps()) {
						return;
					}
				}
				log.info("IP {} is being blocked by jax", clientIp);
				BlockedIPAdress blockedIp = new BlockedIPAdress(clientIp, reasonCode);
				blockedIPAdressRepository.save(blockedIp);
				sendEmailAlert(blockedIp);
			}
		}

	}

	private Date getLastUnblockDate(String clientIp) {
		Date unblockedDate = null;
		List<BlockedIPAdress> blockedIpList = blockedIPAdressRepository.findByIpAddressAndIsActiveAndBlockedIpIdIsNotNull(clientIp,
				ConstantDocument.No, new Sort("unblockedDate"));
		if (CollectionUtils.isNotEmpty(blockedIpList)) {
			unblockedDate = blockedIpList.get(0).getUnblockedDate();
		}
		return unblockedDate;
	}

	private void sendEmailAlert(BlockedIPAdress blockedIp) {
		String itOpsEmail = jaxTenantProperties.getItOpsEmail();
		if (itOpsEmail != null) {
			StringBuilder sBuf = new StringBuilder();
			Email email = new Email();
			email.addTo(itOpsEmail);
			sBuf.append("IP address: ").append(blockedIp.getIpAddress());
			sBuf.append(" is blocked from JAX application, due to multiple failed attempts");
			email.setMessage(sBuf.toString());
			email.setSubject("IP Blocked - JAX");
			postManService.sendEmail(email);
		}
	}

	/**
	 * validates ip address based on blockedIPaddress table
	 */
	public void validateAuthFailure() {
		String clientIp = AppContextUtil.getUserClient().getIp();
		List<BlockedIPAdress> blockedRecords = blockedIPAdressRepository.findByIpAddressAndIsActive(clientIp, AmxDBConstants.Yes);
		if (blockedRecords.size() > 0) {
			log.info("clientIP {} is blocked {} times", clientIp, blockedRecords.size());
			throw new GlobalException(JaxError.CLIENT_IP_BLOCKED,
					"IP address " + clientIp + " is blocked due to frequent failed attempts, please contact/visit branch to unblock it.");
		}
	}

	/**
	 * validates ip address based on defined interval in property
	 */
	private void validateAuthFailureByInterval() {
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
