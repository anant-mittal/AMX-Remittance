package com.amx.jax.dbmodel.fx;

/**
 * @author :rabil
 * Date    :02/12/2018
 */
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="JAX_VW_FX_TRNX_LIMIT_CHECK")
public class FxOrderTranxLimitView implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CUSTOMER_ID")
	BigDecimal customerId;

	@Column(name = "TOTALAMOUNT")
	BigDecimal totalAmount;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}


	
}

