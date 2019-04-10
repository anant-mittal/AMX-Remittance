package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

//@Embeddable
//@Table(name = "EX_EXCHANGE_RATE_MASTER_APRDET")
public class ExchangeRateAPRDET implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8135844760604861076L;

	private String isActive;
	private BigDecimal sellRateMin;
	private BigDecimal sellRateMax;
	private BigDecimal serviceId;
	private BankMasterModel bankMaster;
	
	private boolean isGLCRate = false;

	public ExchangeRateAPRDET() {
		super();
	}

	public ExchangeRateAPRDET(String isActive, BigDecimal sellRateMin, BigDecimal sellRateMax, BigDecimal serviceId,
			BankMasterModel bankMaster) {
		super();
		this.isActive = isActive;
		this.sellRateMin = sellRateMin;
		this.sellRateMax = sellRateMax;
		this.serviceId = serviceId;
		this.bankMaster = bankMaster;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getSellRateMin() {
		return sellRateMin;
	}

	public void setSellRateMin(BigDecimal sellRateMin) {
		this.sellRateMin = sellRateMin;
	}

	public BigDecimal getSellRateMax() {
		return sellRateMax;
	}

	public void setSellRateMax(BigDecimal sellRateMax) {
		this.sellRateMax = sellRateMax;
	}

	public BigDecimal getServiceId() {
		return serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	public BankMasterModel getBankMaster() {
		return bankMaster;
	}

	public void setBankMaster(BankMasterModel bankMaster) {
		this.bankMaster = bankMaster;
	}

	public boolean isGLCRate() {
		return isGLCRate;
	}

	public void setGLCRate(boolean isGLCRate) {
		this.isGLCRate = isGLCRate;
	}

	@Override
	public String toString() {
		return "ExchangeRateAPRDET [isActive=" + isActive + ", sellRateMin=" + sellRateMin + ", sellRateMax="
				+ sellRateMax + ", serviceId=" + serviceId + ", bankId=" + bankMaster.getBankId() + ", bankCode="
				+ bankMaster.getBankCode() + "]";
	}

}
