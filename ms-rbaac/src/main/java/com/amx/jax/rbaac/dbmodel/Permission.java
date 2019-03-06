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
 * The Class Permission.
 */
@Entity
@Table(name = "JAX_RB_PERMISSION" )
public class Permission  implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5129093020280830438L;
	
	/** The id. */
	@Id
	@GeneratedValue(generator = "permission_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "permission_seq", sequenceName = "JX_RB_PERMISSION_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal id;
	
	/** The permission. */
	@Column(name = "PERM_KEY", unique = true, nullable = false)
	private String permission;
	
	/** The access list json. */
	@Column(name = "ACCESS_TYPE_LIST")
	private String accessListJson;
	
	/** The scope list json. */
	@Column(name = "SCOPE_LIST")
	private String scopeListJson;

	/** The context. */
	@Column(name = "CONTEXT")
	private String context;
	
	
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
	 * Gets the permission.
	 *
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * Sets the permission.
	 *
	 * @param permission the new permission
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * Gets the access list json.
	 *
	 * @return the access list json
	 */
	public String getAccessListJson() {
		return accessListJson;
	}

	/**
	 * Sets the access list json.
	 *
	 * @param accessListJson the new access list json
	 */
	public void setAccessListJson(String accessListJson) {
		this.accessListJson = accessListJson;
	}

	/**
	 * Gets the scope list json.
	 *
	 * @return the scope list json
	 */
	public String getScopeListJson() {
		return scopeListJson;
	}

	/**
	 * Sets the scope list json.
	 *
	 * @param scopeListJson the new scope list json
	 */
	public void setScopeListJson(String scopeListJson) {
		this.scopeListJson = scopeListJson;
	}

	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * Sets the context.
	 *
	 * @param context the new context
	 */
	public void setContext(String context) {
		this.context = context;
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
