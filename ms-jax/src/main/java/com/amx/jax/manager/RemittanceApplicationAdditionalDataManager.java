package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsViewx;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dbmodel.remittance.FlexFiledView;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.services.BankService;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemittanceApplicationAdditionalDataManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	MetaData metaData;

	@Resource
	private Map<String, Object> remitApplParametersMap;

	@Autowired
	private BankService bankService;
	
	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;
	
	

	public List<AdditionalInstructionData> createAdditionalInstnData(RemittanceApplication remittanceApplication,
			RemittanceTransactionRequestModel remittanceTransactionRequestModel) {

		logger.info(" Enter into saveAdditionalInstnData ");

		BigDecimal applicationCountryId = metaData.getCountryId();
		List<AdditionalInstructionData> lstAddInstrData = new ArrayList<AdditionalInstructionData>();
		Map<String, FlexFieldDto> flexFields = remittanceTransactionRequestModel.getFlexFieldDtoMap();
		flexFields.forEach((k, v) -> {
			BigDecimal bankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
			BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
			BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
			BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
			
			if (v.getSrlId() != null) {
				AdditionalBankDetailsViewx additionaBnankDetail = bankService.getAdditionalBankDetail(v.getSrlId(),
						foreignCurrencyId, bankId, remittanceModeId, deliveryModeId);
				AdditionalInstructionData additionalInsDataTmp = createAdditionalIndicatorsData(remittanceApplication,
						applicationCountryId, k, additionaBnankDetail.getAmiecCode(),
						additionaBnankDetail.getAmieceDescription(), v.getAdditionalBankRuleFiledId());
				lstAddInstrData.add(additionalInsDataTmp);
			} else {
				AdditionalInstructionData additionalInsDataTmp = createAdditionalIndicatorsData(remittanceApplication,
						applicationCountryId, k, ConstantDocument.AMIEC_CODE, v.getAmieceDescription(),
						v.getAdditionalBankRuleFiledId());
				lstAddInstrData.add(additionalInsDataTmp);
			}
		});

		logger.info(" Exit from saveAdditionalInstnData ");

		return lstAddInstrData;
	}

	// checking Indic1,Indic2,Indic3,Indic4,Indic5
	private AdditionalInstructionData createAdditionalIndicatorsData(RemittanceApplication remittanceApplication,
			BigDecimal applicationCountryId, String indicatorCode, String amiecCode, String flexFieldValue,
			BigDecimal additionalBankRuleId) {

		logger.info(" Enter into fetchIndicatorsData :" + flexFieldValue);

		AdditionalInstructionData additionalInsData = new AdditionalInstructionData();

		// document Id
		Document document = new Document();
		document.setDocumentID(remittanceApplication.getExDocument().getDocumentID());
		additionalInsData.setExDocument(document);

		// company Id
		CompanyMaster companymaster = new CompanyMaster();
		companymaster.setCompanyId(remittanceApplication.getFsCompanyMaster().getCompanyId());
		additionalInsData.setFsCompanyMaster(companymaster);

		// Application Country
		CountryMaster countrymaster = new CountryMaster();
		countrymaster.setCountryId(applicationCountryId);
		additionalInsData.setFsCountryMaster(countrymaster);

		if (additionalBankRuleId != null) {
			AdditionalBankRuleMap additionalBank = new AdditionalBankRuleMap();
			additionalBank.setAdditionalBankRuleId(additionalBankRuleId);
			additionalInsData.setAdditionalBankFieldsId(additionalBank);
		}
		additionalInsData.setFlexField(indicatorCode);
		additionalInsData.setFlexFieldValue(flexFieldValue);
		if (amiecCode != null) {
			additionalInsData.setAmiecCode(amiecCode);
		} else {
			additionalInsData.setAmiecCode(ConstantDocument.AMIEC_CODE);
			// additionalInsData.setFlexFieldValue("ONLINE TEST");
		}

		additionalInsData.setExRemittanceApplication(remittanceApplication);
		additionalInsData.setExUserFinancialYear(remittanceApplication.getExUserFinancialYearByDocumentFinanceYear());
		additionalInsData.setDocumentFinanceYear(remittanceApplication.getDocumentFinancialyear());

		additionalInsData.setCreatedBy(remittanceApplication.getCreatedBy());
		additionalInsData.setCreatedDate(new Date());
		additionalInsData.setIsactive(ConstantDocument.Yes);
		additionalInsData.setDocumentNo(remittanceApplication.getDocumentNo());

		logger.info(" Exit from fetchIndicatorsData ");

		return additionalInsData;
	}
	
	
	
	public List<AdditionalInstructionData> createAdditionalInstnDataForBranch(RemittanceApplication remittanceApplication, Map<String, Object> remitApplParaMap) {

		logger.info(" Enter into saveAdditionalInstnData ");

		BigDecimal applicationCountryId = metaData.getCountryId();
		
		BranchRemittanceApplRequestModel remittanceTransactionRequestModel =(BranchRemittanceApplRequestModel)remitApplParaMap.get("APPL_REQ_MODEL");
		Map<String,Object> remitApplExchMap =(HashMap)remitApplParaMap.get("EXCH_RATE_MAP");
		Map<String,Object> routingSetupDetails =(HashMap)remitApplParaMap.get("ROUTING_DETAILS_MAP");
		BenificiaryListView beneDetails = (BenificiaryListView) remitApplParaMap.get("BENEFICIARY_DETAILS");
		
		remittanceTransactionRequestModel.populateFlexFieldDtoMap();
		
		
		
		List<FlexFiledView> allFlexFields = remittanceApplicationDao.getFlexFields();
		Map<String, FlexFieldDto> requestFlexFields = remittanceTransactionRequestModel.getFlexFieldDtoMap();
		if (requestFlexFields == null) {
			requestFlexFields = new HashMap<>();
			remittanceTransactionRequestModel.setFlexFieldDtoMap(requestFlexFields);
		}
		requestFlexFields.put("INDIC1",new FlexFieldDto(remittanceTransactionRequestModel.getAdditionalBankRuleFiledId(), remittanceTransactionRequestModel.getSrlId(), null));
		
		
		List<AdditionalInstructionData> lstAddInstrData = new ArrayList<AdditionalInstructionData>();
		//Map<String, FlexFieldDto> requestFlexFields = remittanceTransactionRequestModel.getFlexFieldDtoMap();
		requestFlexFields.forEach((k, v) -> {
			BigDecimal bankId = (BigDecimal) routingSetupDetails.get("P_ROUTING_BANK_ID");
			BigDecimal remittanceModeId = (BigDecimal) remitApplExchMap.get("P_REMITTANCE_MODE_ID");
			BigDecimal deliveryModeId = (BigDecimal) remitApplExchMap.get("P_DELIVERY_MODE_ID");
			BigDecimal foreignCurrencyId = beneDetails.getCurrencyId();
			
			if (v.getSrlId() != null) {
				AdditionalBankDetailsViewx additionaBnankDetail = bankService.getAdditionalBankDetail(v.getSrlId(),foreignCurrencyId, bankId, remittanceModeId, deliveryModeId);
				AdditionalInstructionData additionalInsDataTmp = createAdditionalIndicatorsData(remittanceApplication,applicationCountryId, k, additionaBnankDetail.getAmiecCode(),additionaBnankDetail.getAmieceDescription(), v.getAdditionalBankRuleFiledId());
				lstAddInstrData.add(additionalInsDataTmp);
			} else {
				AdditionalInstructionData additionalInsDataTmp = createAdditionalIndicatorsData(remittanceApplication,applicationCountryId, k, ConstantDocument.AMIEC_CODE, v.getAmieceDescription(),v.getAdditionalBankRuleFiledId());
				lstAddInstrData.add(additionalInsDataTmp);
			}
		});

		logger.info(" Exit from saveAdditionalInstnData ");

		return lstAddInstrData;
	}
	

	class AdditionalRuleDataParamer {
		String pruleId;
		String pamieCode;
		String pflexField;
		String indic;

		public AdditionalRuleDataParamer(String pruleId, String pamieCode, String pflexField, String indic) {
			super();
			this.pruleId = pruleId;
			this.pamieCode = pamieCode;
			this.pflexField = pflexField;
			this.indic = indic;
		}

	}
}
