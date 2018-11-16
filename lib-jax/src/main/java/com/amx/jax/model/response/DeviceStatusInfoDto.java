package com.amx.jax.model.response;

import java.io.Serializable;
import java.util.Date;

import com.amx.jax.constants.DeviceState;
import com.amx.jax.constants.DeviceStateDataType;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;

public class DeviceStatusInfoDto implements Serializable {

	private static final long serialVersionUID = -6352739842431809408L;

	DeviceState deviceState;

	DeviceStateDataType stateDataType;

	SignaturePadCustomerRegStateInfo signaturePadCustomerRegStateInfo;
	SignaturePadFCPurchaseSaleInfo signaturePadFCSaleInfo;
	SignaturePadFCPurchaseSaleInfo signaturePadFCPurchaseInfo;
	SignaturePadRemittanceInfo signaturePadRemittanceInfo;

	Date branchPcLastLogoutTime;

	public DeviceState getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(DeviceState deviceState) {
		this.deviceState = deviceState;
	}

	public DeviceStateDataType getStateDataType() {
		return stateDataType;
	}

	public void setStateDataType(DeviceStateDataType stateDataType) {
		this.stateDataType = stateDataType;
	}

	public SignaturePadCustomerRegStateInfo getSignaturePadCustomerRegStateInfo() {
		return signaturePadCustomerRegStateInfo;
	}

	public void setSignaturePadCustomerRegStateInfo(SignaturePadCustomerRegStateInfo signaturePadCustomerRegStateInfo) {
		this.signaturePadCustomerRegStateInfo = signaturePadCustomerRegStateInfo;
	}

	public SignaturePadFCPurchaseSaleInfo getSignaturePadFCSaleInfo() {
		return signaturePadFCSaleInfo;
	}

	public void setSignaturePadFCSaleInfo(SignaturePadFCPurchaseSaleInfo signaturePadFCSaleInfo) {
		this.signaturePadFCSaleInfo = signaturePadFCSaleInfo;
	}

	public SignaturePadFCPurchaseSaleInfo getSignaturePadFCPurchaseInfo() {
		return signaturePadFCPurchaseInfo;
	}

	public void setSignaturePadFCPurchaseInfo(SignaturePadFCPurchaseSaleInfo signaturePadFCPurchaseInfo) {
		this.signaturePadFCPurchaseInfo = signaturePadFCPurchaseInfo;
	}

	public SignaturePadRemittanceInfo getSignaturePadRemittanceInfo() {
		return signaturePadRemittanceInfo;
	}

	public void setSignaturePadRemittanceInfo(SignaturePadRemittanceInfo signaturePadRemittanceInfo) {
		this.signaturePadRemittanceInfo = signaturePadRemittanceInfo;
	}

	public Date getBranchPcLastLogoutTime() {
		return branchPcLastLogoutTime;
	}

	public void setBranchPcLastLogoutTime(Date branchPcLastLogoutTime) {
		this.branchPcLastLogoutTime = branchPcLastLogoutTime;
	}

}
