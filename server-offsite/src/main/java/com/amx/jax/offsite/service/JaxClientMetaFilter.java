package com.amx.jax.offsite.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.offsite.OffsiteAppConfig;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ContextUtil;

@Component
public class JaxClientMetaFilter implements IMetaRequestOutFilter<JaxMetaInfo> {

	@Autowired
	OffsiteAppConfig offsiteAppConfig;

	@Autowired(required = false)
	CustomerSession customerSession;

	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo jaxMetaInfo = new JaxMetaInfo();

		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setTraceId(ContextUtil.getTraceId());
		jaxMetaInfo.setCountryId(offsiteAppConfig.getCountryId());
		jaxMetaInfo.setCompanyId(offsiteAppConfig.getCompanyId());
		jaxMetaInfo.setLanguageId(offsiteAppConfig.getLanguageId());
		jaxMetaInfo.setCountryBranchId(offsiteAppConfig.getCountrybranchId());

		if (customerSession != null) {
			jaxMetaInfo.setCustomerId(customerSession.getCustomerId());
			AppContextUtil.setTranxId(customerSession.getTranxId());
		}

		// HardCoded
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));

		return jaxMetaInfo;
	}

	@Override
	public void outFilter(JaxMetaInfo requestMeta) {

	}

}
