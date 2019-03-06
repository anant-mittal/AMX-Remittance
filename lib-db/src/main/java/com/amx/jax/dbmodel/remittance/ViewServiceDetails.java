package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="V_EX_SERVICE_MASTER")
public class ViewServiceDetails  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="SERVICE_CODE")
	private String serviceCode;
	@Id
	@Column(name="SERVICE_MASTER_ID")
	private BigDecimal serviceMasterId;
	@Column(name="SERVICE_DESCRIPTION")
	private String serviceDescription;
	
	@Column(name="SERVICE_GROUP_CODE")
	private String serviceGroupCode;

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public BigDecimal getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(BigDecimal serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public String getServiceGroupCode() {
		return serviceGroupCode;
	}

	public void setServiceGroupCode(String serviceGroupCode) {
		this.serviceGroupCode = serviceGroupCode;
	}
	
	


}
