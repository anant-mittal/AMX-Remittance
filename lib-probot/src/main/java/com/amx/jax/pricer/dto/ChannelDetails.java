package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.dict.UserClient.Channel;

public class ChannelDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	private Channel channel;
	
	private BigDecimal channelId;

	private BigDecimal discountId;

	private BigDecimal groupId;

	private BigDecimal discountTypeId;

	private String isActive;
	
	private BigDecimal discountPips;

	private BigDecimal minDiscountPips;

	private BigDecimal maxDiscountPips;

	public BigDecimal getChannelId() {
		return channelId;
	}

	public void setChannelId(BigDecimal channelId) {
		this.channelId = channelId;
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

	public BigDecimal getDiscountId() {
		return discountId;
	}

	public void setDiscountId(BigDecimal discountId) {
		this.discountId = discountId;
	}

	public BigDecimal getGroupId() {
		return groupId;
	}

	public void setGroupId(BigDecimal groupId) {
		this.groupId = groupId;
	}

	public BigDecimal getDiscountTypeId() {
		return discountTypeId;
	}

	public void setDiscountTypeId(BigDecimal discountTypeId) {
		this.discountTypeId = discountTypeId;
	}

	
}
