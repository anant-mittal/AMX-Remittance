package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import com.amx.amxlib.model.FlexFieldDto;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsViewx;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleMap;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.meta.MetaData;
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
	// @Qualifier("remitApplParametersMap")
	private Map<String, Object> remitApplParametersMap;

	@Autowired
	private BankService bankService;

	public List<AdditionalInstructionData> createAdditionalInstnData(RemittanceApplication remittanceApplication,
			RemittanceTransactionRequestModel remittanceTransactionRequestModel) {

		logger.info(" Enter into saveAdditionalInstnData ");

		BigDecimal applicationCountryId = metaData.getCountryId();
		List<AdditionalInstructionData> lstAddInstrData = new ArrayList<AdditionalInstructionData>();
		AdditionalInstructionData additionalInsData = null;
		AdditionalRuleDataParamer[] parameterList = {
				new AdditionalRuleDataParamer("P_ADDITIONAL_BANK_RULE_ID_1", "P_AMIEC_CODE_1", "P_FLEX_FIELD_VALUE_1",
						ConstantDocument.INDIC1),
				new AdditionalRuleDataParamer("P_ADDITIONAL_BANK_RULE_ID_2", "P_AMIEC_CODE_2", "P_FLEX_FIELD_VALUE_2",
						ConstantDocument.INDIC2),
				new AdditionalRuleDataParamer("P_ADDITIONAL_BANK_RULE_ID_3", "P_AMIEC_CODE_3", "P_FLEX_FIELD_VALUE_3",
						ConstantDocument.INDIC3),
				new AdditionalRuleDataParamer("P_ADDITIONAL_BANK_RULE_ID_4", "P_AMIEC_CODE_4", "P_FLEX_FIELD_VALUE_4",
						ConstantDocument.INDIC4),
				new AdditionalRuleDataParamer("P_ADDITIONAL_BANK_RULE_ID_5", "P_AMIEC_CODE_5", "P_FLEX_FIELD_VALUE_5",
						ConstantDocument.INDIC5) };
		Map<String, FlexFieldDto> flexFields = remittanceTransactionRequestModel.getFlexFieldDtoMap();
		flexFields.forEach((k, v) -> {
			BigDecimal bankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
			BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
			BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
			BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
			AdditionalBankDetailsViewx additionaBnankDetail = bankService.getAdditionalBankDetail(v.getSrlId(),
					foreignCurrencyId, bankId, remittanceModeId, deliveryModeId);
			if (additionaBnankDetail != null) {
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

		AdditionalBankRuleMap additionalBank = new AdditionalBankRuleMap();
		additionalBank.setAdditionalBankRuleId(additionalBankRuleId);
		additionalInsData.setAdditionalBankFieldsId(additionalBank);
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
