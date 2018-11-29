package com.amx.jax.pricer.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.ExchangeRateDao;
import com.amx.jax.pricer.dao.ExchangeRateProcedureDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dto.BankMasterDTO;
import com.amx.jax.pricer.dto.ExchangeRateBreakup;
import com.amx.jax.pricer.dto.PricingReqDTO;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;

@Component
public class RemitPriceManager {

	@Autowired
	PipsMasterDao pipsMasterDao;

	@Autowired
	ExchangeRateDao exchangeRateDao;

	@Autowired
	ExchangeRateProcedureDao exchangeRateProcedureDao;

	public List<BankMasterDTO> fetchPricesForRoutingBanks(PricingReqDTO reqDto) {

		List<BankMasterDTO> bankWiseRates = new ArrayList<BankMasterDTO>();

		reqDto.setLocalCountryId(new BigDecimal(91));

		if (reqDto.getCountryBranchId().intValue() == 78) {
			// This is code for fetching prices for online Channel

			/*
			 * List<PipsMaster> pips =
			 * pipsMasterDao.getPipsForOnline(reqDto.getForeignCurrencyId(),
			 * reqDto.getCountryBranchId()); if (pips == null || pips.isEmpty()) { throw new
			 * PricerServiceException("============ No exchange data found ==========="); }
			 */

			List<BigDecimal> validBankIds = getValidBankIds(reqDto);

			if (validBankIds.isEmpty()) {
				throw new PricerServiceException("============ No Routing Bank Data Found ===========");
			}

			if (reqDto.getLocalAmount() != null) {
				// Get Bank Wise Rates for Local Currency
				bankWiseRates = computeBankPricesForLcCurOnline(reqDto.getForeignCurrencyId(), reqDto.getLocalAmount(),
						reqDto.getCountryBranchId(), reqDto.getForeignCountryId(), validBankIds);

			} else {

				// Get Bank wise Rates for Foreign Currency
				bankWiseRates = computeBankPricesForFcCurOnline(reqDto.getForeignCurrencyId(),
						reqDto.getForeignAmount(), reqDto.getCountryBranchId(), reqDto.getForeignCountryId(),
						validBankIds);

			}

			if (bankWiseRates == null || bankWiseRates.isEmpty()) {
				throw new PricerServiceException("============ No Bank Wise Rates Found ===========");
			}

		} else {

			// This is code for fetching prices for Other Channel

			List<BigDecimal> validBankIds = getValidBankIds(reqDto);

			List<ExchangeRateApprovalDetModel> bankExchangeRates = exchangeRateDao.getExchangeRatesForRoutingBanks(
					reqDto.getForeignCurrencyId(), reqDto.getCountryBranchId(), reqDto.getForeignCountryId(),
					reqDto.getLocalCountryId(), validBankIds);

			for (ExchangeRateApprovalDetModel exchangeRate : bankExchangeRates) {
				BankMasterDTO bankMasterDTO = convertBankMasterData(exchangeRate.getBankMaster());

				if (reqDto.getLocalAmount() != null) {

					bankMasterDTO.setExRateBreakup(
							createBreakUpForLcCur(exchangeRate.getSellRateMin(), reqDto.getLocalAmount()));

				} else {
					bankMasterDTO.setExRateBreakup(
							createBreakUpForFcCur(exchangeRate.getSellRateMin(), reqDto.getForeignAmount()));
				}

				bankMasterDTO.setServiceIndicatorId(exchangeRate.getServiceId());

				bankWiseRates.add(bankMasterDTO);

			} // for

		} // else

		return bankWiseRates;
	}

	private List<BigDecimal> getValidBankIds(PricingReqDTO reqDto) {

		List<BigDecimal> availableBankIds = exchangeRateProcedureDao
				.getBankIdsForExchangeRates(reqDto.getForeignCurrencyId());

		List<BigDecimal> validBankIds;

		if (PRICE_BY.ROUTING_BANK.equals(reqDto.getPricingLevel()) && reqDto.getRoutingBankIds() != null
				&& !reqDto.getRoutingBankIds().isEmpty()) {

			reqDto.getRoutingBankIds().forEach(bId -> {
				if (!availableBankIds.contains(bId)) {
					throw new PricerServiceException("============ Routing Bank Id Invalid ==>" + bId.toString());
				}
			});

			validBankIds = reqDto.getRoutingBankIds();

		} else {
			validBankIds = availableBankIds;
		}

		return validBankIds;
	}

	private List<BankMasterDTO> computeBankPricesForLcCurOnline(BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal countryBranchId, BigDecimal foreignCountryId, List<BigDecimal> validBankIds) {
		List<BankMasterDTO> bankMasterDtoList = new ArrayList<BankMasterDTO>();

		List<PipsMaster> pips = pipsMasterDao.getPipsMasterForLcCur(toCurrency, lcAmount, countryBranchId,
				foreignCountryId, validBankIds);

		if (pips != null && !pips.isEmpty()) {
			pips.forEach(i -> {
				BankMasterDTO dto = this.convertBankMasterData(i.getBankMaster());
				dto.setExRateBreakup(createBreakUpForLcCur(i.getDerivedSellRate(), lcAmount));
				bankMasterDtoList.add(dto);
			});
		}

		return bankMasterDtoList;
	}

	private List<BankMasterDTO> computeBankPricesForFcCurOnline(BigDecimal toCurrency, BigDecimal fcAmount,
			BigDecimal countryBranchId, BigDecimal foreignCountryId, List<BigDecimal> validBankIds) {
		List<BankMasterDTO> bankMasterDtoList = new ArrayList<BankMasterDTO>();

		List<PipsMaster> pips = pipsMasterDao.getPipsMasterForFcCur(toCurrency, fcAmount, countryBranchId,
				foreignCountryId, validBankIds);

		if (pips != null && !pips.isEmpty()) {
			pips.forEach(i -> {
				BankMasterDTO dto = this.convertBankMasterData(i.getBankMaster());
				dto.setExRateBreakup(createBreakUpForFcCur(i.getDerivedSellRate(), fcAmount));
				bankMasterDtoList.add(dto);

			});
		}
		return bankMasterDtoList;
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

	@SuppressWarnings("unused")
	private ExchangeRateBreakup getExchangeRateBreakUp(BigDecimal toCurrency, BigDecimal lcAmount, BigDecimal fcAmount,
			BigDecimal bankId, BigDecimal countryBranchId) {
		List<PipsMaster> pips = null;
		if (lcAmount != null) {
			pips = pipsMasterDao.getPipsMasterForLocalAmount(toCurrency, lcAmount, countryBranchId, bankId);
		}
		if (fcAmount != null) {
			pips = pipsMasterDao.getPipsMasterForForeignAmount(toCurrency, fcAmount, countryBranchId, bankId);
		}
		if (pips == null || pips.isEmpty()) {
			throw new PricerServiceException("======== Pips Null in Exchange Rate Breakup ==========");
		}

		if (fcAmount != null) {
			return createBreakUpForFcCur(pips.get(0).getDerivedSellRate(), fcAmount);
		} else {
			return createBreakUpForLcCur(pips.get(0).getDerivedSellRate(), lcAmount);
		}
	}

	private ExchangeRateBreakup createBreakUpForLcCur(BigDecimal exrate, BigDecimal lcAmount) {
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

	ExchangeRateBreakup createBreakUpForFcCur(BigDecimal exrate, BigDecimal fcAmount) {
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
