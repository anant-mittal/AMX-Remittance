package com.amx.jax.dbmodel;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.model.IResourceEntity;

@Entity
@Table(name = "VW_WIRE_PAYMENT_TYPE")
public class PaymentModesModel implements IResourceEntity,Serializable {
private static final long serialVersionUID = 7309549091432024935L;
	
	@Id
	@Column(name="PAYMENT_TYPE")
	private String paymentType;
	
	@Column(name="PAYMENT_DESCRIPTION")
	private String paymentDescription;

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentDescription() {
		return paymentDescription;
	}

	public void setPaymentDescription(String paymentDescription) {
		this.paymentDescription = paymentDescription;
	}

	@Override
	public BigDecimal resourceId() {
		
		return null;
	}

	@Override
	public String resourceName() {
		
		return paymentDescription;
	}

	@Override
	public String resourceCode() {
		
		return paymentType;
	}

	@Override
	public String resourceLocalName() {
		
		return null;
	}

	
}
