package com.amx.jax.dbmodel.remittance;
/**
 * @author rabil 
 * @date 10/29/2019
 */
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
@Table(name = "EX_RATE_PLACE_ORDER")
public class RatePlaceOrder implements Serializable {

		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private BigDecimal ratePlaceOrderId;
		private BigDecimal customerId;
		private String customerEmail;
		private BigDecimal countryBranchId;
		private BigDecimal beneficiaryCountryId;
		//private  BeneficaryMaster beneficiaryMasterId;
		private  BigDecimal beneficiaryMasterId;
		private BigDecimal beneficiaryBankId;
		private String beneficiaryAccountNo;
		private BigDecimal requestCurrencyId;
		private BigDecimal transactionAmount;
		private BigDecimal remitType;
		private BigDecimal rateOffered;
		private String transactionConcluded;
		private String createdBy;
		private Date createdDate;
		private String modifiedBy;
		private Date modifiedDate;
		private String approvedBy;
		private Date approvedDate;
		private String customerRateAcceptance;
		private String convertToRateAlert;
		private String isActive;
		//private  BeneficaryAccount accountSeqquenceId;
		private  BigDecimal accountSeqquenceId;
		private BigDecimal transactionAmountPaid;
		private BigDecimal remitDocumentNumber;
		private BigDecimal remitDocumentYear;
		private String customerUnqiueNumber;
		private String branchSupportIndicator;
		private BigDecimal supportBranchId;
		private BigDecimal applicationCountryId;
		private BigDecimal companyId;
		private BigDecimal documentCode;
		private BigDecimal documentNumber;
		private BigDecimal documentId;
		private BigDecimal documentFinanceYear;
		private Date appointmentTime;
		private BigDecimal sourceOfincomeId;
		private String collectionMode;
		private BigDecimal routingBankId;
		private BigDecimal chequeBankCode;
		private Date chequeDate;
		private String chequeReference;
		private String approvalNo;
		private String dbCardName; 
		private String debitCard;
		private String knetReceipt;
		private String knetReceiptDateTime;
		private BigDecimal serviceMasterId;
		private BigDecimal routingCountryId;
		private BigDecimal routingBranchId;
		private BigDecimal remittanceModeId;
		private BigDecimal deliveryModeId;
		private String customerIndicator;
		private BigDecimal applDocumentNumber;
		private BigDecimal applDocumentFinanceYear;
		private String chequeClearanceInd;
		private BigDecimal customerreference;
		private String areaName;
		private String negotiateSts;
		private Date valueDate;
		private BigDecimal destinationCurrenyId;
		private String remarks;
		
		private BigDecimal beneficiaryRelationId;
		
		private BigDecimal discountOnCommission;
		private String isDiscountAvailed;
		private BigDecimal cusCatDiscountId;
		private BigDecimal cusCatDiscount;
		private BigDecimal channelDiscountId;
		private BigDecimal channelDiscount;
		private BigDecimal pipsFromAmt;
		private BigDecimal pipsToAmt;
		private BigDecimal pipsDiscount;
		private String reachedCostRateLimit;
		private String requestModel;
		private BigDecimal exchangeRateApplied;
		private BigDecimal rackExchangeRate;
		private String terminalId;
		private String loyaltyPointInd;
		private BigDecimal avgCost;
		private BigDecimal savedLocalAmount;
		private BigDecimal negotiateCount;
		
		 
		
		
		public RatePlaceOrder() {
			super();
		}

		public RatePlaceOrder(BigDecimal ratePlaceOrderId, BigDecimal customerId,
				String customerEmail, BigDecimal countryBranchId,
				BigDecimal beneficiaryCountryId,
				//BeneficaryMaster beneficiaryMasterId, 
				BigDecimal beneficiaryBankId,
				String beneficiaryAccountNo, BigDecimal requestCurrencyId,
				BigDecimal transactionAmount, BigDecimal remitType,
				BigDecimal rateOffered, String transactionConcluded,
				String createdBy, Date createdDate, String modifiedBy,
				Date modifiedDate, String approvedBy, Date approvedDate,
				String customerRateAcceptance, String convertToRateAlert,
				String isActive,
				//BeneficaryAccount accountSeqquenceId,
				BigDecimal remitDocumentNumber,
				BigDecimal remitDocumentYear, String customerUnqiueNumber,
				String branchSupportIndicator, BigDecimal supportBranchId,
				BigDecimal applicationCountryId, BigDecimal companyId,
				BigDecimal documentCode, BigDecimal documentNumber,
				BigDecimal documentId, BigDecimal documentFinanceYear,
				Date appointmentTime, BigDecimal sourceOfincomeId,
				String collectionMode, BigDecimal routingBankId,
				BigDecimal chequeBankCode, Date chequeDate, String chequeReference,
				String approvalNo, String dbCardName, String debitCard,
				String knetReceipt, String knetReceiptDateTime,
				BigDecimal serviceMasterId, BigDecimal routingCountryId,
				BigDecimal routingBranchId, BigDecimal remittanceModeId,
				BigDecimal deliveryModeId, String customerIndicator,BigDecimal applDocumentNumber,
				BigDecimal applDocumentFinanceYear,String chequeClearanceInd,BigDecimal customerreference,
				String areaName,String negotiateSts,Date valueDate,BigDecimal destinationCurrenyId) {
			super();
			this.ratePlaceOrderId = ratePlaceOrderId;
			this.customerId = customerId;
			this.customerEmail = customerEmail;
			this.countryBranchId = countryBranchId;
			this.beneficiaryCountryId = beneficiaryCountryId;
			this.beneficiaryMasterId = beneficiaryMasterId;
			this.beneficiaryBankId = beneficiaryBankId;
			this.beneficiaryAccountNo = beneficiaryAccountNo;
			this.requestCurrencyId = requestCurrencyId;
			this.transactionAmount = transactionAmount;
			this.remitType = remitType;
			this.rateOffered = rateOffered;
			this.transactionConcluded = transactionConcluded;
			this.createdBy = createdBy;
			this.createdDate = createdDate;
			this.modifiedBy = modifiedBy;
			this.modifiedDate = modifiedDate;
			this.approvedBy = approvedBy;
			this.approvedDate = approvedDate;
			this.customerRateAcceptance = customerRateAcceptance;
			this.convertToRateAlert = convertToRateAlert;
			this.isActive = isActive;
			this.accountSeqquenceId = accountSeqquenceId;
			//this.transactionRequest = transactionRequest;
			this.remitDocumentNumber = remitDocumentNumber;
			this.remitDocumentYear = remitDocumentYear;
			this.customerUnqiueNumber = customerUnqiueNumber;
			this.branchSupportIndicator = branchSupportIndicator;
			this.supportBranchId = supportBranchId;
			this.applicationCountryId = applicationCountryId;
			this.companyId = companyId;
			this.documentCode = documentCode;
			this.documentNumber = documentNumber;
			this.documentId = documentId;
			this.documentFinanceYear = documentFinanceYear;
			this.appointmentTime = appointmentTime;
			this.sourceOfincomeId = sourceOfincomeId;
			this.collectionMode = collectionMode;
			this.routingBankId = routingBankId;
			this.chequeBankCode = chequeBankCode;
			this.chequeDate = chequeDate;
			this.chequeReference = chequeReference;
			this.approvalNo = approvalNo;
			this.dbCardName = dbCardName;
			this.debitCard = debitCard;
			this.knetReceipt = knetReceipt;
			this.knetReceiptDateTime = knetReceiptDateTime;
			this.serviceMasterId = serviceMasterId;
			this.routingCountryId = routingCountryId;
			this.routingBranchId = routingBranchId;
			this.remittanceModeId = remittanceModeId;
			this.deliveryModeId = deliveryModeId;
			this.customerIndicator = customerIndicator;
			this.applDocumentNumber=applDocumentNumber;
			this.applDocumentFinanceYear=applDocumentFinanceYear;
			this.chequeClearanceInd=chequeClearanceInd;
			this.customerreference=customerreference;
			this.areaName=areaName;
			this.negotiateSts=negotiateSts;
			this.valueDate=valueDate;
			this.destinationCurrenyId=destinationCurrenyId;
			
		}
		
		@Id
		@GeneratedValue(generator = "ex_rate_place_order_seq", strategy = GenerationType.SEQUENCE)
		@SequenceGenerator(name = "ex_rate_place_order_seq", sequenceName = "EX_RATE_PLACE_ORDER_SEQ", allocationSize = 1)
		@Column(name = "RATE_PLACE_ORDER_ID", unique = true, nullable = false, precision = 22, scale = 0)
		public BigDecimal getRatePlaceOrderId() {
			return ratePlaceOrderId;
		}
		public void setRatePlaceOrderId(BigDecimal ratePlaceOrderId) {
			this.ratePlaceOrderId = ratePlaceOrderId;
		}
		
		@Column(name = "APPLICATION_COUNTRY_ID")
		public BigDecimal getApplicationCountryId() {
			return applicationCountryId;
		}
		public void setApplicationCountryId(BigDecimal applicationCountryId) {
			this.applicationCountryId = applicationCountryId;
		}
		
		@Column(name = "COMPANY_ID")
		public BigDecimal getCompanyId() {
			return companyId;
		}
		public void setCompanyId(BigDecimal companyId) {
			this.companyId = companyId;
		}
		
		@Column(name = "DOCUMENT_CODE")
		public BigDecimal getDocumentCode() {
			return documentCode;
		}
		public void setDocumentCode(BigDecimal documentCode) {
			this.documentCode = documentCode;
		}
		
		@Column(name = "DOCUMENT_NO")
		public BigDecimal getDocumentNumber() {
			return documentNumber;
		}
		public void setDocumentNumber(BigDecimal documentNumber) {
			this.documentNumber = documentNumber;
		}
		
		@Column(name = "DOCUMENT_ID")
		public BigDecimal getDocumentId() {
			return documentId;
		}
		public void setDocumentId(BigDecimal documentId) {
			this.documentId = documentId;
		}
		
		@Column(name = "DOCUMENT_FINANCE_YEAR")
		public BigDecimal getDocumentFinanceYear() {
			return documentFinanceYear;
		}
		public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
			this.documentFinanceYear = documentFinanceYear;
		}
		
		@Column(name = "CUSTOMER_ID")
		public BigDecimal getCustomerId() {
			return customerId;
		}
		public void setCustomerId(BigDecimal customerId) {
			this.customerId = customerId;
		}
		
		@Column(name = "CUSTOMER_EMAIL_ID")
		public String getCustomerEmail() {
			return customerEmail;
		}
		public void setCustomerEmail(String customerEmail) {
			this.customerEmail = customerEmail;
		}
		
		@Column(name = "COUNTRY_BRANCH_ID",nullable = false)
		public BigDecimal getCountryBranchId() {
			return countryBranchId;
		}
		public void setCountryBranchId(BigDecimal countryBranchId) {
			this.countryBranchId = countryBranchId;
		}
		
		@Column(name = "BENEFICIARY_COUNTRY_ID",nullable = false)
		public BigDecimal getBeneficiaryCountryId() {
			return beneficiaryCountryId;
		}
		public void setBeneficiaryCountryId(BigDecimal beneficiaryCountryId) {
			this.beneficiaryCountryId = beneficiaryCountryId;
		}
		
	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "BENEFICIARY_MASTER_ID") public BeneficaryMaster
	 * getBeneficiaryMasterId() { return beneficiaryMasterId; } public void
	 * setBeneficiaryMasterId(BeneficaryMaster beneficiaryMasterId) {
	 * this.beneficiaryMasterId = beneficiaryMasterId; }
	 */
		
		@Column(name = "BENEFICIARY_BANK_ID",nullable = false)
		public BigDecimal getBeneficiaryBankId() {
			return beneficiaryBankId;
		}
		public void setBeneficiaryBankId(BigDecimal beneficiaryBankId) {
			this.beneficiaryBankId = beneficiaryBankId;
		}
		
		@Column(name = "BENEFICIARY_ACCOUNT_NUMBER")
		public String getBeneficiaryAccountNo() {
			return beneficiaryAccountNo;
		}
		public void setBeneficiaryAccountNo(String beneficiaryAccountNo) {
			this.beneficiaryAccountNo = beneficiaryAccountNo;
		}	

		@Column(name = "TRANSACTION_AMOUNT")
		public BigDecimal getTransactionAmount() {
			return transactionAmount;
		}
		public void setTransactionAmount(BigDecimal transactionAmount) {
			this.transactionAmount = transactionAmount;
		}

		@Column(name = "REMITTANCE_TYPE")
		public BigDecimal getRemitType() {
			return remitType;
		}

		public void setRemitType(BigDecimal remitType) {
			this.remitType = remitType;
		}

		@Column(name = "ISACTIVE")
		public String getIsActive() {
			return isActive;
		}

		public void setIsActive(String isActive) {
			this.isActive = isActive;
		}

		@Column(name = "RATE_OFFERED")
		public BigDecimal getRateOffered() {
			return rateOffered;
		}
		public void setRateOffered(BigDecimal rateOffered) {
			this.rateOffered = rateOffered;
		}
		
		@Column(name = "TRANSACTION_CONCLUDED")
		public String getTransactionConcluded() {
			return transactionConcluded;
		}
		public void setTransactionConcluded(String transactionConcluded) {
			this.transactionConcluded = transactionConcluded;
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
		
		@Column(name = "MODIFIED_BY")
		public String getModifiedBy() {
			return modifiedBy;
		}
		public void setModifiedBy(String modifiedBy) {
			this.modifiedBy = modifiedBy;
		}
		
		@Column(name = "MODIFIED_DATE")
		public Date getModifiedDate() {
			return modifiedDate;
		}
		public void setModifiedDate(Date modifiedDate) {
			this.modifiedDate = modifiedDate;
		}
		
		@Column(name = "APPROVED_BY")
		public String getApprovedBy() {
			return approvedBy;
		}
		public void setApprovedBy(String approvedBy) {
			this.approvedBy = approvedBy;
		}
		
		@Column(name = "APPROVED_DATE")
		public Date getApprovedDate() {
			return approvedDate;
		}
		public void setApprovedDate(Date approvedDate) {
			this.approvedDate = approvedDate;
		}

		@Column(name = "CUSTOMER_RATE_ACCEPTANCE")
		public String getCustomerRateAcceptance() {
			return customerRateAcceptance;
		}
		public void setCustomerRateAcceptance(String customerRateAcceptance) {
			this.customerRateAcceptance = customerRateAcceptance;
		}

		@Column(name = "CONVER_TO_RATE_ALERT")
		public String getConvertToRateAlert() {
			return convertToRateAlert;
		}
		public void setConvertToRateAlert(String convertToRateAlert) {
			this.convertToRateAlert = convertToRateAlert;
		}
		
	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "BENEFICARY_ACCOUNT_ID") public BeneficaryAccount
	 * getAccountSeqquenceId() { return accountSeqquenceId; } public void
	 * setAccountSeqquenceId(BeneficaryAccount accountSeqquenceId) {
	 * this.accountSeqquenceId = accountSeqquenceId; }
	 */

		@Column(name = "TRANSACTION_AMOUNT_PAID")
		public BigDecimal getTransactionAmountPaid() {
			return transactionAmountPaid;
		}
		public void setTransactionAmountPaid(BigDecimal transactionAmountPaid) {
			this.transactionAmountPaid = transactionAmountPaid;
		}
		
		@Column(name = "REMIT_DOCUMENT_NUMBER")
		public BigDecimal getRemitDocumentNumber() {
			return remitDocumentNumber;
		}
		public void setRemitDocumentNumber(BigDecimal remitDocumentNumber) {
			this.remitDocumentNumber = remitDocumentNumber;
		}
		
		@Column(name = "REMIT_DOCUMENT_YEAR")
		public BigDecimal getRemitDocumentYear() {
			return remitDocumentYear;
		}
		public void setRemitDocumentYear(BigDecimal remitDocumentYear) {
			this.remitDocumentYear = remitDocumentYear;
		}
		
		@Column(name = "UNIQUE_NUMBER")
		public String getCustomerUnqiueNumber() {
			return customerUnqiueNumber;
		}
		public void setCustomerUnqiueNumber(String customerUnqiueNumber) {
			this.customerUnqiueNumber = customerUnqiueNumber;
		}
		
		@Column(name = "BRANCH_SUPPORT_INDICATOR")
		public String getBranchSupportIndicator() {
			return branchSupportIndicator;
		}
		public void setBranchSupportIndicator(String branchSupportIndicator) {
			this.branchSupportIndicator = branchSupportIndicator;
		}
		
		@Column(name = "SUPPORT_BRANCH_ID")
		public BigDecimal getSupportBranchId() {
			return supportBranchId;
		}
		public void setSupportBranchId(BigDecimal supportBranchId) {
			this.supportBranchId = supportBranchId;
		}
		
		@Column(name = "SOURCE_OF_INCOME_ID")
		public BigDecimal getSourceOfincomeId() {
			return sourceOfincomeId;
		}
		public void setSourceOfincomeId(BigDecimal sourceOfincomeId) {
			this.sourceOfincomeId = sourceOfincomeId;
		}
		
		@Column(name = "APPL_DOCUMENT_NUMBER")
		public BigDecimal getApplDocumentNumber() {
			return applDocumentNumber;
		}
		public void setApplDocumentNumber(BigDecimal applDocumentNumber) {
			this.applDocumentNumber = applDocumentNumber;
		}

		@Column(name = "APPL_FINANCE_YEAR")
		public BigDecimal getApplDocumentFinanceYear() {
			return applDocumentFinanceYear;
		}
		public void setApplDocumentFinanceYear(BigDecimal applDocumentFinanceYear) {
			this.applDocumentFinanceYear = applDocumentFinanceYear;
		}

		@Column(name = "CHEQUE_CLEARANCE_IND")
		public String getChequeClearanceInd() {
			return chequeClearanceInd;
		}
		public void setChequeClearanceInd(String chequeClearanceInd) {
			this.chequeClearanceInd = chequeClearanceInd;
		}
		
		@Column(name = "COLLECTION_MODE")
		public String getCollectionMode() {
			return collectionMode;
		}
		public void setCollectionMode(String collectionMode) {
			this.collectionMode = collectionMode;
		}
		
		@Column(name = "ROUTING_BANK_ID")
		public BigDecimal getRoutingBankId() {
			return routingBankId;
		}
		public void setRoutingBankId(BigDecimal routingBankId) {
			this.routingBankId = routingBankId;
		}

		@Column(name = "CHEQUE_BANK_CODE")
		public BigDecimal getChequeBankCode() {
			return chequeBankCode;
		}
		public void setChequeBankCode(BigDecimal chequeBankCode) {
			this.chequeBankCode = chequeBankCode;
		}
		
		@Column(name = "CHEQUE_DATE")
		public Date getChequeDate() {
			return chequeDate;
		}
		public void setChequeDate(Date chequeDate) {
			this.chequeDate = chequeDate;
		}
		
		@Column(name = "CHEQUE_REFERENCE")
		public String getChequeReference() {
			return chequeReference;
		}
		public void setChequeReference(String chequeReference) {
			this.chequeReference = chequeReference;
		}
		
		@Column(name = "APPROVAL_NO")
		public String getApprovalNo() {
			return approvalNo;
		}
		public void setApprovalNo(String approvalNo) {
			this.approvalNo = approvalNo;
		}
		
		@Column(name = "DB_CARD_NAME")
		public String getDbCardName() {
			return dbCardName;
		}
		public void setDbCardName(String dbCardName) {
			this.dbCardName = dbCardName;
		}
		
		@Column(name = "DEBIT_CARD")
		public String getDebitCard() {
			return debitCard;
		}
		public void setDebitCard(String debitCard) {
			this.debitCard = debitCard;
		}
		
		@Column(name = "KNET_RECEIPT")
		public String getKnetReceipt() {
			return knetReceipt;
		}
		public void setKnetReceipt(String knetReceipt) {
			this.knetReceipt = knetReceipt;
		}
		
		@Column(name = "SERVICE_MASTER_ID")
		public BigDecimal getServiceMasterId() {
			return serviceMasterId;
		}
		public void setServiceMasterId(BigDecimal serviceMasterId) {
			this.serviceMasterId = serviceMasterId;
		}
		
		@Column(name = "ROUTING_COUNTRY_ID")
		public BigDecimal getRoutingCountryId() {
			return routingCountryId;
		}
		public void setRoutingCountryId(BigDecimal routingCountryId) {
			this.routingCountryId = routingCountryId;
		}
		
		@Column(name = "ROUTING_BANK_BRANCH_ID")
		public BigDecimal getRoutingBranchId() {
			return routingBranchId;
		}
		public void setRoutingBranchId(BigDecimal routingBranchId) {
			this.routingBranchId = routingBranchId;
		}
		
		@Column(name = "REMITTANCE_MODE_ID")
		public BigDecimal getRemittanceModeId() {
			return remittanceModeId;
		}
		public void setRemittanceModeId(BigDecimal remittanceModeId) {
			this.remittanceModeId = remittanceModeId;
		}
		
		@Column(name = "DELIVERY_MODE_ID")
		public BigDecimal getDeliveryModeId() {
			return deliveryModeId;
		}
		public void setDeliveryModeId(BigDecimal deliveryModeId) {
			this.deliveryModeId = deliveryModeId;
		}
		
		@Column(name = "KNET_RECEIPT_DATE_TIME")
		public String getKnetReceiptDateTime() {
			return knetReceiptDateTime;
		}
		public void setKnetReceiptDateTime(String knetReceiptDateTime) {
			this.knetReceiptDateTime = knetReceiptDateTime;
		}
		
		@Column(name = "SPECIAL_POOL_IND")
		public String getCustomerIndicator() {
			return customerIndicator;
		}
		public void setCustomerIndicator(String customerIndicator) {
			this.customerIndicator = customerIndicator;
		}

		@Column(name = "CUSTOMER_REFERENCE")
		public BigDecimal getCustomerreference() {
			return customerreference;
		}
		public void setCustomerreference(BigDecimal customerreference) {
			this.customerreference = customerreference;
		}

		@Column(name = "AREA")
		public String getAreaName() {
			return areaName;
		}
		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}

		@Column(name = "NEGOTIATE")
		public String getNegotiateSts() {
			return negotiateSts;
		}
		public void setNegotiateSts(String negotiateSts) {
			this.negotiateSts = negotiateSts;
		}

		@Column(name = "VALUE_DATE")
		public Date getValueDate() {
			return valueDate;
		}
		public void setValueDate(Date valueDate) {
			this.valueDate = valueDate;
		}

		@Column(name = "DESTINATION_CURRENCY_ID")
		public BigDecimal getDestinationCurrenyId() {
			return destinationCurrenyId;
		}
		public void setDestinationCurrenyId(BigDecimal destinationCurrenyId) {
			this.destinationCurrenyId = destinationCurrenyId;
		}
		
		@Column(name = "REQUEST_CURRENCY_ID")
		public BigDecimal getRequestCurrencyId() {
			return requestCurrencyId;
		}
		public void setRequestCurrencyId(BigDecimal requestCurrencyId) {
			this.requestCurrencyId = requestCurrencyId;
		}
		
		@Column(name = "APPOINTMENT_TIME")
		public Date getAppointmentTime() {
			return appointmentTime;
		}
		public void setAppointmentTime(Date appointmentTime) {
			this.appointmentTime = appointmentTime;
		}

		@Column(name = "REMARKS")
		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		@Column(name="BENEFICIARY_MASTER_ID")
		public BigDecimal getBeneficiaryMasterId() {
			return beneficiaryMasterId;
		}

		public void setBeneficiaryMasterId(BigDecimal beneficiaryMasterId) {
			this.beneficiaryMasterId = beneficiaryMasterId;
		}
		
		@Column(name="BENEFICARY_ACCOUNT_ID")
		public BigDecimal getAccountSeqquenceId() {
			return accountSeqquenceId;
		}

		public void setAccountSeqquenceId(BigDecimal accountSeqquenceId) {
			this.accountSeqquenceId = accountSeqquenceId;
		}
		
		@Column(name="BENEFICARY_RELATIONSHIP_SEQ_ID")
		public BigDecimal getBeneficiaryRelationId() {
			return beneficiaryRelationId;
		}

		public void setBeneficiaryRelationId(BigDecimal beneficiaryRelationId) {
			this.beneficiaryRelationId = beneficiaryRelationId;
		}
		
		
		@Column(name="DISCOUNT_ON_COMM")
		public BigDecimal getDiscountOnCommission() {
			return discountOnCommission;
		}

		public void setDiscountOnCommission(BigDecimal discountOnCommission) {
			this.discountOnCommission = discountOnCommission;
		}

		@Column(name="IS_DISCOUNT_AVAILED")
		public String getIsDiscountAvailed() {
			return isDiscountAvailed;
		}

		public void setIsDiscountAvailed(String isDiscountAvailed) {
			this.isDiscountAvailed = isDiscountAvailed;
		}

		@Column(name="CUSTCAT_DISCOUNT_ID")
		public BigDecimal getCusCatDiscountId() {
			return cusCatDiscountId;
		}

		public void setCusCatDiscountId(BigDecimal cusCatDiscountId) {
			this.cusCatDiscountId = cusCatDiscountId;
		}

		@Column(name="CUSTCAT_DISCOUNT")
		public BigDecimal getCusCatDiscount() {
			return cusCatDiscount;
		}

		public void setCusCatDiscount(BigDecimal cusCatDiscount) {
			this.cusCatDiscount = cusCatDiscount;
		}

		@Column(name="CHANNEL_DISCOUNT_ID")
		public BigDecimal getChannelDiscountId() {
			return channelDiscountId;
		}

		public void setChannelDiscountId(BigDecimal channelDiscountId) {
			this.channelDiscountId = channelDiscountId;
		}

		@Column(name="CHANNEL_DISCOUNT")
		public BigDecimal getChannelDiscount() {
			return channelDiscount;
		}

		public void setChannelDiscount(BigDecimal channelDiscount) {
			this.channelDiscount = channelDiscount;
		}

		@Column(name="PIPS_FROMAMT")
		public BigDecimal getPipsFromAmt() {
			return pipsFromAmt;
		}

		public void setPipsFromAmt(BigDecimal pipsFromAmt) {
			this.pipsFromAmt = pipsFromAmt;
		}

		@Column(name="PIPS_TOAMT")
		public BigDecimal getPipsToAmt() {
			return pipsToAmt;
		}

		public void setPipsToAmt(BigDecimal pipsToAmt) {
			this.pipsToAmt = pipsToAmt;
		}

		@Column(name="PIPS_DISCOUNT")
		public BigDecimal getPipsDiscount() {
			return pipsDiscount;
		}

		public void setPipsDiscount(BigDecimal pipsDiscount) {
			this.pipsDiscount = pipsDiscount;
		}

		@Column(name="REACHED_COST_RATE_LIMIT")
		public String getReachedCostRateLimit() {
			return reachedCostRateLimit;
		}

		public void setReachedCostRateLimit(String reachedCostRateLimit) {
			this.reachedCostRateLimit = reachedCostRateLimit;
		}

		@Column(name="REQUEST_MODEL")
		public String getRequestModel() {
			return requestModel;
		}

		public void setRequestModel(String requestModel) {
			this.requestModel = requestModel;
		}
		
		@Column(name="EXCHANGE_RATE_APPLIED")
		public BigDecimal getExchangeRateApplied() {
			return exchangeRateApplied;
		}

		public void setExchangeRateApplied(BigDecimal exchangeRateApplied) {
			this.exchangeRateApplied = exchangeRateApplied;
		}

		@Column(name="RACK_EXCHANGE_RATE")
		public BigDecimal getRackExchangeRate() {
			return rackExchangeRate;
		}

		public void setRackExchangeRate(BigDecimal rackExchangeRate) {
			this.rackExchangeRate = rackExchangeRate;
		}

		@Column(name="TERMINAL_ID")
		public String getTerminalId() {
			return terminalId;
		}

		
		
		public void setTerminalId(String terminalId) {
			this.terminalId = terminalId;
		}
		
		
		@Column(name="LOYALTY_POINTS_IND")
		public String getLoyaltyPointInd() {
			return loyaltyPointInd;
		}
		public void setLoyaltyPointInd(String loyaltyPointInd) {
			this.loyaltyPointInd = loyaltyPointInd;
		}

		@Column(name="AVG_COST")
		public BigDecimal getAvgCost() {
			return avgCost;
		}

		public void setAvgCost(BigDecimal avgCost) {
			this.avgCost = avgCost;
		}

		@Column(name="SAVED_AMOUNT")
		public BigDecimal getSavedLocalAmount() {
			return savedLocalAmount;
		}

		public void setSavedLocalAmount(BigDecimal savedLocalAmount) {
			this.savedLocalAmount = savedLocalAmount;
		}

		@Column(name="NEGOTIATE_COUNT")
		public BigDecimal getNegotiateCount() {
			return negotiateCount;
		}

		public void setNegotiateCount(BigDecimal negotiateCount) {
			this.negotiateCount = negotiateCount;
		}
	}

