package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "EX_BANK_ACCOUNT_LENGTH")
public class BankAccountLength implements java.io.Serializable {

	private BigDecimal accountLenId;
	private BigDecimal acLength;
	private String recordStatus;
	private Date createDate;
	private String creator;
	private String remarks;
	private BigDecimal bankId;

	public BankAccountLength() {
	}

	public BankAccountLength(BigDecimal accountLenId) {
		this.accountLenId = accountLenId;
	}
	


	public BankAccountLength(BigDecimal accountLenId,
			//BankMaster bankMaster,
			BigDecimal acLength, String recordStatus, Date createDate,
			String creator, String remarks) {
		super();
		this.accountLenId = accountLenId;
		this.acLength = acLength;
		this.recordStatus = recordStatus;
		this.createDate = createDate;
		this.creator = creator;
		this.remarks = remarks;
	}

	@Id
	@GeneratedValue(generator="ex_bank_account_length_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_bank_account_length_seq",sequenceName="EX_BANK_ACCOUNT_LENGTH_SEQ",allocationSize=1)
	@Column(name = "ACCOUNT_LENGTH_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getAccountLenId() {
		return this.accountLenId;
	}

	public void setAccountLenId(BigDecimal accountLenId) {
		this.accountLenId = accountLenId;
	}

	


	@Column(name = "ACCOUNT_LENGTH", precision = 22, scale = 0)
	public BigDecimal getAcLength() {
		return this.acLength;
	}

	public void setAcLength(BigDecimal acLength) {
		this.acLength = acLength;
	}

	@Column(name = "ISACTIVE", length = 1)
	public String getRecordStatus() {
		return this.recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	@Column(name = "CREATED_BY")
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name="BANK_ID")
	public BigDecimal getBankId() {
		return bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}


}
