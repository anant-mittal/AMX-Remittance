package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

/**
 * @author Subodh Bhoir
 *
 */

public class BranchDetailDTO {

	private String area;
	private String branchName;
	private BigDecimal contactNumber;
	private String branchAddress;
	
	public BranchDetailDTO() {
		super();
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