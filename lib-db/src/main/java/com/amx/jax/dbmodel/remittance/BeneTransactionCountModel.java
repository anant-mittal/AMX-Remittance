package com.amx.jax.dbmodel.remittance;

import java.math.BigDecimal;

public class BeneTransactionCountModel {

	BigDecimal beneRelSeqId;
	long trnxCount;
	public BigDecimal getBeneRelSeqId() {
		return beneRelSeqId;
	}
	public void setBeneRelSeqId(BigDecimal beneRelSeqId) {
		this.beneRelSeqId = beneRelSeqId;
	}
	public long getTrnxCount() {
		return trnxCount;
	}
	public void setTrnxCount(long trnxCount) {
		this.trnxCount = trnxCount;
	}
	public BeneTransactionCountModel(BigDecimal beneRelSeqId, long trnxCount) {
		super();
		this.beneRelSeqId = beneRelSeqId;
		this.trnxCount = trnxCount;
	}
	
	
	
	
}
