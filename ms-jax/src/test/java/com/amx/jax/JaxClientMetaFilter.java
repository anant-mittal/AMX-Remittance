package com.amx.jax;

import java.math.BigDecimal;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ContextUtil;

@Component
public class JaxClientMetaFilter implements IMetaRequestOutFilter<JaxMetaInfo> {

	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo jaxMetaInfo = new JaxMetaInfo();
		outFilter(jaxMetaInfo);
		return jaxMetaInfo;
	}

	@Override
	public void outFilter(JaxMetaInfo jaxMetaInfo) {
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setTraceId(ContextUtil.getTraceId());

		jaxMetaInfo.setCountryId(jaxMetaInfo.getTenant().getBDCode());
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));

		// jaxMetaInfo.setCountryBranchId(offsiteAppConfig.getCountrybranchId());
	}

}
