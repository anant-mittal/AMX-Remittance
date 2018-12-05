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
 * The Class Role.
 */
@Entity
@Table(name = "JAX_RB_ROLE")
public class Role implements Serializable {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3884746406628189127L;
	
	
	/** The id. */
	@Id
	@GeneratedValue(generator = "role_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "role_seq", sequenceName = "JX_RB_ROLE_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal id;
	
	/** The role. */
	@Column(name = "ROLE", unique = true, nullable = false)
	private String role;
	
	/** The permissions json. */
	@Column(name = "PERMS")
	private String permissionsJson;

	/** The suspended. */
	@Column(name = "SUSPENDED")
	private String suspended;
	
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
	 * @param id the new id
	 */
	public void setId(BigDecimal id) {
		this.id = id;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 *
	 * @param role the new role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Gets the permissions json.
	 *
	 * @return the permissions json
	 */
	public String getPermissionsJson() {
		return permissionsJson;
	}

	/**
	 * Sets the permissions json.
	 *
	 * @param permissionsJson the new permissions json
	 */
	public void setPermissionsJson(String permissionsJson) {
		this.permissionsJson = permissionsJson;
	}

	/**
	 * Gets the suspended.
	 *
	 * @return the suspended
	 */
	public String getSuspended() {
		return suspended;
	}

	/**
	 * Sets the suspended.
	 *
	 * @param suspended the new suspended
	 */
	public void setSuspended(String suspended) {
		this.suspended = suspended;
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
	 * @param flags the new flags
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
	 * @param info the new info
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
	 * @param createdDate the new created date
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
	 * @param updatedDate the new updated date
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}


	
	

}
