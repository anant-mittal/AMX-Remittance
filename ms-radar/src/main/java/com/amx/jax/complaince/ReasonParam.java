package com.amx.jax.complaince;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_VW_CORE_PARAM")
public class ReasonParam implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String recordId;;

	private String reasonCode;

	private String reasonDesc;

	@Column(name = "RECORD_ID")
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	@Id
	@Column(name = "REASON_CODE")
	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	@Column(name = "REASON_DESCRIPTION")
	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	
	

}
