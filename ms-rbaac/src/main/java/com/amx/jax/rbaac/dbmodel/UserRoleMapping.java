package com.amx.jax.rbaac.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The Class UserRoleMapping.
 */
@Entity
@Table(name = "JAX_RB_USER_ROLE_MAPPING")
public class UserRoleMapping implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5253585909369150240L;

	/** The id. */
	@Id
	@GeneratedValue(generator = "user_role_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "user_role_seq", sequenceName = "JX_RB_USER_ROLE_MAPPING_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal id;

	/** The employee id. */
	@Column(name = "EMPLOYEE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal employeeId;

	/** The role id. */
	@Column(name = "ROLE_ID", nullable = false)
	private BigDecimal roleId;

	/** The suspended. */
	@Column(name = "SUSPENDED")
	private String suspended;

	/** The flags. */
	@Column(name = "FLAGS")
	private BigDecimal flags;

	/** The info. */
	@Column(name = "INFO")
	private String info;

	/** The created date. */
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	/** The updated date. */
	@Column(name = "UPDATED_DATE")
	private Date updatedDate;

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
	 * Gets the employee id.
	 *
	 * @return the employee id
	 */
	public BigDecimal getEmployeeId() {
		return employeeId;
	}

	/**
	 * Sets the employee id.
	 *
	 * @param employeeId the new employee id
	 */
	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * Gets the role id.
	 *
	 * @return the role id
	 */
	public BigDecimal getRoleId() {
		return roleId;
	}

	/**
	 * Sets the role id.
	 *
	 * @param roleId the new role id
	 */
	public void setRoleId(BigDecimal roleId) {
		this.roleId = roleId;
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
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Gets the updated date.
	 *
	 * @return the updated date
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * Sets the updated date.
	 *
	 * @param updatedDate the new updated date
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
