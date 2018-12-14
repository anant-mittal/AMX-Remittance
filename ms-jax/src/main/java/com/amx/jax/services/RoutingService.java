package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.config.JaxProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.RoutingProcedureDao;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.meta.ServiceMaster;
import com.amx.jax.error.JaxError;
import com.amx.jax.routing.IRoutingLogic;
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
	@Autowired
	List<IRoutingLogic> routingLogics;
	@Autowired
	JaxProperties jaxProperties;

	public Map<String, Object> getRoutingDetails(Map<String, Object> inputValue) {
		Map<String, Object> output;
		String serviceGroupCode = inputValue.get("P_SERVICE_GROUP_CODE").toString();
		BigDecimal beneAccountSeqId = (BigDecimal) inputValue.get("P_BENEFICARY_ACCOUNT_SEQ_ID");
		if (ConstantDocument.SERVICE_GROUP_CODE_CASH.equals(serviceGroupCode)) {
			output = new HashMap<>();
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
				inputValue.put("P_ROUTING_BANK_BRANCH_ID", routingBankBranchId);
			}
			output.put("P_REMITTANCE_MODE_ID", routingProcedureDao.getRemittanceModeIdForCash(inputValue));
			inputValue.putAll(output);
			output.put("P_DELIVERY_MODE_ID", routingProcedureDao.getDeliveryModeIdForCash(inputValue));
		} else {
			// banking
			output = getRoutingDetail(inputValue);
			inputValue.putAll(output);
		}
		inputValue.putAll(output);
		checkRemittanceAndDeliveryMode(inputValue);
		return output;
	}

	public Map<String, Object> getRoutingDetail(Map<String, Object> inputValue) {
		if (jaxProperties.getRoutingProcOthDisable()) {
			return applicationProcedureDao.getRoutingDetails(inputValue);
		} else if (jaxProperties.getExrateBestRateLogicEnable()) {
			return applicationProcedureDao.getRoutingDetailFromOthRateProcedure(inputValue);
		} else {
			return applicationProcedureDao.getRoutingDetailFromOthProcedure(inputValue);
		}
	}

	private void checkRemittanceAndDeliveryMode(Map<String, Object> inputValue) {
		if (inputValue.get("P_REMITTANCE_MODE_ID") == null) {
			throw new GlobalException(JaxError.REMITTANCE_SERVICE_NOT_AVAILABLE, "Service not available");
		}
		if (inputValue.get("P_DELIVERY_MODE_ID") == null) {
			throw new GlobalException(JaxError.REMITTANCE_SERVICE_NOT_AVAILABLE, "Service not available");
		}
	}

	public void recalculateRemittanceAndDeliveryMode(Map<String, Object> inputValue) {
		String serviceGroupCode = inputValue.get("P_SERVICE_GROUP_CODE").toString();
		if (!ConstantDocument.SERVICE_GROUP_CODE_CASH.equals(serviceGroupCode)) {

			routingLogics.forEach(i -> {
				if (i.isApplicable()) {
					Map<String, Object> output = new HashMap<>();
					i.apply(inputValue, output);
					inputValue.putAll(output);
				}
			});
		}
	}
}
