package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;


@Entity
@Table(name = "JAX_PR_CUST_CAT_DISCOUNT")
public class CustomerCategoryDiscountModel implements Serializable,IResourceEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID")
	private BigDecimal id;
	
	@Column(name="CUSTOMER_CATEGORY")
	private String customerCatagory;
	
	
	@Column(name="ISACTIVE")
	private String isActive;
	
	@Column(name="DISCOUNT_PIPS")
	private BigDecimal discountPips;
	
	
	@Column(name="INFO")
	private String info;
	
	
	@Column(name="FLAGS")
	private BigDecimal flag;


	@Override
	public BigDecimal resourceId() {
		// TODO Auto-generated method stub
		return this.id;
	}


	@Override
	public String resourceName() {
		return this.customerCatagory;
	}


	@Override
	public String resourceCode() {
		return null;
	}


	public BigDecimal getId() {
		return id;
	}


	public void setId(BigDecimal id) {
		this.id = id;
	}


	public String getCustomerCatagory() {
		return customerCatagory;
	}


	public void setCustomerCatagory(String customerCatagory) {
		this.customerCatagory = customerCatagory;
	}


	public String getIsActive() {
		return isActive;
	}


	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


	public BigDecimal getDiscountPips() {
		return discountPips;
	}


	public void setDiscountPips(BigDecimal discountPips) {
		this.discountPips = discountPips;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public BigDecimal getFlag() {
		return flag;
	}


	public void setFlag(BigDecimal flag) {
		this.flag = flag;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
