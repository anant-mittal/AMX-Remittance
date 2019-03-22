package com.amx.jax.exrateservice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.config.JaxProperties;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.dbmodel.PipsMaster;
import com.amx.jax.error.JaxError;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.remittance.manager.RemittanceParameterMapManager;
import com.amx.jax.util.RoundUtil;

/**
 * @author Prashant
 *
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class NewExchangeRateService extends ExchangeRateService {

	@Autowired
	JaxDynamicPriceService jaxDynamicPriceService;
	@Autowired
	JaxTenantProperties jaxTenantProperties;
	@Autowired
	RemittanceParameterMapManager remittanceParameterMapManager;
	@Autowired
	RoutingDetailService routingDetailService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.exrateservice.service.ExchangeRateService#
	 * getExchangeRatesForOnline(java.math.BigDecimal, java.math.BigDecimal,
	 * java.math.BigDecimal, java.math.BigDecimal)
	 */
	public ApiResponse<ExchangeRateResponseModel> getExchangeRatesForOnline(BigDecimal fromCurrency,
			BigDecimal toCurrency, BigDecimal lcAmount, BigDecimal routingBankId, BigDecimal beneBankCountryId) {
		ExchangeRateResponseModel outputModel = new ExchangeRateResponseModel();
		ApiResponse<ExchangeRateResponseModel> response = getBlackApiResponse();
		if (jaxTenantProperties.getIsDynamicPricingEnabled() && beneBankCountryId != null
				&& !remittanceParameterMapManager.isCashChannel()) {
			try {
				outputModel = jaxDynamicPriceService.getExchangeRatesWithDiscount(fromCurrency, toCurrency, lcAmount,
						null, beneBankCountryId, routingBankId);
			} catch (Exception e) {
			}
			List<BankMasterDTO> cashChannelRates = getCashRateFromBestRateLogic(fromCurrency, toCurrency, lcAmount,
					routingBankId, beneBankCountryId);
			List<BankMasterDTO> bankChannelRates = outputModel.getBankWiseRates();
			outputModel.setBankWiseRates(
					NewExchangeRateService.mergeExchangeRateResponse(bankChannelRates, cashChannelRates));
			response.getData().getValues().add(outputModel);
			response.getData().setType(outputModel.getModelType());
		} else if (!jaxTenantProperties.getExrateBestRateLogicEnable()) {
			response = super.getExchangeRatesForOnline(fromCurrency, toCurrency, lcAmount, routingBankId);
		} else {
			response = getExchangeRateFromBestRateLogic(fromCurrency, toCurrency, lcAmount, routingBankId,
					beneBankCountryId);
		}
		sortRates(response);
		addCashPayoutText(response);
		checkExchangeRateResponse(response);
		
		return response;
	}

	private void checkExchangeRateResponse(ApiResponse<ExchangeRateResponseModel> response) {
		ExchangeRateResponseModel exchangeRateResponseModel = response.getResult();
		List<BankMasterDTO> exRates = exchangeRateResponseModel.getBankWiseRates();
		if (CollectionUtils.isEmpty(exRates)) {
			throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
		}
		if (exchangeRateResponseModel.getExRateBreakup() == null) {
			exchangeRateResponseModel.setExRateBreakup(exRates.get(0).getExRateBreakup());
		}
	}

	private void addCashPayoutText(ApiResponse<ExchangeRateResponseModel> response) {
		List<BankMasterDTO> exchangeRates = response.getResult().getBankWiseRates();
		exchangeRates.forEach(i -> {
			boolean isSameBankId = isSameBankId(i, exchangeRates);
			if (Boolean.TRUE.equals(i.getIsCashPayout()) && isSameBankId) {
				i.setBankFullName(i.getBankFullName() + " CASH PAYOUT");
				i.setBankCode(i.getBankCode() + "_C");
			}
		});
	}

	private boolean isSameBankId(BankMasterDTO rate, List<BankMasterDTO> exchangeRates) {
		long matches = exchangeRates.stream().filter(i -> i.getBankId().equals(rate.getBankId())).count();
		return matches > 1;
	}

	private void sortRates(ApiResponse<ExchangeRateResponseModel> response) {
		List<BankMasterDTO> exchangeRates = response.getResult().getBankWiseRates();
		Collections.sort(exchangeRates);
	}

	private ApiResponse<ExchangeRateResponseModel> getExchangeRateFromBestRateLogic(BigDecimal fromCurrency,
			BigDecimal toCurrency, BigDecimal lcAmount, BigDecimal routingBankId, BigDecimal beneBankCountryId) {
		ExchangeRateResponseModel outputModel = null;
		ApiResponse<ExchangeRateResponseModel> response = getBlackApiResponse();
		logger.info("In getExchangeRatesForOnline, parames- " + fromCurrency + " toCurrency " + toCurrency + " amount "
				+ lcAmount + " bankId: " + routingBankId);
		if (fromCurrency.equals(meta.getDefaultCurrencyId())) {
			List<PipsMaster> pips = pipsDao.getPipsForOnline(toCurrency);
			if (pips == null || pips.isEmpty()) {
				throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
			}
			validateExchangeRateInputdata(lcAmount);
			List<BigDecimal> validBankIds = exchangeRateProcedureDao.getBankIdsForExchangeRates(toCurrency);

			if (validBankIds.isEmpty()) {
				throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
			}

			List<BankMasterDTO> bankWiseRates = chooseBankWiseRates(toCurrency, lcAmount, meta.getCountryBranchId(),
					validBankIds);
			if (bankWiseRates == null || bankWiseRates.isEmpty()) {
				throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
			}
			outputModel = new ExchangeRateResponseModel();
			outputModel.setBankWiseRates(bankWiseRates);
			if (routingBankId != null) {
				outputModel.setExRateBreakup(
						getExchangeRateBreakUpUsingBestRate(toCurrency, lcAmount, null, routingBankId));
			} else {
				outputModel.setExRateBreakup(bankWiseRates.get(0).getExRateBreakup());
			}
			response.getData().getValues().add(outputModel);
			response.getData().setType(outputModel.getModelType());
		}
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	/**
	 * Only for best rate logic
	 * 
	 * @param toCurrency
	 * @param lcAmount
	 * @param fcAmount
	 * @param bankId
	 * @return
	 */
	public ExchangeRateBreakup getExchangeRateBreakUpUsingBestRate(BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal fcAmount, BigDecimal bankId) {
		List<PipsMaster> pips = null;
		if (lcAmount != null) {
			pips = pipsDao.getPipsMasterForLocalAmount(toCurrency, lcAmount, meta.getCountryBranchId(), bankId);
		}
		if (fcAmount != null) {
			pips = pipsDao.getPipsMasterForForeignAmount(toCurrency, fcAmount, meta.getCountryBranchId(), bankId);
		}
		if (pips == null || pips.isEmpty()) {
			throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
		}

		if (fcAmount != null) {
			return createBreakUpFromForeignCurrency(pips.get(0).getDerivedSellRate(), fcAmount);
		} else {
			return createBreakUp(pips.get(0).getDerivedSellRate(), lcAmount);
		}
	}

	/**
	 * fetch exchange rates from dynamic pricing api
	 * 
	 * @param toCurrency
	 * @param lcAmount
	 * @param fcAmount
	 * @param countryId
	 * @param routingBankId
	 * @return
	 */
	public ExchangeRateBreakup getExchangeRateBreakUpUsingDynamicPricing(BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal fcAmount, BigDecimal countryId, BigDecimal routingBankId) {
		ExchangeRateResponseModel exchangeRateResponseModel = jaxDynamicPriceService.getExchangeRatesWithDiscount(
				meta.getDefaultCurrencyId(), toCurrency, lcAmount, fcAmount, countryId, routingBankId);
		return exchangeRateResponseModel.getExRateBreakup();
	}

	/**
	 * return bank wise exchange rates
	 * 
	 * @param toCurrency
	 * @param lcAmount
	 * @param countryBranchId
	 * @return
	 * 
	 */
	private List<BankMasterDTO> chooseBankWiseRates(BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal countryBranchId, List<BigDecimal> validBankIds) {
		List<BankMasterDTO> bankMasterDto = new ArrayList<>();

		List<PipsMaster> pips = pipsDao.getPipsMaster(toCurrency, lcAmount, countryBranchId, validBankIds);
		pips.forEach(i -> {
			BankMasterDTO dto = bankMasterService.convert(i.getBankMaster());
			bankMasterDto.add(dto);
			dto.setExRateBreakup(createBreakUp(i.getDerivedSellRate(), lcAmount));

		});
		return bankMasterDto;
	}

	public ExchangeRateBreakup getExchangeRateBreakup(BigDecimal toCurrencyId, BigDecimal localAmount,
			BigDecimal routingBankId, BigDecimal beneBankCountryId) {
		ApiResponse<ExchangeRateResponseModel> apiResponse = getExchangeRatesForOnline(meta.getDefaultCurrencyId(),
				toCurrencyId, localAmount, routingBankId, beneBankCountryId);
		return apiResponse.getResult().getExRateBreakup();
	}

	public BigDecimal getForeignAmount(Map<String, Object> inputTemp) {

		if (inputTemp.get("P_FOREIGN_AMT") != null) {
			return (BigDecimal) inputTemp.get("P_FOREIGN_AMT");
		}

		if (inputTemp.get("P_CALCULATED_FC_AMOUNT") != null) {
			return (BigDecimal) inputTemp.get("P_CALCULATED_FC_AMOUNT");
		}

		BigDecimal localAmount = (BigDecimal) inputTemp.get("P_LOCAL_AMT");
		BigDecimal toCurrencyId = (BigDecimal) inputTemp.get("P_CURRENCY_ID");
		BigDecimal routingBankId = (BigDecimal) inputTemp.get("P_ROUTING_BANK_ID");
		BigDecimal beneBankCountryId = (BigDecimal) inputTemp.get("P_BENEFICIARY_COUNTRY_ID");
		ExchangeRateBreakup exRateBreakup = getExchangeRateBreakup(toCurrencyId, localAmount, routingBankId,
				beneBankCountryId);
		return exRateBreakup.getConvertedFCAmount();

	}

	public ExchangeRateBreakup calcEquivalentAmount(RemittanceTransactionRequestModel request, int fcDecimalNumber) {
		ExchangeRateBreakup breakup = new ExchangeRateBreakup();
		int lcDecimalNumber = meta.getDefaultCurrencyId().intValue();
		if (request.getForeignAmount() != null && request.getDomXRate() != null) {
			BigDecimal convertedLCAmount = request.getForeignAmount().divide(request.getDomXRate(), 2,
					RoundingMode.HALF_UP);
			breakup.setConvertedLCAmount(RoundUtil.roundBigDecimal(convertedLCAmount, lcDecimalNumber));
			breakup.setConvertedFCAmount(request.getForeignAmount());
		}

		if (request.getLocalAmount() != null && request.getDomXRate() != null) {
			BigDecimal convertedFCAmount = request.getDomXRate().multiply(request.getLocalAmount());
			breakup.setConvertedFCAmount(RoundUtil.roundToZeroDecimalPlaces(convertedFCAmount));
			breakup.setConvertedFCAmount(RoundUtil.roundBigDecimal(convertedFCAmount, fcDecimalNumber));
			breakup.setConvertedLCAmount(request.getLocalAmount());
		}
		breakup.setNetAmount(breakup.getConvertedLCAmount());
		breakup.setNetAmountWithoutLoyality(breakup.getConvertedLCAmount());
		breakup.setRate(request.getDomXRate());
		return breakup;

	}
	

	private List<BankMasterDTO> getCashRateFromBestRateLogic(BigDecimal fromCurrency, BigDecimal toCurrency,
			BigDecimal lcAmount, BigDecimal routingBankId, BigDecimal beneBankCountryId) {
		ApiResponse<ExchangeRateResponseModel> exchangeRateResponse = getExchangeRateFromBestRateLogic(fromCurrency,
				toCurrency, lcAmount, routingBankId, beneBankCountryId);
		List<BankMasterDTO> allExchangeRates = exchangeRateResponse.getResult().getBankWiseRates();
		List<BigDecimal> cashRoutingBanks = routingDetailService.getCashRoutingBanks(toCurrency);
		List<BankMasterDTO> allCashRates = allExchangeRates.stream().filter(i -> {
			return cashRoutingBanks.contains(i.getBankId());
		}).map(i -> {
			i.setIsCashPayout(true);
			return i;
		}).collect(Collectors.toList());
		return allCashRates;
	}

	public static List<BankMasterDTO> mergeExchangeRateResponse(List<BankMasterDTO> list1, List<BankMasterDTO> list2) {
		Set<BankMasterDTO> bankMasterDtoSet = new HashSet<>();
		if (list1 != null) {
			bankMasterDtoSet.addAll(list1);
		}
		if (list2 != null) {
			bankMasterDtoSet.addAll(list2);
		}
		return new ArrayList<>(bankMasterDtoSet);
	}
}
