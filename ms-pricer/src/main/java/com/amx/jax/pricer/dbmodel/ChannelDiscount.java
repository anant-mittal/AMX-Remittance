package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.dict.UserClient.Channel;

@Entity
@Table(name = "JAX_PR_CHANNEL_DISCOUNT")
public class ChannelDiscount implements Serializable {

	private static final long serialVersionUID = 5102691255371845605L;

	@Id
	@GeneratedValue(generator = "channel_discount_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "channel_discount_seq", sequenceName = "JAX_PR_CHANNEL_DISCOUNT_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal id;

	@Column(name = "CHANNEL")
	private String channel;

	@Column(name = "DISCOUNT_PIPS")
	private BigDecimal discountPips;

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "INFO")
	private String info;

	@Column(name = "FLAGS")
	private BigDecimal flags;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "APPROVED_BY")
	private String approvedBy;

	@Column(name = "APPROVED_DATE")
	private Date approvedDate;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public Channel getChannel() {
		return Channel.valueOf(channel);
	}

	public void setChannel(Channel channel) {
		this.channel = channel.toString();
	}

	public BigDecimal getDiscountPips() {
		return discountPips;
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public BigDecimal getFlags() {
		return flags;
	}

	public void setFlags(BigDecimal flags) {
		this.flags = flags;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	
	
	
	

}
