package com.amx.jax.dbmodel;

import java.math.BigDecimal;

/**
 * @author Subodh Bhoir
 *
 */

public class BranchDetailModel implements java.io.Serializable
{
	private String area;
	private String branchName;
	private BigDecimal contactNumber;
	private String branchAddress;
	
	public BranchDetailModel() {
		super();
	}
	
	public BranchDetailModel(String area, String branchName, BigDecimal contactNumber, String branchAddress) {
		this.area = area;
		this.branchName = branchName;
		this.contactNumber = contactNumber;
		this.branchAddress = branchAddress;
	}

	public String getArea() {
		return area;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getBranchName() {
		return branchName;
	}
	
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	public BigDecimal getContactNumber() {
		return contactNumber;
	}
	
	public void setContactNumber(BigDecimal contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	public String getBranchAddress() {
		return branchAddress;
	}
	
	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}	
}