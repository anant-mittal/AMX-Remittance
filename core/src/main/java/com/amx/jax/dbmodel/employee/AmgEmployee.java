package com.amx.jax.dbmodel.employee;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Prashant
 *
 */
@Entity
@Table(name = "EX_AMG_EMPLOYEE")
public class AmgEmployee {

	@Id
	@Column(name = "CIVILID")
	String civilId;

	@Column(name = "EMPLOYEE_IND")
	String employeeInd;

	@Column(name = "MODIFIER")
	String modifier;

	@Column(name = "CUSREF")
	BigDecimal cusref;

	@Column(name = "ECNO")
	BigDecimal ecno;

	public String getCivilId() {
		return civilId;
	}

	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}

	public String getEmployeeInd() {
		return employeeInd;
	}

	public void setEmployeeInd(String employeeInd) {
		this.employeeInd = employeeInd;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public BigDecimal getCusref() {
		return cusref;
	}

	public void setCusref(BigDecimal cusref) {
		this.cusref = cusref;
	}

	public BigDecimal getEcno() {
		return ecno;
	}

	public void setEcno(BigDecimal ecno) {
		this.ecno = ecno;
	}

}
