package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.cache.ComputeRequestTransientDataCache;
import com.amx.jax.cache.TransientRoutingComputeDetails;
import com.amx.jax.cache.WorkingHoursData;
import com.amx.jax.pricer.dao.ViewExRoutingMatrixDao;
import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;
import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.DprRequestDto;
import com.amx.jax.pricer.dto.EstimatedDeliveryDetails;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.utils.DateUtil;
import com.amx.utils.JsonUtil;

@Component
public class RemitRoutingManager {

	private static final int MAX_DELIVERY_ATTEMPT_DAYS = 60;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RemitRoutingManager.class);

	@Autowired
	ViewExRoutingMatrixDao viewExRoutingMatrixDao;

	@Autowired
	HolidayListManager holidayListManager;

	@Resource
	ComputeRequestTransientDataCache computeRequestTransientDataCache;

	public List<ViewExRoutingMatrix> getRoutingMatrixForRemittance(DprRequestDto dprRequestDto) {

		// StopWatch watch = new StopWatch();
		// watch.start();

		List<ViewExRoutingMatrix> routingMatrix = viewExRoutingMatrixDao.getRoutingMatrix(
				dprRequestDto.getLocalCountryId(), dprRequestDto.getForeignCountryId(),
				dprRequestDto.getBeneficiaryBankId(), dprRequestDto.getBeneficiaryBranchId(),
				dprRequestDto.getForeignCurrencyId(), dprRequestDto.getServiceGroup().getGroupCode());

		// watch.stop();
		// System.out.println(" Time taken ==> " + (watch.getLastTaskTimeMillis() ));

		System.out.println(" Routing matrix ==>  " + JsonUtil.toJson(routingMatrix));

		if (null == routingMatrix || routingMatrix.isEmpty()) {

			LOGGER.info("Routing Matrix is Data is Empty or Null for the Pricing/Routing Request");

			throw new PricerServiceException(PricerServiceError.INVALID_ROUTING_BANK_IDS,
					"Invalid Routing Bank Ids : None Found matching with the Requested Ids: "
							+ dprRequestDto.getRoutingBankIds());
		}

		List<TransientRoutingComputeDetails> routingComputationObjects = new ArrayList<TransientRoutingComputeDetails>();

		for (ViewExRoutingMatrix viewExRoutingMatrix : routingMatrix) {
			TransientRoutingComputeDetails obj = new TransientRoutingComputeDetails();
			obj.setViewExRoutingMatrix(viewExRoutingMatrix);
			routingComputationObjects.add(obj);
		}

		computeRequestTransientDataCache.setRoutingMatrix(routingComputationObjects);

		return routingMatrix;

	}

	public EstimatedDeliveryDetails getEstimatedBlockDelivery(long startTT, String timezone, BigDecimal weekFrom,
			BigDecimal weekTo, BigDecimal weekHrsFrom, BigDecimal weekHrsTo, BigDecimal weekEndFrom,
			BigDecimal weekEndTo, BigDecimal weekEndHrsFrom, BigDecimal weekEndHrsTo, BigDecimal processTimeInHrs,
			boolean noHolidayLag, BigDecimal countryId) {

		// Get An instantaneous point on the time-line for EPOCH TT
		Instant epochInstant = Instant.ofEpochMilli(startTT);

		// Get the appropriate Timezone
		ZoneId zoneId = ZoneId.of(timezone);

		// Compute the Correct Zone Date and Time of Block Delivery BEGIN
		ZonedDateTime beginZonedDT = ZonedDateTime.ofInstant(epochInstant, zoneId);

		if (!noHolidayLag && !computeRequestTransientDataCache.isHolidayListSetForCountry(countryId)) {
			List<HolidayListMasterModel> sortedHolidays = holidayListManager.getHoidaysForCountryAndDateRange(countryId,
					Date.from(beginZonedDT.toInstant()), Date.from(beginZonedDT.plusDays(MAX_DELIVERY_ATTEMPT_DAYS).toInstant()));

			computeRequestTransientDataCache.setHolidaysForCountry(countryId, sortedHolidays);

		}

		WorkingHoursData workingHoursData = this.computeWorkMatrix(weekFrom, weekTo, weekHrsFrom, weekHrsTo,
				weekEndFrom, weekEndTo, weekEndHrsFrom, weekEndHrsTo, processTimeInHrs);

		EstimatedDeliveryDetails goodBusinessDeliveryDT = this.getGoodBusinessDateTime(beginZonedDT, workingHoursData,
				countryId, noHolidayLag);

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

		System.out.println(" Estimated Delivery Details ===> " + JsonUtil.toJson(goodBusinessDeliveryDT));

		return null;
	}

	private WorkingHoursData computeWorkMatrix(BigDecimal weekFrom, BigDecimal weekTo, BigDecimal weekHrsFrom,
			BigDecimal weekHrsTo, BigDecimal weekEndFrom, BigDecimal weekEndTo, BigDecimal weekEndHrsFrom,
			BigDecimal weekEndHrsTo, BigDecimal processTimeInHrs) {

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

		System.out.println(" Work Week matrix ==> " + JsonUtil.toJson(workingHoursData));

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
			BigDecimal countryId, boolean noHolidayLag) {

		EstimatedDeliveryDetails estimatedDeliveryDetails = new EstimatedDeliveryDetails();

		ZonedDateTime estimatedGoodBusinessDay = beginZonedDT;

		for (int i = 0; i < MAX_DELIVERY_ATTEMPT_DAYS; i++) {
			/**
			 * Find out if the estimated Good Business Day is a real Good Business Day.
			 */

			// Check if holidays not applicable or its not a holiday on this day.
			boolean isHoliday = false;

			// Default Working Status
			boolean isWorking = true;

			if (noHolidayLag || !(isHoliday = computeRequestTransientDataCache.isHolidayOn(countryId,
					estimatedGoodBusinessDay))) {

				int dayOfWeek = estimatedGoodBusinessDay.getDayOfWeek().getValue();
				int hourOfDay = estimatedGoodBusinessDay.getHour();
				int minOfHr = estimatedGoodBusinessDay.getMinute();

				int hrMinIntVal = DateUtil.getHrMinIntVal(hourOfDay, minOfHr);

				// Current Date Time is Working
				if (workHrsData.isWorkingDayTime(dayOfWeek, hrMinIntVal)) {

					// No Holiday Lag or No Holiday on the Date

					estimatedDeliveryDetails.setStartDateForeign(estimatedGoodBusinessDay);
					return estimatedDeliveryDetails;

				} else if (workHrsData.isBeforeWorkingHours(dayOfWeek, hrMinIntVal)) {
					int hrMinOffset = workHrsData.getWorkWindowTimeOffset(dayOfWeek, hrMinIntVal);
					if (hrMinOffset >= 0) {

						int offsetHr = workHrsData.extractHour(hrMinOffset);
						int offsetMin = workHrsData.extractMinute(hrMinOffset);
						long offsetSecs = 60 * 60 * offsetHr + 60 * offsetMin;

						estimatedGoodBusinessDay = estimatedGoodBusinessDay.plusHours(offsetHr).plusMinutes(offsetMin);

						estimatedDeliveryDetails.addToProcessTimeOperational(offsetSecs);
						estimatedDeliveryDetails.setStartDateForeign(estimatedGoodBusinessDay);

						return estimatedDeliveryDetails;

					}
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

			}

			estimatedGoodBusinessDay = nextEstimatedGoodBusinessDay;

		} // for(int i...

		// Return Default - Estimated Good Business : D+100

		estimatedDeliveryDetails.setStartDateForeign(estimatedGoodBusinessDay);
		estimatedDeliveryDetails.setCrossedMaxDeliveryDays(true);
		return estimatedDeliveryDetails;

	}

}
