package com.amx.amxlib.model.trnx;

import java.io.Serializable;
import java.math.BigDecimal;

public class BeneficiaryTrnxModel implements Serializable {

	BigDecimal bankId;

	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
}
