package com.amx.jax.client.task;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.amx.jax.model.customer.document.CustomerDocInfoDto;
import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerDocUploadNotificationTaskData {

	@NotNull
	@Size(min = 1)
	List<CustomerDocInfoDto> customerDocInfo;

	public CustomerDocUploadNotificationTaskData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerDocUploadNotificationTaskData(List<CustomerDocInfoDto> customerDocInfo, BigDecimal remittanceTransactionId) {
		super();
		this.customerDocInfo = customerDocInfo;
		this.remittanceTransactionId = remittanceTransactionId;
	}

	@NotNull
	@ApiMockModelProperty(example = "4228")
	BigDecimal remittanceTransactionId;

	public BigDecimal getRemittanceTransactionId() {
		return remittanceTransactionId;
	}

	public void setRemittanceTransactionId(BigDecimal remittanceTransactionId) {
		this.remittanceTransactionId = remittanceTransactionId;
	}

	public List<CustomerDocInfoDto> getCustomerDocInfo() {
		return customerDocInfo;
	}

	public void setCustomerDocInfo(List<CustomerDocInfoDto> customerDocInfo) {
		this.customerDocInfo = customerDocInfo;
	}

}
