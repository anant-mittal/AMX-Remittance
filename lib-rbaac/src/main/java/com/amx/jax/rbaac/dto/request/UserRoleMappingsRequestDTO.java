package com.amx.jax.rbaac.dto.request;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.amx.jax.rbaac.dto.response.UserRoleMappingDTO;

/**
 * The Class UserRoleMappingsRequestDTO.
 * 
 */
public class UserRoleMappingsRequestDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7204846177787386980L;

	/** The employee role id map. */
	private List<UserRoleMappingDTO> userRoleMappingInfoList;

	/** The ip addr. */
	@NotBlank(message = "IP Address Can not be Blank.")
	private String ipAddr;

	/** The device id. */
	private String deviceId;

	/**
	 * Gets the user role mapping info list.
	 *
	 * @return the user role mapping info list
	 */
	public List<UserRoleMappingDTO> getUserRoleMappingInfoList() {
		return userRoleMappingInfoList;
	}

	/**
	 * Sets the user role mapping info list.
	 *
	 * @param userRoleMappingInfoList the new user role mapping info list
	 */
	public void setUserRoleMappingInfoList(List<UserRoleMappingDTO> userRoleMappingInfoList) {
		this.userRoleMappingInfoList = userRoleMappingInfoList;
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
	 * @param ipAddr
	 *            the new ip addr
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
	 * @param deviceId
	 *            the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
