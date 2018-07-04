package com.amx.jax.auth.trnx;

import java.io.Serializable;
import java.util.List;

import com.amx.jax.auth.dbmodel.Employee;
import com.amx.jax.auth.dbmodel.RoleDefinition;
import com.amx.jax.auth.dbmodel.UserRoleMaster;
import com.amx.jax.model.OtpData;

public class AuthLoginTrnxModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	OtpData otpData;
	Employee empDetails;
	List<RoleDefinition> roleDefinition;
	UserRoleMaster userMaster;
	
	public OtpData getOtpData() {
		return otpData;
	}
	public void setOtpData(OtpData otpData) {
		this.otpData = otpData;
	}
	
	public Employee getEmpDetails() {
		return empDetails;
	}
	public void setEmpDetails(Employee empDetails) {
		this.empDetails = empDetails;
	}
	
	public UserRoleMaster getUserMaster() {
		return userMaster;
	}
	public void setUserMaster(UserRoleMaster userMaster) {
		this.userMaster = userMaster;
	}
	
	public List<RoleDefinition> getRoleDefinition() {
		return roleDefinition;
	}
	public void setRoleDefinition(List<RoleDefinition> roleDefinition) {
		this.roleDefinition = roleDefinition;
	}
	
}
