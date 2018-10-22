package com.amx.jax.device;

import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.jax.model.response.IDeviceStateData;

public class SignaturePadRemittanceInfo implements IDeviceStateData {

	RemittanceReceiptSubreport reportData;

	public SignaturePadRemittanceInfo(RemittanceReceiptSubreport remittanceReceiptSubreport) {

		this.reportData = remittanceReceiptSubreport;
	}

	public RemittanceReceiptSubreport getReportData() {
		return reportData;
	}

	public void setReportData(RemittanceReceiptSubreport reportData) {
		this.reportData = reportData;
	}
}
