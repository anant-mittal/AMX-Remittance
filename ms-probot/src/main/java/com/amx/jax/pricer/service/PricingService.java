/**
 * 
 */
package com.amx.jax.pricer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.cache.ComputeRequestTransientDataCache;
import com.amx.jax.pricer.dao.CustomerDao;
import com.amx.jax.pricer.dbmodel.Customer;
import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.DprRequestDto;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.manager.CustomerDiscountManager;
import com.amx.jax.pricer.manager.RemitPriceManager;
import com.amx.jax.pricer.manager.RemitRoutingManager;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;

/**
 * @author abhijeet
 *
 */
@Service
public class PricingService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PricingService.class);

	@Autowired
	CustomerDao customerDao;

	@Autowired
	RemitPriceManager remitPriceManager;

	@Autowired
	CustomerDiscountManager customerDiscountManager;

	@Autowired
	RemitRoutingManager remitRoutingManager;

	@Resource
	ComputeRequestTransientDataCache computeRequestTransientDataCache;

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

		pricingResponseDTO.setBankDetails(computeRequestTransientDataCache.getBankDetails());

		pricingResponseDTO.setSellRateDetails(computeRequestTransientDataCache.getSellRateDetails());

		Collections.sort(pricingResponseDTO.getSellRateDetails(), Collections.reverseOrder());

		pricingResponseDTO.setInfo(computeRequestTransientDataCache.getInfo());

		return pricingResponseDTO;
	}

	public PricingResponseDTO fetchBaseRemitPrices(PricingRequestDTO pricingRequestDTO) {

		validatePricingRequest(pricingRequestDTO, Boolean.FALSE);

		remitPriceManager.computeBaseSellRatesPrices(pricingRequestDTO);

		PricingResponseDTO pricingResponseDTO = new PricingResponseDTO();

		pricingResponseDTO.setBankDetails(computeRequestTransientDataCache.getBankDetails());

		pricingResponseDTO.setSellRateDetails(computeRequestTransientDataCache.getSellRateDetails());

		Collections.sort(pricingResponseDTO.getSellRateDetails(), Collections.reverseOrder());

		return pricingResponseDTO;
	}

	public List<PricingResponseDTO> fetchDiscountedRatesAcrossCustCategories(PricingRequestDTO pricingRequestDTO) {
		validatePricingRequest(pricingRequestDTO, Boolean.FALSE);

		remitPriceManager.computeBaseSellRatesPrices(pricingRequestDTO);

		List<PricingResponseDTO> allDiscountedRates = new ArrayList<PricingResponseDTO>();

		for (CUSTOMER_CATEGORY cc : CUSTOMER_CATEGORY.values()) {

			customerDiscountManager.getDiscountedRates(pricingRequestDTO, null, cc);
			PricingResponseDTO pricingResponseDTO = new PricingResponseDTO();

			pricingResponseDTO.setBankDetails(computeRequestTransientDataCache.getBankDetails());

			List<ExchangeRateDetails> exRateDetails = new ArrayList<ExchangeRateDetails>();

			for (ExchangeRateDetails exRateDetail : computeRequestTransientDataCache.getSellRateDetails()) {

				exRateDetails.add(exRateDetail.clone());

			}

			pricingResponseDTO.setSellRateDetails(exRateDetails);

			pricingResponseDTO.setCustomerCategory(cc);

			Collections.sort(pricingResponseDTO.getSellRateDetails(), Collections.reverseOrder());

			pricingResponseDTO.setInfo(computeRequestTransientDataCache.getInfo());

			allDiscountedRates.add(pricingResponseDTO);
		}

		return allDiscountedRates;

	}

	public PricingResponseDTO fetchRemitRoutesAndPrices(DprRequestDto dprRequestDto) {

		List<ViewExRoutingMatrix> routingMatrix = remitRoutingManager.getRoutingMatrixForRemittance(dprRequestDto);

		for (ViewExRoutingMatrix oneMatrix : routingMatrix) {

			remitRoutingManager.getEstimatedBlockDelivery(System.currentTimeMillis(), "Asia/Kolkata",
					oneMatrix.getWeekFrom(), oneMatrix.getWeekTo(), oneMatrix.getWeekHoursFrom(),
					oneMatrix.getWeekHoursTo(), oneMatrix.getWeekendFrom(), oneMatrix.getWeekendTo(),
					oneMatrix.getWeekendHoursFrom(), oneMatrix.getWeekendHoursTo(), oneMatrix.getDelievryMinutes(),
					Boolean.FALSE, oneMatrix.getRoutingCountryId());

		}
		/*
		 * ******** TimeZones ********** Asia/Kolkata Asia/Kuwait Asia/Karachi
		 * America/New_York Asia/Singapore Australia/Sydney America/Los_Angeles
		 * 
		 */

		/*
		 * remitRoutingManager.getEstimatedBlockDelivery(System.currentTimeMillis(),
		 * "Asia/Kolkata", oneMatrix.getWeekFrom(), oneMatrix.getWeekTo(),
		 * oneMatrix.getWeekHoursFrom(), oneMatrix.getWeekHoursTo(),
		 * oneMatrix.getWeekendFrom(), oneMatrix.getWeekendTo(),
		 * oneMatrix.getWeekendHoursFrom(), oneMatrix.getWeekendHoursTo(),
		 * oneMatrix.getDelievryMinutes(), Boolean.FALSE,
		 * oneMatrix.getRoutingCountryId());
		 * 
		 * 
		 * remitRoutingManager.getEstimatedBlockDelivery(System.currentTimeMillis(),
		 * "Asia/Kuwait", oneMatrix.getWeekFrom(), oneMatrix.getWeekTo(),
		 * oneMatrix.getWeekHoursFrom(), oneMatrix.getWeekHoursTo(),
		 * oneMatrix.getWeekendFrom(), oneMatrix.getWeekendTo(),
		 * oneMatrix.getWeekendHoursFrom(), oneMatrix.getWeekendHoursTo(),
		 * oneMatrix.getDelievryMinutes(), Boolean.FALSE,
		 * oneMatrix.getRoutingCountryId());
		 * 
		 * remitRoutingManager.getEstimatedBlockDelivery(System.currentTimeMillis(),
		 * "America/New_York", oneMatrix.getWeekFrom(), oneMatrix.getWeekTo(),
		 * oneMatrix.getWeekHoursFrom(), oneMatrix.getWeekHoursTo(),
		 * oneMatrix.getWeekendFrom(), oneMatrix.getWeekendTo(),
		 * oneMatrix.getWeekendHoursFrom(), oneMatrix.getWeekendHoursTo(),
		 * oneMatrix.getDelievryMinutes(), Boolean.FALSE,
		 * oneMatrix.getRoutingCountryId());
		 */

		List<BigDecimal> routingBankIds = routingMatrix.stream().map(rm -> rm.getRoutingBankId()).distinct()
				.collect(Collectors.toList());

		dprRequestDto.setRoutingBankIds(routingBankIds);

		dprRequestDto.setPricingLevel(PRICE_BY.ROUTING_BANK);

		remitPriceManager.computeBaseSellRatesPrices(dprRequestDto);

		PricingResponseDTO pricingResponseDTO = new PricingResponseDTO();

		pricingResponseDTO.setBankDetails(computeRequestTransientDataCache.getBankDetails());

		pricingResponseDTO.setSellRateDetails(computeRequestTransientDataCache.getSellRateDetails());

		Collections.sort(pricingResponseDTO.getSellRateDetails(), Collections.reverseOrder());

		return pricingResponseDTO;

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
