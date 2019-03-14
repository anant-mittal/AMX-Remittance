package com.amx.jax.dbmodel.remittance;

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
@Table(name = "LOYALTYPOINTS")
public class LoyaltyPointsModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	@Id
	@GeneratedValue(generator="lty_points_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="lty_points_seq",sequenceName="LTY_POINTS_SEQ",allocationSize=1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal  id;
	
	@Column(name="PROMOID")
	private BigDecimal  promoId;
	
	@Column(name="CUSTCODE")
	private BigDecimal  customerReference;
	
	@Column(name="LOYALTYPOINT")
	private BigDecimal  loyaltyPoints;
	
	@Column(name="TRANSDATE")
	private Date transDate;
	
	@Column(name="PROCESSDATE")
	private Date processDate;
	
	
	@Column(name="DOCFYR")
	private BigDecimal  docfyr;
	
	
	@Column(name="TRNREF")
	private BigDecimal  trnRefNo;
	
	@Column(name="TEMPLATEDESC")
	private String templateDesc;
	
	@Column(name="TYPE")
	private String type;
	
	@Column(name="CALCFLAG")
	private String calcFlag;
	
	
	@Column(name="CALCSUM")
	private String calcSum;
	
	
	@Column(name="ACYYMM")
	private Date accMonth;
	
	
	@Column(name="COMCOD")
	private BigDecimal  compCode;
	
	
	@Column(name="DOCCOD")
	private BigDecimal  docCode;
	
	
	@Column(name="ASSOCOD")
	private BigDecimal  assoCode;
	
	
	@Column(name="SLAB_AMT")
	private BigDecimal  slabAmt;
	
	
	@Column(name="AVG_RATE")
	private BigDecimal  avgRate;
	
	@Column(name="UPD_CUSMAS")
	private String updCusmas;
	
	
	@Column(name="EXPIRY_DT")
	private Date expiryDate;


	public BigDecimal getId() {
		return id;
	}


	public void setId(BigDecimal id) {
		this.id = id;
	}


	public BigDecimal getPromoId() {
		return promoId;
	}


	public void setPromoId(BigDecimal promoId) {
		this.promoId = promoId;
	}


	public BigDecimal getCustomerReference() {
		return customerReference;
	}


	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}


	public BigDecimal getLoyaltyPoints() {
		return loyaltyPoints;
	}


	public void setLoyaltyPoints(BigDecimal loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}


	public Date getTransDate() {
		return transDate;
	}


	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}


	public Date getProcessDate() {
		return processDate;
	}


	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}


	public BigDecimal getDocfyr() {
		return docfyr;
	}


	public void setDocfyr(BigDecimal docfyr) {
		this.docfyr = docfyr;
	}


	public BigDecimal getTrnRefNo() {
		return trnRefNo;
	}


	public void setTrnRefNo(BigDecimal trnRefNo) {
		this.trnRefNo = trnRefNo;
	}


	public String getTemplateDesc() {
		return templateDesc;
	}


	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getCalcFlag() {
		return calcFlag;
	}


	public void setCalcFlag(String calcFlag) {
		this.calcFlag = calcFlag;
	}


	public String getCalcSum() {
		return calcSum;
	}


	public void setCalcSum(String calcSum) {
		this.calcSum = calcSum;
	}


	public Date getAccMonth() {
		return accMonth;
	}


	public void setAccMonth(Date accMonth) {
		this.accMonth = accMonth;
	}


	public BigDecimal getCompCode() {
		return compCode;
	}


	public void setCompCode(BigDecimal compCode) {
		this.compCode = compCode;
	}


	public BigDecimal getDocCode() {
		return docCode;
	}


	public void setDocCode(BigDecimal docCode) {
		this.docCode = docCode;
	}


	public BigDecimal getAssoCode() {
		return assoCode;
	}


	public void setAssoCode(BigDecimal assoCode) {
		this.assoCode = assoCode;
	}


	public BigDecimal getSlabAmt() {
		return slabAmt;
	}


	public void setSlabAmt(BigDecimal slabAmt) {
		this.slabAmt = slabAmt;
	}


	public BigDecimal getAvgRate() {
		return avgRate;
	}


	public void setAvgRate(BigDecimal avgRate) {
		this.avgRate = avgRate;
	}


	public String getUpdCusmas() {
		return updCusmas;
	}


	public void setUpdCusmas(String updCusmas) {
		this.updCusmas = updCusmas;
	}


	public Date getExpiryDate() {
		return expiryDate;
	}


	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
		

}
