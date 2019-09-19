package com.amx.jax.model.request;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.NumberFormat;

import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.model.request.benebranch.BenePersonalDetailModel;
import com.amx.jax.model.request.benebranch.BeneficiaryTrnxModel;
import com.amx.jax.swagger.ApiMockModelProperty;

public abstract class AbtractUpdateBeneDetailDto {

	@NotNull
	@ApiMockModelProperty(example = "19340")
	Integer idNo;

	@ApiMockModelProperty(example = "1")
	BigDecimal beneficaryTypeId;

	@Size(min = 1, max = 50, message = "firstName should be between 1 and 50 characters")
	@ApiMockModelProperty(example = "testbene")
	private String firstName;

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

	@ApiMockModelProperty(example = "94")
	private BigDecimal nationality;

	@ApiMockModelProperty(example = "5")
	private BigDecimal relationsId;
	// TODO: add dob fields

	// bene contact
	// bene master
	@ApiMockModelProperty(example = "585")
	private BigDecimal stateId;

	@ApiMockModelProperty(example = "4166")
	private BigDecimal districtId;

	@ApiMockModelProperty(example = "92")
	private BigDecimal countryId;

	@NumberFormat
	@ApiMockModelProperty(example = "1234568751")
	private String mobileNumber;

	@ApiMockModelProperty(example = "91")
	private String countryTelCode;

	@ApiMockModelProperty(example = "2")
	private BigDecimal serviceGroupId;

	@ApiMockModelProperty(example = "HDFCINBBAHM")
	String swiftCode;

	Date dateOfBirth;

	@ApiMockModelProperty(example = "25")
	@Min(1)
	Integer age;

	@ApiMockModelProperty(example = "1986")
	@Min(1900)
	Integer yearOfBirth;

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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(Integer yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public Integer getIdNo() {
		return idNo;
	}

	public void setIdNo(Integer idNo) {
		this.idNo = idNo;
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

	public BigDecimal getBeneficaryTypeId() {
		return beneficaryTypeId;
	}

	public void setBeneficaryTypeId(BigDecimal beneficaryTypeId) {
		this.beneficaryTypeId = beneficaryTypeId;
	}
}
