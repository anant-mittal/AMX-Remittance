package com.amx.jax.pricer.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.serviceprovider.ServiceProviderClient;
import com.amx.jax.dbmodel.partner.BankExternalReferenceDetail;
import com.amx.jax.dbmodel.partner.BankExternalReferenceHead;
import com.amx.jax.dbmodel.partner.PaymentModeLimitsView;
import com.amx.jax.dbmodel.partner.ServiceProviderXmlLog;
import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.request.serviceprovider.ServiceProviderLogDTO;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.serviceprovider.Quotation_Call_Response;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.partner.dto.BeneficiaryDetailsDTO;
import com.amx.jax.partner.dto.CustomerDetailsDTO;
import com.amx.jax.partner.dto.CustomerDiscountReqDTO;
import com.amx.jax.partner.dto.HomeSendSrvcProviderInfo;
import com.amx.jax.partner.dto.ProductDetailsDTO;
import com.amx.jax.partner.dto.RoutingBankDetails;
import com.amx.jax.partner.dto.SrvPrvFeeInqReqDTO;
import com.amx.jax.partner.dto.SrvPrvFeeInqResDTO;
import com.amx.jax.pricer.dao.PartnerServiceDao;
import com.amx.jax.pricer.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.pricer.dbmodel.BankBranchView;
import com.amx.jax.pricer.dbmodel.BankCharges;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.BankServiceRule;
import com.amx.jax.pricer.dbmodel.BenificiaryListView;
import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.dbmodel.CountryMasterModel;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.dbmodel.CustomerDetailsView;
import com.amx.jax.pricer.dbmodel.EmployeeDetailView;
import com.amx.jax.pricer.dbmodel.ParameterDetailsModel;
import com.amx.jax.pricer.dbmodel.ServiceProviderRateView;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.repository.AuthenticationLimitCheckRepository;
import com.amx.jax.pricer.repository.BankMasterRepository;
import com.amx.jax.pricer.repository.CountryBranchRepository;
import com.amx.jax.pricer.repository.EmployeeDetailsViewRepository;
import com.amx.jax.pricer.repository.IBankBranchViewRepository;
import com.amx.jax.pricer.repository.IPaymentModeLimitsRepository;
import com.amx.jax.pricer.repository.IServiceProviderXMLRepository;
import com.amx.jax.pricer.service.ExchangeDataService;
import com.amx.jax.pricer.var.PricerServiceConstants;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE;
import com.amx.jax.util.AmxDBConstants;
import com.amx.utils.IoUtils;
import com.amx.utils.JsonUtil;
import com.amx.utils.RoundUtil;

@Component
public class PartnerDataManager {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PartnerDataManager.class);

	@Autowired
	PartnerServiceDao partnerServiceDao;

	@Autowired
	ServiceProviderClient serviceProviderClient;

	@Autowired
	ExchangeDataService exchangeDataService;

	@Autowired
	CustomerDiscountManager customerDiscountManager;

	@Autowired
	EmployeeDetailsViewRepository employeeDetailsRepository;

	@Autowired
	IServiceProviderXMLRepository serviceProviderXMLRepository;
	
	@Autowired
	BankMasterRepository bankMasterRepository;
	
	@Autowired
	IPaymentModeLimitsRepository paymentModeLimitsRepository;
	
	@Autowired
	AuthenticationLimitCheckRepository authenticationLimitCheckRepository;
	
	@Autowired
	IBankBranchViewRepository bankBranchViewRepository;
	
	@Autowired
	CountryBranchRepository countryBranchRepository;

	// validate get quotation
	public void validateGetQuotation(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {
		nullCheckValidation(srvPrvFeeInqReqDTO);
	}

	// fetch the quotation
	public SrvPrvFeeInqResDTO fetchQuotationForServiceProvider(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {
		String bankCode = null;
		SrvPrvFeeInqResDTO srvPrvFeeInqResDTO = null;
		SrvPrvFeeInqResDTO localCurrencySrvPrvFeeInqResDTO = null;
		BigDecimal marginAmount = BigDecimal.ZERO;
		BigDecimal amxRateWithMargin = BigDecimal.ZERO;
		BigDecimal amxRateWithMarginWithPips = BigDecimal.ZERO;
		BigDecimal settlementAmtwithDecimal = BigDecimal.ZERO;
		BigDecimal settlementAmount = BigDecimal.ZERO;
		BigDecimal settlementTotalDiscountPips = BigDecimal.ZERO;

		AmxApiResponse<Quotation_Call_Response, Object> quotationResponse = null;
		
		// fetching customer details
		CustomerDetailsDTO customerDetailsDTO = fetchCustomerDetails(srvPrvFeeInqReqDTO.getCustomerId());
		// fetching customer beneficiary details
		BeneficiaryDetailsDTO beneficiaryDetailsDTO = fetchBeneficiaryDetails(srvPrvFeeInqReqDTO.getCustomerId(), srvPrvFeeInqReqDTO.getBeneficiaryRelationShipId(), srvPrvFeeInqReqDTO.getForeignCurrencyId());
		
		validateCusBeneDetails(customerDetailsDTO, beneficiaryDetailsDTO);

		ProductDetailsDTO productDetailsDTO = fetchProductDetails(srvPrvFeeInqReqDTO);
		
		// bank Id is available to fetch bank code
		BankMasterModel bankMasterModel = bankMasterRepository.findBybankIdAndRecordStatus(productDetailsDTO.getBankId(), PricerServiceConstants.Yes);
		if(bankMasterModel == null) {
			throw new PricerServiceException(PricerServiceError.INVALID_BANK_ID,
					"Invalid bank id Details : None Found " + productDetailsDTO.getBankId());
		}else {
			bankCode = bankMasterModel.getBankCode();
			productDetailsDTO.setCountryId(bankMasterModel.getBankCountryId());
		}
		if(bankCode != null && bankCode.equalsIgnoreCase(SERVICE_PROVIDER_BANK_CODE.HOME.name())) {
			Long servProvFeeStartTime = System.currentTimeMillis();
			BigDecimal settlementExchangeRate = fetchUsdExchangeRate();
			if(settlementExchangeRate != null) {
				LOGGER.info("Settlement Exchange Rate : "+settlementExchangeRate+ " startTime : " + servProvFeeStartTime);
				CurrencyMasterModel currencyMasterModel = fetchCurrencyMasterDetails(PricerServiceConstants.SETTLEMENT_CURRENCY_CODE, PricerServiceConstants.Yes);
				if(currencyMasterModel != null) {
					validateExchangeRate(settlementExchangeRate,currencyMasterModel.getFundMinRate(),currencyMasterModel.getFundMaxRate());
					ServiceProviderRateView serviceProviderRateView = fetchMarginByProduct(productDetailsDTO);
					if(serviceProviderRateView != null) {
						marginAmount = serviceProviderRateView.getMargin() == null ? BigDecimal.ZERO : serviceProviderRateView.getMargin();
						amxRateWithMargin = settlementExchangeRate.add(marginAmount);

						if (srvPrvFeeInqReqDTO.getSelectedCurrency().compareTo(srvPrvFeeInqReqDTO.getLocalCurrencyId()) == 0) {
							
							settlementAmtwithDecimal = new BigDecimal(srvPrvFeeInqReqDTO.getAmount().doubleValue()/amxRateWithMargin.doubleValue());
							settlementAmount = RoundUtil.roundBigDecimal(settlementAmtwithDecimal, currencyMasterModel.getDecinalNumber().intValue());
							LOGGER.info("Amx Exchange Rate with Margin : "+amxRateWithMargin+" settlement Amount : "+settlementAmtwithDecimal+" settlement Amount with round : "+settlementAmount);
							
							//checkingAmountLimit(bankMasterModel.getBankId(), currencyMasterModel.getCurrencyId(), customerDetailsDTO.getCustomerTypeCode(), beneficiaryDetailsDTO.getBenificaryStatusId(), settlementAmount);

							quotationResponse = fetchQuotationDetails(srvPrvFeeInqReqDTO, serviceProviderRateView, settlementAmount, customerDetailsDTO, beneficiaryDetailsDTO);

							if(quotationResponse != null) {
								//localCurrencySrvPrvFeeInqResDTO = ServiceProviderResponse(quotationResponse, srvPrvFeeInqReqDTO, productDetailsDTO, serviceProviderRateView, settlementExchangeRate, settlementTotalDiscountPips,servProvFeeStartTime);
								localCurrencySrvPrvFeeInqResDTO = ServiceProviderResponse(quotationResponse, srvPrvFeeInqReqDTO, productDetailsDTO, serviceProviderRateView, amxRateWithMargin, settlementTotalDiscountPips,servProvFeeStartTime);
								// call the customer discounts
								if(localCurrencySrvPrvFeeInqResDTO != null) {
									ExchangeRateDetails exchangeRateDetails = fetchCustomerChannelDiscounts(srvPrvFeeInqReqDTO, productDetailsDTO, marginAmount, localCurrencySrvPrvFeeInqResDTO.getForeignAmount());
									if(exchangeRateDetails != null) {
										settlementTotalDiscountPips = convertDiscountToSettlementCurrency(exchangeRateDetails, settlementExchangeRate);
									}

									if(marginAmount.compareTo(settlementTotalDiscountPips) >= 0 ) {
										//amxRateWithMargin = settlementExchangeRate.add(marginAmount).subtract(settlementTotalDiscountPips);
										amxRateWithMarginWithPips = amxRateWithMargin.subtract(settlementTotalDiscountPips);
									}else {
										//amxRateWithMargin = settlementExchangeRate.add(marginAmount);
										amxRateWithMarginWithPips = amxRateWithMargin;
									}
									//settlementAmtwithDecimal = new BigDecimal(srvPrvFeeInqReqDTO.getAmount().doubleValue()/amxRateWithMargin.doubleValue());
									settlementAmtwithDecimal = new BigDecimal(srvPrvFeeInqReqDTO.getAmount().doubleValue()/amxRateWithMarginWithPips.doubleValue());
									settlementAmount = RoundUtil.roundBigDecimal(settlementAmtwithDecimal, currencyMasterModel.getDecinalNumber().intValue());
									
									checkingAmountLimit(bankMasterModel.getBankId(), currencyMasterModel.getCurrencyId(), customerDetailsDTO.getCustomerTypeCode(), beneficiaryDetailsDTO.getBenificaryStatusId(), settlementAmount);

									quotationResponse = fetchQuotationDetails(srvPrvFeeInqReqDTO, serviceProviderRateView, settlementAmount, customerDetailsDTO, beneficiaryDetailsDTO);
									if(quotationResponse != null) {
										//srvPrvFeeInqResDTO = ServiceProviderResponse(quotationResponse, srvPrvFeeInqReqDTO, productDetailsDTO, serviceProviderRateView, settlementExchangeRate, settlementTotalDiscountPips,servProvFeeStartTime);
										srvPrvFeeInqResDTO = ServiceProviderResponse(quotationResponse, srvPrvFeeInqReqDTO, productDetailsDTO, serviceProviderRateView, amxRateWithMargin, settlementTotalDiscountPips,servProvFeeStartTime);
										srvPrvFeeInqResDTO.setCustomerDiscountDetails(exchangeRateDetails.getCustomerDiscountDetails());
										LOGGER.info("Service Provider Response" + JsonUtil.toJson(srvPrvFeeInqResDTO));
									}
								}
							}
						} else {
							quotationResponse = fetchQuotationDetails(srvPrvFeeInqReqDTO, serviceProviderRateView, srvPrvFeeInqReqDTO.getAmount(), customerDetailsDTO, beneficiaryDetailsDTO);
							if(quotationResponse != null) {
								
								Quotation_Call_Response quotationCallResponse = quotationResponse.getResult();
								settlementAmount = quotationCallResponse.getInitial_amount_in_settlement_currency();
								checkingAmountLimit(bankMasterModel.getBankId(), currencyMasterModel.getCurrencyId(), customerDetailsDTO.getCustomerTypeCode(), beneficiaryDetailsDTO.getBenificaryStatusId(), settlementAmount);
								
								// call the customer discounts
								ExchangeRateDetails exchangeRateDetails = fetchCustomerChannelDiscounts(srvPrvFeeInqReqDTO, productDetailsDTO, marginAmount, srvPrvFeeInqReqDTO.getAmount());
								if(exchangeRateDetails != null) {
									settlementTotalDiscountPips = convertDiscountToSettlementCurrency(exchangeRateDetails, settlementExchangeRate);
								}

								//srvPrvFeeInqResDTO = ServiceProviderResponse(quotationResponse, srvPrvFeeInqReqDTO, productDetailsDTO, serviceProviderRateView, settlementExchangeRate, settlementTotalDiscountPips,servProvFeeStartTime);
								srvPrvFeeInqResDTO = ServiceProviderResponse(quotationResponse, srvPrvFeeInqReqDTO, productDetailsDTO, serviceProviderRateView, amxRateWithMargin, settlementTotalDiscountPips,servProvFeeStartTime);
								srvPrvFeeInqResDTO.setCustomerDiscountDetails(exchangeRateDetails.getCustomerDiscountDetails());
								LOGGER.info("Service Provider Response" + JsonUtil.toJson(srvPrvFeeInqResDTO));
							}
						}
					}else {
						// fail
						LOGGER.warn("Selling Rate, Margin Not Found : " + JsonUtil.toJson(productDetailsDTO));
						throw new PricerServiceException(PricerServiceError.INVALID_MARGIN,
								"Selling Rate, Margin Not Found : " + JsonUtil.toJson(productDetailsDTO));
					}
				}else {
					// fail
					LOGGER.warn("Invalid Settlement Currency Code Details : None Found " + PricerServiceConstants.SETTLEMENT_CURRENCY_CODE);
					throw new PricerServiceException(PricerServiceError.INVALID_SELECTED_CURRENCY,
							"Invalid Settlement Currency Code Details : None Found " + PricerServiceConstants.SETTLEMENT_CURRENCY_CODE);
				}
			}else {
				LOGGER.warn("Missing Settlement Currency Exchange Rate : " + settlementExchangeRate );
				throw new PricerServiceException(PricerServiceError.MISSING_SETTLEMENT_EXCHANGE_RATES,
						"Missing Settlement Currency Exchange Rate : " + settlementExchangeRate );
			}

		}else {
			throw new PricerServiceException(PricerServiceError.INVALID_BANK_API_CALL,
					"Invalid bank id api call : None Found " + productDetailsDTO.getBankId());
		}

		return srvPrvFeeInqResDTO;
	}

	// home send response drive
	public SrvPrvFeeInqResDTO ServiceProviderResponse(AmxApiResponse<Quotation_Call_Response, Object> quotationResponse,SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO,ProductDetailsDTO productDetailsDTO,ServiceProviderRateView serviceProviderRateView,BigDecimal amxRateWithMargin,BigDecimal settlementTotalDiscountPips,Long servProvFeeStartTime) {
		BigDecimal amxRateWithMarginWithPips = null;
		BigDecimal marginAmount = null;

		BigDecimal hsForeignSettleCurrencyRate = null;
		BigDecimal destinationAmt = null;
		BigDecimal hsCommissionAmt = null;

		BigDecimal amiecCommissionAmt = null;
		BigDecimal exchangeRate = null;
		BigDecimal exchangeRatewithpips = null;
		BigDecimal exchangeLocalAmt = null;
		BigDecimal commissionAmt = null;
		BigDecimal settlementExchangeRate = null;

		SrvPrvFeeInqResDTO srvPrvFeeInqResDTO = null;
		CurrencyMasterModel localCurrencyMaster = fetchCurrencyMasterData(srvPrvFeeInqReqDTO.getLocalCurrencyId());
		CurrencyMasterModel foreignCurrencyMaster = fetchCurrencyMasterData(srvPrvFeeInqReqDTO.getForeignCurrencyId());

		marginAmount = serviceProviderRateView.getMargin() == null ? BigDecimal.ZERO : serviceProviderRateView.getMargin();
		LOGGER.info("Margin : "+marginAmount);
		settlementExchangeRate = amxRateWithMargin.subtract(marginAmount);

		if(marginAmount.compareTo(settlementTotalDiscountPips) >= 0 ) {
			//amxRateWithMargin = settlementExchangeRate.add(marginAmount).subtract(settlementTotalDiscountPips);
			amxRateWithMarginWithPips = amxRateWithMargin.subtract(settlementTotalDiscountPips);
		}else {
			//margin and settlementTotalDiscountPips becomes zero. only usd rate is applied
			amxRateWithMarginWithPips = settlementExchangeRate;
		}
		LOGGER.info("Amx Exchange Rate with Margin : "+amxRateWithMarginWithPips);

		if(quotationResponse != null && quotationResponse.getResult() != null) {
			Quotation_Call_Response quotationCall = quotationResponse.getResult();
			ServiceProviderResponse serviceProviderResponse = (ServiceProviderResponse) quotationCall;
			// success return I
			if(quotationCall != null && quotationCall.getAction_ind() != null && quotationCall.getAction_ind().equalsIgnoreCase(PricerServiceConstants.ACTION_IND_I)) {

				hsForeignSettleCurrencyRate = quotationCall.getWhole_sale_fx_rate();
				destinationAmt = quotationCall.getCredited_amount_in_destination_currency();
				hsCommissionAmt = quotationCall.getFix_charged_amount_in_settlement_currency().add(quotationCall.getVariable_charged_amount_in_settlement_currency());

				BankServiceRule bankServiceRule = fetchBankserviceRule(productDetailsDTO);
				if(bankServiceRule != null) {
					BankCharges bankCharges = fetchBankChargesServiceProvider(bankServiceRule.getBankServiceRuleId(), destinationAmt, PricerServiceConstants.BOTH_BANK_SERVICE_COMPONENT, PricerServiceConstants.CHARGES_TYPE, productDetailsDTO.getBeneCountryId());
					if(bankCharges != null) {
						amiecCommissionAmt =  bankCharges.getChargeAmount();
						//exchangeRate = new BigDecimal(settlementExchangeRate.doubleValue()/hsForeignSettleCurrencyRate.doubleValue());
						exchangeRate = new BigDecimal(amxRateWithMargin.doubleValue()/hsForeignSettleCurrencyRate.doubleValue());
						exchangeRate = RoundUtil.roundBigDecimal(exchangeRate, AmxDBConstants.EXCHANGE_RATE_DECIMAL.intValue());

						// formula
						//exchangeRatewithpips = new BigDecimal(amxRateWithMargin.doubleValue()/hsForeignSettleCurrencyRate.doubleValue());
						exchangeRatewithpips = new BigDecimal(amxRateWithMarginWithPips.doubleValue()/hsForeignSettleCurrencyRate.doubleValue());
						exchangeRatewithpips = RoundUtil.roundBigDecimal(exchangeRatewithpips, AmxDBConstants.EXCHANGE_RATE_DECIMAL.intValue());
						exchangeLocalAmt = destinationAmt.multiply(exchangeRatewithpips);

						commissionAmt = amiecCommissionAmt.add(hsCommissionAmt.multiply(settlementExchangeRate));

						srvPrvFeeInqResDTO = new SrvPrvFeeInqResDTO();

						commissionAmt = RoundUtil.roundBigDecimal(commissionAmt, localCurrencyMaster.getDecinalNumber().intValue());
						srvPrvFeeInqResDTO.setCommissionAmount(commissionAmt);

						srvPrvFeeInqResDTO.setExchangeRateByServiceProvider(hsForeignSettleCurrencyRate);
						srvPrvFeeInqResDTO.setExchangeRateWithLocalAndSettlementCurrency(settlementExchangeRate);
						srvPrvFeeInqResDTO.setExchangeRateWithPips(exchangeRatewithpips);
						srvPrvFeeInqResDTO.setForeignAmount(destinationAmt);

						if (srvPrvFeeInqReqDTO.getSelectedCurrency().compareTo(srvPrvFeeInqReqDTO.getLocalCurrencyId()) == 0) {
							// checking with local amount
							srvPrvFeeInqResDTO.setGrossAmount(srvPrvFeeInqReqDTO.getAmount());
						} else {
							// checking with destination amount
							srvPrvFeeInqResDTO.setGrossAmount(exchangeLocalAmt);
						}

						BigDecimal localAmount = srvPrvFeeInqResDTO.getGrossAmount().add(commissionAmt);
						localAmount = RoundUtil.roundBigDecimal(localAmount, localCurrencyMaster.getDecinalNumber().intValue());
						srvPrvFeeInqResDTO.setLocalAmount(localAmount);

						srvPrvFeeInqResDTO.setExchangeRateBase(exchangeRate);
						srvPrvFeeInqResDTO.setMargin(marginAmount);
						
						if (srvPrvFeeInqReqDTO.getSelectedCurrency().compareTo(srvPrvFeeInqReqDTO.getLocalCurrencyId()) == 0) {
							// checking with local amount
							BigDecimal baseGrossAmount = srvPrvFeeInqReqDTO.getAmount();
							srvPrvFeeInqResDTO.setBaseLocalAmount(baseGrossAmount.add(commissionAmt));
							BigDecimal calBaseForeignAmt = new BigDecimal(baseGrossAmount.doubleValue()/exchangeRate.doubleValue());
							calBaseForeignAmt = RoundUtil.roundBigDecimal(calBaseForeignAmt, foreignCurrencyMaster.getDecinalNumber().intValue());
							srvPrvFeeInqResDTO.setBaseForeignAmount(calBaseForeignAmt);
						} else {
							// checking with destination amount
							BigDecimal exchangeBaseLocalAmt = destinationAmt.multiply(exchangeRate);
							exchangeBaseLocalAmt = RoundUtil.roundBigDecimal(exchangeBaseLocalAmt, localCurrencyMaster.getDecinalNumber().intValue());
							srvPrvFeeInqResDTO.setBaseLocalAmount(exchangeBaseLocalAmt.add(commissionAmt));
							srvPrvFeeInqResDTO.setBaseForeignAmount(destinationAmt);
						}

						HomeSendSrvcProviderInfo homeSendSrvcProviderInfo = fetchHomeSendData(serviceProviderResponse,quotationCall,marginAmount,servProvFeeStartTime);
						srvPrvFeeInqResDTO.setHomeSendInfoDTO(homeSendSrvcProviderInfo);
						LOGGER.info("Service Provider Response" + JsonUtil.toJson(srvPrvFeeInqResDTO));
					}
				}
			}else if(quotationCall != null && quotationCall.getAction_ind() != null && quotationCall.getAction_ind().equalsIgnoreCase(PricerServiceConstants.ACTION_IND_C)) {
				LOGGER.warn("Service Provider Connection Issue : " + JsonUtil.toJson(quotationCall));
				throw new PricerServiceException(PricerServiceError.INVALID_SRV_PROV_ACTION_IND_C,
						"Service Provider Connection Issue : " + quotationCall.getResponse_code() + " : " + quotationCall.getResponse_description());
			}else if(quotationCall != null && quotationCall.getAction_ind() != null && quotationCall.getAction_ind().equalsIgnoreCase(PricerServiceConstants.ACTION_IND_T)) {
				LOGGER.warn("Service Provider Technical Issue : " + JsonUtil.toJson(quotationCall));
				throw new PricerServiceException(PricerServiceError.INVALID_SRV_PROV_ACTION_IND_T,
						"Service Provider Technical Issue : " + quotationCall.getResponse_code() + " : " + quotationCall.getResponse_description());
			}else if(quotationCall != null && quotationCall.getAction_ind() != null && quotationCall.getAction_ind().equalsIgnoreCase(PricerServiceConstants.ACTION_IND_R)) {
				LOGGER.warn("Service Provider Rejected Issue : " + JsonUtil.toJson(quotationCall));
				throw new PricerServiceException(PricerServiceError.INVALID_SRV_PROV_ACTION_IND_R,
						"Service Provider Rejected Issue : " + quotationCall.getResponse_code() + " : " + quotationCall.getResponse_description());
			}else if(quotationCall != null && quotationCall.getAction_ind() != null && quotationCall.getAction_ind().equalsIgnoreCase(PricerServiceConstants.ACTION_IND_U)) {
				LOGGER.warn("Service Provider Undefined Code Issue : " + JsonUtil.toJson(quotationCall));
				throw new PricerServiceException(PricerServiceError.INVALID_SRV_PROV_ACTION_IND_U,
						"Service Provider Undefined Code Issue : " + quotationCall.getResponse_code() + " : " + quotationCall.getResponse_description());
			}else {
				LOGGER.warn("Service Provider Data Issue : " + JsonUtil.toJson(quotationCall));
				throw new PricerServiceException(PricerServiceError.INVALID_SRV_PROV_ACTION_IND,
						"Service Provider Data Issue : " + quotationCall.getResponse_code() + " : " + quotationCall.getResponse_description());
			}
		}else {
			LOGGER.warn("Service Provider Response Issue" + JsonUtil.toJson(srvPrvFeeInqReqDTO) + " : Margin Details :" + JsonUtil.toJson(serviceProviderRateView));
			throw new PricerServiceException(PricerServiceError.INVALID_QUOTATION_RESPONSE,
					"Service Provider Response Issue" + JsonUtil.toJson(srvPrvFeeInqReqDTO) + " : Margin Details :" + JsonUtil.toJson(serviceProviderRateView));
		}

		return srvPrvFeeInqResDTO;
	}

	// home send quotation call
	public AmxApiResponse<Quotation_Call_Response, Object> fetchQuotationDetails(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO,ServiceProviderRateView serviceProviderRateView,BigDecimal amount,CustomerDetailsDTO customerDetailsDTO,BeneficiaryDetailsDTO beneficiaryDetailsDTO) {

		ServiceProviderCallRequestDto quatationRequestDto = new ServiceProviderCallRequestDto();
		AmxApiResponse<Quotation_Call_Response, Object> srvPrvResp = null;
		Benificiary beneficiaryDto = new Benificiary();
		Customer customerDto = new Customer();
		TransactionData transactionDto = new TransactionData();

		String beneFirstName = null;
		String beneMiddleName = null;
		String beneLastName = null;
		String designationCountryAlpha2Code = null;
		String designationCountryAlpha3Code = null;

		try {

			if(srvPrvFeeInqReqDTO.getSelectedCurrency().compareTo(srvPrvFeeInqReqDTO.getLocalCurrencyId()) == 0){
				transactionDto.setSettlement_amount(amount);
			}else{
				transactionDto.setDestination_amount(amount);
			}
			transactionDto.setSettlement_currency(PricerServiceConstants.SETTLEMENT_CURRENCY_CODE);
			transactionDto.setDestination_currency(serviceProviderRateView.getCurrencyCode());

			HashMap<String, String> designationAlphaCodes = fetchCountryAlphaCode(beneficiaryDetailsDTO.getBenificaryCountry());
			if(designationAlphaCodes != null && !designationAlphaCodes.isEmpty()) {
				if(designationAlphaCodes.get("countryAlpha2Code") != null) {
					designationCountryAlpha2Code = designationAlphaCodes.get("countryAlpha2Code");
					transactionDto.setDestination_country_2_digit_ISO(designationCountryAlpha2Code);
				}
				if(designationAlphaCodes.get("countryAlpha3Code") != null) {
					designationCountryAlpha3Code = designationAlphaCodes.get("countryAlpha3Code");
					transactionDto.setDestination_country_3_digit_ISO(designationCountryAlpha3Code);
				}
			}

			HashMap<String, String> originAlphaCodes = fetchCountryAlphaCode(srvPrvFeeInqReqDTO.getApplicationCountryId());
			if(originAlphaCodes != null && !originAlphaCodes.isEmpty()) {
				if(originAlphaCodes.get("countryAlpha3Code") != null) {
					String originCountryAlpha3Code = originAlphaCodes.get("countryAlpha3Code");
					transactionDto.setApplication_country_3_digit_ISO(originCountryAlpha3Code);
					transactionDto.setOrigin_country_3_digit_ISO(originCountryAlpha3Code);
				}
			}

			BigDecimal tokenno = partnerServiceDao.fetchServiceProviderRefernceNum();
			if(tokenno != null){
				transactionDto.setRequest_sequence_id(tokenno.toString());
			}
			transactionDto.setRemittance_mode(serviceProviderRateView.getRemittanceCode());
			transactionDto.setDelivery_mode(serviceProviderRateView.getDeliveryCode());
			transactionDto.setRoutting_bank_code(serviceProviderRateView.getBankCode());

			if(customerDetailsDTO.getCustomerReference() != null) {
				customerDto.setCustomer_reference(customerDetailsDTO.getCustomerReference().toString());
			}
			customerDto.setCustomer_type(customerDetailsDTO.getCustomerTypeCode());

			if(beneficiaryDetailsDTO.getMapSequenceId() != null) {
				beneficiaryDto.setBeneficiary_reference(beneficiaryDetailsDTO.getMapSequenceId().toString());
			}
			
			if(beneficiaryDetailsDTO.getBenificaryStatusId() != null && beneficiaryDetailsDTO.getBenificaryStatusId().compareTo(BigDecimal.ONE) == 0) {
				beneficiaryDto.setBeneficiary_type(AmxDBConstants.Individual);
			}else {
				beneficiaryDto.setBeneficiary_type(AmxDBConstants.Non_Individual);
			}

			if(beneficiaryDetailsDTO.getFirstName() != null){
				beneFirstName = beneficiaryDetailsDTO.getFirstName();
			}
			if(beneficiaryDetailsDTO.getThirdName() != null){
				if(beneficiaryDetailsDTO.getSecondName() != null){
					beneMiddleName = beneficiaryDetailsDTO.getSecondName();
				}
				beneLastName = beneficiaryDetailsDTO.getThirdName();
			}else{
				if(beneficiaryDetailsDTO.getSecondName() != null){
					beneLastName = beneficiaryDetailsDTO.getSecondName();
				}
				if(beneficiaryDetailsDTO.getThirdName() != null){
					beneLastName = beneLastName +  " " + beneficiaryDetailsDTO.getThirdName();
				}
			}
			if(beneficiaryDetailsDTO.getFourthName() != null){
				beneLastName = beneLastName +  " " +beneficiaryDetailsDTO.getFourthName();
			}
			if(beneficiaryDetailsDTO.getFiftheName() != null){
				beneLastName = beneLastName +  " " +beneficiaryDetailsDTO.getFiftheName();
			}
			if(beneLastName != null && beneLastName.length() > 80){
				beneLastName = beneLastName.substring(0,79);
			}

			beneficiaryDto.setFirst_name(beneFirstName);
			beneficiaryDto.setMiddle_name(beneMiddleName);
			beneficiaryDto.setLast_name(beneLastName);
			
			String bankAccountNumber = null;
			String dummyRoutingNumber = null;
			HashMap<String, String> beneAccountDetails = validateBeneBankAccount(beneficiaryDetailsDTO.getBankAccountNumber(), designationCountryAlpha3Code);
			if(beneAccountDetails != null) {
				//bankAccountNumber = beneAccountDetails.get("beneBankAccount");
				bankAccountNumber = beneficiaryDetailsDTO.getBankAccountNumber();
				dummyRoutingNumber = beneAccountDetails.get("dummyRoutingNumber");
			}else {
				bankAccountNumber = beneficiaryDetailsDTO.getBankAccountNumber();
			}
			beneficiaryDto.setBeneficiary_account_number(bankAccountNumber);

			HashMap<String, Integer> mapBICandBankDt = fetchBICandBankCodeData();
			int bicValue = 0;
			int bankBranch = 0;
			HashMap<String, String> mapBicAndBankCode = fetchBICandBankCode(serviceProviderRateView.getCountryCode());
			if (mapBicAndBankCode != null && mapBICandBankDt != null) {
				String bicCode = mapBicAndBankCode.get("BIC_CODE");
				String bankCode = mapBicAndBankCode.get("BANK_BRANCH");
				if(bicCode != null) {
					bicValue = mapBICandBankDt.get(bicCode.trim());
				}
				if(bankCode != null) {
					bankBranch = mapBICandBankDt.get(bankCode.trim());
				}
			}

			beneficiaryDto.setBic_indicator(bicValue);
			beneficiaryDto.setBeneficiary_bank_branch_indicator(bankBranch);

			 
			if(dummyRoutingNumber != null) {
				// dummy routing number setted for USA to Home Send
				beneficiaryDto.setBeneficiary_bank_code(dummyRoutingNumber);
			}else {
				//beneficiaryDto.setBeneficiary_bank_code(beneficiaryDetailsDTO.getBankCode());
				String extBankRef = fetchBankCodeHomeSend(beneficiaryDetailsDTO.getBenificaryCountry(), serviceProviderRateView.getBankId(), beneficiaryDetailsDTO.getBankId());
				if(extBankRef != null) {
					beneficiaryDto.setBeneficiary_bank_code(extBankRef);
				}else {
					beneficiaryDto.setBeneficiary_bank_code(beneficiaryDetailsDTO.getBankCode());
				}
			}
			
			if(beneficiaryDetailsDTO.getBranchCode() != null) {
				String extBankBranchRef = fetchBankBranchCodeHomeSend(beneficiaryDetailsDTO.getBenificaryCountry(), serviceProviderRateView.getBankId(), beneficiaryDetailsDTO.getBankId(), beneficiaryDetailsDTO.getBranchId());
				if(extBankBranchRef != null) {
					beneficiaryDto.setBeneficiary_branch_code(extBankBranchRef);
				}else {
					beneficiaryDto.setBeneficiary_branch_code(beneficiaryDetailsDTO.getBranchCode().toString());
				}
			}
			
			if(beneficiaryDetailsDTO.getSwiftBic() != null){
				beneficiaryDto.setBeneficiary_bank_branch_swift_code(beneficiaryDetailsDTO.getSwiftBic());
			}else {
				List<BankBranchView> bankBranchView = bankBranchViewRepository.getBankBranch(beneficiaryDetailsDTO.getBankId(), beneficiaryDetailsDTO.getBranchId());
				if(bankBranchView != null && bankBranchView.size() == 1) {
					BankBranchView branchDetails = bankBranchView.get(0);
					if(branchDetails != null) {
						if(branchDetails.getSwift() != null) {
							beneficiaryDto.setBeneficiary_bank_branch_swift_code(branchDetails.getSwift());
						}else if(branchDetails.getIfscCode() != null) {
							beneficiaryDto.setBeneficiary_bank_branch_swift_code(branchDetails.getIfscCode());
						}else {
							// error
						}
					}else {
						// error
					}
				}
			}
			
			if(beneficiaryDetailsDTO.getServiceProvider() != null) {
				beneficiaryDto.setWallet_service_provider(beneficiaryDetailsDTO.getServiceProvider().toString());
			}

			quatationRequestDto.setBeneficiaryDto(beneficiaryDto);
			quatationRequestDto.setCustomerDto(customerDto);
			quatationRequestDto.setTransactionDto(transactionDto);

			LOGGER.info("Inputs passed to Service Provider Home Send : " + JsonUtil.toJson(quatationRequestDto));
			srvPrvResp = serviceProviderClient.getQuatation(quatationRequestDto);
			fetchServiceProviderData(srvPrvResp,srvPrvFeeInqReqDTO,customerDetailsDTO);
			LOGGER.info("Output from Service Provider Home Send : " + JsonUtil.toJson(srvPrvResp));

		}catch (Exception e) {
			LOGGER.error("Unable to get response from Service Provider Home Send : Exception " +e);
			LOGGER.error("Unable to get response from Service Provider Home Send : Exception " + JsonUtil.toJson(srvPrvFeeInqReqDTO) + " : serviceProviderRateView : " + JsonUtil.toJson(serviceProviderRateView) + " : amount : " + amount);
			throw new PricerServiceException(PricerServiceError.INVALID_FETCH_SERVICE_PROVIDE_DATA,
					"Unable to get response from Service Provider Home Send : srvPrvFeeInqReqDTO : " + JsonUtil.toJson(srvPrvFeeInqReqDTO) + " : serviceProviderRateView : " + JsonUtil.toJson(serviceProviderRateView) + " : amount : " + amount);
		}
		return srvPrvResp;
	}

	// codes for the bic and bank code
	public HashMap<String, Integer> fetchBICandBankCodeData() {
		HashMap<String, Integer> mapBankCode = new HashMap<String, Integer>();

		mapBankCode.put("SWIFT", 0);
		mapBankCode.put("BANK_CODE", 1);
		mapBankCode.put("BRANCH_CODE", 2);
		mapBankCode.put("BRANCH_NAME", 3);

		return mapBankCode;
	}

	// parameter for 0-swift , 1-bank code , 2-branch code and 3-branch name
	public HashMap<String, String> fetchBICandBankCode(String beneCountryCode) {
		HashMap<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("BIC_CODE", null);
		mapParam.put("BANK_BRANCH", null);
		ParameterDetailsModel parameterDetails = partnerServiceDao.fetchServPrvBankCode(PricerServiceConstants.PARAM_BIC_BRANCH, beneCountryCode);
		if (parameterDetails != null) {
			mapParam.put("BIC_CODE", parameterDetails.getCharField3());
			mapParam.put("BANK_BRANCH", parameterDetails.getCharField4());
		}
		return mapParam;
	}

	// fetch alpha codes
	public HashMap<String, String> fetchCountryAlphaCode(BigDecimal countryId){
		HashMap<String, String> countryObj = new HashMap<String, String>();
		CountryMasterModel countryAplhaCode = partnerServiceDao.fetchCountryMasterDetails(countryId);
		if (countryAplhaCode != null) {
			countryObj.put("countryAlpha2Code", countryAplhaCode.getCountryAlpha2Code());
			countryObj.put("countryAlpha3Code", countryAplhaCode.getCountryAlpha3Code());
		}else {
			LOGGER.error("Invalid Country Details : Country Id " + countryId);
			throw new PricerServiceException(PricerServiceError.INVALID_COUNTRY,
					"Invalid Country Details : Country Id " + countryId);
		}
		return countryObj;
	}

	// fetch beneficiary details
	public BeneficiaryDetailsDTO fetchBeneficiaryDetails(BigDecimal customerId,BigDecimal beneficiaryRelationShipId,BigDecimal foreignCurrencyId) {
		BeneficiaryDetailsDTO beneficiaryDto = new BeneficiaryDetailsDTO();
		BenificiaryListView beneficaryDetails = partnerServiceDao.getBeneficiaryDetails(customerId,beneficiaryRelationShipId);
		if(beneficaryDetails != null) {
			try {
				if(foreignCurrencyId != null && foreignCurrencyId.compareTo(beneficaryDetails.getCurrencyId()) == 0) {
					BeanUtils.copyProperties(beneficiaryDto, beneficaryDetails);
				}else {
					LOGGER.error("Foreign Currency Id miss match with Beneficiary Details : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId + " Req Foreign CurrencyId " + foreignCurrencyId + " Bene Foreign CurrencyId " +beneficaryDetails.getCurrencyId());
					throw new PricerServiceException(PricerServiceError.INVALID_BENEFICIARY,
							"Foreign Currency Id miss match with Beneficiary Details : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId + " Req Foreign CurrencyId " + foreignCurrencyId + " Bene Foreign CurrencyId " +beneficaryDetails.getCurrencyId());
				}
			} catch (IllegalAccessException | InvocationTargetException e) {
				LOGGER.error("Unable to convert Beneficiary Details : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId + " Foreign CurrencyId " + foreignCurrencyId + " Exception " +e);
				throw new PricerServiceException(PricerServiceError.UNKNOWN_EXCEPTION,
						"Unable to convert Beneficiary Details : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId + " Foreign CurrencyId " + foreignCurrencyId);
			}
		}else {
			// fail
			LOGGER.error("Invalid Beneficiary Details : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId + " Foreign CurrencyId " + foreignCurrencyId);
			throw new PricerServiceException(PricerServiceError.INVALID_BENEFICIARY,
					"Invalid Beneficiary Details : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId + " Foreign CurrencyId " + foreignCurrencyId);
		}

		return beneficiaryDto;
	}

	// fetch Customer details
	public CustomerDetailsDTO fetchCustomerDetails(BigDecimal customerId) {
		CustomerDetailsDTO customerdto = new CustomerDetailsDTO();
		CustomerDetailsView customerDetails = partnerServiceDao.getCustomerDetails(customerId);
		if(customerDetails != null) {
			try {
				BeanUtils.copyProperties(customerdto, customerDetails);
			} catch (IllegalAccessException | InvocationTargetException e) {
				LOGGER.error("Unable to convert Customer Details : None Found : " + customerId + " Exception " +e);
				throw new PricerServiceException(PricerServiceError.UNKNOWN_EXCEPTION,
						"Unable to convert Customer Details : None Found : " + customerId);
			}
		}else {
			// fail
			LOGGER.warn("Invalid Customer : None Found : " + customerId);
			throw new PricerServiceException(PricerServiceError.INVALID_CUSTOMER,
					"Invalid Customer : None Found : " + customerId);
		}

		return customerdto;
	}

	// fetch usd exchange rate details
	public BigDecimal fetchUsdExchangeRate() {
		BigDecimal usdExchageRate = partnerServiceDao.fetchUsdExchangeRate();
		return usdExchageRate;
	}

	// fetch currency Master for Settlement currency "USD" for Home Send
	public CurrencyMasterModel fetchCurrencyMasterDetails(String currencyCode,String isActive) {
		CurrencyMasterModel currencyMasterModel = null;
		List<CurrencyMasterModel> currencyMasterDetails = partnerServiceDao.fetchCurrencyMasterDetails(currencyCode,isActive);
		if(currencyMasterDetails != null && currencyMasterDetails.size() != 0) {
			if(currencyMasterDetails.size() == 1) {
				currencyMasterModel = currencyMasterDetails.get(0);
			}else {
				// fail multiple records
				LOGGER.warn("Multiple ISO Currency Code Details : " + currencyCode );
				throw new PricerServiceException(PricerServiceError.INVALID_CURRENCY,
						"Multiple ISO Currency Code Details : " + currencyCode );
			}
		}
		return currencyMasterModel;
	}

	// fetch margin from foreign country , currency , remittance and delivery code data
	public ServiceProviderRateView fetchMarginByProduct(ProductDetailsDTO productDetailsDTO) {
		ServiceProviderRateView serviceProviderRateView = partnerServiceDao.fetchMarginByProduct(productDetailsDTO.getBeneCountryId(), productDetailsDTO.getBankId(), productDetailsDTO.getCurrencyId(), productDetailsDTO.getRemittanceId(), productDetailsDTO.getDeliveryId());
		return serviceProviderRateView;
	}

	// check bank service rule defined
	public BankServiceRule fetchBankserviceRule(ProductDetailsDTO productDetailsDTO) {
		BankServiceRule bankServiceRuleDt = new BankServiceRule();
		List<BankServiceRule> bankServiceRule = partnerServiceDao.fetchBankServiceRuleDetails(productDetailsDTO.getCountryId(), productDetailsDTO.getBankId(), productDetailsDTO.getCurrencyId(), productDetailsDTO.getRemittanceId(), productDetailsDTO.getDeliveryId());
		if(bankServiceRule != null && bankServiceRule.size() != 0) {
			if(bankServiceRule.size() == 1) {
				bankServiceRuleDt = bankServiceRule.get(0);
			}else {
				LOGGER.warn("Multiple Bank Service Rule" + JsonUtil.toJson(productDetailsDTO));
				throw new PricerServiceException(PricerServiceError.MULTIPLE_BANK_SERVICE_RULE,
						"Multiple Bank Service Rule : None Found : " + JsonUtil.toJson(productDetailsDTO));
			}
		}else {
			// fail
			LOGGER.warn("Invalid Bank Service Rule" + JsonUtil.toJson(productDetailsDTO));
			throw new PricerServiceException(PricerServiceError.INVALID_BANK_SERVICE_RULE,
					"Invalid Bank Service Rule : None Found : " + JsonUtil.toJson(productDetailsDTO));
		}
		return bankServiceRuleDt;
	}

	// fetch bank Service Rule for charges
	public BankCharges fetchBankChargesServiceProvider(BigDecimal bankServiceRuleId,BigDecimal fcAmount,BigDecimal chargesFor,String chargesType,BigDecimal beneCountryId) {
		BankCharges bankChargesDt = new BankCharges();
		List<BankCharges> bankCharges = partnerServiceDao.fetchBankChargesDetails(bankServiceRuleId, fcAmount, chargesFor, chargesType, beneCountryId);
		if(bankCharges != null && bankCharges.size() != 0) {
			if(bankCharges.size() == 1) {
				bankChargesDt = bankCharges.get(0);
			}else {
				LOGGER.warn("Multiple Bank Charges : None Found : bankServiceRuleId : " + bankServiceRuleId + " : fcAmount : " + fcAmount + " : chargesFor : " + chargesFor + " : chargesType : " + chargesType);
				throw new PricerServiceException(PricerServiceError.MULTIPLE_BANK_CHARGES,
						"Multiple Bank Charges : None Found : bankServiceRuleId : " + bankServiceRuleId + " : fcAmount : " + fcAmount + " : chargesFor : " + chargesFor + " : chargesType : " + chargesType);
			}
		}else {
			// fail
			LOGGER.warn("Invalid Bank Charges : None Found : bankServiceRuleId : " + bankServiceRuleId + " : fcAmount : " + fcAmount + " : chargesFor : " + chargesFor + " : chargesType : " + chargesType);
			throw new PricerServiceException(PricerServiceError.INVALID_BANK_CHARGES,
					"Invalid Bank Charges : None Found : bankServiceRuleId : " + bankServiceRuleId + " : fcAmount : " + fcAmount + " : chargesFor : " + chargesFor + " : chargesType : " + chargesType);
		}
		return bankChargesDt;
	}

	// null checking for inputs
	public void nullCheckValidation(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {

		if(null == srvPrvFeeInqReqDTO.getCustomerId() || (srvPrvFeeInqReqDTO.getCustomerId() != null && srvPrvFeeInqReqDTO.getCustomerId().compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Invalid Customer : None Found : " + srvPrvFeeInqReqDTO.getCustomerId());
			throw new PricerServiceException(PricerServiceError.INVALID_CUSTOMER,
					"Invalid Customer : None Found : " + srvPrvFeeInqReqDTO.getCustomerId());
		}

		if(null == srvPrvFeeInqReqDTO.getBeneficiaryRelationShipId() || (srvPrvFeeInqReqDTO.getBeneficiaryRelationShipId() != null && srvPrvFeeInqReqDTO.getBeneficiaryRelationShipId().compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Invalid Beneficiary Relation Id : None Found : " + srvPrvFeeInqReqDTO.getBeneficiaryRelationShipId());
			throw new PricerServiceException(PricerServiceError.INVALID_BENEFICIARY_RELATIONSHIP_ID,
					"Invalid Beneficiary Relation Id : None Found : " + srvPrvFeeInqReqDTO.getBeneficiaryRelationShipId());
		}

		if(null == srvPrvFeeInqReqDTO.getForeignCurrencyId() || (srvPrvFeeInqReqDTO.getForeignCurrencyId() != null && srvPrvFeeInqReqDTO.getForeignCurrencyId().compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Invalid Foreign Currency Id : None Found : " + srvPrvFeeInqReqDTO.getForeignCurrencyId());
			throw new PricerServiceException(PricerServiceError.INVALID_FOREIGN_CURRENCY,
					"Invalid Foreign Currency Id : None Found : " + srvPrvFeeInqReqDTO.getForeignCurrencyId());
		}

		if(null == srvPrvFeeInqReqDTO.getLocalCurrencyId() || (srvPrvFeeInqReqDTO.getLocalCurrencyId() != null && srvPrvFeeInqReqDTO.getLocalCurrencyId().compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Invalid local Currency Id : None Found : " + srvPrvFeeInqReqDTO.getLocalCurrencyId());
			throw new PricerServiceException(PricerServiceError.INVALID_LOCAL_CURRENCY,
					"Invalid local Currency Id : None Found : " + srvPrvFeeInqReqDTO.getLocalCurrencyId());
		}

		if(null == srvPrvFeeInqReqDTO.getSelectedCurrency() || (srvPrvFeeInqReqDTO.getSelectedCurrency() != null && srvPrvFeeInqReqDTO.getSelectedCurrency().compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Invalid selected Currency Id : None Found : " + srvPrvFeeInqReqDTO.getSelectedCurrency());
			throw new PricerServiceException(PricerServiceError.INVALID_SELECTED_CURRENCY,
					"Invalid selected Currency Id : None Found : " + srvPrvFeeInqReqDTO.getSelectedCurrency());
		}

		if(null == srvPrvFeeInqReqDTO.getAmount() || (srvPrvFeeInqReqDTO.getAmount() != null && srvPrvFeeInqReqDTO.getAmount().compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Invalid Amount : None Found : " + srvPrvFeeInqReqDTO.getAmount());
			throw new PricerServiceException(PricerServiceError.MISSING_AMOUNT,
					"Invalid Amount : None Found : " + srvPrvFeeInqReqDTO.getAmount());
		}

		if(null == srvPrvFeeInqReqDTO.getApplicationCountryId() || (srvPrvFeeInqReqDTO.getApplicationCountryId() != null && srvPrvFeeInqReqDTO.getApplicationCountryId().compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Invalid Application Country Id : None Found : " + srvPrvFeeInqReqDTO.getApplicationCountryId());
			throw new PricerServiceException(PricerServiceError.MISSING_APPLICATION_COUNTRY_ID,
					"Invalid Application Country Id : None Found : " + srvPrvFeeInqReqDTO.getApplicationCountryId());
		}

		if(null == srvPrvFeeInqReqDTO.getDestinationCountryId() || (srvPrvFeeInqReqDTO.getDestinationCountryId() != null && srvPrvFeeInqReqDTO.getDestinationCountryId().compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Invalid Destination Country Id : None Found : " + srvPrvFeeInqReqDTO.getDestinationCountryId());
			throw new PricerServiceException(PricerServiceError.MISSING_DESTINATION_COUNTRY_ID,
					"Invalid Destination Country Id : None Found : " + srvPrvFeeInqReqDTO.getDestinationCountryId());
		}

		if(null == srvPrvFeeInqReqDTO.getRoutingBankDetails()) {
			LOGGER.warn("Invalid Routing Bank Details : None Found : " + srvPrvFeeInqReqDTO.getRoutingBankDetails());
			throw new PricerServiceException(PricerServiceError.MISSING_ROUTING_BANK_DETAILS,
					"Invalid Routing Bank Details : None Found : " + srvPrvFeeInqReqDTO.getRoutingBankDetails());
		}

	}

	// validate the exchange Rate min and max
	public void validateExchangeRate(BigDecimal exchangeRate,BigDecimal fundMinRate,BigDecimal fundMaxRate) {

		if(null == exchangeRate || (exchangeRate != null && exchangeRate.compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Missing Settlement Currency Exchange Rate : " + exchangeRate);
			throw new PricerServiceException(PricerServiceError.MISSING_SETTLEMENT_EXCHANGE_RATES,
					"Missing Settlement Currency Exchange Rate : " + exchangeRate );
		}

		if(null == fundMinRate || (fundMinRate != null && fundMinRate.compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Missing Fund Min Exchange Rate : " + fundMinRate);
			throw new PricerServiceException(PricerServiceError.MISSING_SETTLEMENT_FUND_MIN_EXCHANGE_RATES,
					"Missing Fund Min Exchange Rate : " + fundMinRate);
		}

		if(null == fundMaxRate || (fundMaxRate != null && fundMaxRate.compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Missing Fund Max Exchange Rate : " + fundMaxRate);
			throw new PricerServiceException(PricerServiceError.MISSING_SETTLEMENT_FUND_MAX_EXCHANGE_RATES,
					"Missing Fund Max Exchange Rate : " + fundMaxRate);
		}

		if(exchangeRate.compareTo(fundMinRate) >= 0 && exchangeRate.compareTo(fundMaxRate) <= 0) {
			// valid
		}else {
			LOGGER.warn("Settlement Exchange Rate not in min and max : exchangeRate : " + exchangeRate + " : fundMinRate :" + fundMinRate + " : fundMaxRate : " + fundMaxRate);
			throw new PricerServiceException(PricerServiceError.INVALID_EXCHANGE_RATES,
					"Settlement Exchange Rate not in min and max : exchangeRate : " + exchangeRate + " : fundMinRate :" + fundMinRate + " : fundMaxRate : " + fundMaxRate);
		}
	}

	// setting the Home Send Data
	public HomeSendSrvcProviderInfo fetchHomeSendData(ServiceProviderResponse serviceProviderResponse,Quotation_Call_Response quotationCall,BigDecimal marginAmount,Long servProvFeeStartTime) {

		HomeSendSrvcProviderInfo homeSendSrvcProviderInfo = new HomeSendSrvcProviderInfo();
		homeSendSrvcProviderInfo.setActionInd(serviceProviderResponse.getAction_ind());
		homeSendSrvcProviderInfo.setCreditedAmountInDestinationCurrency(quotationCall.getCredited_amount_in_destination_currency());
		homeSendSrvcProviderInfo.setDestinationCurrency(quotationCall.getDestination_currency());
		homeSendSrvcProviderInfo.setFixChargedAmountInSettlementCurrency(quotationCall.getFix_charged_amount_in_settlement_currency());
		homeSendSrvcProviderInfo.setInitialAmountInSettlementCurrency(quotationCall.getInitial_amount_in_settlement_currency());
		homeSendSrvcProviderInfo.setOfferExpirationDate(quotationCall.getOffer_expiration_date());
		homeSendSrvcProviderInfo.setPartnerTransactionReference(quotationCall.getPartner_transaction_reference());
		homeSendSrvcProviderInfo.setOutGoingTransactionReference(quotationCall.getOut_going_transaction_reference());
		homeSendSrvcProviderInfo.setRequestXML(serviceProviderResponse.getRequest_XML());
		homeSendSrvcProviderInfo.setResponseCode(serviceProviderResponse.getResponse_code());
		homeSendSrvcProviderInfo.setResponseDescription(serviceProviderResponse.getResponse_description());
		homeSendSrvcProviderInfo.setResponseXML(serviceProviderResponse.getResponse_XML());
		homeSendSrvcProviderInfo.setSettlementCurrency(quotationCall.getSettlement_currency());
		homeSendSrvcProviderInfo.setTechnicalDetails(serviceProviderResponse.getTechnical_details());
		homeSendSrvcProviderInfo.setTotalChargedAmountInSettlementCurrency(quotationCall.getTotal_charged_amount_in_settlement_currency());
		homeSendSrvcProviderInfo.setVariableChargedAmountInSettlementCurrency(quotationCall.getVariable_charged_amount_in_settlement_currency());
		homeSendSrvcProviderInfo.setWholeSaleFxRate(quotationCall.getWhole_sale_fx_rate());
		homeSendSrvcProviderInfo.setBeneficiaryDeduct(Boolean.FALSE);
		homeSendSrvcProviderInfo.setTransactionMargin(marginAmount);
		
		LOGGER.info("HomeSendSrvcProviderInfo orignal expiry time  : " + JsonUtil.toJson(homeSendSrvcProviderInfo));

		Calendar startcalendar = Calendar.getInstance();
		startcalendar.setTimeInMillis(servProvFeeStartTime);
		homeSendSrvcProviderInfo.setOfferStartDate(startcalendar);
		
		AuthenticationLimitCheckView authHSLimit = authenticationLimitCheckRepository.getHomeSendTimerLimit();
		if(authHSLimit != null && authHSLimit.getAuthLimit() != null && authHSLimit.getAuthLimit().compareTo(BigDecimal.ZERO) != 0) {
			Calendar endcalendar = Calendar.getInstance();
			endcalendar.setTimeInMillis(servProvFeeStartTime);
			endcalendar.add(Calendar.MINUTE, authHSLimit.getAuthLimit().intValue());
			homeSendSrvcProviderInfo.setOfferExpirationDate(endcalendar);
		}else {
			Calendar endcalendar = Calendar.getInstance();
			endcalendar.setTimeInMillis(servProvFeeStartTime);
			endcalendar.add(Calendar.MINUTE, 5);
			homeSendSrvcProviderInfo.setOfferExpirationDate(endcalendar);
		}

		LOGGER.info("HomeSendSrvcProviderInfo fixed : " + JsonUtil.toJson(homeSendSrvcProviderInfo));

		return homeSendSrvcProviderInfo;
	}

	public ProductDetailsDTO fetchProductDetails(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {

		ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();

		productDetailsDTO.setBeneCountryId(srvPrvFeeInqReqDTO.getDestinationCountryId());
		productDetailsDTO.setCurrencyId(srvPrvFeeInqReqDTO.getForeignCurrencyId());

		if(srvPrvFeeInqReqDTO.getRoutingBankDetails() != null) {
			if(srvPrvFeeInqReqDTO.getRoutingBankDetails().size() == 1) {
				for (RoutingBankDetails routing : srvPrvFeeInqReqDTO.getRoutingBankDetails()) {
					validateProduct(routing);
					productDetailsDTO.setBankId(routing.getRoutingBankId());
					productDetailsDTO.setRemittanceId(routing.getRemittanceId());
					productDetailsDTO.setDeliveryId(routing.getDeliveryId());
				}
			}else {
				LOGGER.warn("Multiple Routing Bank Details : None Found : " + srvPrvFeeInqReqDTO.getRoutingBankDetails());
				throw new PricerServiceException(PricerServiceError.MULTIPLE_ROUTING_BANK_DETAILS,
						"Multiple Routing Bank Details : None Found : " + srvPrvFeeInqReqDTO.getRoutingBankDetails());
			}
		}

		return productDetailsDTO;
	}

	public void validateProduct(RoutingBankDetails routingBankDetails) {
		if(null == routingBankDetails.getRoutingBankId() || (routingBankDetails.getRoutingBankId() != null && routingBankDetails.getRoutingBankId().compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Invalid Routing Bank Id : None Found : " + routingBankDetails.getRoutingBankId());
			throw new PricerServiceException(PricerServiceError.MISSING_ROUTING_BANK_IDS,
					"Invalid Routing Bank Id : None Found : " + routingBankDetails.getRoutingBankId());
		}

		if(null == routingBankDetails.getRemittanceId() || (routingBankDetails.getRemittanceId() != null && routingBankDetails.getRemittanceId().compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Invalid Remittance Id : None Found : " + routingBankDetails.getRemittanceId());
			throw new PricerServiceException(PricerServiceError.INVALID_REMITTANCE_ID,
					"Invalid Remittance Id : None Found : " + routingBankDetails.getRemittanceId());
		}

		if(null == routingBankDetails.getDeliveryId() || (routingBankDetails.getDeliveryId() != null && routingBankDetails.getDeliveryId().compareTo(BigDecimal.ZERO) == 0)) {
			LOGGER.warn("Invalid Delivery Id : None Found : " + routingBankDetails.getDeliveryId());
			throw new PricerServiceException(PricerServiceError.INVALID_DELIVERY_ID,
					"Invalid Delivery Id : None Found : " + routingBankDetails.getDeliveryId());
		}
	}

	// iterate the response and insert the log table
	public void fetchServiceProviderData(AmxApiResponse<Quotation_Call_Response, Object> srvPrvResp,SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO,CustomerDetailsDTO customerDto) {
		String requestXml = null;
		String responseXml = null;
		String referenceNo = null;
		BigDecimal customerReference = null;
		String partnerReference = null;
		String trnxType = PricerServiceConstants.SEND_TRNX;
		BigDecimal reqSeq = new BigDecimal(1);
		BigDecimal resSeq = new BigDecimal(2);
		if(srvPrvResp != null && srvPrvResp.getResult() != null) {
			if(customerDto != null) {
				customerReference = customerDto.getCustomerReference();
			}
			if(srvPrvResp.getResult().getOut_going_transaction_reference() != null) {
				referenceNo = srvPrvResp.getResult().getOut_going_transaction_reference();
			}
			if(srvPrvResp.getResult() != null && srvPrvResp.getResult().getPartner_transaction_reference() != null) {
				partnerReference = srvPrvResp.getResult().getPartner_transaction_reference();
			}
			if(srvPrvResp.getResult().getRequest_XML() != null) {
				requestXml = srvPrvResp.getResult().getRequest_XML();
				ServiceProviderLogDTO serviceProviderXmlLog = saveServiceProviderXMLlogData(PricerServiceConstants.FEE_REQUEST, requestXml, referenceNo, reqSeq, PricerServiceConstants.REQUEST, trnxType, partnerReference,srvPrvFeeInqReqDTO,customerReference);
				if(serviceProviderXmlLog != null) {
					saveServiceProviderXml(serviceProviderXmlLog);
				}
			}
			if(srvPrvResp.getResult().getResponse_XML() != null) {
				responseXml = srvPrvResp.getResult().getResponse_XML();
				ServiceProviderLogDTO serviceProviderXmlLog = saveServiceProviderXMLlogData(PricerServiceConstants.FEE_RESPONSE, responseXml, referenceNo, resSeq, PricerServiceConstants.RESPONSE, trnxType, partnerReference,srvPrvFeeInqReqDTO,customerReference);
				if(serviceProviderXmlLog != null) {
					saveServiceProviderXml(serviceProviderXmlLog);
				}
			}
		}
	}

	public ServiceProviderLogDTO saveServiceProviderXMLlogData(String filename,String content,String referenceNo,BigDecimal seq,String xmlType,String trnxType,String bene_Bank_Txn_Ref,SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO,BigDecimal customerReference) {
		ServiceProviderLogDTO serviceProviderXmlLog = new ServiceProviderLogDTO();
		try {
			serviceProviderXmlLog.setApplicationCountryId(srvPrvFeeInqReqDTO.getApplicationCountryId());
			serviceProviderXmlLog.setCompanyId(srvPrvFeeInqReqDTO.getCompanyId());
			serviceProviderXmlLog.setCountryBranchId(srvPrvFeeInqReqDTO.getCountryBranchId());

			serviceProviderXmlLog.setCreatedDate(new Date());
			serviceProviderXmlLog.setCustomerId(srvPrvFeeInqReqDTO.getCustomerId());
			serviceProviderXmlLog.setCustomerReference(customerReference);
			if(srvPrvFeeInqReqDTO.getCountryBranchId() != null) {
				CountryBranch countryBranch = countryBranchRepository.findByCountryBranchId(srvPrvFeeInqReqDTO.getCountryBranchId());
				if(countryBranch != null) {
					serviceProviderXmlLog.setEmosBranchCode(countryBranch.getBranchId());
				}
			}
			serviceProviderXmlLog.setFileName(filename);
			if(srvPrvFeeInqReqDTO.getEmployeeId() != null) {
				EmployeeDetailView empDetails = employeeDetailsRepository.findByEmployeeId(srvPrvFeeInqReqDTO.getEmployeeId());
				serviceProviderXmlLog.setForeignTerminalId(srvPrvFeeInqReqDTO.getEmployeeId().toPlainString());
				serviceProviderXmlLog.setCreatedBy(empDetails.getUserName());
			}else {
				serviceProviderXmlLog.setCreatedBy("ON_LINE");
			}
			serviceProviderXmlLog.setIdentifier(PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE.HOME.name());
			serviceProviderXmlLog.setMtcNo(bene_Bank_Txn_Ref);
			if(referenceNo != null) {
				serviceProviderXmlLog.setRefernceNo(new BigDecimal(referenceNo));
			}
			serviceProviderXmlLog.setSequence(seq);
			serviceProviderXmlLog.setTransactionType(trnxType);
			serviceProviderXmlLog.setXmlData(IoUtils.stringToClob(content));
			serviceProviderXmlLog.setXmlType(xmlType);
		} catch (SerialException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return serviceProviderXmlLog;
	}

	public void saveServiceProviderXml(ServiceProviderLogDTO serviceProviderLogDTO) {
		try {
			ServiceProviderXmlLog serviceProviderXmlLog =  new ServiceProviderXmlLog();
			try {
				BeanUtils.copyProperties(serviceProviderXmlLog, serviceProviderLogDTO);
				serviceProviderXMLRepository.save(serviceProviderXmlLog);
			} catch (IllegalAccessException | InvocationTargetException e) {
				LOGGER.error("Unable to convert Customer Details Exception " +e);
				throw new PricerServiceException(PricerServiceError.UNKNOWN_EXCEPTION,
						"Unable to convert Customer Details");
			}
		}  catch (Exception e) {
			LOGGER.error("Unable to saveServiceProviderXml Exception " +e);
		}
	}

	// fetch decimal for currency id 
	public CurrencyMasterModel fetchCurrencyMasterData(BigDecimal currencyId) {
		CurrencyMasterModel currencyMasterModel = partnerServiceDao.fetchCurrencyMaster(currencyId);
		return currencyMasterModel;
	}

	// apply the fetch discounts in based on destination Country and Currency
	public DiscountDetailsReqRespDTO getDiscountDetailsCountryCurrency(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {

		DiscountMgmtReqDTO discountMgmtReqDTO = new DiscountMgmtReqDTO();
		discountMgmtReqDTO.setCountryId(srvPrvFeeInqReqDTO.getDestinationCountryId());
		discountMgmtReqDTO.setCurrencyId(srvPrvFeeInqReqDTO.getForeignCurrencyId());

		List<DISCOUNT_TYPE> lstDicountType = new ArrayList<>();
		lstDicountType.add(DISCOUNT_TYPE.CUSTOMER_CATEGORY);
		lstDicountType.add(DISCOUNT_TYPE.CHANNEL);
		lstDicountType.add(DISCOUNT_TYPE.AMOUNT_SLAB);
		discountMgmtReqDTO.setDiscountType(lstDicountType);

		LOGGER.info("DiscountMgmtReqDTO : " + JsonUtil.toJson(discountMgmtReqDTO));
		DiscountDetailsReqRespDTO discountDetailsReqRespDTO = exchangeDataService.getDiscountManagementData(discountMgmtReqDTO);
		LOGGER.info("DiscountDetailsReqRespDTO : " + JsonUtil.toJson(discountDetailsReqRespDTO));

		return discountDetailsReqRespDTO;
	}

	// iterate the response of discount
	public ExchangeRateDetails fetchCustomerChannelDiscounts(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO,ProductDetailsDTO productDetailsDTO,BigDecimal marginAmount,BigDecimal fcAmount) {

		CustomerDiscountReqDTO customerDiscountReqDTO = new CustomerDiscountReqDTO();

		//testing
		//customerDiscountReqDTO.setBankId(new BigDecimal(351));
		customerDiscountReqDTO.setBankId(productDetailsDTO.getBankId());
		customerDiscountReqDTO.setChannel(srvPrvFeeInqReqDTO.getChannel());
		customerDiscountReqDTO.setCustomerId(srvPrvFeeInqReqDTO.getCustomerId());
		customerDiscountReqDTO.setForeignCurrencyId(srvPrvFeeInqReqDTO.getForeignCurrencyId());
		customerDiscountReqDTO.setForeignAmount(fcAmount);

		LOGGER.info("DiscountDetailsReqRespDTO : " + JsonUtil.toJson(customerDiscountReqDTO));
		ExchangeRateDetails bankExRateDetail = customerDiscountManager.fetchCustomerChannelDiscounts(customerDiscountReqDTO);
		LOGGER.info("DiscountDetailsReqRespDTO : " + JsonUtil.toJson(bankExRateDetail));

		return bankExRateDetail;
	}

	public BigDecimal convertDiscountToSettlementCurrency(ExchangeRateDetails bankExRateDetail,BigDecimal settlementExchangeRate) {
		BigDecimal ccDiscountPips = BigDecimal.ZERO;
		BigDecimal amountSlabPips = BigDecimal.ZERO;
		BigDecimal channelDiscountPips = BigDecimal.ZERO;
		BigDecimal totalDiscountPips = BigDecimal.ZERO;

		channelDiscountPips = bankExRateDetail.getCustomerDiscountDetails().get(DISCOUNT_TYPE.CHANNEL).getDiscountPipsValue();
		ccDiscountPips = bankExRateDetail.getCustomerDiscountDetails().get(DISCOUNT_TYPE.CUSTOMER_CATEGORY).getDiscountPipsValue();
		amountSlabPips = bankExRateDetail.getCustomerDiscountDetails().get(DISCOUNT_TYPE.AMOUNT_SLAB).getDiscountPipsValue();

		totalDiscountPips = (amountSlabPips == null ? BigDecimal.ZERO : amountSlabPips).add(channelDiscountPips == null ? BigDecimal.ZERO : channelDiscountPips).add(ccDiscountPips == null ? BigDecimal.ZERO : ccDiscountPips);

		BigDecimal settlementTotalDiscountPips = settlementExchangeRate.multiply(totalDiscountPips);
		settlementTotalDiscountPips = RoundUtil.roundBigDecimal(settlementTotalDiscountPips, AmxDBConstants.EXCHANGE_RATE_DECIMAL.intValue());

		return settlementTotalDiscountPips;
	}

	// validating account number for AUS
	public HashMap<String, String> validateBeneBankAccount(String bankAccount,String countryAplha3Code) {
		HashMap<String, String> mapValidAcc = new HashMap<String, String>();
		String beneBankAccount = bankAccount;
		String dummyRoutingNumber = null;
		String bsbCode = null;
		if(bankAccount != null) {
			if(countryAplha3Code.equals(PricerServiceConstants.COUNTRY_AUS_ALPHA3CODE)) {
				int accountLength = bankAccount.length();
				if(accountLength >= 6 && accountLength<= 9) {
					// continue as account number
				}else if(accountLength >= 12 && accountLength<= 15) {
					// need to remove first 6 digits BSB code from account number
					beneBankAccount = bankAccount.substring(6);
					bsbCode = bankAccount.substring(0, 6);
				}
			}if(countryAplha3Code.equals(PricerServiceConstants.COUNTRY_USA_ALPHA3CODE)) {
				List<ParameterDetailsModel> lstparameterDetails = partnerServiceDao.fetchUSDummyAccountDetails(PricerServiceConstants.PARAM_FEE_DUMMY_ACCOUNT,PricerServiceConstants.Yes);
				if(lstparameterDetails != null && lstparameterDetails.size() != 0) {
					for (ParameterDetailsModel parameterDetails : lstparameterDetails) {
						if(parameterDetails.getCharField1() != null && parameterDetails.getCharField1().equalsIgnoreCase(countryAplha3Code)) {
							//beneBankAccount = parameterDetails.getCharField2();
							dummyRoutingNumber = parameterDetails.getCharField3();
							break;
						}
					}
				}
			}
		}

		mapValidAcc.put("beneBankAccount", beneBankAccount);
		mapValidAcc.put("dummyRoutingNumber", dummyRoutingNumber);
		mapValidAcc.put("bsbCode", bsbCode);
		
		return mapValidAcc;
	}
	
	// checking the usd amount with limit
	public void checkingAmountLimit(BigDecimal bankId,BigDecimal currencyId,String customerType,BigDecimal beneficiaryType,BigDecimal usdAmount) {
		String customerTypeFrom = null;
		String beneficiaryTypeFrom = null;
		
		if(customerType != null) {
			if(customerType.equalsIgnoreCase(AmxDBConstants.Individual)) {
				customerTypeFrom = PricerServiceConstants.Personal;
			}else {
				customerTypeFrom = PricerServiceConstants.Business;
			}
		}
		
		if(beneficiaryType != null) {
			if(beneficiaryType.compareTo(BigDecimal.ONE) == 0) {
				beneficiaryTypeFrom = PricerServiceConstants.Personal;
			}else {
				beneficiaryTypeFrom = PricerServiceConstants.Business;
			}
		}
		
		if(bankId != null && currencyId != null && customerTypeFrom != null && beneficiaryTypeFrom != null) {
			PaymentModeLimitsView paymentModeLimitsView = fetchPaymentLimits(bankId, currencyId, customerTypeFrom, beneficiaryTypeFrom);
			if(paymentModeLimitsView != null) {
				if(paymentModeLimitsView.getKnetLimit() != null && usdAmount != null) {
					if(paymentModeLimitsView.getKnetLimit().compareTo(usdAmount) < 0) {
						throw new PricerServiceException(PricerServiceError.INVALID_AMOUNT,"Amount limit crossed : " + paymentModeLimitsView.getKnetLimit());
					}
				}
			}
		}else {
			throw new PricerServiceException(PricerServiceError.MISSING_PAYMENT_LIMIT_DATA,"Proper data not available to fetch the limits");
		}
	}
	
	// fetch the payment Limits
	public PaymentModeLimitsView fetchPaymentLimits(BigDecimal bankId,BigDecimal currencyId,String customerTypeFrom,String customerTypeTo) {
		PaymentModeLimitsView paymentModeLimitsView = null;
		List<PaymentModeLimitsView> lstpaymentLimit = paymentModeLimitsRepository.fetchPaymentLimitDetails(bankId, currencyId, customerTypeFrom, customerTypeTo);
		// check the usd amount
		if(lstpaymentLimit != null && lstpaymentLimit.size() == 1) {
			paymentModeLimitsView = lstpaymentLimit.get(0);
		}
		return paymentModeLimitsView;
	}
	
	// validate the customer and beneficiary
	public void validateCusBeneDetails(CustomerDetailsDTO customerDetailsDTO,BeneficiaryDetailsDTO beneficiaryDetailsDTO) {
		if(customerDetailsDTO != null) {
			if(customerDetailsDTO.getCustomerName() != null) {
				String custName[] = customerDetailsDTO.getCustomerName().split("\\s");
				for (int i=0; i < custName.length; i++){
					if(custName[i].length() == 1) {
						// error msg
						throw new PricerServiceException(PricerServiceError.INVALID_CUSTOMER,"Customer name should not contains any initials");
					}
				}
			}
		}
		if(beneficiaryDetailsDTO != null) {
			String beneFirstName = null;
			String beneMiddleName = null;
			String beneLastName = null;
			String beneFullName= null;
			if(beneficiaryDetailsDTO.getFirstName() != null){
				beneFirstName = beneficiaryDetailsDTO.getFirstName();
			}
			if(beneficiaryDetailsDTO.getThirdName() != null){
				if(beneficiaryDetailsDTO.getSecondName() != null){
					beneMiddleName = beneficiaryDetailsDTO.getSecondName();
				}
				beneLastName = beneficiaryDetailsDTO.getThirdName();
			}else{
				if(beneficiaryDetailsDTO.getSecondName() != null){
					beneLastName = beneficiaryDetailsDTO.getSecondName();
				}
				if(beneficiaryDetailsDTO.getThirdName() != null){
					beneLastName = beneLastName +  " " + beneficiaryDetailsDTO.getThirdName();
				}
			}
			if(beneficiaryDetailsDTO.getFourthName() != null){
				beneLastName = beneLastName +  " " +beneficiaryDetailsDTO.getFourthName();
			}
			if(beneficiaryDetailsDTO.getFiftheName() != null){
				beneLastName = beneLastName +  " " +beneficiaryDetailsDTO.getFiftheName();
			}
			if(beneLastName != null && beneLastName.length() > 80){
				beneLastName = beneLastName.substring(0,79);
			}
			beneFullName = beneFirstName + " " + beneMiddleName + " " + beneLastName;

			String beneName[] = beneFullName.split("\\s");
			for (int i=0; i < beneName.length; i++){
				if(beneName[i].length() == 1) {
					// error msg
					throw new PricerServiceException(PricerServiceError.INVALID_BENEFICIARY,"Beneficiary name should not contains any initials");
				}
			}
		}
	}
	
	// external bank codes
	public String fetchBankCodeHomeSend(BigDecimal countryId,BigDecimal corBankId,BigDecimal beneBankId){
		String mapBankCode = null;
		try {
			List<BankExternalReferenceHead> lstBankExtRefHead = partnerServiceDao.fetchBankExternalReferenceHeadDetails(countryId, corBankId, beneBankId);
			if(lstBankExtRefHead != null && lstBankExtRefHead.size() == 1) {
				BankExternalReferenceHead bankExternalReferenceHead = lstBankExtRefHead.get(0);
				mapBankCode = bankExternalReferenceHead.getBankExternalId();
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}

		return mapBankCode;
	}

	// external bank branch codes
	public String fetchBankBranchCodeHomeSend(BigDecimal countryId,BigDecimal corBankId,BigDecimal beneBankId,BigDecimal beneBankBranchId){
		String mapBankBranchCode = null;
		try {
			List<BankExternalReferenceDetail> lstBankExtRefBranchDetails = partnerServiceDao.fetchBankExternalReferenceBranchDetails(countryId, corBankId, beneBankId, beneBankBranchId);
			if(lstBankExtRefBranchDetails != null && lstBankExtRefBranchDetails.size() == 1) {
				BankExternalReferenceDetail bankExternalReferenceDetail = lstBankExtRefBranchDetails.get(0);
				mapBankBranchCode = bankExternalReferenceDetail.getBankBranchExternalId();
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}

		return mapBankBranchCode;
	}

}
