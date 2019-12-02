package com.amx.jax.branchremittance.manager;

import static com.amx.amxlib.constant.ApplicationProcedureParam.P_DELIVERY_MODE_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_REMITTANCE_MODE_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_ROUTING_BANK_BRANCH_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_ROUTING_BANK_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_ROUTING_COUNTRY_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_SERVICE_MASTER_ID;
import static com.amx.jax.error.JaxError.COMISSION_NOT_DEFINED_FOR_ROUTING_BANK;
import static com.amx.jax.error.JaxError.TOO_MANY_COMISSION_NOT_DEFINED_FOR_ROUTING_BANK;

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

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.branchremittance.service.BranchRemittanceExchangeRateService;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.ExchangeRateProcedureDao;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.IRemittanceApplicationParams;
import com.amx.jax.model.response.remittance.DeliveryModeDto;
import com.amx.jax.model.response.remittance.RemittanceModeDto;
import com.amx.jax.model.response.remittance.RoutingBankDto;
import com.amx.jax.model.response.remittance.RoutingBranchDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.RoutingServiceDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
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

	@Autowired
	private ExchangeRateProcedureDao exchangeRateProcedureDao;

	@Autowired
	private BizcomponentDao bizcomponentDao;

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
			LOGGER.debug("IMPS logic applicable for remittanceApplicationParams: {}",
					remittanceApplicationParams.toString());
			remitApplParametersMap.putAll(impsOutputParams);
			routingResponseDto.getRoutingBankDto().clear();
			routingResponseDto.getRoutingBankBranchDto().clear();
			routingResponseDto.getRemittanceModeList().clear();
			routingResponseDto.getDeliveryModeList().clear();
			routingResponseDto.getServiceList().clear();
			BigDecimal routingBankBranchIdImps = P_ROUTING_BANK_BRANCH_ID.getValue(impsOutputParams);
			BigDecimal remittanceModeId = P_REMITTANCE_MODE_ID.getValue(impsOutputParams);
			BigDecimal deliveryModeId = P_DELIVERY_MODE_ID.getValue(impsOutputParams);
			BigDecimal serviceId = P_SERVICE_MASTER_ID.getValue(impsOutputParams);
			BigDecimal routingCountryId = P_ROUTING_COUNTRY_ID.getValue(impsOutputParams);
			RoutingBankDto routingBankDto = branchRoutingManager.getRoutingBankDto(routingBankIdImps);
			RoutingBranchDto routingBankBranchIdDto = branchRoutingManager.getRoutingBranchDto(routingBankBranchIdImps);
			RemittanceModeDto remittanceModeIdDto = branchRoutingManager.getRemittanceModeDto(remittanceModeId,
					metaData.getLanguageId());
			DeliveryModeDto deliveryModeIdDto = branchRoutingManager.getDeliveryModeDto(deliveryModeId,
					metaData.getLanguageId());
			RoutingServiceDto serviceDto = branchRoutingManager.getServiceDto(serviceId);
			ResourceDTO routingCountryDTO = branchRoutingManager.getRoutingCountryDto(routingCountryId);

			routingResponseDto.getRoutingBankDto().add(routingBankDto);
			routingResponseDto.getRoutingBankBranchDto().add(routingBankBranchIdDto);
			routingResponseDto.getRemittanceModeList().add(remittanceModeIdDto);
			routingResponseDto.getDeliveryModeList().add(deliveryModeIdDto);
			routingResponseDto.getServiceList().add(serviceDto);
			routingResponseDto.getRoutingCountrydto().add(routingCountryDTO);
			impsApplicable = true;
		}
		return impsApplicable;
	}

	public Map<String, Object> recalculateDeliveryAndRemittanceModeId(BranchRemittanceGetExchangeRateResponse result,
			RoutingResponseDto branchRoutingDto) {

		Map<String, Object> outputMap = null;
		if (result.getExRateBreakup() != null && result.getExRateBreakup().getConvertedFCAmount() != null) {

			BigDecimal custtype = bizcomponentDao.findCustomerTypeId("I");
			remitApplParametersMap.put("P_CUSTYPE_ID", custtype);
			remitApplParametersMap.put("P_ROUTING_BANK_BRANCH_ID",
					branchRoutingDto.getRoutingBankBranchDto().get(0).getBankBranchId());
			remitApplParametersMap.put("P_SERVICE_MASTER_ID",
					branchRoutingDto.getServiceList().get(0).getServiceMasterId());

			outputMap = exchangeRateProcedureDao.findRemittanceAndDevlieryModeId(remitApplParametersMap);
			if (outputMap.size() == 0) {
				remitApplParametersMap.put("P_CUSTYPE_ID", new BigDecimal(777));
				outputMap = exchangeRateProcedureDao.findRemittanceAndDevlieryModeId(remitApplParametersMap);
			}
			if (outputMap.size() > 2) {
				throw new GlobalException(TOO_MANY_COMISSION_NOT_DEFINED_FOR_ROUTING_BANK,
						"TOO MANY COMMISSION DEFINED for rounting bankid: "
								+ remitApplParametersMap.get("P_ROUTING_BANK_ID"));
			}

			if (outputMap.get("P_DELIVERY_MODE_ID") == null) {
				throw new GlobalException(COMISSION_NOT_DEFINED_FOR_ROUTING_BANK,
						"COMMISSION NOT DEFINED BankId: " + remitApplParametersMap.get("P_ROUTING_BANK_ID"));
			}
			remitApplParametersMap.putAll(outputMap);
		}
		return outputMap;
	}

}
