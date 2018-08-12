package com.amx.jax.exrateservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateBreakup;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.config.JaxProperties;
import com.amx.jax.dbmodel.PipsMaster;

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
				+ lcAmount);
		ApiResponse<ExchangeRateResponseModel> response = getBlackApiResponse();
		if (fromCurrency.equals(meta.getDefaultCurrencyId())) {
			List<PipsMaster> pips = pipsDao.getPipsForOnline(toCurrency);
			if (pips == null || pips.isEmpty()) {
				throw new GlobalException("No exchange data found", JaxError.EXCHANGE_RATE_NOT_FOUND);
			}
			validateExchangeRateInputdata(lcAmount);
			List<BigDecimal> validBankIds = exchangeRateProcedureDao.getBankIdsForExchangeRates(toCurrency);

			if (validBankIds.isEmpty()) {
				throw new GlobalException("No exchange data found", JaxError.EXCHANGE_RATE_NOT_FOUND);
			}

			List<BankMasterDTO> bankWiseRates = chooseBankWiseRates(toCurrency, lcAmount, meta.getCountryBranchId());
			if (bankWiseRates == null || bankWiseRates.isEmpty()) {
				throw new GlobalException("No exchange data found", JaxError.EXCHANGE_RATE_NOT_FOUND);
			}
			ExchangeRateResponseModel outputModel = new ExchangeRateResponseModel();
			outputModel.setBankWiseRates(bankWiseRates);
			if (bankId != null) {
				outputModel.setExRateBreakup(getExchangeRateBreakUp(toCurrency, lcAmount, null, bankId));
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
			pips = pipsDao.getPipsMasterForLocalAmount(toCurrency, fcAmount, meta.getCountryBranchId(), bankId);
		}
		if (pips == null || pips.isEmpty()) {
			throw new GlobalException("No exchange data found", JaxError.EXCHANGE_RATE_NOT_FOUND);
		}

		return createBreakUp(pips.get(0).getDerivedSellRate(), lcAmount);
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
			BigDecimal countryBranchId) {
		List<BankMasterDTO> bankMasterDto = new ArrayList<>();

		List<PipsMaster> pips = pipsDao.getPipsMaster(toCurrency, lcAmount, countryBranchId);
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

		if (inputTemp.get("P_CALCULATED_FC_AMOUNT") != null) {
			return (BigDecimal) inputTemp.get("P_CALCULATED_FC_AMOUNT");
		}
		BigDecimal localAmount = (BigDecimal) inputTemp.get("P_LOCAL_AMT");
		BigDecimal toCurrencyId = (BigDecimal) inputTemp.get("P_CURRENCY_ID");
		BigDecimal routingBankId = (BigDecimal) inputTemp.get("P_ROUTING_BANK_ID");
		ExchangeRateBreakup exRateBreakup = getExchangeRateBreakup(toCurrencyId, localAmount, routingBankId);
		return exRateBreakup.getConvertedFCAmount();

	}
}
