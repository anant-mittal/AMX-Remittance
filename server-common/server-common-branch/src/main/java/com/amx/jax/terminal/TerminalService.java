package com.amx.jax.terminal;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.DeviceStateClient;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.signpad.SignPadBox;
import com.amx.jax.signpad.SignPadData;
import com.amx.utils.ArgUtil;

@Component
public class TerminalService {

	@Autowired
	private DeviceStateClient deviceClient;

	@Autowired
	private TerminalBox terminalBox;

	@Autowired
	private SignPadBox signPadBox;

	public void clearSignature(Integer terminalId) {
		String terminalIdStr = ArgUtil.parseAsString(terminalId);
		SignPadData signPadData = signPadBox.getOrDefault(terminalIdStr);
		signPadData.setSignature(null);
		signPadBox.fastPut(terminalIdStr, signPadData);
	}

	public AmxApiResponse<BoolRespModel, Object> updateRemittanceState(
			Integer terminalId, BigDecimal employeeId,
			SignaturePadRemittanceInfo signaturePadRemittanceInfo) {
		try {
			clearSignature(terminalId);
			return deviceClient.updateRemittanceState(ClientType.SIGNATURE_PAD, terminalId,
					signaturePadRemittanceInfo, employeeId);
		} finally {
			terminalBox.updateStamp(terminalId);
		}
	}

	public AmxApiResponse<BoolRespModel, Object> updateFcPurchaseState(
			Integer terminalId, BigDecimal employeeId,
			SignaturePadFCPurchaseSaleInfo signaturePadRemittanceInfo) {
		try {
			clearSignature(terminalId);
			return deviceClient.updateFcPurchase(ClientType.SIGNATURE_PAD, terminalId,
					signaturePadRemittanceInfo, employeeId);
		} finally {
			terminalBox.updateStamp(terminalId);
		}
	}

	public AmxApiResponse<BoolRespModel, Object> updateFcSaleState(Integer terminalId,
			BigDecimal employeeId,
			SignaturePadFCPurchaseSaleInfo signaturePadRemittanceInfo) {
		try {
			clearSignature(terminalId);
			return deviceClient.updateFcSale(ClientType.SIGNATURE_PAD, terminalId,
					signaturePadRemittanceInfo, employeeId);
		} finally {
			terminalBox.updateStamp(terminalId);
		}
	}

	public AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(
			Integer terminalId, BigDecimal employeeId,
			SignaturePadCustomerRegStateMetaInfo signaturePadRemittanceInfo) {
		try {
			clearSignature(terminalId);
			return deviceClient.updateCustomerRegStateData(ClientType.SIGNATURE_PAD, terminalId,
					signaturePadRemittanceInfo, employeeId);
		} finally {
			terminalBox.updateStamp(terminalId);
		}
	}
}
