package com.amx.jax.pricer.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;
import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.dto.BankDetailsDTO;
import com.amx.jax.pricer.dto.ExchangeRateDetails;

public class PricingRateDetailsDTO {

	private List<ExchangeRateDetails> sellRateDetails = new ArrayList<>();

	private Map<BigDecimal, BankDetailsDTO> bankDetails = new HashMap<>();

	private Map<BigDecimal, List<ViewExGLCBAL>> bankGlcBalMap = new HashMap<>();

	private Map<BigDecimal, BigDecimal> bankGLCBALAvgRateMap = new HashMap<BigDecimal, BigDecimal>();

	private OnlineMarginMarkup margin = null;

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

	public Map<String, Object> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Object> info) {
		this.info = info;
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

			/** hot fix  13/03/2019 **/
			if(glcBalList !=null && !glcBalList.isEmpty()) {
				if(glcBalList.size()>1) {
					for (ViewExGLCBAL glcbal : glcBalList) {
						sumRateCurBal = sumRateCurBal
								.add(null == glcbal.getRateCurBal() ? new BigDecimal(0) : glcbal.getRateCurBal());
						sumRateFcCurBal = sumRateFcCurBal
								.add(null == glcbal.getRateFcCurBal() ? new BigDecimal(0) : glcbal.getRateFcCurBal());


					}

					if (sumRateCurBal.doubleValue() != 0 || sumRateFcCurBal.doubleValue() != 0) {
						avgRate = sumRateCurBal.divide(sumRateFcCurBal, 10, RoundingMode.HALF_UP);
					}

				}else {
					for (ViewExGLCBAL glcbal : glcBalList) {
						avgRate = glcbal.getRateAvgRate();
					}
				}

			}

			/** original  code **/ 
			/*for (ViewExGLCBAL glcbal : glcBalList) {
				sumRateCurBal = sumRateCurBal
						.add(null == glcbal.getRateCurBal() ? new BigDecimal(0) : glcbal.getRateCurBal());
				sumRateFcCurBal = sumRateFcCurBal
						.add(null == glcbal.getRateFcCurBal() ? new BigDecimal(0) : glcbal.getRateFcCurBal());
				
			}

			
			if (sumRateCurBal.doubleValue() != 0 || sumRateFcCurBal.doubleValue() != 0) {
				avgRate = sumRateCurBal.divide(sumRateFcCurBal, 10, RoundingMode.HALF_UP);
			}
			 */
			
			this.bankGLCBALAvgRateMap.put(bankId, avgRate);

			return avgRate;

		} else {
			return this.bankGLCBALAvgRateMap.get(bankId);
		}

	}

}
