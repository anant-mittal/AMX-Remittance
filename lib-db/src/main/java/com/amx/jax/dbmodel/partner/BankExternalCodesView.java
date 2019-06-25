package com.amx.jax.dbmodel.partner;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_VW_EXT_BANK_CODES")
public class BankExternalCodesView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal idNo;
	private String bankCode;
	private String BeneBankCode;
	private String BankExchId;
	
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
	
	@Column(name = "BANK_EXCH_ID")
	public String getBankExchId() {
		return BankExchId;
	}
	public void setBankExchId(String bankExchId) {
		BankExchId = bankExchId;
	}

}
