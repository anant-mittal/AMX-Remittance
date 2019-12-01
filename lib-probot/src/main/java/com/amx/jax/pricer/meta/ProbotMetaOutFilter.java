package com.amx.jax.pricer.meta;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.amx.jax.rest.IMetaRequestOutFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ContextUtil;

@Primary
@Component
public class ProbotMetaOutFilter implements IMetaRequestOutFilter<ProbotMetaInfo> {

	@Override
	public ProbotMetaInfo exportMeta() {
		ProbotMetaInfo requestMeta = new ProbotMetaInfo();
		outFilter(requestMeta);
		return requestMeta;
	}

	@Override
	public void outFilter(ProbotMetaInfo requestMeta) {
		requestMeta.setTenant(TenantContextHolder.currentSite());
		requestMeta.setTraceId(ContextUtil.getTraceId());
		//requestMeta.setChannel(Channel.ONLINE);

		/*
		 * requestMeta.setEmployeeId(AppContextUtil.);
		 * 
		 * if (!ArgUtil.isEmpty(ssoUser.getUserDetails())) {
		 * requestMeta.setEmployeeId(ssoUser.getUserDetails().getEmployeeId());
		 * requestMeta.setCountryBranchId(ssoUser.getUserDetails().getCountryBranchId())
		 * ; requestMeta.setCountryId(ssoUser.getUserDetails().getCountryId()); }
		 * 
		 * if (!ArgUtil.isEmpty(ssoUser.getUserClient())) {
		 * requestMeta.setTerminalId(ssoUser.getUserClient().getTerminalId()); }
		 * 
		 * if (!ArgUtil.isEmpty(branchSession) &&
		 * !ArgUtil.isEmpty(branchSession.getCustomerId())) {
		 * requestMeta.setCustomerId(branchSession.getCustomerId()); }
		 */
	}

}
