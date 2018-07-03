package com.amx.jax.auth.meta.model;

import java.io.Serializable;

public class UserDetailsDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	private String userName;
	private String userRole;
	private String hierarchyId;
	private String hierarchyValue;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public String getHierarchyId() {
		return hierarchyId;
	}
	public void setHierarchyId(String hierarchyId) {
		this.hierarchyId = hierarchyId;
	}
	
	public String getHierarchyValue() {
		return hierarchyValue;
	}
	public void setHierarchyValue(String hierarchyValue) {
		this.hierarchyValue = hierarchyValue;
	}

}
