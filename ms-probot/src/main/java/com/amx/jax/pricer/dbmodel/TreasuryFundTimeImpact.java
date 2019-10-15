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
@Table(name = "JAX_PR_TREASURY_TIME_IMPACT")
public class TreasuryFundTimeImpact implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4071821960815131191L;

	@Id
	@GeneratedValue(generator = "treasury_ft_impact_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "treasury_ft_impact_seq", sequenceName = "JAX_PR_TREA_TIME_IMPACT_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal id;

	@Column(name = "COUNTRY_ID")
	private BigDecimal countryId;

	@Column(name = "CURRENCY_ID")
	private BigDecimal currencyId;

	@Column(name = "FUNDING_CUR_ID")
	private BigDecimal fundingCurId;

	@Column(name = "FUND_STATUS")
	private String fundStatus;

	@Column(name = "TRNX_TIME_FROM")
	private BigDecimal trnxTimeFrom;

	@Column(name = "TRNX_TIME_TO")
	private BigDecimal trnxTimeTo;

	@Column(name = "TREASURY_START_TIME")
	private BigDecimal treasuryStartTime;

	@Column(name = "TREASURY_END_TIME")
	private BigDecimal treasuryEndTime;

	@Column(name = "WORK_DAY_FROM")
	private BigDecimal workDayFrom;

	@Column(name = "WORK_DAY_TO")
	private BigDecimal workDayTo;

	@Column(name = "ITW_TTD_IMPACT_MIN")
	private BigDecimal inTrWindowTtdImpactMin;

	@Column(name = "ITW_DAY_IMPACT")
	private BigDecimal inTrWindowDayImpact;

	@Column(name = "OOTW_TTD_IMPACT_MIN")
	private BigDecimal outOfTrWindowTtdImpactMin;

	@Column(name = "OOTW_DAY_IMPACT")
	private BigDecimal outOfTrWindowDayImpact;

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "INFO")
	private String info;

	@Column(name = "FLAGS")
	private BigDecimal flags;

	@Column(name = "CREATED_BY")
	private String createdBy;

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

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getFundingCurId() {
		return fundingCurId;
	}

	public void setFundingCurId(BigDecimal fundingCurId) {
		this.fundingCurId = fundingCurId;
	}

	public String getFundStatus() {
		return fundStatus;
	}

	public void setFundStatus(String fundStatus) {
		this.fundStatus = fundStatus;
	}

	public BigDecimal getTrnxTimeFrom() {
		return trnxTimeFrom;
	}

	public void setTrnxTimeFrom(BigDecimal trnxTimeFrom) {
		this.trnxTimeFrom = trnxTimeFrom;
	}

	public BigDecimal getTrnxTimeTo() {
		return trnxTimeTo;
	}

	public void setTrnxTimeTo(BigDecimal trnxTimeTo) {
		this.trnxTimeTo = trnxTimeTo;
	}

	public BigDecimal getTreasuryStartTime() {
		return treasuryStartTime;
	}

	public void setTreasuryStartTime(BigDecimal treasuryStartTime) {
		this.treasuryStartTime = treasuryStartTime;
	}

	public BigDecimal getTreasuryEndTime() {
		return treasuryEndTime;
	}

	public void setTreasuryEndTime(BigDecimal treasuryEndTime) {
		this.treasuryEndTime = treasuryEndTime;
	}

	public BigDecimal getWorkDayFrom() {
		return workDayFrom;
	}

	public void setWorkDayFrom(BigDecimal workDayFrom) {
		this.workDayFrom = workDayFrom;
	}

	public BigDecimal getWorkDayTo() {
		return workDayTo;
	}

	public void setWorkDayTo(BigDecimal workDayTo) {
		this.workDayTo = workDayTo;
	}

	public BigDecimal getInTrWindowTtdImpactMin() {
		return inTrWindowTtdImpactMin;
	}

	public void setInTrWindowTtdImpactMin(BigDecimal inTrWindowTtdImpactMin) {
		this.inTrWindowTtdImpactMin = inTrWindowTtdImpactMin;
	}

	public BigDecimal getInTrWindowDayImpact() {
		return inTrWindowDayImpact;
	}

	public void setInTrWindowDayImpact(BigDecimal inTrWindowDayImpact) {
		this.inTrWindowDayImpact = inTrWindowDayImpact;
	}

	public BigDecimal getOutOfTrWindowTtdImpactMin() {
		return outOfTrWindowTtdImpactMin;
	}

	public void setOutOfTrWindowTtdImpactMin(BigDecimal outOfTrWindowTtdImpactMin) {
		this.outOfTrWindowTtdImpactMin = outOfTrWindowTtdImpactMin;
	}

	public BigDecimal getOutOfTrWindowDayImpact() {
		return outOfTrWindowDayImpact;
	}

	public void setOutOfTrWindowDayImpact(BigDecimal outOfTrWindowDayImpact) {
		this.outOfTrWindowDayImpact = outOfTrWindowDayImpact;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
