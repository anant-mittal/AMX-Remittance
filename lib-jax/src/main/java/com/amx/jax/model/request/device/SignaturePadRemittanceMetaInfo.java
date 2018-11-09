package com.amx.jax.model.request.device;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.model.response.IDeviceStateData;

public class SignaturePadRemittanceMetaInfo implements IDeviceStateData {

	private static final long serialVersionUID = -5336542874871758699L;

	@NotNull
	private BigDecimal employeeId;

	@NotNull
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
