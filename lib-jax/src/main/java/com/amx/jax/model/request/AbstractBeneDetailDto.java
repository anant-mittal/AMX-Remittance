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
	BigDecimal beneficaryTypeId;

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

	@NotNull(message = "serviceGroupId may not be null")
	@ApiMockModelProperty(example = "2")
	private BigDecimal serviceGroupId;

	@ApiMockModelProperty(example = "HDFCINBBAHM")
	String swiftCode;

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

	protected BenePersonalDetailModel createPersonalDetailObject() {
		BenePersonalDetailModel model = new BenePersonalDetailModel();
		try {
			BeanUtils.copyProperties(model, this);
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

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public BigDecimal getBeneficaryTypeId() {
		return beneficaryTypeId;
	}

	public void setBeneficaryTypeId(BigDecimal beneficaryTypeId) {
		this.beneficaryTypeId = beneficaryTypeId;
	}
}
