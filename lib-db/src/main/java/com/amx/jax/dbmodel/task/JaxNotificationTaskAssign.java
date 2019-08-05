package com.amx.jax.dbmodel.task;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_NOTIFICATION_TASK_ASSIGN")
public class JaxNotificationTaskAssign {

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JAX_NOTIFICATION_TASK_ASSIGN_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "JAX_NOTIFICATION_TASK_ASSIGN_SEQ", sequenceName = "JAX_NOTIFICATION_TASK_ASSIGN_SEQ", allocationSize = 1)
	BigDecimal id;

	@ManyToOne
	@JoinColumn(name = "NOTIFICATION_TASK_ID")
	JaxNotificationTask task;

	@Column(name = "EMPLOYEE_ROLE_ID")
	BigDecimal employeeRoleId;

	@Column(name = "COUNTRY_BRANCH_ID")
	BigDecimal countryBranchId;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public JaxNotificationTask getTask() {
		return task;
	}

	public void setTask(JaxNotificationTask task) {
		this.task = task;
	}

	public BigDecimal getEmployeeRoleId() {
		return employeeRoleId;
	}

	public void setEmployeeRoleId(BigDecimal employeeRoleId) {
		this.employeeRoleId = employeeRoleId;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

}
