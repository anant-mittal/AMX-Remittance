package com.amx.amxlib.meta.model;

import java.math.BigDecimal;
import java.util.List;

import com.amx.amxlib.model.PromotionDto;

public class RemittanceReportBean {

	// for remittance Application
	private String applicationNo;
	private String date;
	private String proposedCurrencyAndAmount;
	private String proposedExchangeRate;
	private String proposedKWDAmount;
	private String proposedCommission;
	private String proposedOtherCharges;
	private String totalKWDAmount;
	private String futherInstructions;
	private String benefPaymentMode;
	private String purposeOfRemittance;
	private String address;
	private String senderName;
	private String contactNumber;
	private String civilId;
	private String idExpiryDate;
	private String incomeSource;
	private String signature;
	private String cashierSignature;
	private String userName;

	// for receipt report
	private BigDecimal customerId;
	private String firstName;
	private String middleName;
	private BigDecimal mobileNo;
	private String insurence1;
	private String insurence2;
	private String location;
	private BigDecimal phoneNumber;
	private String receiptNo;
	private String transactionNo;
	private String beneficiaryName;
	private String benefeciaryBankName;
	private String benefeciaryBranchName;
	private String benefeciaryAccountNumber;
	private String perposeOfRemittance;
	private String paymentChannel;
	private String currencyAndAmount;
	private String exchangeRate;
	private String localTransactionAmount;
	private String commision;
	private String otherCharges;
	private String totalAmount;
	private String sourceOfIncome;
	private String intermediataryBank;
	private BigDecimal amountPaid;
	private BigDecimal amountRefund;
	private BigDecimal netPaidAmount;
	private String paymentMode;
	private String saleAmount;
	private String currencyQuoteName;
	private String beneSwiftBank1;
	private String beneSwiftBank2;
	private String beneSwiftAddr1;
	private String beneSwiftAddr2;
	private String instruction;
	private String loyalityPointExpiring;
	private BigDecimal noOfTransaction;
	private String pinNo;
	private String logoPath;

	private String purchageAmount;

	private String subReport;

	private List<RemittanceReportBean> collectionDetailList;

	private List<CollectionDetailBean> collectionViewList;

	private String engCompanyInfo;
	private String arabicCompanyInfo;

	// for collect mode
	private String collectionMode;
	private String approvalNo;
	private String transactionId;
	private String knetreceiptDateTime;
	private Boolean knetBooleanCheck = false;
	private Boolean drawLine = false;

	private String netAmount;
	private String paidAmount;
	private String refundedAmount;
	// private BigDecimal collectAmount;
	private String collectAmount;
	private List<PurposeOfRemittanceReportBean> purposeOfRemitTrList;

	private String lessLoyaltyEncasement;
	private String amountPayable;
	private PromotionDto promotionDto;

	// Receipt new fields
	private String branchExchangeRate;
	private String kwdAmount;
	private String specialExchangeRate;
	private String specialKwdAmount;
	private String vatNumber;
	private String vatDate;
	private String vatType;
	private BigDecimal vatPercentage;
	private String vatAmount;

	private String customerVatNumber;

	private String amountSaved;
	private String promotionMessage;
	private Boolean isArabic;
	private String totalAmountSavedStr;
	
	//Fields getting from KNET
	private String pgPaymentId;
	private String pgReferenceId;
	private String pgAuth;
	private String pgTransId;
	
	private BigDecimal discountOnCommission;
	private String discountOnCommissionStr;
	
	

	public String getTotalAmountSavedStr() {
		return totalAmountSavedStr;
	}

	public void setTotalAmountSavedStr(String totalAmountSavedStr) {
		this.totalAmountSavedStr = totalAmountSavedStr;
	}

	public Boolean getDrawLine() {
		return drawLine;
	}

	public void setDrawLine(Boolean drawLine) {
		this.drawLine = drawLine;
	}

	public Boolean getKnetBooleanCheck() {
		return knetBooleanCheck;
	}

	public void setKnetBooleanCheck(Boolean knetBooleanCheck) {
		this.knetBooleanCheck = knetBooleanCheck;
	}

	public String getApprovalNo() {
		return approvalNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getKnetreceiptDateTime() {
		return knetreceiptDateTime;
	}

	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setKnetreceiptDateTime(String knetreceiptDateTime) {
		this.knetreceiptDateTime = knetreceiptDateTime;
	}

	public String getPurchageAmount() {
		return purchageAmount;
	}

	public void setPurchageAmount(String purchageAmount) {
		this.purchageAmount = purchageAmount;
	}

	public String getCashierSignature() {
		return cashierSignature;
	}

	public void setCashierSignature(String cashierSignature) {
		this.cashierSignature = cashierSignature;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCollectAmount() {
		return collectAmount;
	}

	public void setCollectAmount(String collectAmount) {
		this.collectAmount = collectAmount;
	}

	public String getCollectionMode() {
		return collectionMode;
	}

	public String getNetAmount() {
		return netAmount;
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public String getRefundedAmount() {
		return refundedAmount;
	}

	public void setCollectionMode(String collectionMode) {
		this.collectionMode = collectionMode;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}

	public void setRefundedAmount(String refundedAmount) {
		this.refundedAmount = refundedAmount;
	}

	public String getSaleAmount() {
		return saleAmount;
	}

	public String getLoyalityPointExpiring() {
		return loyalityPointExpiring;
	}

	public BigDecimal getNoOfTransaction() {
		return noOfTransaction;
	}

	public String getPinNo() {
		return pinNo;
	}

	public void setLoyalityPointExpiring(String loyalityPointExpiring) {
		this.loyalityPointExpiring = loyalityPointExpiring;
	}

	public void setNoOfTransaction(BigDecimal noOfTransaction) {
		this.noOfTransaction = noOfTransaction;
	}

	public void setPinNo(String pinNo) {
		this.pinNo = pinNo;
	}

	public void setSaleAmount(String saleAmount) {
		this.saleAmount = saleAmount;
	}

	public String getCurrencyQuoteName() {
		return currencyQuoteName;
	}

	public void setCurrencyQuoteName(String currencyQuoteName) {
		this.currencyQuoteName = currencyQuoteName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getProposedCurrencyAndAmount() {
		return proposedCurrencyAndAmount;
	}

	public void setProposedCurrencyAndAmount(String proposedCurrencyAndAmount) {
		this.proposedCurrencyAndAmount = proposedCurrencyAndAmount;
	}

	public String getProposedExchangeRate() {
		return proposedExchangeRate;
	}

	public void setProposedExchangeRate(String proposedExchangeRate) {
		this.proposedExchangeRate = proposedExchangeRate;
	}

	public String getProposedKWDAmount() {
		return proposedKWDAmount;
	}

	public void setProposedKWDAmount(String proposedKWDAmount) {
		this.proposedKWDAmount = proposedKWDAmount;
	}

	public String getProposedCommission() {
		return proposedCommission;
	}

	public void setProposedCommission(String proposedCommission) {
		this.proposedCommission = proposedCommission;
	}

	public String getProposedOtherCharges() {
		return proposedOtherCharges;
	}

	public void setProposedOtherCharges(String proposedOtherCharges) {
		this.proposedOtherCharges = proposedOtherCharges;
	}

	public String getTotalKWDAmount() {
		return totalKWDAmount;
	}

	public void setTotalKWDAmount(String totalKWDAmount) {
		this.totalKWDAmount = totalKWDAmount;
	}

	public String getFutherInstructions() {
		return futherInstructions;
	}

	public void setFutherInstructions(String futherInstructions) {
		this.futherInstructions = futherInstructions;
	}

	public String getBenefPaymentMode() {
		return benefPaymentMode;
	}

	public void setBenefPaymentMode(String benefPaymentMode) {
		this.benefPaymentMode = benefPaymentMode;
	}

	public String getPurposeOfRemittance() {
		return purposeOfRemittance;
	}

	public void setPurposeOfRemittance(String purposeOfRemittance) {
		this.purposeOfRemittance = purposeOfRemittance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getCivilId() {
		return civilId;
	}

	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}

	public String getIdExpiryDate() {
		return idExpiryDate;
	}

	public void setIdExpiryDate(String idExpiryDate) {
		this.idExpiryDate = idExpiryDate;
	}

	public String getIncomeSource() {
		return incomeSource;
	}

	public void setIncomeSource(String incomeSource) {
		this.incomeSource = incomeSource;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public BigDecimal getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(BigDecimal mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getInsurence1() {
		return insurence1;
	}

	public void setInsurence1(String insurence1) {
		this.insurence1 = insurence1;
	}

	public String getInsurence2() {
		return insurence2;
	}

	public void setInsurence2(String insurence2) {
		this.insurence2 = insurence2;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(BigDecimal phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBenefeciaryBankName() {
		return benefeciaryBankName;
	}

	public void setBenefeciaryBankName(String benefeciaryBankName) {
		this.benefeciaryBankName = benefeciaryBankName;
	}

	public String getBenefeciaryBranchName() {
		return benefeciaryBranchName;
	}

	public void setBenefeciaryBranchName(String benefeciaryBranchName) {
		this.benefeciaryBranchName = benefeciaryBranchName;
	}

	public String getBenefeciaryAccountNumber() {
		return benefeciaryAccountNumber;
	}

	public void setBenefeciaryAccountNumber(String benefeciaryAccountNumber) {
		this.benefeciaryAccountNumber = benefeciaryAccountNumber;
	}

	public String getPerposeOfRemittance() {
		return perposeOfRemittance;
	}

	public void setPerposeOfRemittance(String perposeOfRemittance) {
		this.perposeOfRemittance = perposeOfRemittance;
	}

	public String getPaymentChannel() {
		return paymentChannel;
	}

	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}

	public String getCurrencyAndAmount() {
		return currencyAndAmount;
	}

	public void setCurrencyAndAmount(String currencyAndAmount) {
		this.currencyAndAmount = currencyAndAmount;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getLocalTransactionAmount() {
		return localTransactionAmount;
	}

	public void setLocalTransactionAmount(String localTransactionAmount) {
		this.localTransactionAmount = localTransactionAmount;
	}

	public String getCommision() {
		return commision;
	}

	public void setCommision(String commision) {
		this.commision = commision;
	}

	public String getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(String otherCharges) {
		this.otherCharges = otherCharges;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSourceOfIncome() {
		return sourceOfIncome;
	}

	public void setSourceOfIncome(String sourceOfIncome) {
		this.sourceOfIncome = sourceOfIncome;
	}

	public String getIntermediataryBank() {
		return intermediataryBank;
	}

	public void setIntermediataryBank(String intermediataryBank) {
		this.intermediataryBank = intermediataryBank;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public BigDecimal getAmountRefund() {
		return amountRefund;
	}

	public void setAmountRefund(BigDecimal amountRefund) {
		this.amountRefund = amountRefund;
	}

	public BigDecimal getNetPaidAmount() {
		return netPaidAmount;
	}

	public void setNetPaidAmount(BigDecimal netPaidAmount) {
		this.netPaidAmount = netPaidAmount;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getSubReport() {
		return subReport;
	}

	public void setSubReport(String subReport) {
		this.subReport = subReport;
	}

	public List<RemittanceReportBean> getCollectionDetailList() {
		return collectionDetailList;
	}

	public void setCollectionDetailList(List<RemittanceReportBean> collectionDetailList) {
		this.collectionDetailList = collectionDetailList;
	}

	/*
	 * public List<RemittanceCollectionBean> getCollectionAppList() { return
	 * collectionAppList; }
	 * 
	 * public void setCollectionAppList(List<RemittanceCollectionBean>
	 * collectionAppList) { this.collectionAppList = collectionAppList; }
	 */

	public List<CollectionDetailBean> getCollectionViewList() {
		return collectionViewList;
	}

	public void setCollectionViewList(List<CollectionDetailBean> collectionViewList) {
		this.collectionViewList = collectionViewList;
	}

	public String getBeneSwiftBank1() {
		return beneSwiftBank1;
	}

	public void setBeneSwiftBank1(String beneSwiftBank1) {
		this.beneSwiftBank1 = beneSwiftBank1;
	}

	public String getBeneSwiftBank2() {
		return beneSwiftBank2;
	}

	public void setBeneSwiftBank2(String beneSwiftBank2) {
		this.beneSwiftBank2 = beneSwiftBank2;
	}

	public String getBeneSwiftAddr1() {
		return beneSwiftAddr1;
	}

	public void setBeneSwiftAddr1(String beneSwiftAddr1) {
		this.beneSwiftAddr1 = beneSwiftAddr1;
	}

	public String getBeneSwiftAddr2() {
		return beneSwiftAddr2;
	}

	public void setBeneSwiftAddr2(String beneSwiftAddr2) {
		this.beneSwiftAddr2 = beneSwiftAddr2;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public List<PurposeOfRemittanceReportBean> getPurposeOfRemitTrList() {
		return purposeOfRemitTrList;
	}

	public void setPurposeOfRemitTrList(List<PurposeOfRemittanceReportBean> purposeOfRemitTrList) {
		this.purposeOfRemitTrList = purposeOfRemitTrList;
	}

	public String getAmountPayable() {
		return amountPayable;
	}

	public void setAmountPayable(String amountPayable) {
		this.amountPayable = amountPayable;
	}

	public String getLessLoyaltyEncasement() {
		return lessLoyaltyEncasement;
	}

	public void setLessLoyaltyEncasement(String lessLoyaltyEncasement) {
		this.lessLoyaltyEncasement = lessLoyaltyEncasement;
	}

	public String getEngCompanyInfo() {
		return engCompanyInfo;
	}

	public void setEngCompanyInfo(String engCompanyInfo) {
		this.engCompanyInfo = engCompanyInfo;
	}

	public String getArabicCompanyInfo() {
		return arabicCompanyInfo;
	}

	public void setArabicCompanyInfo(String arabicCompanyInfo) {
		this.arabicCompanyInfo = arabicCompanyInfo;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public void setPromotionDto(PromotionDto promotionDto) {
		this.promotionDto = promotionDto;
	}

	public PromotionDto getPromotionDto() {
		return promotionDto;
	}

	public String getBranchExchangeRate() {
		return branchExchangeRate;
	}

	public void setBranchExchangeRate(String branchExchangeRate) {
		this.branchExchangeRate = branchExchangeRate;
	}

	public String getKwdAmount() {
		return kwdAmount;
	}

	public void setKwdAmount(String kwdAmount) {
		this.kwdAmount = kwdAmount;
	}

	public String getSpecialExchangeRate() {
		return specialExchangeRate;
	}

	public void setSpecialExchangeRate(String specialExchangeRate) {
		this.specialExchangeRate = specialExchangeRate;
	}

	public String getSpecialKwdAmount() {
		return specialKwdAmount;
	}

	public void setSpecialKwdAmount(String specialKwdAmount) {
		this.specialKwdAmount = specialKwdAmount;
	}

	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}

	public String getVatDate() {
		return vatDate;
	}

	public String getVatType() {
		return vatType;
	}

	public void setVatType(String vatType) {
		this.vatType = vatType;
	}

	public BigDecimal getVatPercentage() {
		return vatPercentage;
	}

	public void setVatPercentage(BigDecimal vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	public String getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(String vatAmount) {
		this.vatAmount = vatAmount;
	}

	public String getCustomerVatNumber() {
		return customerVatNumber;
	}

	public void setCustomerVatNumber(String customerVatNumber) {
		this.customerVatNumber = customerVatNumber;
	}

	public void setVatDate(String vatDate) {
		this.vatDate = vatDate;
	}

	public String getAmountSaved() {
		return amountSaved;
	}

	public Boolean getIsArabic() {
		return isArabic;
	}

	public void setAmountSaved(String amountSaved) {
		this.amountSaved = amountSaved;
	}

	public String getPromotionMessage() {
		return promotionMessage;
	}

	public void setPromotionMessage(String promotionMessage) {
		this.promotionMessage = promotionMessage;
	}

	public void setIsArabic(Boolean isArabic) {
		this.isArabic = isArabic;
	}

	public String getPgPaymentId() {
		return pgPaymentId;
	}

	public void setPgPaymentId(String pgPaymentId) {
		this.pgPaymentId = pgPaymentId;
	}

	public String getPgReferenceId() {
		return pgReferenceId;
	}

	public void setPgReferenceId(String pgReferenceId) {
		this.pgReferenceId = pgReferenceId;
	}

	public String getPgAuth() {
		return pgAuth;
	}

	public void setPgAuth(String pgAuth) {
		this.pgAuth = pgAuth;
	}

	public String getPgTransId() {
		return pgTransId;
	}

	public void setPgTransId(String pgTransId) {
		this.pgTransId = pgTransId;
	}

	public BigDecimal getDiscountOnCommission() {
		return discountOnCommission;
	}

	public void setDiscountOnCommission(BigDecimal discountOnCommission) {
		this.discountOnCommission = discountOnCommission;
	}

	public String getDiscountOnCommissionStr() {
		return discountOnCommissionStr;
	}

	public void setDiscountOnCommissionStr(String discountOnCommissionStr) {
		this.discountOnCommissionStr = discountOnCommissionStr;
	}

}
