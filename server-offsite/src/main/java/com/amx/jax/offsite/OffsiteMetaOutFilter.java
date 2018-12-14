package com.amx.jax.offsite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.offsite.service.CustomerSession;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.sso.SSOUser;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

@Component
public class OffsiteMetaOutFilter implements IMetaRequestOutFilter<JaxMetaInfo> {

	@Autowired
	OffsiteAppConfig offsiteAppConfig;

	@Autowired(required = false)
	CustomerSession customerSession;

	@Autowired(required = false)
	SSOUser sSOUser;

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

}
