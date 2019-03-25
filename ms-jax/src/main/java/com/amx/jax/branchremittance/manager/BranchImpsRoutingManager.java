package com.amx.jax.branchremittance.manager;

import static com.amx.amxlib.constant.ApplicationProcedureParam.P_DELIVERY_MODE_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_REMITTANCE_MODE_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_ROUTING_BANK_BRANCH_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_ROUTING_BANK_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_SERVICE_MASTER_ID;

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

import com.amx.jax.branchremittance.service.BranchRemittanceExchangeRateService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.IRemittanceApplicationParams;
import com.amx.jax.model.response.remittance.DeliveryModeDto;
import com.amx.jax.model.response.remittance.RemittanceModeDto;
import com.amx.jax.model.response.remittance.RoutingBankDto;
import com.amx.jax.model.response.remittance.RoutingBranchDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.RoutingServiceDto;
import com.amx.jax.routing.ImpsRoutingLogic;

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
	@Autowired
	MetaData metaData;

	/**
	 * modifies routingResponseDto passed in method argument if IMPS routing is
	 * applicable for given remittance Application Parameter
	 * 
	 * @param RoutingResponseDto
	 *            - routing response dto object
	 * @param remittanceApplicationParams
	 *            - remittance application parameters
	 */
	public boolean checkAndApplyImpsRouting(RoutingResponseDto routingResponseDto,
			IRemittanceApplicationParams remittanceApplicationParams) {
		Map<String, Object> impsOutputParams = new HashMap<>();
		boolean impsApplicable = false;
		impsRoutingLogic.apply(remitApplParametersMap, impsOutputParams);
		BigDecimal routingBankIdImps = P_ROUTING_BANK_ID.getValue(impsOutputParams);
		if (routingBankIdImps != null) {
			LOGGER.debug("IMPS logic applicable for remittanceApplicationParams: {}", remittanceApplicationParams.toString());
			routingResponseDto.getRoutingBankDto().clear();
			routingResponseDto.getRoutingBankBranchDto().clear();
			routingResponseDto.getRemittanceModeList().clear();
			routingResponseDto.getDeliveryModeList().clear();
			routingResponseDto.getServiceList().clear();
			BigDecimal routingBankBranchIdImps = P_ROUTING_BANK_BRANCH_ID.getValue(impsOutputParams);
			BigDecimal remittanceModeId = P_REMITTANCE_MODE_ID.getValue(impsOutputParams);
			BigDecimal deliveryModeId = P_DELIVERY_MODE_ID.getValue(impsOutputParams);
			BigDecimal serviceId = P_SERVICE_MASTER_ID.getValue(impsOutputParams);
			RoutingBankDto routingBankDto = branchRoutingManager.getRoutingBankDto(routingBankIdImps);
			RoutingBranchDto routingBankBranchIdDto = branchRoutingManager.getRoutingBranchDto(routingBankBranchIdImps);
			RemittanceModeDto remittanceModeIdDto = branchRoutingManager.getRemittanceModeDto(remittanceModeId, metaData.getLanguageId());
			DeliveryModeDto deliveryModeIdDto = branchRoutingManager.getDeliveryModeDto(deliveryModeId, metaData.getLanguageId());
			RoutingServiceDto serviceDto = branchRoutingManager.getServiceDto(serviceId);
			routingResponseDto.getRoutingBankDto().add(routingBankDto);
			routingResponseDto.getRoutingBankBranchDto().add(routingBankBranchIdDto);
			routingResponseDto.getRemittanceModeList().add(remittanceModeIdDto);
			routingResponseDto.getDeliveryModeList().add(deliveryModeIdDto);
			routingResponseDto.getServiceList().add(serviceDto);
			impsApplicable = true;
		}
		return impsApplicable;
	}
}
