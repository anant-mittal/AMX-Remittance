package com.amx.jax.serviceprovider.venteja;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.dbmodel.bene.RelationsDescription;
import com.amx.jax.dbmodel.partner.TransactionDetailsView;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.request.partner.RemittanceTransactionPartnerDTO;
import com.amx.jax.model.request.partner.SrvProvBeneficiaryTransactionDTO;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;
import com.amx.jax.partner.dao.PartnerTransactionDao;
import com.amx.jax.partner.dto.BeneficiaryDetailsDTO;
import com.amx.jax.partner.manager.PartnerTransactionManager;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE;
import com.amx.jax.repository.IRemittanceTransactionDao;
import com.amx.jax.repository.ParameterDetailsRespository;
import com.amx.jax.serviceprovider.service.ServiceProviderApiManager;
import com.amx.jax.userservice.repository.RelationsRepository;
import com.amx.jax.util.AmxDBConstants;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class VentajaManager extends AbstractModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	RelationsRepository relationsRepository;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	PartnerTransactionManager partnerTransactionManager;
	
	@Autowired
	ParameterDetailsRespository parameterDetailsRespository;
	
	@Autowired
	PartnerTransactionDao partnerTransactionDao;
	
	@Autowired
	IRemittanceTransactionDao remittanceTransactionDao;
	
	@Autowired
	ApplicationContext appContext;
	
	// fetch member type // 0 – OFW  //  1 – Self-employed // 2 – Voluntary
	public String fetchMemberType(BigDecimal customerId,BigDecimal beneficiaryRelationShipId) {
		String memberType = "0";
		List<RelationsDescription> selfId = relationsRepository.findByRelationsCodeAndLangId(AmxDBConstants.SELF_STR,metaData.getLanguageId());
		if(selfId != null && selfId.size() == 1) {
			RelationsDescription relationsDescription = selfId.get(0);
			BeneficiaryDetailsDTO beneficiaryDetailsDTO = partnerTransactionManager.fetchBeneficiaryDetails(customerId, beneficiaryRelationShipId);
			
			if(beneficiaryDetailsDTO != null) {
				if(beneficiaryDetailsDTO.getRelationShipId() != null && beneficiaryDetailsDTO.getRelationShipId().compareTo(relationsDescription.getRelationsId()) == 0) {
					memberType = "0";
				}else {
					memberType = "2";
				}
			}
		}
		
		logger.info("Member Type" + memberType + " customerId " + customerId + " beneficiaryRelationShipId " + beneficiaryRelationShipId);
		
		return memberType;
	}
	
	// fetch amount based on product
	public void fetchAmountDetailsByProduct(String recordId,String paramCodeDef) {
		parameterDetailsRespository.findByRecordIdAndParamCodeDefAndIsActive(recordId, paramCodeDef, AmxDBConstants.Yes);
	}
	
	// save api for ventaja
	public Map<BigDecimal, Remittance_Call_Response> callVentajaPartnerApi(RemittanceResponseDto responseDto) {
		Remittance_Call_Response response = null;
		Map<String, Object> remitParametersMap = new HashMap<>();
		Map<BigDecimal, Remittance_Call_Response> allRemitParametersMap = new HashMap<>();
		List<String> dupcheck = new ArrayList<>();
		
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal collectionDocYear = responseDto.getCollectionDocumentFYear();
		BigDecimal collectionDocNumber = responseDto.getCollectionDocumentNo();
		BigDecimal collectionDocCode = responseDto.getCollectionDocumentCode();
		
		List<TransactionDetailsView> lstTrnxDetails = partnerTransactionDao.fetchTrnxSPDetails(customerId,collectionDocYear,collectionDocNumber,collectionDocCode);
		
		for (TransactionDetailsView transactionDetailsView : lstTrnxDetails) {
			if(transactionDetailsView.getBankCode().equalsIgnoreCase(SERVICE_PROVIDER_BANK_CODE.VINTJA.name())) {
				String docYearNo = transactionDetailsView.getDocumentFinanceYear().toString()+transactionDetailsView.getDocumentNo();
				if(!dupcheck.contains(docYearNo)) {
					dupcheck.add(docYearNo);
					
					remitParametersMap.put("P_APPLICATION_COUNTRY_ID", transactionDetailsView.getApplicationCountryId());
					remitParametersMap.put("P_ROUTING_COUNTRY_ID", transactionDetailsView.getBankCountryId());
					remitParametersMap.put("P_REMITTANCE_MODE_ID", transactionDetailsView.getRemittanceId());
					remitParametersMap.put("P_DELIVERY_MODE_ID", transactionDetailsView.getDeliveryId());
					remitParametersMap.put("P_FOREIGN_CURRENCY_ID", transactionDetailsView.getForeignCurrencyId());
					remitParametersMap.put("P_ROUTING_BANK_ID", transactionDetailsView.getBankId());
					remitParametersMap.put("P_BENE_BANK_COUNTRY_ID", transactionDetailsView.getBeneficiaryCountryId());
					remitParametersMap.put("P_BENEFICIARY_RELASHIONSHIP_ID", transactionDetailsView.getBeneficiaryRelationShipId());
					
					Map<String, FlexFieldDto> flexFieldDtoMap = fetchTransactionWiseRecords(transactionDetailsView);
					remitParametersMap.put("flexFieldDtoMap", flexFieldDtoMap);
					
					try {
						ServiceProviderApiManager serviceProviderApiManager = (ServiceProviderApiManager) appContext.getBean(transactionDetailsView.getBankCode());
						response = serviceProviderApiManager.sendRemittance(remitParametersMap);
						allRemitParametersMap.put(transactionDetailsView.getDocumentNo(), response);
					} catch (NoSuchBeanDefinitionException ex) {
						// exception
					}
				}
			}
		}
		
		return allRemitParametersMap;
	}
	
	// fetch the transaction document no wise records
	public Map<String, FlexFieldDto> fetchTransactionWiseRecords(TransactionDetailsView transactionDetailsView) {
		Map<String, FlexFieldDto> flexFieldDtoMap = new HashMap<>();
		List<TransactionDetailsView> lstTrnxDetails = partnerTransactionDao.fetchTrnxWiseDetailsForCustomer(transactionDetailsView.getCustomerId(), transactionDetailsView.getDocumentFinanceYear(),
				transactionDetailsView.getDocumentNo(), transactionDetailsView.getCollDocumentFinanceYear(), transactionDetailsView.getCollDocumentNo(), transactionDetailsView.getCollDocumentCode());
		
		for (TransactionDetailsView transactionDetails : lstTrnxDetails) {
			flexFieldDtoMap.put(transactionDetails.getFlexField(), new FlexFieldDto(transactionDetails.getFlexFieldValue()));
		}
		
		return flexFieldDtoMap;
	}

}
