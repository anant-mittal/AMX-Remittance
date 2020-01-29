package com.amx.jax.proto;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.VendorAuthConfig;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dict.Language;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.CommonHttpRequest.ApiRequestDetail;
import com.amx.jax.rest.AppRequestInterfaces.VendorAuthFilter;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;

@Component
public class TPCRequestFilter implements IMetaRequestOutFilter<JaxMetaInfo>, VendorAuthFilter {

	@Override
	public JaxMetaInfo exportMeta() {
		JaxMetaInfo requestMeta = new JaxMetaInfo();
		outFilter(requestMeta);
		return requestMeta;
	}

	@Autowired
	JaxMetaInfo jaxMetaInfoContext;

	@Override
	public void outFilter(JaxMetaInfo jaxMetaInfo) {
		jaxMetaInfo.setCustomerId(jaxMetaInfoContext.getCustomerId());
		jaxMetaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setLanguageId(Language.DEFAULT.getBDCode());
		jaxMetaInfo.setCompanyId(new BigDecimal(JaxMetaInfo.DEFAULT_COMPANY_ID));
		jaxMetaInfo.setCountryBranchId(jaxMetaInfoContext.getCountryBranchId());
		jaxMetaInfo.setChannel(JaxChannel.TPC);
	}

	@Autowired(required = false)
	VendorAuthConfig appVendorConfig;

	@Override
	public boolean isAuthVendorRequest(ApiRequestDetail apiRequest, CommonHttpRequest req, String traceId,
			String authToken) {
		return appVendorConfig.isRequestValid(apiRequest, req, traceId, authToken);
	}

}
