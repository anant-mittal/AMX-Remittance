package com.amx.jax.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.meta.ServiceMaster;
import com.amx.jax.service.MetaService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RoutingService {

	@Autowired
	ApplicationProcedureDao applicationProcedureDao;
	@Autowired
	MetaService metaService;

	public Map<String, Object> getRoutingDetails(HashMap<String, Object> inputValue) {
		String serviceGroupCode = inputValue.get("P_SERVICE_GROUP_CODE").toString();
		if (ConstantDocument.SERVICE_GROUP_CODE_CASH.equals(serviceGroupCode)) {
			Map<String, Object> output = new HashMap<>();
			ServiceMaster serviceGroupMaster = metaService.getServiceMaster(serviceGroupCode).get(0);
			output.put("P_SERVICE_MASTER_ID", serviceGroupMaster.getServiceId());
			output.put("P_ROUTING_COUNTRY_ID", inputValue.get("P_BENEFICIARY_COUNTRY_ID"));
			return null;
		} else {
			// banking
			return applicationProcedureDao.getRoutingDetails(inputValue);
		}
	}
}
