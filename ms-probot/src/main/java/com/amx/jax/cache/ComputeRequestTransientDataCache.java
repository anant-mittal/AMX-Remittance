package com.amx.jax.cache;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;
import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;
import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.dto.BankDetailsDTO;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.util.RoutingTransientDataComputationObject;

public class ComputeRequestTransientDataCache {

	private List<ExchangeRateDetails> sellRateDetails;

	private Map<BigDecimal, BankDetailsDTO> bankDetails;

	private Map<BigDecimal, List<ViewExGLCBAL>> bankGlcBalMap;

	private Map<BigDecimal, BigDecimal> bankGLCBALAvgRateMap = new HashMap<BigDecimal, BigDecimal>();

	private OnlineMarginMarkup margin;

	private List<RoutingTransientDataComputationObject> routingMatrixData;

	private Map<BigDecimal, List<HolidayListMasterModel>> countryHolidays = new HashMap<BigDecimal, List<HolidayListMasterModel>>();

	private Map<String, Object> info = new HashMap<String, Object>();

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

	public List<RoutingTransientDataComputationObject> getRoutingMatrix() {
		return routingMatrixData;
	}

	public void setRoutingMatrix(List<RoutingTransientDataComputationObject> routingMatrix) {
		this.routingMatrixData = routingMatrix;
	}

	public Map<String, Object> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}

	public List<RoutingTransientDataComputationObject> getRoutingMatrixData() {
		return routingMatrixData;
	}

	public void setRoutingMatrixData(List<RoutingTransientDataComputationObject> routingMatrixData) {
		this.routingMatrixData = routingMatrixData;
	}

	public Map<BigDecimal, List<HolidayListMasterModel>> getCountryHolidays() {
		return countryHolidays;
	}

	public List<HolidayListMasterModel> getHolidaysForCountryId(BigDecimal countryId) {
		return countryHolidays.get(countryId);
	}

	public void setHolidaysForCountry(BigDecimal countryId, List<HolidayListMasterModel> holidayList) {
		countryHolidays.put(countryId, holidayList);
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

			for (ViewExGLCBAL glcbal : glcBalList) {
				sumRateCurBal = sumRateCurBal
						.add(null == glcbal.getRateCurBal() ? new BigDecimal(0) : glcbal.getRateCurBal());
				sumRateFcCurBal = sumRateFcCurBal
						.add(null == glcbal.getRateFcCurBal() ? new BigDecimal(0) : glcbal.getRateFcCurBal());
			}

			BigDecimal avgRate = new BigDecimal(0);

			if (sumRateCurBal.doubleValue() != 0 || sumRateFcCurBal.doubleValue() != 0) {
				avgRate = sumRateCurBal.divide(sumRateFcCurBal, 10, RoundingMode.HALF_UP);
			}

			this.bankGLCBALAvgRateMap.put(bankId, avgRate);

			return avgRate;

		} else {
			return this.bankGLCBALAvgRateMap.get(bankId);
		}

	}

}
