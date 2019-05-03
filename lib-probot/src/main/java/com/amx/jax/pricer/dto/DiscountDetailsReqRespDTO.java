package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.util.List;

public class DiscountDetailsReqRespDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2594412775985894857L;

	private List<ChannelDetails> channelDetails;

	private List<CustomerCategoryDetails> customerCategoryDetails;

	private List<AmountSlabDetails> amountSlabDetails;

	public List<ChannelDetails> getChannelDetails() {
		return channelDetails;
	}

	public void setChannelDetails(List<ChannelDetails> channelDetails) {
		this.channelDetails = channelDetails;
	}

	public List<CustomerCategoryDetails> getCustomerCategoryDetails() {
		return customerCategoryDetails;
	}

	public void setCustomerCategoryDetails(List<CustomerCategoryDetails> customerCategoryDetails) {
		this.customerCategoryDetails = customerCategoryDetails;
	}

	public List<AmountSlabDetails> getAmountSlabDetails() {
		return amountSlabDetails;
	}

	public void setAmountSlabDetails(List<AmountSlabDetails> amountSlabDetails) {
		this.amountSlabDetails = amountSlabDetails;
	}

}
