package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FS_CUSTOMER_EMPLOYMENT_INFO")
public class EmployeeDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal custEmpInfoId;
	private Customer fsCustomer;
	private CountryMaster fsCountryMaster;
	private BigDecimal fsCompanyMaster;
	private BigDecimal fsLanguageType;
	private BizComponentData fsBizComponentDataByEmploymentTypeId;
	private BizComponentData fsBizComponentDataByOccupationId;
	private String employerName;
	private String department;
	private BigDecimal fsDistrictMaster;
	private BigDecimal fsStateMaster;
	private BigDecimal fsCityMaster;
	private String area;
	private String block;
	private String street;
	private String postal;
	private String officeTelephone;
	private String createdBy;
	private String updatedBy;
	private Date creationDate;
	private Date lastUpdated;
	private String isActive;

	public EmployeeDetails() {
	}
	

	@Id	
	@GeneratedValue(generator="fs_customer_emp_info_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="fs_customer_emp_info_seq" ,sequenceName="FS_CUSTOMER_EMP_INFO_SEQ",allocationSize=1)
	@Column(name = "CUST_EMP_INFO_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getCustEmpInfoId() {
		return this.custEmpInfoId;
	}

	public void setCustEmpInfoId(BigDecimal custEmpInfoId) {
		this.custEmpInfoId = custEmpInfoId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	public Customer getFsCustomer() {
		return this.fsCustomer;
	}

	public void setFsCustomer(Customer fsCustomer) {
		this.fsCustomer = fsCustomer;
	}

	//@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "LANGUAGE_ID")
	public BigDecimal getFsLanguageType() {
		return this.fsLanguageType;
	}

	public void setFsLanguageType(BigDecimal fsLanguageType) {
		this.fsLanguageType = fsLanguageType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYMENT_TYPE_ID")
	public BizComponentData getFsBizComponentDataByEmploymentTypeId() {
		return fsBizComponentDataByEmploymentTypeId;
	}

	public void setFsBizComponentDataByEmploymentTypeId(BizComponentData fsBizComponentDataByEmploymentTypeId) {
		this.fsBizComponentDataByEmploymentTypeId = fsBizComponentDataByEmploymentTypeId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OCCUPATION_ID")
	public BizComponentData getFsBizComponentDataByOccupationId() {
		return fsBizComponentDataByOccupationId;
	}

	public void setFsBizComponentDataByOccupationId(BizComponentData fsBizComponentDataByOccupationId) {
		this.fsBizComponentDataByOccupationId = fsBizComponentDataByOccupationId;
	}

	@Column(name = "EMPLOYER_NAME", length = 200)
	public String getEmployerName() {
		return this.employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	@Column(name = "DEPARTMENT", length = 200)
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	//@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "DISTRICT_ID")
	public BigDecimal getFsDistrictMaster() {
		return this.fsDistrictMaster;
	}

	public void setFsDistrictMaster(BigDecimal fsDistrictMaster) {
		this.fsDistrictMaster = fsDistrictMaster;
	}
	
	//@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "STATE_ID")
	public BigDecimal getFsStateMaster() {
		return this.fsStateMaster;
	}

	public void setFsStateMaster(BigDecimal fsStateMaster) {
		this.fsStateMaster = fsStateMaster;
	}
	
	//@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "CITY_ID")
	public BigDecimal getFsCityMaster() {
		return this.fsCityMaster;
	}

	public void setFsCityMaster(BigDecimal fsCityMaster) {
		this.fsCityMaster = fsCityMaster;
	}

	@Column(name = "AREA", length = 100)
	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Column(name = "BLOCK", length = 100)
	public String getBlock() {
		return this.block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	@Column(name = "STREET", length = 100)
	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(name = "POSTAL", length = 100)
	public String getPostal() {
		return this.postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	@Column(name = "OFFICE_TELEPHONE", length = 20)
	public String getOfficeTelephone() {
		return this.officeTelephone;
	}

	public void setOfficeTelephone(String officeTelephone) {
		this.officeTelephone = officeTelephone;
	}

	@Column(name = "CREATED_BY", length = 200)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "UPDATED_BY", length = 200)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "CREATION_DATE")
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "LAST_UPDATED")
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_ID")
	public CountryMaster getFsCountryMaster() {
		return fsCountryMaster;
	}

	public void setFsCountryMaster(CountryMaster fsCountryMaster) {
		this.fsCountryMaster = fsCountryMaster;
	}
	

	//@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "COMPANY_ID")
	public BigDecimal getFsCompanyMaster() {
		return fsCompanyMaster;
	}

	public void setFsCompanyMaster(BigDecimal fsCompanyMaster) {
		this.fsCompanyMaster = fsCompanyMaster;
	}

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}}
