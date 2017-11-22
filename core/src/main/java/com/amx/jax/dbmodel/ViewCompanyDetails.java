package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/* *****************************************************************************************************************

File		: Common company Master

Project	: AlmullaExchange Online

Package	: com.amg.exchange.online.common

Created	:		Rabil
				Date	: 22-May-2016
				By		:  
				Revision:
/***********************************************************/




@Entity
@Table(name = "VIEW_EX_COMPANY")
public class ViewCompanyDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private BigDecimal companyId;
	private BigDecimal languageId;
	private BigDecimal companyCode;
	private String isActive;
	private String companyName;
	private Blob companyLogoImg1;
	private BigDecimal applicationCountryId;
	private String telephoneNo;
	private String email;
	private String onlineHeaderEnglishText;
	private String onlineHeaderArabicText;
	private String onlineInnerHeaderEnglishText;
	private String onlineInnerHeaderArabicText;
	private BigDecimal countryId;
	
	
	private String faxNo;
	private String estYear;
	private BigDecimal currencyId;
	private String capitalAmount;
	//private BigDecimal grpcod;
	//private BigDecimal finbgn;
	//private BigDecimal finend;
	//private String telexNo;
	
	
	
	
	@Id
	@Column(name = "COMPANY_ID", length = 2)
	public BigDecimal getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	
	@Id
	@Column(name="LANGUAGE_ID")
	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	@Column(name = "COMPANY_CODE", length = 2)
	public BigDecimal getCompanyCode() {
		return this.companyCode;
	}

	public void setCompanyCode(BigDecimal companyCode) {
		this.companyCode = companyCode;
	}

	
	@Column(name = "ISACTIVE", length = 1)
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	

	@Column(name = "TELEPHONE_NO")
	public String getTelephoneNo() {
		return telephoneNo;
	}
	public void setTelephoneNo(String telephoneNo) {
		this.telephoneNo=telephoneNo;
	}
	
	@Column(name = "FAX_NO")
	public String getFaxNo() {
		return faxNo;
	}
	
	public void setFaxNo(String faxNo) {
	this.faxNo=faxNo;
	}
	
	@Column(name = "EXCHANGE_FROM_EMAIL_ID")
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email=email;
	}
	
	@Column(name = "EST_YEAR")
	public String getEstYear() {
		return estYear;
	}
	
	public void setEstYear(String estYear) {
		this.estYear=estYear;
	}
	
	
	@Column(name = "CURRENCY_ID")
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	

	
	@Column(name = "CAPITAL_AMOUNT")
	public String getCapitalAmount() {
		return capitalAmount;
	}
	
	public void setCapitalAmount(String capitalAmount) {
		this.capitalAmount=capitalAmount;
	}
	
	
	
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}
	
	@Column(name="COMPANY_NAME")
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@Column(name = "LOGO_IMG1", nullable = true,  length = 10485760 )
	public Blob getCompanyLogoImg1() {
		return companyLogoImg1;
	}
	public void setCompanyLogoImg1(Blob companyLogoImg1) {
		this.companyLogoImg1 = companyLogoImg1;
	}
	@Column(name="APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
    

	@Column(name="ONLINE_HEADER_EN_TEXT")
	public String getOnlineHeaderEnglishText() {
		return onlineHeaderEnglishText;
	}

	public void setOnlineHeaderEnglishText(String onlineHeaderEnglishText) {
		this.onlineHeaderEnglishText = onlineHeaderEnglishText;
	}
	@Column(name="ONLINE_HEADER_AR_TEXT")
	public String getOnlineHeaderArabicText() {
		return onlineHeaderArabicText;
	}

	public void setOnlineHeaderArabicText(String onlineHeaderArabicText) {
		this.onlineHeaderArabicText = onlineHeaderArabicText;
	}
	
	@Column(name="ONLINE_INNER_HEADER_EN_TEXT")
	public String getOnlineInnerHeaderEnglishText() {
		return onlineInnerHeaderEnglishText;
	}

	public void setOnlineInnerHeaderEnglishText(String onlineInnerHeaderEnglishText) {
		this.onlineInnerHeaderEnglishText = onlineInnerHeaderEnglishText;
	}

	@Column(name="ONLINE_INNER_HEADER_AR_TEXT")
	public String getOnlineInnerHeaderArabicText() {
		return onlineInnerHeaderArabicText;
	}

	public void setOnlineInnerHeaderArabicText(String onlineInnerHeaderArabicText) {
		this.onlineInnerHeaderArabicText = onlineInnerHeaderArabicText;
	}
	
	


}
