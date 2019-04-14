package com.amx.jax.cache;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;
import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;
import com.amx.jax.pricer.dbmodel.TimezoneMasterModel;
import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.dto.BankDetailsDTO;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_GROUP;
import com.amx.utils.DateUtil;

/**
 * This is a transient data cache for the Exchange Rate and Routing Requests.
 * This is injected as a bean to every Exchange Rate and Routing Request for
 * Data computation and caching - for the purpose of avoiding multiple and/or
 * repeated data computations and fetching from the db.
 * 
 * The cache is to be shared by the multiple data and compute managers.
 * 
 * @author abhijeet
 *
 */
public class ExchRateAndRoutingTransientDataCache {

	private final long trnxBeginTime = System.currentTimeMillis();

	private SERVICE_GROUP serviceGroup = SERVICE_GROUP.BANK;

	private List<ExchangeRateDetails> sellRateDetails = new ArrayList<>();

	private Map<BigDecimal, BankDetailsDTO> bankDetails = new HashMap<>();

	private Map<BigDecimal, List<ViewExGLCBAL>> bankGlcBalMap = new HashMap<>();

	private Map<BigDecimal, BigDecimal> bankGLCBALAvgRateMap = new HashMap<BigDecimal, BigDecimal>();

	private List<TransientRoutingComputeDetails> routingMatrixData;

	private Map<BigDecimal, Map<String, HolidayListMasterModel>> countryHolidays = new HashMap<BigDecimal, Map<String, HolidayListMasterModel>>();

	private OnlineMarginMarkup margin = null;

	private final Map<BigDecimal, TimezoneMasterModel> countryTimezones = new HashMap<BigDecimal, TimezoneMasterModel>();

	private Map<String, Object> info = new HashMap<String, Object>();

	public long getTrnxBeginTime() {
		return trnxBeginTime;
	}

	public SERVICE_GROUP getServiceGroup() {
		return serviceGroup;
	}

	public void setServiceGroup(SERVICE_GROUP serviceGroup) {
		this.serviceGroup = serviceGroup;
	}

	public Map<BigDecimal, List<ViewExGLCBAL>> getBankGlcBalMap() {
		return bankGlcBalMap;
	}

	public void setBankGlcBalMap(Map<BigDecimal, List<ViewExGLCBAL>> bankGlcBalMap) {
		this.bankGlcBalMap = bankGlcBalMap;
	}

	public OnlineMarginMarkup getMargin() {
		return margin;
	}

	public void setMargin(OnlineMarginMarkup margin) {
		this.margin = margin;
	}

	public List<ExchangeRateDetails> getSellRateDetails() {
		return sellRateDetails;
	}

	public void setSellRateDetails(List<ExchangeRateDetails> sellRateDetails) {
		this.sellRateDetails = sellRateDetails;
	}

	public Map<BigDecimal, BankDetailsDTO> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(Map<BigDecimal, BankDetailsDTO> bankDetails) {
		this.bankDetails = bankDetails;
	}

	public List<TransientRoutingComputeDetails> getRoutingMatrixData() {
		return routingMatrixData;
	}

	public void setRoutingMatrixData(List<TransientRoutingComputeDetails> routingMatrix) {
		this.routingMatrixData = routingMatrix;
	}

	public Map<String, Object> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}

	public boolean isHolidayListSetForCountry(BigDecimal countryId) {
		return countryHolidays.containsKey(countryId);
	}

	public List<HolidayListMasterModel> getHolidaysForCountryId(BigDecimal countryId) {
		Map<String, HolidayListMasterModel> holidayMap = countryHolidays.get(countryId);

		if (null != holidayMap) {
			return Collections.list(Collections.enumeration(holidayMap.values()));
		}

		return null;
	}

	public void setHolidaysForCountry(BigDecimal countryId, List<HolidayListMasterModel> holidayList) {

		if (null == holidayList || holidayList.isEmpty()) {
			countryHolidays.put(countryId, new HashMap<String, HolidayListMasterModel>());
			return;
		}

		Map<String, HolidayListMasterModel> dateHolidayMap = new HashMap<String, HolidayListMasterModel>();

		for (HolidayListMasterModel holiday : holidayList) {

			if (holiday.getEventDate() != null) {
				dateHolidayMap.put(DateUtil.formatDate(holiday.getEventDate()), holiday);
			}

		}

		countryHolidays.put(countryId, dateHolidayMap);

	}

	public boolean isHolidayOn(BigDecimal countryId, ZonedDateTime date) {

		if (countryHolidays.containsKey(countryId) && (null != countryHolidays.get(countryId))) {
			return countryHolidays.get(countryId).containsKey(DateUtil.formatDateTime(date));
		}

		return false;
	}

	public HolidayListMasterModel getHolidayInfoOn(BigDecimal countryId, ZonedDateTime date) {
		if (isHolidayOn(countryId, date)) {
			return countryHolidays.get(countryId).get(DateUtil.formatDateTime(date));
		}
		return null;
	}

	/**
	 * Get Average GLCBAL Rate
	 * 
	 * @param bankId
	 * @return
	 */
	public BigDecimal getAvgRateGLCForBank(BigDecimal bankId) {

		if (!this.bankGLCBALAvgRateMap.containsKey(bankId)) {
			// Compute and Save Avg Rate

			if (!this.bankGlcBalMap.containsKey(bankId)) {
				return null;
			}

			List<ViewExGLCBAL> glcBalList = this.bankGlcBalMap.get(bankId);

			BigDecimal sumRateCurBal = new BigDecimal(0);
			BigDecimal sumRateFcCurBal = new BigDecimal(0);
			BigDecimal avgRate = new BigDecimal(0);

			/** hot fix 13/03/2019 **/
			if (glcBalList != null && !glcBalList.isEmpty()) {
				if (glcBalList.size() > 1) {
					for (ViewExGLCBAL glcbal : glcBalList) {
						sumRateCurBal = sumRateCurBal
								.add(null == glcbal.getRateCurBal() ? new BigDecimal(0) : glcbal.getRateCurBal());
						sumRateFcCurBal = sumRateFcCurBal
								.add(null == glcbal.getRateFcCurBal() ? new BigDecimal(0) : glcbal.getRateFcCurBal());

					}

					if (sumRateCurBal.doubleValue() != 0 || sumRateFcCurBal.doubleValue() != 0) {
						avgRate = sumRateCurBal.divide(sumRateFcCurBal, 10, RoundingMode.HALF_UP);
					}

				} else {
					for (ViewExGLCBAL glcbal : glcBalList) {
						avgRate = glcbal.getRateAvgRate();
					}
				}

			}

			/** original code **/
			/*
			 * for (ViewExGLCBAL glcbal : glcBalList) { sumRateCurBal = sumRateCurBal
			 * .add(null == glcbal.getRateCurBal() ? new BigDecimal(0) :
			 * glcbal.getRateCurBal()); sumRateFcCurBal = sumRateFcCurBal .add(null ==
			 * glcbal.getRateFcCurBal() ? new BigDecimal(0) : glcbal.getRateFcCurBal()); }
			 * 
			 * 
			 * if (sumRateCurBal.doubleValue() != 0 || sumRateFcCurBal.doubleValue() != 0) {
			 * avgRate = sumRateCurBal.divide(sumRateFcCurBal, 10, RoundingMode.HALF_UP); }
			 */

			this.bankGLCBALAvgRateMap.put(bankId, avgRate);

			return avgRate;

		} else {
			return this.bankGLCBALAvgRateMap.get(bankId);
		}

	}

	public void setTimezoneForCountry(BigDecimal countryId, TimezoneMasterModel tz) {
		this.countryTimezones.put(countryId, tz);
	}

	public TimezoneMasterModel getTimezoneForCountry(BigDecimal countryId) {
		return this.countryTimezones.get(countryId);
	}

}
