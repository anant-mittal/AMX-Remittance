package com.amx.jax.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

@Profile("test")
@Component
public class JaxClientMetaFilter implements IMetaRequestOutFilter<JaxMetaInfo> {


	@Autowired
	private JaxMetaInfo jaxMetaInfoBean;
	
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
		
		if(!ArgUtil.isEmpty(jaxMetaInfoBean.getCustomerId())) {
			jaxMetaInfo.setCustomerId(jaxMetaInfoBean.getCustomerId());
		} else {
			jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		}
		

		// jaxMetaInfo.setCountryBranchId(offsiteAppConfig.getCountrybranchId());
	}

}
