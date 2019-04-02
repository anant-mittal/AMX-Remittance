/**
 * 
 */
package com.amx.jax.branchremittance.service;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.ApplicationProcedureParam;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branchremittance.manager.BranchImpsRoutingManager;
import com.amx.jax.branchremittance.manager.BranchRemittanceExchangeRateManager;
import com.amx.jax.branchremittance.manager.BranchRoutingManager;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.IRemittanceApplicationParams;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;

/**
 * @author Prashant
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BranchRemittanceExchangeRateService {

	static final Logger LOGGER = LoggerFactory.getLogger(BranchRemittanceExchangeRateService.class);

	@Autowired
	BranchRemittanceExchangeRateManager branchRemittanceExchangeRateManager;
	@Autowired
	BranchRoutingManager branchRoutingManager;
	@Autowired
	BranchImpsRoutingManager branchImpsRoutingManager;
	@Resource
	Map<String, Object> remitApplParametersMap;

	public AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object> getExchaneRate(IRemittanceApplicationParams request) {
		branchRemittanceExchangeRateManager.validateGetExchangRateRequest(request);
		BranchRemittanceGetExchangeRateResponse result = branchRemittanceExchangeRateManager.getExchangeRateResponse(request);
		RoutingResponseDto routingResponseDto = new RoutingResponseDto();
		boolean isImpsApplicable = branchImpsRoutingManager.checkAndApplyImpsRouting(routingResponseDto, request);
		if (!isImpsApplicable) {
			BranchRemittanceApplRequestModel requestApplModel = new BranchRemittanceApplRequestModel();
			requestApplModel.setBeneId(request.getBeneficiaryRelationshipSeqIdBD());
			requestApplModel.setServiceMasterId(request.getServiceIndicatorIdBD());
			requestApplModel.setRemittanceModeId(request.getRemitModeIdBD());
			requestApplModel.setBeneId(request.getBeneficiaryRelationshipSeqIdBD());
			routingResponseDto = branchRoutingManager.getRoutingSetup(requestApplModel);
		}
		result.setRoutingResponseDto(routingResponseDto);
		Object flexFields = branchRemittanceExchangeRateManager.fetchFlexFields(request);
		return AmxApiResponse.build(result, flexFields);
	}
}
