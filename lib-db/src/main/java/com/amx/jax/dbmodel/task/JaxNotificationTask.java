package com.amx.jax.dbmodel.task;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;

@Entity
@Table(name = "JAX_NOTIFICATION_TASK")
public class JaxNotificationTask {

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

	@Column(name = "DATA_DOC_TYPE_MASTER_ID")
	@OneToOne
	CustomerDocumentTypeMaster docTypeMaster;

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

	public CustomerDocumentTypeMaster getDocTypeMaster() {
		return docTypeMaster;
	}

	public void setDocTypeMaster(CustomerDocumentTypeMaster docTypeMaster) {
		this.docTypeMaster = docTypeMaster;
	}

}
