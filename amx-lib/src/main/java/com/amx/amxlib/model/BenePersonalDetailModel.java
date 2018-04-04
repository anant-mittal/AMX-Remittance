package com.amx.amxlib.model;

import java.math.BigDecimal;

public class BenePersonalDetailModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// bene master
	private BigDecimal stateId;
	private BigDecimal cityId;
	private BigDecimal countryId;
	private BigDecimal districtId;
	private String firstName;
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
	private BigDecimal mobileNumber;
	private String countryTelCode;
	// bene relationship
	private String remarks;
	private BigDecimal relationsId;

	@Override
	public String getModelType() {
		return "bene-personal-details";
	}

	public BigDecimal getFsStateId() {
		return stateId;
	}

	public void setFsStateId(BigDecimal fsStateId) {
		this.stateId = fsStateId;
	}

	public BigDecimal getFsCityId() {
		return cityId;
	}

	public void setFsCityId(BigDecimal fsCityId) {
		this.cityId = fsCityId;
	}

	public BigDecimal getFsCountryId() {
		return countryId;
	}

	public void setFsCountryId(BigDecimal fsCountryId) {
		this.countryId = fsCountryId;
	}

	public BigDecimal getFsDistrictId() {
		return districtId;
	}

	public void setFsDistrictId(BigDecimal fsDistrictId) {
		this.districtId = fsDistrictId;
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

}
