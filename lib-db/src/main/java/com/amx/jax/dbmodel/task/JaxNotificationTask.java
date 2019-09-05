package com.amx.jax.dbmodel.task;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.amx.jax.client.task.JaxNotificationTaskType;

@Entity
@Table(name = "JAX_NOTIFICATION_TASK")
public class JaxNotificationTask {

	public JaxNotificationTask() {
		super();
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JAX_NOTIFICATION_TASK_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "JAX_NOTIFICATION_TASK_SEQ", sequenceName = "JAX_NOTIFICATION_TASK_SEQ", allocationSize = 1)
	BigDecimal id;

	@Column(name = "MESSAGE")
	String message;

	@Column(name = "CREATED_AT")
	Date createdAt;

	@Column(name = "DATA_REMIT_TRNX_ID")
	BigDecimal remittanceTransactionid;

	@Column(name = "DATA_DOCUMENT_CATEGORY")
	String documentCategory;

	@Column(name = "DATA_DOCUMENT_TYPES")
	String documentTypes;

	@Column(name = "DATA_CUSTOMER_ID")
	BigDecimal customerId;

	@OneToMany(mappedBy = "task")
	@Cascade(value = { CascadeType.ALL })
	List<JaxNotificationTaskAssign> taskAssign;

	@Column(name = "TASK_TYPE")
	@Enumerated(EnumType.STRING)
	JaxNotificationTaskType taskType;

	@Column(name = "PERMISSION")
	String permission;

	@Column(name = "REQUEST_ID")
	String requestId;

	@Column(name = "RESPONSE_ID")
	String responseId;

	@Column(name = "ISACTIVE")
	String isActive;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public BigDecimal getRemittanceTransactionid() {
		return remittanceTransactionid;
	}

	public void setRemittanceTransactionid(BigDecimal remittanceTransactionid) {
		this.remittanceTransactionid = remittanceTransactionid;
	}

	public String getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}

	public String getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(String documentTypes) {
		this.documentTypes = documentTypes;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public List<JaxNotificationTaskAssign> getTaskAssign() {
		return taskAssign;
	}

	public void setTaskAssign(List<JaxNotificationTaskAssign> taskAssign) {
		this.taskAssign = taskAssign;
	}

	public JaxNotificationTaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(JaxNotificationTaskType taskType) {
		this.taskType = taskType;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
