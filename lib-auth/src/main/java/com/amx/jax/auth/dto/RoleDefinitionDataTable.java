package com.amx.jax.auth.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class RoleDefinitionDataTable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal moduleId;
	private BigDecimal permissionId;
	private BigDecimal permScopeId;
	private String admin;
	
	
	public BigDecimal getModuleId() {
		return moduleId;
	}
	public void setModuleId(BigDecimal moduleId) {
		this.moduleId = moduleId;
	}
	
	public BigDecimal getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(BigDecimal permissionId) {
		this.permissionId = permissionId;
	}
	
	public BigDecimal getPermScopeId() {
		return permScopeId;
	}
	public void setPermScopeId(BigDecimal permScopeId) {
		this.permScopeId = permScopeId;
	}
	
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
	
}
