package com.amx.jax.dbmodel.partner;

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
@Table(name="EX_APPL_TRNX_SRV_PROV")
public class RemitApplSrvProv implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal remitapplSrvProvId;
	private BigDecimal remittanceApplicationId;
	private BigDecimal bankId;
	private BigDecimal amgSessionId;
	private String partnerSessionId;
	private String partnerReferenceNo;
	private String createdBy;
	private Date createdDate;
	private BigDecimal intialAmountInSettlCurr;
	private BigDecimal fixedCommInSettlCurr;
	private BigDecimal variableCommInSettlCurr;
	private String settlementCurrency;
	private BigDecimal transactionMargin;
	private Date offerExpirationDate;
	private Date offerStartingDate;
	private BigDecimal partnerExchangeRate;
	
	public RemitApplSrvProv(){
		super();
	}

	@Id
	@GeneratedValue(generator="ex_appl_trnx_srv_prov_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_appl_trnx_srv_prov_seq",sequenceName="EX_APPL_TRNX_SRV_PROV_SEQ",allocationSize=1)
	@Column(name = "EX_APPL_TRNX_SRV_PROV_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getRemitapplSrvProvId() {
		return remitapplSrvProvId;
	}
	public void setRemitapplSrvProvId(BigDecimal remitapplSrvProvId) {
		this.remitapplSrvProvId = remitapplSrvProvId;
	}
	
	@Column(name = "REMITTANCE_APPLICATION_ID")
	public BigDecimal getRemittanceApplicationId() {
		return remittanceApplicationId;
	}
	public void setRemittanceApplicationId(BigDecimal remittanceApplicationId) {
		this.remittanceApplicationId = remittanceApplicationId;
	}
	
	@Column(name = "BANK_ID")
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	
	@Column(name = "AMG_SESSION_ID")
	public BigDecimal getAmgSessionId() {
		return amgSessionId;
	}
	public void setAmgSessionId(BigDecimal amgSessionId) {
		this.amgSessionId = amgSessionId;
	}
	
	@Column(name = "PARTNER_SESSION_ID")
	public String getPartnerSessionId() {
		return partnerSessionId;
	}
	public void setPartnerSessionId(String partnerSessionId) {
		this.partnerSessionId = partnerSessionId;
	}

	@Column(name = "PARTNER_REFERENCE_NO")
	public String getPartnerReferenceNo() {
		return partnerReferenceNo;
	}
	public void setPartnerReferenceNo(String partnerReferenceNo) {
		this.partnerReferenceNo = partnerReferenceNo;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "INITIAL_AMOUNT_IN_SETTL_CURR")
	public BigDecimal getIntialAmountInSettlCurr() {
		return intialAmountInSettlCurr;
	}
	public void setIntialAmountInSettlCurr(BigDecimal intialAmountInSettlCurr) {
		this.intialAmountInSettlCurr = intialAmountInSettlCurr;
	}

	@Column(name = "FIXED_COMM_IN_SETTL_CURR")
	public BigDecimal getFixedCommInSettlCurr() {
		return fixedCommInSettlCurr;
	}
	public void setFixedCommInSettlCurr(BigDecimal fixedCommInSettlCurr) {
		this.fixedCommInSettlCurr = fixedCommInSettlCurr;
	}

	@Column(name = "VARIABLE_COMM_IN_SETTL_CURR")
	public BigDecimal getVariableCommInSettlCurr() {
		return variableCommInSettlCurr;
	}
	public void setVariableCommInSettlCurr(BigDecimal variableCommInSettlCurr) {
		this.variableCommInSettlCurr = variableCommInSettlCurr;
	}

	@Column(name = "SETTLEMENT_CURRENCY")
	public String getSettlementCurrency() {
		return settlementCurrency;
	}
	public void setSettlementCurrency(String settlementCurrency) {
		this.settlementCurrency = settlementCurrency;
	}

	@Column(name = "TRANSACTION_MARGIN")
	public BigDecimal getTransactionMargin() {
		return transactionMargin;
	}
	public void setTransactionMargin(BigDecimal transactionMargin) {
		this.transactionMargin = transactionMargin;
	}

	@Column(name = "OFFER_EXPIRATION_DATE")
	public Date getOfferExpirationDate() {
		return offerExpirationDate;
	}
	public void setOfferExpirationDate(Date offerExpirationDate) {
		this.offerExpirationDate = offerExpirationDate;
	}

	@Column(name = "OFFER_STARTING_DATE")
	public Date getOfferStartingDate() {
		return offerStartingDate;
	}
	public void setOfferStartingDate(Date offerStartingDate) {
		this.offerStartingDate = offerStartingDate;
	}
	
	@Column(name = "PARTNER_EXCHANGE_RATE")
	public BigDecimal getPartnerExchangeRate() {
		return partnerExchangeRate;
	}
	public void setPartnerExchangeRate(BigDecimal partnerExchangeRate) {
		this.partnerExchangeRate = partnerExchangeRate;
	}
			
}
