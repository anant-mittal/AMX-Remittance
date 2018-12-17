package com.amx.jax.dbmodel.fx;

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
@Table(name = "EX_FC_STOCK_TRNF_DETAIL")
public class ForeignCurrencyStockTransfer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "EX_FC_STOCK_TRNF_DETAIL_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "EX_FC_STOCK_TRNF_DETAIL_SEQ", sequenceName = "EX_FC_STOCK_TRNF_DETAIL_SEQ", allocationSize = 1)
	@Column(name = "FC_STOCK_TRF_ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal fcStockTrfId;
	
	@Column(name="DELIVERY_DET_SEQ_ID")
	private BigDecimal deliveryDetSeqId;
	
	@Column(name="COMPANY_ID")
	private BigDecimal companyId;
	
	@Column(name="COMCOD")
	private BigDecimal companyCode;
	
	@Column(name="DOCUMENT_NO")
	private BigDecimal documentNo;
	
	@Column(name="DOCUMENT_FINANCE_YEAR")
	private BigDecimal documentFinanceYear;
	
	@Column(name="DOCUMENT_CODE")
	private BigDecimal documentCode;
	
	@Column(name="DOCUMENT_DATE")
	private Date documentDate;
	
	@Column(name="CURRENCY_ID")
	private BigDecimal currencyId;
	
	@Column(name="FROM_BRANCH_ID")
	private BigDecimal fromBranchId;
	
	@Column(name="TO_BRANCH_ID")
	private BigDecimal toBranchId;
	
	@Column(name="FROM_EMPLOYEE_ID")
	private BigDecimal fromEmployeeId;
	
	@Column(name="TO_EMPLOYEE_ID")
	private BigDecimal toEmployeeId;
	
	@Column(name="ISACTIVE")
	private String isActive;
	
	@Column(name="FC_VALUE")
	private BigDecimal fcValue;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="CREATION_DATE")
	private Date creationDate;
	
	@Column(name="UPDATED_BY")
	private String updateBy;
	
	@Column(name="UPDATED_DATE")
	private Date updatedDate;
	
	@Column(name="ORDER_STATUS")
	private String orderStatus;
	
	@Column(name="CURCOD")
	private String currencyCode;
	
	@Column(name="FROM_LOCCOD")
	private BigDecimal fromLocationCode;
	
	@Column(name="TO_LOCCOD")
	private BigDecimal toLocationCode;
	
	@Column(name="FROM_USER")
	private String fromUser;
	
	@Column(name="TO_USER")
	private String toUser;
	
	@Column(name="GLENT")
	private String glent;
	
	@Column(name="GLDATE")
	private Date glDate;
	
	@Column(name="ELERR")
	private String elerr;
	
	@Column(name="ACCOUNT_MMYYYY")
	private Date accountMMYYYY;

	public BigDecimal getFcStockTrfId() {
		return fcStockTrfId;
	}

	public void setFcStockTrfId(BigDecimal fcStockTrfId) {
		this.fcStockTrfId = fcStockTrfId;
	}

	public BigDecimal getDeliveryDetSeqId() {
		return deliveryDetSeqId;
	}

	public void setDeliveryDetSeqId(BigDecimal deliveryDetSeqId) {
		this.deliveryDetSeqId = deliveryDetSeqId;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	public BigDecimal getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(BigDecimal companyCode) {
		this.companyCode = companyCode;
	}

	public BigDecimal getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(BigDecimal documentNo) {
		this.documentNo = documentNo;
	}

	public BigDecimal getDocumentFinanceYear() {
		return documentFinanceYear;
	}

	public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
		this.documentFinanceYear = documentFinanceYear;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getFromBranchId() {
		return fromBranchId;
	}

	public void setFromBranchId(BigDecimal fromBranchId) {
		this.fromBranchId = fromBranchId;
	}

	public BigDecimal getToBranchId() {
		return toBranchId;
	}

	public void setToBranchId(BigDecimal toBranchId) {
		this.toBranchId = toBranchId;
	}

	public BigDecimal getFromEmployeeId() {
		return fromEmployeeId;
	}

	public void setFromEmployeeId(BigDecimal fromEmployeeId) {
		this.fromEmployeeId = fromEmployeeId;
	}

	public BigDecimal getToEmployeeId() {
		return toEmployeeId;
	}

	public void setToEmployeeId(BigDecimal toEmployeeId) {
		this.toEmployeeId = toEmployeeId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getFcValue() {
		return fcValue;
	}

	public void setFcValue(BigDecimal fcValue) {
		this.fcValue = fcValue;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public BigDecimal getFromLocationCode() {
		return fromLocationCode;
	}

	public void setFromLocationCode(BigDecimal fromLocationCode) {
		this.fromLocationCode = fromLocationCode;
	}

	public BigDecimal getToLocationCode() {
		return toLocationCode;
	}

	public void setToLocationCode(BigDecimal toLocationCode) {
		this.toLocationCode = toLocationCode;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getGlent() {
		return glent;
	}

	public void setGlent(String glent) {
		this.glent = glent;
	}

	public Date getGlDate() {
		return glDate;
	}

	public void setGlDate(Date glDate) {
		this.glDate = glDate;
	}

	public String getElerr() {
		return elerr;
	}

	public void setElerr(String elerr) {
		this.elerr = elerr;
	}

	public Date getAccountMMYYYY() {
		return accountMMYYYY;
	}

	public void setAccountMMYYYY(Date accountMMYYYY) {
		this.accountMMYYYY = accountMMYYYY;
	}

	public BigDecimal getDocumentCode() {
		return documentCode;
	}

	public void setDocumentCode(BigDecimal documentCode) {
		this.documentCode = documentCode;
	}
	
}
