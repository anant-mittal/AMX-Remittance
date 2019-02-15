package com.amx.jax.branchremittance.manager;

import java.math.BigDecimal;
import java.util.HashMap;
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
import com.amx.jax.branchremittance.service.BranchRemittanceExchangeRateService;
import com.amx.jax.model.request.remittance.IRemittanceApplicationParams;
import com.amx.jax.model.response.remittance.RoutingBankDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.routing.ImpsRoutingLogic;
import static com.amx.amxlib.constant.ApplicationProcedureParam.*;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchImpsRoutingManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(BranchImpsRoutingManager.class);

	@Autowired
	ImpsRoutingLogic impsRoutingLogic;
	@Autowired
	BranchRemittanceExchangeRateService branchRemittanceExchangeRateService;
	@Resource
	Map<String, Object> remitApplParametersMap;
	@Autowired
	BranchRoutingManager branchRoutingManager;

	/**
	 * modifies routingResponseDto passed in method argument if IMPS routing is
	 * applicable for given remittance Application Parameter
	 * 
	 * @param RoutingResponseDto
	 *            - routing response dto object
	 * @param remittanceApplicationParams
	 *            - remittance application parameters
	 */
	public void checkAndapplyImpsRouting(RoutingResponseDto routingResponseDto, IRemittanceApplicationParams remittanceApplicationParams) {
		// call getexchange rate method to populated remitApplParametersMap
		branchRemittanceExchangeRateService.getExchaneRate(remittanceApplicationParams);
		Map<String, Object> impsOutputParams = new HashMap<>();
		impsRoutingLogic.apply(remitApplParametersMap, impsOutputParams);
		BigDecimal routingBankIdImps = P_ROUTING_BANK_ID.getValue(impsOutputParams);
		if (routingBankIdImps != null) {
			routingResponseDto.getRoutingBankDto().clear();
			routingResponseDto.getRoutingBankBranchDto().clear();
			routingResponseDto.getRemittanceModeList().clear();
			routingResponseDto.getDeliveryModeList().clear();
			routingResponseDto.getServiceList().clear();
			LOGGER.debug("IMPS logic applicable for remittanceApplicationParams: {}", remittanceApplicationParams.toString());
			// routing bank change
			RoutingBankDto routingBankDto = branchRoutingManager.getRoutingBankDto(routingBankIdImps);
			routingResponseDto.getRoutingBankDto().add(routingBankDto);

		}
	}
}
