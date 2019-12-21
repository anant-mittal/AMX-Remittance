package com.amx.jax.complaince;

import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;

import com.amx.jax.model.ResourceDTO;

public class ExCbkStrReportLogDto extends ResourceDTO {
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
	private ResourceDTO resourceDto;
	
	public BigDecimal getCbkStrRepLogId() {
		return cbkStrRepLogId;
	}
	public void setCbkStrRepLogId(BigDecimal cbkStrRepLogId) {
		this.cbkStrRepLogId = cbkStrRepLogId;
	}
	
	public BigDecimal getRemittanceTranxId() {
		return remittanceTranxId;
	}
	public void setRemittanceTranxId(BigDecimal remittanceTranxId) {
		this.remittanceTranxId = remittanceTranxId;
	}
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getCustomerrRef() {
		return customerrRef;
	}
	public void setCustomerrRef(BigDecimal customerrRef) {
		this.customerrRef = customerrRef;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCbkStrId() {
		return cbkStrId;
	}
	public void setCbkStrId(String cbkStrId) {
		this.cbkStrId = cbkStrId;
	}
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Clob getReqXml() {
		return reqXml;
	}
	public void setReqXml(Clob reqXml) {
		this.reqXml = reqXml;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	public ResourceDTO getResourceDto() {
		return resourceDto;
	}

	public void setResourceDto(ResourceDTO resourceDto) {
		this.resourceDto = resourceDto;
	}
	
	@Override
	public BigDecimal resourceId() {
		return this.remittanceTranxId;
	}
	@Override
	public String getResourceCode() {
		return this.cbkStrId;
	}
}
