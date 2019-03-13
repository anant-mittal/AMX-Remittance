package com.amx.jax.remittance.manager;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceParameterMapManager {

	@Resource
	private Map<String, Object> remitApplParametersMap;

	public boolean isCashChannel() {
		Object serviceGroupCode = remitApplParametersMap.get("P_SERVICE_GROUP_CODE");
		boolean isCashGroup = false;
		if (serviceGroupCode != null) {
			isCashGroup = ConstantDocument.SERVICE_GROUP_CODE_CASH.equals(serviceGroupCode);
		}
		return isCashGroup;
	}
}
