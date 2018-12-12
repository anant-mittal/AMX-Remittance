package com.amx.amxlib.meta.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Subodh Bhoir
 *
 */

public class BranchDetailDTO implements Serializable {

	private static final long serialVersionUID = -6918862071933405769L;

	private String area;
	private String branchName;
	private BigDecimal contactNumber;
	private String branchAddress;
	private BigDecimal latitude;
	private BigDecimal longitude;
	
	private String govName;
	private String branches;
	private String branchWebsiteName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String country;
	private String postalCode;
	private String branchTiming;
	private BigDecimal countryBranchId;

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

	public String getGovName() {
		return govName;
	}

	public void setGovName(String govName) {
		this.govName = govName;
	}

	public String getBranches() {
		return branches;
	}

	public void setBranches(String branches) {
		this.branches = branches;
	}

	public String getBranchWebsiteName() {
		return branchWebsiteName;
	}

	public void setBranchWebsiteName(String branchWebsiteName) {
		this.branchWebsiteName = branchWebsiteName;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getBranchTiming() {
		return branchTiming;
	}

	public void setBranchTiming(String branchTiming) {
		this.branchTiming = branchTiming;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}
	
}