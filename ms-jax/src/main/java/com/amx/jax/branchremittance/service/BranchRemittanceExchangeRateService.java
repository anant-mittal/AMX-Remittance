/**
 * 
 */
package com.amx.jax.branchremittance.service;

import static com.amx.jax.error.JaxError.COMISSION_NOT_DEFINED_FOR_ROUTING_BANK;
import static com.amx.jax.error.JaxError.TOO_MANY_COMISSION_NOT_DEFINED_FOR_ROUTING_BANK;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.ApplicationProcedureParam;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branchremittance.manager.BranchImpsRoutingManager;
import com.amx.jax.branchremittance.manager.BranchRemittanceExchangeRateManager;
import com.amx.jax.branchremittance.manager.BranchRoutingManager;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.ExchangeRateProcedureDao;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.remittance.ViewRemittanceMode;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.IRemittanceApplicationParams;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.remittance.IViewRemittanceMode;

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
	
	@Autowired
	IViewRemittanceMode remittanceModeRepository;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	CountryRepository countryRepository;
	


	public AmxApiResponse<BranchRemittanceGetExchangeRateResponse, Object> getExchaneRate(IRemittanceApplicationParams request) {
		branchRemittanceExchangeRateManager.validateGetExchangRateRequest(request);
		BranchRemittanceGetExchangeRateResponse result = branchRemittanceExchangeRateManager.getExchangeRateResponse(request);
		RoutingResponseDto routingResponseDto = new RoutingResponseDto();
		boolean isImpsApplicable = branchImpsRoutingManager.checkAndApplyImpsRouting(routingResponseDto, request);
		if (!isImpsApplicable ) {  /** imps false **/
			BranchRemittanceApplRequestModel requestApplModel = new BranchRemittanceApplRequestModel();
			requestApplModel.setRemittanceModeId(request.getRemitModeIdBD());
			requestApplModel.setRoutingCountryId((BigDecimal)remitApplParametersMap.get("P_ROUTING_COUNTRY_ID"));
			List<CountryMasterView> countryMasterView = countryRepository.findByLanguageIdAndCountryId(metaData.getLanguageId(), requestApplModel.getRoutingCountryId());
			
			requestApplModel.setServiceMasterId(request.getServiceIndicatorIdBD());
			if(countryMasterView!=null && !countryMasterView.isEmpty()) {
				String countryCode = countryMasterView.get(0)==null?"": countryMasterView.get(0).getCountryCode();
				if(!StringUtils.isBlank(countryCode) &&  countryCode.equalsIgnoreCase(ConstantDocument.IND_COUNTRY_CODE)) {
					requestApplModel.setServiceMasterId(BigDecimal.ZERO);
				}
			}
			
			requestApplModel.setBeneId(request.getBeneficiaryRelationshipSeqIdBD());
			requestApplModel.setRoutingBankId(request.getCorrespondanceBankIdBD());
			routingResponseDto = branchRoutingManager.getRoutingSetup(requestApplModel);
		}
		/** for IMPS **/
		result.setRoutingResponseDto(routingResponseDto);
		Object flexFields = branchRemittanceExchangeRateManager.fetchFlexFields(request);
		return AmxApiResponse.build(result, flexFields);
	}
	
	

	
}
