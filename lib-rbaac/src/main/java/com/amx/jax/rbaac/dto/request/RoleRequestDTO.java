package com.amx.jax.rbaac.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;

/**
 * The Class RoleRequestDTO.
 */
public class RoleRequestDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3772680674304665585L;

	/** The id. */
	private BigDecimal id;

	/** The role. */
	@NotBlank(message = "Role Can not be Blank.")
	private String role;

	/** The permission map. */
	@NotBlank(message = "Permission Map Can not be Blank.")
	private Map<String, Map<String, String>> permissionMap;

	/** The suspended. */
	private String suspended;

	/** The flags. */
	private BigDecimal flags;

	/** The info. */
	private String info;

	/** The ip addr. */
	@NotBlank(message = "IP Address Can not be Blank.")
	private String ipAddr;

	/** The device id. */
	private String deviceId;

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
	 * Gets the ip addr.
	 *
	 * @return the ip addr
	 */
	public String getIpAddr() {
		return ipAddr;
	}

	/**
	 * Sets the ip addr.
	 *
	 * @param ipAddr the new ip addr
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	/**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceId the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
