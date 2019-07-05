package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.constant.UserType;


@Entity
@Table(name="FS_CUSTOMER_LOGIN")
public class CustomerOnlineRegistration {
	
	private static final long serialVersionUID = 1L;
	private BigDecimal custLoginId;
	private BigDecimal languageId;
	private BigDecimal countryId;
	private BigDecimal companyId;
	private BigDecimal customerId;

	private String userName;
	private String password;
	private String secondaryPassword;
	
	private BigDecimal securityQuestion1;
	private BigDecimal securityQuestion2;
	private BigDecimal securityQuestion3;
	private BigDecimal securityQuestion4;
	private BigDecimal securityQuestion5;
	
	
	private String securityAnswer1;
	private String securityAnswer2;
	private String securityAnswer3;
	private String securityAnswer4;
	private String securityAnswer5;
	private String createdBy;
	private String updatedBy;
	private Date creationDate;
	private Date lastUpdated;
	private String userType;
	private String email;
	private String caption;
	private String imageUrl;
	private String loginType;
	private Date lockDt;
	private BigDecimal lockCnt;
	private String unlockBy;
	private String unLockIp;
	private Date unlockDt;
	private String resetBy;
	private String hresetBy;
	private String hresetIp;
	private Date hresetkDt;
	private String resetIp;
	private Date resetDt;
	private String status;
	
	/**Added  by Rabil*/
	private String emailToken;
	private String smsToken;
	private String whatsAppToken;
	private BigDecimal tokenSentCount;
	private String mobileNumber;
	private Date tokenDate;
	private String loginId;
	
	private String deviceId;
	private String deviceType;
	private String devicePassword;
	private String fingerprintDeviceId;
	

	
	
	
	public String getFingerprintDeviceId() {
		return fingerprintDeviceId;
	}

	public void setFingerprintDeviceId(String fingerprintDeviceId) {
		this.fingerprintDeviceId = fingerprintDeviceId;
	}
	public String getDevicePassword() {
		return devicePassword;
	}

	public void setDevicePassword(String devicePassword) {
		this.devicePassword = devicePassword;
	}

	/** added by Prashant */
	public CustomerOnlineRegistration(Customer cust) {
		this.customerId = cust.getCustomerId();
		this.countryId = cust.getCountryId();
		this.countryId = cust.getCountryId();
		this.companyId = cust.getCompanyId();
		this.languageId = cust.getLanguageId();
		this.userName = cust.getIdentityInt();
		this.createdBy = cust.getIdentityInt();
		this.setCreationDate(new Date());
		this.userType = UserType.USER.getType();
		this.loginType = UserType.USER.getType();
		this.email = cust.getEmail();
		this.mobileNumber = cust.getMobile();
		this.setStatus("N");
		this.setResetBy(cust.getIdentityInt());
		this.setResetDt(new Date());

	}

	public CustomerOnlineRegistration() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(generator="fs_customer_login_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="fs_customer_login_seq" ,sequenceName="FS_CUSTOMER_LOGIN_SEQ",allocationSize=1)		
	@Column(name = "CUST_LOGIN_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getCustLoginId() {
		return this.custLoginId;
	}

	public void setCustLoginId(BigDecimal custLoginId) {
		this.custLoginId = custLoginId;
	}

	
	
	
	
	@Column(name = "USER_NAME", length = 20)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "PASSWORD", nullable = false, length = 100)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "SECONDARY_PASSWORD", length = 100)
	public String getSecondaryPassword() {
		return this.secondaryPassword;
	}

	public void setSecondaryPassword(String secondaryPassword) {
		this.secondaryPassword = secondaryPassword;
	}

	
	@Column(name = "SECURITY_ANSWER1", length = 200)
	public String getSecurityAnswer1() {
		return this.securityAnswer1;
	}

	public void setSecurityAnswer1(String securityAnswer1) {
		this.securityAnswer1 = securityAnswer1;
	}

	@Column(name = "SECURITY_ANSWER2", length = 200)
	public String getSecurityAnswer2() {
		return this.securityAnswer2;
	}

	public void setSecurityAnswer2(String securityAnswer2) {
		this.securityAnswer2 = securityAnswer2;
	}

	@Column(name = "SECURITY_ANSWER3", length = 200)
	public String getSecurityAnswer3() {
		return this.securityAnswer3;
	}

	public void setSecurityAnswer3(String securityAnswer3) {
		this.securityAnswer3 = securityAnswer3;
	}

	@Column(name = "SECURITY_ANSWER4", length = 200)
	public String getSecurityAnswer4() {
		return this.securityAnswer4;
	}

	public void setSecurityAnswer4(String securityAnswer4) {
		this.securityAnswer4 = securityAnswer4;
	}

	@Column(name = "SECURITY_ANSWER5", length = 200)
	public String getSecurityAnswer5() {
		return this.securityAnswer5;
	}

	public void setSecurityAnswer5(String securityAnswer5) {
		this.securityAnswer5 = securityAnswer5;
	}

	@Column(name = "CREATED_BY", length = 30)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "UPDATED_BY", length = 30)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "CREATION_DATE")
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "LAST_UPDATED")
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Column(name = "USER_TYPE", length = 30)
	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Column(name = "EMAIL", length = 30)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "CAPTION", length = 30)
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@Column(name = "IMAGE_PATH", length = 30)
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Column(name = "LOGIN_TYP")
	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	@Column(name = "LOCK_DT")
	public Date getLockDt() {
		return lockDt;
	}

	public void setLockDt(Date lockDt) {
		this.lockDt = lockDt;
	}

	@Column(name = "LOCK_CNT")
	public BigDecimal getLockCnt() {
		return lockCnt;
	}

	public void setLockCnt(BigDecimal lockCnt) {
		this.lockCnt = lockCnt;
	}

	@Column(name = "UNLOCK_BY")
	public String getUnlockBy() {
		return unlockBy;
	}

	public void setUnlockBy(String unlockBy) {
		this.unlockBy = unlockBy;
	}

	@Column(name = "UNLOCK_IP")
	public String getUnLockIp() {
		return unLockIp;
	}

	public void setUnLockIp(String unLockIp) {
		this.unLockIp = unLockIp;
	}

	@Column(name = "UNLOCK_DT")
	public Date getUnlockDt() {
		return unlockDt;
	}

	public void setUnlockDt(Date unlockDt) {
		this.unlockDt = unlockDt;
	}

	@Column(name = "RESET_BY")
	public String getResetBy() {
		return resetBy;
	}

	public void setResetBy(String resetBy) {
		this.resetBy = resetBy;
	}

	@Column(name = "HRESET_BY")
	public String getHresetBy() {
		return hresetBy;
	}

	public void setHresetBy(String hresetBy) {
		this.hresetBy = hresetBy;
	}

	@Column(name = "HRESET_IP")
	public String getHresetIp() {
		return hresetIp;
	}

	public void setHresetIp(String hresetIp) {
		this.hresetIp = hresetIp;
	}

	@Column(name = "HRESET_DT")
	public Date getHresetkDt() {
		return hresetkDt;
	}

	public void setHresetkDt(Date hresetkDt) {
		this.hresetkDt = hresetkDt;
	}

	@Column(name = "RESET_IP")
	public String getResetIp() {
		return resetIp;
	}

	public void setResetIp(String resetIp) {
		this.resetIp = resetIp;
	}

	@Column(name = "RESET_DT")
	public Date getResetDt() {
		return resetDt;
	}

	public void setResetDt(Date resetDt) {
		this.resetDt = resetDt;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name="EMAIL_TOKEN")
	public String getEmailToken() {
		return emailToken;
	}

	public void setEmailToken(String emailToken) {
		this.emailToken = emailToken;
	}

	@Column(name="SMS_TOKEN")
	public String getSmsToken() {
		return smsToken;
	}

	public void setSmsToken(String smsToken) {
		this.smsToken = smsToken;
	}

	@Column(name="MOBILE")
	public String getMobileNumber() {
		return mobileNumber;
	}

	
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Column(name="TOKEN_DATE")
	public Date getTokenDate() {
		return tokenDate;
	}

	public void setTokenDate(Date tokenDate) {
		this.tokenDate = tokenDate;
	}

	@Column(name="CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Column(name="LANGUAGE_ID")
	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	@Column(name="COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	@Column(name="COMPANY_ID")
	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	@Column(name="SECURITY_QUESTION1")
	public BigDecimal getSecurityQuestion1() {
		return securityQuestion1;
	}

	public void setSecurityQuestion1(BigDecimal securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}

	@Column(name="SECURITY_QUESTION2")
	public BigDecimal getSecurityQuestion2() {
		return securityQuestion2;
	}

	public void setSecurityQuestion2(BigDecimal securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}
	
	@Column(name="SECURITY_QUESTION3")
	public BigDecimal getSecurityQuestion3() {
		return securityQuestion3;
	}

	public void setSecurityQuestion3(BigDecimal securityQuestion3) {
		this.securityQuestion3 = securityQuestion3;
	}

	@Column(name="SECURITY_QUESTION4")
	public BigDecimal getSecurityQuestion4() {
		return securityQuestion4;
	}

	public void setSecurityQuestion4(BigDecimal securityQuestion4) {
		this.securityQuestion4 = securityQuestion4;
	}
	
	@Column(name="SECURITY_QUESTION5")
	public BigDecimal getSecurityQuestion5() {
		return securityQuestion5;
	}

	public void setSecurityQuestion5(BigDecimal securityQuestion5) {
		this.securityQuestion5 = securityQuestion5;
	}

	@Column(name="LOGIN_ID")
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Column(name="DEVICE_ID")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name="DEVICE_TYPE")
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	@PrePersist
	public void prepersist() {
		this.lastUpdated = new Date();
	}

	@Column(name="TOKEN_SENT_CNT")
	public BigDecimal getTokenSentCount() {
		return tokenSentCount;
	}

	public void setTokenSentCount(BigDecimal tokenSentCount) {
		this.tokenSentCount = tokenSentCount;
	}

	@Column(name="WHATSAPP_TOKEN")
	public String getWhatsAppToken() {
		return whatsAppToken;
	}

	public void setWhatsAppToken(String whatsAppToken) {
		this.whatsAppToken = whatsAppToken;
	}
		
}
