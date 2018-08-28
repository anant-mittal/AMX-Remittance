package com.amx.jax.rbaac.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * The Class RolesResponseDTO.
 */
public class RolesResponseDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8763145353885254066L;
	
	/** The id. */
	private BigDecimal id;
	
	/** The role. */
	private String role;
	
	/** The permission map. */
	private Map<String, Map<String, String>> permissionMap;
	
	/** The suspended. */
	private String suspended;
	
	/** The flags. */
	private BigDecimal flags;
	
	/** The info. */
	private String info;
	
	/** The created date. */
	private Date createdDate;
	
	/** The updated date. */
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
	 * Gets the permission map.
	 *
	 * @return the permission map
	 */
	public Map<String, Map<String, String>> getPermissionMap() {
		return permissionMap;
	}
	
	/**
	 * Sets the permission map.
	 *
	 * @param permissionMap the permission map
	 */
	public void setPermissionMap(Map<String, Map<String, String>> permissionMap) {
		this.permissionMap = permissionMap;
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
