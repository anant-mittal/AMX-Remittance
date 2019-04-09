package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

@Entity
@Table(name = "BLIST_DT" )
@Proxy(lazy = false)
@NamedQuery(name="BlackListDetailModel" ,query="select rownum as rowid,fuName,natCode,isActive, idType,idNumber from BlackListDetailModel")
public class BlackListDetailModel implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

	@Id
	@Column(name="rowid") 
	private BigDecimal rowId;
	
	@Column(name="FUNAME") 
	private String fuName;
	
	@Column(name="NATCOD") 
	private String natCode;
	
	
	@Column(name="IDTYP") 
	private String idType;
	
	@Column(name="IDNO") 
	private String idNumber;
	
	@Column(name="RECSTS") 
	private String isActive;
	
	@Column(name="SEQ_NO") 
	private BigDecimal seqNo;

	public String getFuName() {
		return fuName;
	}

	public void setFuName(String fuName) {
		this.fuName = fuName;
	}

	public String getNatCode() {
		return natCode;
	}

	public void setNatCode(String natCode) {
		this.natCode = natCode;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(BigDecimal seqNo) {
		this.seqNo = seqNo;
	}

	public BigDecimal getRowId() {
		return rowId;
	}

	public void setRowId(BigDecimal rowId) {
		this.rowId = rowId;
	}


	
	
}
