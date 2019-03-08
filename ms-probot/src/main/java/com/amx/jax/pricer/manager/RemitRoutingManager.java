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
import com.amx.jax.cache.WorkingHoursData;
import com.amx.jax.pricer.dao.ViewExRoutingMatrixDao;
import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;
import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.DprRequestDto;
import com.amx.jax.pricer.dto.EstimatedDeliveryDetails;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.util.RoutingTransientDataComputationObject;
import com.amx.utils.DateUtil;
import com.amx.utils.JsonUtil;

@Component
public class RemitRoutingManager {

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

		List<RoutingTransientDataComputationObject> routingComputationObjects = new ArrayList<RoutingTransientDataComputationObject>();

		for (ViewExRoutingMatrix viewExRoutingMatrix : routingMatrix) {
			RoutingTransientDataComputationObject obj = new RoutingTransientDataComputationObject();
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

		EstimatedDeliveryDetails estimatedDeliveryDetails = new EstimatedDeliveryDetails();

		// Get An instantaneous point on the time-line for EPOCH TT
		Instant epochInstant = Instant.ofEpochMilli(startTT);

		// Get the appropriate Timezone
		ZoneId zoneId = ZoneId.of(timezone);

		// Compute the Correct Zone Date and Time of Block Delivery BEGIN
		ZonedDateTime beginZonedDT = ZonedDateTime.ofInstant(epochInstant, zoneId);

		if (!noHolidayLag && !computeRequestTransientDataCache.isHolidayListSetForCountry(countryId)) {
			List<HolidayListMasterModel> sortedHolidays = holidayListManager.getHoidaysForCountryAndDateRange(countryId,
					Date.from(beginZonedDT.toInstant()), Date.from(beginZonedDT.plusMonths(2).toInstant()));

			computeRequestTransientDataCache.setHolidaysForCountry(countryId, sortedHolidays);

		}

		WorkingHoursData workingHoursData = this.computeWorkMatrix(weekFrom, weekTo, weekHrsFrom, weekHrsTo,
				weekEndFrom, weekEndTo, weekEndHrsFrom, weekEndHrsTo, processTimeInHrs);

		workingHoursData.setProcessTimeInHrs(processTimeInHrs.doubleValue());

		ZonedDateTime goodBusinessDT = this.getGoodBusinessDateTime(beginZonedDT, workingHoursData, countryId,
				noHolidayLag);

		return null;
	}

	private WorkingHoursData computeWorkMatrix(BigDecimal weekFrom, BigDecimal weekTo, BigDecimal weekHrsFrom,
			BigDecimal weekHrsTo, BigDecimal weekEndFrom, BigDecimal weekEndTo, BigDecimal weekEndHrsFrom,
			BigDecimal weekEndHrsTo, BigDecimal processTimeInHrs) {

		WorkingHoursData workingHoursData = new WorkingHoursData();

		// Set Work Hours For the WeekDays.
		if (DateUtil.isValidDayOfWeek(weekFrom.intValue()) && DateUtil.isValidDayOfWeek(weekTo.intValue())) {
			workingHoursData.setWorkHrsThroughArabicDoW(weekFrom.intValue(), weekTo.intValue(), weekHrsFrom.intValue(),
					weekHrsTo.intValue());
		}

		// Set Work Hours For the WeekEnd.
		if (DateUtil.isValidDayOfWeek(weekEndFrom.intValue()) && DateUtil.isValidDayOfWeek(weekEndTo.intValue())) {
			workingHoursData.setWorkHrsThroughArabicDoW(weekEndFrom.intValue(), weekEndTo.intValue(),
					weekEndHrsFrom.intValue(), weekEndHrsTo.intValue());
		}

		return workingHoursData;
	}

	private ZonedDateTime getGoodBusinessDateTime(ZonedDateTime beginZonedDT, WorkingHoursData workHrsData,
			BigDecimal countryId, boolean noHolidayLag) {

		ZonedDateTime estimatedGoodBusinessDay = beginZonedDT;

		for (int i = 0; i < 100; i++) {
			/**
			 * Find out if the estimated Good Business Day is a real Good Business Day.
			 */

			// Check if holidays not applicable or its a holiday on this day.
			if (noHolidayLag || !computeRequestTransientDataCache.isHolidayOn(countryId, estimatedGoodBusinessDay)) {

				int dayOfWeek = estimatedGoodBusinessDay.getDayOfWeek().getValue();
				int hourOfDay = estimatedGoodBusinessDay.getHour();
				int minOfHr = estimatedGoodBusinessDay.getMinute();

				int hrMinIntVal = DateUtil.getHrMinIntVal(hourOfDay, minOfHr);

				// Current Date Time is Working
				if (workHrsData.isWorkingDayTime(dayOfWeek, hrMinIntVal)) {

					// No Holiday Lag or No Holiday on the Date

					return estimatedGoodBusinessDay;

				} else if (workHrsData.isBeforeWorkingHours(dayOfWeek, hrMinIntVal)) {
					int hrMinOffset = workHrsData.getWorkWindowTimeOffset(dayOfWeek, hrMinIntVal);
					return estimatedGoodBusinessDay.plusHours(workHrsData.extractHour(hrMinOffset))
							.plusMinutes(workHrsData.extractHour(hrMinOffset));
				}
			}

			/**
			 * If the estimated GBD is not working day - roll on to the next day.
			 */

			estimatedGoodBusinessDay = DateUtil.getNextZonedDay(estimatedGoodBusinessDay);

		}

		/*
		 * int beginDayIndex = beginZonedDT.getDayOfWeek().getValue();
		 * 
		 * boolean isWorking = workHrsData.isWorkingDayTime(beginDayIndex,
		 * DateUtil.getHrMinIntVal(beginZonedDT.getHour(), beginZonedDT.getMinute()));
		 */
		// ZonedDateTime dPlusOne =

		// System.out.println(" Date Now ==> " + beginZonedDT);

		// System.out.println(" Work Matrix ==> " + JsonUtil.toJson(workHrsData));

		// System.out.println(" Is Working ==> " + isWorking);

		return null;

	}

}
