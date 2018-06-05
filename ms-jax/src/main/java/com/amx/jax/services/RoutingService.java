package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.RoutingProcedureDao;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.meta.ServiceMaster;
import com.amx.jax.service.MetaService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RoutingService {

	@Autowired
	ApplicationProcedureDao applicationProcedureDao;
	@Autowired
	MetaService metaService;
	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	RoutingProcedureDao routingProcedureDao;

	public Map<String, Object> getRoutingDetails(HashMap<String, Object> inputValue) {
		String serviceGroupCode = inputValue.get("P_SERVICE_GROUP_CODE").toString();
		BigDecimal beneAccountSeqId = (BigDecimal) inputValue.get("P_BENEFICARY_ACCOUNT_SEQ_ID");
		if (ConstantDocument.SERVICE_GROUP_CODE_CASH.equals(serviceGroupCode)) {
			Map<String, Object> output = new HashMap<>();
			ServiceMaster serviceGroupMaster = metaService.getServiceMaster(serviceGroupCode).get(0);
			output.put("P_SERVICE_MASTER_ID", serviceGroupMaster.getServiceId());
			BeneficaryAccount beneAccount = beneficiaryService.getBeneAccountByAccountSeqId(beneAccountSeqId);
			output.put("P_ROUTING_COUNTRY_ID", beneAccount.getBeneficaryCountryId());
			output.put("P_ROUTING_BANK_ID", beneAccount.getServiceProviderId());
			output.put("P_ROUTING_BANK_BRANCH_ID", beneAccount.getServiceProviderBranchId());
			inputValue.putAll(output);
			BigDecimal routingBankBranchId = routingProcedureDao.getRoutingBankBranchIdForCash(inputValue);
			if (routingBankBranchId != null) {
				output.put("P_ROUTING_BANK_BRANCH_ID", routingBankBranchId);
			}
			output.put("P_REMITTANCE_MODE_ID", routingProcedureDao.getRemittanceModeIdForCash(inputValue));
			inputValue.putAll(output);
			output.put("P_DELIVERY_MODE_ID", routingProcedureDao.getDeliveryModeIdForCash(inputValue));

			return output;
		} else {
			// banking
			return applicationProcedureDao.getRoutingDetails(inputValue);
		}
	}

}