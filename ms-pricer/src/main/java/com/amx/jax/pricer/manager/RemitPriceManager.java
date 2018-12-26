package com.amx.jax.pricer.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.UserClient.Channel;
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
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.util.PricingRateDetailsDTO;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;

@Component
public class RemitPriceManager {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RemitPriceManager.class);

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

	private static int OnlineCountryBranchId = 78;

	static {
		ValidServiceIndicatorIds.add(new BigDecimal(101));
		ValidServiceIndicatorIds.add(new BigDecimal(102));
	}

	public List<BankRateDetailsDTO> computeBaseSellRatesPrices(PricingRequestDTO requestDto) {

		List<BankRateDetailsDTO> bankWiseRates = new ArrayList<BankRateDetailsDTO>();

		if ((Channel.ONLINE.equals(requestDto.getChannel()) || Channel.MOBILE.equals(requestDto.getChannel()))
				&& requestDto.getCountryBranchId().intValue() == OnlineCountryBranchId) {

			// This is code for fetching prices for online Channel
			List<BigDecimal> validBankIds = getValidBankIds(requestDto.getForeignCurrencyId(),
					requestDto.getPricingLevel(), requestDto.getRoutingBankIds());

			if (validBankIds.isEmpty()) {

				LOGGER.info("No Valid bank Ids found for Pricing Request");

				throw new PricerServiceException(PricerServiceError.INVALID_ROUTING_BANK_IDS,
						"Invalid Routing Bank Ids : None Found : " + requestDto.getRoutingBankIds());
			}

			Map<BigDecimal, ExchangeRateAPRDET> exchangeRateMap = computeBestRateForOnline(
					requestDto.getForeignCurrencyId(), requestDto.getForeignCountryId(), requestDto.getLocalCountryId(),
					validBankIds);

			if (exchangeRateMap == null || exchangeRateMap.isEmpty()) {
				throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
						"Missing Valid Exchange rates : None Found");
			}

			for (ExchangeRateAPRDET exchangeRate : exchangeRateMap.values()) {
				BankRateDetailsDTO bankRateDto = this.convertBankMasterData(exchangeRate.getBankMaster());

				if (requestDto.getLocalAmount() != null) {

					// Get Bank Wise Rates for Local Currency
					bankRateDto.setExRateBreakup(
							createBreakUpForLcCur(exchangeRate.getSellRateMin(), requestDto.getLocalAmount()));

				} else {

					// Get Bank wise Rates for Foreign Currency
					bankRateDto.setExRateBreakup(
							createBreakUpForFcCur(exchangeRate.getSellRateMin(), requestDto.getForeignAmount()));

				}

				bankWiseRates.add(bankRateDto);

			}

			/**
			 * Compute and Compare Pips Master Rates
			 */
			// bankWiseRates.addAll(pipsBankWiseRates);

		} else {

			// This is code for fetching prices for Other Channel

			List<BigDecimal> validBankIds = getValidBankIds(requestDto.getForeignCurrencyId(),
					requestDto.getPricingLevel(), requestDto.getRoutingBankIds());
			
			if (validBankIds.isEmpty()) {

				LOGGER.info("No Valid bank Ids found for Pricing Request");

				throw new PricerServiceException(PricerServiceError.INVALID_ROUTING_BANK_IDS,
						"Invalid Routing Bank Ids : None Found : " + requestDto.getRoutingBankIds());
			}

			List<ExchangeRateApprovalDetModel> bankExchangeRates = exchangeRateDao
					.getBranchExchangeRatesForRoutingBanks(requestDto.getForeignCurrencyId(),
							requestDto.getCountryBranchId(), requestDto.getForeignCountryId(),
							requestDto.getLocalCountryId(), validBankIds);

			if (bankExchangeRates == null || bankExchangeRates.isEmpty()) {
				throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
						"Missing Valid Exchange rates : None Found");
			}

			for (ExchangeRateApprovalDetModel exchangeRate : bankExchangeRates) {

				BankRateDetailsDTO bankRateDetailsDTO = convertBankMasterData(exchangeRate.getBankMaster());

				if (requestDto.getLocalAmount() != null) {

					bankRateDetailsDTO.setExRateBreakup(
							createBreakUpForLcCur(exchangeRate.getSellRateMin(), requestDto.getLocalAmount()));

				} else {
					bankRateDetailsDTO.setExRateBreakup(
							createBreakUpForFcCur(exchangeRate.getSellRateMin(), requestDto.getForeignAmount()));
				}

				bankRateDetailsDTO.setServiceIndicatorId(exchangeRate.getServiceId());

				bankWiseRates.add(bankRateDetailsDTO);

			} // for

			/**
			 * For Further computations
			 */
			pricingRateDetailsDTO.setBankGlcBalMap(getGLCBALRates(requestDto.getForeignCurrencyId(), validBankIds));

			/**
			 * Get margin for the Rate
			 */
			pricingRateDetailsDTO.setMargin(getOnlineMarginMarkup(requestDto.getLocalCountryId(),
					requestDto.getForeignCountryId(), requestDto.getForeignCurrencyId()));

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

		if (bankGlcBalMap == null || bankGlcBalMap.isEmpty()) {
			throw new PricerServiceException(PricerServiceError.MISSING_GLCBAL_ENTRIES,
					"GLCBAL Inventory is Missing for Given Input : ");

		}

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
				BigDecimal adjustedSellRate = viewExGLCBAL.getRateAvgRate().add(margin.getMarginMarkup());

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

		/*
		 * System.out.
		 * println(" ===================== ALL GLC BAL Rates ===================== ");
		 * 
		 * for (Entry<BigDecimal, ViewExGLCBAL> entry : bankGlcBalMap.entrySet()) {
		 * System.out.println(" GLCBAL Rate ==> " + entry.getValue().toString()); }
		 */

		/*
		 * System.out.
		 * println(" ===================== ALL Exchange Rate  Master Rates ===================== "
		 * );
		 * 
		 * for (Entry<BigDecimal, ExchangeRateAPRDET> exchangeRate :
		 * bankExchangeRateMap.entrySet()) { System.out.println(" Exchange Rate ==> " +
		 * exchangeRate.toString()); }
		 */

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

	private List<BigDecimal> getValidBankIds(BigDecimal fCurrencyId, PRICE_BY pricingLevel,
			List<BigDecimal> routingBnaks) {

		List<BigDecimal> availableBankIds = exchangeRateProcedureDao.getBankIdsForExchangeRates(fCurrencyId);

		List<BigDecimal> validBankIds;

		if (PRICE_BY.ROUTING_BANK.equals(pricingLevel) && routingBnaks != null && !routingBnaks.isEmpty()) {

			routingBnaks.forEach(bId -> {
				if (!availableBankIds.contains(bId)) {
					throw new PricerServiceException("============ Routing Bank Id Invalid ==>" + bId.toString());
				}
			});

			validBankIds = routingBnaks;

		} else {
			validBankIds = availableBankIds;
		}

		return validBankIds;
	}

	private BankRateDetailsDTO convertBankMasterData(BankMasterModel dbmodel) {
		BankRateDetailsDTO dto = new BankRateDetailsDTO();
		try {
			BeanUtils.copyProperties(dto, dbmodel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOGGER.warn("error in convert of bankmaster");
		}
		return dto;
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

	@SuppressWarnings("unused")
	private List<BankRateDetailsDTO> computePipsRateForOnline(PricingRequestDTO reqDto, List<BigDecimal> validBankIds) {

		List<BankRateDetailsDTO> pipsBankWiseRates;
		if (reqDto.getLocalAmount() != null) {

			// Get Bank Wise Rates for Local Currency
			// Earlier Pips based Rate Computation
			pipsBankWiseRates = computeBankPricesForLcCurOnline(reqDto.getForeignCurrencyId(), reqDto.getLocalAmount(),
					reqDto.getCountryBranchId(), reqDto.getForeignCountryId(), validBankIds);

		} else {

			// Get Bank wise Rates for Foreign Currency
			// Earlier Pips based Rate Computation
			pipsBankWiseRates = computeBankPricesForFcCurOnline(reqDto.getForeignCurrencyId(),
					reqDto.getForeignAmount(), reqDto.getCountryBranchId(), reqDto.getForeignCountryId(), validBankIds);

		}

		return pipsBankWiseRates;

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

}
