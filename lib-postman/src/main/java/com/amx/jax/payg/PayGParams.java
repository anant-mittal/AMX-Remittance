package com.amx.jax.payg;

import com.amx.jax.dict.Channel;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.Tenant;
import com.amx.utils.ArgUtil;

public class PayGParams {
	String docId = null;
	String docNo = null;
	String docFy = null;

	String amount = null;
	String trackId = null;
	String redirectUrl = null;
	String payId = null;
	Tenant tenant = null;
	Channel channel = null;
	PayGServiceCode serviceCode = null;
	Object product = null;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(Object amount) {
		this.amount = ArgUtil.parseAsString(amount);
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(Object trackId) {
		this.trackId = ArgUtil.parseAsString(trackId);
	}

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = ArgUtil.parseAsString(docNo);
	}

	/**
	 * @return the tenant
	 */
	public Tenant getTenant() {
		return tenant;
	}

	/**
	 * @param tenant the tenant to set
	 */
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Object getProduct() {
		return product;
	}

	public void setProduct(Object product) {
		this.product = product;
	}

	public PayGServiceCode getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(PayGServiceCode serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = ArgUtil.parseAsString(docId);
	}

	public String getDocFy() {
		return docFy;
	}

	public void setDocFy(Object docFy) {
		this.docFy = ArgUtil.parseAsString(docFy);
	}

}
