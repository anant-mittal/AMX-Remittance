package com.amx.jax.model.request;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.NumberFormat;

import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.model.request.benebranch.BenePersonalDetailModel;
import com.amx.jax.model.request.benebranch.BeneficiaryTrnxModel;
import com.amx.jax.swagger.ApiMockModelProperty;

public abstract class AbstractBeneDetailDto {
	// bene details
	@NotNull
	@ApiMockModelProperty(example = "1")
	BigDecimal benificaryStatusId;

	@NotNull(message = "First Name may not be null")
	@Size(min = 1, max = 50, message = "firstName should be between 1 and 50 characters")
	@ApiMockModelProperty(example = "testbene")
	private String firstName;

	@NotNull
	@Size(min = 0, max = 50, message = "secondName should be between 0 and 50 characters")
	@ApiMockModelProperty(example = "testbene")
	private String secondName;

	@Size(min = 1, max = 50, message = "thirdName should be between 1 and 50 characters")
	private String thirdName;

	@Size(min = 1, max = 50, message = "fourthName should be between 1 and 50 characters")
	private String fourthName;

	@Size(min = 0, max = 50, message = "fifthName should be between 0 and 50 characters")
	private String fiftheName;

	@Size(min = 1, max = 50, message = "localFirstName should be between 1 and 50 characters")
	private String firstNameLocal;

	@Size(min = 1, max = 50, message = "localSecondName should be between 1 and 50 characters")
	private String secondNameLocal;

	@Size(min = 1, max = 50, message = "localThirdName should be between 1 and 50 characters")
	private String thirdNameLocal;

	@Size(min = 1, max = 50, message = "localFourthName should be between 1 and 50 characters")
	private String fourthNameLocal;

	@Size(min = 0, max = 50, message = "localFifthName should be between 0 and 50 characters")
	private String fifthNameLocal;

	@NotNull(message = "nationality may not be null")
	@ApiMockModelProperty(example = "94")
	private BigDecimal nationality;

	@NotNull
	@ApiMockModelProperty(example = "5")
	private BigDecimal relationShipId;
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

	@NotNull(message = "serviceGroupId may not be null")
	@ApiMockModelProperty(example = "2")
	private BigDecimal serviceGroupId;

	@ApiMockModelProperty(example = "HDFCINBBAHM")
	String swiftBic;

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
	
	public BigDecimal getNationality() {
		return nationality;
	}

	public void setNationality(BigDecimal nationality) {
		this.nationality = nationality;
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

	protected BenePersonalDetailModel createPersonalDetailObject() {
		BenePersonalDetailModel model = new BenePersonalDetailModel();
		try {
			BeanUtils.copyProperties(model, this);
			model.setFifthName(this.getFiftheName());
			model.setLocalFifthName(this.getFifthNameLocal());
			model.setLocalFirstName(this.getFirstNameLocal());
			model.setLocalFourthName(this.getFourthNameLocal());
			model.setLocalSecondName(this.getSecondNameLocal());
			model.setLocalThirdName(this.getThirdNameLocal());
			model.setRelationsId(this.getRelationShipId());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return model;
	}

	public BeneficiaryTrnxModel createBeneficiaryTrnxModelObject() {
		BeneficiaryTrnxModel model = new BeneficiaryTrnxModel();
		model.setBeneAccountModel(createBeneAccountModelObject());
		model.setBenePersonalDetailModel(createPersonalDetailObject());
		return model;
	}

	protected abstract BeneAccountModel createBeneAccountModelObject();

	public BigDecimal getServiceGroupId() {
		return serviceGroupId;
	}

	public void setServiceGroupId(BigDecimal serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}

	public BigDecimal getBenificaryStatusId() {
		return benificaryStatusId;
	}

	public void setBenificaryStatusId(BigDecimal benificaryStatusId) {
		this.benificaryStatusId = benificaryStatusId;
	}

	public String getFiftheName() {
		return fiftheName;
	}

	public void setFiftheName(String fiftheName) {
		this.fiftheName = fiftheName;
	}

	public String getFirstNameLocal() {
		return firstNameLocal;
	}

	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}

	public String getSecondNameLocal() {
		return secondNameLocal;
	}

	public void setSecondNameLocal(String secondNameLocal) {
		this.secondNameLocal = secondNameLocal;
	}

	public String getThirdNameLocal() {
		return thirdNameLocal;
	}

	public void setThirdNameLocal(String thirdNameLocal) {
		this.thirdNameLocal = thirdNameLocal;
	}

	public String getFourthNameLocal() {
		return fourthNameLocal;
	}

	public void setFourthNameLocal(String fourthNameLocal) {
		this.fourthNameLocal = fourthNameLocal;
	}

	public String getFifthNameLocal() {
		return fifthNameLocal;
	}

	public void setFifthNameLocal(String fifthNameLocal) {
		this.fifthNameLocal = fifthNameLocal;
	}

	public BigDecimal getRelationShipId() {
		return relationShipId;
	}

	public void setRelationShipId(BigDecimal relationShipId) {
		this.relationShipId = relationShipId;
	}

	public String getSwiftBic() {
		return swiftBic;
	}

	public void setSwiftBic(String swiftBic) {
		this.swiftBic = swiftBic;
	}

}
