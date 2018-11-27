package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "EX_EXCHANGE_RATE_MASTER_APRDET")
public class ExchangeRateApprovalDetModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal exchangeRateMasterAprDetId;
	private BigDecimal applicationCountryId;
	private String approvedBy;
	private Date approvedDate;
	private String authorisedBy;
	private Date authorisedDate;
	private BankMasterModel bankMaster;
	private BigDecimal buyRateMax;
	private BigDecimal buyRateMin;
	private BigDecimal corporateRate;
	private BigDecimal countryBranchId;
	private BigDecimal countryId;
	private String createdBy;
	private Date createdDate;
	private BigDecimal currencyId;
	private BigDecimal deliveryModeId;
	private BigDecimal exchangeRateMasterId;
	private String isActive;
	private String modifiedBy;
	private Date modifiedDate;
	private String remarks;
	private BigDecimal remitanceModeId;
	private BigDecimal sellRateMin;
	private BigDecimal sellRateMax;
	private BigDecimal serviceId;
	private BigDecimal prvBuyRateMax;
	private BigDecimal prvBuyRateMin;
	private BigDecimal prvSellRateMin;
	private BigDecimal prvSellRateMax;
	
	public ExchangeRateApprovalDetModel() {
		super();
	}




	@Id
	@GeneratedValue(generator="ex_exchange_rate_master_ap_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_exchange_rate_master_ap_seq",sequenceName="EX_EXCHANGE_RATE_MASTER_AP_SEQ",allocationSize=1)
	@Column(name ="EXCHANGE_RATE_MASTER_APR_ID" , unique=true, nullable=false, precision=22, scale=0)
	public BigDecimal getExchangeRateMasterAprDetId() {
		return exchangeRateMasterAprDetId;
	}
	public void setExchangeRateMasterAprDetId(BigDecimal exchangeRateMasterAprDetId) {
		this.exchangeRateMasterAprDetId = exchangeRateMasterAprDetId;
	}

	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	@Column(name = "APPROVED_BY")
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Column(name = "APPROVED_DATE")
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	@Column(name = "AUTHORISED_BY")
	public String getAuthorisedBy() {
		return authorisedBy;
	}
	public void setAuthorisedBy(String authorisedBy) {
		this.authorisedBy = authorisedBy;
	}

	@Column(name = "AUTHORISED_DATE")
	public Date getAuthorisedDate() {
		return authorisedDate;
	}
	public void setAuthorisedDate(Date authorisedDate) {
		this.authorisedDate = authorisedDate;
	}

	@Column(name = "BUY_RATE_MAX")
	public BigDecimal getBuyRateMax() {
		return buyRateMax;
	}
	public void setBuyRateMax(BigDecimal buyRateMax) {
		this.buyRateMax = buyRateMax;
	}

	@Column(name = "BUY_RATE_MIN")
	public BigDecimal getBuyRateMin() {
		return buyRateMin;
	}
	public void setBuyRateMin(BigDecimal buyRateMin) {
		this.buyRateMin = buyRateMin;
	}

	@Column(name = "CORPORATE_RATE")
	public BigDecimal getCorporateRate() {
		return corporateRate;
	}
	public void setCorporateRate(BigDecimal corporateRate) {
		this.corporateRate = corporateRate;
	}

	@Column(name = "COUNTRY_BRANCH_ID")
	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}
	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "CURRENCY_ID")
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name = "DELIVERY_MODE_ID")
	public BigDecimal getDeliveryModeId() {
		return deliveryModeId;
	}
	public void setDeliveryModeId(BigDecimal deliveryModeId) {
		this.deliveryModeId = deliveryModeId;
	}

	@Column(name = "EXCHANGE_RATE_MASTER_ID")
	public BigDecimal getExchangeRateMasterId() {
		return exchangeRateMasterId;
	}
	public void setExchangeRateMasterId(BigDecimal exchangeRateMasterId) {
		this.exchangeRateMasterId = exchangeRateMasterId;
	}

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Column(name = "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "REMITTANCE_MODE_ID")
	public BigDecimal getRemitanceModeId() {
		return remitanceModeId;
	}
	public void setRemitanceModeId(BigDecimal remitanceModeId) {
		this.remitanceModeId = remitanceModeId;
	}

	@Column(name = "SELL_RATE_MIN")
	public BigDecimal getSellRateMin() {
		return sellRateMin;
	}
	public void setSellRateMin(BigDecimal sellRateMin) {
		this.sellRateMin = sellRateMin;
	}

	@Column(name = "SELL_RATE_MAX")
	public BigDecimal getSellRateMax() {
		return sellRateMax;
	}
	public void setSellRateMax(BigDecimal sellRateMax) {
		this.sellRateMax = sellRateMax;
	}

	@Column(name = "SERVICE_INDICATOR_ID")
	public BigDecimal getServiceId() {
		return serviceId;
	}
	public void setServiceId(BigDecimal serviceId) {
		this.serviceId = serviceId;
	}

	@Column(name = "PRV_BUY_RATE_MAX")
	public BigDecimal getPrvBuyRateMax() {
		return prvBuyRateMax;
	}
	public void setPrvBuyRateMax(BigDecimal prvBuyRateMax) {
		this.prvBuyRateMax = prvBuyRateMax;
	}

	@Column(name = "PRV_BUY_RATE_MIN")
	public BigDecimal getPrvBuyRateMin() {
		return prvBuyRateMin;
	}
	public void setPrvBuyRateMin(BigDecimal prvBuyRateMin) {
		this.prvBuyRateMin = prvBuyRateMin;
	}

	@Column(name = "PRV_SELL_RATE_MIN")
	public BigDecimal getPrvSellRateMin() {
		return prvSellRateMin;
	}
	public void setPrvSellRateMin(BigDecimal prvSellRateMin) {
		this.prvSellRateMin = prvSellRateMin;
	}

	@Column(name = "PRV_SELL_RATE_MAX")
	public BigDecimal getPrvSellRateMax() {
		return prvSellRateMax;
	}
	public void setPrvSellRateMax(BigDecimal prvSellRateMax) {
		this.prvSellRateMax = prvSellRateMax;
	}

	@JoinColumn(name = "BANK_ID")
	@ManyToOne(fetch = FetchType.EAGER)
	public BankMasterModel getBankMaster() {
		return bankMaster;
	}

	public void setBankMaster(BankMasterModel bankMaster) {
		this.bankMaster = bankMaster;
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationCountryId == null) ? 0 : applicationCountryId.hashCode());
		result = prime * result + ((approvedBy == null) ? 0 : approvedBy.hashCode());
		result = prime * result + ((approvedDate == null) ? 0 : approvedDate.hashCode());
		result = prime * result + ((authorisedBy == null) ? 0 : authorisedBy.hashCode());
		result = prime * result + ((authorisedDate == null) ? 0 : authorisedDate.hashCode());
		result = prime * result + ((bankMaster == null) ? 0 : bankMaster.hashCode());
		result = prime * result + ((buyRateMax == null) ? 0 : buyRateMax.hashCode());
		result = prime * result + ((buyRateMin == null) ? 0 : buyRateMin.hashCode());
		result = prime * result + ((corporateRate == null) ? 0 : corporateRate.hashCode());
		result = prime * result + ((countryBranchId == null) ? 0 : countryBranchId.hashCode());
		result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((currencyId == null) ? 0 : currencyId.hashCode());
		result = prime * result + ((deliveryModeId == null) ? 0 : deliveryModeId.hashCode());
		result = prime * result + ((exchangeRateMasterAprDetId == null) ? 0 : exchangeRateMasterAprDetId.hashCode());
		result = prime * result + ((exchangeRateMasterId == null) ? 0 : exchangeRateMasterId.hashCode());
		result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
		result = prime * result + ((modifiedBy == null) ? 0 : modifiedBy.hashCode());
		result = prime * result + ((modifiedDate == null) ? 0 : modifiedDate.hashCode());
		result = prime * result + ((prvBuyRateMax == null) ? 0 : prvBuyRateMax.hashCode());
		result = prime * result + ((prvBuyRateMin == null) ? 0 : prvBuyRateMin.hashCode());
		result = prime * result + ((prvSellRateMax == null) ? 0 : prvSellRateMax.hashCode());
		result = prime * result + ((prvSellRateMin == null) ? 0 : prvSellRateMin.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result + ((remitanceModeId == null) ? 0 : remitanceModeId.hashCode());
		result = prime * result + ((sellRateMax == null) ? 0 : sellRateMax.hashCode());
		result = prime * result + ((sellRateMin == null) ? 0 : sellRateMin.hashCode());
		result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
		return result;
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExchangeRateApprovalDetModel other = (ExchangeRateApprovalDetModel) obj;
		if (applicationCountryId == null) {
			if (other.applicationCountryId != null)
				return false;
		} else if (!applicationCountryId.equals(other.applicationCountryId))
			return false;
		if (approvedBy == null) {
			if (other.approvedBy != null)
				return false;
		} else if (!approvedBy.equals(other.approvedBy))
			return false;
		if (approvedDate == null) {
			if (other.approvedDate != null)
				return false;
		} else if (!approvedDate.equals(other.approvedDate))
			return false;
		if (authorisedBy == null) {
			if (other.authorisedBy != null)
				return false;
		} else if (!authorisedBy.equals(other.authorisedBy))
			return false;
		if (authorisedDate == null) {
			if (other.authorisedDate != null)
				return false;
		} else if (!authorisedDate.equals(other.authorisedDate))
			return false;
		if (bankMaster == null) {
			if (other.bankMaster != null)
				return false;
		} else if (!bankMaster.equals(other.bankMaster))
			return false;
		if (buyRateMax == null) {
			if (other.buyRateMax != null)
				return false;
		} else if (!buyRateMax.equals(other.buyRateMax))
			return false;
		if (buyRateMin == null) {
			if (other.buyRateMin != null)
				return false;
		} else if (!buyRateMin.equals(other.buyRateMin))
			return false;
		if (corporateRate == null) {
			if (other.corporateRate != null)
				return false;
		} else if (!corporateRate.equals(other.corporateRate))
			return false;
		if (countryBranchId == null) {
			if (other.countryBranchId != null)
				return false;
		} else if (!countryBranchId.equals(other.countryBranchId))
			return false;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (currencyId == null) {
			if (other.currencyId != null)
				return false;
		} else if (!currencyId.equals(other.currencyId))
			return false;
		if (deliveryModeId == null) {
			if (other.deliveryModeId != null)
				return false;
		} else if (!deliveryModeId.equals(other.deliveryModeId))
			return false;
		if (exchangeRateMasterAprDetId == null) {
			if (other.exchangeRateMasterAprDetId != null)
				return false;
		} else if (!exchangeRateMasterAprDetId.equals(other.exchangeRateMasterAprDetId))
			return false;
		if (exchangeRateMasterId == null) {
			if (other.exchangeRateMasterId != null)
				return false;
		} else if (!exchangeRateMasterId.equals(other.exchangeRateMasterId))
			return false;
		if (isActive == null) {
			if (other.isActive != null)
				return false;
		} else if (!isActive.equals(other.isActive))
			return false;
		if (modifiedBy == null) {
			if (other.modifiedBy != null)
				return false;
		} else if (!modifiedBy.equals(other.modifiedBy))
			return false;
		if (modifiedDate == null) {
			if (other.modifiedDate != null)
				return false;
		} else if (!modifiedDate.equals(other.modifiedDate))
			return false;
		if (prvBuyRateMax == null) {
			if (other.prvBuyRateMax != null)
				return false;
		} else if (!prvBuyRateMax.equals(other.prvBuyRateMax))
			return false;
		if (prvBuyRateMin == null) {
			if (other.prvBuyRateMin != null)
				return false;
		} else if (!prvBuyRateMin.equals(other.prvBuyRateMin))
			return false;
		if (prvSellRateMax == null) {
			if (other.prvSellRateMax != null)
				return false;
		} else if (!prvSellRateMax.equals(other.prvSellRateMax))
			return false;
		if (prvSellRateMin == null) {
			if (other.prvSellRateMin != null)
				return false;
		} else if (!prvSellRateMin.equals(other.prvSellRateMin))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (remitanceModeId == null) {
			if (other.remitanceModeId != null)
				return false;
		} else if (!remitanceModeId.equals(other.remitanceModeId))
			return false;
		if (sellRateMax == null) {
			if (other.sellRateMax != null)
				return false;
		} else if (!sellRateMax.equals(other.sellRateMax))
			return false;
		if (sellRateMin == null) {
			if (other.sellRateMin != null)
				return false;
		} else if (!sellRateMin.equals(other.sellRateMin))
			return false;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		return true;
	}
	
}
