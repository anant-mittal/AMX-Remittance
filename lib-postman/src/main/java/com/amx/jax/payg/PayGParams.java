package com.amx.jax.payg;

import java.io.Serializable;

import com.amx.jax.dict.Channel;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.Tenant;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayGParams implements Serializable {

	private static final long serialVersionUID = 3434309644048921758L;

	private String uuid = null;
	private String verification = null;

	// Identifiers
	private String docId = null;
	private String docNo = null;
	private String docFy = null;
	private String trackId = null;
	private String payId = null;

	// Money
	private String amount = null;

	// Meta
	private Tenant tenant = null;
	private Channel channel = null;
	private PayGServiceCode serviceCode = null;
	private Object product = null;

	// Processing
	private String redirectUrl = null;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@JsonIgnoreProperties
	public void setAmountObject(Object amount) {
		this.amount = ArgUtil.parseAsString(amount);
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	@JsonIgnoreProperties
	public void setTrackIdObject(Object trackId) {
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

	public void setDocFy(String docFy) {
		this.docFy = docFy;
	}

	@JsonIgnoreProperties
	public void setDocFyObject(Object docFy) {
		this.docFy = ArgUtil.parseAsString(docFy);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

}
