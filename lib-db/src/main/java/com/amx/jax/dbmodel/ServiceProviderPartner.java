package com.amx.jax.dbmodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="JAX_VW_TPC_PARTNER")
public class ServiceProviderPartner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7128922370602174758L;
	
	@Id
	@Column(name="RECORD_ID")
	private String recordId;
	
	@Column(name="TPC_CODE")
	private String tpcCode;
	
	@Column(name="TPC_NAME")
	private String tpcName;

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getTpcCode() {
		return tpcCode;
	}

	public void setTpcCode(String tpcCode) {
		this.tpcCode = tpcCode;
	}

	public String getTpcName() {
		return tpcName;
	}

	public void setTpcName(String tpcName) {
		this.tpcName = tpcName;
	}

}
