package com.amx.jax.dbmodel.remittance;

// Generated Jan 23, 2015 1:10:41 PM by Hibernate Tools 3.4.0.CR1

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.UserFinancialYear;


/**
 * ExRemittanceApplication generated by hbm2java
 */
@Entity
@Table(name = "EX_APPL_TRNX")
public class RemittanceApplication implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal remittanceApplicationId;
	private CurrencyMasterModel exCurrencyMasterByForeignCurrencyId;
	private Document exDocument;
	private CompanyMaster fsCompanyMaster;
	private UserFinancialYear exUserFinancialYearByTransactionFinanceYearID;
	private CountryBranch exCountryBranch;
	private CurrencyMasterModel exCurrencyMasterByLocalCommisionCurrencyId;
	private CurrencyMasterModel exCurrencyMasterByLocalTranxCurrencyId;
	private CountryMaster fsCountryMasterByBankCountryId;
	private CountryMaster fsCountryMasterByApplicationCountryId;
	private DeliveryMode exDeliveryMode;
	private CurrencyMasterModel exCurrencyMasterByLocalChargeCurrencyId;
	private RemittanceModeMaster exRemittanceMode;
	private Customer fsCustomer;
	private UserFinancialYear exUserFinancialYearByDocumentFinanceYear;
	private CurrencyMasterModel exCurrencyMasterByLocalNetCurrencyId;
	private CurrencyMasterModel exCurrencyMasterByLocalDeliveryCurrencyId;
	private BigDecimal documentNo;
	private Date documentDate;
	private BigDecimal transactionDocumentNo;
	private BigDecimal customerRef;
	private BankMasterModel exBankMaster;
	private BankBranch exBankBranch;
	private String debitAccountNo;
	private BigDecimal foreignTranxAmount;
	private BigDecimal localTranxAmount;
	private BigDecimal exchangeRateApplied;
	private BigDecimal localCommisionAmount;
	private BigDecimal localChargeAmount;
	private BigDecimal localDeliveryAmount;
	private BigDecimal localNetTranxAmount;
	private String applicaitonStatus;
	private String blackListIndicator;
	private Date accountMmyyyy;
	private String westernUnionMtcno;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	private String isactive;
	private BigDecimal loyaltyPointsEncashed;
	private BigDecimal documentFinancialyear;
	private BigDecimal transactionFinancialyear;
	private String spldeal;
	private String paymentId;
	private String payToken;
	private String customerSignature;
	private Clob customerSignatureClob;
	private BigDecimal sourceofincome;
	private String resultCode;
	private BigDecimal averageRate;
	private BigDecimal currentaverageRate;
	private String faAccountNumber;
	private Date averageRateDt;
	private String instruction;
	private BigDecimal selectedCurrencyId;
	private String spotRateInd;
	private String loyaltyPointInd;
	private String customerName;
	private BigDecimal originalExchangeRate;
	private BigDecimal approvalNumber;
	private BigDecimal approvalYear;	
	private String agentCode;
	private String subAgentCode;
	private String modeofTransfer;
	private BigDecimal usdAmt;
	private BigDecimal srvPrvSettleRate;
	private BigDecimal employeeId;
	private String applInd;
	private BigDecimal loccod;
	private BigDecimal documentCode;
	private BigDecimal companyCode;
	
	private String pgReferenceId;
	private String pgTransactionId;
	private String pgAuthCode;
	private String pgErrorText;
	private String pgReceiptDate;
	private String errorMessage;
	private String wuIpAddress;
	private String errorCategory;
	private BigDecimal discountOnCommission;
	
	/** added by rabil on 10 apr 2019 for customer category dis analysis**/
	private String isDiscountAvailed;
	private BigDecimal cusCatDiscountId;
	private BigDecimal cusCatDiscount;
	private BigDecimal channelDiscountId;
	private BigDecimal channelDiscount;
	private BigDecimal pipsFromAmt;
	private BigDecimal pipsToAmt;
	private BigDecimal pipsDiscount;
	private String reachedCostRateLimit;
	
	private String vatType;
	private BigDecimal vatPercentage;
	private BigDecimal vatAmount;
	/** added by rabil **/
	private String beneDeductFlag;
	private String wuPurposeOfTransaction;
	private BigDecimal savedAmount;
	/** lowest exchange rate amount all the branches **/
	private BigDecimal rackExchangeRate;
	private String customerChoice;


	private List<RemittanceAppBenificiary> exRemittanceAppBenificiary = new ArrayList<RemittanceAppBenificiary>(0);
	
	public RemittanceApplication() {
		super();
	}
	
	@Id
	@GeneratedValue(generator="ex_remittance_application_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_remittance_application_seq",sequenceName="EX_REMITTANCE_APPLICATION_SEQ",allocationSize=1)
	@Column(name = "REMITTANCE_APPLICATION_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getRemittanceApplicationId() {
		return this.remittanceApplicationId;
	}
	public void setRemittanceApplicationId(BigDecimal remittanceApplicationId) {
		this.remittanceApplicationId = remittanceApplicationId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FOREIGN_CURRENCY_ID")
	public CurrencyMasterModel getExCurrencyMasterByForeignCurrencyId() {
		return this.exCurrencyMasterByForeignCurrencyId;
	}
	public void setExCurrencyMasterByForeignCurrencyId(CurrencyMasterModel exCurrencyMasterByForeignCurrencyId) {
		this.exCurrencyMasterByForeignCurrencyId = exCurrencyMasterByForeignCurrencyId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOCUMENT_ID")
	public Document getExDocument() {
		return this.exDocument;
	}
	public void setExDocument(Document exDocument) {
		this.exDocument = exDocument;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPANY_ID")
	public CompanyMaster getFsCompanyMaster() {
		return this.fsCompanyMaster;
	}
	public void setFsCompanyMaster(CompanyMaster fsCompanyMaster) {
		this.fsCompanyMaster = fsCompanyMaster;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRANSACTION_FINANCE_YEAR_ID")
	public UserFinancialYear getExUserFinancialYearByTransactionFinanceYearID() {
		return exUserFinancialYearByTransactionFinanceYearID;
	}
	public void setExUserFinancialYearByTransactionFinanceYearID(UserFinancialYear exUserFinancialYearByTransactionFinanceYearID) {
		this.exUserFinancialYearByTransactionFinanceYearID = exUserFinancialYearByTransactionFinanceYearID;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COUNTRY_BRANCH_ID")
	public CountryBranch getExCountryBranch() {
		return this.exCountryBranch;
	}
	public void setExCountryBranch(CountryBranch exCountryBranch) {
		this.exCountryBranch = exCountryBranch;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCAL_COMMISION_CURRENCY_ID")
	public CurrencyMasterModel getExCurrencyMasterByLocalCommisionCurrencyId() {
		return this.exCurrencyMasterByLocalCommisionCurrencyId;
	}
	public void setExCurrencyMasterByLocalCommisionCurrencyId(CurrencyMasterModel exCurrencyMasterByLocalCommisionCurrencyId) {
		this.exCurrencyMasterByLocalCommisionCurrencyId = exCurrencyMasterByLocalCommisionCurrencyId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCAL_TRANX_CURRENCY_ID")
	public CurrencyMasterModel getExCurrencyMasterByLocalTranxCurrencyId() {
		return this.exCurrencyMasterByLocalTranxCurrencyId;
	}
	public void setExCurrencyMasterByLocalTranxCurrencyId(CurrencyMasterModel exCurrencyMasterByLocalTranxCurrencyId) {
		this.exCurrencyMasterByLocalTranxCurrencyId = exCurrencyMasterByLocalTranxCurrencyId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BANK_COUNTRY_ID")
	public CountryMaster getFsCountryMasterByBankCountryId() {
		return this.fsCountryMasterByBankCountryId;
	}
	public void setFsCountryMasterByBankCountryId(CountryMaster fsCountryMasterByBankCountryId) {
		this.fsCountryMasterByBankCountryId = fsCountryMasterByBankCountryId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPLICATION_COUNTRY_ID")
	public CountryMaster getFsCountryMasterByApplicationCountryId() {
		return this.fsCountryMasterByApplicationCountryId;
	}
	public void setFsCountryMasterByApplicationCountryId(CountryMaster fsCountryMasterByApplicationCountryId) {
		this.fsCountryMasterByApplicationCountryId = fsCountryMasterByApplicationCountryId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DELIVERY_MODE_ID")
	public DeliveryMode getExDeliveryMode() {
		return this.exDeliveryMode;
	}
	public void setExDeliveryMode(DeliveryMode exDeliveryMode) {
		this.exDeliveryMode = exDeliveryMode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCAL_CHARGE_CURRENCY_ID")
	public CurrencyMasterModel getExCurrencyMasterByLocalChargeCurrencyId() {
		return this.exCurrencyMasterByLocalChargeCurrencyId;
	}
	public void setExCurrencyMasterByLocalChargeCurrencyId(CurrencyMasterModel exCurrencyMasterByLocalChargeCurrencyId) {
		this.exCurrencyMasterByLocalChargeCurrencyId = exCurrencyMasterByLocalChargeCurrencyId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REMITTANCE_MODE_ID")
	public RemittanceModeMaster getExRemittanceMode() {
		return this.exRemittanceMode;
	}
	public void setExRemittanceMode(RemittanceModeMaster exRemittanceMode) {
		this.exRemittanceMode = exRemittanceMode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	public Customer getFsCustomer() {
		return this.fsCustomer;
	}
	public void setFsCustomer(Customer fsCustomer) {
		this.fsCustomer = fsCustomer;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOCUMENT_FINANCE_YEAR_ID")
	public UserFinancialYear getExUserFinancialYearByDocumentFinanceYear() {
		return this.exUserFinancialYearByDocumentFinanceYear;
	}
	public void setExUserFinancialYearByDocumentFinanceYear(UserFinancialYear exUserFinancialYearByDocumentFinanceYear) {
		this.exUserFinancialYearByDocumentFinanceYear = exUserFinancialYearByDocumentFinanceYear;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCAL_NET_CURRENCY_ID")
	public CurrencyMasterModel getExCurrencyMasterByLocalNetCurrencyId() {
		return this.exCurrencyMasterByLocalNetCurrencyId;
	}
	public void setExCurrencyMasterByLocalNetCurrencyId(CurrencyMasterModel exCurrencyMasterByLocalNetCurrencyId) {
		this.exCurrencyMasterByLocalNetCurrencyId = exCurrencyMasterByLocalNetCurrencyId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOCAL_DELIVERY_CURRENCY_ID")
	public CurrencyMasterModel getExCurrencyMasterByLocalDeliveryCurrencyId() {
		return this.exCurrencyMasterByLocalDeliveryCurrencyId;
	}
	public void setExCurrencyMasterByLocalDeliveryCurrencyId(CurrencyMasterModel exCurrencyMasterByLocalDeliveryCurrencyId) {
		this.exCurrencyMasterByLocalDeliveryCurrencyId = exCurrencyMasterByLocalDeliveryCurrencyId;
	}

	@Column(name = "DOCUMENT_NO", precision = 14, scale = 0)
	public BigDecimal getDocumentNo() {
		return this.documentNo;
	}
	public void setDocumentNo(BigDecimal documentNo) {
		this.documentNo = documentNo;
	}

	@Column(name = "DOCUMENT_DATE", length = 7)
	public Date getDocumentDate() {
		return this.documentDate;
	}
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	@Column(name = "TRANSACTION_DOCUMENT_NO", precision = 14, scale = 0)
	public BigDecimal getTransactionDocumentNo() {
		return transactionDocumentNo;
	}
	public void setTransactionDocumentNo(BigDecimal transactionDocumentNo) {
		this.transactionDocumentNo = transactionDocumentNo;
	}

	@Column(name = "CUSTOMER_REFERENCE", precision = 22, scale = 0)
	public BigDecimal getCustomerRef() {
		return this.customerRef;
	}
	public void setCustomerRef(BigDecimal customerRef) {
		this.customerRef = customerRef;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BANK_ID")
	public BankMasterModel getExBankMaster() {
		return exBankMaster;
	}
	public void setExBankMaster(BankMasterModel exBankMaster) {
		this.exBankMaster = exBankMaster;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BANK_BRANCH_ID")
	public BankBranch getExBankBranch() {
		return exBankBranch;
	}
	public void setExBankBranch(BankBranch exBankBranch) {
		this.exBankBranch = exBankBranch;
	}

	@Column(name = "DEBIT_ACCOUNT_NO", length = 60)
	public String getDebitAccountNo() {
		return this.debitAccountNo;
	}
	public void setDebitAccountNo(String debitAccountNo) {
		this.debitAccountNo = debitAccountNo;
	}

	@Column(name = "FOREIGN_TRANX_AMOUNT", precision = 22, scale = 3)
	public BigDecimal getForeignTranxAmount() {
		return this.foreignTranxAmount;
	}
	public void setForeignTranxAmount(BigDecimal foreignTranxAmount) {
		this.foreignTranxAmount = foreignTranxAmount;
	}

	@Column(name = "LOCAL_TRANX_AMOUNT", precision = 18, scale = 3)
	public BigDecimal getLocalTranxAmount() {
		return this.localTranxAmount;
	}
	public void setLocalTranxAmount(BigDecimal localTranxAmount) {
		this.localTranxAmount = localTranxAmount;
	}

	@Column(name = "EXCHANGE_RATE_APPLIED", precision = 22, scale = 0)
	public BigDecimal getExchangeRateApplied() {
		return this.exchangeRateApplied;
	}
	public void setExchangeRateApplied(BigDecimal exchangeRateApplied) {
		this.exchangeRateApplied = exchangeRateApplied;
	}

	@Column(name = "LOCAL_COMMISION_AMOUNT", precision = 18, scale = 3)
	public BigDecimal getLocalCommisionAmount() {
		return this.localCommisionAmount;
	}
	public void setLocalCommisionAmount(BigDecimal localCommisionAmount) {
		this.localCommisionAmount = localCommisionAmount;
	}

	@Column(name = "LOCAL_CHARGE_AMOUNT", precision = 18, scale = 3)
	public BigDecimal getLocalChargeAmount() {
		return this.localChargeAmount;
	}
	public void setLocalChargeAmount(BigDecimal localChargeAmount) {
		this.localChargeAmount = localChargeAmount;
	}

	@Column(name = "LOCAL_DELIVERY_AMOUNT", precision = 18, scale = 3)
	public BigDecimal getLocalDeliveryAmount() {
		return this.localDeliveryAmount;
	}
	public void setLocalDeliveryAmount(BigDecimal localDeliveryAmount) {
		this.localDeliveryAmount = localDeliveryAmount;
	}

	@Column(name = "LOCAL_NET_TRANX_AMOUNT", precision = 18, scale = 3)
	public BigDecimal getLocalNetTranxAmount() {
		return this.localNetTranxAmount;
	}
	public void setLocalNetTranxAmount(BigDecimal localNetTranxAmount) {
		this.localNetTranxAmount = localNetTranxAmount;
	}

	@Column(name = "APPLICATION_STATUS", length = 1)
	public String getApplicaitonStatus() {
		return this.applicaitonStatus;
	}
	public void setApplicaitonStatus(String applicaitonStatus) {
		this.applicaitonStatus = applicaitonStatus;
	}

	@Column(name = "BLACK_LIST_INDICATOR", length = 1)
	public String getBlackListIndicator() {
		return this.blackListIndicator;
	}

	public void setBlackListIndicator(String blackListIndicator) {
		this.blackListIndicator = blackListIndicator;
	}

	
	@Column(name = "ACCOUNT_MMYYYY", length = 7)
	public Date getAccountMmyyyy() {
		return this.accountMmyyyy;
	}
	public void setAccountMmyyyy(Date accountMmyyyy) {
		this.accountMmyyyy = accountMmyyyy;
	}

	@Column(name = "WESTERN_UNION_MTCNO", length = 30)
	public String getWesternUnionMtcno() {
		return this.westernUnionMtcno;
	}
	public void setWesternUnionMtcno(String westernUnionMtcno) {
		this.westernUnionMtcno = westernUnionMtcno;
	}

	
	@Column(name = "CREATED_DATE", length = 7)
	public Date getCreatedDate() {
		return this.createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "CREATED_BY", length = 15)
	public String getCreatedBy() {
		return this.createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "MODIFIED_DATE", length = 7)
	public Date getModifiedDate() {
		return this.modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "MODIFIED_BY", length = 15)
	public String getModifiedBy() {
		return this.modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "ISACTIVE", length = 1)
	public String getIsactive() {
		return this.isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}

	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "exRemittanceAppfromAml")
	public Set<RemitApplAmlModel> getExRemitApplAmls() {
		return this.exRemitApplAmls;
	}
	public void setExRemitApplAmls(Set<RemitApplAmlModel> exRemitApplAmls) {
		this.exRemitApplAmls = exRemitApplAmls;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exRemittanceApplication")
	public Set<AdditionalInstructionData> getExAdditionalInstructionDatas() {
		return this.exAdditionalInstructionDatas;
	}
	public void setExAdditionalInstructionDatas(
			Set<AdditionalInstructionData> exAdditionalInstructionDatas) {
		this.exAdditionalInstructionDatas = exAdditionalInstructionDatas;
	}*/

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "exRemittanceAppfromBenfi")
	public List<RemittanceAppBenificiary> getExRemittanceAppBenificiary() {
		return exRemittanceAppBenificiary;
	}
	public void setExRemittanceAppBenificiary(
			List<RemittanceAppBenificiary> exRemittanceAppBenificiary) {
		this.exRemittanceAppBenificiary = exRemittanceAppBenificiary;
	}

	@Column(name = "LOYALTY_POINTS_ENCASHED")
	public BigDecimal getLoyaltyPointsEncashed() {
		return loyaltyPointsEncashed;
	}
	public void setLoyaltyPointsEncashed(BigDecimal loyaltyPointsEncashed) {
		this.loyaltyPointsEncashed = loyaltyPointsEncashed;
	}
	
	@Column(name = "DOCUMENT_FINANCE_YEAR")
	public BigDecimal getDocumentFinancialyear() {
		return documentFinancialyear;
	}
	public void setDocumentFinancialyear(BigDecimal documentFinancialyear) {
		this.documentFinancialyear = documentFinancialyear;
	}
	
	@Column(name = "TRANSACTION_FINANCE_YEAR")
	public BigDecimal getTransactionFinancialyear() {
		return transactionFinancialyear;
	}
	public void setTransactionFinancialyear(BigDecimal transactionFinancialyear) {
		this.transactionFinancialyear = transactionFinancialyear;
	}

	@Column(name = "IS_SPECIAL_DEAL")
	public String getSpldeal() {
		return spldeal;
	}
	public void setSpldeal(String spldeal) {
		this.spldeal = spldeal;
	}

	@Column(name = "PAYMENT_ID")
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	@Column(name = "PAY_TOKEN")
	public String getPayToken() {
		return payToken;
	}
	public void setPayToken(String payToken) {
		this.payToken = payToken;
	}

	@Column(name = "SIGNATURE_SPECIMEN")
	public String getCustomerSignature() {
		return customerSignature;
	}
	public void setCustomerSignature(String customerSignature) {
		this.customerSignature = customerSignature;
	}
	
	
	@Column(name = "SOURCE_OF_INCOME_ID")
	public BigDecimal getSourceofincome() {
		return sourceofincome;
	}
	public void setSourceofincome(BigDecimal sourceofincome) {
		this.sourceofincome = sourceofincome;
	}

	@Column(name = "RESULT_CODE")
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	@Column(name = "AVERAGE_RATE")
	public BigDecimal getAverageRate() {
		return averageRate;
	}
	public void setAverageRate(BigDecimal averageRate) {
		this.averageRate = averageRate;
	}

	@Column(name = "CURRENT_AVG_RATE")
	public BigDecimal getCurrentaverageRate() {
		return currentaverageRate;
	}
	public void setCurrentaverageRate(BigDecimal currentaverageRate) {
		this.currentaverageRate = currentaverageRate;
	}

	@Column(name = "FA_ACCOUNT_NUMBER")
	public String getFaAccountNumber() {
		return faAccountNumber;
	}
	public void setFaAccountNumber(String faAccountNumber) {
		this.faAccountNumber = faAccountNumber;
	}

	@Column(name = "AVERAGE_RATE_DT")
	public Date getAverageRateDt() {
		return averageRateDt;
	}
	public void setAverageRateDt(Date averageRateDt) {
		this.averageRateDt = averageRateDt;
	}
	
	@Column(name = "INSTRUCTION")
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	@Column(name="SIGNATURE_SPECIMEN_CLOB")
	public Clob getCustomerSignatureClob() {
		return customerSignatureClob;
	}
	public void setCustomerSignatureClob(Clob customerSignatureClob) {
		this.customerSignatureClob = customerSignatureClob;
	}

	@Column(name="SELECTED_CURRENCY_ID")
	public BigDecimal getSelectedCurrencyId() {
		return selectedCurrencyId;
	}
	public void setSelectedCurrencyId(BigDecimal selectedCurrencyId) {
		this.selectedCurrencyId = selectedCurrencyId;
	}

	@Column(name="SPOT_RATE_IND")
	public String getSpotRateInd() {
		return spotRateInd;
	}
	public void setSpotRateInd(String spotRateInd) {
		this.spotRateInd = spotRateInd;
	}

	@Column(name="LOYALTY_POINTS_IND")
	public String getLoyaltyPointInd() {
		return loyaltyPointInd;
	}
	public void setLoyaltyPointInd(String loyaltyPointInd) {
		this.loyaltyPointInd = loyaltyPointInd;
	}

	@Column(name="CUSTOMER_NAME")
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Column(name="ORIGINAL_EXCHANGE_RATE")
	public BigDecimal getOriginalExchangeRate() {
		return originalExchangeRate;
	}
	public void setOriginalExchangeRate(BigDecimal originalExchangeRate) {
		this.originalExchangeRate = originalExchangeRate;
	}

	@Column(name="APPROVAL_NO")
	public BigDecimal getApprovalNumber() {
		return approvalNumber;
	}
	public void setApprovalNumber(BigDecimal approvalNumber) {
		this.approvalNumber = approvalNumber;
	}
   
	@Column(name="APPROVAL_YEAR")
  	public BigDecimal getApprovalYear() {
		return approvalYear;
	}
	public void setApprovalYear(BigDecimal approvalYear) {
		this.approvalYear = approvalYear;
	}

	@Column(name="AGENT_CODE")
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	@Column(name="SUB_AGENT_CODE")
	public String getSubAgentCode() {
		return subAgentCode;
	}
	public void setSubAgentCode(String subAgentCode) {
		this.subAgentCode = subAgentCode;
	}

	@Column(name="MODE_OF_TRANSFER")
	public String getModeofTransfer() {
		return modeofTransfer;
	}
	public void setModeofTransfer(String modeofTransfer) {
		this.modeofTransfer = modeofTransfer;
	}

	@Column(name="USD_AMT")
	public BigDecimal getUsdAmt() {
		return usdAmt;
	}
	public void setUsdAmt(BigDecimal usdAmt) {
		this.usdAmt = usdAmt;
	}

	@Column(name="SRV_PROV_SETTLE_RATE")
	public BigDecimal getSrvPrvSettleRate() {
		return srvPrvSettleRate;
	}
	public void setSrvPrvSettleRate(BigDecimal srvPrvSettleRate) {
		this.srvPrvSettleRate = srvPrvSettleRate;
	}
	
	@Column(name="EMPLOYEE_ID")
	public BigDecimal getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

	@Column(name="APPL_IND")
	public String getApplInd() {
		return applInd;
	}
	public void setApplInd(String applInd) {
		this.applInd = applInd;
	}

	@Column(name="LOCCOD")
	public BigDecimal getLoccod() {
		return loccod;
	}
	public void setLoccod(BigDecimal loccod) {
		this.loccod = loccod;
	}

	@Column(name="DOCUMENT_CODE")
	public BigDecimal getDocumentCode() {
		return documentCode;
	}
	public void setDocumentCode(BigDecimal documentCode) {
		this.documentCode = documentCode;
	}

	@Column(name="COMPANY_CODE")
	public BigDecimal getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(BigDecimal companyCode) {
		this.companyCode = companyCode;
	}
	
	
	@Column(name="PG_REFERENCE_ID")
	public String getPgReferenceId() {
		return pgReferenceId;
	}
	public void setPgReferenceId(String pgReferenceId) {
		this.pgReferenceId = pgReferenceId;
	}
	@Column(name="PG_TRANSACTION_ID")
	public String getPgTransactionId() {
		return pgTransactionId;
	}
	public void setPgTransactionId(String pgTransactionId) {
		this.pgTransactionId = pgTransactionId;
	}
	@Column(name="PG_AUTH_CODE")
	public String getPgAuthCode() {
		return pgAuthCode;
	}
	public void setPgAuthCode(String pgAuthCode) {
		this.pgAuthCode = pgAuthCode;
	}
	@Column(name="PG_ERROR_TEXT")
	public String getPgErrorText() {
		return pgErrorText;
	}
	public void setPgErrorText(String pgErrorText) {
		this.pgErrorText = pgErrorText;
	}
	
	@Column(name="PG_RCEIPT_DATE")
	public String getPgReceiptDate() {
		return pgReceiptDate;
	}
	
	public void setPgReceiptDate(String pgReceiptDate) {
		this.pgReceiptDate = pgReceiptDate;
	}
	
	@Column(name="WU_TERMINAL_ADDRESS")
    public String getWuIpAddress() {
           return wuIpAddress;
    }

    public void setWuIpAddress(String wuIpAddress) {
           this.wuIpAddress = wuIpAddress;
    }

	@Column(name="ERROR_MESSAGE")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Column(name="ERROR_CATEGORY")
	public String getErrorCategory() {
		return errorCategory;
	}

	public void setErrorCategory(String errorCategory) {
		this.errorCategory = errorCategory;
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

	@Column(name="VAT_TYPE")
	public String getVatType() {
		return vatType;
	}

	public void setVatType(String vatType) {
		this.vatType = vatType;
	}

	@Column(name="VAT_PERCENTAGE")
	public BigDecimal getVatPercentage() {
		return vatPercentage;
	}

	public void setVatPercentage(BigDecimal vatPercentage) {
		this.vatPercentage = vatPercentage;
	}

	@Column(name="VAT_AMOUNT")
	public BigDecimal getVatAmount() {
		return vatAmount;
	}

	public void setVatAmount(BigDecimal vatAmount) {
		this.vatAmount = vatAmount;
	}

	
	@Column(name="BENE_DEDUCT_FLAG")
	public String getBeneDeductFlag() {
		return beneDeductFlag;
	}

	public void setBeneDeductFlag(String beneDeductFlag) {
		this.beneDeductFlag = beneDeductFlag;
	}

	@Column(name="WU_PURPOSE_OF_TRANSACTION")
	public String getWuPurposeOfTransaction() {
		return wuPurposeOfTransaction;
	}
	public void setWuPurposeOfTransaction(String wuPurposeOfTransaction) {
		this.wuPurposeOfTransaction = wuPurposeOfTransaction;
	}

	@Column(name="SAVED_AMOUNT")
	public BigDecimal getSavedAmount() {
		return savedAmount;
	}

	public void setSavedAmount(BigDecimal savedAmount) {
		this.savedAmount = savedAmount;
	}

	@Column(name="RACK_EXCHANGE_RATE")
	public BigDecimal getRackExchangeRate() {
		return rackExchangeRate;
	}

	public void setRackExchangeRate(BigDecimal rackExchangeRate) {
		this.rackExchangeRate = rackExchangeRate;
	}
	
	@Column(name="CUSTOMER_CHOICE")
	public String getCustomerChoice() {
		return customerChoice;
	}

	public void setCustomerChoice(String customerChoice) {
		this.customerChoice = customerChoice;
	}
	
	
}
