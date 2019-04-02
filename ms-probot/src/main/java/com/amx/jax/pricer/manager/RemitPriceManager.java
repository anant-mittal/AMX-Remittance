package com.amx.jax.pricer.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.cache.ExchRateAndRoutingTransientDataCache;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.pricer.dao.CountryBranchDao;
import com.amx.jax.pricer.dao.ExchangeRateDao;
import com.amx.jax.pricer.dao.ExchangeRateProcedureDao;
import com.amx.jax.pricer.dao.MarginMarkupDao;
import com.amx.jax.pricer.dao.PipsMasterDao;
import com.amx.jax.pricer.dao.RoutingDao;
import com.amx.jax.pricer.dao.ViewExGLCBALDao;
import com.amx.jax.pricer.dbmodel.BankMasterModel;
import com.amx.jax.pricer.dbmodel.CountryBranch;
import com.amx.jax.pricer.dbmodel.ExchangeRateAPRDET;
import com.amx.jax.pricer.dbmodel.ExchangeRateApprovalDetModel;
import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;
import com.amx.jax.pricer.dbmodel.PipsMaster;
import com.amx.jax.pricer.dbmodel.RoutingHeader;
import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.dto.BankDetailsDTO;
import com.amx.jax.pricer.dto.ExchangeRateBreakup;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
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

	@Autowired
	CountryBranchDao countryBranchDao;

	@Autowired
	RoutingDao routingDao;

	@Resource
	ExchRateAndRoutingTransientDataCache exchRateAndRoutingTransientDataCache;

	private static List<BigDecimal> ValidServiceIndicatorIds = new ArrayList<BigDecimal>();

	private static BigDecimal OnlineCountryBranchId;

	static {
		ValidServiceIndicatorIds.add(new BigDecimal(101));
		ValidServiceIndicatorIds.add(new BigDecimal(102));
		
		//ValidServiceIndicatorIds.add(new BigDecimal(103));
	}

	/**
	 * Function to compute Base Sell Rates, Cost Rate, Banks Details and other
	 * related data Result Data is computed and saved into <b>@Resource
	 * ExchRateAndRoutingTransientDataCache </b>
	 * 
	 * @param requestDto
	 */
	public void computeBaseSellRatesPrices(PricingRequestDTO requestDto) {

		List<ExchangeRateDetails> bankWiseRates = new ArrayList<ExchangeRateDetails>();
		exchRateAndRoutingTransientDataCache.setSellRateDetails(bankWiseRates);

		Map<BigDecimal, BankDetailsDTO> bankIdDetailsMap = new HashMap<BigDecimal, BankDetailsDTO>();
		exchRateAndRoutingTransientDataCache.setBankDetails(bankIdDetailsMap);

		if ((Channel.ONLINE.equals(requestDto.getChannel()) || Channel.MOBILE.equals(requestDto.getChannel()))) {

			if (OnlineCountryBranchId == null) {
				CountryBranch cb = countryBranchDao.getOnlineCountryBranch();
				OnlineCountryBranchId = cb.getCountryBranchId();
			}

			if (OnlineCountryBranchId.longValue() != requestDto.getCountryBranchId().longValue()) {
				throw new PricerServiceException(PricerServiceError.INVALID_BRANCH_ID,
						"Invalid CountryBranchId, for Channel ONLINE, Id : " + requestDto.getCountryBranchId());
			}

			// This is code for fetching prices for online Channel
			List<BigDecimal> validBankIds = getValidRoutingBankIds(requestDto.getForeignCountryId(),
					requestDto.getForeignCurrencyId(), requestDto.getPricingLevel(), requestDto.getRoutingBankIds());

			if (validBankIds.isEmpty()) {

				LOGGER.warn("No Valid bank Ids found for Pricing Request");

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

				BankDetailsDTO bankDetailsDto;

				if (bankIdDetailsMap.containsKey(exchangeRate.getBankMaster().getBankId())) {
					bankDetailsDto = bankIdDetailsMap.get(exchangeRate.getBankMaster().getBankId());
				} else {
					bankDetailsDto = this.convertBankMasterData(exchangeRate.getBankMaster());
					bankIdDetailsMap.put(bankDetailsDto.getBankId(), bankDetailsDto);
				}

				ExchangeRateDetails exRateDetails = new ExchangeRateDetails();
				exRateDetails.setBankId(bankDetailsDto.getBankId());
				exRateDetails.setServiceIndicatorId(exchangeRate.getServiceId());

				if (requestDto.getLocalAmount() != null) {

					// Get Bank Wise Rates for Local Currency
					exRateDetails.setSellRateBase(
							createBreakUpForLcCur(exchangeRate.getSellRateMax(), requestDto.getLocalAmount()));

				} else {

					// Get Bank wise Rates for Foreign Currency
					exRateDetails.setSellRateBase(
							createBreakUpForFcCur(exchangeRate.getSellRateMax(), requestDto.getForeignAmount()));

				}

				bankWiseRates.add(exRateDetails);

			}

			/**
			 * Compute and Compare Pips Master Rates
			 */
			// bankWiseRates.addAll(pipsBankWiseRates);

		} else {

			// This is code for fetching prices for Other Channel

			List<BigDecimal> validBankIds = getValidRoutingBankIds(requestDto.getForeignCountryId(),
					requestDto.getForeignCurrencyId(), requestDto.getPricingLevel(), requestDto.getRoutingBankIds());

			if (validBankIds.isEmpty()) {

				LOGGER.info("No Valid bank Ids found for Pricing Request");

				throw new PricerServiceException(PricerServiceError.INVALID_ROUTING_BANK_IDS,
						"Invalid Routing Bank Ids : None Found : " + requestDto.getRoutingBankIds());
			}

			List<ExchangeRateApprovalDetModel> bankExchangeRates;

			// Filter Bank Exchange rates for Required Service Indicator Ids
			if (requestDto.getServiceIndicatorId() != null) {

				List<BigDecimal> serviceIdsList = new ArrayList<BigDecimal>();
				serviceIdsList.add(requestDto.getServiceIndicatorId());

				bankExchangeRates = exchangeRateDao.getBranchExchangeRatesForRoutingBanksAndServiceIds(
						requestDto.getForeignCurrencyId(), requestDto.getCountryBranchId(),
						 requestDto.getLocalCountryId(), validBankIds, serviceIdsList);
			} else {

				bankExchangeRates = exchangeRateDao.getBranchExchangeRatesForRoutingBanks(
						requestDto.getForeignCurrencyId(), requestDto.getCountryBranchId(),
						requestDto.getLocalCountryId(), validBankIds);

			}

			if (bankExchangeRates == null || bankExchangeRates.isEmpty()) {
				throw new PricerServiceException(PricerServiceError.MISSING_VALID_EXCHANGE_RATES,
						"Missing Valid Exchange rates : None Found");
			}

			for (ExchangeRateApprovalDetModel exchangeRate : bankExchangeRates) {

				BankDetailsDTO bankDetailsDTO;

				if (bankIdDetailsMap.containsKey(exchangeRate.getBankMaster().getBankId())) {
					bankDetailsDTO = bankIdDetailsMap.get(exchangeRate.getBankMaster().getBankId());
				} else {
					bankDetailsDTO = this.convertBankMasterData(exchangeRate.getBankMaster());
					bankIdDetailsMap.put(bankDetailsDTO.getBankId(), bankDetailsDTO);
				}

				ExchangeRateDetails exRateDetails = new ExchangeRateDetails();
				exRateDetails.setBankId(bankDetailsDTO.getBankId());
				exRateDetails.setServiceIndicatorId(exchangeRate.getServiceId());

				if (requestDto.getLocalAmount() != null) {

					exRateDetails.setSellRateBase(
							createBreakUpForLcCur(exchangeRate.getSellRateMin(), requestDto.getLocalAmount()));

				} else {
					exRateDetails.setSellRateBase(
							createBreakUpForFcCur(exchangeRate.getSellRateMin(), requestDto.getForeignAmount()));
				}

				bankWiseRates.add(exRateDetails);

			} // for

			/**
			 * For Further computations
			 */
			exchRateAndRoutingTransientDataCache
					.setBankGlcBalMap(getGLCBALRates(requestDto.getForeignCurrencyId(), validBankIds));

			/**
			 * Get margin for the Rate
			 */
			exchRateAndRoutingTransientDataCache.setMargin(getOnlineMarginMarkup(requestDto.getLocalCountryId(),
					requestDto.getForeignCountryId(), requestDto.getForeignCurrencyId()));

		} // else

	}

	private Map<BigDecimal, ExchangeRateAPRDET> computeBestRateForOnline(BigDecimal currencyId,
			BigDecimal foreignCountryId, BigDecimal applicationCountryId, List<BigDecimal> routingBankIds) {

		/**
		 * Get All Cost rates from GLCBAL
		 */
		Map<BigDecimal, List<ViewExGLCBAL>> bankGlcBalMap = getGLCBALRates(currencyId, routingBankIds);

		if (bankGlcBalMap == null || bankGlcBalMap.isEmpty()) {
			throw new PricerServiceException(PricerServiceError.MISSING_GLCBAL_ENTRIES,
					"GLCBAL Inventory is Missing for Given Input : ");

		}

		/**
		 * For Further computations
		 */
		exchRateAndRoutingTransientDataCache.setBankGlcBalMap(bankGlcBalMap);

		/**
		 * Get margin for the Rate
		 */
		OnlineMarginMarkup margin = getOnlineMarginMarkup(applicationCountryId, foreignCountryId, currencyId);

		/**
		 * For Further computations
		 */
		exchRateAndRoutingTransientDataCache.setMargin(margin);

		// Get Distinct Bank Rates from APRDET - for a given Currency, destination
		// country, routing banks and service Indicator Ids.
		List<ExchangeRateAPRDET> exchangeRates = exchangeRateDao.getUniqueSellRatesForRoutingBanks(currencyId,
				foreignCountryId, applicationCountryId, routingBankIds, ValidServiceIndicatorIds);

		Map<BigDecimal, ExchangeRateAPRDET> bankExchangeRateMap = new HashMap<BigDecimal, ExchangeRateAPRDET>();

		for (ExchangeRateAPRDET rate : exchangeRates) {

			BigDecimal bankId = rate.getBankMaster().getBankId();

			// ViewExGLCBAL viewExGLCBAL = bankGlcBalMap.get(bankId);

			BigDecimal avgBankGLCBALRate = exchRateAndRoutingTransientDataCache.getAvgRateGLCForBank(bankId);

			if (null != avgBankGLCBALRate) {

				// Update GLCBAL Rate to Markup Adjusted Rates
				BigDecimal adjustedSellRate = avgBankGLCBALRate.add(margin.getMarginMarkup());

				if (bankExchangeRateMap.containsKey(bankId)) {

					ExchangeRateAPRDET ratePrev = bankExchangeRateMap.get(bankId);

					// New Better Rate Found
					// Lower than Previous Exchange Bank Rate
					// Higher than GLCBAL Rate
					if (ratePrev.getSellRateMax().compareTo(rate.getSellRateMax()) > 0
							&& rate.getSellRateMax().compareTo(adjustedSellRate) > 0) {
						bankExchangeRateMap.put(rate.getBankMaster().getBankId(), rate);
					}

				} else {

					if (rate.getSellRateMax().compareTo(adjustedSellRate) < 0) {

						rate.setSellRateMin(adjustedSellRate);
						rate.setSellRateMax(adjustedSellRate);
					}

					bankExchangeRateMap.put(rate.getBankMaster().getBankId(), rate);

				}

			}
		}

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

	/**
	 * Average Rate Computation changed to compute the weighted Average of all the
	 * GLCBAL
	 * 
	 * @param currencyId
	 * @param routingBankIds
	 * @return
	 */
	private Map<BigDecimal, List<ViewExGLCBAL>> getGLCBALRates(BigDecimal currencyId, List<BigDecimal> routingBankIds) {

		String curCode = StringUtils.leftPad(String.valueOf(currencyId.intValue()), 3, "0");

		Map<BigDecimal, List<ViewExGLCBAL>> bankGlcBalMap = new HashMap<BigDecimal, List<ViewExGLCBAL>>();

		List<ViewExGLCBAL> glcbalRatesForBanks = viewExGLCBALDao.getGLCBALforCurrencyAndBank(curCode, routingBankIds);

		for (ViewExGLCBAL viewExGLCBAL : glcbalRatesForBanks) {

			if (bankGlcBalMap.containsKey(viewExGLCBAL.getBankId())) {

				bankGlcBalMap.get(viewExGLCBAL.getBankId()).add(viewExGLCBAL);

			} else {

				List<ViewExGLCBAL> glcBalList = new ArrayList<ViewExGLCBAL>();

				glcBalList.add(viewExGLCBAL);

				bankGlcBalMap.put(viewExGLCBAL.getBankId(), glcBalList);
			}

		}

		return bankGlcBalMap;

	}

	private List<BigDecimal> getValidRoutingBankIds(BigDecimal fCountryId, BigDecimal fCurrencyId,
			PRICE_BY pricingLevel, List<BigDecimal> routingBanks) {

		/**
		 * Old Code for fetching the Routing Bank Ids from VW_EX_TRATE
		 * 
		 * List<BigDecimal> availableBankIds =
		 * exchangeRateProcedureDao.getBankIdsForExchangeRates(fCurrencyId);
		 **/

		/** Start: Routing Bank Find **/
		List<RoutingHeader> routingHeaders = routingDao.getRoutHeadersByCountryIdAndCurrenyId(fCountryId, fCurrencyId);

		List<BigDecimal> availableBankIds = routingHeaders.stream().map(rh -> rh.getRoutingBankId()).distinct().sorted()
				.collect(Collectors.toList());

		/** End: Routing Bank Find **/

		List<BigDecimal> validBankIds;

		if (PRICE_BY.ROUTING_BANK.equals(pricingLevel) && routingBanks != null && !routingBanks.isEmpty()) {

			routingBanks.forEach(bId -> {
				if (!availableBankIds.contains(bId)) {
					throw new PricerServiceException(PricerServiceError.INVALID_ROUTING_BANK_IDS,
							" Routing Bank Id is Invalid: " + bId.toString());
				}
			});

			validBankIds = routingBanks;

		} else if (PRICE_BY.COUNTRY.equals(pricingLevel)) {
			validBankIds = availableBankIds;
		} else {
			validBankIds = new ArrayList<BigDecimal>();
		}

		return validBankIds;
	}

	private BankDetailsDTO convertBankMasterData(BankMasterModel dbmodel) {
		BankDetailsDTO dto = new BankDetailsDTO();
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
	private List<ExchangeRateDetails> computePipsRateForOnline(PricingRequestDTO reqDto,
			List<BigDecimal> validBankIds) {

		List<ExchangeRateDetails> exchangeRateDetailList;

		if (reqDto.getLocalAmount() != null) {

			// Get Bank Wise Rates for Local Currency
			// Earlier Pips based Rate Computation
			exchangeRateDetailList = computeBankPricesForLcCurOnline(reqDto.getForeignCurrencyId(),
					reqDto.getLocalAmount(), reqDto.getCountryBranchId(), reqDto.getForeignCountryId(), validBankIds);

		} else {

			// Get Bank wise Rates for Foreign Currency
			// Earlier Pips based Rate Computation
			exchangeRateDetailList = computeBankPricesForFcCurOnline(reqDto.getForeignCurrencyId(),
					reqDto.getForeignAmount(), reqDto.getCountryBranchId(), reqDto.getForeignCountryId(), validBankIds);

		}

		return exchangeRateDetailList;

	}

	@SuppressWarnings("unused")
	private List<ExchangeRateDetails> computeBankPricesForLcCurOnline(BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal countryBranchId, BigDecimal foreignCountryId, List<BigDecimal> validBankIds) {
		List<ExchangeRateDetails> exchangeRateDetailList = new ArrayList<ExchangeRateDetails>();

		List<PipsMaster> pips = pipsMasterDao.getPipsMasterForLcCur(toCurrency, lcAmount, countryBranchId,
				foreignCountryId, validBankIds);

		if (this.exchRateAndRoutingTransientDataCache.getBankDetails() == null) {
			this.exchRateAndRoutingTransientDataCache.setBankDetails(new HashMap<BigDecimal, BankDetailsDTO>());
		}

		if (pips != null && !pips.isEmpty()) {
			pips.forEach(i -> {
				ExchangeRateDetails exRateDetails = new ExchangeRateDetails();
				BankDetailsDTO dto = this.convertBankMasterData(i.getBankMaster());
				exRateDetails.setBankId(dto.getBankId());
				exRateDetails
						.setSellRateBase(createBreakUpForLcCur(i.getDerivedSellRate().add(i.getPipsNo()), lcAmount));

				exchangeRateDetailList.add(exRateDetails);
				this.exchRateAndRoutingTransientDataCache.getBankDetails().put(dto.getBankId(), dto);
			});
		}

		return exchangeRateDetailList;
	}

	@SuppressWarnings("unused")
	private List<ExchangeRateDetails> computeBankPricesForFcCurOnline(BigDecimal toCurrency, BigDecimal fcAmount,
			BigDecimal countryBranchId, BigDecimal foreignCountryId, List<BigDecimal> validBankIds) {
		List<ExchangeRateDetails> exchangeRateDetailList = new ArrayList<ExchangeRateDetails>();

		List<PipsMaster> pips = pipsMasterDao.getPipsMasterForFcCur(toCurrency, fcAmount, countryBranchId,
				foreignCountryId, validBankIds);

		if (this.exchRateAndRoutingTransientDataCache.getBankDetails() == null) {
			this.exchRateAndRoutingTransientDataCache.setBankDetails(new HashMap<BigDecimal, BankDetailsDTO>());
		}

		if (pips != null && !pips.isEmpty()) {
			pips.forEach(i -> {
				ExchangeRateDetails exRateDetails = new ExchangeRateDetails();
				BankDetailsDTO dto = this.convertBankMasterData(i.getBankMaster());
				exRateDetails.setBankId(dto.getBankId());
				exRateDetails
						.setSellRateBase(createBreakUpForFcCur(i.getDerivedSellRate().add(i.getPipsNo()), fcAmount));

				exchangeRateDetailList.add(exRateDetails);
				this.exchRateAndRoutingTransientDataCache.getBankDetails().put(dto.getBankId(), dto);

			});
		}
		return exchangeRateDetailList;
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
