package com.amx.jax.dbmodel.login;

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
@Table(name = "EX_FUNCTIONALITY_TYPE_MASTER" )
public class FunctionalityTypeMaster implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal functionalityTypeId;
	private String functionalityTypeEnum;
	private String functionalityTypeDescription;
	private String isactive;
	private Date createdDate;

	@Id
	@GeneratedValue(generator="ex_funct_type_master_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_funct_type_master_seq",sequenceName="EX_FUNCT_TYPE_MASTER_SEQ",allocationSize=1)
	@Column(name = "FUNCTIONALITY_TYPE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getFunctionalityTypeId() {
		return functionalityTypeId;
	}
	public void setFunctionalityTypeId(BigDecimal functionalityTypeId) {
		this.functionalityTypeId = functionalityTypeId;
	}

	@Column(name = "FUNCTIONALITY_TYPE_ENUM")
	public String getFunctionalityTypeEnum() {
		return functionalityTypeEnum;
	}
	public void setFunctionalityTypeEnum(String functionalityTypeEnum) {
		this.functionalityTypeEnum = functionalityTypeEnum;
	}

	@Column(name = "FUNCTIONALITY_TYPE_DESCRIPTION")
	public String getFunctionalityTypeDescription() {
		return functionalityTypeDescription;
	}
	public void setFunctionalityTypeDescription(String functionalityTypeDescription) {
		this.functionalityTypeDescription = functionalityTypeDescription;
	}

	@Column(name = "ISACTIVE")
	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
