package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author :Rabil
 *
 */

@Entity
@Table(name = "LOYALTY_YTDBAL" )
public class LoyaltyPointModel  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */

	
	private BigDecimal cusRef;
	private BigDecimal finYear;
	private BigDecimal YtdnetPoints;
	private String creator;	
	private Date createdDate;
	private Date updatedDate;
	
	private Date expiryDate;
	
	@Id
	@Column(name="CUSREF")
	public BigDecimal getCusRef() {
		return cusRef;
	}
	public void setCusRef(BigDecimal cusRef) {
		this.cusRef = cusRef;
	}
	@Column(name="FINYR")
	public BigDecimal getFinYear() {
		return finYear;
	}
	public void setFinYear(BigDecimal finYear) {
		this.finYear = finYear;
	}
	@Column(name="YTDNET_POINTS")
	public BigDecimal getYtdnetPoints() {
		return YtdnetPoints;
	}
	public void setYtdnetPoints(BigDecimal ytdnetPoints) {
		YtdnetPoints = ytdnetPoints;
	}
	@Column(name="CREATOR")
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	@Temporal(TemporalType.DATE)
	@Column(name="CRTDAT")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Temporal(TemporalType.DATE)
	@Column(name="UPDDAT")
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="EXPIRY_DT")
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	

}

