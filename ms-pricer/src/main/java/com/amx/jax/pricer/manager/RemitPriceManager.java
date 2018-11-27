package com.amx.jax.pricer.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.ExchangeRateProcedureDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dto.BankMasterDTO;
import com.amx.jax.pricer.dto.ExchangeRateBreakup;
import com.amx.jax.pricer.dto.PricingReqDTO;
import com.amx.jax.pricer.exception.PricerServiceException;

@Component
public class RemitPriceManager {

	@Autowired
	PipsMasterDao pipsDao;

	@Autowired
	ExchangeRateProcedureDao exchangeRateProcedureDao;

	public List<BankMasterDTO> fetchPricesForRoutingBanks(PricingReqDTO reqDto) {

		List<BankMasterDTO> bankWiseRates;

		if (reqDto.getCountryBranchId().intValue() == 78) {
			// This is code for fetching prices for online Channel

			List<PipsMaster> pips = pipsDao.getPipsForOnline(reqDto.getForeignCurrencyId(),
					reqDto.getCountryBranchId());
			if (pips == null || pips.isEmpty()) {
				throw new PricerServiceException("============ No exchange data found ===========");
			}

			List<BigDecimal> validBankIds = exchangeRateProcedureDao
					.getBankIdsForExchangeRates(reqDto.getForeignCurrencyId());

			if (validBankIds.isEmpty()) {
				throw new PricerServiceException("============ No Routing Bank Data Found ===========");
			}

			bankWiseRates = chooseBankWiseRates(reqDto.getForeignCurrencyId(), reqDto.getLocalAmount(),
					reqDto.getCountryBranchId(), validBankIds);
			if (bankWiseRates == null || bankWiseRates.isEmpty()) {
				throw new PricerServiceException("============ No Bank Wise Rates Found ===========");
			}

			bankWiseRates.forEach(bankRate -> {
				ExchangeRateBreakup breakup = getExchangeRateBreakUp(reqDto.getForeignCurrencyId(),
						reqDto.getLocalAmount(), reqDto.getForeignAmount(), bankRate.getBankId(),
						reqDto.getCountryBranchId());

				if (breakup == null) {
					System.err.println(" ============= Exchange Rate Breakup Null for bank ==> " + bankRate.getBankId()
							+ " bankCode ==>" + bankRate.getBankCode());
				}

			});

		} else {
			// This is code for fetching prices for Other Channel

			bankWiseRates = null;
		}

		return bankWiseRates;
	}

	private List<BankMasterDTO> chooseBankWiseRates(BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal countryBranchId, List<BigDecimal> validBankIds) {
		List<BankMasterDTO> bankMasterDto = new ArrayList<>();

		List<PipsMaster> pips = pipsDao.getPipsMaster(toCurrency, lcAmount, countryBranchId, validBankIds);
		pips.forEach(i -> {
			BankMasterDTO dto = this.convertBankMasterData(i.getBankMaster());
			bankMasterDto.add(dto);
			dto.setExRateBreakup(createBreakUp(i.getDerivedSellRate(), lcAmount));

		});
		return bankMasterDto;
	}

	private BankMasterDTO convertBankMasterData(BankMasterModel dbmodel) {
		BankMasterDTO dto = new BankMasterDTO();
		try {
			BeanUtils.copyProperties(dto, dbmodel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			System.out.println("error in convert of bankmaster");
		}
		return dto;
	}

	private ExchangeRateBreakup getExchangeRateBreakUp(BigDecimal toCurrency, BigDecimal lcAmount, BigDecimal fcAmount,
			BigDecimal bankId, BigDecimal countryBranchId) {
		List<PipsMaster> pips = null;
		if (lcAmount != null) {
			pips = pipsDao.getPipsMasterForLocalAmount(toCurrency, lcAmount, countryBranchId, bankId);
		}
		if (fcAmount != null) {
			pips = pipsDao.getPipsMasterForForeignAmount(toCurrency, fcAmount, countryBranchId, bankId);
		}
		if (pips == null || pips.isEmpty()) {
			throw new PricerServiceException("======== Pips Null in Exchange Rate Breakup ==========");
		}

		if (fcAmount != null) {
			return createBreakUpFromForeignCurrency(pips.get(0).getDerivedSellRate(), fcAmount);
		} else {
			return createBreakUp(pips.get(0).getDerivedSellRate(), lcAmount);
		}
	}

	private ExchangeRateBreakup createBreakUp(BigDecimal exrate, BigDecimal lcAmount) {
		ExchangeRateBreakup breakup = null;
		if (exrate != null) {
			breakup = new ExchangeRateBreakup();
			breakup.setInverseRate(exrate);
			breakup.setRate(new BigDecimal(1).divide(exrate, 10, RoundingMode.HALF_UP));
			breakup.setConvertedFCAmount(breakup.getRate().multiply(lcAmount));
			breakup.setConvertedLCAmount(lcAmount);
		}
		return breakup;
	}

	ExchangeRateBreakup createBreakUpFromForeignCurrency(BigDecimal exrate, BigDecimal fcAmount) {
		ExchangeRateBreakup breakup = null;
		if (exrate != null) {
			breakup = new ExchangeRateBreakup();
			breakup.setConvertedLCAmount(fcAmount.multiply(exrate));
			breakup.setConvertedFCAmount(fcAmount);
			breakup.setInverseRate(exrate);
			breakup.setRate(new BigDecimal(1).divide(exrate, 10, RoundingMode.HALF_UP));
		}
		return breakup;
	}

}
