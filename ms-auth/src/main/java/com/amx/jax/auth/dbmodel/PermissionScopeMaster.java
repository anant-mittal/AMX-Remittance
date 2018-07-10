package com.amx.jax.auth.dbmodel;

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
@Table(name = "EX_SCOPE_MASTER" )
public class PermissionScopeMaster implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal scopeId;
	private String scopeEnum;
	private String scopeDescription;
	private String isactive;
	private Date createdDate;
	
	@Id
	@GeneratedValue(generator="ex_scope_master_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_scope_master_seq",sequenceName="EX_SCOPE_MASTER_SEQ",allocationSize=1)
	@Column(name = "SCOPE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getScopeId() {
		return scopeId;
	}
	public void setScopeId(BigDecimal scopeId) {
		this.scopeId = scopeId;
	}
	
	@Column(name = "SCOPE_ENUM")
	public String getScopeEnum() {
		return scopeEnum;
	}
	public void setScopeEnum(String scopeEnum) {
		this.scopeEnum = scopeEnum;
	}
	
	@Column(name = "SCOPE_DESCRIPTION")
	public String getScopeDescription() {
		return scopeDescription;
	}
	public void setScopeDescription(String scopeDescription) {
		this.scopeDescription = scopeDescription;
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
