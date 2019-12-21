package com.amx.jax.branch;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.branch.beans.BranchSession;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dict.UserClient.AppType;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.AuditActor;
import com.amx.jax.logger.AuditDetailProvider;
import com.amx.jax.model.UserDevice;
import com.amx.jax.rest.AppRequestContextInFilter;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.sso.SSOUser;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

@Primary
@Component
public class BranchOutFilter implements IMetaRequestOutFilter<JaxMetaInfo>, AuditDetailProvider,
		AppRequestContextInFilter {

	@Autowired
	private SSOUser ssoUser;

	@Autowired(required = false)
	private BranchSession branchSession;

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
		requestMeta.setChannel(JaxChannel.BRANCH);

		if (!ArgUtil.isEmpty(ssoUser.getUserDetails())) {
			requestMeta.setEmployeeId(ssoUser.getUserDetails().getEmployeeId());
			requestMeta.setCountryBranchId(ssoUser.getUserDetails().getCountryBranchId());
			requestMeta.setCountryId(ssoUser.getUserDetails().getCountryId());
		}

		if (!ArgUtil.isEmpty(ssoUser.getUserClient())) {
			requestMeta.setTerminalId(ssoUser.getUserClient().getTerminalId());
		}

		if (!ArgUtil.isEmpty(branchSession) && !ArgUtil.isEmpty(branchSession.getCustomerId())) {
			requestMeta.setCustomerId(branchSession.getCustomerId());
		}
	}

	private BigDecimal getCustomerId() {
		return ArgUtil.isEmpty(branchSession) ? null : branchSession.getCustomerId();
	}

	@Override
	public AuditActor getActor() {
		return new AuditActor(AuditActor.ActorType.EMP, getCustomerId());
	}

	@Override
	public void appRequestContextInFilter(CommonHttpRequest localCommonHttpRequest) {
		UserDevice userDevice = localCommonHttpRequest.getUserDevice();
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
