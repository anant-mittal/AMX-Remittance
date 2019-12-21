/*
 * package com.amx.jax.complaince;
 * 
 * import java.io.Serializable; import java.math.BigDecimal;
 * 
 * import javax.persistence.Column; import javax.persistence.Entity; import
 * javax.persistence.Table;
 * 
 * @Entity
 * 
 * @Table(name = "V_CBK_REPORT_AK") public class CBK_Reportt implements
 * Serializable {
 * 
 *//**
	* 
	*//*
		 * private static final long serialVersionUID = 1L;
		 * 
		 * private String rentityId ; private String submissionCode; private String
		 * reportCode; private BigDecimal docNo; private BigDecimal docFyr; private
		 * String currencyCodeLocal; private String tranxRef; private BigDecimal
		 * tranxNo; private String tranxDate; private String trnxLocation; private
		 * String tranxMode; private BigDecimal amountLocal; private String fundsCode;
		 * private String foreignCurrencyCode; private BigDecimal foreignAmount; private
		 * BigDecimal foreignExRate; private String custGender; private String
		 * custTitle; private String custFirstName; private String custLastName; private
		 * String custSsn; private String custNationality; private String
		 * custPhcontactType; private String custPhcommunicationType; private String
		 * custPhcountryPrefix; private String custPhNumber; private String
		 * custAddressType; private String custaddress; private String custcity; private
		 * String custcountrycode; private String custIdentificationType; private String
		 * custIdentityNumber; private String custIdIssueCountry; private String
		 * fromcountry; private String beneFirstName; private String beneLastName;
		 * private String beneNationality; private String benePhoneContactType; private
		 * String benePhoneCommunicationType; private String benePhoneNo; private String
		 * beneAddressType; private String beneAddress; private String beneCity; private
		 * String beneCountrycode; private String toCountry;
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getRentityId() { return
		 * rentityId; } public void setRentityId(String rentityId) { this.rentityId =
		 * rentityId; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getSubmissionCode() { return
		 * submissionCode; } public void setSubmissionCode(String submissionCode) {
		 * this.submissionCode = submissionCode; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getReportCode() { return
		 * reportCode; } public void setReportCode(String reportCode) { this.reportCode
		 * = reportCode; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public BigDecimal getDocNo() { return docNo;
		 * } public void setDocNo(BigDecimal docNo) { this.docNo = docNo; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public BigDecimal getDocFyr() { return
		 * docFyr; } public void setDocFyr(BigDecimal docFyr) { this.docFyr = docFyr; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCurrencyCodeLocal() {
		 * return currencyCodeLocal; } public void setCurrencyCodeLocal(String
		 * currencyCodeLocal) { this.currencyCodeLocal = currencyCodeLocal; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getTranxRef() { return
		 * tranxRef; } public void setTranxRef(String tranxRef) { this.tranxRef =
		 * tranxRef; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public BigDecimal getTranxNo() { return
		 * tranxNo; } public void setTranxNo(BigDecimal tranxNo) { this.tranxNo =
		 * tranxNo; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getTranxDate() { return
		 * tranxDate; } public void setTranxDate(String tranxDate) { this.tranxDate =
		 * tranxDate; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getTrnxLocation() { return
		 * trnxLocation; } public void setTrnxLocation(String trnxLocation) {
		 * this.trnxLocation = trnxLocation; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getTranxMode() { return
		 * tranxMode; } public void setTranxMode(String tranxMode) { this.tranxMode =
		 * tranxMode; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public BigDecimal getAmountLocal() { return
		 * amountLocal; } public void setAmountLocal(BigDecimal amountLocal) {
		 * this.amountLocal = amountLocal; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getFundsCode() { return
		 * fundsCode; } public void setFundsCode(String fundsCode) { this.fundsCode =
		 * fundsCode; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getForeignCurrencyCode() {
		 * return foreignCurrencyCode; } public void setForeignCurrencyCode(String
		 * foreignCurrencyCode) { this.foreignCurrencyCode = foreignCurrencyCode; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public BigDecimal getForeignAmount() {
		 * return foreignAmount; } public void setForeignAmount(BigDecimal
		 * foreignAmount) { this.foreignAmount = foreignAmount; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public BigDecimal getForeignExRate() {
		 * return foreignExRate; } public void setForeignExRate(BigDecimal
		 * foreignExRate) { this.foreignExRate = foreignExRate; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustGender() { return
		 * custGender; } public void setCustGender(String custGender) { this.custGender
		 * = custGender; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustTitle() { return
		 * custTitle; } public void setCustTitle(String custTitle) { this.custTitle =
		 * custTitle; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustFirstName() { return
		 * custFirstName; } public void setCustFirstName(String custFirstName) {
		 * this.custFirstName = custFirstName; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustLastName() { return
		 * custLastName; } public void setCustLastName(String custLastName) {
		 * this.custLastName = custLastName; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustSsn() { return custSsn;
		 * } public void setCustSsn(String custSsn) { this.custSsn = custSsn; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustNationality() { return
		 * custNationality; } public void setCustNationality(String custNationality) {
		 * this.custNationality = custNationality; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustPhcontactType() {
		 * return custPhcontactType; } public void setCustPhcontactType(String
		 * custPhcontactType) { this.custPhcontactType = custPhcontactType; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustPhcommunicationType() {
		 * return custPhcommunicationType; } public void
		 * setCustPhcommunicationType(String custPhcommunicationType) {
		 * this.custPhcommunicationType = custPhcommunicationType; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustPhcountryPrefix() {
		 * return custPhcountryPrefix; } public void setCustPhcountryPrefix(String
		 * custPhcountryPrefix) { this.custPhcountryPrefix = custPhcountryPrefix; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustPhNumber() { return
		 * custPhNumber; } public void setCustPhNumber(String custPhNumber) {
		 * this.custPhNumber = custPhNumber; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustAddressType() { return
		 * custAddressType; } public void setCustAddressType(String custAddressType) {
		 * this.custAddressType = custAddressType; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustaddress() { return
		 * custaddress; } public void setCustaddress(String custaddress) {
		 * this.custaddress = custaddress; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustcity() { return
		 * custcity; } public void setCustcity(String custcity) { this.custcity =
		 * custcity; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustcountrycode() { return
		 * custcountrycode; } public void setCustcountrycode(String custcountrycode) {
		 * this.custcountrycode = custcountrycode; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustIdentificationType() {
		 * return custIdentificationType; } public void setCustIdentificationType(String
		 * custIdentificationType) { this.custIdentificationType =
		 * custIdentificationType; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustIdentityNumber() {
		 * return custIdentityNumber; } public void setCustIdentityNumber(String
		 * custIdentityNumber) { this.custIdentityNumber = custIdentityNumber; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getCustIdIssueCountry() {
		 * return custIdIssueCountry; } public void setCustIdIssueCountry(String
		 * custIdIssueCountry) { this.custIdIssueCountry = custIdIssueCountry; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getFromcountry() { return
		 * fromcountry; } public void setFromcountry(String fromcountry) {
		 * this.fromcountry = fromcountry; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getBeneFirstName() { return
		 * beneFirstName; } public void setBeneFirstName(String beneFirstName) {
		 * this.beneFirstName = beneFirstName; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getBeneLastName() { return
		 * beneLastName; } public void setBeneLastName(String beneLastName) {
		 * this.beneLastName = beneLastName; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getBeneNationality() { return
		 * beneNationality; } public void setBeneNationality(String beneNationality) {
		 * this.beneNationality = beneNationality; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getBenePhoneContactType() {
		 * return benePhoneContactType; } public void setBenePhoneContactType(String
		 * benePhoneContactType) { this.benePhoneContactType = benePhoneContactType; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String
		 * getBenePhoneCommunicationType() { return benePhoneCommunicationType; } public
		 * void setBenePhoneCommunicationType(String benePhoneCommunicationType) {
		 * this.benePhoneCommunicationType = benePhoneCommunicationType; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getBenePhoneNo() { return
		 * benePhoneNo; } public void setBenePhoneNo(String benePhoneNo) {
		 * this.benePhoneNo = benePhoneNo; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getBeneAddressType() { return
		 * beneAddressType; } public void setBeneAddressType(String beneAddressType) {
		 * this.beneAddressType = beneAddressType; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getBeneAddress() { return
		 * beneAddress; } public void setBeneAddress(String beneAddress) {
		 * this.beneAddress = beneAddress; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getBeneCity() { return
		 * beneCity; }
		 * 
		 * public void setBeneCity(String beneCity) { this.beneCity = beneCity; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getBeneCountrycode() { return
		 * beneCountrycode; } public void setBeneCountrycode(String beneCountrycode) {
		 * this.beneCountrycode = beneCountrycode; }
		 * 
		 * @Column(name = "TRANSMODE_CODE") public String getToCountry() { return
		 * toCountry; } public void setToCountry(String toCountry) { this.toCountry =
		 * toCountry; }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * }
		 */