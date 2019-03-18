package com.amx.amxlib.model;

public class CustomerFlags {
	public Boolean annualIncomeExpired;

	public CustomerFlags(Boolean annualIncomeExpired) {
		this.annualIncomeExpired = annualIncomeExpired;
	}

	public CustomerFlags(){
		
	}

	
	public Boolean getAnnualIncomeExpired() {
		return annualIncomeExpired;
	}

	public void setAnnualIncomeExpired(Boolean annualIncomeExpired) {
		this.annualIncomeExpired = annualIncomeExpired;
	}
}
