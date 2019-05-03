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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "JAX_VW_FX_DELIVERY_DETAIL")
public class VwFxDeliveryDetailsModel implements Serializable {

	/**
	 * 
	 */
	static final long serialVersionUID = -6931598240921298822L;

	@Id
	@Column(name = "DELIVERY_DET_SEQ_ID")
	BigDecimal deleviryDelSeqId;

	@Column(name = "COLLECTION_DOCUMENT_NO")
	BigDecimal collectionDocNo;

	@Column(name = "COLLECTION_DOC_FINANCE_YEAR")
	BigDecimal collectionDocFinYear;

	@Column(name = "CUSTOMER_NAME")
	String customerName;

	@Column(name = "CUSTOMER_ID")
	BigDecimal customerId;

	@Column(name = "DELIVERY_DATE")
	@Temporal(TemporalType.DATE)
	Date deliveryDate;

	@Column(name = "EX_DELIVERY_REMARK_SEQ_ID")
	BigDecimal deliveryRemarkId;

	@Column(name = "DELIVERY_TIME")
	String deliveryTimeSlot;

	@Column(name = "DRIVER_EMPLOYEE_ID")
	BigDecimal driverEmployeeId;

	@Column(name = "SHIPPING_ADDRESS_ID")
	BigDecimal shippingAddressId;

	@Column(name = "ORDER_STATUS")
	String orderStatus;

	@Column(name = "INVENTORY_ID")
	String inventoryId;

	@Column(name = "OTP_TOKEN_PREFIX")
	String otpTokenPrefix;
	
	//Added By Radhika
	@Column(name = "TO_BRANCH_ID")
	BigDecimal toBranchId;
	
	@Column(name = "FROM_BRANCH_ID")
	BigDecimal fromBranchId;
	
	@Column(name = "ISACTIVE")
	String isActive;

	public String getOtpTokenPrefix() {
		return otpTokenPrefix;
	}

	public void setOtpTokenPrefix(String otpTokenPrefix) {
		this.otpTokenPrefix = otpTokenPrefix;
	}

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public BigDecimal getDeleviryDelSeqId() {
		return deleviryDelSeqId;
	}

	public void setDeleviryDelSeqId(BigDecimal deleviryDelSeqId) {
		this.deleviryDelSeqId = deleviryDelSeqId;
	}

	public BigDecimal getCollectionDocNo() {
		return collectionDocNo;
	}

	public void setCollectionDocNo(BigDecimal collectionDocNo) {
		this.collectionDocNo = collectionDocNo;
	}

	public BigDecimal getCollectionDocFinYear() {
		return collectionDocFinYear;
	}

	public void setCollectionDocFinYear(BigDecimal collectionDocFinYear) {
		this.collectionDocFinYear = collectionDocFinYear;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryTimeSlot() {
		return deliveryTimeSlot;
	}

	public void setDeliveryTimeSlot(String deliveryTimeSlot) {
		this.deliveryTimeSlot = deliveryTimeSlot;
	}

	public BigDecimal getDriverEmployeeId() {
		return driverEmployeeId;
	}

	public void setDriverEmployeeId(BigDecimal driverEmployeeId) {
		this.driverEmployeeId = driverEmployeeId;
	}

	public BigDecimal getShippingAddressId() {
		return shippingAddressId;
	}

	public void setShippingAddressId(BigDecimal shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}

	public BigDecimal getDeliveryRemarkId() {
		return deliveryRemarkId;
	}

	public void setDeliveryRemarkId(BigDecimal deliveryRemarkId) {
		this.deliveryRemarkId = deliveryRemarkId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getToBranchId() {
		return toBranchId;
	}

	public void setToBranchId(BigDecimal toBranchId) {
		this.toBranchId = toBranchId;
	}

	public BigDecimal getFromBranchId() {
		return fromBranchId;
	}

	public void setFromBranchId(BigDecimal fromBranchId) {
		this.fromBranchId = fromBranchId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	
	
	

}
