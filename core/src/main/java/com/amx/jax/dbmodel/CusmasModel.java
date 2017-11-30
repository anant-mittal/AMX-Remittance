package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="cusmas")
public class CusmasModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5344922903794550152L;
	
	@Id
	@Column(name="CUSREF")
	private BigDecimal customerReference;
	
	@Column(name="CIVILID")
	private String civilId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="IDEXPDT")
	private Date idExpireDate;
	
	@Column(name="RECSTS")
	private String status;
	
	@Column(name="IDTYP")
	private String idType;

	public BigDecimal getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}

	public String getCivilId() {
		return civilId;
	}

	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}

	public Date getIdExpireDate() {
		return idExpireDate;
	}

	public void setIdExpireDate(Date idExpireDate) {
		this.idExpireDate = idExpireDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}
	

}
