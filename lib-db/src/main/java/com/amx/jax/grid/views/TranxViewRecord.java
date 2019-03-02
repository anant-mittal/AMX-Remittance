package com.amx.jax.grid.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.es.ESDocFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "VW_KIBANA_TRNX")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranxViewRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APPL_ID")
	private BigDecimal id;

	@ESDocFormat(ESDocFormat.Type.DATE)
	@Column(name = "LATEST_TIMESTAMP")
	private Date lastUpdateDate;

	@Column(name = "APPL_FINYEAR")
	private BigDecimal docFy;

	@Column(name = "APPL_DOCNO")
	private BigDecimal docNo;

	@ESDocFormat(ESDocFormat.Type.DATE)
	@Column(name = "APPL_DATE")
	private Date creationDate;

	// Bene
	@Column(name = "BENE_BANK_ID")
	private BigDecimal beneBankId;

	@Column(name = "BENE_BANK_NAME")
	private String beneBankName;

	@Column(name = "BENE_BANKBRANCH_ID")
	private BigDecimal beneBankBranchId;

	@Column(name = "BENE_BANKBRANCH_NAME")
	private String beneBankBranchName;

	@Column(name = "BENE_COUNTRY_ID")
	private BigDecimal beneCountryId;

	@Column(name = "BENE_COUNTRY_CODE")
	private String beneCountryCode;

	// Branch
	@Column(name = "COUNTRY_BRANCH_ID")
	private BigDecimal countryBranchId;

	@Column(name = "COUNTRY_BRANCH_NAME")
	private String countryBranchName;

	@Column(name = "BRANCH_AREA_ID")
	private String branchAreaId;

	@Column(name = "BRANCH_AREA_NAME")
	private String branchAreaName;

	// ATTR
	@Column(name = "CHANNEL")
	private String channel;

	@Column(name = "USER_NAME")
	private String branchUser;

	@Column(name = "DELIVERY_MODE_ID")
	private BigDecimal deliveryModeId;

	@Column(name = "DELIVERY_MODE_NAME")
	private String deliveryModeName;

	@Column(name = "FOREIGN_CURRENCY_ID")
	private BigDecimal forCurId;

	@Column(name = "FOREIGN_CURRENCY_CODE")
	private String forCurName;

	@Column(name = "FOREIGN_TRNX_AMOUNT")
	private BigDecimal forTrnxAmount;

	@Column(name = "LOCAL_TRANX_AMOUNT")
	private BigDecimal domTrnxAmount;

	// Channe/Rounting/Service
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

	@Column(name = "ROUTING_COUNTRY_CODE")
	private String routingCountryCode;

	@Column(name = "PARTNER")
	private String partner;

	@Column(name = "SERVICE_TYPE")
	private String serviceType;

	// Tranx
	@Column(name = "TRNX_ID")
	private BigDecimal trnxId;

	@ESDocFormat(ESDocFormat.Type.DATE)
	@Column(name = "TRNX_DATETIME")
	private Date trnxDate;

	@ESDocFormat(ESDocFormat.Type.DATE)
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

	// Revenue
	@Column(name = "TRNX_ACCOUNT_MMYYYY")
	private BigDecimal trnxAccountMonth;

	@Column(name = "EXCHANGE_GAIN")
	private BigDecimal exchangeGain;

	@Column(name = "CO_BANK_CHARGES")
	private BigDecimal coBankCharges;

	@Column(name = "COMM_RECV")
	private BigDecimal commRecv;

	@Column(name = "LOCAL_COMMISION_AMOUNT")
	private BigDecimal localCommRecv;

	// Customer
	@Column(name = "IDENTITY_INT")
	private String identity;

	@Column(name = "CUSTOMER_ID")
	private BigDecimal custmerId;

	@Column(name = "CUSTOMER_EN_NAME")
	private String custmerName;

	@Column(name = "NATIONALITY")
	private BigDecimal custmerNation;

	@Column(name = "NATIONALITY_CODE")
	private String custmerNationCode;

	// DeviceClient Info
	@Column(name = "CLIENT_TYPE")
	private String clientType;

	@Column(name = "CLIENT_ID")
	private String clientId;

	@Column(name = "CLIENT_IP")
	private String clientIp;

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

	public BigDecimal getTrnxId() {
		return trnxId;
	}

	public void setTrnxId(BigDecimal trnaxId) {
		this.trnxId = trnaxId;
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

	public String getBeneCountryCode() {
		return beneCountryCode;
	}

	public void setBeneCountryCode(String beneCountryCode) {
		this.beneCountryCode = beneCountryCode;
	}

	public String getRoutingCountryCode() {
		return routingCountryCode;
	}

	public void setRoutingCountryCode(String routingCountryCode) {
		this.routingCountryCode = routingCountryCode;
	}

	public String getCustmerNationCode() {
		return custmerNationCode;
	}

	public void setCustmerNationCode(String custmerNationCode) {
		this.custmerNationCode = custmerNationCode;
	}

	public BigDecimal getExchangeGain() {
		return exchangeGain;
	}

	public void setExchangeGain(BigDecimal exchangeGain) {
		this.exchangeGain = exchangeGain;
	}

	public BigDecimal getCoBankCharges() {
		return coBankCharges;
	}

	public void setCoBankCharges(BigDecimal coBankCharges) {
		this.coBankCharges = coBankCharges;
	}

	public BigDecimal getCommRecv() {
		return commRecv;
	}

	public void setCommRecv(BigDecimal commRecv) {
		this.commRecv = commRecv;
	}

	public String getBranchUser() {
		return branchUser;
	}

	public void setBranchUser(String branchUser) {
		this.branchUser = branchUser;
	}

	public String getCountryBranchName() {
		return countryBranchName;
	}

	public void setCountryBranchName(String countryBranchName) {
		this.countryBranchName = countryBranchName;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getBranchAreaId() {
		return branchAreaId;
	}

	public void setBranchAreaId(String branchAreaId) {
		this.branchAreaId = branchAreaId;
	}

	public String getBranchAreaName() {
		return branchAreaName;
	}

	public void setBranchAreaName(String branchAreaName) {
		this.branchAreaName = branchAreaName;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public BigDecimal getTrnxAccountMonth() {
		return trnxAccountMonth;
	}

	public void setTrnxAccountMonth(BigDecimal trnxAccountMonth) {
		this.trnxAccountMonth = trnxAccountMonth;
	}

	public BigDecimal getLocalCommRecv() {
		return localCommRecv;
	}

	public void setLocalCommRecv(BigDecimal localCommRecv) {
		this.localCommRecv = localCommRecv;
	}

}
