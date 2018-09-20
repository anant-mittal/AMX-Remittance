package com.amx.jax.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.rest.RestMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ContextUtil;

@Profile("test")
@Component
public class JaxClientMetaFilter extends RestMetaRequestOutFilter<JaxMetaInfo> {

	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo jaxMetaInfo = new JaxMetaInfo();

		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setTraceId(ContextUtil.getTraceId());

		//jaxMetaInfo.setCountryId(offsiteAppConfig.getCountryId());
		//jaxMetaInfo.setCompanyId(offsiteAppConfig.getCompanyId());
		//jaxMetaInfo.setLanguageId(offsiteAppConfig.getLanguageId());
		//jaxMetaInfo.setCountryBranchId(offsiteAppConfig.getCountrybranchId());

		return jaxMetaInfo;
	}

}
