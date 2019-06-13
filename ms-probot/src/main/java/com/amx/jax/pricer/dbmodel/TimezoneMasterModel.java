package com.amx.jax.pricer.dbmodel;

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
@Table(name = "JAX_PR_TIMEZONE_MASTER")
public class TimezoneMasterModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2568407108114984983L;

	@Id
	@GeneratedValue(generator = "jax_pr_timezone_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "jax_pr_timezone_seq", sequenceName = "JAX_PR_TIMEZONE_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal id;

	@Column(name = "TIMEZONE", unique = true, nullable = false)
	private String timezone;

	@Column(name = "GMT_OFFSET", unique = true, nullable = false)
	private String gmtOffset;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "ISACTIVE")
	private char isActive;

	//@Column(name = "COUNTRY_ID", unique = true)
	//private BigDecimal countryId;

	@Column(name = "INFO")
	private String info;

	@Column(name = "FLAGS")
	private BigDecimal flags;

	@Column(name = "CREATED_BY")
	private String createdBY;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getGmtOffset() {
		return gmtOffset;
	}

	public void setGmtOffset(String gmtOffset) {
		this.gmtOffset = gmtOffset;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public char getIsActive() {
		return isActive;
	}

	public void setIsActive(char isActive) {
		this.isActive = isActive;
	}

	/*TODO: Remove
	 * Columns Removed. Keeping to avoid side effects.
	  public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}*/

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public BigDecimal getFlags() {
		return flags;
	}

	public void setFlags(BigDecimal flags) {
		this.flags = flags;
	}

	public String getCreatedBY() {
		return createdBY;
	}

	public void setCreatedBY(String createdBY) {
		this.createdBY = createdBY;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
