package com.amx.jax.grid.views;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import com.amx.jax.grid.GridViewRecord;

@Entity
@Table(name = "JAX_VW_TRNX_FEEDBACK")
public class FeedbackStarRating implements GridViewRecord {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "RATING_ID")
	private BigDecimal ratingId;

	@Column(name = "CUSTOMER_EN_NAME")
	private String customerName;

	@Column(name = "IDENTITY_INT")
	private String identityInt;

	@Column(name = "NATIONALITY")
	private BigDecimal nationality;

	@Column(name = "NATIONALITY_DESC")
	private String nationalityDesc;

	@Column(name = "REMITTANCE_MODE_NAME")
	private String remittanceModeName;

	@Column(name = "BENE_COUNTRY_NAME")
	private String beneCountryName;

	@Column(name = "BENE_BANK_NAME")
	private String beneBankName;

	@Column(name = "LOCAL_CURRENCY_CODE")
	private String localCurrencyCode;

	@Column(name = "LOCAL_TRANX_AMOUNT")
	private BigDecimal localTrnxAmount;

	@Column(name = "RATING")
	private BigDecimal rating;

	@Column(name = "RATING_REMARK")
	private String ratingRemark;

	@Column(name = "TRNX_DATE")
	private Date trnxDate;

	@Column(name = "TRNX_CREDIT_DATE")
	private Date trnxCreditDate;

	@Column(name = "RATING_DATE")
	private Timestamp ratingDate;

	private Integer totalRecords;

	private Integer rn;

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Integer getRn() {
		return rn;
	}

	public void setRn(Integer rn) {
		this.rn = rn;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public BigDecimal getNationality() {
		return nationality;
	}

	public void setNationality(BigDecimal nationality) {
		this.nationality = nationality;
	}

	public String getNationalityDesc() {
		return nationalityDesc;
	}

	public void setNationalityDesc(String nationalityDesc) {
		this.nationalityDesc = nationalityDesc;
	}

	public String getRemittanceModeName() {
		return remittanceModeName;
	}

	public void setRemittanceModeName(String remittanceModeName) {
		this.remittanceModeName = remittanceModeName;
	}

	public String getBeneCountryName() {
		return beneCountryName;
	}

	public void setBeneCountryName(String beneCountryName) {
		this.beneCountryName = beneCountryName;
	}

	public String getBeneBankName() {
		return beneBankName;
	}

	public void setBeneBankName(String beneBankName) {
		this.beneBankName = beneBankName;
	}

	public String getLocalCurrencyCode() {
		return localCurrencyCode;
	}

	public void setLocalCurrencyCode(String localCurrencyCode) {
		this.localCurrencyCode = localCurrencyCode;
	}

	public BigDecimal getLocalTrnxAmount() {
		return localTrnxAmount;
	}

	public void setLocalTrnxAmount(BigDecimal localTrnxAmount) {
		this.localTrnxAmount = localTrnxAmount;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public String getRatingRemark() {
		return ratingRemark;
	}

	public void setRatingRemark(String ratingRemark) {
		this.ratingRemark = ratingRemark;
	}

	public Date getTrnxDate() {
		return trnxDate;
	}

	public void setTrnxDate(Date trnxDate) {
		this.trnxDate = trnxDate;
	}

	public Date getTrnxCreditDate() {
		return trnxCreditDate;
	}

	public void setTrnxCreditDate(Date trnxCreditDate) {
		this.trnxCreditDate = trnxCreditDate;
	}

	public BigDecimal getRatingId() {
		return ratingId;
	}

	public void setRatingId(BigDecimal ratingId) {
		this.ratingId = ratingId;
	}

	public Timestamp getRatingDate() {
		return ratingDate;
	}

	public void setRatingDate(Timestamp ratingDate) {
		this.ratingDate = ratingDate;
	}

}
