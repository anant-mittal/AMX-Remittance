/*
 * package com.amx.jax.complaince;
 * 
 * import java.io.Serializable; import java.math.BigDecimal; import
 * java.util.Date; import javax.persistence.Column; import
 * javax.persistence.Entity; import javax.persistence.Table;
 * 
 * @Entity
 * 
 * @Table(name = "JAX_VW_CBK_REPORT") public class jaxCbkReport implements
 * Serializable {
 * 
 * private static final long serialVersionUID = 1L;
 * 
 * private String rentityId; private String submissionCode; private String
 * reportCode; private BigDecimal docNo; private BigDecimal docFyr; private Date
 * documentDate; private String currencyCodeLocal; private String tranxRef;
 * private BigDecimal tranxNo; private String tranxDate; private String
 * trnxLocation; private String tranxMode; private Date accountMMYYY; private
 * BigDecimal amountLocal; private String fundsCode; private String
 * foreignCurrencyCode; private BigDecimal foreignAmount; private BigDecimal
 * foreignExRate; private String custGender; private String custTitle; private
 * String custFirstName; private String custLastName; private String custSsn;
 * private String custNationality; private String custPhcontactType; private
 * String custPhcommunicationType; private String custPhcountryPrefix; private
 * String custPhNumber; private String custAddressType; private String
 * custaddress; private String custcity; private String custcountrycode; private
 * String custIdentificationType; private String custIdentityNumber; private
 * String custIdIssueCountry; private String fromcountry; private String
 * beneFirstName; private String beneLastName; private String beneNationality;
 * private String benePhoneContactType; private String
 * benePhoneCommunicationType; private String benePhoneNo; private String
 * beneAddressType; private String beneAddress; private String beneCity; private
 * String beneCountrycode; private String toCountry;
 * 
 * @Column(name = "RENTITY_ID") public String getRentityId() { return rentityId;
 * } public void setRentityId(String rentityId) { this.rentityId = rentityId; }
 * 
 * @Column(name = "SUBMISSION_CODE") public String getSubmissionCode() { return
 * submissionCode; } public void setSubmissionCode(String submissionCode) {
 * this.submissionCode = submissionCode; }
 * 
 * @Column(name = "REPORT_CODE") public String getReportCode() { return
 * reportCode; } public void setReportCode(String reportCode) { this.reportCode
 * = reportCode; }
 * 
 * @Column(name = "DOCNO") public BigDecimal getDocNo() { return docNo; } public
 * void setDocNo(BigDecimal docNo) { this.docNo = docNo; }
 * 
 * @Column(name = "DOCFYR") public BigDecimal getDocFyr() { return docFyr; }
 * public void setDocFyr(BigDecimal docFyr) { this.docFyr = docFyr; }
 * 
 * @Column(name = "DOCUMENT_DATE") public Date getDocumentDate() { return
 * documentDate; } public void setDocumentDate(Date documentDate) {
 * this.documentDate = documentDate; }
 * 
 * @Column(name = "CURRENCY_CODE_LOCAL") public String getCurrencyCodeLocal() {
 * return currencyCodeLocal; } public void setCurrencyCodeLocal(String
 * currencyCodeLocal) { this.currencyCodeLocal = currencyCodeLocal; }
 * 
 * @Column(name = "TRANSACTION_REF") public String getTranxRef() { return
 * tranxRef; } public void setTranxRef(String tranxRef) { this.tranxRef =
 * tranxRef; }
 * 
 * @Column(name = "DATE_TRANSACTION") public String getTranxDate() { return
 * tranxDate; } public void setTranxDate(String tranxDate) { this.tranxDate =
 * tranxDate; }
 * 
 * @Column(name = "TRANSACTION_LOCATION") public String getTrnxLocation() {
 * return trnxLocation; } public void setTrnxLocation(String trnxLocation) {
 * this.trnxLocation = trnxLocation; }
 * 
 * @Column(name = "TRANSMODE_CODE") public String getTranxMode() { return
 * tranxMode; } public void setTranxMode(String tranxMode) { this.tranxMode =
 * tranxMode; }
 * 
 * @Column(name = "ACCOUNT_MMYYYY") public Date getAccountMMYYY() { return
 * accountMMYYY; } public void setAccountMMYYY(Date accountMMYYY) {
 * this.accountMMYYY = accountMMYYY; }
 * 
 * @Column(name = "AMOUNT_LOCAL") public BigDecimal getAmountLocal() { return
 * amountLocal; } public void setAmountLocal(BigDecimal amountLocal) {
 * this.amountLocal = amountLocal; }
 * 
 * @Column(name = "FUNDS_CODE") public String getFundsCode() { return fundsCode;
 * } public void setFundsCode(String fundsCode) { this.fundsCode = fundsCode; }
 * 
 * @Column(name = "FOREIGN_CURRENCY_CODE") public String
 * getForeignCurrencyCode() { return foreignCurrencyCode; } public void
 * setForeignCurrencyCode(String foreignCurrencyCode) { this.foreignCurrencyCode
 * = foreignCurrencyCode; }
 * 
 * @Column(name = "FOREIGN_AMOUNT") public BigDecimal getForeignAmount() {
 * return foreignAmount; } public void setForeignAmount(BigDecimal
 * foreignAmount) { this.foreignAmount = foreignAmount; }
 * 
 * @Column(name = "FOREIGN_EXCHANGE_RATE") public BigDecimal getForeignExRate()
 * { return foreignExRate; } public void setForeignExRate(BigDecimal
 * foreignExRate) { this.foreignExRate = foreignExRate; }
 * 
 * @Column(name = "CUST_GENDER") public String getCustGender() { return
 * custGender; } public void setCustGender(String custGender) { this.custGender
 * = custGender; }
 * 
 * @Column(name = "CUST_TITLE") public String getCustTitle() { return custTitle;
 * } public void setCustTitle(String custTitle) { this.custTitle = custTitle; }
 * 
 * @Column(name = "CUST_FIRST_NAME") public String getCustFirstName() { return
 * custFirstName; } public void setCustFirstName(String custFirstName) {
 * this.custFirstName = custFirstName; }
 * 
 * @Column(name = "CUST_LAST_NAME") public String getCustLastName() { return
 * custLastName; } public void setCustLastName(String custLastName) {
 * this.custLastName = custLastName; }
 * 
 * @Column(name = "CUST_SSN") public String getCustSsn() { return custSsn; }
 * public void setCustSsn(String custSsn) { this.custSsn = custSsn; }
 * 
 * @Column(name = "CUST_NATIONALITY") public String getCustNationality() {
 * return custNationality; } public void setCustNationality(String
 * custNationality) { this.custNationality = custNationality; }
 * 
 * @Column(name = "CUST_PH_CONTACT_TYPE") public String getCustPhcontactType() {
 * return custPhcontactType; } public void setCustPhcontactType(String
 * custPhcontactType) { this.custPhcontactType = custPhcontactType; }
 * 
 * @Column(name = "CUST_PH_COMMUNICATION_TYPE") public String
 * getCustPhcommunicationType() { return custPhcommunicationType; } public void
 * setCustPhcommunicationType(String custPhcommunicationType) {
 * this.custPhcommunicationType = custPhcommunicationType; }
 * 
 * @Column(name = "CUST_PH_COUNTRY_PREFIX") public String
 * getCustPhcountryPrefix() { return custPhcountryPrefix; } public void
 * setCustPhcountryPrefix(String custPhcountryPrefix) { this.custPhcountryPrefix
 * = custPhcountryPrefix; }
 * 
 * @Column(name = "CUST_PH_NUMBER") public String getCustPhNumber() { return
 * custPhNumber; } public void setCustPhNumber(String custPhNumber) {
 * this.custPhNumber = custPhNumber; }
 * 
 * @Column(name = "CUST_ADDRESS_TYPE") public String getCustAddressType() {
 * return custAddressType; } public void setCustAddressType(String
 * custAddressType) { this.custAddressType = custAddressType; }
 * 
 * @Column(name = "CUST_ADDRESS") public String getCustaddress() { return
 * custaddress; } public void setCustaddress(String custaddress) {
 * this.custaddress = custaddress; }
 * 
 * @Column(name = "CUST_CITY") public String getCustcity() { return custcity; }
 * public void setCustcity(String custcity) { this.custcity = custcity; }
 * 
 * @Column(name = "CUST_COUNTRY_CODE") public String getCustcountrycode() {
 * return custcountrycode; } public void setCustcountrycode(String
 * custcountrycode) { this.custcountrycode = custcountrycode; }
 * 
 * @Column(name = "CUST_IDENTIFICATION_TYPE") public String
 * getCustIdentificationType() { return custIdentificationType; } public void
 * setCustIdentificationType(String custIdentificationType) {
 * this.custIdentificationType = custIdentificationType; }
 * 
 * @Column(name = "CUST_IDENTITY_NUMBER") public String getCustIdentityNumber()
 * { return custIdentityNumber; } public void setCustIdentityNumber(String
 * custIdentityNumber) { this.custIdentityNumber = custIdentityNumber; }
 * 
 * @Column(name = "CUST_ID_ISSUE_COUNTRY") public String getCustIdIssueCountry()
 * { return custIdIssueCountry; } public void setCustIdIssueCountry(String
 * custIdIssueCountry) { this.custIdIssueCountry = custIdIssueCountry; }
 * 
 * @Column(name = "FROM_COUNTRY") public String getFromcountry() { return
 * fromcountry; } public void setFromcountry(String fromcountry) {
 * this.fromcountry = fromcountry; }
 * 
 * @Column(name = "BENE_FIRST_NAME") public String getBeneFirstName() { return
 * beneFirstName; } public void setBeneFirstName(String beneFirstName) {
 * this.beneFirstName = beneFirstName; }
 * 
 * @Column(name = "BENE_LAST_NAME") public String getBeneLastName() { return
 * beneLastName; } public void setBeneLastName(String beneLastName) {
 * this.beneLastName = beneLastName; }
 * 
 * @Column(name = "BENE_NATIONALITY") public String getBeneNationality() {
 * return beneNationality; } public void setBeneNationality(String
 * beneNationality) { this.beneNationality = beneNationality; }
 * 
 * @Column(name = "BENE_PHONE_CONTACT_TYPE") public String
 * getBenePhoneContactType() { return benePhoneContactType; } public void
 * setBenePhoneContactType(String benePhoneContactType) {
 * this.benePhoneContactType = benePhoneContactType; }
 * 
 * @Column(name = "BENE_PHONE_COMMUNICATION_TYPE") public String
 * getBenePhoneCommunicationType() { return benePhoneCommunicationType; } public
 * void setBenePhoneCommunicationType(String benePhoneCommunicationType) {
 * this.benePhoneCommunicationType = benePhoneCommunicationType; }
 * 
 * @Column(name = "BENE_PHONE_NUMBER") public String getBenePhoneNo() { return
 * benePhoneNo; } public void setBenePhoneNo(String benePhoneNo) {
 * this.benePhoneNo = benePhoneNo; }
 * 
 * @Column(name = "BENE_ADDRESS_TYPE") public String getBeneAddressType() {
 * return beneAddressType; } public void setBeneAddressType(String
 * beneAddressType) { this.beneAddressType = beneAddressType; }
 * 
 * @Column(name = "BENE_ADDRESS") public String getBeneAddress() { return
 * beneAddress; } public void setBeneAddress(String beneAddress) {
 * this.beneAddress = beneAddress; }
 * 
 * @Column(name = "BENE_CITY") public String getBeneCity() { return beneCity; }
 * public void setBeneCity(String beneCity) { this.beneCity = beneCity; }
 * 
 * @Column(name = "BENE_COUNTRY_CODE") public String getBeneCountrycode() {
 * return beneCountrycode; } public void setBeneCountrycode(String
 * beneCountrycode) { this.beneCountrycode = beneCountrycode; }
 * 
 * @Column(name = "TO_COUNTRY") public String getToCountry() { return toCountry;
 * } public void setToCountry(String toCountry) { this.toCountry = toCountry; }
 * 
 * @Column(name = "TRANSACTION_NUMBER") public BigDecimal getTranxNo() { return
 * tranxNo; } public void setTranxNo(BigDecimal tranxNo) { this.tranxNo =
 * tranxNo; }
 * 
 * 
 * 
 * }
 */