package com.amx.jax.auditlog;

import java.math.BigDecimal;

import com.amx.jax.dbmodel.fx.FxDeliveryDetailsModel;

public class FcSaleOrderStatusChangeAuditEvent extends JaxAuditEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BigDecimal deliveryDetailSeqId;
	String oldOrderStatusCode;
	String newOrderStatusCode;
	BigDecimal branchEmployeeId;
	BigDecimal driverEmployeeId;

	public FcSaleOrderStatusChangeAuditEvent(FxDeliveryDetailsModel deliveryDetailModel, String oldOrderStatus,
			EventType type) {
		super(type);
		this.deliveryDetailSeqId = deliveryDetailModel.getDeleviryDelSeqId();
		this.newOrderStatusCode = deliveryDetailModel.getOrderStatus();
		this.branchEmployeeId = deliveryDetailModel.getDriverEmployeeId();
		this.driverEmployeeId = deliveryDetailModel.getDriverEmployeeId();
		this.oldOrderStatusCode = oldOrderStatus;
	}

	public BigDecimal getDeliveryDetailSeqId() {
		return deliveryDetailSeqId;
	}

	public void setDeliveryDetailSeqId(BigDecimal deliveryDetailSeqId) {
		this.deliveryDetailSeqId = deliveryDetailSeqId;
	}

	public String getOldOrderStatusCode() {
		return oldOrderStatusCode;
	}

	public void setOldOrderStatusCode(String oldOrderStatusCode) {
		this.oldOrderStatusCode = oldOrderStatusCode;
	}

	public String getNewOrderStatusCode() {
		return newOrderStatusCode;
	}

	public void setNewOrderStatusCode(String newOrderStatusCode) {
		this.newOrderStatusCode = newOrderStatusCode;
	}

	public BigDecimal getBranchEmployeeId() {
		return branchEmployeeId;
	}

	public void setBranchEmployeeId(BigDecimal branchEmployeeId) {
		this.branchEmployeeId = branchEmployeeId;
	}

	public BigDecimal getDriverEmployeeId() {
		return driverEmployeeId;
	}

	public void setDriverEmployeeId(BigDecimal driverEmployeeId) {
		this.driverEmployeeId = driverEmployeeId;
	}

}
