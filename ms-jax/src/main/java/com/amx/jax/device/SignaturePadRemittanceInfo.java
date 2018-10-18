package com.amx.jax.device;

import java.math.BigDecimal;

import com.amx.jax.model.response.IDeviceStateData;

public class SignaturePadRemittanceInfo implements IDeviceStateData {

	private BigDecimal employeeId;

	private BigDecimal remittanceTransactionId;

	public BigDecimal getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

	public BigDecimal getRemittanceTransactionId() {
		return remittanceTransactionId;
	}

	public void setRemittanceTransactionId(BigDecimal remittanceTransactionId) {
		this.remittanceTransactionId = remittanceTransactionId;
	}
}
