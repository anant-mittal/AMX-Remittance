package com.amx.jax.model.response.customer;

import java.math.BigDecimal;

public class DocumentUploadMeta extends CustomerMgmtUIFieldMeta {

	BigDecimal customerDocTypeMasterId;

	public BigDecimal getCustomerDocTypeMasterId() {
		return customerDocTypeMasterId;
	}

	public void setCustomerDocTypeMasterId(BigDecimal customerDocTypeMasterId) {
		this.customerDocTypeMasterId = customerDocTypeMasterId;
	}

	
}
