package com.amx.jax.offsite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.offsite.OffsiteAppConfig;
import com.amx.jax.rest.RestMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.scope.TenantScoped;
import com.amx.utils.ContextUtil;

@Component
@TenantScoped
public class JaxClientMetaFilter implements RestMetaRequestOutFilter<JaxMetaInfo> {

	@Autowired
	OffsiteAppConfig offsiteAppConfig;

	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo jaxMetaInfo = new JaxMetaInfo();

		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setTraceId(ContextUtil.getTraceId());

		jaxMetaInfo.setCountryId(offsiteAppConfig.getCountryId());
		jaxMetaInfo.setCompanyId(offsiteAppConfig.getCompanyId());
		jaxMetaInfo.setLanguageId(offsiteAppConfig.getLanguageId());
		jaxMetaInfo.setCountryBranchId(offsiteAppConfig.getCountrybranchId());

		return jaxMetaInfo;
	}

}
