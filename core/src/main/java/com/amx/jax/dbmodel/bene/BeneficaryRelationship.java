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
@Table(name="EX_BENEFICARY_RELATIONSHIP")
public class BeneficaryRelationship implements Serializable {
	
	private static final long serialVersionUID = 2315791709068216697L;


	private BigDecimal beneficaryRelationshipId;
	private BigDecimal applicationCountry;
	private BigDecimal customerId;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private BigDecimal beneficaryMasterId;
	private BigDecimal beneficaryAccountId;
	private BigDecimal mapSequenceId;
	private String remarks ;
	private BigDecimal orsSatus;
	private String myFavouriteBene;
	private BigDecimal relationsId;
	private String firstName;
	private String secondName;
	private String thirdName;
	private String fourthName;
	private String fifthName;
	private String localFirstName;
	private String localSecondName;
	private String localThirdName;
	private String localFourthName;

	private String localFifthName;
	private String deviceIp;
	private String deviceType;
	private BigDecimal reasonCodeId;		


	@Id
	@GeneratedValue(generator="ex_beneficary_relationship_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_beneficary_relationship_seq",sequenceName="EX_BENEFICARY_RELATIONSHIP_SEQ",allocationSize=1)
	@Column(name="BENEFICARY_RELATIONSHIP_SEQ_ID",unique=true, nullable=false, precision=22, scale=0)
	public BigDecimal getBeneficaryRelationshipId() {
		return beneficaryRelationshipId;
	}
	
	public void setBeneficaryRelationshipId(BigDecimal beneficaryRelationshipId) {
		this.beneficaryRelationshipId = beneficaryRelationshipId;
	}

	@Column(name="APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountry() {
		return applicationCountry;
	}
	
	public void setApplicationCountry(BigDecimal applicationCountry) {
		this.applicationCountry = applicationCountry;
	}
	
	
	@Column(name="CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	
	@Column(name="BENEFICARY_MASTER_SEQ_ID")
	public BigDecimal getBeneficaryMasterId() {
		return beneficaryMasterId;
	}
	
	public void setBeneficaryMasterId(BigDecimal beneficaryMaster) {
		this.beneficaryMasterId = beneficaryMaster;
	}
	
	
	
	/**
	 * @return the isActive
	 */
	@Column(name="ISACTIVE")
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the createdBy
	 */
	@Column(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the createdDate
	 */
	@Column(name="CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the modifiedBy
	 */
	@Column(name="MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}
	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	/**
	 * @return the modifiedDate
	 */
	@Column(name="MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
	
	
	
	
	@Column(name="MAP_BENE_SEQ")
	public BigDecimal getMapSequenceId() {
		return mapSequenceId;
	}

	public void setMapSequenceId(BigDecimal mapSequenceId) {
		this.mapSequenceId = mapSequenceId;
	}
	@Column(name="REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name="BENEFICARY_ACCOUNT_SEQ_ID")
	public BigDecimal getBeneficaryAccountId() {
		return beneficaryAccountId;
	}

	public void setBeneficaryAccountId(BigDecimal beneficaryAccountId) {
		this.beneficaryAccountId = beneficaryAccountId;
	}

	@Column(name="ORS_STATUS")
	public BigDecimal getOrsSatus() {
		return orsSatus;
	}

	public void setOrsSatus(BigDecimal orsSatus) {
		this.orsSatus = orsSatus;
	}
	@Column(name="MY_FAVOURITE_BENE")
	public String getMyFavouriteBene() {
		return myFavouriteBene;
	}
	public void setMyFavouriteBene(String myFavouriteBene) {
		this.myFavouriteBene = myFavouriteBene;
	}

	@Column(name="RELATIONS_ID")
	public BigDecimal getRelationsId() {
		return relationsId;
	}

	public void setRelationsId(BigDecimal relationsId) {
		this.relationsId = relationsId;
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
		@Column(name="FIFTH_NAME_LOCAL")
		public String getLocalFifthName() {
			return localFifthName;
		}

		public void setLocalFifthName(String localFifthName) {
			this.localFifthName = localFifthName;
		
		}
	 
		@Column(name="DEVICE_IP")
		public String getDeviceIp() {
			return deviceIp;
		}

		public void setDeviceIp(String deviceIp) {
			this.deviceIp = deviceIp;
		}

		@Column(name="DEVICE_TYPE")
		public String getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(String deviceType) {
			this.deviceType = deviceType;
		}
		
		@Column(name="REASON_CODE_ID")
		public BigDecimal getReasonCodeId() {
			return reasonCodeId;
		}

		public void setReasonCodeId(BigDecimal reasonCodeId) {
			this.reasonCodeId = reasonCodeId;
		}
}
