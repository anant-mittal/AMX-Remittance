package com.amx.jax.model.request.benebranch;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;

import com.amx.jax.model.AbstractModel;

public class BenePersonalDetailModel extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// bene master
	@NotNull(message="State Id may not be null")
	private BigDecimal stateId;
	private BigDecimal cityId;
	@NotNull(message="Country Id may not be null")
	private BigDecimal countryId;
	@NotNull(message="District Id may not be null")
	private BigDecimal districtId;
	
	@Size(min=1, max=50, message="firstName should be between 1 and 50 characters")
	private String firstName;
	
	@Size(min=1, max=50, message="secondName should be between 1 and 50 characters")
	private String secondName;
	@Size(min=1, max=50, message="thirdName should be between 1 and 50 characters")
	private String thirdName;
	@Size(min=1, max=50, message="fourthName should be between 1 and 50 characters")
	private String fourthName;
	@Size(min=0, max=50, message="fifthName should be between 0 and 50 characters")
	private String fifthName;
	private String nationality;
	@Size(min=1, max=50, message="localFirstName should be between 1 and 50 characters")
	private String localFirstName;
	@Size(min=1, max=50, message="localSecondName should be between 1 and 50 characters")
	private String localSecondName;
	@Size(min=1, max=50, message="localThirdName should be between 1 and 50 characters")
	private String localThirdName;
	@Size(min=1, max=50, message="localFourthName should be between 1 and 50 characters")
	private String localFourthName;
	@Size(min=0, max=50, message="localFifthName should be between 0 and 50 characters")
	private String localFifthName;
	// bene contact
	private String telephoneNumber;
	
	@NumberFormat
	private String mobileNumber;
	@NotNull
	private String countryTelCode;
	// bene relationship
	private String remarks;
	
	private BigDecimal relationsId;
	private BigDecimal beneficaryTypeId;
	
	@Size(min = 1, max = 50, message = "institutionName should be between 1 and 50 characters")
	@Pattern(regexp = "^[A-Za-z\\s\\d,.'-]+$", message = "Invalid institutionName, only alphabets allowed")
	private String institutionName;
	
	@Size(min = 1, max = 50, message = "institutionName local should be between 1 and 50 characters")
	@Pattern(regexp="^[\\u0621-\\u064A0-9 ]+$")
	private String institutionNameLocal;

	private BigDecimal institutionCategoryId;

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
	
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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

	public BigDecimal getBeneficaryTypeId() {
		return beneficaryTypeId;
	}

	public void setBeneficaryTypeId(BigDecimal beneficaryTypeId) {
		this.beneficaryTypeId = beneficaryTypeId;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public BigDecimal getInstitutionCategoryId() {
		return institutionCategoryId;
	}

	public void setInstitutionCategoryId(BigDecimal institutionCategoryId) {
		this.institutionCategoryId = institutionCategoryId;
	}

	public String getInstitutionNameLocal() {
		return institutionNameLocal;
	}

	public void setInstitutionNameLocal(String institutionNameLocal) {
		this.institutionNameLocal = institutionNameLocal;
	}

}
