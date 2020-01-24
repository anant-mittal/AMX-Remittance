package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Entity
@Table(name="EX_GIFT_PACKAGE_DESC")
public class GiftPackageDescModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "ex_gift_package_desc_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_gift_package_desc_seq", sequenceName = "EX_GIFT_PACKAGE_DESC_SEQ", allocationSize = 1)
	@Column(name = "gift_package_seq", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal giftPackageSeq;
	
	@Column(name="PACKAGE_DESC")
	private String packageDesc;

	@Column(name="AMIEC_CODE")
	private String amiecCode;
	
	@Column(name="BANK_BRANCH_ID")
	private BigDecimal bankBranchId;
	
	@Column(name="BENEFICIARY_BANK_ID")
	private BigDecimal beneficiaryBankId;
	
	@Column(name="ROUTING_BANK_ID")
	private BigDecimal routingBankId;

	public BigDecimal getGiftPackageSeq() {
		return giftPackageSeq;
	}

	public void setGiftPackageSeq(BigDecimal giftPackageSeq) {
		this.giftPackageSeq = giftPackageSeq;
	}

	public String getAmiecCode() {
		return amiecCode;
	}

	public void setAmiecCode(String amiecCode) {
		this.amiecCode = amiecCode;
	}

	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}

	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
	}

	public BigDecimal getBeneficiaryBankId() {
		return beneficiaryBankId;
	}

	public void setBeneficiarybankId(BigDecimal beneficiaryBankId) {
		this.beneficiaryBankId = beneficiaryBankId;
	}

	public BigDecimal getRoutingBankId() {
		return routingBankId;
	}

	public void setRoutingBankId(BigDecimal routingBankId) {
		this.routingBankId = routingBankId;
	}

	public String getPackageDesc() {
		return packageDesc;
	}

	public void setPackageDesc(String packageDesc) {
		this.packageDesc = packageDesc;
	}
	

}
