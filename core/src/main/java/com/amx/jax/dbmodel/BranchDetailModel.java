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
	private BigDecimal latitude;
	private BigDecimal longitude;
	
	public BranchDetailModel() {
		super();
	}
	
	public BranchDetailModel(String area, String branchName, BigDecimal contactNumber, String branchAddress) {
		this.area = area;
		this.branchName = branchName;
		this.contactNumber = contactNumber;
		this.branchAddress = branchAddress;
	}
	
	public BranchDetailModel(String area, String branchName, BigDecimal contactNumber, String branchAddress,
			BigDecimal latitude, BigDecimal longitude) {
		super();
		this.area = area;
		this.branchName = branchName;
		this.contactNumber = contactNumber;
		this.branchAddress = branchAddress;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}	
}