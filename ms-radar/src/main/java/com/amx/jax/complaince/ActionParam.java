package com.amx.jax.complaince;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_VW_COAC_PARAM")
public class ActionParam implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String recordId;;

	private String actionCode;

	private String actionDesc;

	@Column(name = "RECORD_ID")
	public String getRecordId() {
		return recordId;
	}

	
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@Id
	@Column(name = "ACTION_CODE")
	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	@Column(name = "ACTION_DESCRIPTION")
	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}
	
	
	
	


}
