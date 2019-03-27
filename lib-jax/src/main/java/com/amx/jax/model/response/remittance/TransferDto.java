package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class TransferDto {
	String trasnferMode;
	BigDecimal transferModeId;
	public String getTrasnferMode() {
		return trasnferMode;
	}
	public void setTrasnferMode(String trasnferMode) {
		this.trasnferMode = trasnferMode;
	}
	public BigDecimal getTransferModeId() {
		return transferModeId;
	}
	public void setTransferModeId(BigDecimal transferModeId) {
		this.transferModeId = transferModeId;
	}
}
