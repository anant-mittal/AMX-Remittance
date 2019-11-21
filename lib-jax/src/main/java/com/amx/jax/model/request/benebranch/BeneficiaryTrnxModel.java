package com.amx.jax.model.request.benebranch;

import java.io.Serializable;
import java.math.BigDecimal;

public class BeneficiaryTrnxModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BeneAccountModel beneAccountModel;
	BenePersonalDetailModel benePersonalDetailModel;
	BigDecimal beneTransactionAmountLimit;
	BigDecimal beneficaryAccountSeqId;
	BigDecimal beneficaryMasterSeqId;
	BigDecimal beneficaryRelationSeqId;

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

	public BigDecimal getBeneficaryAccountSeqId() {
		return beneficaryAccountSeqId;
	}

	public void setBeneficaryAccountSeqId(BigDecimal beneficaryAccountSeqId) {
		this.beneficaryAccountSeqId = beneficaryAccountSeqId;
	}

	public BigDecimal getBeneficaryMasterSeqId() {
		return beneficaryMasterSeqId;
	}

	public void setBeneficaryMasterSeqId(BigDecimal beneficaryMasterSeqId) {
		this.beneficaryMasterSeqId = beneficaryMasterSeqId;
	}

	@Override
	public String toString() {
		return "BeneficiaryTrnxModel [beneAccountModel=" + beneAccountModel + ", benePersonalDetailModel=" + benePersonalDetailModel
				+ ", beneTransactionAmountLimit=" + beneTransactionAmountLimit + ", beneficaryAccountSeqId=" + beneficaryAccountSeqId
				+ ", beneficaryMasterSeqId=" + beneficaryMasterSeqId + "]";
	}

	public BigDecimal getBeneficaryRelationSeqId() {
		return beneficaryRelationSeqId;
	}

	public void setBeneficaryRelationSeqId(BigDecimal beneficaryRelationSeqId) {
		this.beneficaryRelationSeqId = beneficaryRelationSeqId;
	}

}
