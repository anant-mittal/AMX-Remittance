package com.amx.jax.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxDynamicField;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.dbmodel.remittance.AdditionalBankDetailsViewx;
import com.amx.jax.dbmodel.remittance.BankBranch;
import com.amx.jax.dbmodel.remittance.DeliveryMode;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceModeMaster;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.AbstractRemittanceApplicationRequestModel;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionDrRequestModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.remittance.FlexFieldDto;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.pricer.dto.ExchangeDiscountInfo;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.IDocumentDao;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.service.CompanyService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.LoyalityPointService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.JsonUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemittanceApplicationManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LoyalityPointService loyalityPointService;

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

	@Autowired
	private BeneficiaryService beneficiaryService;

	@Autowired
	private BankMetaService bankMetaService;

	@Autowired
	ICurrencyDao currencyDao;

	/**
	 * @param remitApplParametersMap2
	 * @param                         validatedObjects: - contains objects obtained
	 *                                after being passed through beneficiary
	 *                                validation process, validationResults-
	 *                                validation result like exchange rate, net
	 *                                amount etc
	 * @return
	 **/
	public RemittanceApplication createRemittanceApplication(RemittanceTransactionRequestModel requestModel,
			Map<String, Object> validatedObjects, RemittanceTransactionResponsetModel validationResults,
			Map<String, Object> remitApplParametersMap) {
		RemittanceApplication remittanceApplication = new RemittanceApplication();

		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		Customer customer = (Customer) validatedObjects.get("CUSTOMER");
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		BigDecimal routingBankBranchId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_BRANCH_ID");
		BenificiaryListView beneDetails = (BenificiaryListView) validatedObjects.get("BENEFICIARY");
		BigDecimal foreignCurrencyId = beneDetails.getCurrencyId();
		BigDecimal deliveryId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal remittanceId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		Document document = documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_REMITTANCE_APPLICATION)
				.get(0);
		BigDecimal selectedCurrency = getSelectedCurrency(foreignCurrencyId, requestModel);

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
		remittanceApplication.setSourceofincome(requestModel.getSourceOfFund());
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
		remittanceApplication.setLoyaltyPointInd(
				loyalityPointsAvailed(requestModel, validationResults) ? ConstantDocument.Yes : ConstantDocument.No);
		// company Id and code
		CompanyMaster companymaster = new CompanyMaster();
		companymaster.setCompanyId(metaData.getCompanyId());
		remittanceApplication.setFsCompanyMaster(companymaster);
		ViewCompanyDetails companyDetails = companyService.getCompanyDetailsById(metaData.getCompanyId());
		remittanceApplication.setCompanyCode(companyDetails.getCompanyCode());
		// branch id
		CountryBranch countryBranch = bankMetaService.getCountryBranchById((metaData.getCountryBranchId()));
		remittanceApplication.setLoccod(metaData.getCountryBranchId());
		remittanceApplication.setExCountryBranch(countryBranch);
		// fin year
		UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
		remittanceApplication.setExUserFinancialYearByDocumentFinanceYear(userFinancialYear);
		remittanceApplication.setTransactionFinancialyear(userFinancialYear.getFinancialYear());
		remittanceApplication.setDocumentFinancialyear(userFinancialYear.getFinancialYear());
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
		logger.info("Created by Refereal :" + metaData.getReferrer() + "\t Device ID :" + metaData.getDeviceId()
				+ "\t Device Type :" + metaData.getDeviceType() + "\t App type :" + metaData.getAppType());
		if (!StringUtils.isBlank(metaData.getReferrer())) {
			remittanceApplication.setCreatedBy(metaData.getReferrer());
		} else {
			if (!StringUtils.isBlank(metaData.getAppType())) {
				remittanceApplication.setCreatedBy(metaData.getAppType());
			} else {
				remittanceApplication.setCreatedBy("WEB");
			}
		}
		remittanceApplication.setCreatedDate(new Date());
		remittanceApplication.setIsactive(ConstantDocument.Yes);
		remittanceApplication.setSourceofincome(requestModel.getSourceOfFund());
		remittanceApplication.setApplInd(ConstantDocument.Online);
		remittanceApplication.setDocumentNo(
				generateDocumentNumber(remittanceApplication.getExCountryBranch(), ConstantDocument.Update));
		remittanceApplication.setPaymentId(remittanceApplication.getDocumentNo().toString());
		remittanceApplication.setWuIpAddress(metaData.getDeviceIp());
		
		validateAdditionalErrorMessages(requestModel);
		validateBannedBank();
		validateDailyBeneficiaryTransactionLimit(beneDetails);

		setFurtherInstruction(remittanceApplication, requestModel.getAdditionalFields());

		/*
		 * if(requestModel.getAdditionalFields()!=null &&
		 * requestModel.getAdditionalFields().get("INSTRUCTION")!=null) { //INSTRUCTION
		 * remittanceApplication.setInstruction(requestModel.getAdditionalFields().get(
		 * "INSTRUCTION").toString()); }else {
		 * remittanceApplication.setInstruction("URGENT"); }
		 */
		setCustomerDiscountColumns(remittanceApplication, validationResults);
		setVatDetails(remittanceApplication, validationResults);
		return remittanceApplication;
	}
	

	public RemittanceApplication createRemittanceApplicationV2(RemittanceTransactionDrRequestModel requestModel,
			Map<String, Object> validatedObjects, RemittanceTransactionResponsetModel validationResults,
			Map<String, Object> remitApplParametersMap) {
		RemittanceApplication remittanceApplication = new RemittanceApplication();

		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		BigDecimal routingCountryId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_COUNTRY_ID");
		Customer customer = (Customer) validatedObjects.get("CUSTOMER");
		BigDecimal routingBankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		BigDecimal routingBankBranchId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_BRANCH_ID");
		BenificiaryListView beneDetails = (BenificiaryListView) validatedObjects.get("BENEFICIARY");
		BigDecimal foreignCurrencyId = beneDetails.getCurrencyId();
		BigDecimal deliveryId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
		BigDecimal remittanceId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
		Document document = documentDao.getDocumnetByCode(ConstantDocument.DOCUMENT_CODE_FOR_REMITTANCE_APPLICATION).get(0);
		BigDecimal selectedCurrency = getSelectedCurrencyV2(foreignCurrencyId, requestModel);

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
		remittanceApplication.setSourceofincome(requestModel.getSourceOfFund());
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
		remittanceApplication.setLoyaltyPointInd(loyalityPointsAvailed(requestModel, validationResults) ? ConstantDocument.Yes : ConstantDocument.No);
		// company Id and code
		CompanyMaster companymaster = new CompanyMaster();
		companymaster.setCompanyId(metaData.getCompanyId());
		remittanceApplication.setFsCompanyMaster(companymaster);
		ViewCompanyDetails companyDetails = companyService.getCompanyDetailsById(metaData.getCompanyId());
		remittanceApplication.setCompanyCode(companyDetails.getCompanyCode());
		// branch id
		CountryBranch countryBranch = bankMetaService.getCountryBranchById((metaData.getCountryBranchId()));
		remittanceApplication.setLoccod(metaData.getCountryBranchId());
		remittanceApplication.setExCountryBranch(countryBranch);
		// fin year
		UserFinancialYear userFinancialYear = finanacialService.getUserFinancialYear();
		remittanceApplication.setExUserFinancialYearByDocumentFinanceYear(userFinancialYear);
		remittanceApplication.setTransactionFinancialyear(userFinancialYear.getFinancialYear());
		remittanceApplication.setDocumentFinancialyear(userFinancialYear.getFinancialYear());
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
		setApplicableRatesV2(remittanceApplication, requestModel, validationResults);
		remittanceApplication.setDocumentFinancialyear(userFinancialYear.getFinancialYear());
		remittanceApplication.setSelectedCurrencyId(foreignCurrencyId);
		try {
			remittanceApplication.setAccountMmyyyy(new SimpleDateFormat("dd/MM/yyyy").parse(DateUtil.getCurrentAccMMYear()));
		} catch (ParseException e) {
			logger.error("Error in saving application", e);
		}
		logger.info("Created by Refereal :"+metaData.getReferrer()+"\t Device ID :"+metaData.getDeviceId()+"\t Device Type :"+metaData.getDeviceType()+"\t App type :"+metaData.getAppType());
		if(!StringUtils.isBlank(metaData.getReferrer())){
			remittanceApplication.setCreatedBy(metaData.getReferrer());
		}else{
			if(!StringUtils.isBlank(metaData.getAppType())){				
				remittanceApplication.setCreatedBy(metaData.getAppType());
			}else{
				remittanceApplication.setCreatedBy("WEB");
			 }
		}
		remittanceApplication.setCreatedDate(new Date());
		remittanceApplication.setIsactive(ConstantDocument.Yes);
		remittanceApplication.setSourceofincome(requestModel.getSourceOfFund());
		remittanceApplication.setApplInd(ConstantDocument.Online);
		remittanceApplication.setDocumentNo(generateDocumentNumber(remittanceApplication.getExCountryBranch(), ConstantDocument.Update));
		remittanceApplication.setPaymentId(remittanceApplication.getDocumentNo().toString());
		remittanceApplication.setWuIpAddress(metaData.getDeviceIp());
		
		DynamicRoutingPricingDto dynamicRoutingPricingResponse = requestModel.getDynamicRroutingPricingBreakup();
		if(dynamicRoutingPricingResponse.getServiceProviderDto() != null && dynamicRoutingPricingResponse.getServiceProviderDto().getIntialAmountInSettlCurr() != null) {
			remittanceApplication.setUsdAmt(dynamicRoutingPricingResponse.getServiceProviderDto().getIntialAmountInSettlCurr());
			if(remittanceApplication.getOriginalExchangeRate() != null && remittanceApplication.getOriginalExchangeRate().compareTo(BigDecimal.ZERO) != 0) {
				// getting original rate
				logger.info("SP Original Exchange Rate : "+JsonUtil.toJson(validationResults.getExRateBreakup()));
			}else {
				logger.info("Unable to get Original Exchange Rate : "+JsonUtil.toJson(validationResults.getExRateBreakup()));
				throw new GlobalException("Unable to get Original Exchange Rate");
			}
		}
		
		validateAdditionalErrorMessagesV2(requestModel);
		validateBannedBank();
		validateDailyBeneficiaryTransactionLimit(beneDetails);
		// remittanceApplication.setInstruction("URGENT");

		setFurtherInstruction(remittanceApplication, requestModel.getAdditionalFields());

		setCustomerDiscountColumns(remittanceApplication, validationResults);
		setVatDetails(remittanceApplication, validationResults);
		return remittanceApplication;
	}
	
	
	

	public void setCustomerDiscountColumns(RemittanceApplication remittanceApplication,
			RemittanceTransactionResponsetModel remittanceTransactionResponsetModel) {
		remittanceApplication.setIsDiscountAvailed(
				Boolean.TRUE.equals(remittanceTransactionResponsetModel.getDiscountAvailed()) ? ConstantDocument.Yes
						: ConstantDocument.No);
		Map<DISCOUNT_TYPE, ExchangeDiscountInfo> customerDiscoutDetails = remittanceTransactionResponsetModel
				.getCustomerDiscountDetails();
		if (customerDiscoutDetails != null && !customerDiscoutDetails.isEmpty()) {
			remittanceApplication
					.setCusCatDiscountId(customerDiscoutDetails.get(DISCOUNT_TYPE.CUSTOMER_CATEGORY).getId());
			remittanceApplication.setCusCatDiscount(
					customerDiscoutDetails.get(DISCOUNT_TYPE.CUSTOMER_CATEGORY).getDiscountPipsValue());
			remittanceApplication.setChannelDiscountId(customerDiscoutDetails.get(DISCOUNT_TYPE.CHANNEL).getId());
			remittanceApplication
					.setChannelDiscount(customerDiscoutDetails.get(DISCOUNT_TYPE.CHANNEL).getDiscountPipsValue());
			String pips = customerDiscoutDetails.get(DISCOUNT_TYPE.AMOUNT_SLAB).getDiscountTypeValue();
			if (!StringUtils.isBlank(pips)) {
				String[] parts = pips.split("-");
				remittanceApplication.setPipsFromAmt(parts[0] == null ? new BigDecimal(0) : new BigDecimal(parts[0]));
				remittanceApplication.setPipsToAmt(parts[1] == null ? new BigDecimal(0) : new BigDecimal(parts[1]));
			}
			remittanceApplication
					.setPipsDiscount(customerDiscoutDetails.get(DISCOUNT_TYPE.AMOUNT_SLAB).getDiscountPipsValue());
		}
	}

	
	
	public void setVatDetails(RemittanceApplication remittanceApplication,RemittanceTransactionResponsetModel remittanceTransactionResponsetModel) {
		remittanceApplication.setVatType(remittanceTransactionResponsetModel.getVatType());
		remittanceApplication.setVatPercentage(remittanceTransactionResponsetModel.getVatPercentage());
		remittanceApplication.setVatAmount(remittanceTransactionResponsetModel.getVatAmount());
	}
	
	private BigDecimal getSelectedCurrency(BigDecimal foreignCurrencyId,RemittanceTransactionRequestModel requestModel) {
		if (requestModel.getForeignAmount() != null) {
			return foreignCurrencyId;
		}
		return metaData.getDefaultCurrencyId();
	}

	
	private BigDecimal getSelectedCurrencyV2(BigDecimal foreignCurrencyId,RemittanceTransactionDrRequestModel requestModel) {
		if (requestModel.getForeignAmount() != null) {
			return foreignCurrencyId;
		}
		return metaData.getDefaultCurrencyId();
	}

	private void validateDailyBeneficiaryTransactionLimit(BenificiaryListView beneDetails) {
		Integer todaysTxns = beneficiaryService.getTodaysTransactionForBene(metaData.getCustomerId(),
				beneDetails.getBeneficaryMasterSeqId());
		if (todaysTxns > 0) {
			throw new GlobalException(JaxError.NO_OF_TRANSACTION_LIMIT_EXCEEDED,
					"Dear Customer, you have already done 1 application to this beneficiary within the last 24"
							+ " hours. In the interest of safety, we do not allow a customer to repeat the same"
							+ " transaction to the same beneficiary more than once in 24 hours."
							+ " Kindly logout and login to make a new application for the same details");
		}
	}

	private void validateBannedBank() {
		Map<String, Object> output = applicationProcedureDao.getBannedBankCheckProcedure(remitApplParametersMap);
		String errorMessage = (String) output.get("P_ERROR_MESSAGE");
		if (errorMessage != null) {
			throw new GlobalException(JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL, errorMessage);
		}
	}

	public void validateAdditionalErrorMessages(RemittanceTransactionRequestModel requestModel) {
		remitApplParametersMap.put("P_FURTHER_INSTR", "URGENT");
		Map<String, Object> errorResponse = applicationProcedureDao
				.toFetchPurtherInstractionErrorMessaage(remitApplParametersMap);
		String errorMessage = (String) errorResponse.get("P_ERRMSG");
		Map<String, Object> furtherSwiftAdditionalDetails = applicationProcedureDao
				.fetchAdditionalBankRuleIndicators(remitApplParametersMap);
		remitApplParametersMap.putAll(furtherSwiftAdditionalDetails);
		remitApplParametersMap.put("P_ADDITIONAL_BANK_RULE_ID_1", requestModel.getAdditionalBankRuleFiledId());
		if (requestModel.getSrlId() != null) {
			BigDecimal srlId = requestModel.getSrlId();
			logger.info("Srl Id received: " + srlId);
			BigDecimal bankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
			BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
			BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
			BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
			logger.info("bankId: " + bankId + "remittanceModeId: " + remittanceModeId + "deliveryModeId "
					+ deliveryModeId + " foreignCurrencyId: " + foreignCurrencyId);
			AdditionalBankDetailsViewx additionaBnankDetail = bankService.getAdditionalBankDetail(srlId,
					foreignCurrencyId, bankId, remittanceModeId, deliveryModeId);
			if (additionaBnankDetail != null) {
				logger.info("additionaBnankDetail getServiceApplicabilityRuleId: "
						+ additionaBnankDetail.getServiceApplicabilityRuleId());
				remitApplParametersMap.put("P_AMIEC_CODE_1", additionaBnankDetail.getAmiecCode());
				remitApplParametersMap.put("P_FLEX_FIELD_VALUE_1", additionaBnankDetail.getAmieceDescription());
				remitApplParametersMap.put("P_FLEX_FIELD_CODE_1", additionaBnankDetail.getFlexField());
			}
		}
		if (remitApplParametersMap.get("P_ADDITIONAL_BANK_RULE_ID_1") == null) {
			errorMessage = "Additional Field required by bank not set";
		}
		if (StringUtils.isNotBlank(errorMessage)) {
			throw new GlobalException(JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL, errorMessage);
		}
	}

	
	public void validateAdditionalErrorMessagesV2(RemittanceTransactionDrRequestModel requestModel) {
		remitApplParametersMap.put("P_FURTHER_INSTR", "URGENT");
		Map<String, Object> errorResponse = applicationProcedureDao
				.toFetchPurtherInstractionErrorMessaage(remitApplParametersMap);
		String errorMessage = (String) errorResponse.get("P_ERRMSG");
		Map<String, Object> furtherSwiftAdditionalDetails = applicationProcedureDao
				.fetchAdditionalBankRuleIndicators(remitApplParametersMap);
		remitApplParametersMap.putAll(furtherSwiftAdditionalDetails);
		remitApplParametersMap.put("P_ADDITIONAL_BANK_RULE_ID_1", requestModel.getAdditionalBankRuleFiledId());
		if (requestModel.getSrlId() != null) {
			BigDecimal srlId = requestModel.getSrlId();
			logger.info("Srl Id received: " + srlId);
			BigDecimal bankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
			BigDecimal remittanceModeId = (BigDecimal) remitApplParametersMap.get("P_REMITTANCE_MODE_ID");
			BigDecimal deliveryModeId = (BigDecimal) remitApplParametersMap.get("P_DELIVERY_MODE_ID");
			BigDecimal foreignCurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
			logger.info("bankId: " + bankId + "remittanceModeId: " + remittanceModeId + "deliveryModeId "
					+ deliveryModeId + " foreignCurrencyId: " + foreignCurrencyId);
			AdditionalBankDetailsViewx additionaBnankDetail = bankService.getAdditionalBankDetail(srlId,
					foreignCurrencyId, bankId, remittanceModeId, deliveryModeId);
			if (additionaBnankDetail != null) {
				logger.info("additionaBnankDetail getServiceApplicabilityRuleId: "
						+ additionaBnankDetail.getServiceApplicabilityRuleId());
				remitApplParametersMap.put("P_AMIEC_CODE_1", additionaBnankDetail.getAmiecCode());
				remitApplParametersMap.put("P_FLEX_FIELD_VALUE_1", additionaBnankDetail.getAmieceDescription());
				remitApplParametersMap.put("P_FLEX_FIELD_CODE_1", additionaBnankDetail.getFlexField());
			}
		}
		if (remitApplParametersMap.get("P_ADDITIONAL_BANK_RULE_ID_1") == null) {
			errorMessage = "Additional Field required by bank not set";
		}
		if (StringUtils.isNotBlank(errorMessage)) {
			throw new GlobalException(JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL, errorMessage);
		}
	}
	

	public BigDecimal generateDocumentNumber(CountryBranch countryBranch, String processInd) {
		BigDecimal appCountryId = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal documentId = (BigDecimal) remitApplParametersMap.get("P_DOCUMENT_ID");
		BigDecimal finYear = (BigDecimal) remitApplParametersMap.get("P_USER_FINANCIAL_YEAR");
		BigDecimal branchId = countryBranch.getBranchId();
		Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(appCountryId, companyId, documentId,
				finYear, processInd, branchId);
		BigDecimal docno = output.get("P_DOC_NO") == null ? BigDecimal.ZERO : (BigDecimal) output.get("P_DOC_NO");

		if (JaxUtil.isNullZeroBigDecimalCheck(docno)) {
			return docno;
		} else {
			throw new GlobalException(JaxError.INVALID_APPLICATION_DOCUMENT_NO, "Document seriality is missing");
		}
	}

	private void setApplicableRates(RemittanceApplication remittanceApplication,
			RemittanceTransactionRequestModel requestModel, RemittanceTransactionResponsetModel validationResults) {
		ExchangeRateBreakup breakup = validationResults.getExRateBreakup();
		BigDecimal loyalityPointsEncashed = BigDecimal.ZERO;
		if (loyalityPointsAvailed(requestModel, validationResults)) {
			loyalityPointsEncashed = validationResults.getLoyalityAmountAvailableForTxn();
		}
		remittanceApplication.setForeignTranxAmount(breakup.getConvertedFCAmount());
		remittanceApplication.setLocalTranxAmount(breakup.getConvertedLCAmount());
		remittanceApplication.setExchangeRateApplied(breakup.getInverseRate());
		remittanceApplication.setLocalCommisionAmount(validationResults.getTxnFee());
		remittanceApplication.setDiscountOnCommission(validationResults.getDiscountOnComission());
		remittanceApplication.setLocalChargeAmount(BigDecimal.ZERO);
		remittanceApplication.setLocalDeliveryAmount(BigDecimal.ZERO);
		remittanceApplication.setLocalNetTranxAmount(breakup.getNetAmountWithoutLoyality());
		remittanceApplication.setLoyaltyPointsEncashed(loyalityPointsEncashed);
		if(JaxUtil.isNullZeroBigDecimalCheck(breakup.getBaseRate())) {
			remittanceApplication.setOriginalExchangeRate(breakup.getBaseRate());
		}
	}

	private void setApplicableRatesV2(RemittanceApplication remittanceApplication,
			RemittanceTransactionDrRequestModel requestModel, RemittanceTransactionResponsetModel validationResults) {
		ExchangeRateBreakup breakup = validationResults.getExRateBreakup();
		BigDecimal loyalityPointsEncashed = BigDecimal.ZERO;
		if (loyalityPointsAvailed(requestModel, validationResults)) {
			loyalityPointsEncashed = validationResults.getLoyalityAmountAvailableForTxn();
		}
		remittanceApplication.setForeignTranxAmount(breakup.getConvertedFCAmount());
		remittanceApplication.setLocalTranxAmount(breakup.getConvertedLCAmount());
		remittanceApplication.setExchangeRateApplied(breakup.getInverseRate());
		remittanceApplication.setLocalCommisionAmount(validationResults.getTxnFee());
		remittanceApplication.setDiscountOnCommission(validationResults.getDiscountOnComission());
		remittanceApplication.setLocalChargeAmount(BigDecimal.ZERO);
		remittanceApplication.setLocalDeliveryAmount(BigDecimal.ZERO);
		remittanceApplication.setLocalNetTranxAmount(breakup.getNetAmountWithoutLoyality());
		remittanceApplication.setLoyaltyPointsEncashed(loyalityPointsEncashed);
		if(JaxUtil.isNullZeroBigDecimalCheck(breakup.getBaseRate())) {
			remittanceApplication.setOriginalExchangeRate(breakup.getBaseRate());
		}
	}

	/**
	 * whether customer has availed loyality points or not
	 * 
	 * @param requestModel
	 * @param responseModel
	 * @return
	 * 
	 */
	public Boolean loyalityPointsAvailed(AbstractRemittanceApplicationRequestModel requestModel,
			RemittanceTransactionResponsetModel responseModel) {
		if (requestModel.isAvailLoyalityPoints() && responseModel.getCanRedeemLoyalityPoints() != null
				&& responseModel.getCanRedeemLoyalityPoints()) {
			return true;
		}
		return false;
	}

	public void setIntermediateSwiftBank(RemittanceAppBenificiary remittanceAppBenificary,
			 BranchRemittanceApplRequestModel applRequestModel) {
		applRequestModel.populateAdditionalFieldsDtoMap();
		Map<String, FlexFieldDto> requestAddlFields = applRequestModel.getAdditionalDtoMap();
		if (requestAddlFields != null && !requestAddlFields.isEmpty()) {

			requestAddlFields.forEach((k, v) -> {
				if (k.equalsIgnoreCase(JaxDynamicField.BENEFICIARY_SWIFT_BANK1.toString())) {
					remittanceAppBenificary.setBeneficiarySwiftBank1Id(v.getSrlId());
					remittanceAppBenificary.setBeneficiarySwiftAddr1(v.getAmieceDescription());
				}
				if (k.equalsIgnoreCase(JaxDynamicField.BENEFICIARY_SWIFT_BANK2.toString())) {
					remittanceAppBenificary.setBeneficiarySwiftBank2Id(v.getSrlId());
					remittanceAppBenificary.setBeneficiarySwiftAddr2(v.getAmieceDescription());
				}

			});

			
		}
	}

	public void setFurtherInstruction(RemittanceApplication remittanceApplication,
			Map<String, Object> additionalFields) {
		if (additionalFields != null && !additionalFields.isEmpty()) {
			if (additionalFields.get(JaxDynamicField.INSTRUCTION.toString()) != null)
				remittanceApplication
						.setInstruction(additionalFields.get(JaxDynamicField.INSTRUCTION.toString()).toString());
		} else {
			remittanceApplication.setInstruction("URGENT");
		}
	}

}
