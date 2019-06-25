package com.amx.jax.dbmodel.partner;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_VW_EXT_BANK_BRANCH_CODES")
public class BankBranchExternalCodeView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal idNo;
	private String bankCode;
	private String BeneBankCode;
	private String BeneBankBranchCode;
	private String BranchExchId;
	
	@Id
	@Column(name = "IDNO")
	public BigDecimal getIdNo() {
		return idNo;
	}
	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}
	
	@Column(name = "BANK_CODE")
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	@Column(name = "BENE_BANK_CODE")
	public String getBeneBankCode() {
		return BeneBankCode;
	}
	public void setBeneBankCode(String beneBankCode) {
		BeneBankCode = beneBankCode;
	}
	
	@Column(name = "BENE_BRANCH_CODE")
	public String getBeneBankBranchCode() {
		return BeneBankBranchCode;
	}
	public void setBeneBankBranchCode(String beneBankBranchCode) {
		BeneBankBranchCode = beneBankBranchCode;
	}
	
	@Column(name = "BRANCH_EXCH_ID")
	public String getBranchExchId() {
		return BranchExchId;
	}
	public void setBranchExchId(String branchExchId) {
		BranchExchId = branchExchId;
	}
	
}
