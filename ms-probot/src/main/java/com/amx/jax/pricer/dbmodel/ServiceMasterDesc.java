package com.amx.jax.pricer.dbmodel;

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
@Table(name = "EX_SERVICE_MASTER_DESC")
public class ServiceMasterDesc implements Serializable {

	private static final long serialVersionUID = 3948079494013331401L;

	private BigDecimal serviceMasterDescId;

	private BigDecimal serviceId;

	private String serviceDesc;

	private BigDecimal languageId;
	
	public ServiceMasterDesc() {
		
	}
	
	public ServiceMasterDesc(BigDecimal serviceMasterDescId) {
		this.serviceMasterDescId = serviceMasterDescId;
	}
	
	@Id
	@GeneratedValue(generator = "ex_service_master_desc_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_service_master_desc_seq", sequenceName = "EX_SERVICE_MASTER_DESC_SEQ", allocationSize = 1)
	@Column(name = "SERVICE_MASTER_DESC_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getServiceMasterDescId() {
		return serviceMasterDescId;
	}

	public void setServiceMasterDescId(BigDecimal serviceMasterDescId) {
		this.serviceMasterDescId = serviceMasterDescId;
	}

	@Column(name = "SERVICE_ID")
	public BigDecimal getServiceId() {
		return serviceId;
	}

	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	@Column(name = "SERVICE_DESCRIPTION")
	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	@Column(name = "LANGUAGE_ID")
	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((languageId == null) ? 0 : languageId.hashCode());
		result = prime * result + ((serviceDesc == null) ? 0 : serviceDesc.hashCode());
		result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
		result = prime * result + ((serviceMasterDescId == null) ? 0 : serviceMasterDescId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceMasterDesc other = (ServiceMasterDesc) obj;
		if (languageId == null) {
			if (other.languageId != null)
				return false;
		} else if (!languageId.equals(other.languageId))
			return false;
		if (serviceDesc == null) {
			if (other.serviceDesc != null)
				return false;
		} else if (!serviceDesc.equals(other.serviceDesc))
			return false;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		if (serviceMasterDescId == null) {
			if (other.serviceMasterDescId != null)
				return false;
		} else if (!serviceMasterDescId.equals(other.serviceMasterDescId))
			return false;
		return true;
	}

	
}
