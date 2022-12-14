package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.cache.ExchRateAndRoutingTransientDataCache;
import com.amx.jax.cache.TransientRoutingComputeDetails;
import com.amx.jax.cache.WorkingHoursData;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.pricer.dao.CountryMasterDao;
import com.amx.jax.pricer.dao.PartnerServiceDao;
import com.amx.jax.pricer.dao.TimezoneDao;
import com.amx.jax.pricer.dao.TreasuryFTImpactDao;
import com.amx.jax.pricer.dao.ViewExRoutingMatrixDao;
import com.amx.jax.pricer.dbmodel.BenificiaryListView;
import com.amx.jax.pricer.dbmodel.CountryMasterModel;
import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;
import com.amx.jax.pricer.dbmodel.TimezoneMasterModel;
import com.amx.jax.pricer.dbmodel.TreasuryFundTimeImpact;
import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.EstimatedDeliveryDetails;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.dto.ExchangeRateBreakup;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.var.PricerServiceConstants;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_GROUP;
import com.amx.utils.ArgUtil;
import com.amx.utils.DateUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemitRoutingManager {

	private static final int MAX_DELIVERY_ATTEMPT_DAYS = 60;

	private static final BigDecimal DEF_DELIVERY_HRS = new BigDecimal(5 * 24);

	// private static final BigDecimal FROM_AMT_FRACTION = new
	// BigDecimal(0.00000001);

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM hh:mm a");

	private static final BigDecimal DEF_TR_TRANSACTION_START_TIME = new BigDecimal("10");
	private static final BigDecimal DEF_TR_TRANSACTION_END_TIME = new BigDecimal("14.3");

	private static final BigDecimal DEF_TR_WORK_DAY_FROM = new BigDecimal("2");
	private static final BigDecimal DEF_TR_WORK_DAY_TO = new BigDecimal("5");

	private static final BigDecimal BIGD_SIXTY = new BigDecimal("60");

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RemitRoutingManager.class);

	@Autowired
	ViewExRoutingMatrixDao viewExRoutingMatrixDao;

	@Autowired
	HolidayListManager holidayListManager;

	@Autowired
	TimezoneDao tzDao;

	@Autowired
	CountryMasterDao countryMasterDao;

	@Autowired
	TreasuryFTImpactDao treasuryFTImpactDao;

	@Resource
	ExchRateAndRoutingTransientDataCache transientDataCache;
	
	@Autowired
	PartnerServiceDao partnerServiceDao;

	public boolean validateViewRoutingMatrixData(ViewExRoutingMatrix routingMatrix,
			ExchangeRateAndRoutingRequest routingRequest) {

		if (routingMatrix == null) {
			throw new PricerServiceException(PricerServiceError.NULL_ROUTING_MATRIX,
					"Null Routing Matrix Received for request Params: " + routingRequest.toJSON());
		}

		return true;

	}

	public List<ViewExRoutingMatrix> getRoutingMatrixForRemittance(
			ExchangeRateAndRoutingRequest exchangeRateAndRoutingRequest) {

		List<ViewExRoutingMatrix> routingMatrix = null;

		if (SERVICE_GROUP.CASH.equals(exchangeRateAndRoutingRequest.getServiceGroup())) {

			/**
			 * Only For Cash : <br>
			 * Hard Condition : RoutingBankId = BeneBankId And RoutingBankBranchId = BeneBankBranchId # wrong
			 * cash logic to fetch routing bank Id and bank branch Id from beneficiary creation
			 */
			
			BenificiaryListView beneficaryDetails = partnerServiceDao.getBeneficiaryDetails(exchangeRateAndRoutingRequest.getCustomerId(),exchangeRateAndRoutingRequest.getBeneficiaryId());
			if(beneficaryDetails != null) {
				routingMatrix = viewExRoutingMatrixDao.getRoutingMatrixForCashService(
						exchangeRateAndRoutingRequest.getLocalCountryId(),
						exchangeRateAndRoutingRequest.getForeignCountryId(),
						exchangeRateAndRoutingRequest.getBeneficiaryBankId(),
						exchangeRateAndRoutingRequest.getBeneficiaryBranchId(),
						exchangeRateAndRoutingRequest.getForeignCurrencyId(),
						exchangeRateAndRoutingRequest.getServiceGroup().getGroupCode(),
						beneficaryDetails.getServiceProvider(), 
						beneficaryDetails.getServiceProviderBranchId()); 
			}
			
			/*routingMatrix = viewExRoutingMatrixDao.getRoutingMatrixForCashService(
					exchangeRateAndRoutingRequest.getLocalCountryId(),
					exchangeRateAndRoutingRequest.getForeignCountryId(),
					exchangeRateAndRoutingRequest.getBeneficiaryBankId(),
					exchangeRateAndRoutingRequest.getBeneficiaryBranchId(),
					exchangeRateAndRoutingRequest.getForeignCurrencyId(),
					exchangeRateAndRoutingRequest.getServiceGroup().getGroupCode(),
					exchangeRateAndRoutingRequest.getBeneficiaryBankId(), // RoutingBankId = BeneBankId
					exchangeRateAndRoutingRequest.getBeneficiaryBranchId()); // RoutingBankBranchId = BeneBankBranchId*/
		} else {
			// Else Bank
			routingMatrix = viewExRoutingMatrixDao.getRoutingMatrixForBankService(
					exchangeRateAndRoutingRequest.getLocalCountryId(),
					exchangeRateAndRoutingRequest.getForeignCountryId(),
					exchangeRateAndRoutingRequest.getBeneficiaryBankId(),
					exchangeRateAndRoutingRequest.getBeneficiaryBranchId(),
					exchangeRateAndRoutingRequest.getForeignCurrencyId(),
					exchangeRateAndRoutingRequest.getServiceGroup().getGroupCode());
		}

		List<ViewExRoutingMatrix> removeMatrix = new ArrayList<ViewExRoutingMatrix>();

		if (null != routingMatrix && !routingMatrix.isEmpty()
				&& null != exchangeRateAndRoutingRequest.getExcludeCorBanks()
				&& !exchangeRateAndRoutingRequest.getExcludeCorBanks().isEmpty()) {

			List<BigDecimal> removeList = exchangeRateAndRoutingRequest.getExcludeCorBanks();

			// Set<BigDecimal> removeSet = removeList.stream().distinct()
			// .collect(Collectors.toSet());

			for (ViewExRoutingMatrix matrix : routingMatrix) {
				if (removeList.contains(matrix.getRoutingBankId())) {
					removeMatrix.add(matrix);
				}
			}

			for (ViewExRoutingMatrix matrix : removeMatrix) {
				routingMatrix.remove(matrix);
			}

		}

		if (null == routingMatrix || routingMatrix.isEmpty()) {

			LOGGER.error("Routing Matrix Data is Empty or Null for the Pricing/Routing Request");

			throw new PricerServiceException(PricerServiceError.INVALID_OR_MISSING_ROUTE,
					"Invalid or Missing Routing Banks to the Beneficiary, Check Routing Setup for:" + " BeneBankId: "
							+ exchangeRateAndRoutingRequest.getBeneficiaryBankId() + ", BeneBranchId: "
							+ exchangeRateAndRoutingRequest.getBeneficiaryBranchId() + ", CurrencyId: "
							+ exchangeRateAndRoutingRequest.getForeignCurrencyId() + ", ServiceGroupCode: "
							+ exchangeRateAndRoutingRequest.getServiceGroup());
		}

		List<TransientRoutingComputeDetails> routingComputationObjects = new ArrayList<TransientRoutingComputeDetails>();

		for (ViewExRoutingMatrix viewExRoutingMatrix : routingMatrix) {
			TransientRoutingComputeDetails obj = new TransientRoutingComputeDetails();
			obj.setViewExRoutingMatrix(viewExRoutingMatrix);
			routingComputationObjects.add(obj);
		}

		transientDataCache.setRoutingMatrixData(routingComputationObjects);

		return routingMatrix;

	}

	public void setExchangeRatesForTransactionRoutes(Channel channel) {

		List<TransientRoutingComputeDetails> routeComputeDetailList = transientDataCache.getRoutingMatrixData();
		List<ExchangeRateDetails> rateList = transientDataCache.getSellRateDetails();

		if (null == routeComputeDetailList || routeComputeDetailList.isEmpty() || null == rateList
				|| rateList.isEmpty()) {
			return;
		}

		boolean isOnlyBankRate = false;

		if (Channel.ONLINE.equals(channel) || Channel.MOBILE.equals(channel)) {
			isOnlyBankRate = true;
		}

		String idSeparator = "#";
		Map<String, ExchangeRateDetails> bankRateMap = new HashMap<String, ExchangeRateDetails>();

		for (ExchangeRateDetails exchRate : rateList) {

			// System.out.println("#### Exchange Rate ==> " + JsonUtil.toJson(exchRate));

			String combinedId;

			if (isOnlyBankRate) {
				combinedId = "" + exchRate.getBankId().longValue();
				// exchRate.setServiceIndicatorId(null);
			} else {
				combinedId = exchRate.getBankId().longValue() + idSeparator
						+ exchRate.getServiceIndicatorId().longValue();
			}

			bankRateMap.put(combinedId, exchRate);

		}

		for (TransientRoutingComputeDetails routeComputeDetails : routeComputeDetailList) {

			ViewExRoutingMatrix view = routeComputeDetails.getViewExRoutingMatrix();

			String combinedId;

			if (isOnlyBankRate) {
				combinedId = "" + view.getRoutingBankId().longValue();
			} else {
				combinedId = view.getRoutingBankId().longValue() + idSeparator + view.getServiceMasterId().longValue();
			}

			ExchangeRateDetails exchangeRateDetails = bankRateMap.get(combinedId);

			// For cash and Service Providers - check if discounts to be applied - EXPLICIT
			// check for - IF ALLOWED, Null : Not Allowed
			// For Bank Default is - Discount Applicable
			if (PricerServiceConstants.SERVICE_GROUP.CASH.getGroupCode().equalsIgnoreCase(view.getServiceGroupCode())
					&& !com.amx.utils.StringUtils.anyMatch(view.getDiscountAllowed(), "Y", "YES")) {

				if (exchangeRateDetails.isDiscountAvailed()) {
					exchangeRateDetails.setSellRateNet(exchangeRateDetails.getSellRateBase());
					exchangeRateDetails.setDiscountAvailed(false);
				}

			}

			routeComputeDetails.setExchangeRateDetails(exchangeRateDetails);

		}

	}

	/**
	 * Computes the Transaction Routes and Delivery Details and saves the data with
	 * the request cache : ExchRateAndRoutingTransientDataCache
	 * 
	 * 
	 * @param exchangeRateAndRoutingRequest
	 */
	public void computeTrnxRoutesAndDelivery(ExchangeRateAndRoutingRequest exchangeRateAndRoutingRequest) {

		String localTimezone = getTimezoneForCountry(exchangeRateAndRoutingRequest.getLocalCountryId());

		List<TransientRoutingComputeDetails> routingDetailsList = transientDataCache.getRoutingMatrixData();

		// Get Routing Matrix Data if not already computed.
		if (null == routingDetailsList || routingDetailsList.isEmpty()) {
			this.getRoutingMatrixForRemittance(exchangeRateAndRoutingRequest);
			routingDetailsList = transientDataCache.getRoutingMatrixData();
		}

		// 1. Process Delivery for Correspondent or Routing Banks for all the Available
		// Routes - as provided by the Routing View Matrix

		// 2. Process Block Delivery for Processing / Intermediary Bank - If Applicable

		// 3. Process Block Delivery for Bene Bank

		for (TransientRoutingComputeDetails routingDetails : routingDetailsList) {

			ViewExRoutingMatrix oneMatrix = routingDetails.getViewExRoutingMatrix();

			if (oneMatrix == null) {
				throw new PricerServiceException(PricerServiceError.NULL_ROUTING_MATRIX,
						"Null Routing Matrix Received for a Routing Bank " + exchangeRateAndRoutingRequest.toJSON());
			}

			BigDecimal routingCountryId = oneMatrix.getRoutingCountryId();

			String timezone = getTimezoneForCountry(routingCountryId);

			if (StringUtils.isEmpty(timezone)) {
				throw new PricerServiceException(PricerServiceError.MISSING_TIMEZONE,
						"Timezone is Missing for Correspondent bank Country Id: " + routingCountryId
								+ exchangeRateAndRoutingRequest.toJSON());
			}

			TreasuryFundTimeImpact trImpact = null;
			EstimatedDeliveryDetails estmdTrDeliveryDetails = null;

			// Compute and Add delay for Treasury in Case of Running low GL Balance
			if (routingDetails.getExchangeRateDetails() == null ? false
					: routingDetails.getExchangeRateDetails().isLowGLBalance()) {

				BigDecimal rBankId = routingDetails.getExchangeRateDetails().getBankId();

				estmdTrDeliveryDetails = transientDataCache.getTreasuryFTDelay(rBankId);

				if (estmdTrDeliveryDetails == null) {

					if (routingDetails.getExchangeRateDetails().isFundedIntermediary()) {
						trImpact = transientDataCache.getTreasuryFundTimeImpact(rBankId, true);
					} else {
						trImpact = transientDataCache.getTreasuryFundTimeImpact(rBankId, false);
					}

					// Set to Default.
					if (trImpact == null) {

						LOGGER.warn("Major: Treasury Delay Impact is not Configured for CountryId: " + routingCountryId
								+ " and CurrencyId: " + exchangeRateAndRoutingRequest.getForeignCurrencyId());

						trImpact = new TreasuryFundTimeImpact();
					}
					trImpact.setInTrWindowDayImpact(
							ArgUtil.assignDefaultIfNull(trImpact.getInTrWindowDayImpact(), BigDecimal.ONE));

					trImpact.setInTrWindowTtdImpactMin(
							ArgUtil.assignDefaultIfNull(trImpact.getInTrWindowTtdImpactMin(), BIGD_SIXTY));

					trImpact.setOutOfTrWindowDayImpact(ArgUtil.assignDefaultIfNull(trImpact.getOutOfTrWindowDayImpact(),
							trImpact.getInTrWindowDayImpact()));

					trImpact.setOutOfTrWindowTtdImpactMin(ArgUtil.assignDefaultIfNull(
							trImpact.getOutOfTrWindowTtdImpactMin(), trImpact.getInTrWindowTtdImpactMin()));

					trImpact.setTrnxTimeFrom(
							ArgUtil.assignDefaultIfNull(trImpact.getTrnxTimeFrom(), DEF_TR_TRANSACTION_START_TIME));

					trImpact.setTrnxTimeTo(
							ArgUtil.assignDefaultIfNull(trImpact.getTrnxTimeTo(), DEF_TR_TRANSACTION_END_TIME));

					trImpact.setWorkDayFrom(
							ArgUtil.assignDefaultIfNull(trImpact.getWorkDayFrom(), DEF_TR_WORK_DAY_FROM));

					trImpact.setWorkDayTo(ArgUtil.assignDefaultIfNull(trImpact.getWorkDayTo(), DEF_TR_WORK_DAY_TO));

					int trnxTimeFrom = DateUtil.getHrMinIntVal(String.valueOf(trImpact.getTrnxTimeFrom()));
					int trnxTimeTo = DateUtil.getHrMinIntVal(String.valueOf(trImpact.getTrnxTimeTo()));

					long startTT = transientDataCache.getTrnxBeginTime();

					// Get An instantaneous point on the time-line for EPOCH TT
					Instant epochInstant = Instant.ofEpochMilli(startTT);

					// Get the appropriate Timezone
					ZoneId zoneId = ZoneId.of(localTimezone);

					// Compute the Correct Zone Date and Time of Block Delivery BEGIN
					ZonedDateTime beginZonedDT = ZonedDateTime.ofInstant(epochInstant, zoneId);

					int nowHrMinIntVal = DateUtil.getHrMinIntVal(beginZonedDT.getHour(), beginZonedDT.getMinute());

					boolean isWithinTrWindow = (trnxTimeFrom <= nowHrMinIntVal && trnxTimeTo > nowHrMinIntVal) ? true
							: false;

					BigDecimal ttdImpactHr, ttdImpactDay, ttdImpactMin;

					// Consider In Treasury Window Time *OR* OOTW - Out of Treasury Window Time
					if (isWithinTrWindow) {

						ttdImpactMin = ArgUtil.assignDefaultIfNull(trImpact.getInTrWindowTtdImpactMin(),
								BigDecimal.ZERO);
						ttdImpactDay = trImpact.getInTrWindowDayImpact();

					} else {

						ttdImpactMin = ArgUtil.assignDefaultIfNull(trImpact.getOutOfTrWindowTtdImpactMin(),
								BigDecimal.ZERO);
						ttdImpactDay = trImpact.getOutOfTrWindowDayImpact();

						// Case Same day Funding Day -- offset for a less additional day.
						if (nowHrMinIntVal < trnxTimeFrom && ttdImpactDay.intValue() > 0) {
							ttdImpactDay = ttdImpactDay.subtract(BigDecimal.ONE);
						}

					}

					ttdImpactHr = ttdImpactMin.divide(BIGD_SIXTY, 4, RoundingMode.HALF_EVEN);

					// Compute Treasury Delay
					estmdTrDeliveryDetails = this.getEstimatedBlockDelivery(startTT, localTimezone,
							trImpact.getWorkDayFrom(), trImpact.getWorkDayTo(), trImpact.getTrnxTimeFrom(),
							trImpact.getTrnxTimeTo(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
							BigDecimal.ZERO, ttdImpactHr, false, routingCountryId, ttdImpactDay.intValue(), false,
							new BigDecimal(0));

					transientDataCache.setTreasuryFTDelay(rBankId, estmdTrDeliveryDetails);

				}

			}

			// System.out.println(" Treasury Completion Time ==> " +
			// JsonUtil.toJson(estmdTrDeliveryDetails));

			/**
			 * Start Time for Correspondent Bank Phase is - completion TT of the preceding
			 * Insufficient Treasury Funding scenario - if any else - the transaction begin
			 * time.
			 */
			long correspondentStartTT;
			if (estmdTrDeliveryDetails != null) {
				correspondentStartTT = estmdTrDeliveryDetails.getCompletionTT();
			} else {
				correspondentStartTT = transientDataCache.getTrnxBeginTime();
			}

			String holidayImpactStr = ArgUtil.assignDefaultIfNull(oneMatrix.getIsHolidayImpact(), "Y");

			// Impact : N, No : NoHolidayLag = true
			// Impact : Y, Yes: NoHolidayLag = false

			boolean noHolidayLag = com.amx.utils.StringUtils.anyMatch(holidayImpactStr, "N", "NO") ? true : false;

			EstimatedDeliveryDetails estmdCBDeliveryDetails = this.getEstimatedBlockDelivery(correspondentStartTT,
					timezone, oneMatrix.getWeekFrom(), oneMatrix.getWeekTo(), oneMatrix.getWeekHoursFrom(),
					oneMatrix.getWeekHoursTo(), oneMatrix.getWeekendFrom(), oneMatrix.getWeekendTo(),
					oneMatrix.getWeekendHoursFrom(), oneMatrix.getWeekendHoursTo(), oneMatrix.getDelievryHours(),
					noHolidayLag, routingCountryId, 0, false, BigDecimal.ZERO);

			routingDetails.setRoutingBankDeliveryDetails(estmdCBDeliveryDetails);

			// Processing or Intermediary Country - Delivery Processing Block
			// Process only if the relevant countryId is present.

			BigDecimal processingCountryId = oneMatrix.getProcessingCountryId();
			EstimatedDeliveryDetails estmdProcessingDeliveryDetails = null;
			boolean isProcessLagIntermediary = false;

			if (processingCountryId != null && processingCountryId.longValue() != routingCountryId.longValue()) {

				isProcessLagIntermediary = true;

				/**
				 * Only Holiday's Lag to be considered here. The work time and non-operational
				 * lag is already compensated and taken care at the correspondent bank work hrs
				 * definition.
				 */

				/**
				 * Start Time for Processing / Intermediary Bank Phase is - completion TT of the
				 * preceding Correspondent Bank Phase.
				 */
				long processingStartTT = estmdCBDeliveryDetails.getCompletionTT();

				String pTimezone = getTimezoneForCountry(processingCountryId);

				if (StringUtils.isEmpty(pTimezone)) {
					throw new PricerServiceException(PricerServiceError.MISSING_TIMEZONE,
							"Timezone is Missing for Processing Country Id: " + processingCountryId
									+ exchangeRateAndRoutingRequest.toJSON());
				}

				CountryMasterModel countryMasterModel = getCountryMaster(oneMatrix.getProcessingCountryId());

				// Default is Zero -- if Null
				BigDecimal processingStartTime = null != countryMasterModel.getWorkTimeFrom()
						? countryMasterModel.getWorkTimeFrom()
						: BigDecimal.ZERO;

				// Default is 24 -- if Null
				// BigDecimal processingStopTime = null != countryMaster.getWorkTimeTo() ?
				// countryMaster.getWorkTimeTo()
				// : BigDecimal.valueOf(24);

				estmdProcessingDeliveryDetails = this.getEstimatedBlockDelivery(processingStartTT, pTimezone,
						BigDecimal.ONE, new BigDecimal(7), BigDecimal.ZERO, BigDecimal.valueOf(24), BigDecimal.ZERO,
						BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, Boolean.FALSE,
						processingCountryId, 0, true, processingStartTime);

				routingDetails.setProcessingDeliveryDetails(estmdProcessingDeliveryDetails);

			} // Processing country block

			/**
			 * Bene Country Delivery Processing Block
			 * 
			 * Bene Delivery Lag Conditions: <br/>
			 * 1. If Processing / Intermediary Country Is Present <br/>
			 * 1.1 Processing Country <IS NOT> Bene Country : LAG is APPLICABLE <br/>
			 * 
			 * 2. If Processing / Intermediary Country Is Absent <br/>
			 * 2.1 If Routing Country <IS NOT> Bene Country : LAG is APPLICABLE <br/>
			 * 2.2 If Routing Country <IS SAME AS> Bene Country: LAG is NOT APPLICABLE <br/>
			 * 
			 */

			BigDecimal beneCountryId = oneMatrix.getBeneCountryId();
			EstimatedDeliveryDetails estmdBeneDeliveryDetails = null;
			long beneStartTT;

			boolean isProcessLagForBene = false;

			if (null != estmdProcessingDeliveryDetails) {

				isProcessLagForBene = processingCountryId.longValue() != beneCountryId.longValue() ? true : false;
				beneStartTT = estmdProcessingDeliveryDetails.getCompletionTT();

			} else {

				isProcessLagForBene = routingCountryId.longValue() != beneCountryId.longValue() ? true : false;
				beneStartTT = estmdCBDeliveryDetails.getCompletionTT();
			}

			// Process For Bene

			if (isProcessLagForBene) {

				String beneTimezone = getTimezoneForCountry(beneCountryId);

				if (StringUtils.isEmpty(beneTimezone)) {
					throw new PricerServiceException(PricerServiceError.MISSING_TIMEZONE,
							"Timezone is Missing for Beneficiary Country Id: " + processingCountryId
									+ exchangeRateAndRoutingRequest.toJSON());
				}

				CountryMasterModel countryMasterModel = getCountryMaster(oneMatrix.getBeneCountryId());

				// Default is Zero -- if Null
				BigDecimal beneProcessStartTime = null != countryMasterModel.getWorkTimeFrom()
						? countryMasterModel.getWorkTimeFrom()
						: BigDecimal.ZERO;

				// Default is 24 -- if Null
				// BigDecimal beneProcessStopTime = null != countryMaster.getWorkTimeTo() ?
				// countryMaster.getWorkTimeTo()
				// : BigDecimal.valueOf(24);

				estmdBeneDeliveryDetails = this.getEstimatedBlockDelivery(beneStartTT, beneTimezone, BigDecimal.ONE,
						new BigDecimal(7), BigDecimal.ZERO, BigDecimal.valueOf(24), BigDecimal.ZERO, BigDecimal.ZERO,
						BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, Boolean.FALSE, beneCountryId, 0, true,
						beneProcessStartTime);

				routingDetails.setBeneBankDeliveryDetails(estmdBeneDeliveryDetails);

			} // BeneBlock

			/**
			 * Compute Final Delivery For the Complete Transaction
			 */

			EstimatedDeliveryDetails finalDeliveryDetails = new EstimatedDeliveryDetails();

			long processTimeAbs = estmdCBDeliveryDetails.getProcessTimeAbsoluteInSeconds();
			long processTimeOps = estmdCBDeliveryDetails.getProcessTimeOperationalInSeconds();

			long processTimeTotal = estmdCBDeliveryDetails.getProcessTimeTotalInSeconds();

			long nonWorkingDelayInDays = estmdCBDeliveryDetails.getNonWorkingDelayInDays();
			long holidayDelay = estmdCBDeliveryDetails.getHolidayDelayInDays();

			long finalCompletionTT = estmdCBDeliveryDetails.getCompletionTT();

			ZonedDateTime startDateForeign = estmdCBDeliveryDetails.getStartDateForeign();
			ZonedDateTime completionDateForeign = estmdCBDeliveryDetails.getCompletionDateForeign();

			boolean crossedMaxDeliveryDays = estmdCBDeliveryDetails.isCrossedMaxDeliveryDays();

			long insufficientFundDelay = 0;
			if (estmdTrDeliveryDetails != null) {
				insufficientFundDelay = estmdTrDeliveryDetails.getProcessTimeTotalInSeconds();
				processTimeOps += estmdTrDeliveryDetails.getProcessTimeOperationalInSeconds();
				processTimeTotal += estmdTrDeliveryDetails.getProcessTimeTotalInSeconds();

				holidayDelay += estmdTrDeliveryDetails.getHolidayDelayInDays();
			}

			if (isProcessLagIntermediary && null != estmdProcessingDeliveryDetails) {
				processTimeAbs += estmdProcessingDeliveryDetails.getProcessTimeAbsoluteInSeconds();
				processTimeOps += estmdProcessingDeliveryDetails.getProcessTimeOperationalInSeconds();
				processTimeTotal += estmdProcessingDeliveryDetails.getProcessTimeTotalInSeconds();

				nonWorkingDelayInDays += estmdProcessingDeliveryDetails.getNonWorkingDelayInDays();
				holidayDelay += estmdProcessingDeliveryDetails.getHolidayDelayInDays();

				// Override the completion Time. This is an absolute figure.
				// Last One to win.
				finalCompletionTT = estmdProcessingDeliveryDetails.getCompletionTT();

				completionDateForeign = estmdProcessingDeliveryDetails.getCompletionDateForeign();
			}

			if (isProcessLagForBene && null != estmdBeneDeliveryDetails) {
				processTimeAbs += estmdBeneDeliveryDetails.getProcessTimeAbsoluteInSeconds();
				processTimeOps += estmdBeneDeliveryDetails.getProcessTimeOperationalInSeconds();
				processTimeTotal += estmdBeneDeliveryDetails.getProcessTimeTotalInSeconds();

				nonWorkingDelayInDays += estmdBeneDeliveryDetails.getNonWorkingDelayInDays();
				holidayDelay += estmdBeneDeliveryDetails.getHolidayDelayInDays();

				// Override the completion Time. This is an absolute figure.
				// Last One to win.
				finalCompletionTT = estmdBeneDeliveryDetails.getCompletionTT();
				completionDateForeign = estmdBeneDeliveryDetails.getCompletionDateForeign();
			}

			// finalDeliveryDetails.setStartTT(transientDataCache.getTrnxBeginTime());

			finalDeliveryDetails.setProcessTimeAbsoluteInSeconds(processTimeAbs);
			finalDeliveryDetails.setProcessTimeOperationalInSeconds(processTimeOps);

			finalDeliveryDetails.setDelayDueToInsufficientFundInSeconds(insufficientFundDelay);

			finalDeliveryDetails.setProcessTimeTotalInSeconds(processTimeTotal);

			finalDeliveryDetails.setNonWorkingDelayInDays(nonWorkingDelayInDays);
			finalDeliveryDetails.setHolidayDelayInDays(holidayDelay);

			finalDeliveryDetails.setCompletionTT(finalCompletionTT);

			finalDeliveryDetails.setStartDateForeign(startDateForeign);
			finalDeliveryDetails.setCompletionDateForeign(completionDateForeign);

			// Change the Duration String -- 12th-Aug-2019
			finalDeliveryDetails.setDeliveryDuration(getDeliveryAtLocalTime(transientDataCache.getTrnxBeginTime(),
					finalCompletionTT, localTimezone, processTimeTotal));

			// finalDeliveryDetails.setDeliveryDuration(getDeliveryDuration(processTimeTotal));

			finalDeliveryDetails.setCrossedMaxDeliveryDays(crossedMaxDeliveryDays);

			routingDetails.setFinalDeliveryDetails(finalDeliveryDetails);

			// System.out.println(" Final Delivery Details ===> " +
			// JsonUtil.toJson(finalDeliveryDetails));

		} // OneMatrix Block

	}

	public void filterTransactionRoutes() {

		List<TransientRoutingComputeDetails> routeDataList = transientDataCache.getRoutingMatrixData();

		List<TransientRoutingComputeDetails> removeList = new ArrayList<TransientRoutingComputeDetails>();

		BigDecimal minFromAmt = null;
		BigDecimal maxtoAmt = null;

		for (TransientRoutingComputeDetails routeData : routeDataList) {

			ViewExRoutingMatrix matrix = routeData.getViewExRoutingMatrix();

			ExchangeRateDetails exchRateDetails = routeData.getExchangeRateDetails();

			if (exchRateDetails == null) {
				removeList.add(routeData);
				continue;
			}

			ExchangeRateBreakup breakup = exchRateDetails.getSellRateNet() != null
					? routeData.getExchangeRateDetails().getSellRateNet()
					: routeData.getExchangeRateDetails().getSellRateBase();

			if (breakup == null) {
				removeList.add(routeData);
				continue;
			}

			/**
			 * Check for Transaction Amount Limit
			 */
			BigDecimal fromAmt = matrix.getFromAmount() == null ? BigDecimal.ZERO : matrix.getFromAmount();
			BigDecimal toAmt = matrix.getToAmount() == null ? PricerServiceConstants.MAX_BIGD_12 : matrix.getToAmount();

			// Adjust the from amount for Range Correction - only for perfect Integer
			// if (fromAmt.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
			// fromAmt = fromAmt.subtract(BigDecimal.ONE).add(FROM_AMT_FRACTION).setScale(8,
			// RoundingMode.UP);
			// }

			if (fromAmt.compareTo(breakup.getConvertedFCAmount()) > 0) {

				removeList.add(routeData);

				if (minFromAmt == null || minFromAmt.compareTo(fromAmt) > 0) {
					minFromAmt = matrix.getFromAmount() == null ? BigDecimal.ZERO : matrix.getFromAmount();
				}

				continue;

			} else if (toAmt.compareTo(breakup.getConvertedFCAmount()) < 0) {

				removeList.add(routeData);

				if (maxtoAmt == null || maxtoAmt.compareTo(toAmt) < 0) {
					maxtoAmt = toAmt;
				}

				continue;
			}

		}

		for (TransientRoutingComputeDetails routeData : removeList) {
			routeDataList.remove(routeData);
		}

		if (routeDataList.isEmpty()) {

			LOGGER.info(
					"No Valid Transaction Routes are Available for Transaction Routing: From / To Amount Check Limit is Voided");

			if (minFromAmt != null) {
				throw new PricerServiceException(PricerServiceError.INVALID_TNX_AMOUNT_TOO_LOW,
						"No valid transaction routes are eligible for amount less than " + minFromAmt + " FC");
			} else if (maxtoAmt != null) {
				throw new PricerServiceException(PricerServiceError.INVALID_TNX_AMOUNT_TOO_HIGH,
						"No valid transaction routes are eligible for amount more than " + maxtoAmt + " FC");
			} else {
				throw new PricerServiceException(PricerServiceError.NO_VALID_TNX_ROUTES_AVAILABLE,
						"No Valid Transaction Routes are Eligible for Transaction Routing");
			}
		}

	}

	private EstimatedDeliveryDetails getEstimatedBlockDelivery(long startTT, String timezone, BigDecimal weekFrom,
			BigDecimal weekTo, BigDecimal weekHrsFrom, BigDecimal weekHrsTo, BigDecimal weekEndFrom,
			BigDecimal weekEndTo, BigDecimal weekEndHrsFrom, BigDecimal weekEndHrsTo, BigDecimal processTimeInHrs,
			boolean noHolidayLag, BigDecimal countryId, int preDelay, boolean isHolidaySnooze,
			BigDecimal snoozeWakeUpTime) {

		// Get An instantaneous point on the time-line for EPOCH TT
		Instant epochInstant = Instant.ofEpochMilli(startTT);

		// Get the appropriate Timezone
		ZoneId zoneId = ZoneId.of(timezone);

		// Compute the Correct Zone Date and Time of Block Delivery BEGIN
		ZonedDateTime beginZonedDT = ZonedDateTime.ofInstant(epochInstant, zoneId);

		/**
		 * Removed noHolidayLag : on 14th Sept 2019 : For GLCDelay - which could turn
		 * this flag on later
		 */
		if (!noHolidayLag && !transientDataCache.isHolidayListSetForCountry(countryId)) {
			List<HolidayListMasterModel> sortedHolidays = holidayListManager.getHoidaysForCountryAndDateRange(countryId,
					Date.from(beginZonedDT.toInstant()),
					Date.from(beginZonedDT.plusDays(MAX_DELIVERY_ATTEMPT_DAYS).toInstant()));

			transientDataCache.setHolidaysForCountry(countryId, sortedHolidays);

		}

		WorkingHoursData workingHoursData = this.computeWorkMatrix(weekFrom, weekTo, weekHrsFrom, weekHrsTo,
				weekEndFrom, weekEndTo, weekEndHrsFrom, weekEndHrsTo, processTimeInHrs, isHolidaySnooze,
				snoozeWakeUpTime);

		EstimatedDeliveryDetails goodBusinessDeliveryDT = this.getGoodBusinessDateTime(beginZonedDT, workingHoursData,
				countryId, noHolidayLag, preDelay);

		/**
		 * Add Delivery Processing Time to Arrive at the Block Completion Time.
		 */

		goodBusinessDeliveryDT.addToProcessTimeAbsolute(workingHoursData.getTotalProcessingTimeInSec());

		/**
		 * Compute Completion Time
		 */
		ZonedDateTime blockDeliveryCompletionDT = goodBusinessDeliveryDT.getStartDateForeign()
				.plusSeconds(goodBusinessDeliveryDT.getProcessTimeAbsoluteInSeconds());

		goodBusinessDeliveryDT.setCompletionDateForeign(blockDeliveryCompletionDT);
		goodBusinessDeliveryDT.setCompletionTT(blockDeliveryCompletionDT.toInstant().toEpochMilli());

		// System.out.println(" Estimated Delivery Details ===> " +
		// JsonUtil.toJson(goodBusinessDeliveryDT));

		return goodBusinessDeliveryDT;
	}

	private CountryMasterModel getCountryMaster(BigDecimal countryId) {
		CountryMasterModel countryMasterModel = transientDataCache.getCountryById(countryId);

		if (null == countryMasterModel) {
			countryMasterModel = countryMasterDao.getByCountryId(countryId);
			if (null != countryMasterModel) {
				transientDataCache.setCountry(countryMasterModel);
			}
		}

		return countryMasterModel;
	}

	private String getTimezoneForCountry(BigDecimal countryId) {

		TimezoneMasterModel tzMasterModel = transientDataCache.getTimezoneForCountry(countryId);

		if (null == tzMasterModel) {

			CountryMasterModel countryMasterModel = this.getCountryMaster(countryId);

			if (null == countryMasterModel || null == countryMasterModel.getTimezoneId()) {
				return null;
			}

			tzMasterModel = tzDao.findById(countryMasterModel.getTimezoneId());

			if (null == tzMasterModel) {
				return null;
			}

			transientDataCache.setTimezoneForCountry(countryId, tzMasterModel);
		}

		return tzMasterModel.getTimezone();
	}

	private WorkingHoursData computeWorkMatrix(BigDecimal weekFrom, BigDecimal weekTo, BigDecimal weekHrsFrom,
			BigDecimal weekHrsTo, BigDecimal weekEndFrom, BigDecimal weekEndTo, BigDecimal weekEndHrsFrom,
			BigDecimal weekEndHrsTo, BigDecimal processTimeInHrs, boolean isHolidaySnooze,
			BigDecimal snoozeWakeUpTime) {

		// Sanitize the data
		weekFrom = ArgUtil.assignDefaultIfNull(weekFrom, BigDecimal.ZERO);
		weekTo = ArgUtil.assignDefaultIfNull(weekTo, BigDecimal.ZERO);

		weekHrsFrom = ArgUtil.assignDefaultIfNull(weekHrsFrom, BigDecimal.ZERO);
		weekHrsTo = ArgUtil.assignDefaultIfNull(weekHrsTo, BigDecimal.ZERO);

		weekEndFrom = ArgUtil.assignDefaultIfNull(weekEndFrom, BigDecimal.ZERO);
		weekEndTo = ArgUtil.assignDefaultIfNull(weekEndTo, BigDecimal.ZERO);

		weekEndHrsFrom = ArgUtil.assignDefaultIfNull(weekEndHrsFrom, BigDecimal.ZERO);
		weekEndHrsTo = ArgUtil.assignDefaultIfNull(weekEndHrsTo, BigDecimal.ZERO);

		processTimeInHrs = ArgUtil.assignDefaultIfNull(processTimeInHrs, DEF_DELIVERY_HRS);

		WorkingHoursData workingHoursData = new WorkingHoursData();

		// Set Work Hours For the WeekDays.
		if (DateUtil.isValidDayOfWeek(weekFrom.intValue()) && DateUtil.isValidDayOfWeek(weekTo.intValue())) {
			workingHoursData.setWorkHrsThroughArabicDoW(weekFrom.intValue(), weekTo.intValue(),
					weekHrsFrom.doubleValue(), weekHrsTo.doubleValue());
		}

		// Set Work Hours For the WeekEnd.
		if (DateUtil.isValidDayOfWeek(weekEndFrom.intValue()) && DateUtil.isValidDayOfWeek(weekEndTo.intValue())) {
			workingHoursData.setWorkHrsThroughArabicDoW(weekEndFrom.intValue(), weekEndTo.intValue(),
					weekEndHrsFrom.doubleValue(), weekEndHrsTo.doubleValue());
		}

		long procTimeInMin = Math.round(processTimeInHrs.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 60);

		workingHoursData.setTotalProcessingTimeInMins(procTimeInMin);

		// Set Snooze
		if (isHolidaySnooze) {
			snoozeWakeUpTime = ArgUtil.assignDefaultIfNull(snoozeWakeUpTime, BigDecimal.ZERO);
			workingHoursData.setHoldaySnooze(isHolidaySnooze);

			workingHoursData.setSnoozeWakeUpInHrMin(snoozeWakeUpTime.doubleValue());
		}

		// System.out.println(" Work Week matrix ==> " +
		// JsonUtil.toJson(workingHoursData));

		return workingHoursData;
	}

	/**
	 * ** CRITICAL ** <br>
	 * 
	 * Computes the Good Business Day to Start the Block Delivery.
	 * 
	 * Blocks are : Correspondent Delivery, Processing Country and Bene Processing
	 * 
	 * @param beginZonedDT
	 * @param workHrsData
	 * @param countryId
	 * @param noHolidayLag
	 * @return
	 */
	private EstimatedDeliveryDetails getGoodBusinessDateTime(ZonedDateTime beginZonedDT, WorkingHoursData workHrsData,
			BigDecimal countryId, boolean noHolidayLag, int preDelay) {

		EstimatedDeliveryDetails estimatedDeliveryDetails = new EstimatedDeliveryDetails();

		ZonedDateTime estimatedGoodBusinessDay = beginZonedDT;

		boolean isSnoozed = false;

		for (int i = 0; i < MAX_DELIVERY_ATTEMPT_DAYS; i++) {
			/**
			 * Find out if the estimated Good Business Day is a real Good Business Day.
			 */

			// Check if holidays not applicable or its not a holiday on this day.
			boolean isHoliday = false;

			// Default Working Status
			boolean isWorking = true;

			if (preDelay > 0) {
				isWorking = false;
				preDelay--;

			} else if (noHolidayLag
					|| !(isHoliday = transientDataCache.isHolidayOn(countryId, estimatedGoodBusinessDay))) {

				int dayOfWeek = estimatedGoodBusinessDay.getDayOfWeek().getValue();
				int hourOfDay = estimatedGoodBusinessDay.getHour();
				int minOfHr = estimatedGoodBusinessDay.getMinute();

				int hrMinIntVal = DateUtil.getHrMinIntVal(hourOfDay, minOfHr);

				// Current Date Time is Working
				if (workHrsData.isWorkingDayTime(dayOfWeek, hrMinIntVal)) {

					if (workHrsData.isHoldaySnooze() && isSnoozed) {

						long offsetSecs = workHrsData.getSnoozeTimeOffsetInSeconds(dayOfWeek, hrMinIntVal);
						if (offsetSecs >= 0) {

							estimatedGoodBusinessDay = estimatedGoodBusinessDay.plusSeconds(offsetSecs);

							estimatedDeliveryDetails.addToProcessTimeOperational(offsetSecs);
							estimatedDeliveryDetails.setStartDateForeign(estimatedGoodBusinessDay);

						}

					} else {
						// No Holiday Lag or No Holiday on the Date
						estimatedDeliveryDetails.setStartDateForeign(estimatedGoodBusinessDay);
					}

					return estimatedDeliveryDetails;

				} else if (workHrsData.isBeforeWorkingHours(dayOfWeek, hrMinIntVal)) {

					long offsetSecs;

					if (workHrsData.isHoldaySnooze() && isSnoozed) {
						offsetSecs = workHrsData.getSnoozeTimeOffsetInSeconds(dayOfWeek, hrMinIntVal);
					} else {
						offsetSecs = workHrsData.getWorkWindowOffsetInSeconds(dayOfWeek, hrMinIntVal);
					}

					if (offsetSecs >= 0) {
						estimatedGoodBusinessDay = estimatedGoodBusinessDay.plusSeconds(offsetSecs);
						estimatedDeliveryDetails.addToProcessTimeOperational(offsetSecs);
					}

					estimatedDeliveryDetails.setStartDateForeign(estimatedGoodBusinessDay);

					return estimatedDeliveryDetails;

				}

				// Either Its not working Day or its After working Hours
				isWorking = false;
			}

			/**
			 * If the estimated GBD is not working day - roll on to the next day.
			 */

			ZonedDateTime nextEstimatedGoodBusinessDay = DateUtil.getNextZonedDay(estimatedGoodBusinessDay);

			long timeDiffInSec = nextEstimatedGoodBusinessDay.toInstant().getEpochSecond()
					- estimatedGoodBusinessDay.toInstant().getEpochSecond();

			// Next Day - Roll On conditions :

			/**
			 * 1. NO HOLIDAY / Holiday Not Applicable : <code> isHoliday = false</code> 1.1
			 * Weekday Or Working WeekEnd : But Work Hours Have Elapsed :
			 * <code> isWorking=false </code> 1.2 Non-Working Day : isWorking=false
			 * 
			 * 2. Is a HOLIDAY and HolidayApplicable <code> isHoliday = true</code> 2.1
			 * DONT-Care for Working Lag
			 * 
			 */

			if (!isHoliday && !isWorking) {

				// Add Non Working Delay in Seconds till 12.00 PM mid night
				estimatedDeliveryDetails.addToProcessTimeOperational(timeDiffInSec);
				estimatedDeliveryDetails.addToNonWorkingDelay(1);

			} else if (isHoliday) {

				estimatedDeliveryDetails.addToProcessTimeTotal(timeDiffInSec);
				estimatedDeliveryDetails.addToHolidayDelay(1);

				// Set Holiday Snooze On if in Snooze Mode
				if (workHrsData.isHoldaySnooze()) {
					isSnoozed = true;
				}

			}

			estimatedGoodBusinessDay = nextEstimatedGoodBusinessDay;

		} // for(int i...

		// Return Default - Estimated Good Business : D+100

		estimatedDeliveryDetails.setStartDateForeign(estimatedGoodBusinessDay);
		estimatedDeliveryDetails.setCrossedMaxDeliveryDays(true);
		return estimatedDeliveryDetails;

	}

	private String getDeliveryAtLocalTime(long startTT, long completionTT, String timezoneLocal, long durationInSecs) {

		// Get the appropriate Timezone
		ZoneId zoneId = ZoneId.of(timezoneLocal);

		// Compute the Correct Zone Date and Time of Block Delivery BEGIN
		Instant epochStartInstant = Instant.ofEpochMilli(startTT);
		ZonedDateTime beginZonedDT = ZonedDateTime.ofInstant(epochStartInstant, zoneId);

		// Compute the Correct Zone Date and Time of Block Delivery COMPLETE
		Instant epochCompletionInstant = Instant.ofEpochMilli(completionTT);
		ZonedDateTime completionZonedDT = ZonedDateTime.ofInstant(epochCompletionInstant, zoneId);

		String deliveryAt;

		if (beginZonedDT.getDayOfMonth() == completionZonedDT.getDayOfMonth()) {
			deliveryAt = getDeliveryDuration(durationInSecs);
		} else {
			// Set Delivery at : dd-mmm HH:MM
			deliveryAt = completionZonedDT.format(DATE_FORMATTER);
		}

		return deliveryAt;

	}

	private String getDeliveryDuration(long durationInSecs) {

		TimeUnit tu = TimeUnit.SECONDS;
		long days = tu.toDays(durationInSecs);
		long hrs = tu.toHours(durationInSecs);
		long mins = tu.toMinutes(durationInSecs);

		String duration;

		if (days > 0) {
			duration = days == 1 ? days + " day" : days + " days";
			hrs = hrs % 24;
			if (hrs > 0) {
				duration += " " + (hrs == 1 ? hrs + " hour" : hrs + " hours");
			}
		} else if (hrs > 0) {
			duration = hrs == 1 ? hrs + " hour" : hrs + " hours";
			mins = mins % 60;
			if (mins > 0) {
				duration += " " + (mins == 1 ? mins + " minute" : mins + " minutes");
			}
		} else {
			duration = DurationFormatUtils.formatDurationWords(durationInSecs * 1000, true, true);
		}

		// Simple Duration Converter
		return duration;

	}

	/**
	 * 
	 * @param routingMatrix
	 * @return ServiceProviders View Matrix List
	 */
	public List<ViewExRoutingMatrix> filterServiceProviders(List<ViewExRoutingMatrix> routingMatrix) {

		if (routingMatrix == null || routingMatrix.isEmpty())
			return new ArrayList<ViewExRoutingMatrix>();

		List<ViewExRoutingMatrix> serviceProvidersMatrix = new ArrayList<>();

		for (ViewExRoutingMatrix matrix : routingMatrix) {
			if (matrix.getBankIndicator().trim().equalsIgnoreCase(PricerServiceConstants.SERVICE_PROVIDER_INDICATOR)) {
				serviceProvidersMatrix.add(matrix);
			}
		}

		return serviceProvidersMatrix;

	}

	public List<BigDecimal> getRoutingBankIds(List<ViewExRoutingMatrix> routingMatrix) {

		if (routingMatrix == null)
			return null;

		List<BigDecimal> routingBankIds = routingMatrix.stream()
				.filter(rm -> !rm.getBankIndicator().trim().equalsIgnoreCase("SB")).map(rm -> rm.getRoutingBankId())
				.distinct().collect(Collectors.toList());

		return routingBankIds;
	}

}
