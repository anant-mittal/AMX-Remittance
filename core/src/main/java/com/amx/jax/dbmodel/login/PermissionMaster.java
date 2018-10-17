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
@Table(name = "EX_PERMISSION_MASTER" )
public class PermissionMaster implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal permissionId;
	private BigDecimal moduleId;
	private BigDecimal functionalityTypeId;
	private String functionality;
	private String permissionEnum;
	private String isactive;
	private Date createdDate;
	
	@Id
	@GeneratedValue(generator="ex_permission_master_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_permission_master_seq",sequenceName="EX_PERMISSION_MASTER_SEQ",allocationSize=1)
	@Column(name = "PERMISSION_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(BigDecimal permissionId) {
		this.permissionId = permissionId;
	}
	
	@Column(name = "MODULE_ID")
	public BigDecimal getModuleId() {
		return moduleId;
	}
	public void setModuleId(BigDecimal moduleId) {
		this.moduleId = moduleId;
	}
	
	@Column(name = "FUNCTIONALITY_TYPE_ID")
	public BigDecimal getFunctionalityTypeId() {
		return functionalityTypeId;
	}
	public void setFunctionalityTypeId(BigDecimal functionalityTypeId) {
		this.functionalityTypeId = functionalityTypeId;
	}
	
	@Column(name = "FUNCTIONALITY")
	public String getFunctionality() {
		return functionality;
	}
	public void setFunctionality(String functionality) {
		this.functionality = functionality;
	}
	
	@Column(name = "PERMISSION_ENUM")
	public String getPermissionEnum() {
		return permissionEnum;
	}
	public void setPermissionEnum(String permissionEnum) {
		this.permissionEnum = permissionEnum;
	}
	
	@Column(name = "ISACTIVE")
	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}
	
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	} 
	
}
