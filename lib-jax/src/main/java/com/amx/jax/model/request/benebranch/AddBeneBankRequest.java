package com.amx.jax.model.request.benebranch;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.NumberFormat;

import com.amx.jax.swagger.ApiMockModelProperty;

public class AddBeneBankRequest {

	// account detail
	@NotNull(message = "Beneficary Country Id may not be null")
	@ApiMockModelProperty(example = "91")
	private BigDecimal beneficaryCountryId;
	@NotNull(message = "Bank Id may not be null")
	@ApiMockModelProperty(example = "2258")
	private BigDecimal bankId;
	@NotNull(message = "Bank branch may not be null")
	@ApiMockModelProperty(example = "247822")
	private BigDecimal bankBranchId;
	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "Invalid Account Number, only alphanumeric allowed")
	@NotNull
	@ApiMockModelProperty(example = "67543227825")
	private String bankAccountNumber;
	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "Invalid ibanNumber, only alphanumeric allowed")
	@Size(min = 1, max = 60)
	private String ibanNumber;
	@NotNull(message = "Currency Id may not be null")
	@ApiMockModelProperty(example = "11")
	private BigDecimal currencyId;
	@NotNull(message = "bankAccountTypeId may not be null")
	@ApiMockModelProperty(example = "4")
	private BigDecimal bankAccountTypeId;

	// bene details
	@NotNull
	@ApiMockModelProperty(example = "Individual")
	String beneficaryType;
	@NotNull(message = "First Name may not be null")
	@Size(min = 1, max = 50, message = "firstName should be between 1 and 50 characters")
	@ApiMockModelProperty(example = "testbene")
	private String firstName;
	@NotNull
	@Size(min = 1, max = 50, message = "secondName should be between 1 and 50 characters")
	@ApiMockModelProperty(example = "testbene")
	private String secondName;
	@Size(min = 1, max = 50, message = "thirdName should be between 1 and 50 characters")
	private String thirdName;
	@Size(min = 1, max = 50, message = "fourthName should be between 1 and 50 characters")
	private String fourthName;
	@Size(min = 0, max = 50, message = "fifthName should be between 0 and 50 characters")
	private String fifthName;
	@Size(min = 1, max = 50, message = "localFirstName should be between 1 and 50 characters")
	private String localFirstName;
	@Size(min = 1, max = 50, message = "localSecondName should be between 1 and 50 characters")
	private String localSecondName;
	@Size(min = 1, max = 50, message = "localThirdName should be between 1 and 50 characters")
	private String localThirdName;
	@Size(min = 1, max = 50, message = "localFourthName should be between 1 and 50 characters")
	private String localFourthName;
	@Size(min = 0, max = 50, message = "localFifthName should be between 0 and 50 characters")
	private String localFifthName;
	@NotNull(message = "nationality may not be null")
	@ApiMockModelProperty(example = "94")
	private BigDecimal nationality;
	@NotNull
	@ApiMockModelProperty(example = "5")
	private BigDecimal relationsId;
	// TODO: add dob fields

	// bene contact
	// bene master
	@NotNull(message = "State Id may not be null")
	@ApiMockModelProperty(example = "585")
	private BigDecimal stateId;
	@NotNull(message = "District Id may not be null")
	@ApiMockModelProperty(example = "4166")
	private BigDecimal districtId;
	@NotNull(message = "Country Id may not be null")
	@ApiMockModelProperty(example = "92")
	private BigDecimal countryId;
	@NumberFormat
	@NotNull
	@ApiMockModelProperty(example = "1234568751")
	private String mobileNumber;
	@NotNull
	@ApiMockModelProperty(example = "91")
	private String countryTelCode;
	public BigDecimal getBeneficaryCountryId() {
		return beneficaryCountryId;
	}
	public void setBeneficaryCountryId(BigDecimal beneficaryCountryId) {
		this.beneficaryCountryId = beneficaryCountryId;
	}
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}
	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getIbanNumber() {
		return ibanNumber;
	}
	public void setIbanNumber(String ibanNumber) {
		this.ibanNumber = ibanNumber;
	}
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}
	public BigDecimal getBankAccountTypeId() {
		return bankAccountTypeId;
	}
	public void setBankAccountTypeId(BigDecimal bankAccountTypeId) {
		this.bankAccountTypeId = bankAccountTypeId;
	}
	public String getBeneficaryType() {
		return beneficaryType;
	}
	public void setBeneficaryType(String beneficaryType) {
		this.beneficaryType = beneficaryType;
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
	public BigDecimal getNationality() {
		return nationality;
	}
	public void setNationality(BigDecimal nationality) {
		this.nationality = nationality;
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
	public BigDecimal getDistrictId() {
		return districtId;
	}
	public void setDistrictId(BigDecimal districtId) {
		this.districtId = districtId;
	}
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getCountryTelCode() {
		return countryTelCode;
	}
	public void setCountryTelCode(String countryTelCode) {
		this.countryTelCode = countryTelCode;
	}

	
	
}
