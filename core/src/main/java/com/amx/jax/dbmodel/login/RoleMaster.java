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
@Table(name = "EX_ROLE_MASTER" )
public class RoleMaster implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal roleId;
	private String roleCode;
	private String roleDescription;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String isactive;
	
	@Id
	@GeneratedValue(generator="ex_role_master_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_role_master_seq",sequenceName="EX_ROLE_MASTER_SEQ",allocationSize=1)
	@Column(name = "ROLE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getRoleId() {
		return roleId;
	}
	public void setRoleId(BigDecimal roleId) {
		this.roleId = roleId;
	}
	
	@Column(name = "ROLE_CODE")
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	@Column(name = "ROLE_DESCRIPTION")
	public String getRoleDescription() {
		return roleDescription;
	}
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	
	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name = "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@Column(name = "ISACTIVE")
	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}
	
}
