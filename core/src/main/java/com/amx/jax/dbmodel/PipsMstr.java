package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PIPS_MSTR")
public class PipsMstr implements Serializable {

	    private static final long serialVersionUID = 1L;
	    private BigDecimal country;
		private String curCod;
		private String corsBnk;
		private String remtMod;
		private BigDecimal locCod;
		private BigDecimal frmAmt;
		private BigDecimal toAmt;
		private String createdBy;
		private Date createdDate;
		private String modifiedBy;
		private BigDecimal derivedSellRate;
		private BigDecimal derivedRate;
		private BigDecimal pips;
		private String pipsType;
		
		@Id
		@Column(name = "COUNTRY")
		public BigDecimal getCountry() {
			return country;
		}
		public void setCountry(BigDecimal country) {
			this.country = country;
		}
		
		@Column(name = "CURCOD")
		public String getCurCod() {
			return curCod;
		}
		public void setCurCod(String curCod) {
			this.curCod = curCod;
		}
		
		@Column(name = "CORSBNK")
		public String getCorsBnk() {
			return corsBnk;
		}
		public void setCorsBnk(String corsBnk) {
			this.corsBnk = corsBnk;
		}
		
		@Column(name = "REMTMOD")
		public String getRemtMod() {
			return remtMod;
		}
		public void setRemtMod(String remtMod) {
			this.remtMod = remtMod;
		}
		
		@Column(name = "LOCCOD")
		public BigDecimal getLocCod() {
			return locCod;
		}
		public void setLocCod(BigDecimal locCod) {
			this.locCod = locCod;
		}
		
		@Column(name = "FRM_AMT")
		public BigDecimal getFrmAmt() {
			return frmAmt;
		}
		public void setFrmAmt(BigDecimal frmAmt) {
			this.frmAmt = frmAmt;
		}
		
		@Column(name = "TO_AMT")
		public BigDecimal getToAmt() {
			return toAmt;
		}
		public void setToAmt(BigDecimal toAmt) {
			this.toAmt = toAmt;
		}
		
		@Column(name = "CREATOR")
		public String getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}
		
		@Column(name = "CRTDAT")
		public Date getCreatedDate() {
			return createdDate;
		}
		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}
		
		@Column(name = "MODIFIER")
		public String getModifiedBy() {
			return modifiedBy;
		}
		public void setModifiedBy(String modifiedBy) {
			this.modifiedBy = modifiedBy;
		}
		
		@Column(name = "DERIVED_SELL_RATE")
		public BigDecimal getDerivedSellRate() {
			return derivedSellRate;
		}
		public void setDerivedSellRate(BigDecimal derivedSellRate) {
			this.derivedSellRate = derivedSellRate;
		}
		
		@Column(name = "DERIVED_RATE")
		public BigDecimal getDerivedRate() {
			return derivedRate;
		}
		public void setDerivedRate(BigDecimal derivedRate) {
			this.derivedRate = derivedRate;
		}
		
		@Column(name = "PIPS")
		public BigDecimal getPips() {
			return pips;
		}
		public void setPips(BigDecimal pips) {
			this.pips = pips;
		}
		
		@Column(name = "PIPS_TYPE")
		public String getPipsType() {
			return pipsType;
		}
		public void setPipsType(String pipsType) {
			this.pipsType = pipsType;
		}
}	
		
		
