package com.amx.jax.cache;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.jax.pricer.dbmodel.CountryMasterModel;
import com.amx.jax.pricer.dbmodel.HolidayListMasterModel;
import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;
import com.amx.jax.pricer.dbmodel.TimezoneMasterModel;
import com.amx.jax.pricer.dbmodel.TreasuryFundTimeImpact;
import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.dbmodel.ViewExGLCBalProvisional;
import com.amx.jax.pricer.dto.BankDetailsDTO;
import com.amx.jax.pricer.dto.EstimatedDeliveryDetails;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;
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

	private CUSTOMER_CATEGORY customerCategory;

	// 1
	private List<ExchangeRateDetails> sellRateDetails = new ArrayList<>();

	private Map<BigDecimal, BankDetailsDTO> bankDetails;

	private Map<BigDecimal, BankGLCData> bankGlcBalMap;

	private List<TransientRoutingComputeDetails> routingMatrixData;

	private final Map<BigDecimal, Map<String, HolidayListMasterModel>> countryHolidays = new HashMap<BigDecimal, Map<String, HolidayListMasterModel>>();

	private Map<BigDecimal, OnlineMarginMarkup> bankMarginMap = new HashMap<BigDecimal, OnlineMarginMarkup>();

	private final Map<BigDecimal, TimezoneMasterModel> countryTimezones = new HashMap<BigDecimal, TimezoneMasterModel>();

	private final Map<BigDecimal, CountryMasterModel> countryMasterModels = new HashMap<BigDecimal, CountryMasterModel>();

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

	public CUSTOMER_CATEGORY getCustomerCategory() {
		return customerCategory;
	}

	public void setCustomerCategory(CUSTOMER_CATEGORY customerCategory) {
		this.customerCategory = customerCategory;
	}

	public Map<BigDecimal, BankGLCData> getBankGlcBalMap() {
		return bankGlcBalMap;
	}

	public void setBankGlcBalMap(Map<BigDecimal, BankGLCData> bankGlcBalMap) {
		this.bankGlcBalMap = bankGlcBalMap;
	}

	public OnlineMarginMarkup getMarginForBank(BigDecimal bankId) {
		/**
		 * Get margin for the Rate
		 */

		OnlineMarginMarkup margin = this.bankMarginMap.get(bankId);

		if (null == margin) {
			margin = new OnlineMarginMarkup();
			margin.setMarginMarkup(new BigDecimal(0));
		}

		return margin;
	}

	public void setMarginForBank(BigDecimal bankId, OnlineMarginMarkup margin) {
		this.bankMarginMap.put(bankId, margin);
	}

	public void setMargins(Map<BigDecimal, OnlineMarginMarkup> bankMargins) {
		if (bankMargins != null && !bankMargins.isEmpty()) {
			this.bankMarginMap = bankMargins;
		}
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

		if (this.bankGlcBalMap == null) {
			return null;
		}

		BankGLCData bankGLCData = this.bankGlcBalMap.get(bankId);

		if (null == bankGLCData || null == bankGLCData.getGlAccountsDetails()
				|| bankGLCData.getGlAccountsDetails().isEmpty()) {
			return null;
		}

		if (bankGLCData.getAvgLcRate() == null) {
			// Compute and Save Avg Rate

			List<ViewExGLCBAL> glcBalList = bankGLCData.getGlAccountsDetails();

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
					avgRate = glcBalList.get(0).getRateAvgRate();
				}

			}

			// this.bankGLCBALAvgRateMap.put(bankId, avgRate);
			bankGLCData.setAvgLcRate(avgRate);
			if (avgRate.compareTo(BigDecimal.ZERO) == 0) {
				bankGLCData.setAvgFcRate(BigDecimal.ZERO);
			} else {
				bankGLCData.setAvgFcRate(BigDecimal.ONE.divide(avgRate, 10, RoundingMode.HALF_DOWN));
			}
			return avgRate;

		} else {
			return bankGLCData.getAvgLcRate();
		}

	}

	public BigDecimal getMaxGLCBalForBank(BigDecimal bankId, boolean isFc) {

		BankGLCData bankGLCData = this.bankGlcBalMap.get(bankId);

		if (null == bankGLCData || null == bankGLCData.getGlAccountsDetails()
				|| bankGLCData.getGlAccountsDetails().isEmpty()) {
			return null;
		}

		if (bankGLCData.getMaxLcCurBalAmount() == null || bankGLCData.getMaxFcCurBalAmount() == null) {

			List<ViewExGLCBAL> glcBalList = bankGLCData.getGlAccountsDetails();

			BigDecimal rateLcCurBal = new BigDecimal(0);
			BigDecimal rateFcCurBal = new BigDecimal(0);

			if (glcBalList != null && !glcBalList.isEmpty()) {
				if (glcBalList.size() > 1) {
					for (ViewExGLCBAL glcbal : glcBalList) {
						BigDecimal lcBal = (null == glcbal.getRateCurBal() ? new BigDecimal(0)
								: glcbal.getRateCurBal());
						if (lcBal.compareTo(rateLcCurBal) > 0) {
							rateLcCurBal = lcBal;
						}

						BigDecimal fcBal = (null == glcbal.getRateFcCurBal() ? new BigDecimal(0)
								: glcbal.getRateFcCurBal());
						if (fcBal.compareTo(rateFcCurBal) > 0) {
							rateFcCurBal = fcBal;
						}

					}

				} else {
					rateLcCurBal = glcBalList.get(0).getRateCurBal();
					rateFcCurBal = glcBalList.get(0).getRateFcCurBal();
				}

				// Adjust the FC and LC Amount for the Pending Provisional Adjustments.
				ViewExGLCBalProvisional provision = bankGLCData.getProvisionalBalDetails();

				if (provision != null && provision.getRateCurBal() != null && provision.getRateFcCurBal() != null) {
					rateLcCurBal = rateLcCurBal.add(provision.getRateCurBal());
					rateFcCurBal = rateFcCurBal.add(provision.getRateFcCurBal());
				}

			}

			bankGLCData.setMaxLcCurBalAmount(rateLcCurBal);
			bankGLCData.setMaxFcCurBalAmount(rateFcCurBal);

		}

		if (isFc) {
			return bankGLCData.getMaxFcCurBalAmount();
		} else {
			return bankGLCData.getMaxLcCurBalAmount();
		}

	}

	public BigDecimal getAdjustedGLCBalForBank(BigDecimal bankId, boolean isFc) {

		BankGLCData bankGLCData = this.bankGlcBalMap.get(bankId);

		if (null == bankGLCData || null == bankGLCData.getGlAccountsDetails()
				|| bankGLCData.getGlAccountsDetails().isEmpty()) {
			return null;
		}

		if (bankGLCData.getAdjustedLcCurBal() == null || bankGLCData.getAdjustedFcCurBal() == null) {

			BigDecimal maxFcCurBal = getMaxGLCBalForBank(bankId, true);
			BigDecimal maxLcCurBal = getMaxGLCBalForBank(bankId, false);

			BigDecimal avgGlcCostRate = getAvgRateGLCForBank(bankId);

			BigDecimal adjustedFcCurBal, adjustedLcCurBal;

			if (bankGLCData.getFundingGlAcDetails() != null && !bankGLCData.getFundingGlAcDetails().isEmpty()) {
				BigDecimal fundingLcBal = BigDecimal.ZERO;

				for (ViewExGLCBAL fundingBal : bankGLCData.getFundingGlAcDetails()) {
					if (fundingBal != null && fundingBal.getRateCurBal() != null) {
						fundingLcBal = fundingLcBal.add(fundingBal.getRateCurBal());
					}
				}

				adjustedLcCurBal = fundingLcBal.add(maxLcCurBal);
				adjustedFcCurBal = adjustedLcCurBal.divide(avgGlcCostRate, 3, RoundingMode.HALF_DOWN);

				// adjustedFcCurBal = maxFcCurBal.add( fundingLcBal.divide(avgGlcCostRate, 3,
				// RoundingMode.HALF_DOWN));

			} else {
				adjustedFcCurBal = maxFcCurBal;
				adjustedLcCurBal = maxLcCurBal;
			}

			bankGLCData.setAdjustedLcCurBal(adjustedLcCurBal);
			bankGLCData.setAdjustedFcCurBal(adjustedFcCurBal);

			// System.out.println("\\n From ExchRateAndRouting Cache");
			// System.out.println(" Max Lc Bal ==> " + maxLcCurBal);
			// System.out.println(" Max Fc Bal ==> " + maxFcCurBal);

			// System.out.println(" Adjusted Lc Bal ==> " +
			// bankGLCData.getAdjustedLcCurBal());
			// System.out.println(" Adjusted Fc Bal ==> " +
			// bankGLCData.getAdjustedFcCurBal());
			// System.out.println(" Exchange Rate ====> " + avgGlcCostRate);
		}

		if (isFc) {
			return bankGLCData.getAdjustedFcCurBal();
		} else {
			return bankGLCData.getAdjustedLcCurBal();
		}

	}

	public TreasuryFundTimeImpact getTreasuryFundTimeImpact(BigDecimal bankId, boolean isFunded) {
		BankGLCData bankGLCData = this.bankGlcBalMap.get(bankId);

		if (null == bankGLCData || null == bankGLCData.getGlAccountsDetails()
				|| bankGLCData.getGlAccountsDetails().isEmpty()) {
			return null;
		}

		if (isFunded) {
			return bankGLCData.getFundedTimeImpact();
		} else {
			return bankGLCData.getOutOfFundTimeImpact();
		}
	}

	public EstimatedDeliveryDetails getTreasuryFTDelay(BigDecimal bankId) {
		BankGLCData bankGLCData = this.bankGlcBalMap.get(bankId);

		if (null != bankGLCData) {
			return bankGLCData.getEstmdFundTimeDelay();
		}

		return null;
	}

	public void setTreasuryFTDelay(BigDecimal bankId, EstimatedDeliveryDetails treasuryFTDelay) {
		BankGLCData bankGLCData = this.bankGlcBalMap.get(bankId);

		if (null != bankGLCData) {
			bankGLCData.setEstmdFundTimeDelay(treasuryFTDelay);
		}
	}

	public void setTimezoneForCountry(BigDecimal countryId, TimezoneMasterModel tz) {
		this.countryTimezones.put(countryId, tz);
	}

	public TimezoneMasterModel getTimezoneForCountry(BigDecimal countryId) {
		return this.countryTimezones.get(countryId);
	}

	public CountryMasterModel getCountryById(BigDecimal countryId) {
		return this.countryMasterModels.get(countryId);
	}

	public void setCountry(CountryMasterModel countryMasterModel) {

		if (null == countryMasterModel || countryMasterModel.getCountryId() == null)
			return;

		this.countryMasterModels.put(countryMasterModel.getCountryId(), countryMasterModel);
	}

}
