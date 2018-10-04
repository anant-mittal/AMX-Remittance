package com.amx.jax.client;

import java.math.BigDecimal;

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

		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		
		//jaxMetaInfo.setCountryBranchId(offsiteAppConfig.getCountrybranchId());

		return jaxMetaInfo;
	}

}
