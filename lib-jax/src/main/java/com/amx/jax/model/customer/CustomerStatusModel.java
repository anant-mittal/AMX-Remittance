package com.amx.jax.model.customer;

public class CustomerStatusModel {

	boolean isActiveBranch;
	boolean isActiveOnline;
	boolean isLockedOnline;
	
	public boolean isActiveBranch() {
		return isActiveBranch;
	}
	public void setActiveBranch(boolean isActiveBranch) {
		this.isActiveBranch = isActiveBranch;
	}
	public boolean isActiveOnline() {
		return isActiveOnline;
	}
	public void setActiveOnline(boolean isActiveOnline) {
		this.isActiveOnline = isActiveOnline;
	}
	public boolean isLockedOnline() {
		return isLockedOnline;
	}
	public void setLockedOnline(boolean isLockedOnline) {
		this.isLockedOnline = isLockedOnline;
	}
	
	
}
