package com.amx.jax.dbmodel.login;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EX_ROLE_DEFINITION" )
public class RoleDefinition implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal roleDefId;
	private BigDecimal roleId;
	private BigDecimal permissionId;
	private BigDecimal scopeId;
	private String admin;
	private Date createdDate;
	private String isactive;
	
	@Id
	@GeneratedValue(generator="ex_role_definition_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_role_definition_seq",sequenceName="EX_ROLE_DEFINITION_SEQ",allocationSize=1)
	@Column(name = "ROLE_DEF_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getRoleDefId() {
		return roleDefId;
	}
	public void setRoleDefId(BigDecimal roleDefId) {
		this.roleDefId = roleDefId;
	}
	
	@Column(name = "ROLE_ID")
	public BigDecimal getRoleId() {
		return roleId;
	}
	public void setRoleId(BigDecimal roleId) {
		this.roleId = roleId;
	}
	
	@Column(name = "PERMISSION_ID")
	public BigDecimal getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(BigDecimal permissionId) {
		this.permissionId = permissionId;
	}
	
	@Column(name = "SCOPE_ID")
	public BigDecimal getScopeId() {
		return scopeId;
	}
	public void setScopeId(BigDecimal scopeId) {
		this.scopeId = scopeId;
	}
	
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "ISACTIVE")
	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}
	
	@Column(name = "ADMIN")
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
}
