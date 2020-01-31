package com.amx.jax.complaince;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_VW_CSSN_PARAM")
public class IndicatorParam implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	private String recordId;;

	private String indicatorCode;

	private String indicatorDesc;

	@Column(name = "RECORD_ID")
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
    @Id
	@Column(name = "INDIC_CODE")
	public String getIndicatorCode() {
		return indicatorCode;
	}

	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}

	@Column(name = "INDIC_DESCRIPTION")
	public String getIndicatorDesc() {
		return indicatorDesc;
	}

	public void setIndicatorDesc(String indicatorDesc) {
		this.indicatorDesc = indicatorDesc;
	}

	
}
