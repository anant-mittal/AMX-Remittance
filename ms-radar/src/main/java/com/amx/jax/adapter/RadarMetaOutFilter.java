package com.amx.jax.adapter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.Language;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;

@Component
public class RadarMetaOutFilter implements IMetaRequestOutFilter<JaxMetaInfo> {

	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo requestMeta = new JaxMetaInfo();
		outFilter(requestMeta);
		return requestMeta;
	}

	@Override
	public void outFilter(JaxMetaInfo jaxMetaInfo) {

		jaxMetaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setLanguageId(Language.DEFAULT.getBDCode());
		jaxMetaInfo.setCompanyId(new BigDecimal(JaxMetaInfo.DEFAULT_COMPANY_ID));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(JaxMetaInfo.DEFAULT_COUNTRY_BRANCH_ID));

	}

}
