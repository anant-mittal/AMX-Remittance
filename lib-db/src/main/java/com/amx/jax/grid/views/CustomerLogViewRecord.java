package com.amx.jax.grid.views;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "JAX_VW_CUSTOMER_CH_LOG")
public class CustomerLogViewRecord implements Serializable {
	@Id
	@Column(name = "CUSTOMER_CH_LOG_ID")
	private BigDecimal id;

	@Column(name = "CUSTOMER_ID")
	private BigDecimal custmerId;

	@Column(name = "FROM_TABLE_NAME")
	private String fromTableName;

	@Column(name = "FROM_COLUMN_NAME")
	private String fromColumnName;

	@Column(name = "OLD_VALUE")
	private String oldValue;

	@Column(name = "NEW_VALUE")
	private String newValue;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CHANNEL")
	private String channel;

	
	public BigDecimal getCustmerId() {
		return custmerId;
	}

	public void setCustmerId(BigDecimal custmerId) {
		this.custmerId = custmerId;
	}

	public String getFromTableName() {
		return fromTableName;
	}

	public void setFromTableName(String fromTableName) {
		this.fromTableName = fromTableName;
	}

	public String getFromColumnName() {
		return fromColumnName;
	}

	public void setFromColumnName(String fromColumnName) {
		this.fromColumnName = fromColumnName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
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

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}
	
	

}
