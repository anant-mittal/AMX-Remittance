package com.amx.jax.rbaac.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * The Class PermissionsResposeDTO.
 */
public class PermissionsResposeDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1700300245171551125L;

	/** The id. */
	private BigDecimal id;
	
	/** The permission. */
	private String permission;
	
	/** The access list. */
	private List<String> accessList;
	
	/** The scope list. */
	private List<String> scopeList;
	
	/** The context. */
	private String context;
	
	/** The flags. */
	private BigDecimal flags;
	
	/** The info. */
	private String info;

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
	 * Gets the access list.
	 *
	 * @return the access list
	 */
	public List<String> getAccessList() {
		return accessList;
	}

	/**
	 * Sets the access list.
	 *
	 * @param accessList the new access list
	 */
	public void setAccessList(List<String> accessList) {
		this.accessList = accessList;
	}

	/**
	 * Gets the scope list.
	 *
	 * @return the scope list
	 */
	public List<String> getScopeList() {
		return scopeList;
	}

	/**
	 * Sets the scope list.
	 *
	 * @param scopeList the new scope list
	 */
	public void setScopeList(List<String> scopeList) {
		this.scopeList = scopeList;
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

}
