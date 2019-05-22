/**
 * 
 */
package com.amx.jax.pricer.service;

import static com.amx.jax.pricer.var.PricerServiceConstants.DEFAULT_ONLINE_SERVICE_ID;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.cache.ExchRateAndRoutingTransientDataCache;
import com.amx.jax.cache.TransientRoutingComputeDetails;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.pricer.dao.CustomerDao;
import com.amx.jax.pricer.dbmodel.Customer;
import com.amx.jax.pricer.dbmodel.TimezoneMasterModel;
import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.EstimatedDeliveryDetails;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingResponse;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.dto.TrnxRoutingDetails;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.manager.CustomerDiscountManager;
import com.amx.jax.pricer.manager.RemitPriceManager;
import com.amx.jax.pricer.manager.RemitRoutingManager;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_TYPE;

/**
 * @author abhijeet
 *
 */
@Service
public class ExchangePricingAndRoutingService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangePricingAndRoutingService.class);

	@Autowired
	CustomerDao customerDao;

	@Autowired
	RemitPriceManager remitPriceManager;

	@Autowired
	CustomerDiscountManager customerDiscountManager;

	@Autowired
	RemitRoutingManager remitRoutingManager;

	@Resource
	ExchRateAndRoutingTransientDataCache exchRateAndRoutingTransientDataCache;

	// private BigDecimal BIGD_ZERO = new BigDecimal(0);

	public PricingResponseDTO fetchRemitPricesForCustomer(PricingRequestDTO pricingRequestDTO) {

		validatePricingRequest(pricingRequestDTO, Boolean.TRUE);

		Customer customer = customerDao.getCustById(pricingRequestDTO.getCustomerId());

		if (null == customer) {

			LOGGER.info("Invalid Customer Id : " + pricingRequestDTO.getCustomerId());

			throw new PricerServiceException(PricerServiceError.INVALID_CUSTOMER,
					"Invalid Customer : None Found : " + pricingRequestDTO.getCustomerId());

		}

		remitPriceManager.computeBaseSellRatesPrices(pricingRequestDTO);

		customerDiscountManager.getDiscountedRates(pricingRequestDTO, customer, CUSTOMER_CATEGORY.BRONZE);

		PricingResponseDTO pricingResponseDTO = new PricingResponseDTO();

		pricingResponseDTO.setBankDetails(exchRateAndRoutingTransientDataCache.getBankDetails());

		pricingResponseDTO.setSellRateDetails(exchRateAndRoutingTransientDataCache.getSellRateDetails());

		pricingResponseDTO.setCustomerCategory(exchRateAndRoutingTransientDataCache.getCustomerCategory());

		Collections.sort(pricingResponseDTO.getSellRateDetails(), Collections.reverseOrder());

		pricingResponseDTO.setInfo(exchRateAndRoutingTransientDataCache.getInfo());

		return pricingResponseDTO;
	}

	public PricingResponseDTO fetchBaseRemitPrices(PricingRequestDTO pricingRequestDTO) {

		validatePricingRequest(pricingRequestDTO, Boolean.FALSE);

		remitPriceManager.computeBaseSellRatesPrices(pricingRequestDTO);

		PricingResponseDTO pricingResponseDTO = new PricingResponseDTO();

		pricingResponseDTO.setBankDetails(exchRateAndRoutingTransientDataCache.getBankDetails());

		pricingResponseDTO.setSellRateDetails(exchRateAndRoutingTransientDataCache.getSellRateDetails());

		Collections.sort(pricingResponseDTO.getSellRateDetails(), Collections.reverseOrder());

		return pricingResponseDTO;
	}

	public List<PricingResponseDTO> fetchDiscountedRatesAcrossCustCategories(PricingRequestDTO pricingRequestDTO) {
		validatePricingRequest(pricingRequestDTO, Boolean.FALSE);

		remitPriceManager.computeBaseSellRatesPrices(pricingRequestDTO);

		List<ExchangeRateDetails> baseRateDetails = getClonedExchangeRates(
				exchRateAndRoutingTransientDataCache.getSellRateDetails());

		List<PricingResponseDTO> allDiscountedRates = new ArrayList<PricingResponseDTO>();

		for (CUSTOMER_CATEGORY cc : CUSTOMER_CATEGORY.values()) {

			exchRateAndRoutingTransientDataCache.setSellRateDetails(getClonedExchangeRates(baseRateDetails));

			customerDiscountManager.getDiscountedRates(pricingRequestDTO, null, cc);
			PricingResponseDTO pricingResponseDTO = new PricingResponseDTO();

			pricingResponseDTO.setBankDetails(exchRateAndRoutingTransientDataCache.getBankDetails());

			pricingResponseDTO.setSellRateDetails(exchRateAndRoutingTransientDataCache.getSellRateDetails());

			pricingResponseDTO.setCustomerCategory(cc);

			Collections.sort(pricingResponseDTO.getSellRateDetails(), Collections.reverseOrder());

			pricingResponseDTO.setInfo(exchRateAndRoutingTransientDataCache.getInfo());

			allDiscountedRates.add(pricingResponseDTO);
		}

		return allDiscountedRates;

	}

	public ExchangeRateAndRoutingResponse fetchRemitRoutesAndPrices(
			ExchangeRateAndRoutingRequest exchangeRateAndRoutingRequest) {

		validatePricingRequest(exchangeRateAndRoutingRequest, Boolean.TRUE);

		exchRateAndRoutingTransientDataCache.setServiceGroup(exchangeRateAndRoutingRequest.getServiceGroup());

		Customer customer = customerDao.getCustById(exchangeRateAndRoutingRequest.getCustomerId());

		if (null == customer) {

			LOGGER.info("Invalid Customer Id : " + exchangeRateAndRoutingRequest.getCustomerId());

			throw new PricerServiceException(PricerServiceError.INVALID_CUSTOMER,
					"Invalid Customer : None Found : " + exchangeRateAndRoutingRequest.getCustomerId());

		}

		List<ViewExRoutingMatrix> routingMatrix = remitRoutingManager
				.getRoutingMatrixForRemittance(exchangeRateAndRoutingRequest);

		List<BigDecimal> routingBankIds = routingMatrix.stream().map(rm -> rm.getRoutingBankId()).distinct()
				.collect(Collectors.toList());

		exchangeRateAndRoutingRequest.setRoutingBankIds(routingBankIds);

		exchangeRateAndRoutingRequest.setPricingLevel(PRICE_BY.ROUTING_BANK);

		remitPriceManager.computeBaseSellRatesPrices(exchangeRateAndRoutingRequest);

		customerDiscountManager.getDiscountedRates(exchangeRateAndRoutingRequest, customer, CUSTOMER_CATEGORY.BRONZE);

		remitRoutingManager.setExchangeRatesForTransactionRoutes(exchangeRateAndRoutingRequest.getChannel());

		remitRoutingManager.computeTrnxRoutesAndDelivery(exchangeRateAndRoutingRequest);

		remitRoutingManager.filterTransactionRoutes();

		/**
		 * Prepare the Response Data
		 */
		ExchangeRateAndRoutingResponse resp = new ExchangeRateAndRoutingResponse();

		/**
		 * Fill In the Bank Details
		 */
		resp.setTrnxBeginTimeEpoch(exchRateAndRoutingTransientDataCache.getTrnxBeginTime());
		resp.setBankDetails(exchRateAndRoutingTransientDataCache.getBankDetails());

		TimezoneMasterModel localTz = exchRateAndRoutingTransientDataCache
				.getTimezoneForCountry(exchangeRateAndRoutingRequest.getLocalCountryId());

		if (localTz != null) {
			resp.setLocalTimezone(localTz.getTimezone());
		} else {
			resp.setLocalTimezone(null);
		}

		TimezoneMasterModel foreignTz = exchRateAndRoutingTransientDataCache
				.getTimezoneForCountry(exchangeRateAndRoutingRequest.getForeignCountryId());

		if (foreignTz != null) {
			resp.setForeignTimezone(foreignTz.getTimezone());
		} else {
			resp.setForeignTimezone(null);
		}

		/**
		 * Fill In Routing Details
		 */
		List<TransientRoutingComputeDetails> routingMatrixData = exchRateAndRoutingTransientDataCache
				.getRoutingMatrixData();

		Collections.sort(routingMatrixData, Collections.reverseOrder());

		Map<String, TrnxRoutingDetails> trnxRoutingPaths = new HashMap<String, TrnxRoutingDetails>();

		Map<PRICE_TYPE, List<String>> bestExchangeRatePaths = new HashMap<PRICE_TYPE, List<String>>();

		bestExchangeRatePaths.put(PRICE_TYPE.BENE_DEDUCT, new ArrayList<String>());
		bestExchangeRatePaths.put(PRICE_TYPE.NO_BENE_DEDUCT, new ArrayList<String>());

		Channel channel = exchangeRateAndRoutingRequest.getChannel();
		boolean skipServiceMode = false;
		if (Channel.ONLINE.equals(channel) || Channel.MOBILE.equals(channel)) {
			skipServiceMode = true;
		}

		/**
		 * Fill In the Rate Details
		 */
		Map<BigDecimal, Map<BigDecimal, ExchangeRateDetails>> bankServiceModeSellRates = new HashMap<BigDecimal, Map<BigDecimal, ExchangeRateDetails>>();

		for (TransientRoutingComputeDetails routeDetails : routingMatrixData) {

			TrnxRoutingDetails trnxRoutingPath = new TrnxRoutingDetails();

			try {
				BeanUtils.copyProperties(trnxRoutingPath, routeDetails.getViewExRoutingMatrix());
			} catch (IllegalAccessException | InvocationTargetException e) {
				// Ignore
				e.printStackTrace();
			}

			EstimatedDeliveryDetails finalDelivery = routeDetails.getFinalDeliveryDetails();
			finalDelivery.setStartTT(finalDelivery.getStartDateForeign().toInstant().toEpochMilli());

			trnxRoutingPath.setEstimatedDeliveryDetails(finalDelivery);

			//// @formatter:off
			
			String pathKey = trnxRoutingPath.getRoutingCountryId()
							+ "-" + trnxRoutingPath.getRoutingBankId() 
							+ "-" + trnxRoutingPath.getServiceMasterId()
							+ "-" + trnxRoutingPath.getDeliveryModeId()
							+ "-" + trnxRoutingPath.getRemittanceModeId()
							+ "-" + trnxRoutingPath.getBankBranchId();						

			//// @formatter:on

			trnxRoutingPaths.put(pathKey, trnxRoutingPath);

			// Each Path - By-Default is a ** Non-Bene Deduct ** Path
			bestExchangeRatePaths.get(PRICE_TYPE.NO_BENE_DEDUCT).add(pathKey);

			if (null != trnxRoutingPath.getBeneDeductChargeAmount()
					&& trnxRoutingPath.getBeneDeductChargeAmount().compareTo(BigDecimal.ZERO) != 0) {
				bestExchangeRatePaths.get(PRICE_TYPE.BENE_DEDUCT).add(pathKey);
			}

			ExchangeRateDetails exchangeRate = routeDetails.getExchangeRateDetails();

			if (!bankServiceModeSellRates.containsKey(exchangeRate.getBankId())) {
				bankServiceModeSellRates.put(exchangeRate.getBankId(), new HashMap<BigDecimal, ExchangeRateDetails>());
			}

			if (skipServiceMode) {

				ExchangeRateDetails clonnedRate = exchangeRate.clone();
				clonnedRate.setServiceIndicatorId(routeDetails.getViewExRoutingMatrix().getServiceMasterId());

				bankServiceModeSellRates.get(exchangeRate.getBankId()).put(clonnedRate.getServiceIndicatorId(),
						clonnedRate);

				/*
				 * if (!bankServiceModeSellRates.get(exchangeRate.getBankId()).containsKey(
				 * DEFAULT_ONLINE_SERVICE_ID)) {
				 * bankServiceModeSellRates.get(exchangeRate.getBankId()).put(
				 * DEFAULT_ONLINE_SERVICE_ID, exchangeRate); }
				 */

			} else {
				bankServiceModeSellRates.get(exchangeRate.getBankId()).put(exchangeRate.getServiceIndicatorId(),
						exchangeRate);
			}

		}

		resp.setTrnxRoutingPaths(trnxRoutingPaths);
		resp.setBestExchangeRatePaths(bestExchangeRatePaths);

		resp.setBankServiceModeSellRates(bankServiceModeSellRates);

		// resp.setInfo(exchRateAndRoutingTransientDataCache.getInfo());

		// resp.setSellRateDetails(exchRateAndRoutingTransientDataCache.getSellRateDetails());

		// Collections.sort(resp.getSellRateDetails(), Collections.reverseOrder());

		return resp;

	}

	private List<ExchangeRateDetails> getClonedExchangeRates(List<ExchangeRateDetails> exRateDetailsOrig) {

		if (exRateDetailsOrig == null) {
			return null;
		}

		List<ExchangeRateDetails> exRateDetailsCloned = new ArrayList<ExchangeRateDetails>();

		for (ExchangeRateDetails exRateDetail : exRateDetailsOrig) {
			exRateDetailsCloned.add(exRateDetail.clone());
		}

		return exRateDetailsCloned;
	}

	private boolean validatePricingRequest(PricingRequestDTO pricingRequestDTO, boolean isCustomer) {

		/**
		 * All Conditional Validations
		 */
		if (null == pricingRequestDTO.getLocalAmount() && null == pricingRequestDTO.getForeignAmount()) {
			throw new PricerServiceException(PricerServiceError.MISSING_AMOUNT,
					"Missing Local and Foreign Amount; Either is Required");
		}

		if (PRICE_BY.ROUTING_BANK.equals(pricingRequestDTO.getPricingLevel())
				&& null == pricingRequestDTO.getRoutingBankIds()) {
			throw new PricerServiceException(PricerServiceError.MISSING_ROUTING_BANK_IDS, "Invalid Pricing Level");
		}

		if (isCustomer && null == pricingRequestDTO.getCustomerId()) {
			throw new PricerServiceException(PricerServiceError.INVALID_CUSTOMER,
					"Customer Id Can not be blank or empty");
		}

		/*
		 * if (null == pricingRequestDTO.getLocalCountryId() || null ==
		 * pricingRequestDTO.getForeignCountryId()) { throw new
		 * PricerServiceException(PricerServiceError.INVALID_COUNTRY,
		 * "Missing Local or Foreign Country Id; Both Required"); }
		 * 
		 * if (null == pricingRequestDTO.getLocalCurrencyId() || null ==
		 * pricingRequestDTO.getForeignCurrencyId()) { throw new
		 * PricerServiceException(PricerServiceError.INVALID_CURRENCY,
		 * "Missing Local or Foreign Currency Id; Both Required"); }
		 */

		/*
		 * if (null == pricingRequestDTO.getCountryBranchId()) { throw new
		 * PricerServiceException(PricerServiceError.INVALID_BRANCH_ID,
		 * "Branch Id is  Missing"); }
		 * 
		 * if (null == pricingRequestDTO.getChannel()) { throw new
		 * PricerServiceException(PricerServiceError.INVALID_CHANNEL,
		 * "Channel is Missing"); }
		 * 
		 * if (null == pricingRequestDTO.getPricingLevel()) { throw new
		 * PricerServiceException(PricerServiceError.INVALID_PRICING_LEVEL,
		 * "Invalid Pricing Level"); }
		 */

		return Boolean.TRUE;
	}

}
