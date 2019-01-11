package com.amx.jax.grid.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * Author Rahamathali Shaik
*/
@Entity
@Table(name = "VW_CUSTOMER_RATING")
public class TranxViewRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APPL_ID")
	private BigDecimal id;

	@Column(name = "LATEST_TIMESTAMP")
	private Date lastUpdateDate;

	@Column(name = "APPL_FINYEAR")
	private BigDecimal docFy;

	@Column(name = "APPL_DOCNO")
	private BigDecimal docNo;

	@Column(name = "appl_date")
	private Date creationDate;

	// Bene
	@Column(name = "BENE_COUNTRY_ID")
	private BigDecimal beneCountryId;

	@Column(name = "BENE_COUNTRY_NAME")
	private String beneCountryName;

	@Column(name = "BENE_BANK_ID")
	private BigDecimal beneBankId;

	@Column(name = "BENE_BANK_NAME")
	private String beneBankName;

	@Column(name = "BENE_BANKBRANCH_ID")
	private BigDecimal beneBankBranchId;

	@Column(name = "BENE_BANKBRANCH_NAME")
	private String beneBankBranchName;

	// ATTR
	@Column(name = "CHANNEL")
	private String channel;

	@Column(name = "COUNTRY_BRANCH_ID")
	private BigDecimal countryBranchId;

	@Column(name = "COUNTRY_BRANCH_NAME")
	private String countryBranchName;

	@Column(name = "DELIVERY_MODE_ID")
	private BigDecimal deliveryModeId;

	@Column(name = "DELIVERY_MODE_NAME")
	private String deliveryModeName;

	@Column(name = "FOREIGN_CURRENCY_ID")
	private BigDecimal forCurId;

	@Column(name = "FOREIGN_CURRENCY_NAME")
	private String forCurName;

	@Column(name = "foreign_trnx_amount")
	private BigDecimal forTrnxAmount;

	@Column(name = "local_tranx_amount")
	private BigDecimal domTrnxAmount;

	@Column(name = "REMITTANCE_MODE_ID")
	private BigDecimal modeId;

	@Column(name = "REMITTANCE_MODE_NAME")
	private String modeName;

	@Column(name = "ROUTING_BANK_ID")
	private BigDecimal routingBankid;

	@Column(name = "ROUTING_BANK_NAME")
	private String routingBankName;

	@Column(name = "ROUTING_COUNTRY_ID")
	private BigDecimal routingCountryid;

	@Column(name = "ROUTING_COUNTRY_NAME")
	private String routingCountryName;

	// Tranx
	@Column(name = "TRNX_ID")
	private BigDecimal trnaxId;

	@Column(name = "TRNX_DATETIME")
	private Date trnxDate;

	@Column(name = "TRNX_PAID_DATETIME")
	private Date trnxPaidDate;

	@Column(name = "TRNX_DOCCOD")
	private String trnxCode;

	@Column(name = "TRNX_DOCNO")
	private String trnxDocNo;

	@Column(name = "TRNX_FINYEAR")
	private String trnxDocFy;

	@Column(name = "TRNX_STATUS")
	private String trnxStatus;

	// Customer
	@Column(name = "IDENTITY_INT")
	private String identity;

	@Column(name = "CUSTOMER_ID")
	private BigDecimal custmerId;

	@Column(name = "CUSTOMER_EN_NAME")
	private String custmerName;

	@Column(name = "NATIONALITY")
	private BigDecimal custmerNation;

	@Column(name = "NATIONALITY_NAME")
	private String custmerNationName;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public BigDecimal getDocFy() {
		return docFy;
	}

	public void setDocFy(BigDecimal docFy) {
		this.docFy = docFy;
	}

	public BigDecimal getDocNo() {
		return docNo;
	}

	public void setDocNo(BigDecimal docNo) {
		this.docNo = docNo;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public BigDecimal getBeneCountryId() {
		return beneCountryId;
	}

	public void setBeneCountryId(BigDecimal beneCountryId) {
		this.beneCountryId = beneCountryId;
	}

	public String getBeneCountryName() {
		return beneCountryName;
	}

	public void setBeneCountryName(String beneCountryName) {
		this.beneCountryName = beneCountryName;
	}

	public BigDecimal getBeneBankId() {
		return beneBankId;
	}

	public void setBeneBankId(BigDecimal beneBankId) {
		this.beneBankId = beneBankId;
	}

	public String getBeneBankName() {
		return beneBankName;
	}

	public void setBeneBankName(String beneBankName) {
		this.beneBankName = beneBankName;
	}

	public BigDecimal getBeneBankBranchId() {
		return beneBankBranchId;
	}

	public void setBeneBankBranchId(BigDecimal beneBankBranchId) {
		this.beneBankBranchId = beneBankBranchId;
	}

	public String getBeneBankBranchName() {
		return beneBankBranchName;
	}

	public void setBeneBankBranchName(String beneBankBranchName) {
		this.beneBankBranchName = beneBankBranchName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public String getCountryBranchName() {
		return countryBranchName;
	}

	public void setCountryBranchName(String countryBranchName) {
		this.countryBranchName = countryBranchName;
	}

	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}

	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}

	public String getDeliveryModeName() {
		return deliveryModeName;
	}

	public void setDeliveryModeName(String deliveryModeName) {
		this.deliveryModeName = deliveryModeName;
	}

	public BigDecimal getForCurId() {
		return forCurId;
	}

	public void setForCurId(BigDecimal forCurId) {
		this.forCurId = forCurId;
	}

	public String getForCurName() {
		return forCurName;
	}

	public void setForCurName(String forCurName) {
		this.forCurName = forCurName;
	}

	public BigDecimal getForTrnxAmount() {
		return forTrnxAmount;
	}

	public void setForTrnxAmount(BigDecimal forTrnxAmount) {
		this.forTrnxAmount = forTrnxAmount;
	}

	public BigDecimal getDomTrnxAmount() {
		return domTrnxAmount;
	}

	public void setDomTrnxAmount(BigDecimal domTrnxAmount) {
		this.domTrnxAmount = domTrnxAmount;
	}

	public BigDecimal getModeId() {
		return modeId;
	}

	public void setModeId(BigDecimal modeId) {
		this.modeId = modeId;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
	}

	public BigDecimal getRoutingBankid() {
		return routingBankid;
	}

	public void setRoutingBankid(BigDecimal routingBankid) {
		this.routingBankid = routingBankid;
	}

	public String getRoutingBankName() {
		return routingBankName;
	}

	public void setRoutingBankName(String routingBankName) {
		this.routingBankName = routingBankName;
	}

	public BigDecimal getRoutingCountryid() {
		return routingCountryid;
	}

	public void setRoutingCountryid(BigDecimal routingCountryid) {
		this.routingCountryid = routingCountryid;
	}

	public String getRoutingCountryName() {
		return routingCountryName;
	}

	public void setRoutingCountryName(String routingCountryName) {
		this.routingCountryName = routingCountryName;
	}

	public BigDecimal getTrnaxId() {
		return trnaxId;
	}

	public void setTrnaxId(BigDecimal trnaxId) {
		this.trnaxId = trnaxId;
	}

	public Date getTrnxDate() {
		return trnxDate;
	}

	public void setTrnxDate(Date trnxDate) {
		this.trnxDate = trnxDate;
	}

	public Date getTrnxPaidDate() {
		return trnxPaidDate;
	}

	public void setTrnxPaidDate(Date trnxPaidDate) {
		this.trnxPaidDate = trnxPaidDate;
	}

	public String getTrnxCode() {
		return trnxCode;
	}

	public void setTrnxCode(String trnxCode) {
		this.trnxCode = trnxCode;
	}

	public String getTrnxDocNo() {
		return trnxDocNo;
	}

	public void setTrnxDocNo(String trnxDocNo) {
		this.trnxDocNo = trnxDocNo;
	}

	public String getTrnxDocFy() {
		return trnxDocFy;
	}

	public void setTrnxDocFy(String trnxDocFy) {
		this.trnxDocFy = trnxDocFy;
	}

	public String getTrnxStatus() {
		return trnxStatus;
	}

	public void setTrnxStatus(String trnxStatus) {
		this.trnxStatus = trnxStatus;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public BigDecimal getCustmerId() {
		return custmerId;
	}

	public void setCustmerId(BigDecimal custmerId) {
		this.custmerId = custmerId;
	}

	public String getCustmerName() {
		return custmerName;
	}

	public void setCustmerName(String custmerName) {
		this.custmerName = custmerName;
	}

	public BigDecimal getCustmerNation() {
		return custmerNation;
	}

	public void setCustmerNation(BigDecimal custmerNation) {
		this.custmerNation = custmerNation;
	}

	public String getCustmerNationName() {
		return custmerNationName;
	}

	public void setCustmerNationName(String custmerNationName) {
		this.custmerNationName = custmerNationName;
	}

}
