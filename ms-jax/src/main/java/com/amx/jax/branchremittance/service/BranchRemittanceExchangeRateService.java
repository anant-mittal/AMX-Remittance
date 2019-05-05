/**
 * 
 */
package com.amx.jax.branchremittance.service;

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

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branchremittance.manager.BranchImpsRoutingManager;
import com.amx.jax.branchremittance.manager.BranchRemittanceExchangeRateManager;
import com.amx.jax.branchremittance.manager.BranchRoutingManager;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.manager.remittance.RemittanceApplicationParamManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.IRemittanceApplicationParams;
import com.amx.jax.model.request.remittance.RoutingPricingRequest;
import com.amx.jax.model.response.remittance.BranchExchangeRateBreakup;
import com.amx.jax.model.response.remittance.DeliveryModeDto;
import com.amx.jax.model.response.remittance.FlexFieldReponseDto;
import com.amx.jax.model.response.remittance.RemittanceModeDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.model.response.remittance.branch.DynamicRoutingPricingResponse;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.remittance.IViewRemittanceMode;
import com.amx.jax.util.JaxUtil;

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
			
			
			
			 if(routingResponseDto != null && routingResponseDto.getRoutingCountrydto()!=null  && !routingResponseDto.getRoutingCountrydto().isEmpty() ) {
				 String countryCode = routingResponseDto.getRoutingCountrydto().get(0).getResourceCode()==null?"":routingResponseDto.getRoutingCountrydto().get(0).getResourceCode(); 
				 BigDecimal serviceMasterId = requestApplModel.getServiceMasterId();
				 if(!JaxUtil.isNullZeroBigDecimalCheck(serviceMasterId)) {
					 serviceMasterId =routingResponseDto.getServiceList().get(0)==null?BigDecimal.ZERO:routingResponseDto.getServiceList().get(0).getServiceMasterId();
				 }
				 
				 if(!StringUtils.isBlank(countryCode) && countryCode.equals(ConstantDocument.IND_COUNTRY_CODE) && serviceMasterId.compareTo(ConstantDocument.SERVICE_MASTER_ID_TT)==0) {
				 if(routingResponseDto.getRemittanceModeList()!=null && routingResponseDto.getRemittanceModeList().get(0).getRemittancCode().compareTo(ConstantDocument.IMPS_CODE)!=0) {
					 Map<String, Object> outPut = branchImpsRoutingManager.recalculateDeliveryAndRemittanceModeId(result,routingResponseDto);
					 BigDecimal newRemitMode = outPut.get("P_REMITTANCE_MODE_ID")==null?requestApplModel.getRemittanceModeId():(BigDecimal)outPut.get("P_REMITTANCE_MODE_ID");
					 BigDecimal newDeliveryMode = outPut.get("P_DELIVERY_MODE_ID")==null?requestApplModel.getDeliveryModeId():(BigDecimal)outPut.get("P_DELIVERY_MODE_ID");
				 
					 LOGGER.info("newRemitMode :"+newRemitMode+"\t Old :"+requestApplModel.getRemittanceModeId());
					 LOGGER.info("newDeliveryMode :"+newDeliveryMode+"\t Old :"+requestApplModel.getDeliveryModeId());
					 if(JaxUtil.isNullZeroBigDecimalCheck(newRemitMode)) {
						 RemittanceModeDto remittanceModeIdDto = branchRoutingManager.getRemittanceModeDto(newRemitMode, metaData.getLanguageId());
						 routingResponseDto.getRemittanceModeList().clear();
						 routingResponseDto.getRemittanceModeList().add(remittanceModeIdDto);
					 }
					 if(JaxUtil.isNullZeroBigDecimalCheck(newDeliveryMode)) {
						 DeliveryModeDto deliveryModeIdDto = branchRoutingManager.getDeliveryModeDto(newDeliveryMode, metaData.getLanguageId());
						 routingResponseDto.getDeliveryModeList().clear();
						 routingResponseDto.getDeliveryModeList().add(deliveryModeIdDto);
					 }
				  }
				 }
				 
			 }
		}
		/** for IMPS **/
		result.setRoutingResponseDto(routingResponseDto);
		Object flexFields = branchRemittanceExchangeRateManager.fetchFlexFields(request);
		return AmxApiResponse.build(result, flexFields);
	}

	
	public AmxApiResponse<DynamicRoutingPricingResponse, Object> getDynamicRoutingAndPricingResponse(RoutingPricingRequest routingPricingRequest){
		DynamicRoutingPricingResponse result = branchRemittanceExchangeRateManager.getDynamicRoutingAndPricingResponse(routingPricingRequest);
		AmxApiResponse resopnse = AmxApiResponse.build(result);
		return resopnse;
	}
	
	
	public AmxApiResponse<FlexFieldReponseDto, Object> getFlexField(IRemittanceApplicationParams request){
		branchRemittanceExchangeRateManager.validateGetExchangRateRequest(request);
		Object obj = branchRemittanceExchangeRateManager.fetchFlexFields(request);
		AmxApiResponse resopnse = AmxApiResponse.build(obj,null);
		return resopnse;
	}
	
	
}
