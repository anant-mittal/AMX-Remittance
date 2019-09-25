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
@Table(name = "JAX_ADDL_BENE_DETAIL_DATA")
public class AdditionalBankDetailData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5082942227282351230L;

	@Id
	@GeneratedValue(generator = "JAX_ADDL_BENE_DETAIL_DATA_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "JAX_ADDL_BENE_DETAIL_DATA_SEQ", sequenceName = "JAX_ADDL_BENE_DETAIL_DATA_SEQ", allocationSize = 1)
	@Column(name = "JAX_ADDL_BENE_DETAIL_DATA_ID", unique = true, nullable = false, precision = 22, scale = 0)
	BigDecimal id;

	@Column(name = "BENEFICARY_RELATIONSHIP_SEQ_ID")
	BigDecimal beneRelSeqId;

	@Column(name = "KEY")
	String key;

	@Column(name = "VALUE")
	String value;
	
	

	public AdditionalBankDetailData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdditionalBankDetailData(BigDecimal beneRelSeqId, String key, String value) {
		super();
		this.beneRelSeqId = beneRelSeqId;
		this.key = key;
		this.value = value;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getBeneRelSeqId() {
		return beneRelSeqId;
	}

	public void setBeneRelSeqId(BigDecimal beneRelSeqId) {
		this.beneRelSeqId = beneRelSeqId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
