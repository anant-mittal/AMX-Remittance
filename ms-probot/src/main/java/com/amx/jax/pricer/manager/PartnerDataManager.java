package com.amx.jax.pricer.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.serviceprovider.ServiceProviderClient;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.serviceprovider.Quotation_Call_Response;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.partner.dao.PartnerServiceDao;
import com.amx.jax.partner.dbmodel.BankCharges;
import com.amx.jax.partner.dbmodel.BankServiceRule;
import com.amx.jax.partner.dbmodel.BenificiaryListView;
import com.amx.jax.partner.dbmodel.CustomerDetailsView;
import com.amx.jax.partner.dbmodel.ServiceProviderRateView;
import com.amx.jax.partner.dto.BeneficiaryDetailsDTO;
import com.amx.jax.partner.dto.CustomerDetailsDTO;
import com.amx.jax.partner.dto.HomeSendInfoDTO;
import com.amx.jax.partner.dto.ProductDetailsDTO;
import com.amx.jax.partner.dto.SrvPrvFeeInqReqDTO;
import com.amx.jax.partner.dto.SrvPrvFeeInqResDTO;
import com.amx.jax.pricer.dbmodel.CountryMasterDT;
import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.var.PricerServiceConstants;
import com.amx.utils.JsonUtil;

@Component
public class PartnerDataManager {

	private static int DEF_DECIMAL_SCALE = 8;

	private static MathContext DEF_CONTEXT = new MathContext(DEF_DECIMAL_SCALE, RoundingMode.HALF_EVEN);

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PartnerDataManager.class);

	@Autowired
	PartnerServiceDao partnerServiceDao;

	@Autowired
	ServiceProviderClient serviceProviderClient;

	// validate get quotation
	public void validateGetQuotation(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {
		nullCheckValidation(srvPrvFeeInqReqDTO);
	}

	// fetch get quotation for local currency
	public SrvPrvFeeInqResDTO fetchQuotationForLocalCurrency(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {
		AmxApiResponse<Quotation_Call_Response, Object> quotationResponse = null;
		SrvPrvFeeInqResDTO srvPrvFeeInqResDTO = null;
		BigDecimal settlementExchangeRate = fetchUsdExchangeRate();
		BigDecimal amxRateWithMargin = null;
		BigDecimal settlementAmtwithDecimal = null;
		BigDecimal marginAmount = null;
		BigDecimal settlementAmount = null;

		BigDecimal hsForeignSettleCurrencyRate = null;
		BigDecimal destinationAmt = null;
		BigDecimal hsCommissionAmt = null;
		BigDecimal hsfeeInquiryRateNew = null;

		BigDecimal amiecCommissionAmt = null;
		BigDecimal exchangeRate = null;
		BigDecimal exchangeRatewithpips = null;
		BigDecimal exchangeLocalAmt = null;
		BigDecimal commissionAmt = null;

		if(settlementExchangeRate != null) {
			LOGGER.info("Settlement Exchange Rate : "+settlementExchangeRate);

			CurrencyMasterModel currencyMasterModel = fetchCurrencyMasterDetails(PricerServiceConstants.SETTLEMENT_CURRENCY_CODE, PricerServiceConstants.Yes);
			if(currencyMasterModel != null) {
				validateExchangeRate(settlementExchangeRate,currencyMasterModel.getFundMinRate(),currencyMasterModel.getFundMaxRate());

				ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
				productDetailsDTO.setCountryId(srvPrvFeeInqReqDTO.getDestinationCountryId());
				productDetailsDTO.setBankId(srvPrvFeeInqReqDTO.getRoutingBankId());
				productDetailsDTO.setCurrencyId(srvPrvFeeInqReqDTO.getForeignCurrencyId());
				productDetailsDTO.setRemittanceId(srvPrvFeeInqReqDTO.getRemittanceId());
				productDetailsDTO.setDeliveryId(srvPrvFeeInqReqDTO.getDeliveryId());

				ServiceProviderRateView serviceProviderRateView = fetchMarginByProduct(productDetailsDTO);
				if(serviceProviderRateView != null) {
					marginAmount = serviceProviderRateView.getMargin() == null ? BigDecimal.ZERO : serviceProviderRateView.getMargin();

					amxRateWithMargin = settlementExchangeRate.add(marginAmount);
					settlementAmtwithDecimal = new BigDecimal(srvPrvFeeInqReqDTO.getAmount().doubleValue()/amxRateWithMargin.doubleValue());
					DEF_DECIMAL_SCALE = (currencyMasterModel.getDecinalNumber() == null ? new BigDecimal(2) : currencyMasterModel.getDecinalNumber()).intValue();
					settlementAmount = settlementAmtwithDecimal.round(DEF_CONTEXT);
					LOGGER.info("Amx Exchange Rate with Margin : "+amxRateWithMargin);
					LOGGER.info("settlement Amount : "+settlementAmtwithDecimal);
					LOGGER.info("settlement Amount with round : "+settlementAmount);

					quotationResponse = fetchQuotationDetails(srvPrvFeeInqReqDTO, serviceProviderRateView, settlementAmount);

					if(quotationResponse != null) {
						if(quotationResponse.getResult() != null) {
							Quotation_Call_Response quotationCall = quotationResponse.getResult();
							ServiceProviderResponse serviceProviderResponse = (ServiceProviderResponse) quotationCall;
							// success return I
							if(quotationCall != null && quotationCall.getAction_ind() != null && quotationCall.getAction_ind().equalsIgnoreCase(PricerServiceConstants.ACTION_IND_I)) {

								hsForeignSettleCurrencyRate = quotationCall.getWhole_sale_fx_rate();
								destinationAmt = quotationCall.getCredited_amount_in_destination_currency();
								hsCommissionAmt = quotationCall.getFix_charged_amount_in_settlement_currency().add(quotationCall.getVariable_charged_amount_in_settlement_currency());
								hsfeeInquiryRateNew = quotationCall.getWhole_sale_fx_rate();

								BankServiceRule bankServiceRule = fetchBankserviceRule(productDetailsDTO);
								if(bankServiceRule != null) {
									BankCharges bankCharges = fetchBankChargesServiceProvider(bankServiceRule.getBankServiceRuleId(), destinationAmt, PricerServiceConstants.BOTH_BANK_SERVICE_COMPONENT, PricerServiceConstants.CHARGES_TYPE);
									if(bankCharges != null) {
										amiecCommissionAmt =  bankCharges.getChargeAmount();
										exchangeRate = new BigDecimal(settlementExchangeRate.doubleValue()/hsForeignSettleCurrencyRate.doubleValue());
										DEF_DECIMAL_SCALE = (new BigDecimal(6)).intValue();
										exchangeRate = exchangeRate.round(DEF_CONTEXT);

										// formula
										exchangeRatewithpips = new BigDecimal(amxRateWithMargin.doubleValue()/hsForeignSettleCurrencyRate.doubleValue());
										DEF_DECIMAL_SCALE = (new BigDecimal(6)).intValue();
										exchangeRatewithpips = exchangeRatewithpips.round(DEF_CONTEXT);
										exchangeLocalAmt = destinationAmt.multiply(exchangeRatewithpips);

										commissionAmt = amiecCommissionAmt.add(hsCommissionAmt.multiply(settlementExchangeRate));

										srvPrvFeeInqResDTO = new SrvPrvFeeInqResDTO();
										srvPrvFeeInqResDTO.setCommissionAmount(commissionAmt);
										srvPrvFeeInqResDTO.setExchangeRateByServiceProvider(hsForeignSettleCurrencyRate);
										srvPrvFeeInqResDTO.setExchangeRateWithLocalAndSettlementCurrency(settlementExchangeRate);
										srvPrvFeeInqResDTO.setExchangeRateWithPips(exchangeRatewithpips);
										srvPrvFeeInqResDTO.setForeignAmount(destinationAmt);
										srvPrvFeeInqResDTO.setGrossAmount(exchangeLocalAmt);
										srvPrvFeeInqResDTO.setLocalAmount(exchangeLocalAmt.add(commissionAmt));

										HomeSendInfoDTO homeSendInfoDTO = new HomeSendInfoDTO();
										homeSendInfoDTO.setAction_ind(serviceProviderResponse.getAction_ind());
										homeSendInfoDTO.setCredited_amount_in_destination_currency(quotationCall.getCredited_amount_in_destination_currency());
										homeSendInfoDTO.setDestination_currency(quotationCall.getDestination_currency());
										homeSendInfoDTO.setFix_charged_amount_in_settlement_currency(quotationCall.getFix_charged_amount_in_settlement_currency());
										homeSendInfoDTO.setInitial_amount_in_settlement_currency(quotationCall.getInitial_amount_in_settlement_currency());
										homeSendInfoDTO.setOffer_expiration_date(quotationCall.getOffer_expiration_date());
										homeSendInfoDTO.setPartner_transaction_reference(quotationCall.getPartner_transaction_reference());
										homeSendInfoDTO.setRequest_XML(serviceProviderResponse.getRequest_XML());
										homeSendInfoDTO.setResponse_code(serviceProviderResponse.getResponse_code());
										homeSendInfoDTO.setResponse_description(serviceProviderResponse.getResponse_description());
										homeSendInfoDTO.setResponse_XML(serviceProviderResponse.getResponse_XML());
										homeSendInfoDTO.setSettlement_currency(quotationCall.getSettlement_currency());
										homeSendInfoDTO.setTechnical_details(serviceProviderResponse.getTechnical_details());
										homeSendInfoDTO.setTotal_charged_amount_in_settlement_currency(quotationCall.getTotal_charged_amount_in_settlement_currency());
										homeSendInfoDTO.setVariable_charged_amount_in_settlement_currency(quotationCall.getVariable_charged_amount_in_settlement_currency());
										homeSendInfoDTO.setWhole_sale_fx_rate(quotationCall.getWhole_sale_fx_rate());
										homeSendInfoDTO.setBeneficiaryDeduct(Boolean.FALSE);

										srvPrvFeeInqResDTO.setHomeSendInfoDTO(homeSendInfoDTO);
									}
								}
							}else {
								LOGGER.warn("Service Provider Data Issue : None Found " + JsonUtil.toJson(quotationCall));
								throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
										"Service Provider Data Issue : None Found " + JsonUtil.toJson(quotationCall));
							}
						}else {
							LOGGER.warn("Service Provider Data Issue : None Found " + JsonUtil.toJson(quotationResponse.getResult()));
							throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
									"Service Provider Data Issue : None Found " + JsonUtil.toJson(quotationResponse.getResult()));
						}
					}else {
						LOGGER.warn("Service Provider Data Issue" + JsonUtil.toJson(quotationResponse));
						throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
								"Service Provider Data Issue : None Found " + JsonUtil.toJson(quotationResponse));
					}
				}else {
					// fail
					LOGGER.warn("Selling Rate : None Found " + JsonUtil.toJson(productDetailsDTO));
					throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
							"Selling Rate : None Found " + JsonUtil.toJson(productDetailsDTO));
				}
			}else {
				// fail
				LOGGER.warn("No ISO Currency Code Details : None Found ");
				throw new PricerServiceException(PricerServiceError.INVALID_CURRENCY,
						"No ISO Currency Code Details : None Found ");
			}
		}else {
			LOGGER.warn("Missing Settlement Currency Exchange Rate : " + settlementExchangeRate );
			throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
					"Missing Settlement Currency Exchange Rate : " + settlementExchangeRate );
		}

		return srvPrvFeeInqResDTO;
	}

	// fetch get quotation for foreign currency
	public SrvPrvFeeInqResDTO fetchQuotationForForeignCurrency(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {
		AmxApiResponse<Quotation_Call_Response, Object> quotationResponse = null;
		SrvPrvFeeInqResDTO srvPrvFeeInqResDTO = null;

		BigDecimal amxRateWithMargin = null;
		BigDecimal localAmt = null;
		BigDecimal marginAmount = null;
		BigDecimal settlementAmount = null;

		BigDecimal hsForeignSettleCurrencyRate = null;
		BigDecimal destinationAmt = null;
		BigDecimal hsCommissionAmt = null;
		BigDecimal hsfeeInquiryRateNew = null;
		BigDecimal settlementAmt = null;

		BigDecimal amiecCommissionAmt = null;
		BigDecimal exchangeRate = null;
		BigDecimal exchangeRatewithpips = null;
		BigDecimal exchangeLocalAmt = null;
		BigDecimal commissionAmt = null;

		ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
		productDetailsDTO.setCountryId(srvPrvFeeInqReqDTO.getDestinationCountryId());
		productDetailsDTO.setBankId(srvPrvFeeInqReqDTO.getRoutingBankId());
		productDetailsDTO.setCurrencyId(srvPrvFeeInqReqDTO.getForeignCurrencyId());
		productDetailsDTO.setRemittanceId(srvPrvFeeInqReqDTO.getRemittanceId());
		productDetailsDTO.setDeliveryId(srvPrvFeeInqReqDTO.getDeliveryId());

		ServiceProviderRateView serviceProviderRateView = fetchMarginByProduct(productDetailsDTO);
		if(serviceProviderRateView != null) {
			quotationResponse = fetchQuotationDetails(srvPrvFeeInqReqDTO, serviceProviderRateView, srvPrvFeeInqReqDTO.getAmount());

			if(quotationResponse != null) {
				if(quotationResponse.getResult() != null) {
					Quotation_Call_Response quotationCall = quotationResponse.getResult();
					ServiceProviderResponse serviceProviderResponse = (Quotation_Call_Response) quotationCall;
					// success return I
					if(quotationCall != null && quotationCall.getAction_ind() != null && quotationCall.getAction_ind().equalsIgnoreCase(PricerServiceConstants.ACTION_IND_I)) {
						BigDecimal settlementExchangeRate = fetchUsdExchangeRate();
						if(settlementExchangeRate != null) {
							LOGGER.info("Settlement Exchange Rate : "+settlementExchangeRate);
							CurrencyMasterModel currencyMasterModel = fetchCurrencyMasterDetails(PricerServiceConstants.SETTLEMENT_CURRENCY_CODE, PricerServiceConstants.Yes);
							if(currencyMasterModel != null) {
								validateExchangeRate(settlementExchangeRate,currencyMasterModel.getFundMinRate(),currencyMasterModel.getFundMaxRate());

								hsForeignSettleCurrencyRate = quotationCall.getWhole_sale_fx_rate();
								destinationAmt = quotationCall.getCredited_amount_in_destination_currency();
								hsCommissionAmt = quotationCall.getFix_charged_amount_in_settlement_currency().add(quotationCall.getVariable_charged_amount_in_settlement_currency());
								hsfeeInquiryRateNew = quotationCall.getWhole_sale_fx_rate();
								settlementAmt = quotationCall.getTotal_charged_amount_in_settlement_currency();

								marginAmount = serviceProviderRateView.getMargin() == null ? BigDecimal.ZERO : serviceProviderRateView.getMargin();

								amxRateWithMargin = settlementExchangeRate.add(marginAmount);
								localAmt = settlementAmt.multiply(amxRateWithMargin);
								DEF_DECIMAL_SCALE = (currencyMasterModel.getDecinalNumber() == null ? new BigDecimal(3) : currencyMasterModel.getDecinalNumber()).intValue();
								localAmt = localAmt.round(DEF_CONTEXT);

								BankServiceRule bankServiceRule = fetchBankserviceRule(productDetailsDTO);
								if(bankServiceRule != null) {
									BankCharges bankCharges = fetchBankChargesServiceProvider(bankServiceRule.getBankServiceRuleId(), destinationAmt, PricerServiceConstants.BOTH_BANK_SERVICE_COMPONENT, PricerServiceConstants.CHARGES_TYPE);
									if(bankCharges != null) {
										amiecCommissionAmt =  bankCharges.getChargeAmount();
										exchangeRate = new BigDecimal(settlementExchangeRate.doubleValue()/hsForeignSettleCurrencyRate.doubleValue());
										DEF_DECIMAL_SCALE = (new BigDecimal(6)).intValue();
										exchangeRate = exchangeRate.round(DEF_CONTEXT);

										// formula
										exchangeRatewithpips = new BigDecimal(amxRateWithMargin.doubleValue()/hsForeignSettleCurrencyRate.doubleValue());
										DEF_DECIMAL_SCALE = (new BigDecimal(6)).intValue();
										exchangeRatewithpips = exchangeRatewithpips.round(DEF_CONTEXT);
										exchangeLocalAmt = destinationAmt.multiply(exchangeRatewithpips);

										commissionAmt = amiecCommissionAmt.add(hsCommissionAmt.multiply(settlementExchangeRate));

										srvPrvFeeInqResDTO = new SrvPrvFeeInqResDTO();
										srvPrvFeeInqResDTO.setCommissionAmount(commissionAmt);
										srvPrvFeeInqResDTO.setExchangeRateByServiceProvider(hsForeignSettleCurrencyRate);
										srvPrvFeeInqResDTO.setExchangeRateWithLocalAndSettlementCurrency(settlementExchangeRate);
										srvPrvFeeInqResDTO.setExchangeRateWithPips(exchangeRatewithpips);
										srvPrvFeeInqResDTO.setForeignAmount(destinationAmt);
										srvPrvFeeInqResDTO.setGrossAmount(exchangeLocalAmt);
										srvPrvFeeInqResDTO.setLocalAmount(exchangeLocalAmt.add(commissionAmt));

										HomeSendInfoDTO homeSendInfoDTO = new HomeSendInfoDTO();
										homeSendInfoDTO.setAction_ind(serviceProviderResponse.getAction_ind());
										homeSendInfoDTO.setCredited_amount_in_destination_currency(quotationCall.getCredited_amount_in_destination_currency());
										homeSendInfoDTO.setDestination_currency(quotationCall.getDestination_currency());
										homeSendInfoDTO.setFix_charged_amount_in_settlement_currency(quotationCall.getFix_charged_amount_in_settlement_currency());
										homeSendInfoDTO.setInitial_amount_in_settlement_currency(quotationCall.getInitial_amount_in_settlement_currency());
										homeSendInfoDTO.setOffer_expiration_date(quotationCall.getOffer_expiration_date());
										homeSendInfoDTO.setPartner_transaction_reference(quotationCall.getPartner_transaction_reference());
										homeSendInfoDTO.setRequest_XML(serviceProviderResponse.getRequest_XML());
										homeSendInfoDTO.setResponse_code(serviceProviderResponse.getResponse_code());
										homeSendInfoDTO.setResponse_description(serviceProviderResponse.getResponse_description());
										homeSendInfoDTO.setResponse_XML(serviceProviderResponse.getResponse_XML());
										homeSendInfoDTO.setSettlement_currency(quotationCall.getSettlement_currency());
										homeSendInfoDTO.setTechnical_details(serviceProviderResponse.getTechnical_details());
										homeSendInfoDTO.setTotal_charged_amount_in_settlement_currency(quotationCall.getTotal_charged_amount_in_settlement_currency());
										homeSendInfoDTO.setVariable_charged_amount_in_settlement_currency(quotationCall.getVariable_charged_amount_in_settlement_currency());
										homeSendInfoDTO.setWhole_sale_fx_rate(quotationCall.getWhole_sale_fx_rate());
										homeSendInfoDTO.setBeneficiaryDeduct(Boolean.FALSE);

										srvPrvFeeInqResDTO.setHomeSendInfoDTO(homeSendInfoDTO);
									}
								}
							}else {
								// fail
								LOGGER.warn("No ISO Currency Code Details : None Found ");
								throw new PricerServiceException(PricerServiceError.INVALID_CURRENCY,
										"No ISO Currency Code Details : None Found ");
							}
						}else {
							LOGGER.warn("Missing Settlement Currency Exchange Rate : " + settlementExchangeRate );
							throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
									"Missing Settlement Currency Exchange Rate : " + settlementExchangeRate );
						}
					}else {
						LOGGER.warn("Service Provider Data Issue : None Found " + JsonUtil.toJson(quotationCall));
						throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
								"Service Provider Data Issue : None Found " + JsonUtil.toJson(quotationCall));
					}
				}else {
					LOGGER.warn("Service Provider Data Issue : None Found " + JsonUtil.toJson(quotationResponse.getResult()));
					throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
							"Service Provider Data Issue : None Found " + JsonUtil.toJson(quotationResponse.getResult()));
				}
			}else {
				LOGGER.warn("Service Provider Data Issue" + JsonUtil.toJson(quotationResponse));
				throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
						"Service Provider Data Issue : None Found " + JsonUtil.toJson(quotationResponse));
			}
		}else {
			// fail
			LOGGER.warn("Selling Rate : None Found " + JsonUtil.toJson(productDetailsDTO));
			throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
					"Selling Rate : None Found " + JsonUtil.toJson(productDetailsDTO));
		}
		return srvPrvFeeInqResDTO;
	}

	// home send quotation call
	public AmxApiResponse<Quotation_Call_Response, Object> fetchQuotationDetails(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO,ServiceProviderRateView serviceProviderRateView,BigDecimal amount) {

		ServiceProviderCallRequestDto quatationRequestDto = new ServiceProviderCallRequestDto();
		AmxApiResponse<Quotation_Call_Response, Object> srvPrvResp = null;
		Benificiary beneficiaryDto = new Benificiary();
		Customer customerDto = new Customer();
		TransactionData transactionDto = new TransactionData();

		String beneFirstName = null;
		String beneMiddleName = null;
		String beneLastName = null;

		try {
			CustomerDetailsDTO customerDetailsDTO = fetchCustomerDetails(srvPrvFeeInqReqDTO.getCustomerId());
			BeneficiaryDetailsDTO beneficiaryDetailsDTO = fetchBeneficiaryDetails(srvPrvFeeInqReqDTO.getCustomerId(), srvPrvFeeInqReqDTO.getBeneficiaryRelationShipId());

			if(srvPrvFeeInqReqDTO.getSelectedCurrency().compareTo(srvPrvFeeInqReqDTO.getLocalCurrencyId()) == 0){
				transactionDto.setSettlement_amount(amount);
			}else{
				transactionDto.setDestination_amount(amount);
			}
			transactionDto.setSettlement_currency(PricerServiceConstants.SETTLEMENT_CURRENCY_CODE);
			transactionDto.setDestination_currency(serviceProviderRateView.getCurrencyCode());

			HashMap<String, Object> designationAlphaCodes = fetchCountryAlphaCode(beneficiaryDetailsDTO.getBenificaryCountry());
			if(designationAlphaCodes != null && !designationAlphaCodes.isEmpty()) {
				if(designationAlphaCodes.get("countryAlpha2Code") != null) {
					String designationCountryAlpha2Code = (String)designationAlphaCodes.get("countryAlpha2Code");
					transactionDto.setDestination_country_2_digit_ISO(designationCountryAlpha2Code);
				}
				if(designationAlphaCodes.get("countryAlpha3Code") != null) {
					String designationCountryAlpha3Code = (String)designationAlphaCodes.get("countryAlpha3Code");
					transactionDto.setDestination_country_3_digit_ISO(designationCountryAlpha3Code);
				}
			}

			HashMap<String, Object> originAlphaCodes = fetchCountryAlphaCode(srvPrvFeeInqReqDTO.getApplicationCountryId());
			if(originAlphaCodes != null && !originAlphaCodes.isEmpty()) {
				if(originAlphaCodes.get("countryAlpha3Code") != null) {
					String originCountryAlpha3Code = (String)originAlphaCodes.get("countryAlpha3Code");
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
			beneficiaryDto.setBeneficiary_type(beneficiaryDetailsDTO.getBenificaryStatusName());

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
			beneficiaryDto.setBeneficiary_account_number(beneficiaryDetailsDTO.getBankAccountNumber());

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

			beneficiaryDto.setBeneficiary_bank_code(beneficiaryDetailsDTO.getBankCode());
			if(beneficiaryDetailsDTO.getBranchCode() != null) {
				beneficiaryDto.setBeneficiary_branch_code(beneficiaryDetailsDTO.getBranchCode().toString());
			}
			beneficiaryDto.setBeneficiary_bank_branch_swift_code(beneficiaryDetailsDTO.getSwiftBic());
			if(beneficiaryDetailsDTO.getServiceProvider() != null) {
				beneficiaryDto.setWallet_service_provider(beneficiaryDetailsDTO.getServiceProvider().toString());
			}

			quatationRequestDto.setBeneficiaryDto(beneficiaryDto);
			quatationRequestDto.setCustomerDto(customerDto);
			quatationRequestDto.setTransactionDto(transactionDto);

			LOGGER.info("Inputs passed to Service Provider Home Send : " + JsonUtil.toJson(quatationRequestDto));
			srvPrvResp = serviceProviderClient.getQuatation(quatationRequestDto);
			LOGGER.info("Output from Service Provider Home Send : " + JsonUtil.toJson(srvPrvResp));

		}catch (Exception e) {
			LOGGER.error("Unable to get response from Service Provider Home Send : Exception " +e);
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
		ParameterDetails parameterDetails = partnerServiceDao.fetchServPrvBankCode("HSBS",beneCountryCode);
		if(parameterDetails != null) {
			mapParam.put("BIC_CODE", parameterDetails.getCharField3());
			mapParam.put("BANK_BRANCH", parameterDetails.getCharField4());
		}
		return mapParam;
	}

	// fetch alpha codes
	public HashMap<String, Object> fetchCountryAlphaCode(BigDecimal countryId){
		HashMap<String, Object> countryObj = new HashMap<String, Object>();
		CountryMasterDT countryAplhaCode = partnerServiceDao.fetchCountryMasterDetails(countryId);
		if(countryAplhaCode != null){
			countryObj.put("countryAlpha2Code", countryAplhaCode.getCountryAlpha2Code());
			countryObj.put("countryAlpha3Code", countryAplhaCode.getCountryAlpha3Code());
		}
		return countryObj;
	}

	// fetch beneficiary details
	public BeneficiaryDetailsDTO fetchBeneficiaryDetails(BigDecimal customerId,BigDecimal beneficiaryRelationShipId) {
		BeneficiaryDetailsDTO beneficiaryDto = new BeneficiaryDetailsDTO();
		BenificiaryListView beneficaryDetails = partnerServiceDao.getBeneficiaryDetails(customerId,beneficiaryRelationShipId);
		if(beneficaryDetails != null) {
			try {
				BeanUtils.copyProperties(beneficiaryDto, beneficaryDetails);
			} catch (IllegalAccessException | InvocationTargetException e) {
				LOGGER.error("Unable to convert Beneficiary Details : None Found : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId + " Exception " +e);
				throw new PricerServiceException(PricerServiceError.INVALID_BENEFICIARY,
						"Unable to convert Beneficiary Details : None Found : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId);
			}
		}else {
			// fail
			LOGGER.warn("Invalid Beneficiary Details : None Found : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId);
			throw new PricerServiceException(PricerServiceError.INVALID_BENEFICIARY,
					"Invalid Beneficiary Details : None Found : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId);
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
				throw new PricerServiceException(PricerServiceError.INVALID_CUSTOMER,
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
		ServiceProviderRateView serviceProviderRateView = partnerServiceDao.fetchMarginByProduct(productDetailsDTO.getCountryId(), productDetailsDTO.getBankId(), productDetailsDTO.getCurrencyId(), productDetailsDTO.getRemittanceId(), productDetailsDTO.getDeliveryId());
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
	public BankCharges fetchBankChargesServiceProvider(BigDecimal bankServiceRuleId,BigDecimal fcAmount,BigDecimal chargesFor,String chargesType) {
		BankCharges bankChargesDt = new BankCharges();
		List<BankCharges> bankCharges = partnerServiceDao.fetchBankChargesDetails(bankServiceRuleId, fcAmount, chargesFor, chargesType);
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

		if(null == srvPrvFeeInqReqDTO.getCustomerId()) {
			LOGGER.warn("Invalid Customer : None Found : " + srvPrvFeeInqReqDTO.getCustomerId());
			throw new PricerServiceException(PricerServiceError.INVALID_CUSTOMER,
					"Invalid Customer : None Found : " + srvPrvFeeInqReqDTO.getCustomerId());
		}

		if(null == srvPrvFeeInqReqDTO.getBeneficiaryRelationShipId()) {
			LOGGER.warn("Invalid Beneficiary Relation Id : None Found : " + srvPrvFeeInqReqDTO.getBeneficiaryRelationShipId());
			throw new PricerServiceException(PricerServiceError.INVALID_BENEFICIARY_RELATIONSHIP_ID,
					"Invalid Beneficiary Relation Id : None Found : " + srvPrvFeeInqReqDTO.getBeneficiaryRelationShipId());
		}

		if(null == srvPrvFeeInqReqDTO.getForeignCurrencyId()) {
			LOGGER.warn("Invalid Foreign Currency Id : None Found : " + srvPrvFeeInqReqDTO.getForeignCurrencyId());
			throw new PricerServiceException(PricerServiceError.INVALID_CURRENCY,
					"Invalid Foreign Currency Id : None Found : " + srvPrvFeeInqReqDTO.getForeignCurrencyId());
		}

		if(null == srvPrvFeeInqReqDTO.getLocalCurrencyId()) {
			LOGGER.warn("Invalid local Currency Id : None Found : " + srvPrvFeeInqReqDTO.getLocalCurrencyId());
			throw new PricerServiceException(PricerServiceError.INVALID_CURRENCY,
					"Invalid local Currency Id : None Found : " + srvPrvFeeInqReqDTO.getLocalCurrencyId());
		}

		if(null == srvPrvFeeInqReqDTO.getSelectedCurrency()) {
			LOGGER.warn("Invalid selected Currency Id : None Found : " + srvPrvFeeInqReqDTO.getSelectedCurrency());
			throw new PricerServiceException(PricerServiceError.INVALID_CURRENCY,
					"Invalid selected Currency Id : None Found : " + srvPrvFeeInqReqDTO.getSelectedCurrency());
		}

		if(null == srvPrvFeeInqReqDTO.getRemittanceId()) {
			LOGGER.warn("Invalid Remittance Id : None Found : " + srvPrvFeeInqReqDTO.getRemittanceId());
			throw new PricerServiceException(PricerServiceError.INVALID_REMITTANCE_ID,
					"Invalid Remittance Id : None Found : " + srvPrvFeeInqReqDTO.getRemittanceId());
		}

		if(null == srvPrvFeeInqReqDTO.getDeliveryId()) {
			LOGGER.warn("Invalid Delivery Id : None Found : " + srvPrvFeeInqReqDTO.getDeliveryId());
			throw new PricerServiceException(PricerServiceError.INVALID_DELIVERY_ID,
					"Invalid Delivery Id : None Found : " + srvPrvFeeInqReqDTO.getDeliveryId());
		}

		if(null == srvPrvFeeInqReqDTO.getAmount()) {
			LOGGER.warn("Invalid Amount : None Found : " + srvPrvFeeInqReqDTO.getAmount());
			throw new PricerServiceException(PricerServiceError.MISSING_AMOUNT,
					"Invalid Amount : None Found : " + srvPrvFeeInqReqDTO.getAmount());
		}

		if(null == srvPrvFeeInqReqDTO.getApplicationCountryId()) {
			LOGGER.warn("Invalid Application Country Id : None Found : " + srvPrvFeeInqReqDTO.getApplicationCountryId());
			throw new PricerServiceException(PricerServiceError.MISSING_COUNTRY_ID,
					"Invalid Application Country Id : None Found : " + srvPrvFeeInqReqDTO.getApplicationCountryId());
		}

		if(null == srvPrvFeeInqReqDTO.getDestinationCountryId()) {
			LOGGER.warn("Invalid Destination Country Id : None Found : " + srvPrvFeeInqReqDTO.getDestinationCountryId());
			throw new PricerServiceException(PricerServiceError.MISSING_COUNTRY_ID,
					"Invalid Destination Country Id : None Found : " + srvPrvFeeInqReqDTO.getDestinationCountryId());
		}

		if(null == srvPrvFeeInqReqDTO.getRoutingBankId()) {
			LOGGER.warn("Invalid Routing Bank Id : None Found : " + srvPrvFeeInqReqDTO.getRoutingBankId());
			throw new PricerServiceException(PricerServiceError.MISSING_ROUTING_BANK_IDS,
					"Invalid Routing Bank Id : None Found : " + srvPrvFeeInqReqDTO.getRoutingBankId());
		}

	}

	// validate the exchange Rate min and max
	public void validateExchangeRate(BigDecimal exchangeRate,BigDecimal fundMinRate,BigDecimal fundMaxRate) {

		if(null == exchangeRate) {
			LOGGER.warn("Missing Settlement Currency Exchange Rate : " + exchangeRate);
			throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
					"Missing Settlement Currency Exchange Rate : " + exchangeRate );
		}

		if(null == fundMinRate) {
			LOGGER.warn("Missing Fund Min Exchange Rate : " + fundMinRate);
			throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
					"Missing Fund Min Exchange Rate : " + fundMinRate);
		}

		if(null == fundMaxRate) {
			LOGGER.warn("Missing Fund Max Exchange Rate : " + fundMaxRate);
			throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
					"Missing Fund Max Exchange Rate : " + fundMaxRate);
		}

		if(exchangeRate.compareTo(fundMinRate) >= 0 && exchangeRate.compareTo(fundMaxRate) <= 0) {
			// valid
		}else {
			LOGGER.warn("Settlement Exchange Rate not in min and max : exchangeRate : " + exchangeRate + " : fundMinRate :" + fundMinRate + " : fundMaxRate : " + fundMaxRate);
			throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
					"Settlement Exchange Rate not in min and max : exchangeRate : " + exchangeRate + " : fundMinRate :" + fundMinRate + " : fundMaxRate : " + fundMaxRate);
		}
	}

}
