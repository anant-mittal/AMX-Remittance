package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_CONFIG")
public class JaxConfig {
	
	

	public JaxConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	public JaxConfig(String type, String value) {
		super();
		this.type = type;
		this.value = value;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="JAX_CONFIG_SEQ")
	@SequenceGenerator(name="JAX_CONFIG_SEQ", sequenceName="JAX_CONFIG_SEQ", allocationSize=1)
	@Column(name = "ID")
	BigDecimal id;

	@Column(name = "TYPE")
	String type;

	@Column(name = "VALUE")
	String value;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
