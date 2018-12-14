package com.amx.jax.exrateservice.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateBreakup;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.config.JaxProperties;
import com.amx.jax.dbmodel.PipsMaster;
import com.amx.jax.error.JaxError;
import com.amx.jax.util.RoundUtil;

/**
 * @author Prashant
 *
 */
@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class NewExchangeRateService extends ExchangeRateService {

	@Autowired
	JaxProperties jaxProperties;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.exrateservice.service.ExchangeRateService#
	 * getExchangeRatesForOnline(java.math.BigDecimal, java.math.BigDecimal,
	 * java.math.BigDecimal, java.math.BigDecimal)
	 */
	public ApiResponse<ExchangeRateResponseModel> getExchangeRatesForOnline(BigDecimal fromCurrency, BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal bankId) {
		if (!jaxProperties.getExrateBestRateLogicEnable()) {
			return super.getExchangeRatesForOnline(fromCurrency, toCurrency, lcAmount, bankId);
		}
		logger.info("In getExchangeRatesForOnline, parames- " + fromCurrency + " toCurrency " + toCurrency + " amount "
				+ lcAmount + " bankId: " + bankId);
		ApiResponse<ExchangeRateResponseModel> response = getBlackApiResponse();
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

			List<BankMasterDTO> bankWiseRates = chooseBankWiseRates(toCurrency, lcAmount, meta.getCountryBranchId(), validBankIds);
			if (bankWiseRates == null || bankWiseRates.isEmpty()) {
				throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
			}
			ExchangeRateResponseModel outputModel = new ExchangeRateResponseModel();
			outputModel.setBankWiseRates(bankWiseRates);
			if (bankId != null) {
				outputModel.setExRateBreakup(getExchangeRateBreakUp(toCurrency, lcAmount, null, bankId));
			} else {
				outputModel.setExRateBreakup(bankWiseRates.get(0).getExRateBreakup());
			}
			response.getData().getValues().add(outputModel);
			response.getData().setType(outputModel.getModelType());
		}
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	public ExchangeRateBreakup getExchangeRateBreakUp(BigDecimal toCurrency, BigDecimal lcAmount, BigDecimal fcAmount,
			BigDecimal bankId) {
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
			BigDecimal routingBankId) {
		ApiResponse<ExchangeRateResponseModel> apiResponse = getExchangeRatesForOnline(meta.getDefaultCurrencyId(),
				toCurrencyId, localAmount, routingBankId);
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
		ExchangeRateBreakup exRateBreakup = getExchangeRateBreakup(toCurrencyId, localAmount, routingBankId);
		return exRateBreakup.getConvertedFCAmount();

	}

	public ExchangeRateBreakup calcEquivalentAmount(RemittanceTransactionRequestModel request, int fcDecimalNumber) {
		ExchangeRateBreakup breakup = new ExchangeRateBreakup();
		int lcDecimalNumber = meta.getDefaultCurrencyId().intValue();
		if (request.getForeignAmount() != null && request.getDomXRate() !=null) {
			BigDecimal convertedLCAmount = request.getForeignAmount().divide(request.getDomXRate(), 2,
					RoundingMode.HALF_UP);
			breakup.setConvertedLCAmount(RoundUtil.roundBigDecimal(convertedLCAmount, lcDecimalNumber));
			breakup.setConvertedFCAmount(request.getForeignAmount());
		}

		if (request.getLocalAmount() != null  && request.getDomXRate() !=null) {
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
}
