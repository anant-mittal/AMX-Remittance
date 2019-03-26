package com.amx.jax.dbmodel.fx;

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

/**
 * @author Prashant
 *
 */
@Entity
@Table(name = "EX_DELIVERY_REMARKS")
public class FxDeliveryRemark implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "EX_DELIVERY_REMARK_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "EX_DELIVERY_REMARK_SEQ", sequenceName = "EX_DELIVERY_REMARK_SEQ", allocationSize = 1)
	@Column(name = "EX_DELIVERY_REMARK_SEQ_ID", unique = true, nullable = false, precision = 22, scale = 0)
	BigDecimal deleviryRemarkSeqId;

	@Column(name = "LANGUAGE_ID")
	BigDecimal langaugeId;

	@Column(name = "DELIVERY_REMARKS")
	String deliveryRemark;

	@Column(name = "CREATED_BY")
	String createdBy;

	@Column(name = "CREATION_DATE")
	Date createdDate;

	@Column(name = "UPDATED_BY")
	String updatedBy;

	@Column(name = "UPDATED_DATE")
	Date updatedDate;

	@Column(name = "ISACTIVE")
	String isActive;

	public BigDecimal getDeleviryRemarkSeqId() {
		return deleviryRemarkSeqId;
	}

	public void setDeleviryRemarkSeqId(BigDecimal deleviryRemarkSeqId) {
		this.deleviryRemarkSeqId = deleviryRemarkSeqId;
	}

	public BigDecimal getLangaugeId() {
		return langaugeId;
	}

	public void setLangaugeId(BigDecimal langaugeId) {
		this.langaugeId = langaugeId;
	}

	public String getDeliveryRemark() {
		return deliveryRemark;
	}

	public void setDeliveryRemark(String deliveryRemark) {
		this.deliveryRemark = deliveryRemark;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


}
