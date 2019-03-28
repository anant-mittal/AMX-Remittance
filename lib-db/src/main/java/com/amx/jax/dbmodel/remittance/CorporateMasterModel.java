package com.amx.jax.dbmodel.remittance;

/**
 * @author rabil
 * @date  19 mar 2019
 */
import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EX_CORPORATE_MASTER")
public class CorporateMasterModel implements Serializable{

	/**

	 */
	private static final long serialVersionUID = -6729335664280195652L;
	
	@Id
	@Column(name="CORPORATE_MASTER_ID")
	private BigDecimal corporateMasterId;
	@Column(name="DISCOUNT_ON_COMMISSION")
	private BigDecimal discountOnCommission;
	@Column(name="CORPORATE_NAME")
	private String corporateName;
	@Column(name="ISACTIVE")
	private String isActive;
	public BigDecimal getCorporateMasterId() {
		return corporateMasterId;
	}
	public void setCorporateMasterId(BigDecimal corporateMasterId) {
		this.corporateMasterId = corporateMasterId;
	}
	public BigDecimal getDiscountOnCommission() {
		return discountOnCommission;
	}
	public void setDiscountOnCommission(BigDecimal discountOnCommission) {
		this.discountOnCommission = discountOnCommission;
	}
	public String getCorporateName() {
		return corporateName;
	}
	public void setCorporateName(String corporateName) {
		this.corporateName = corporateName;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}

