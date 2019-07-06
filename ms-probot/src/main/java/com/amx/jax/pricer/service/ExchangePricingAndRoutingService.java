/**
 * 
 */
package com.amx.jax.pricer.service;

//import static com.amx.jax.pricer.var.PricerServiceConstants.DEFAULT_ONLINE_SERVICE_ID;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.cache.ExchRateAndRoutingTransientDataCache;
import com.amx.jax.cache.TransientRoutingComputeDetails;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.multitenant.TenantContext;
import com.amx.jax.partner.dto.RoutingBankDetails;
import com.amx.jax.partner.dto.SrvPrvFeeInqReqDTO;
import com.amx.jax.partner.dto.SrvPrvFeeInqResDTO;
import com.amx.jax.pricer.dao.CustomerDao;
import com.amx.jax.pricer.dbmodel.Customer;
import com.amx.jax.pricer.dbmodel.TimezoneMasterModel;
import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.BankDetailsDTO;
import com.amx.jax.pricer.dto.EstimatedDeliveryDetails;
import com.amx.jax.pricer.dto.ExchangeDiscountInfo;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingResponse;
import com.amx.jax.pricer.dto.ExchangeRateBreakup;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.dto.TrnxRoutingDetails;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.manager.CustomerDiscountManager;
import com.amx.jax.pricer.manager.RemitPriceManager;
import com.amx.jax.pricer.manager.RemitRoutingManager;
import com.amx.jax.pricer.manager.ServiceProviderManager;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_INDICATOR;
import com.amx.utils.JsonUtil;

/**
 * @author abhijeet
 *
 */
@Service
public class ExchangePricingAndRoutingService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangePricingAndRoutingService.class);

	@Autowired
	ServiceProviderManager serviceProviderManager;

	@Autowired
	PartnerExchDataService partnerDataService;

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

		// 1 : Old
		// Collections.sort(pricingResponseDTO.getSellRateDetails(),
		// Collections.reverseOrder());

		// Sort Order Modified : Sorting on the basis of InverseRate Now.
		Collections.sort(pricingResponseDTO.getSellRateDetails());

		pricingResponseDTO.setInfo(exchRateAndRoutingTransientDataCache.getInfo());

		pricingResponseDTO.setServiceIdDescription(getServiceIdDescriptions());

		return pricingResponseDTO;
	}

	public PricingResponseDTO fetchBaseRemitPrices(PricingRequestDTO pricingRequestDTO) {

		validatePricingRequest(pricingRequestDTO, Boolean.FALSE);

		remitPriceManager.computeBaseSellRatesPrices(pricingRequestDTO);

		PricingResponseDTO pricingResponseDTO = new PricingResponseDTO();

		pricingResponseDTO.setBankDetails(exchRateAndRoutingTransientDataCache.getBankDetails());

		pricingResponseDTO.setSellRateDetails(exchRateAndRoutingTransientDataCache.getSellRateDetails());

		pricingResponseDTO.setServiceIdDescription(getServiceIdDescriptions());

		// 2
		// Collections.sort(pricingResponseDTO.getSellRateDetails(),
		// Collections.reverseOrder());

		// Sort Order Modified : Sorting on the basis of InverseRate Now.
		Collections.sort(pricingResponseDTO.getSellRateDetails());

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

			// 3
			// Collections.sort(pricingResponseDTO.getSellRateDetails(),
			// Collections.reverseOrder());

			// Sort Order Modified : Sorting on the basis of InverseRate Now.
			Collections.sort(pricingResponseDTO.getSellRateDetails());

			pricingResponseDTO.setInfo(exchRateAndRoutingTransientDataCache.getInfo());

			pricingResponseDTO.setServiceIdDescription(getServiceIdDescriptions());

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

		// BigDecimal homeSendId = new BigDecimal(5759);
		boolean isSPRouting = false;
		ViewExRoutingMatrix homeSendMatrix = null;

		List<ViewExRoutingMatrix> serviceProviderMatrix = remitRoutingManager.filterServiceProviders(routingMatrix);

		/*
		 * for (ViewExRoutingMatrix matrix : routingMatrix) { if
		 * (matrix.getRoutingBankCode().equalsIgnoreCase("HOME")) {
		 * serviceProviderMatrix.add(matrix); homeSendRouting = matrix; } }
		 */

		Future<SrvPrvFeeInqResDTO> sProviderFuture = null;

		if (!serviceProviderMatrix.isEmpty()) {
			isSPRouting = true;
			homeSendMatrix = serviceProviderMatrix.get(0);

			// asynch Call to get the Service Provider Prices
			sProviderFuture = serviceProviderManager.getServiceProviderQuote(homeSendMatrix,
					exchangeRateAndRoutingRequest);

		}

		// Get Non-Service-Provider Core Routing Bank Ids.
		List<BigDecimal> routingBankIds = remitRoutingManager.getRoutingBankIds(routingMatrix);

		exchangeRateAndRoutingRequest.setRoutingBankIds(routingBankIds);

		exchangeRateAndRoutingRequest.setPricingLevel(PRICE_BY.ROUTING_BANK);

		// Get The Rates for Routing Banks.
		remitPriceManager.computeBaseSellRatesPrices(exchangeRateAndRoutingRequest);

		SrvPrvFeeInqResDTO partnerResp = null;

		if (isSPRouting && sProviderFuture != null) {
			// Blocking Call
			try {

				long timeHS = System.currentTimeMillis();

				System.out.println("======= Blocked For Homesend Response ======");

				partnerResp = sProviderFuture.get();

				System.out.println("======= Released : Time taken ==> " + (System.currentTimeMillis() - timeHS) / 1000);

				// process here
				serviceProviderManager.processServiceProviderData(homeSendMatrix, partnerResp);

			} catch (InterruptedException | ExecutionException e1) {
				e1.printStackTrace();
			} catch (PricerServiceException pe) {
				pe.printStackTrace();
			}
		}

		System.out.println(" All Transaction Rates ==> "
				+ JsonUtil.toJson(exchRateAndRoutingTransientDataCache.getSellRateDetails()));

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

		// Customer Category:
		resp.setCustomerCategory(exchRateAndRoutingTransientDataCache.getCustomerCategory());

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

		// 4
		// Collections.sort(routingMatrixData, Collections.reverseOrder());

		// Sort Order Modified : Sorting on the basis of InverseRate Now.
		Collections.sort(routingMatrixData);

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

		TransientRoutingComputeDetails lastNoBeneRoute = null;
		TransientRoutingComputeDetails lastBeneDedRoute = null;

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
			// Paths are sorted -
			// 1. Ascending order of rate
			// 2. Ascending order of delivery time

			if (lastNoBeneRoute == null) {
				bestExchangeRatePaths.get(PRICE_TYPE.NO_BENE_DEDUCT).add(pathKey);
				lastNoBeneRoute = routeDetails;
			} else {

				if (lastNoBeneRoute.compareTo(routeDetails) == 0) {

					// Ignore if the rate is from the same Bank
					if (lastNoBeneRoute.getExchangeRateDetails().getBankId().longValue() != routeDetails
							.getExchangeRateDetails().getBankId().longValue()) {

						// GLC Comparison goes here.
						BigDecimal lastGlcBalFc = exchRateAndRoutingTransientDataCache
								.getMaxGLLcBalForBank(lastNoBeneRoute.getExchangeRateDetails().getBankId(), true);

						BigDecimal curGLCBalFc = exchRateAndRoutingTransientDataCache
								.getMaxGLLcBalForBank(routeDetails.getExchangeRateDetails().getBankId(), true);

						if (curGLCBalFc.compareTo(lastGlcBalFc) > 0) {
							// Replace only if the Current GLC BAL is More than the prev GLC BAL
							int replaceIndex = bestExchangeRatePaths.get(PRICE_TYPE.NO_BENE_DEDUCT).size() - 1;
							bestExchangeRatePaths.get(PRICE_TYPE.NO_BENE_DEDUCT).set(replaceIndex, pathKey);

						}
					}

				} else if (lastNoBeneRoute.getFinalDeliveryDetails().getCompletionTT() > routeDetails
						.getFinalDeliveryDetails().getCompletionTT()) {
					// Add the new path only if its making faster delivery with Lower Exchange Rate.
					bestExchangeRatePaths.get(PRICE_TYPE.NO_BENE_DEDUCT).add(pathKey);
					lastNoBeneRoute = routeDetails;
				}
			}

			// Best Rates of Bene Deduct
			if (null != trnxRoutingPath.getBeneDeductChargeAmount()
					&& trnxRoutingPath.getBeneDeductChargeAmount().compareTo(BigDecimal.ZERO) != 0) {

				if (lastBeneDedRoute == null) {
					bestExchangeRatePaths.get(PRICE_TYPE.BENE_DEDUCT).add(pathKey);
					lastBeneDedRoute = routeDetails;
				} else {

					if (lastBeneDedRoute.compareTo(routeDetails) == 0) {

						// GLC Comparison goes here.
						BigDecimal lastGlcBalFc = exchRateAndRoutingTransientDataCache
								.getMaxGLLcBalForBank(lastNoBeneRoute.getExchangeRateDetails().getBankId(), true);

						BigDecimal curGLCBalFc = exchRateAndRoutingTransientDataCache
								.getMaxGLLcBalForBank(routeDetails.getExchangeRateDetails().getBankId(), true);

						if (curGLCBalFc.compareTo(lastGlcBalFc) > 0) {
							// Replace only if the Current GLC BAL is More than the prev GLC BAL
							int replaceIndex = bestExchangeRatePaths.get(PRICE_TYPE.BENE_DEDUCT).size() - 1;
							bestExchangeRatePaths.get(PRICE_TYPE.BENE_DEDUCT).set(replaceIndex, pathKey);

						}

					} else if (lastBeneDedRoute.getFinalDeliveryDetails().getCompletionTT() > routeDetails
							.getFinalDeliveryDetails().getCompletionTT()) {
						// Add the new path only if its making faster delivery with Lower Exchange Rate.
						bestExchangeRatePaths.get(PRICE_TYPE.BENE_DEDUCT).add(pathKey);
						lastBeneDedRoute = routeDetails;
					}
				}

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

		resp.setServiceIdDescription(getServiceIdDescriptions());

		// resp.setInfo(exchRateAndRoutingTransientDataCache.getInfo());

		/*
		 * if (isSPRouting) {
		 * 
		 * addHomeSendInfo(resp, homeSendMatrix, exchangeRateAndRoutingRequest);
		 * 
		 * }
		 */
		return resp;

	}

	private ExchangeRateAndRoutingResponse addHomeSendInfo(ExchangeRateAndRoutingResponse resp,
			ViewExRoutingMatrix homeSendMatrix, ExchangeRateAndRoutingRequest request) {

		System.out.println(" Tenant Context Parent  ==> " + TenantContext.getCurrentTenant());

		Future<SrvPrvFeeInqResDTO> sProviderFuture = serviceProviderManager.getServiceProviderQuote(homeSendMatrix,
				request);

		System.out.println(" ========= Waiting For HomeSend thread to complete ======== ");

		// Wait for thread to complete
		System.out.println("======= Blocked 1======");

		SrvPrvFeeInqResDTO partnerResp;
		try {

			long timeHS = System.currentTimeMillis();

			System.out.println("======= Blocked 2======");

			partnerResp = sProviderFuture.get();

			System.out.println("======= Released : Time taken ==> " + (System.currentTimeMillis() - timeHS) / 1000);

		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
			return null;
		}

		ExchangeDiscountInfo ccDiscount = new ExchangeDiscountInfo();
		ccDiscount.setDiscountType(DISCOUNT_TYPE.CUSTOMER_CATEGORY);
		ccDiscount.setDiscountPipsValue(BigDecimal.ZERO);
		ccDiscount.setDiscountTypeValue("BRONZE");
		ccDiscount.setId(BigDecimal.ONE);

		ExchangeDiscountInfo channelDiscount = new ExchangeDiscountInfo();
		channelDiscount.setDiscountType(DISCOUNT_TYPE.CHANNEL);
		channelDiscount.setDiscountPipsValue(BigDecimal.ZERO);
		channelDiscount.setDiscountTypeValue("ONLINE");
		channelDiscount.setId(BigDecimal.ONE);

		ExchangeDiscountInfo pipsDiscount = new ExchangeDiscountInfo();
		pipsDiscount.setDiscountType(DISCOUNT_TYPE.AMOUNT_SLAB);
		pipsDiscount.setDiscountPipsValue(BigDecimal.ZERO);
		pipsDiscount.setDiscountTypeValue("0-50000");
		pipsDiscount.setId(BigDecimal.ONE);

		Map<DISCOUNT_TYPE, ExchangeDiscountInfo> customerDiscountDetails = new HashMap<>();

		customerDiscountDetails.put(DISCOUNT_TYPE.CUSTOMER_CATEGORY, ccDiscount);
		customerDiscountDetails.put(DISCOUNT_TYPE.CHANNEL, channelDiscount);
		customerDiscountDetails.put(DISCOUNT_TYPE.AMOUNT_SLAB, pipsDiscount);

		ExchangeRateBreakup sellRateBase = new ExchangeRateBreakup();

		sellRateBase.setInverseRate(partnerResp.getExchangeRateWithPips());

		if (partnerResp.getExchangeRateWithPips() != null
				&& partnerResp.getExchangeRateWithPips().compareTo(BigDecimal.ZERO) != 0) {

			int DEF_DECIMAL_SCALE = 8;

			MathContext DEF_CONTEXT = new MathContext(DEF_DECIMAL_SCALE, RoundingMode.HALF_EVEN);

			sellRateBase.setRate(BigDecimal.ONE.divide(partnerResp.getExchangeRateWithPips(), DEF_CONTEXT));
		} else {
			sellRateBase.setRate(BigDecimal.ZERO);
		}

		sellRateBase.setConvertedLCAmount(partnerResp.getGrossAmount());
		sellRateBase.setConvertedFCAmount(partnerResp.getForeignAmount());

		ExchangeRateDetails exchRateDetails = new ExchangeRateDetails();
		exchRateDetails.setBankId(homeSendMatrix.getRoutingBankId());
		exchRateDetails.setCostRateLimitReached(false);
		exchRateDetails.setCustomerDiscountDetails(customerDiscountDetails);
		exchRateDetails.setDiscountAvailed(false);
		exchRateDetails.setLowGLBalance(false);
		exchRateDetails.setSellRateBase(sellRateBase);
		exchRateDetails.setSellRateNet(sellRateBase);
		exchRateDetails.setServiceIndicatorId(homeSendMatrix.getServiceMasterId());

		// Fill Bank Details

		BankDetailsDTO bankDetailsDTO = new BankDetailsDTO();
		bankDetailsDTO.setBankCode(homeSendMatrix.getRoutingBankCode());
		bankDetailsDTO.setBankCountryId(homeSendMatrix.getRoutingCountryId());
		bankDetailsDTO.setBankFullName(homeSendMatrix.getRoutingBankCode());
		bankDetailsDTO.setBankId(homeSendMatrix.getRoutingBankId());
		bankDetailsDTO.setBankShortName(homeSendMatrix.getRoutingBankCode());

		// Routing Details

		EstimatedDeliveryDetails estimatedDeliveryDetails = new EstimatedDeliveryDetails();
		estimatedDeliveryDetails.setStartTT(System.currentTimeMillis());
		estimatedDeliveryDetails.setCompletionTT(estimatedDeliveryDetails.getStartTT() + (3 * 60 * 60 * 1000));
		estimatedDeliveryDetails.setCrossedMaxDeliveryDays(false);
		estimatedDeliveryDetails.setDeliveryDuration("3 hr");
		estimatedDeliveryDetails.setHolidayDelayInDays(0);
		estimatedDeliveryDetails.setNonWorkingDelayInDays(0);
		estimatedDeliveryDetails.setProcessTimeAbsoluteInSeconds(3 * 60 * 60);
		estimatedDeliveryDetails.setProcessTimeOperationalInSeconds(3 * 60 * 60);
		estimatedDeliveryDetails.setProcessTimeTotalInSeconds(3 * 60 * 60);

		TrnxRoutingDetails trnxRoutingDetails = new TrnxRoutingDetails();
		trnxRoutingDetails.setEstimatedDeliveryDetails(estimatedDeliveryDetails);

		try {
			BeanUtils.copyProperties(trnxRoutingDetails, homeSendMatrix);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		trnxRoutingDetails.setChargeAmount(partnerResp.getCommissionAmount());

		//// @formatter:off
		
		String pathKey = homeSendMatrix.getRoutingCountryId()
						+ "-" + homeSendMatrix.getRoutingBankId() 
						+ "-" + homeSendMatrix.getServiceMasterId()
						+ "-" + homeSendMatrix.getDeliveryModeId()
						+ "-" + homeSendMatrix.getRemittanceModeId()
						+ "-" + homeSendMatrix.getBankBranchId();						

	    //// @formatter:on

		Map<BigDecimal, ExchangeRateDetails> serviceModeSellRate = new HashMap<>();
		serviceModeSellRate.put(exchRateDetails.getServiceIndicatorId(), exchRateDetails);

		if (null == resp.getBankServiceModeSellRates() || resp.getBankServiceModeSellRates().isEmpty()) {
			Map<BigDecimal, Map<BigDecimal, ExchangeRateDetails>> bankServiceModeSellRates = new HashMap<>();
			resp.setBankServiceModeSellRates(bankServiceModeSellRates);
		}

		resp.getBankServiceModeSellRates().put(exchRateDetails.getBankId(), serviceModeSellRate);

		if (resp.getBankDetails() == null || resp.getBankDetails().isEmpty()) {
			Map<BigDecimal, BankDetailsDTO> bankDetails = new HashMap<>();
			resp.setBankDetails(bankDetails);
		}

		resp.getBankDetails().put(bankDetailsDTO.getBankId(), bankDetailsDTO);

		if (resp.getTrnxRoutingPaths() == null || resp.getTrnxRoutingPaths().isEmpty()) {
			Map<String, TrnxRoutingDetails> paths = new HashMap<>();
			resp.setTrnxRoutingPaths(paths);
		}

		resp.getTrnxRoutingPaths().put(pathKey, trnxRoutingDetails);

		if (resp.getBestExchangeRatePaths() == null || resp.getBestExchangeRatePaths().isEmpty()) {
			Map<PRICE_TYPE, List<String>> bestExchangeRatePaths = new HashMap<>();
			bestExchangeRatePaths.put(PRICE_TYPE.NO_BENE_DEDUCT, new ArrayList<String>());

			resp.setBestExchangeRatePaths(bestExchangeRatePaths);
		}

		resp.getBestExchangeRatePaths().get(PRICE_TYPE.NO_BENE_DEDUCT).add(0, pathKey);

		resp.setHomeSendSrvcProviderInfo(partnerResp.getHomeSendInfoDTO());

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

		return Boolean.TRUE;
	}

	private Map<BigDecimal, String> getServiceIdDescriptions() {

		Map<BigDecimal, String> serviceIdDescription = new HashMap<BigDecimal, String>();

		for (SERVICE_INDICATOR ind : SERVICE_INDICATOR.values()) {
			serviceIdDescription.put(BigDecimal.valueOf(ind.getServiceId()), ind.getDescription());
		}

		return serviceIdDescription;
	}

}
