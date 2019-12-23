package com.amx.jax.complaince;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.model.ResourceDTO;

@Entity
@Table(name = "EX_CBK_STR_REPORT_LOG")
public class ExCbkStrReportLOG implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal cbkStrRepLogId;
	private BigDecimal remittanceTranxId;
	private BigDecimal customerId;
	private BigDecimal customerrRef;
	private String customerName;
	private String cbkStrId;
	private String actionCode;
	private String reasonCode;
	private Date createdDate;
	private String createdBy;
	private Clob reqXml;
	private String ipAddress;
	private String reportType;
	private String custIsActive;
	
	@Id
	@GeneratedValue(generator = "ex_cbk_report_log_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_cbk_report_log_seq", sequenceName = "EX_CBK_REPORT_LOG_SEQ", allocationSize = 1)
	@Column(name = "CBK_STR_REP_LOG_ID")
	public BigDecimal getCbkStrRepLogId() {
		return cbkStrRepLogId;
	}
	public void setCbkStrRepLogId(BigDecimal cbkStrRepLogId) {
		this.cbkStrRepLogId = cbkStrRepLogId;
	}
	
	@Column(name = "REMITTANCE_TRANSACTION_ID")
	public BigDecimal getRemittanceTranxId() {
		return remittanceTranxId;
	}
	public void setRemittanceTranxId(BigDecimal remittanceTranxId) {
		this.remittanceTranxId = remittanceTranxId;
	}
	
	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	@Column(name = "CUSTOMER_REFERENCE")
	public BigDecimal getCustomerrRef() {
		return customerrRef;
	}
	public void setCustomerrRef(BigDecimal customerrRef) {
		this.customerrRef = customerrRef;
	}
	@Column(name = "CUSTOMER_NAME")
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	@Column(name = "CBK_STR_ID")
	public String getCbkStrId() {
		return cbkStrId;
	}
	public void setCbkStrId(String cbkStrId) {
		this.cbkStrId = cbkStrId;
	}
	@Column(name = "ACTION_CODE")
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	@Column(name = "REASON_CODE")
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Column(name = "REQ_XML")
	public Clob getReqXml() {
		return reqXml;
	}
	public void setReqXml(Clob reqXml) {
		this.reqXml = reqXml;
	}
	@Column(name = "IP_ADDRESS")
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	@Column(name = "REPORT_TYPE")
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	@Column(name = "CUST_ISACTIVE")
	public String getCustIsActive() {
		return custIsActive;
	}
	public void setCustIsActive(String custIsActive) {
		this.custIsActive = custIsActive;
	}
	
	
	

}
