/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.model.db;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Viki Sangani 15-Dec-2017 Demo.java
 */
@Entity
@Table(name = "DEMO")
public class Demo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;

	public Demo() {
	}

	public Demo(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID", length = 200)
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME", length = 200)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
