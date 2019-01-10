package com.amx.jax.grid.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.grid.GridViewRecord;

/*
 * Author Rahamathali Shaik
*/
@Entity
@Table(name = "VW_CUSTOMER_KIBANA")
public class TranxViewRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APPL_ID")
	private BigDecimal id;

	@Column(name = "ES_TIMESTAMP")
	private Date updated;

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

	// Customer
	@Column(name = "IDENTITY_INT")
	private String identity;

	@Column(name = "CUSTOMER_ID")
	private BigDecimal custmerId;

	@Column(name = "CUSTOMER_EN_NAME")
	private String custmerName;

	// Delvry

	@Column(name = "DATE_OF_BIRTH")
	private Date dateOfBirth;

	@Column(name = "NATIONALITY")
	private BigDecimal nationality;

	@Column(name = "NATIONALITY_CODE")
	private String nationalityCode;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "LAST_UPDATED")
	private Date updateDate;

	@Column(name = "LATEST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "IS_MOBILE_WHATSAPP")
	private String isMobileWhatsApp;

	@Column(name = "IS_ONLINE_USER")
	private String isOnlineUser;

	@Column(name = "LAST_TRANSACTION_DATE")
	private Date lastTransactionDate;

}
