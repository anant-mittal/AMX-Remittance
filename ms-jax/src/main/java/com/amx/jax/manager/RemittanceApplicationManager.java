package com.amx.jax.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ExchangeRateBreakup;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.ApplicationProcedureDao;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsView;
import com.amx.jax.dbmodel.remittance.BankBranch;
import com.amx.jax.dbmodel.remittance.DeliveryMode;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceModeMaster;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.services.BankService;
import com.amx.jax.util.DateUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemittanceApplicationManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	MetaData metaData;

	@Autowired
	CompanyService companyService;

	@Autowired
	IDocumentDao documentDao;

	@Autowired
	FinancialService finanacialService;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	private ApplicationProcedureDao applicationProcedureDao;

	@Resource
	private Map<String, Object> remitApplParametersMap;

	@Autowired
	private BankService bankService;

	/**
	 * @param validatedObjects:
	 *            - contains objects obtained after being passed through beneficiary
	 *            validation process, validationResults- validation result like
	 *            exchange rate, net amount etc
	 * @return
	 **/
	public RemittanceApplication createRemittanceApplication(RemittanceTransactionRequestModel requestModel,
			Map<String, Object> validatedObjects, RemittanceTransactionResponsetModel validationResults) {

		RemittanceApplication remittanceApplication = new RemittanceApplication();

		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		@SuppressWarnings("unchecked")
		Map<String, Object> routingDetails = (Map<String, Object>) validatedObjects.get("ROUTINGDETAILS");
		BigDecimal routingCountryId = (BigDecimal) routingDetails.get("P_ROUTING_COUNTRY_ID");
		Customer customer = (Customer) validatedObjects.get("CUSTOMER");
		BigDecimal routingBankId = (BigDecimal) routingDetails.get("P_ROUTING_BANK_ID");
		BigDecimal routingBankBranchId = (BigDecimal) routingDetails.get("P_ROUTING_BANK_BRANCH_ID");
		BenificiaryListView beneDetails = (BenificiaryListView) validatedObjects.get("BENEFICIARY");
		BigDecimal foreignCurrencyId = beneDetails.getCurrencyId();
		BigDecimal deliveryId = (BigDecimal) routingDetails.get("P_DELIVERY_MODE_ID");
		BigDecimal remittanceId = (BigDecimal) routingDetails.get("P_REMITTANCE_MODE_ID");
		Document document = documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_REMITTANCE_APPLICATION)
				.get(0);

		remitApplParametersMap.put("P_DOCUMENT_ID", document.getDocumentID());
		remitApplParametersMap.put("P_DOCUMENT_CODE", document.getDocumentCode());
		remittanceApplication.setExDocument(document);
		remittanceApplication.setDocumentCode(document.getDocumentCode());
		CountryMaster appCountryId = new CountryMaster();
		appCountryId.setCountryId(metaData.getCountryId());
		remittanceApplication.setFsCountryMasterByApplicationCountryId(appCountryId);
		CurrencyMasterModel localCurrency = new CurrencyMasterModel();
		CurrencyMasterModel foreignCurrency = new CurrencyMasterModel();
		foreignCurrency.setCurrencyId(foreignCurrencyId);
		localCurrency.setCurrencyId(localCurrencyId);
		remittanceApplication.setExCurrencyMasterByForeignCurrencyId(foreignCurrency);
		// commission currency
		remittanceApplication.setExCurrencyMasterByLocalCommisionCurrencyId(localCurrency);
		// local currency
		remittanceApplication.setExCurrencyMasterByLocalTranxCurrencyId(localCurrency);
		// local charge currency
		remittanceApplication.setExCurrencyMasterByLocalChargeCurrencyId(localCurrency);
		// net amt currency
		remittanceApplication.setExCurrencyMasterByLocalNetCurrencyId(localCurrency);
		remittanceApplication.setSpotRateInd(ConstantDocument.No);
		remittanceApplication
				.setLoyaltyPointInd(requestModel.isAvailLoyalityPoints() ? ConstantDocument.Yes : ConstantDocument.No);
		// company Id and code
		CompanyMaster companymaster = new CompanyMaster();
		companymaster.setCompanyId(metaData.getCompanyId());
		remittanceApplication.setFsCompanyMaster(companymaster);
		ViewCompanyDetails companyDetails = companyService.getCompanyDetailsById(metaData.getCompanyId());
		remittanceApplication.setCompanyCode(companyDetails.getCompanyCode());
		// branch id
		CountryBranch countryBranch = new CountryBranch();
		countryBranch.setCountryBranchId(metaData.getCountryBranchId());
		remittanceApplication.setLoccod(metaData.getCountryBranchId());
		remittanceApplication.setExCountryBranch(countryBranch);
		// fin year
		UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
		remittanceApplication.setExUserFinancialYearByDocumentFinanceYear(userFinancialYear);
		remitApplParametersMap.put("P_USER_FINANCIAL_YEAR", userFinancialYear.getFinancialYear());
		// routing Country
		CountryMaster bencountrymaster = new CountryMaster();
		bencountrymaster.setCountryId(routingCountryId);
		remittanceApplication.setFsCountryMasterByBankCountryId(bencountrymaster);

		// Delivery Mode from service
		DeliveryMode deliverymode = new DeliveryMode();

		deliverymode.setDeliveryModeId(deliveryId);
		remittanceApplication.setExDeliveryMode(deliverymode);

		// RemittanceModeMaster to get Remittance
		RemittanceModeMaster remittancemode = new RemittanceModeMaster();
		remittancemode.setRemittanceModeId(remittanceId);
		remittanceApplication.setExRemittanceMode(remittancemode);

		// Customer id
		remittanceApplication.setFsCustomer(customer);
		remittanceApplication.setCustomerRef(customer.getCustomerReference());

		// Routing Bank
		BankMasterModel bankmaster = new BankMasterModel();
		bankmaster.setBankId(routingBankId);
		remittanceApplication.setExBankMaster(bankmaster);

		// Routing Bank Branch
		BankBranch bankbranch = new BankBranch();
		bankbranch.setBankBranchId(routingBankBranchId);
		remittanceApplication.setExBankBranch(bankbranch);
		// document date
		remittanceApplication.setDocumentDate(new Date());
		remittanceApplication.setDebitAccountNo(beneDetails.getBankAccountNumber());
		// rates
		setApplicableRates(remittanceApplication, requestModel, validationResults);
		remittanceApplication.setDocumentFinancialyear(userFinancialYear.getFinancialYear());
		remittanceApplication.setSelectedCurrencyId(foreignCurrencyId);

		try {
			remittanceApplication
					.setAccountMmyyyy(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));
		} catch (ParseException e) {
			logger.error("Error in saving application", e);
		}
		remittanceApplication.setCreatedBy(customer.getIdentityInt());
		remittanceApplication.setCreatedDate(new Date());
		remittanceApplication.setIsactive(ConstantDocument.Yes);
		remittanceApplication.setSourceofincome(requestModel.getSourceOfFund());
		remittanceApplication.setApplInd(ConstantDocument.Individual);
		remittanceApplication.setDocumentNo(generateDocumentNumber());
		validateAdditionalErrorMessages(requestModel);
		remittanceApplication.setInstruction("URGENT");
		return remittanceApplication;
	}

	private void validateAdditionalErrorMessages(RemittanceTransactionRequestModel requestModel) {
		Map<String, Object> errorResponse = applicationProcedureDao
				.toFetchPurtherInstractionErrorMessaage(remitApplParametersMap);
		String errorMessage = (String) errorResponse.get("P_ERRMSG");
		Map<String, Object> furtherSwiftAdditionalDetails = applicationProcedureDao
				.fetchAdditionalBankRuleIndicators(remitApplParametersMap);
		remitApplParametersMap.putAll(furtherSwiftAdditionalDetails);
		remitApplParametersMap.put("P_ADDITIONAL_BANK_RULE_ID_1", requestModel.getAdditionalBankRuleFiledId());
		if (requestModel.getSrlId() != null) {
			BigDecimal srlId = requestModel.getSrlId();
			AdditionalBankDetailsView additionaBnankDetail = bankService.getAdditionalBankDetail(srlId);
			if (additionaBnankDetail != null) {
				remitApplParametersMap.put("P_AMIEC_CODE_1", additionaBnankDetail.getAmiecCode());
				remitApplParametersMap.put("P_FLEX_FIELD_VALUE_1", additionaBnankDetail.getAmieceDescription());
				remitApplParametersMap.put("P_FLEX_FIELD_CODE_1", additionaBnankDetail.getFlexField());
			}
		}
		if (remitApplParametersMap.get("P_ADDITIONAL_BANK_RULE_ID_1") == null) {
			errorMessage = "Additional Field required by bank not set";
		}
		if (StringUtils.isNotBlank(errorMessage)) {
			//throw new GlobalException(errorMessage, JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL);
			//TODO: check why error is coming from s.p.
		}
	}

	private BigDecimal generateDocumentNumber() {
		BigDecimal appCountryId = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal documentId = (BigDecimal) remitApplParametersMap.get("P_DOCUMENT_ID");
		BigDecimal finYear = (BigDecimal) remitApplParametersMap.get("P_USER_FINANCIAL_YEAR");
		BigDecimal branchId = metaData.getCountryBranchId();
		Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(appCountryId, companyId, documentId,
				finYear, "U", branchId);
		return (BigDecimal) output.get("P_DOC_NO");
	}

	private void setApplicableRates(RemittanceApplication remittanceApplication,
			RemittanceTransactionRequestModel requestModel, RemittanceTransactionResponsetModel validationResults) {
		ExchangeRateBreakup breakup = validationResults.getExRateBreakup();
		BigDecimal loyalityPointsEncashed = null;
		if (requestModel.isAvailLoyalityPoints()) {
			loyalityPointsEncashed = validationResults.getMaxLoyalityPointsAvailableForTxn();
		}
		remittanceApplication.setForeignTranxAmount(breakup.getConvertedFCAmount());
		remittanceApplication.setLocalTranxAmount(breakup.getConvertedLCAmount());
		remittanceApplication.setExchangeRateApplied(breakup.getRate());
		remittanceApplication.setLocalCommisionAmount(validationResults.getTxnFee());
		remittanceApplication.setLocalChargeAmount(null);
		remittanceApplication.setLocalDeliveryAmount(BigDecimal.ZERO);
		remittanceApplication.setLocalNetTranxAmount(breakup.getNetAmount());
		remittanceApplication.setLoyaltyPointsEncashed(loyalityPointsEncashed);

	}
}
