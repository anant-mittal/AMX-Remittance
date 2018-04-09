package com.amx.amxlib.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class BenePersonalDetailModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// bene master
	@NotNull
	private BigDecimal stateId;
	@NotNull
	private BigDecimal cityId;
	@NotNull
	private BigDecimal countryId;
	@NotNull
	private BigDecimal districtId;
	@NotNull
	private String firstName;
	@NotNull
	private String secondName;
	private String thirdName;
	private String fourthName;
	private String fifthName;
	private String nationality;
	private String localFirstName;
	private String localSecondName;
	private String localThirdName;
	private String localFourthName;
	private String localFifthName;
	// bene contact
	private String telephoneNumber;
	@NotNull
	private BigDecimal mobileNumber;
	@NotNull
	private String countryTelCode;
	// bene relationship
	private String remarks;
	@NotNull
	private BigDecimal relationsId;

	@Override
	public String getModelType() {
		return "bene-personal-details";
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getThirdName() {
		return thirdName;
	}

	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}

	public String getFourthName() {
		return fourthName;
	}

	public void setFourthName(String fourthName) {
		this.fourthName = fourthName;
	}

	public String getFifthName() {
		return fifthName;
	}

	public void setFifthName(String fifthName) {
		this.fifthName = fifthName;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getLocalFirstName() {
		return localFirstName;
	}

	public void setLocalFirstName(String localFirstName) {
		this.localFirstName = localFirstName;
	}

	public String getLocalSecondName() {
		return localSecondName;
	}

	public void setLocalSecondName(String localSecondName) {
		this.localSecondName = localSecondName;
	}

	public String getLocalThirdName() {
		return localThirdName;
	}

	public void setLocalThirdName(String localThirdName) {
		this.localThirdName = localThirdName;
	}

	public String getLocalFourthName() {
		return localFourthName;
	}

	public void setLocalFourthName(String localFourthName) {
		this.localFourthName = localFourthName;
	}

	public String getLocalFifthName() {
		return localFifthName;
	}

	public void setLocalFifthName(String localFifthName) {
		this.localFifthName = localFifthName;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public BigDecimal getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(BigDecimal mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCountryTelCode() {
		return countryTelCode;
	}

	public void setCountryTelCode(String countryTelCode) {
		this.countryTelCode = countryTelCode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public BigDecimal getRelationsId() {
		return relationsId;
	}

	public void setRelationsId(BigDecimal relationsId) {
		this.relationsId = relationsId;
	}

	public BigDecimal getStateId() {
		return stateId;
	}

	public void setStateId(BigDecimal stateId) {
		this.stateId = stateId;
	}

	public BigDecimal getCityId() {
		return cityId;
	}

	public void setCityId(BigDecimal cityId) {
		this.cityId = cityId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getDistrictId() {
		return districtId;
	}

	public void setDistrictId(BigDecimal districtId) {
		this.districtId = districtId;
	}

	@Override
	public String toString() {
		return "BenePersonalDetailModel [stateId=" + stateId + ", cityId=" + cityId + ", countryId=" + countryId
				+ ", districtId=" + districtId + ", firstName=" + firstName + ", secondName=" + secondName
				+ ", thirdName=" + thirdName + ", fourthName=" + fourthName + ", fifthName=" + fifthName
				+ ", nationality=" + nationality + ", localFirstName=" + localFirstName + ", localSecondName="
				+ localSecondName + ", localThirdName=" + localThirdName + ", localFourthName=" + localFourthName
				+ ", localFifthName=" + localFifthName + ", telephoneNumber=" + telephoneNumber + ", mobileNumber="
				+ mobileNumber + ", countryTelCode=" + countryTelCode + ", remarks=" + remarks + ", relationsId="
				+ relationsId + "]";
	}

}
