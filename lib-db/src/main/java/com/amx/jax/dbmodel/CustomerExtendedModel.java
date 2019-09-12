package com.amx.jax.dbmodel;

/**
 @author rabil
 @date  05 mar 2019
**/
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FS_CUSTOMER_EXTENDED")
public class CustomerExtendedModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4943440026915307328L;

	@Id
	@Column(name = "CUSTOMER_ID")
	private BigDecimal customerId;

	@Column(name = "CUST_CAT_MASTER_ID")
	private BigDecimal custCatMasterId;

	@Column(name = "LAST_YEAR_NO_OF_TRN")
	private BigDecimal lastYearNoOfTrnx;

	@Column(name = "LAST_YEAR_REVENUE")
	private BigDecimal lastYearRevenue;

	@Column(name = "LAST_YEAR_TRN_VALUE")
	private BigDecimal lastYearTrnxValue;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getCustCatMasterId() {
		return custCatMasterId;
	}

	public void setCustCatMasterId(BigDecimal custCatMasterId) {
		this.custCatMasterId = custCatMasterId;
	}

	public BigDecimal getLastYearNoOfTrnx() {
		return lastYearNoOfTrnx;
	}

	public void setLastYearNoOfTrnx(BigDecimal lastYearNoOfTrnx) {
		this.lastYearNoOfTrnx = lastYearNoOfTrnx;
	}

	public BigDecimal getLastYearRevenue() {
		return lastYearRevenue;
	}

	public void setLastYearRevenue(BigDecimal lastYearRevenue) {
		this.lastYearRevenue = lastYearRevenue;
	}

	public BigDecimal getLastYearTrnxValue() {
		return lastYearTrnxValue;
	}

	public void setLastYearTrnxValue(BigDecimal lastYearTrnxValue) {
		this.lastYearTrnxValue = lastYearTrnxValue;
	}

}
