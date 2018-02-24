package com.amx.jax.dbmodel.remittance;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "v_TRANSFER")
public class ViewTransfer {

	@Column(name = "DOCDAT")
	Date docDate;

	@Column(name = "CUSREF")
	BigDecimal cusRef;

	@Column(name = "ACYYMM")
	Date acyymm;

	@Column(name = "CANSTS")
	String cansts;

	@Id
	@Column(name = "TRNREF")
	String trnref;
	
	@Column(name = "NETAMT")
	BigDecimal netAmount;
	
	@Column(name="LOCCOD")
	BigDecimal locCode;
	
	@Column(name="BNFBNKCOD")
	String bnfBankCode;
	
	@Column(name="BNFACNO")
	String bnfAccountNo;
	
	@Column(name="E_BNFNAME")
	String engName;
	
	@Column(name="TRNFSTS")
	String trnfStatus;
	

	public Date getDocDate() {
		return docDate;
	}

	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}

	public BigDecimal getCusRef() {
		return cusRef;
	}

	public void setCusRef(BigDecimal cusRef) {
		this.cusRef = cusRef;
	}

	public Date getAcyymm() {
		return acyymm;
	}

	public void setAcyymm(Date acyymm) {
		this.acyymm = acyymm;
	}

	public String getCansts() {
		return cansts;
	}

	public void setCansts(String cansts) {
		this.cansts = cansts;
	}

	public String getTrnref() {
		return trnref;
	}

	public void setTrnref(String trnref) {
		this.trnref = trnref;
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public String getTrnfStatus() {
		return trnfStatus;
	}

	public void setTrnfStatus(String trnfStatus) {
		this.trnfStatus = trnfStatus;
	}




}
