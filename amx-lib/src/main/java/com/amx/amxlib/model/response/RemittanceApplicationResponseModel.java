package com.amx.amxlib.model.response;

import java.math.BigDecimal;

import com.amx.amxlib.model.AbstractModel;

public class RemittanceApplicationResponseModel extends AbstractModel {

	public BigDecimal remittanceAppId;
	
	@Override
	public String getModelType() {
		return "remittance-application";
	}

	public BigDecimal getRemittanceAppId() {
		return remittanceAppId;
	}

	public void setRemittanceAppId(BigDecimal remittanceAppId) {
		this.remittanceAppId = remittanceAppId;
	}

}
