package com.amx.jax.branchremittance.manager;

import static com.amx.amxlib.constant.ApplicationProcedureParam.P_DELIVERY_MODE_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_FOREIGN_CURRENCY_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_REMITTANCE_MODE_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_ROUTING_BANK_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_ROUTING_COUNTRY_ID;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.AdditionalFlexRequiredException;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.util.JaxValidationUtil;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerCoreDetailsView;
import com.amx.jax.dbmodel.CustomerEmploymentInfo;
import com.amx.jax.dbmodel.remittance.CorporateMasterModel;
import com.amx.jax.dbmodel.remittance.ViewVatDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.exrateservice.service.JaxDynamicPriceService;
import com.amx.jax.exrateservice.service.NewExchangeRateService;
import com.amx.jax.manager.RemittanceTransactionManager;
import com.amx.jax.manager.remittance.CorporateDiscountManager;
import com.amx.jax.manager.remittance.RemittanceAdditionalFieldManager;
import com.amx.jax.manager.remittance.RemittanceApplicationParamManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.IRemittanceApplicationParams;
import com.amx.jax.model.response.remittance.BranchExchangeRateBreakup;
import com.amx.jax.model.response.remittance.VatDetailsDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.remittance.manager.RemittanceParameterMapManager;
import com.amx.jax.repository.CustomerCoreDetailsRepository;
import com.amx.jax.repository.ICustomerEmploymentInfoRepository;
import com.amx.jax.repository.remittance.ICorporateMasterRepository;
import com.amx.jax.repository.remittance.IViewVatDetailsRespository;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.BeneficiaryValidationService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.RoundUtil;
import com.amx.jax.validation.RemittanceTransactionRequestValidator;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittanceExchangeRateManager {

	static final Logger LOGGER = LoggerFactory.getLogger(BranchRemittanceExchangeRateManager.class);

	@Autowired
	JaxDynamicPriceService jaxDynamicPriceService;
	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	MetaData metaData;
	@Autowired
	BeneficiaryValidationService beneValidationService;
	@Autowired
	RemittanceApplicationParamManager remittanceApplicationParamManager;
	@Autowired
	RemittanceTransactionManager remittanceTransactionManager;
	@Resource
	Map<String, Object> remitApplParametersMap;
	@Autowired
	UserService userService;
	@Autowired
	RemittanceAdditionalFieldManager remittanceAdditionalFieldManager;
	@Autowired
	RemittanceTransactionRequestValidator remittanceTransactionRequestValidator;
	@Autowired
	ICorporateMasterRepository corporateMasterRepository;
	
	@Autowired
	JaxTenantProperties jaxTenantProperties;
	@Autowired
	RemittanceParameterMapManager remittanceParameterMapManager;
	@Autowired
	NewExchangeRateService newExchangeRateService;
	@Autowired
	CorporateDiscountManager corporateDiscountManager;
	
	@Autowired
	IViewVatDetailsRespository vatDetailsRepository;

	public void validateGetExchangRateRequest(IRemittanceApplicationParams request) {

		if (request.getForeignAmountBD() == null && request.getLocalAmountBD() == null) {
			throw new GlobalException(JaxError.INVALID_AMOUNT, "Either local or foreign amount must be present");
		}
 		JaxValidationUtil.validatePositiveNumber(request.getForeignAmountBD(), "Foreign Amount should be positive",JaxError.INVALID_AMOUNT);
		JaxValidationUtil.validatePositiveNumber(request.getLocalAmountBD(), "Local Amount should be positive", JaxError.INVALID_AMOUNT);
		JaxValidationUtil.validatePositiveNumber(request.getCorrespondanceBankIdBD(), "corespondance bank must be positive number");
		JaxValidationUtil.validatePositiveNumber(request.getBeneficiaryRelationshipSeqIdBD(), "bene seq id bank must be positive number");
		JaxValidationUtil.validatePositiveNumber(request.getServiceIndicatorIdBD(), "service indic id bank must be positive number");
	}

	public BranchRemittanceGetExchangeRateResponse getExchangeRateResponse(IRemittanceApplicationParams request) {
		BenificiaryListView beneficiaryView = beneValidationService.validateBeneficiary(request.getBeneficiaryRelationshipSeqIdBD());
		Customer customer = userService.getCustById(metaData.getCustomerId());
		ExchangeRateResponseModel exchangeRateResponseModel = null;
		if (!beneficiaryService.isCashBene(beneficiaryView)) {
			exchangeRateResponseModel = jaxDynamicPriceService.getExchangeRatesWithDiscount(
					metaData.getDefaultCurrencyId(), beneficiaryView.getCurrencyId(), request.getLocalAmountBD(),
					request.getForeignAmountBD(), beneficiaryView.getBenificaryCountry(),
					request.getCorrespondanceBankIdBD(), request.getServiceIndicatorIdBD());
		} else {
			exchangeRateResponseModel = newExchangeRateService.getExchangeRateResponseFromAprDet(
					beneficiaryView.getCurrencyId(), request.getLocalAmountBD(), request.getForeignAmountBD(),
					request.getCorrespondanceBankIdBD(), beneficiaryView.getBenificaryCountry(),
					beneficiaryView.getApplicationCountryId(), request.getServiceIndicatorIdBD());
		}
		if (exchangeRateResponseModel.getExRateBreakup() == null) {
			throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
		}
		remittanceApplicationParamManager.populateRemittanceApplicationParamMap(request, beneficiaryView,exchangeRateResponseModel.getExRateBreakup());
		BranchRemittanceGetExchangeRateResponse result = new BranchRemittanceGetExchangeRateResponse();
		BranchExchangeRateBreakup branchExchangeRate = new BranchExchangeRateBreakup(exchangeRateResponseModel.getExRateBreakup());
		result.setExRateBreakup(branchExchangeRate);
		result.setCustomerDiscountDetails(exchangeRateResponseModel.getCustomerDiscountDetails());
		result.setDiscountAvailed(exchangeRateResponseModel.getDiscountAvailed());
		result.setCostRateLimitReached(exchangeRateResponseModel.getCostRateLimitReached());
		// trnx fee
		BigDecimal commission = getComission();
		
		LOGGER.debug("commisionValue:" +commission);
		
		VatDetailsDto vatDetails = getVatAmount(commission);
		if(vatDetails!=null && !StringUtils.isBlank(vatDetails.getVatApplicable()) && vatDetails.getVatApplicable().equalsIgnoreCase(ConstantDocument.Yes)) {
			result.setVatAmount(vatDetails.getVatAmount()==null?BigDecimal.ZERO:vatDetails.getVatAmount());
			result.setVatPercentage(vatDetails.getVatPercentage()==null?BigDecimal.ZERO:vatDetails.getVatPercentage());
			result.setVatType(vatDetails.getVatType()==null?"":vatDetails.getVatType());
			if(JaxUtil.isNullZeroBigDecimalCheck(vatDetails.getCommission())) {
				commission =vatDetails.getCommission();
			}
		}
		
		result.setTxnFee(commission);
		// loyality points
		remittanceTransactionManager.setLoyalityPointFlags(customer, result);
		remittanceTransactionManager.setLoyalityPointIndicaters(result);
		BranchRemittanceApplRequestModel remittanceApplRequestModel = buildRemittanceTransactionModel(request);
		remittanceTransactionManager.applyChannelAmountRouding(branchExchangeRate,metaData.getChannel().getClientChannel(), true);
		remittanceTransactionManager.setNetAmountAndLoyalityState(branchExchangeRate, remittanceApplRequestModel, result, commission);
		remittanceTransactionManager.applyCurrencyRoudingLogic(branchExchangeRate);
		
		return result;
	}

	private BranchRemittanceApplRequestModel buildRemittanceTransactionModel(IRemittanceApplicationParams request) {

		BranchRemittanceApplRequestModel model = new BranchRemittanceApplRequestModel();
		model.setBeneId(request.getBeneficiaryRelationshipSeqIdBD());
		model.setAvailLoyalityPoints(request.getAvailLoyalityPoints());
		model.setLocalAmount(request.getLocalAmountBD());
		model.setForeignAmount(request.getForeignAmountBD());

		return model;
	}

	private BigDecimal getComission() {
		BigDecimal routingBankId = P_ROUTING_BANK_ID.getValue(remitApplParametersMap);
		BigDecimal rountingCountryId = P_ROUTING_COUNTRY_ID.getValue(remitApplParametersMap);
		BigDecimal currencyId = P_FOREIGN_CURRENCY_ID.getValue(remitApplParametersMap);
		BigDecimal remittanceMode = P_REMITTANCE_MODE_ID.getValue(remitApplParametersMap);
		BigDecimal deliveryMode = P_DELIVERY_MODE_ID.getValue(remitApplParametersMap);
		BigDecimal commission = remittanceTransactionManager.getCommissionAmount(routingBankId, rountingCountryId, currencyId,remittanceMode, deliveryMode);
		BigDecimal newCommission = remittanceTransactionManager.reCalculateComission();
		LOGGER.debug("routingBankId:"+routingBankId+ "rountingCountryId" +rountingCountryId+ "currencyId" +currencyId+ "remittanceMode" +remittanceMode+ "deliveryMode"+deliveryMode+
				"commission" +commission+ "newCommission" +newCommission);
		
		if (newCommission != null) {
			commission = newCommission;
		}
		
		BigDecimal corpDiscount = corporateDiscountManager.corporateDiscount();
		if(JaxUtil.isNullZeroBigDecimalCheck(commission) && commission.compareTo(corpDiscount)>=0) {
			commission =commission.subtract(corpDiscount);
			LOGGER.debug("commission1"+commission);
		}
		return commission;
	}

	public Object fetchFlexFields(IRemittanceApplicationParams exchangeRateRequest) {
		BranchRemittanceApplRequestModel branchRemittanceApplRequestModel = new BranchRemittanceApplRequestModel(exchangeRateRequest);
		List<JaxConditionalFieldDto> flexFields = new ArrayList<>();
		try {
			remittanceAdditionalFieldManager.validateAdditionalFields(branchRemittanceApplRequestModel, remitApplParametersMap);

		} catch (GlobalException ex) {
			if (ex.getMeta() != null) {
				flexFields.addAll((Collection<? extends JaxConditionalFieldDto>) ex.getMeta());
			}
		}
		try {
			remittanceTransactionRequestValidator.validateFlexFields(branchRemittanceApplRequestModel, remitApplParametersMap);
		} catch (GlobalException | AdditionalFlexRequiredException ex) {
			if (ex.getMeta() != null) {
				flexFields.addAll((Collection<? extends JaxConditionalFieldDto>) ex.getMeta());
			}
		}

		List<JaxConditionalFieldDto> amlFlexFields = remittanceAdditionalFieldManager.validateAmlCheck(branchRemittanceApplRequestModel);
		if (CollectionUtils.isNotEmpty(amlFlexFields)) {
			flexFields.addAll(amlFlexFields);
		}
		return flexFields;
	}
	
	
	private VatDetailsDto getVatAmount(BigDecimal commission) {
		VatDetailsDto vatDetails = new VatDetailsDto();
		List<ViewVatDetails> vatList = vatDetailsRepository.getVatDetails(metaData.getCountryId(),ConstantDocument.VAT_CATEGORY,ConstantDocument.VAT_ACCOUNT_TYPE_COMM);
		String vatAppliable = null;
		if(vatList.isEmpty()) {
			vatAppliable ="N";
		}else if(vatList!=null && !vatList.isEmpty() && vatList.size()>1) {
			vatAppliable ="N";
			throw new GlobalException(JaxError.MUTIPLE_RECORD_FOUND, "More than one record available for VAT on Commission");
		}else if(vatList!=null && !vatList.isEmpty() && vatList.size()==1) {
			vatAppliable ="Y";
			vatDetails.setVatPercentage(vatList.get(0).getVatPercentage());
			vatDetails.setVatType(vatList.get(0).getVatType());
			vatDetails.setCalculatuonType(vatList.get(0).getCalculationType());
			vatDetails.setRoudingOff(vatList.get(0).getRoundOff()==null?BigDecimal.ZERO:vatList.get(0).getRoundOff());
		}
		if(JaxUtil.isNullZeroBigDecimalCheck(commission) && commission.compareTo(BigDecimal.ZERO)>0) {
		if(!StringUtils.isBlank(vatAppliable) && vatAppliable.equalsIgnoreCase(ConstantDocument.Yes) ) {
			vatDetails.setVatApplicable(vatAppliable);
			if(JaxUtil.isNullZeroBigDecimalCheck(vatDetails.getVatPercentage()) && vatDetails.getVatPercentage().compareTo(BigDecimal.ZERO)>0) {
				BigDecimal BIG_HUNDRED = new BigDecimal(100);
				BigDecimal vatAmount =BigDecimal.ZERO;
				if(!StringUtils.isBlank(vatDetails.getCalculatuonType()) && vatDetails.getCalculatuonType().equalsIgnoreCase(ConstantDocument.VAT_CALCULATION_TYPE_INCLUDE)) {
					vatAmount = RoundUtil.roundBigDecimal(((new BigDecimal(commission.doubleValue()/((vatDetails.getVatPercentage().add(BIG_HUNDRED)).doubleValue())).multiply(BIG_HUNDRED))), vatDetails.getRoudingOff().intValue());
					vatDetails.setVatAmount(commission.subtract(vatAmount));
					vatDetails.setCommission(commission);
				}else {
					vatAmount = commission.multiply(RoundUtil.roundBigDecimal(vatDetails.getVatPercentage().divide(BIG_HUNDRED),vatDetails.getRoudingOff().intValue()));
					vatDetails.setVatAmount(vatAmount);
					vatDetails.setCommission(commission.add(vatAmount));
				}
			}
		}
	}else {
		vatDetails.setVatApplicable(vatAppliable);
	}

		return  vatDetails;
	}
	
}
