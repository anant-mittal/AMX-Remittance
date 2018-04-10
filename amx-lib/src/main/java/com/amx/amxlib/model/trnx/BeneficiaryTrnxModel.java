package com.amx.amxlib.model.trnx;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.BenePersonalDetailModel;

public class BeneficiaryTrnxModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BeneAccountModel beneAccountModel;
	BenePersonalDetailModel benePersonalDetailModel;
	BigDecimal beneTransactionAmountLimit;

	public BeneAccountModel getBeneAccountModel() {
		return beneAccountModel;
	}

	public void setBeneAccountModel(BeneAccountModel beneAccountModel) {
		this.beneAccountModel = beneAccountModel;
	}

	public BenePersonalDetailModel getBenePersonalDetailModel() {
		return benePersonalDetailModel;
	}

	public void setBenePersonalDetailModel(BenePersonalDetailModel benePersonalDetailModel) {
		this.benePersonalDetailModel = benePersonalDetailModel;
	}

	public BigDecimal getBeneTransactionAmountLimit() {
		return beneTransactionAmountLimit;
	}

	public void setBeneTransactionAmountLimit(BigDecimal beneTransactionAmountLimit) {
		this.beneTransactionAmountLimit = beneTransactionAmountLimit;
	}

}
