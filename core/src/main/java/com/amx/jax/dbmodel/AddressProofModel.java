package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name = "EX_ADDRESS_PROOF")
public class AddressProofModel implements IResourceEntity, Serializable {
	private static final long serialVersionUID = 7309549091432024935L;
	@Id
	@Column(name = "EX_ADDRESS_PROOF_ID")
	private BigDecimal addressProofId;
	public BigDecimal getAddressProofId() {
		return addressProofId;
	}

	public void setAddressProofId(BigDecimal addressProofId) {
		this.addressProofId = addressProofId;
	}
	
	@Column(name ="CODE")
	private String code;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="DESCRIPTION")
	private String description;
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="ISACTIVE")
	private String isActive;
	
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Override
	public BigDecimal resourceId() {
		
		return this.addressProofId;
	}

	@Override
	public String resourceName() {
		
		return this.description;
	}

	@Override
	public String resourceCode() {
		
		return this.code;
	}

	@Override
	public String resourceLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

}

	
	
	
	
	
	
	

	
	
	