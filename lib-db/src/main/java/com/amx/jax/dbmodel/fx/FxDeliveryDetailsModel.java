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
@Table(name="EX_DELIVERY_DETAILS")
public class FxDeliveryDetailsModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6931598240921298822L;


	@Id
	@GeneratedValue(generator = "EX_DELIVERY_DETAILS_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "EX_DELIVERY_DETAILS_SEQ", sequenceName = "EX_DELIVERY_DETAILS_SEQ", allocationSize = 1)
	@Column(name = "DELIVERY_DET_SEQ_ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal deleviryDelSeqId;
	
	@Column(name="SHIPPING_ADDRESS_ID")
	private BigDecimal shippingAddressId;
	
	@Column(name="DELIVERY_DATE")
	@Temporal(TemporalType.DATE)
	private Date deliveryDate;
	
	@Column(name="DELIVERY_TIME")
	private String deliveryTimeSlot;

	@Column(name="ORDER_STATUS")
	private String orderStatus;
	
	@Column(name="DRIVER_EMPLOYEE_ID")
	private BigDecimal driverEmployeeId;
	
	@Column(name="APPL_DOCUMNET_NO")
	private String applDocNo;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATION_DATE")
	private Date createdDate;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
	
	@Column(name="UPDATED_DATE")
	private Date uopdateDate;
	
	@Column(name="EX_DELIVERY_REMARK_SEQ_ID")
	private BigDecimal remarksId;
	
	/** Status description will come  from master **/
	@Column(name="DELIVERY_CHARGES")
    private BigDecimal deliveryCharges;
	
	@Column(name="ISACTIVE")
	private String isActive;
	
	@Column(name = "TRANSACTION_RECEIPT_CLOB")
	String transactionReceipt;
	
	@Column(name = "OTP_TOKEN")
	String otpToken;
	
	@Column(name = "OTP_TOKEN_PREFIX")
	String otpTokenPrefix;

	@Column(name = "ORDER_LOCK")
	Date orderLock;
	
	@Column(name = "EMPLOYEE_ID")
	BigDecimal employeeId;
	
	public BigDecimal getDeleviryDelSeqId() {
		return deleviryDelSeqId;
	}
	public void setDeleviryDelSeqId(BigDecimal deleviryDelSeqId) {
		this.deleviryDelSeqId = deleviryDelSeqId;
	}
	
	public BigDecimal getShippingAddressId() {
		return shippingAddressId;
	}
	public void setShippingAddressId(BigDecimal shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
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
	
	public String getApplDocNo() {
		return applDocNo;
	}
	public void setApplDocNo(String applDocNo) {
		this.applDocNo = applDocNo;
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
	
	public Date getUopdateDate() {
		return uopdateDate;
	}
	public void setUopdateDate(Date uopdateDate) {
		this.uopdateDate = uopdateDate;
	}
	
	public BigDecimal getRemarksId() {
		return remarksId;
	}
	public void setRemarksId(BigDecimal remarksId) {
		this.remarksId = remarksId;
	}
	
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
		
	public BigDecimal getDeliveryCharges() {
		return deliveryCharges;
	}
	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}
	
	public String getTransactionReceipt() {
		return transactionReceipt;
	}
	public void setTransactionReceipt(String transactionReceipt) {
		this.transactionReceipt = transactionReceipt;
	}
	
	public String getOtpToken() {
		return otpToken;
	}
	public void setOtpToken(String otpToken) {
		this.otpToken = otpToken;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public Date getOrderLock() {
		return orderLock;
	}
	public void setOrderLock(Date orderLock) {
		this.orderLock = orderLock;
	}
	
	public BigDecimal getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getOtpTokenPrefix() {
		return otpTokenPrefix;
	}
	
	public void setOtpTokenPrefix(String otpTokenPrefix) {
		this.otpTokenPrefix = otpTokenPrefix;
	}
}

