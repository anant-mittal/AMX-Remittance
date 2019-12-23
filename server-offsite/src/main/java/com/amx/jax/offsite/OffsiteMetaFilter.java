package com.amx.jax.offsite;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.UserClient.AppType;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.AuditActor;
import com.amx.jax.logger.AuditDetailProvider;
import com.amx.jax.model.UserDevice;
import com.amx.jax.offsite.service.CustomerSession;
import com.amx.jax.rest.AppRequestContextInFilter;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.sso.SSOUser;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

@Component
public class OffsiteMetaFilter
		implements IMetaRequestOutFilter<JaxMetaInfo>, AuditDetailProvider, AppRequestContextInFilter {

	@Autowired
	OffsiteAppConfig offsiteAppConfig;

	@Autowired(required = false)
	CustomerSession customerSession;

	@Autowired(required = false)
	SSOUser sSOUser;

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo requestMeta = new JaxMetaInfo();
		outFilter(requestMeta);
		return requestMeta;
	}

	@Override
	public void outFilter(JaxMetaInfo requestMeta) {
		requestMeta.setTenant(TenantContextHolder.currentSite());
		requestMeta.setTraceId(ContextUtil.getTraceId());
		requestMeta.setCountryId(offsiteAppConfig.getCountryId());
		requestMeta.setCompanyId(offsiteAppConfig.getCompanyId());
		requestMeta.setLanguageId(offsiteAppConfig.getLanguageId());
		requestMeta.setCountryBranchId(offsiteAppConfig.getCountrybranchId());

		if (customerSession != null) {
			requestMeta.setCustomerId(customerSession.getCustomerId());
			AppContextUtil.setTranxId(customerSession.getTranxId());
		}
		// HardCoded
		if (!ArgUtil.isEmpty(sSOUser.getUserDetails())) {
			requestMeta.setEmployeeId(sSOUser.getUserDetails().getEmployeeId());
		}

	}

	private BigDecimal getCustomerId() {
		return ArgUtil.isEmpty(customerSession) ? null : customerSession.getCustomerId();
	}

	@Override
	public AuditActor getActor() {
		return new AuditActor(AuditActor.ActorType.EMP, getCustomerId());
	}

	@Override
	public void appRequestContextInFilter(CommonHttpRequest localCommonHttpRequest) {
		UserDevice userDevice = commonHttpRequest.getUserDevice();
		UserDeviceClient userClient = AppContextUtil.getUserClient();
		if (AppType.ANDROID.equals(userClient.getAppType())) {
			userClient.setClientType(ClientType.ONLINE_AND);
		} else if (AppType.IOS.equals(userClient.getAppType())) {
			userClient.setClientType(ClientType.ONLINE_IOS);
		} else if (AppType.WEB.equals(userClient.getAppType())) {
			userClient.setClientType(ClientType.ONLINE_WEB);
		} else {
			userClient.setClientType(ClientType.UNKNOWN);
		}
	}
}
