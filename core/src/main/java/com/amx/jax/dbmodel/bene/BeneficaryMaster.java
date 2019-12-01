package com.amx.jax.dbmodel.bene;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EX_BENEFICARY_MASTER")
public class BeneficaryMaster implements Serializable {

	private static final long serialVersionUID = 2315791709068216697L;

	private BigDecimal beneficaryMasterSeqId;
	private BigDecimal applicationCountryId;
	private BigDecimal fsStateMaster;
	private BigDecimal fsCityMaster;
	private BigDecimal fsCountryMaster;
	private BigDecimal fsDistrictMaster;
	private BigDecimal beneficaryStatus;
	private String firstName;
	private String secondName;
	private String thirdName;
	private String fourthName;
	private String fifthName;
	private String nationality;
	private Date dateOfBrith;
	private BigDecimal yearOfBrith;
	private BigDecimal age;
	private String occupation;
	private String noOfRemittance;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String cityName;
	private String stateName;
	private String districtName;
	private String beneficaryStatusName;

	private String localFirstName;
	private String localSecondName;
	private String localThirdName;
	private String localFourthName;
	private String localFifthName;
	
	private String buildingNo;
	private String flatNo;
	private String streetNo;
	private String beneficiaryZipCode;
	
	@Id
	@GeneratedValue(generator = "ex_beneficary_master_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_beneficary_master_seq", sequenceName = "EX_BENEFICARY_MASTER_SEQ", allocationSize = 1)
	@Column(name = "BENEFICARY_MASTER_SEQ_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getBeneficaryMasterSeqId() {
		return beneficaryMasterSeqId;
	}

	public void setBeneficaryMasterSeqId(BigDecimal beneficaryMasterSeqId) {
		this.beneficaryMasterSeqId = beneficaryMasterSeqId;
	}

	/**
	 * @return the applicationCountryId
	 */
	
	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}


	@Column(name = "STATE_ID")
	public BigDecimal getFsStateMaster() {
		return this.fsStateMaster;
	}

	public void setFsStateMaster(BigDecimal fsStateMaster) {
		this.fsStateMaster = fsStateMaster;
	}

	
	@Column(name = "CITY_ID")
	public BigDecimal getFsCityMaster() {
		return this.fsCityMaster;
	}

	public void setFsCityMaster(BigDecimal fsCityMaster) {
		this.fsCityMaster = fsCityMaster;
	}

	
	@Column(name = "COUNTRY_ID")
	public BigDecimal getFsCountryMaster() {
		return this.fsCountryMaster;
	}

	public void setFsCountryMaster(BigDecimal fsCountryMaster) {
		this.fsCountryMaster = fsCountryMaster;
	}

	
	@Column(name = "DISTRICT_ID")
	public BigDecimal getFsDistrictMaster() {
		return fsDistrictMaster;
	}

	public void setFsDistrictMaster(BigDecimal fsDistrictMaster) {
		this.fsDistrictMaster = fsDistrictMaster;
	}

	/**
	 * @return the firstName
	 */
	@Column(name = "FIRST_NAME")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the secondName
	 */
	@Column(name = "SECOND_NAME")
	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	/**
	 * @return the thirdName
	 */
	@Column(name = "THIRD_NAME")
	public String getThirdName() {
		return thirdName;
	}

	public void setThirdName(String thirdName) {
		this.thirdName = thirdName;
	}

	/**
	 * @return the fourthName
	 */
	@Column(name = "FOURTH_NAME")
	public String getFourthName() {
		return fourthName;
	}

	public void setFourthName(String fourthName) {
		this.fourthName = fourthName;
	}

	/**
	 * @return the fifthName
	 */
	@Column(name = "FIFTH_NAME")
	public String getFifthName() {
		return fifthName;
	}

	public void setFifthName(String fifthName) {
		this.fifthName = fifthName;
	}

	/**
	 * @return the beneficaryStatus
	 */

	@Column(name = "BENEFICARY_STATUS_ID")
	public BigDecimal getBeneficaryStatus() {
		return beneficaryStatus;
	}

	public void setBeneficaryStatus(BigDecimal beneficaryStatus) {
		this.beneficaryStatus = beneficaryStatus;
	}

	/**
	 * @return the nationality
	 */
	@Column(name = "NATIONALITY")
	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return the dateOfBrith
	 */
	@Column(name = "DATE_OF_BIRTH")
	public Date getDateOfBrith() {
		return dateOfBrith;
	}

	public void setDateOfBrith(Date dateOfBrith) {
		this.dateOfBrith = dateOfBrith;
	}

	/**
	 * @return the yearOfBrith
	 */
	@Column(name = "YEAR_OF_BIRTH")
	public BigDecimal getYearOfBrith() {
		return yearOfBrith;
	}

	public void setYearOfBrith(BigDecimal yearOfBrith) {
		this.yearOfBrith = yearOfBrith;
	}

	/**
	 * @return the age
	 */
	@Column(name = "AGE")
	public BigDecimal getAge() {
		return age;
	}

	public void setAge(BigDecimal age) {
		this.age = age;
	}

	/**
	 * @return the occupation
	 */
	@Column(name = "OCCUPATION")
	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	/**
	 * @return the noOfRemittance
	 */
	@Column(name = "NO_OF_REMITTANCE")
	public String getNoOfRemittance() {
		return noOfRemittance;
	}

	public void setNoOfRemittance(String noOfRemittance) {
		this.noOfRemittance = noOfRemittance;
	}

	/**
	 * @return the isActive
	 */
	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the createdBy
	 */
	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedBy
	 */
	@Column(name = "MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modifiedDate
	 */
	@Column(name = "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "CITY_NAME")
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Column(name = "STATE_NAME")
	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Column(name = "DISTRICT_NAME")
	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	@Column(name = "BENEFICARY_STATUS_NAME")
	public String getBeneficaryStatusName() {
		return beneficaryStatusName;
	}

	public void setBeneficaryStatusName(String beneficaryStatusName) {
		this.beneficaryStatusName = beneficaryStatusName;
	}

	@Column(name = "FIRST_NAME_LOCAL")
	public String getLocalFirstName() {
		return localFirstName;
	}

	public void setLocalFirstName(String localFirstName) {
		this.localFirstName = localFirstName;
	}

	@Column(name = "SECOND_NAME_LOCAL")
	public String getLocalSecondName() {
		return localSecondName;
	}

	public void setLocalSecondName(String localSecondName) {
		this.localSecondName = localSecondName;
	}

	@Column(name = "THIRD_NAME_LOCAL")
	public String getLocalThirdName() {
		return localThirdName;
	}

	public void setLocalThirdName(String localThirdName) {
		this.localThirdName = localThirdName;
	}

	@Column(name = "FOURTH_NAME_LOCAL")
	public String getLocalFourthName() {
		return localFourthName;
	}

	public void setLocalFourthName(String localFourthName) {
		this.localFourthName = localFourthName;
	}
	
	@Column(name = "BUILDING_NO")
	public String getBuildingNo() {
		return buildingNo;
	}

	public void setBuildingNo(String buildingNo) {
		this.buildingNo = buildingNo;
	}
	
	@Column(name = "FLAT")
	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}
	
	@Column(name = "STREET")
	public String getStreetNo() {
		return streetNo;
	}

	public void setStreetNo(String streetNo) {
		this.streetNo = streetNo;
	}

	@Column(name="FIFTH_NAME_LOCAL")
	public String getLocalFifthName() {
		return localFifthName;
	}

	public void setLocalFifthName(String localFifthName) {
		this.localFifthName = localFifthName;
	}

	@Column(name="BENEFICARY_ZIP_CODE")
	public String getBeneficiaryZipCode() {
		return beneficiaryZipCode;
	}

	public void setBeneficiaryZipCode(String beneficiaryZipCode) {
		this.beneficiaryZipCode = beneficiaryZipCode;
	}
	
}
