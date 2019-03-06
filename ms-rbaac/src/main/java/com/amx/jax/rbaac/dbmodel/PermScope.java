/**
 * 
 */
package com.amx.jax.rbaac.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The Class PermScope.
 *
 * @author abhijeet
 */
@Entity
@Table(name = "JAX_RB_SCOPE" )
public class PermScope implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8008244265891481170L;

	/** The id. */
	@Id
	@GeneratedValue(generator = "scope_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "scope_seq", sequenceName = "JX_RB_SCOPE_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal id;

	/** The scope. */
	@Column(name = "SCOPE", unique = true, nullable = false)
	private String scope;

	/** The flags. */
	@Column(name = "FLAGS")
	private BigDecimal flags;

	/** The info. */
	@Column(name = "INFO")
	private String info;

	/** The created date. */
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	/** The updated date. */
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public BigDecimal getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 *            the new id
	 */
	public void setId(BigDecimal id) {
		this.id = id;
	}

	/**
	 * Gets the scope.
	 *
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}

	/**
	 * Sets the scope.
	 *
	 * @param scope
	 *            the new scope
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * Gets the flags.
	 *
	 * @return the flags
	 */
	public BigDecimal getFlags() {
		return flags;
	}

	/**
	 * Sets the flags.
	 *
	 * @param flags
	 *            the new flags
	 */
	public void setFlags(BigDecimal flags) {
		this.flags = flags;
	}

	/**
	 * Gets the info.
	 *
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Sets the info.
	 *
	 * @param info
	 *            the new info
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate
	 *            the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the updated date.
	 *
	 * @return the updated date
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * Sets the updated date.
	 *
	 * @param updatedDate
	 *            the new updated date
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
