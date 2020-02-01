package com.amx.jax.dbmodel;

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

@Entity
@Table(name = "JAX_CUSTOMER_CART")
public class CustomerCartMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal cartId;
	private BigDecimal customerId;
	private String applIds;
	private BigDecimal linkCount;
	private Date createdDate;
	private Date modifiedDate;

	@Id
	@GeneratedValue(generator = "JAX_CUSTOMER_CART_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "JAX_CUSTOMER_CART_SEQ", sequenceName = "JAX_CUSTOMER_CART_SEQ", allocationSize = 1)
	@Column(name = "CART_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getCartId() {
		return cartId;
	}

	public void setCartId(BigDecimal cartId) {
		this.cartId = cartId;
	}

	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Column(name = "APPLICATION_IDS")
	public String getApplIds() {
		return applIds;
	}

	public void setApplIds(String applIds) {
		this.applIds = applIds;
	}

	@Column(name = "LINK_COUNT")
	public BigDecimal getLinkCount() {
		return linkCount;
	}

	public void setLinkCount(BigDecimal linkCount) {
		this.linkCount = linkCount;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
}
