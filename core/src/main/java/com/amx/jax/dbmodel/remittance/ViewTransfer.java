package com.amx.jax.dbmodel.remittance;

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
	String cusRef;

	@Column(name = "ACYYMM")
	Date acyymm;

	@Column(name = "CANSTS")
	String cansts;

	@Id
	@Column(name = "TRNREF")
	String trnref;

	public Date getDocDate() {
		return docDate;
	}

	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}

	public String getCusRef() {
		return cusRef;
	}

	public void setCusRef(String cusRef) {
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

}
