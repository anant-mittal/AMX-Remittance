package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_EX_IDLT_LENGTH")
public class IDNumberLengthCheckView implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	private BigDecimal iDTypeId;
	private BigDecimal iDLength;
	private String iDTypeName;
	private BigDecimal paramId;
	private String fullDesc;
	private BigDecimal paramCode;
	private BigDecimal MinIDLength;
	
	@Id
	@Column(name="PARAM_ID")
	public BigDecimal getParamId() {
		return paramId;
	}
	public void setParamId(BigDecimal paramId) {
		this.paramId = paramId;
	}
	
	
	@Column(name="ID_TYPE_ID")
	public BigDecimal getiDTypeId() {
		return iDTypeId;
	}
	public void setiDTypeId(BigDecimal iDTypeId) {
		this.iDTypeId = iDTypeId;
	}
	
	
	@Column(name="ID_LENGTH")
	public BigDecimal getiDLength() {
		return iDLength;
	}
	public void setiDLength(BigDecimal iDLength) {
		this.iDLength = iDLength;
	}
	
	@Column(name="ID_TYPE_NAME")
	public String getiDTypeName() {
		return iDTypeName;
	}
	public void setiDTypeName(String iDTypeName) {
		this.iDTypeName = iDTypeName;
	}
	
	
	@Column(name="ID_TYPE")
	public String getFullDesc() {
		return fullDesc;
	}
	public void setFullDesc(String fullDesc) {
		this.fullDesc = fullDesc;
	}
	
	@Column(name="PARAM_CODE")
	public BigDecimal getParamCode() {
		return paramCode;
	}
	public void setParamCode(BigDecimal paramCode) {
		this.paramCode = paramCode;
	}
	
	@Column(name="MIN_ID_LENGTH")
	public BigDecimal getMinIDLength() {
		return MinIDLength;
	}
	public void setMinIDLength(BigDecimal minIDLength) {
		MinIDLength = minIDLength;
	}
	
}
