package com.amx.jax.pricer.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.ExchangeRateDao;
import com.amx.jax.pricer.dao.ExchangeRateProcedureDao;
import com.amx.jax.pricer.dao.MarginMarkupDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dao.ViewExGLCBALDao;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.ExchangeRateAPRDET;
import com.amx.jax.pricer.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.dto.BankRateDetailsDTO;
import com.amx.jax.pricer.dto.ExchangeRateBreakup;
import com.amx.jax.pricer.dto.PricingReqDTO;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.util.PricingRateDetailsDTO;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;

@Component
public class RemitPriceManager {

	@Autowired
	PipsMasterDao pipsMasterDao;

	@Autowired
	ExchangeRateDao exchangeRateDao;

	@Autowired
	ExchangeRateProcedureDao exchangeRateProcedureDao;

	@Autowired
	ViewExGLCBALDao viewExGLCBALDao;

	@Autowired
	MarginMarkupDao marginMarkupDao;

	@Resource
	PricingRateDetailsDTO pricingRateDetailsDTO;

	private static List<BigDecimal> ValidServiceIndicatorIds = new ArrayList<BigDecimal>();

	static {
		ValidServiceIndicatorIds.add(new BigDecimal(101));
		ValidServiceIndicatorIds.add(new BigDecimal(102));
	}

	public List<BankRateDetailsDTO> fetchPricesForRoutingBanks(PricingReqDTO reqDto) {

		List<BankRateDetailsDTO> bankWiseRates = new ArrayList<BankRateDetailsDTO>();

		reqDto.setLocalCountryId(new BigDecimal(91));

		if (reqDto.getCountryBranchId().intValue() == 78) {

			// This is code for fetching prices for online Channel
			List<BigDecimal> validBankIds = getValidBankIds(reqDto);

			if (validBankIds.isEmpty()) {
				throw new PricerServiceException("============ No Routing Bank Data Found ===========");
			}

			Map<BigDecimal, ExchangeRateAPRDET> exchangeRateMap = computeBestRateForOnline(
					reqDto.getForeignCurrencyId(), reqDto.getForeignCountryId(), reqDto.getLocalCountryId(),
					validBankIds);

			if (exchangeRateMap != null && !exchangeRateMap.isEmpty()) {

				for (ExchangeRateAPRDET exchangeRate : exchangeRateMap.values()) {
					BankRateDetailsDTO dto = this.convertBankMasterData(exchangeRate.getBankMaster());

					if (reqDto.getLocalAmount() != null) {

						// Get Bank Wise Rates for Local Currency
						dto.setExRateBreakup(
								createBreakUpForLcCur(exchangeRate.getSellRateMin(), reqDto.getLocalAmount()));

					} else {

						// Get Bank wise Rates for Foreign Currency
						dto.setExRateBreakup(
								createBreakUpForFcCur(exchangeRate.getSellRateMin(), reqDto.getForeignAmount()));

					}

					bankWiseRates.add(dto);

				}

				if (bankWiseRates == null || bankWiseRates.isEmpty()) {
					throw new PricerServiceException("============ No Bank Wise Rates Found ===========");
				}

				/**
				 * Compute and Compare Pips Master Rates
				 */

				// // TODO : *** Remove - Only For Debug and Price Comparison. ***
				// List<BankRateDetailsDTO> pipsBankWiseRates;
				// if (reqDto.getLocalAmount() != null) {
				//
				// // Get Bank Wise Rates for Local Currency
				// // Earlier Pips based Rate Computation
				// pipsBankWiseRates =
				// computeBankPricesForLcCurOnline(reqDto.getForeignCurrencyId(),
				// reqDto.getLocalAmount(), reqDto.getCountryBranchId(),
				// reqDto.getForeignCountryId(),
				// validBankIds);
				//
				// } else {
				//
				// // Get Bank wise Rates for Foreign Currency
				// // Earlier Pips based Rate Computation
				// pipsBankWiseRates =
				// computeBankPricesForFcCurOnline(reqDto.getForeignCurrencyId(),
				// reqDto.getForeignAmount(), reqDto.getCountryBranchId(),
				// reqDto.getForeignCountryId(),
				// validBankIds);
				//
				// }
				//
				// bankWiseRates.addAll(pipsBankWiseRates);

			}

		} else {

			// This is code for fetching prices for Other Channel

			List<BigDecimal> validBankIds = getValidBankIds(reqDto);

			List<ExchangeRateApprovalDetModel> bankExchangeRates = exchangeRateDao
					.getBranchExchangeRatesForRoutingBanks(reqDto.getForeignCurrencyId(), reqDto.getCountryBranchId(),
							reqDto.getForeignCountryId(), reqDto.getLocalCountryId(), validBankIds);

			if (bankExchangeRates != null && !bankExchangeRates.isEmpty()) {

				for (ExchangeRateApprovalDetModel exchangeRate : bankExchangeRates) {

					System.out.println(" Retrieved Exchange Rate ===> " + exchangeRate);

					BankRateDetailsDTO bankRateDetailsDTO = convertBankMasterData(exchangeRate.getBankMaster());

					if (reqDto.getLocalAmount() != null) {

						bankRateDetailsDTO.setExRateBreakup(
								createBreakUpForLcCur(exchangeRate.getSellRateMin(), reqDto.getLocalAmount()));

					} else {
						bankRateDetailsDTO.setExRateBreakup(
								createBreakUpForFcCur(exchangeRate.getSellRateMin(), reqDto.getForeignAmount()));
					}

					bankRateDetailsDTO.setServiceIndicatorId(exchangeRate.getServiceId());

					bankWiseRates.add(bankRateDetailsDTO);

				} // for

				/**
				 * For Further computations
				 */
				pricingRateDetailsDTO.setBankGlcBalMap(getGLCBALRates(reqDto.getForeignCurrencyId(), validBankIds));

				/**
				 * Get margin for the Rate
				 */
				pricingRateDetailsDTO.setMargin(getOnlineMarginMarkup(reqDto.getLocalCountryId(),
						reqDto.getForeignCountryId(), reqDto.getForeignCurrencyId()));

			} // if (bankExchang......

		} // else

		pricingRateDetailsDTO.setBaseBankRatesNPrices(bankWiseRates);

		return bankWiseRates;
	}

	private Map<BigDecimal, ExchangeRateAPRDET> computeBestRateForOnline(BigDecimal currencyId,
			BigDecimal foreignCountryId, BigDecimal applicationCountryId, List<BigDecimal> routingBankIds) {

		// StopWatch watch = new StopWatch();
		// watch.start();

		/**
		 * Get All Cost rates from GLCBAL
		 */
		Map<BigDecimal, ViewExGLCBAL> bankGlcBalMap = getGLCBALRates(currencyId, routingBankIds);

		/**
		 * For Further computations
		 */
		pricingRateDetailsDTO.setBankGlcBalMap(bankGlcBalMap);

		/**
		 * Get margin for the Rate
		 */
		OnlineMarginMarkup margin = getOnlineMarginMarkup(applicationCountryId, foreignCountryId, currencyId);

		/**
		 * For Further computations
		 */
		pricingRateDetailsDTO.setMargin(margin);

		List<ExchangeRateAPRDET> exchangeRates = exchangeRateDao.getUniqueSellRatesForRoutingBanks(currencyId,
				foreignCountryId, applicationCountryId, routingBankIds, ValidServiceIndicatorIds);

		Map<BigDecimal, ExchangeRateAPRDET> bankExchangeRateMap = new HashMap<BigDecimal, ExchangeRateAPRDET>();

		for (ExchangeRateAPRDET rate : exchangeRates) {

			BigDecimal bankId = rate.getBankMaster().getBankId();

			ViewExGLCBAL viewExGLCBAL = bankGlcBalMap.get(bankId);

			if (null != viewExGLCBAL) {

				// Update GLCBAL Rate to Markup Adjusted Rates

				System.out.println(" Original GLCBAL Rate ==> " + viewExGLCBAL.getBankCode() + " Rate ==> "
						+ viewExGLCBAL.getRateAvgRate());

				BigDecimal adjustedSellRate = viewExGLCBAL.getRateAvgRate().add(margin.getMarginMarkup());

				System.out.println(
						" Modified GLCBAL Rate ==> " + viewExGLCBAL.getBankCode() + " Rate ==> " + adjustedSellRate);

				if (bankExchangeRateMap.containsKey(bankId)) {

					ExchangeRateAPRDET ratePrev = bankExchangeRateMap.get(bankId);

					// New Better Rate Found
					// Lower than Previous Exchange Bank Rate
					// Higher than GLCBAL Rate
					if (ratePrev.getSellRateMin().compareTo(rate.getSellRateMin()) > 0
							&& rate.getSellRateMin().compareTo(adjustedSellRate) > 0) {
						bankExchangeRateMap.put(rate.getBankMaster().getBankId(), rate);
					}

				} else {

					if (rate.getSellRateMin().compareTo(adjustedSellRate) < 0) {

						rate.setSellRateMin(adjustedSellRate);
						rate.setSellRateMax(adjustedSellRate);
					}

					bankExchangeRateMap.put(rate.getBankMaster().getBankId(), rate);

				}

			}
		}

		System.out.println(" ===================== ALL GLC BAL Rates ===================== ");

		for (Entry<BigDecimal, ViewExGLCBAL> entry : bankGlcBalMap.entrySet()) {
			System.out.println(" GLCBAL Rate ==> " + entry.getValue().toString());
		}

		System.out.println(" ===================== ALL Exchange Rate  Master Rates ===================== ");

		for (Entry<BigDecimal, ExchangeRateAPRDET> exchangeRate : bankExchangeRateMap.entrySet()) {
			System.out.println(" Exchange Rate ==> " + exchangeRate.toString());
		}

		// watch.stop();
		// long timetaken = watch.getLastTaskTimeMillis();
		// System.out.println("Total time taken to fetch prices from db: " + timetaken +
		// " milli-seconds");

		return bankExchangeRateMap;
	}

	private OnlineMarginMarkup getOnlineMarginMarkup(BigDecimal applicationCountryId, BigDecimal foreignCountryId,
			BigDecimal currencyId) {
		/**
		 * Get margin for the Rate
		 */
		OnlineMarginMarkup margin = marginMarkupDao.getMarkupForCountryAndCurrency(applicationCountryId,
				foreignCountryId, currencyId);

		if (null == margin) {
			margin = new OnlineMarginMarkup();
			margin.setMarginMarkup(new BigDecimal(0));
		}

		return margin;
	}

	private Map<BigDecimal, ViewExGLCBAL> getGLCBALRates(BigDecimal currencyId, List<BigDecimal> routingBankIds) {

		String curCode = StringUtils.leftPad(String.valueOf(currencyId.intValue()), 3, "0");

		Map<BigDecimal, ViewExGLCBAL> bankGlcBalMap = new HashMap<BigDecimal, ViewExGLCBAL>();

		List<ViewExGLCBAL> glcbalRatesForBanks = viewExGLCBALDao.getGLCBALforCurrencyAndBank(curCode, routingBankIds);

		for (ViewExGLCBAL viewExGLCBAL : glcbalRatesForBanks) {

			if (bankGlcBalMap.containsKey(viewExGLCBAL.getBankId())) {

				ViewExGLCBAL viewExGLCBALPrev = bankGlcBalMap.get(viewExGLCBAL.getBankId());

				// Considering only the rates with Max GLCBAL
				if (viewExGLCBALPrev.getRateFcCurBal().compareTo(viewExGLCBAL.getRateCurBal()) < 0) {
					bankGlcBalMap.put(viewExGLCBAL.getBankId(), viewExGLCBAL);
				}
			} else {
				bankGlcBalMap.put(viewExGLCBAL.getBankId(), viewExGLCBAL);
			}

		}

		return bankGlcBalMap;

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

	@SuppressWarnings("unused")
	private List<BankRateDetailsDTO> computeBankPricesForLcCurOnline(BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal countryBranchId, BigDecimal foreignCountryId, List<BigDecimal> validBankIds) {
		List<BankRateDetailsDTO> bankMasterDtoList = new ArrayList<BankRateDetailsDTO>();

		List<PipsMaster> pips = pipsMasterDao.getPipsMasterForLcCur(toCurrency, lcAmount, countryBranchId,
				foreignCountryId, validBankIds);

		if (pips != null && !pips.isEmpty()) {
			pips.forEach(i -> {
				BankRateDetailsDTO dto = this.convertBankMasterData(i.getBankMaster());
				dto.setBankCode("PIPS : " + dto.getBankCode());
				dto.setExRateBreakup(createBreakUpForLcCur(i.getDerivedSellRate().add(i.getPipsNo()), lcAmount));
				bankMasterDtoList.add(dto);
			});
		}

		return bankMasterDtoList;
	}

	@SuppressWarnings("unused")
	private List<BankRateDetailsDTO> computeBankPricesForFcCurOnline(BigDecimal toCurrency, BigDecimal fcAmount,
			BigDecimal countryBranchId, BigDecimal foreignCountryId, List<BigDecimal> validBankIds) {
		List<BankRateDetailsDTO> bankMasterDtoList = new ArrayList<BankRateDetailsDTO>();

		List<PipsMaster> pips = pipsMasterDao.getPipsMasterForFcCur(toCurrency, fcAmount, countryBranchId,
				foreignCountryId, validBankIds);

		if (pips != null && !pips.isEmpty()) {
			pips.forEach(i -> {
				BankRateDetailsDTO dto = this.convertBankMasterData(i.getBankMaster());
				dto.setBankCode("PIPS : " + dto.getBankCode());
				dto.setExRateBreakup(createBreakUpForFcCur(i.getDerivedSellRate().add(i.getPipsNo()), fcAmount));
				bankMasterDtoList.add(dto);

			});
		}
		return bankMasterDtoList;
	}

	private BankRateDetailsDTO convertBankMasterData(BankMasterModel dbmodel) {
		BankRateDetailsDTO dto = new BankRateDetailsDTO();
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

	public static ExchangeRateBreakup createBreakUpForLcCur(BigDecimal exrate, BigDecimal lcAmount) {
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

	public static ExchangeRateBreakup createBreakUpForFcCur(BigDecimal exrate, BigDecimal fcAmount) {
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
