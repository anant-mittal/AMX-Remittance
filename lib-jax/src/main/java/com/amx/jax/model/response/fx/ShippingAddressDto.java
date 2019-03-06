package com.amx.jax.model.response.fx;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.amx.jax.model.ResourceDTO;

public class ShippingAddressDto {

	@NotNull(message = "addressId may not be null")
	private BigDecimal addressId;
	@NotNull(message = "customer may not be null")
	private BigDecimal customerId;
	private BigDecimal companyId;
	private BigDecimal countryId;
	private BigDecimal languageId;
	private String shortName;
	private String shortNameLocal;
	private String amlStatus;

	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String titleLocal;
	private String firstNameLocal;
	private String middleNameLocal;
	private String lastNameLocal;
	private String alterEmailId;
	@NotNull(message = "Mobile may not be null")
	@Pattern(regexp = "^[1-8]\\d*$", message = "Invalid Mobile No")
	@Size(min = 8)
	private String mobile;
	private String companyName;
	private String companyNameLocal;
	private String email;

	private String fatherName;

	private String contactPerson;
	private BigDecimal contactNumber;
	private String isActive;
	private String engFullName;

	/** Local Contact details **/
	private String localContactCountry;
	private String localContactState;
	private String localContactDistrict;
	private String localContactCity;
	private String localContactBuilding;
	private String localContatFlat;

	private String street;
	private String house;
	private String block;
	private String adressType;
	private String areaDesc;
	private String flat;
	private String telephone;
	private String buildingNo;
	/** country telephone prefix */
	@NotNull(message = "telPrefix may not be null")
	@Pattern(regexp = "^[1-9]\\d*$", message = "Invalid Tele Prefix")
	private String telephoneCode;

	private ResourceDTO cityDto;
	private ResourceDTO stateDto;
	private ResourceDTO districtDto;
	@NotNull(message = "Country Id may not be null")
	private ResourceDTO countryDto;
	@NotNull(message = "Address type Id may not be null")
	private AddressTypeDto addressDto;
	
	private ResourceDTO areaDto;
	private String deliveryAddress;
	private ResourceDTO governoatesDto;
	private ResourceDTO governoateAreaDto;
	private String govtAreaDesc;
	private String govtDesc;
	private Boolean isDefault = Boolean.FALSE;
	

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortNameLocal() {
		return shortNameLocal;
	}

	public void setShortNameLocal(String shortNameLocal) {
		this.shortNameLocal = shortNameLocal;
	}

	public String getAmlStatus() {
		return amlStatus;
	}

	public void setAmlStatus(String amlStatus) {
		this.amlStatus = amlStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitleLocal() {
		return titleLocal;
	}

	public void setTitleLocal(String titleLocal) {
		this.titleLocal = titleLocal;
	}

	public String getFirstNameLocal() {
		return firstNameLocal;
	}

	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}

	public String getMiddleNameLocal() {
		return middleNameLocal;
	}

	public void setMiddleNameLocal(String middleNameLocal) {
		this.middleNameLocal = middleNameLocal;
	}

	public String getLastNameLocal() {
		return lastNameLocal;
	}

	public void setLastNameLocal(String lastNameLocal) {
		this.lastNameLocal = lastNameLocal;
	}

	public String getAlterEmailId() {
		return alterEmailId;
	}

	public void setAlterEmailId(String alterEmailId) {
		this.alterEmailId = alterEmailId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyNameLocal() {
		return companyNameLocal;
	}

	public void setCompanyNameLocal(String companyNameLocal) {
		this.companyNameLocal = companyNameLocal;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public BigDecimal getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(BigDecimal contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getEngFullName() {
		return engFullName;
	}

	public void setEngFullName(String engFullName) {
		this.engFullName = engFullName;
	}

	public String getLocalContactCountry() {
		return localContactCountry;
	}

	public void setLocalContactCountry(String localContactCountry) {
		this.localContactCountry = localContactCountry;
	}

	public String getLocalContactState() {
		return localContactState;
	}

	public void setLocalContactState(String localContactState) {
		this.localContactState = localContactState;
	}

	public String getLocalContactDistrict() {
		return localContactDistrict;
	}

	public void setLocalContactDistrict(String localContactDistrict) {
		this.localContactDistrict = localContactDistrict;
	}

	public String getLocalContactCity() {
		return localContactCity;
	}

	public void setLocalContactCity(String localContactCity) {
		this.localContactCity = localContactCity;
	}

	public String getLocalContactBuilding() {
		return localContactBuilding;
	}

	public void setLocalContactBuilding(String localContactBuilding) {
		this.localContactBuilding = localContactBuilding;
	}

	public String getLocalContatFlat() {
		return localContatFlat;
	}

	public void setLocalContatFlat(String localContatFlat) {
		this.localContatFlat = localContatFlat;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public String getBlockNo() {
		return block;
	}

	public void setBlockNo(String blockNo) {
		this.block = blockNo;
	}

	public String getAdressType() {
		return adressType;
	}

	public void setAdressType(String adressType) {
		this.adressType = adressType;
	}

	public BigDecimal getAddressId() {
		return addressId;
	}

	public void setAddressId(BigDecimal addressId) {
		this.addressId = addressId;
	}

	public String getAreaDesc() {
		return areaDesc;
	}

	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getFlat() {
		return flat;
	}

	public void setFlat(String flat) {
		this.flat = flat;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getBuildingNo() {
		return buildingNo;
	}

	public void setBuildingNo(String buildingNo) {
		this.buildingNo = buildingNo;
	}

	public String getTelephoneCode() {
		return telephoneCode;
	}

	public void setTelephoneCode(String telephoneCode) {
		this.telephoneCode = telephoneCode;
	}

	/*
	 * public String getAddressType() { return addressType; }
	 * 
	 * public void setAddressType(String addressType) { this.addressType =
	 * addressType; }
	 */

	public ResourceDTO getCityDto() {
		return cityDto;
	}

	public void setCityDto(ResourceDTO cityDto) {
		this.cityDto = cityDto;
	}

	public ResourceDTO getStateDto() {
		return stateDto;
	}

	public void setStateDto(ResourceDTO stateDto) {
		this.stateDto = stateDto;
	}

	public ResourceDTO getDistrictDto() {
		return districtDto;
	}

	public void setDistrictDto(ResourceDTO districtDto) {
		this.districtDto = districtDto;
	}

	public ResourceDTO getCountryDto() {
		return countryDto;
	}

	public void setCountryDto(ResourceDTO countryDto) {
		this.countryDto = countryDto;
	}

	public AddressTypeDto getAddressDto() {
		return addressDto;
	}

	public void setAddressDto(AddressTypeDto addressDto) {
		this.addressDto = addressDto;
	}

	public ResourceDTO getAreaDto() {
		return areaDto;
	}

	public void setAreaDto(ResourceDTO areaDto) {
		this.areaDto = areaDto;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public ResourceDTO getGovernoatesDto() {
		return governoatesDto;
	}

	public void setGovernoatesDto(ResourceDTO governoatesDto) {
		this.governoatesDto = governoatesDto;
	}

	public ResourceDTO getGovernoateAreaDto() {
		return governoateAreaDto;
	}

	public void setGovernoateAreaDto(ResourceDTO governoateAreaDto) {
		this.governoateAreaDto = governoateAreaDto;
	}

	public String getGovtAreaDesc() {
		return govtAreaDesc;
	}

	public void setGovtAreaDesc(String govtAreaDesc) {
		this.govtAreaDesc = govtAreaDesc;
	}

	public String getGovtDesc() {
		return govtDesc;
	}

	public void setGovtDesc(String govtDesc) {
		this.govtDesc = govtDesc;
	}
}
