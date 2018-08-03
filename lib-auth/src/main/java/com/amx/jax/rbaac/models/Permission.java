package com.amx.jax.rbaac.models;

public enum Permission {

	TR_TRX_FUN_EST(Module.TREASURY, PermType.TRANSACTIONAL, "Fund_Estimation");

	private PermType permType;
	private Module module;
	private String permission;

	Permission(Module module, PermType permType, String permission) {
		this.module = module;
		this.permType = permType;
		this.permission = permission;

	}

	public PermType getPermType() {
		return permType;
	}

	public Module getModule() {
		return module;
	}

	public String getPermission() {
		return permission;
	}
}
