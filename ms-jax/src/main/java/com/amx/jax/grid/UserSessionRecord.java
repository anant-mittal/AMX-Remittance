package com.amx.jax.grid;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.amx.jax.dbmodel.employee.UserSession;

@Entity
public class UserSessionRecord extends UserSession implements GridViewRecord {

	private static final long serialVersionUID = 412176528892609487L;

	private Integer totalRecords;

	@Transient
	private Integer rn;

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Integer getRn() {
		return rn;
	}

	public void setRn(Integer rn) {
		this.rn = rn;
	}
}
