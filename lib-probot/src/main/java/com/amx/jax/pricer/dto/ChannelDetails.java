package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.dict.UserClient.Channel;

public class ChannelDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal channelId;

	private BigDecimal disountId;

	private BigDecimal groupId;

	private BigDecimal discountTypeId;

	private Channel channel;

	private BigDecimal discountPips;

	private String isActive;

	private BigDecimal minDiscountPips;

	private BigDecimal maxDiscountPips;

	public BigDecimal getId() {
		return channelId;
	}

	public void setId(BigDecimal id) {
		this.channelId = id;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public BigDecimal getDiscountPips() {
		return discountPips;
	}

	public String getDiscountPipsPT() {
		return discountPips == null ? null : discountPips.toPlainString();
	}

	public void setDiscountPips(BigDecimal discountPips) {
		this.discountPips = discountPips;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getMinDiscountPips() {
		return minDiscountPips;
	}

	public String getMinDiscountPipsPT() {
		return minDiscountPips == null ? null : minDiscountPips.toPlainString();
	}

	public void setMinDiscountPips(BigDecimal minDiscountPips) {
		this.minDiscountPips = minDiscountPips;
	}

	public BigDecimal getMaxDiscountPips() {
		return maxDiscountPips;
	}

	public String getMaxDiscountPipsPT() {
		return maxDiscountPips == null ? null : maxDiscountPips.toPlainString();
	}

	public void setMaxDiscountPips(BigDecimal maxDiscountPips) {
		this.maxDiscountPips = maxDiscountPips;
	}

}
