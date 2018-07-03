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
@Table(name = "EX_USER_ROLE_MASTER" )
public class UserRoleMaster implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal userRoleId;
	private BigDecimal employeeId;
	private BigDecimal roleId;
	private Date createdDate;
	private String isactive;
	
	@Id
	@GeneratedValue(generator="ex_user_role_master_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_user_role_master_seq",sequenceName="EX_USER_ROLE_MASTER",allocationSize=1)
	@Column(name = "USER_ROLE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(BigDecimal userRoleId) {
		this.userRoleId = userRoleId;
	}
	
	@Column(name = "EMPLOYEE_ID")
	public BigDecimal getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}
	
	@Column(name = "ROLE_ID")
	public BigDecimal getRoleId() {
		return roleId;
	}
	public void setRoleId(BigDecimal roleId) {
		this.roleId = roleId;
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
}
