package com.amx.jax.branchremittance.manager;

import static com.amx.amxlib.constant.ApplicationProcedureParam.P_DELIVERY_MODE_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_FOREIGN_CURRENCY_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_REMITTANCE_MODE_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_ROUTING_BANK_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_ROUTING_COUNTRY_ID;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.util.JaxValidationUtil;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.CurrencyMasterDao;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleAmiec;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.error.JaxError;
import com.amx.jax.exrateservice.service.ExchangeRateService;
import com.amx.jax.exrateservice.service.JaxDynamicPriceService;
import com.amx.jax.exrateservice.service.JaxDynamicRoutingPricingService;
import com.amx.jax.exrateservice.service.NewExchangeRateService;
import com.amx.jax.manager.RemittanceTransactionManager;
import com.amx.jax.manager.remittance.CorporateDiscountManager;
import com.amx.jax.manager.remittance.RemittanceAdditionalFieldManager;
import com.amx.jax.manager.remittance.RemittanceApplicationParamManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.request.remittance.IRemittanceApplicationParams;
import com.amx.jax.model.request.remittance.RoutingPricingRequest;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.BranchExchangeRateBreakup;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.ServiceProviderDto;
import com.amx.jax.model.response.remittance.VatDetailsDto;
import com.amx.jax.model.response.remittance.branch.BranchRemittanceGetExchangeRateResponse;
import com.amx.jax.model.response.remittance.branch.DynamicRoutingPricingResponse;
import com.amx.jax.partner.dto.HomeSendSrvcProviderInfo;
import com.amx.jax.pricer.dto.ExchangeDiscountInfo;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingResponse;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.TrnxRoutingDetails;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_TYPE;
import com.amx.jax.remittance.manager.RemittanceParameterMapManager;
import com.amx.jax.repository.IAdditionalBankRuleAmiecRepository;
import com.amx.jax.repository.remittance.ICorporateMasterRepository;
import com.amx.jax.repository.remittance.IViewVatDetailsRespository;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.BeneficiaryValidationService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.RemittanceTransactionRequestValidator;
import com.amx.libjax.model.jaxfield.JaxConditionalFieldDto;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittanceExchangeRateManager {

	static final Logger logger = LoggerFactory.getLogger(BranchRemittanceExchangeRateManager.class);

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
	JaxDynamicRoutingPricingService jaxDynamicRoutingPriceService;
	
	@Autowired
	ExchangeRateService exchangeRateService;
	
	@Autowired
	IAdditionalBankRuleAmiecRepository amiecBankRuleRepo;
	
	@Autowired
	BranchRemittanceManager branchRemittanceManager;
	
	@Autowired
	BankMetaService bankMetaService;
	
	@Autowired
	IViewVatDetailsRespository vatDetailsRepository;
	
	@Autowired
	CurrencyMasterDao currencyMasterDao;
	

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
		VatDetailsDto vatDetails = remittanceTransactionManager.getVatAmount(commission);
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
		remittanceTransactionManager.setNetAmountAndLoyalityState(branchExchangeRate, remittanceApplRequestModel, result, commission,vatDetails.getVatApplicableAmount());
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
		
		if (newCommission != null) {
			commission = newCommission;
		}else {
			throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "COMMISSION NOT DEFINED FOR Country "+rountingCountryId+" currencyId :"+currencyId+" remittanceMode :"+remittanceMode);
		}
		
		BigDecimal corpDiscount = corporateDiscountManager.corporateDiscount();
		if(JaxUtil.isNullZeroBigDecimalCheck(commission) && commission.compareTo(corpDiscount)>=0) {
			commission =commission.subtract(corpDiscount);
		}
		return commission;
		
	}

	
	
	
	
	public DynamicRoutingPricingResponse getDynamicRoutingAndPricingResponse(RoutingPricingRequest routingPricingRequest) {
		BenificiaryListView beneficiaryView = beneValidationService.validateBeneficiary(routingPricingRequest.getBeneficiaryRelationshipSeqId());
		Customer customer = userService.getCustById(metaData.getCustomerId());
		BigDecimal exclusiveBankId= null;
		String accValid = remittanceTransactionManager.beneAccountValidationThroughApi(beneficiaryView.getServiceGroupId(), beneficiaryView.getBankId(), beneficiaryView);
		if(!StringUtils.isBlank(accValid) && accValid.equals(ConstantDocument.No)) {
			exclusiveBankId = beneficiaryView.getBankId();
		}
		AmxApiResponse<ExchangeRateAndRoutingResponse,Object> apiResposne = jaxDynamicRoutingPriceService.getDynamicRoutingAndPrice(metaData.getDefaultCurrencyId(), beneficiaryView.getCurrencyId(), routingPricingRequest.getLocalAmount(),
				routingPricingRequest.getForeignAmount(), beneficiaryView.getBenificaryCountry(),
				null, beneficiaryView.getServiceGroupId(),beneficiaryView.getBankId(),beneficiaryView.getBranchId(),beneficiaryView.getServiceGroupCode(),beneficiaryView.getBeneficiaryRelationShipSeqId(),exclusiveBankId);
		DynamicRoutingPricingResponse dynamicRoutingPricingResponse=getDynamicRoutingPricing(apiResposne,routingPricingRequest,beneficiaryView);
		
	return dynamicRoutingPricingResponse;
		
	}
	
	private DynamicRoutingPricingResponse getDynamicRoutingPricing(AmxApiResponse<ExchangeRateAndRoutingResponse,Object> apiResponse,RoutingPricingRequest routingPricingRequest,BenificiaryListView beneficiaryView) {
		DynamicRoutingPricingResponse dynamicRoutingPricingResponse = new DynamicRoutingPricingResponse();
		if (apiResponse != null) {
				Map<PRICE_TYPE, List<String>> bestExchangeRatePaths =apiResponse.getResult().getBestExchangeRatePaths();
				List<Map<String,List<DynamicRoutingPricingDto>>> dynamicRoutingPricingList = new ArrayList<>();
				
				if(bestExchangeRatePaths!=null && !bestExchangeRatePaths.isEmpty()) {
					for (Map.Entry<PRICE_TYPE, List<String>> mapEntry : bestExchangeRatePaths.entrySet()) {
						dynamicRoutingPricingResponse = new DynamicRoutingPricingResponse();
						if(PRICE_TYPE.BENE_DEDUCT.equals(mapEntry.getKey())) {
							List<String> beneDeductList=mapEntry.getValue();
							Map<String,List<DynamicRoutingPricingDto>> dynamicRoutingPricingMap= new HashMap<>();
							List<DynamicRoutingPricingDto> dynamicRoutingPricingDtoList= new ArrayList<>();
							for(String beneDed : beneDeductList) {
								DynamicRoutingPricingDto dto =  dynamicxchangeRateResponseModel(apiResponse,beneDed,routingPricingRequest,beneficiaryView,mapEntry.getKey());
								dynamicRoutingPricingDtoList.add(dto);
								
							}
							dynamicRoutingPricingMap.put(PRICE_TYPE.BENE_DEDUCT.toString(), dynamicRoutingPricingDtoList);
							dynamicRoutingPricingList.add(dynamicRoutingPricingMap);
						}
						if(PRICE_TYPE.NO_BENE_DEDUCT.equals(mapEntry.getKey())) {
							List<String> nonBeneDeduct =mapEntry.getValue();
							Map<String,List<DynamicRoutingPricingDto>> dynamicRoutingPricingMap= new HashMap<>();
							List<DynamicRoutingPricingDto> dynamicRoutingPricingDtoList= new ArrayList<>();
							for(String noBeneDed : nonBeneDeduct) {
								DynamicRoutingPricingDto dto =  dynamicxchangeRateResponseModel(apiResponse,noBeneDed,routingPricingRequest,beneficiaryView,mapEntry.getKey());
								dynamicRoutingPricingDtoList.add(dto);
							}
							dynamicRoutingPricingMap.put(PRICE_TYPE.NO_BENE_DEDUCT.toString(), dynamicRoutingPricingDtoList);
							dynamicRoutingPricingList.add(dynamicRoutingPricingMap);
							
						}
			        }
				}else {
					throw new GlobalException(JaxError.ROUTING_DETAILS_NOT_AVAIL, "Routing service is not available at this time. Please contact support."); 
				}
				
				dynamicRoutingPricingResponse.setDynamicRoutingPricingList(dynamicRoutingPricingList);
		
		}
		return dynamicRoutingPricingResponse;
	}
	
	
	private DynamicRoutingPricingDto dynamicxchangeRateResponseModel(AmxApiResponse<ExchangeRateAndRoutingResponse,Object> apiResponse,String key,RoutingPricingRequest routingPricingRequest,BenificiaryListView beneficiaryView,PRICE_TYPE prType) {
		
		
		DynamicRoutingPricingDto result = new DynamicRoutingPricingDto();
		
		Map<String, TrnxRoutingDetails> trnxRoutingPathList = apiResponse.getResult().getTrnxRoutingPaths();
		Map<BigDecimal, Map<BigDecimal, ExchangeRateDetails>> bankServiceModeSellRates = apiResponse.getResult().getBankServiceModeSellRates();
		HomeSendSrvcProviderInfo homeSendSrvcProviderInfo = apiResponse.getResult().getHomeSendSrvcProviderInfo();
		Customer customer = userService.getCustById(metaData.getCustomerId());
		Channel channel = Channel.valueOf(metaData.getChannel().toString());
		if (AppContextUtil.getUserClient() != null && AppContextUtil.getUserClient().getClientType() != null) {
			channel = AppContextUtil.getUserClient().getClientType().getChannel();
		}
		TrnxRoutingDetails trnxRoutingDetails = trnxRoutingPathList.get(key);
		if(trnxRoutingDetails!=null) {
			result.setTrnxRoutingPaths(trnxRoutingDetails);
			
			// service provider condition check
			if(homeSendSrvcProviderInfo != null && trnxRoutingDetails.getBankIndicator() != null && trnxRoutingDetails.getBankIndicator().equalsIgnoreCase(ConstantDocument.BANK_INDICATOR_SERVICE_PROVIDER_BANK)) {
				ServiceProviderDto serviceProviderDto = fetchRemitServiceProviderDt(homeSendSrvcProviderInfo);
				result.setServiceProviderDto(serviceProviderDto);
			}
		}
		
		ExchangeRateDetails sellRateDetail= bankServiceModeSellRates.get(trnxRoutingDetails.getRoutingBankId()).get(trnxRoutingDetails.getServiceMasterId());
		if(sellRateDetail!=null) {
			result.setCustomerDiscountDetails(sellRateDetail.getCustomerDiscountDetails());
			result.setDiscountAvailed(sellRateDetail.isDiscountAvailed());
			result.setCostRateLimitReached(sellRateDetail.isCostRateLimitReached());
			result.setDiffInBetterRateFcAmount(sellRateDetail.getDiffInBetterRateFcAmount());
			result.setBetterRateAvailable(sellRateDetail.isBetterRateAvailable());
			result.setBetterRateAmountSlab(sellRateDetail.getBetterRateAmountSlab());
			
			BigDecimal commission =null;
			if(prType.equals(PRICE_TYPE.NO_BENE_DEDUCT)) {
			 commission =trnxRoutingDetails.getChargeAmount();
			 result.setBeneDeductFlag(ConstantDocument.No);
			}else if(prType.equals(PRICE_TYPE.BENE_DEDUCT)) {
				commission =trnxRoutingDetails.getBeneDeductChargeAmount();
				 result.setBeneDeductFlag(ConstantDocument.Yes);
			}
			BigDecimal corpDiscount = corporateDiscountManager.corporateDiscount();
			
			if(JaxUtil.isNullZeroBigDecimalCheck(commission) &&  JaxUtil.isNullZeroBigDecimalCheck(corpDiscount) && commission.compareTo(corpDiscount)>=0) {
				commission =commission.subtract(corpDiscount);
				result.setDiscountOnComissionFlag(ConstantDocument.Yes);
			}
			
			VatDetailsDto vatDetails = remittanceTransactionManager.getVatAmount(commission);
			if(vatDetails!=null && !StringUtils.isBlank(vatDetails.getVatApplicable()) && vatDetails.getVatApplicable().equalsIgnoreCase(ConstantDocument.Yes)) {
				result.setVatAmount(vatDetails.getVatAmount()==null?BigDecimal.ZERO:vatDetails.getVatAmount());
				result.setVatPercentage(vatDetails.getVatPercentage()==null?BigDecimal.ZERO:vatDetails.getVatPercentage());
				result.setVatType(vatDetails.getVatType()==null?"":vatDetails.getVatType());
				if(JaxUtil.isNullZeroBigDecimalCheck(vatDetails.getCommission())) {
					commission =vatDetails.getCommission();
				}
			}
			result.setTxnFee(commission);
			result.setDiscountOnComission(corpDiscount);
			
			if(trnxRoutingDetails != null && trnxRoutingDetails.getBankIndicator() != null && !trnxRoutingDetails.getBankIndicator().equalsIgnoreCase(ConstantDocument.BANK_INDICATOR_SERVICE_PROVIDER_BANK)) {
				if (routingPricingRequest.getForeignAmount() != null) {
					result.setExRateBreakup(exchangeRateService.createBreakUpFromForeignCurrency(sellRateDetail.getSellRateNet().getInverseRate(), routingPricingRequest.getForeignAmount()));
				} else {
					result.setExRateBreakup(exchangeRateService.createBreakUp(sellRateDetail.getSellRateNet().getInverseRate(), routingPricingRequest.getLocalAmount()));
				}
			}else {
				result.setExRateBreakup(exchangeRateService.createBreakUpSP(sellRateDetail.getSellRateNet().getInverseRate(), sellRateDetail.getSellRateNet().getConvertedLCAmount(),sellRateDetail.getSellRateNet().getConvertedFCAmount()));
				if(sellRateDetail.getSellRateBase().getInverseRate() != null) {
					result.getExRateBreakup().setBaseRate(sellRateDetail.getSellRateBase().getInverseRate());
				}
			}
			
			remittanceApplicationParamManager.populateRemittanceApplicationParamMap(null, beneficiaryView,result.getExRateBreakup());
			remittanceTransactionManager.setLoyalityPointFlags(customer, result);
			remittanceTransactionManager.setLoyalityPointIndicaters(result);
			BranchRemittanceApplRequestModel remittanceApplRequestModel = buildRemittanceTransactionModel(routingPricingRequest);
			//if(trnxRoutingDetails != null && trnxRoutingDetails.getBankIndicator() != null && !trnxRoutingDetails.getBankIndicator().equalsIgnoreCase(ConstantDocument.BANK_INDICATOR_SERVICE_PROVIDER_BANK)) {
			/** isFcRoundingAllowed() --Yes normal ,N -Not allowed **/
			if(trnxRoutingDetails != null && trnxRoutingDetails.getIsFcRoundingAllowed() !=null && trnxRoutingDetails.getIsFcRoundingAllowed().equalsIgnoreCase(ConstantDocument.Yes)) { 
				remittanceTransactionManager.applyChannelAmountRouding(result.getExRateBreakup(),metaData.getChannel().getClientChannel(), true);
			}
			remittanceTransactionManager.setNetAmountAndLoyalityState(result.getExRateBreakup(), remittanceApplRequestModel, result, commission,vatDetails.getVatApplicableAmount());
			if(trnxRoutingDetails != null && trnxRoutingDetails.getIsFcRoundingAllowed() !=null && trnxRoutingDetails.getIsFcRoundingAllowed().equalsIgnoreCase(ConstantDocument.Yes)) {
				remittanceTransactionManager.applyCurrencyRoudingLogic(result.getExRateBreakup());
			}else {
				remittanceTransactionManager.applyCurrencyRoudingLogicSP(result.getExRateBreakup());
			}
			/** Imps split message for multiple trnx  **/
			String msg = impsSplittingMessage(result);
			result.setErrorMessage(msg);
		}
		return result;
	}
	
	
	
	public Object fetchFlexFields(IRemittanceApplicationParams request) {
		
		
		BenificiaryListView beneficiaryView = beneValidationService.validateBeneficiary(request.getBeneficiaryRelationshipSeqIdBD());
		remittanceApplicationParamManager.populateRemittanceApplicationParamMap(request, beneficiaryView,null);
		BranchRemittanceApplRequestModel branchRemittanceApplRequestModel = new BranchRemittanceApplRequestModel(request);
		
		CountryMaster cntMaster = new CountryMaster();
		List<AdditionalBankRuleAmiec> amiecRuleMap  = null;
		
		List<AdditionalExchAmiecDto> addExchDto = null;
		
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
	
	
	public BranchRemittanceGetExchangeRateResponse getDynamicRoutingAndPricingExchangeRateResponseCompare(IRemittanceApplicationParams request) {
		BenificiaryListView beneficiaryView = beneValidationService.validateBeneficiary(request.getBeneficiaryRelationshipSeqIdBD());
		Customer customer = userService.getCustById(metaData.getCustomerId());
		ExchangeRateResponseModel exchangeRateResponseModel = null;
		AmxApiResponse<ExchangeRateAndRoutingResponse,Object> apiResposne = jaxDynamicRoutingPriceService.getDynamicRoutingAndPrice(metaData.getDefaultCurrencyId(), beneficiaryView.getCurrencyId(), request.getLocalAmountBD(),
				request.getForeignAmountBD(), beneficiaryView.getBenificaryCountry(),
				request.getCorrespondanceBankIdBD(), beneficiaryView.getServiceGroupId(),beneficiaryView.getBankId(),beneficiaryView.getBranchId(),beneficiaryView.getServiceGroupCode(),beneficiaryView.getBeneficiaryRelationShipSeqId(),null);
		 exchangeRateResponseModel  = createExchangeRateResponseModel(apiResposne,request.getLocalAmountBD(),request.getForeignAmountBD(),request.getCorrespondanceBankIdBD(),request.getServiceIndicatorIdBD());
		
		 if (exchangeRateResponseModel.getExRateBreakup() == null) {
				throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
			}
	    remittanceApplicationParamManager.populateRemittanceApplicationParamMap(request, beneficiaryView,exchangeRateResponseModel.getExRateBreakup());
		BranchRemittanceGetExchangeRateResponse result = new BranchRemittanceGetExchangeRateResponse();
		BranchExchangeRateBreakup branchExchangeRate = new BranchExchangeRateBreakup(exchangeRateResponseModel.getExRateBreakup());
		result.setExRateBreakup(branchExchangeRate);
		return result;
		 
	}
	
	
	
	private ExchangeRateResponseModel createExchangeRateResponseModel(AmxApiResponse<ExchangeRateAndRoutingResponse,Object> apiResponse, BigDecimal lcAmount, BigDecimal foreignAmount,BigDecimal routingBankId,BigDecimal serviceIndicatorId) {
		ExchangeRateResponseModel exchangeRateResponseModel = new ExchangeRateResponseModel();
		List<BankMasterDTO> bankWiseRates = new ArrayList<>();
		Map<DISCOUNT_TYPE, ExchangeDiscountInfo> customerDiscountDetails = new HashMap<>();
		Boolean discountAvailed=false;
		Boolean costRateLimitReached=false;
		exchangeRateResponseModel.setBankWiseRates(bankWiseRates);
		if (apiResponse != null) {
			Map<BigDecimal, Map<BigDecimal, ExchangeRateDetails>> bankServiceModeSellRates=apiResponse.getResult().getBankServiceModeSellRates();
			Map<BigDecimal, ExchangeRateDetails> exchangeRateDetails = bankServiceModeSellRates.get(routingBankId);
				if(JaxUtil.isNullZeroBigDecimalCheck(routingBankId) && JaxUtil.isNullZeroBigDecimalCheck(serviceIndicatorId)){
				ExchangeRateDetails sellRateDetail= bankServiceModeSellRates.get(routingBankId).get(serviceIndicatorId);
		
				if (serviceIndicatorId != null && serviceIndicatorId.equals(sellRateDetail.getServiceIndicatorId())) {
					BankMasterDTO dto = bankMetaService.convert(bankMetaService.getBankMasterbyId(sellRateDetail.getBankId()));
					if (foreignAmount != null) {
						dto.setExRateBreakup(exchangeRateService.createBreakUpFromForeignCurrency(sellRateDetail.getSellRateNet().getInverseRate(), foreignAmount));
					} else {
						dto.setExRateBreakup(exchangeRateService.createBreakUp(sellRateDetail.getSellRateNet().getInverseRate(), lcAmount));
					}
					bankWiseRates.add(dto);
					customerDiscountDetails =sellRateDetail.getCustomerDiscountDetails();
					discountAvailed=sellRateDetail.isDiscountAvailed();
					costRateLimitReached = sellRateDetail.isCostRateLimitReached();
				exchangeRateResponseModel.setBankWiseRates(bankWiseRates);
				if (CollectionUtils.isNotEmpty(bankWiseRates)) {
					exchangeRateResponseModel.setExRateBreakup(bankWiseRates.get(0).getExRateBreakup());
					exchangeRateResponseModel.setCustomerDiscountDetails(customerDiscountDetails);
					exchangeRateResponseModel.setDiscountAvailed(discountAvailed);
					exchangeRateResponseModel.setCostRateLimitReached(costRateLimitReached);
				}
			}
			}else {
				throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
			}	
			}
		return exchangeRateResponseModel;
	}

	private ServiceProviderDto fetchRemitServiceProviderDt(HomeSendSrvcProviderInfo homeSendSrvcProviderInfo) {
		
		ServiceProviderDto serviceProviderDto  = new ServiceProviderDto();
		
		if(homeSendSrvcProviderInfo.getOutGoingTransactionReference() != null) {
			serviceProviderDto.setAmgSessionId(new BigDecimal(homeSendSrvcProviderInfo.getOutGoingTransactionReference()));
		}
		serviceProviderDto.setFixedCommInSettlCurr(homeSendSrvcProviderInfo.getFixChargedAmountInSettlementCurrency());
		serviceProviderDto.setIntialAmountInSettlCurr(homeSendSrvcProviderInfo.getInitialAmountInSettlementCurrency());
		serviceProviderDto.setPartnerSessionId(homeSendSrvcProviderInfo.getPartnerTransactionReference());
		serviceProviderDto.setSettlementCurrency(homeSendSrvcProviderInfo.getSettlementCurrency());
		serviceProviderDto.setTransactionMargin(homeSendSrvcProviderInfo.getTransactionMargin());
		serviceProviderDto.setVariableCommInSettlCurr(homeSendSrvcProviderInfo.getVariableChargedAmountInSettlementCurrency());
		serviceProviderDto.setOfferExpirationDate(homeSendSrvcProviderInfo.getOfferExpirationDate());
		serviceProviderDto.setOfferStartingDate(homeSendSrvcProviderInfo.getOfferStartDate());
		
		return serviceProviderDto ; 
	}
	
	
	
	private String impsSplittingMessage(DynamicRoutingPricingDto drDto) {
		String msg = null;
		String reminder = "";
		try {
		TrnxRoutingDetails routingDetails = drDto.getTrnxRoutingPaths();
		BigDecimal foreignAmont = drDto.getExRateBreakup().getConvertedFCAmount();
		
		BigDecimal fcurrencyId = (BigDecimal) remitApplParametersMap.get("P_FOREIGN_CURRENCY_ID");
		CurrencyMasterModel currMaster = currencyMasterDao.getCurrencyMasterById(fcurrencyId); 
		String currQuoteName = currMaster!=null?(currMaster.getQuoteName()==null?"":currMaster.getQuoteName()):currMaster.getCurrencyCode(); 
		BigDecimal[] splitCount = foreignAmont.divideAndRemainder(routingDetails.getSplitAmount());
		BigDecimal count = new BigDecimal(0);
		Map<BigDecimal,BigDecimal> mapSplitAmount = new HashMap<>();
		if(splitCount!=null && splitCount.length>0) {
			count = splitCount[0].add(splitCount[1].compareTo(BigDecimal.ZERO)>0?BigDecimal.ONE:BigDecimal.ZERO);
			List<String> amountStrList= new ArrayList<>();
			for(int i=0;i<splitCount[0].intValue();i++) {
				amountStrList.add(routingDetails.getSplitAmount().toString());
			}
			String joinedString = amountStrList.stream().collect(Collectors.joining(","));
			if(splitCount[1]!=null && splitCount[1].compareTo(BigDecimal.ZERO)>0) { 
				reminder ="and "+ currQuoteName+" {"+splitCount[1]+"}";
			}
		    msg = "This single remittance will be reflected as {"+count.intValue()+"} transactions in your bank account.The {"+count.intValue()+"} transactions will be "+currQuoteName+" {"+joinedString+"} "+reminder +".";
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
}
