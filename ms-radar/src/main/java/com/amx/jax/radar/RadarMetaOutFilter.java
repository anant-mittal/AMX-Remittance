package com.amx.jax.radar;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dict.Language;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;

@Primary
@Component
public class RadarMetaOutFilter implements IMetaRequestOutFilter<JaxMetaInfo> {

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo requestMeta = new JaxMetaInfo();
		outFilter(requestMeta);
		return requestMeta;
	}

	@Override
	public void outFilter(JaxMetaInfo jaxMetaInfoOut) {

		jaxMetaInfoOut.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfoOut.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfoOut.setLanguageId(Language.DEFAULT.getBDCode());
		jaxMetaInfoOut.setCompanyId(new BigDecimal(JaxMetaInfo.DEFAULT_COMPANY_ID));
		jaxMetaInfoOut.setCountryBranchId(new BigDecimal(JaxMetaInfo.DEFAULT_COUNTRY_BRANCH_ID));

		jaxMetaInfo.copyTo(jaxMetaInfoOut);
		jaxMetaInfoOut.setChannel(JaxChannel.SYSTEM);

	}

}
