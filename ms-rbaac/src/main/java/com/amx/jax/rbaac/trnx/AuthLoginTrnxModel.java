package com.amx.jax.rbaac.trnx;

import java.io.Serializable;
import java.util.List;

import com.amx.jax.model.OtpData;
import com.amx.jax.rbaac.dbmodel.FSEmployee;
import com.amx.jax.rbaac.dbmodel.RoleDefinition;
import com.amx.jax.rbaac.dbmodel.UserRoleMaster;

public class AuthLoginTrnxModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	OtpData otpData;
	FSEmployee empDetails;
	List<RoleDefinition> roleDefinition;
	UserRoleMaster userMaster;
	
	public OtpData getOtpData() {
		return otpData;
	}
	public void setOtpData(OtpData otpData) {
		this.otpData = otpData;
	}
	
	public FSEmployee getEmpDetails() {
		return empDetails;
	}
	public void setEmpDetails(FSEmployee empDetails) {
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
