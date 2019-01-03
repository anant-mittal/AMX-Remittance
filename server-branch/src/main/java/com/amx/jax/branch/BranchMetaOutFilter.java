package com.amx.jax.branch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.sso.SSOUser;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

@Component
public class BranchMetaOutFilter implements IMetaRequestOutFilter<JaxMetaInfo> {

	@Autowired
	private SSOUser ssoUser;

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

		// HardCoded
		if (!ArgUtil.isEmpty(ssoUser.getUserDetails())) {
			requestMeta.setEmployeeId(ssoUser.getUserDetails().getEmployeeId());
			requestMeta.setCountryBranchId(ssoUser.getUserDetails().getCountryBranchId());
			requestMeta.setCountryId(ssoUser.getUserDetails().getCountryId());
		}
	}

}
