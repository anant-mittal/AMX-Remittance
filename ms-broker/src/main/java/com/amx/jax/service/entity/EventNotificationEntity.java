package com.amx.jax.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EX_EVENT_NOTIFICATION")
public class EventNotificationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 * This table is created to hold notification records to any system component.
	 * 
	 * Table will be feeded by different system components (Oracle procedures, Java
	 * service, etc.) Table will be consume by scheduled service that will push the
	 * records into a Message Queue and delete it at later stage
	 * 
	 * 12th August 2018 Salman and Lalit
	 */

	@Id
	@Column(name = "EVENT_NOTIFICATION_ID")
	private BigDecimal event_notification_id;

	@Column(name = "EVENT_MASTER_ID")
	private BigDecimal event_master_id; // Foreign key from the table EX_EVENT_MASTER

	@Column(name = "EVENT_DATA")
	private String event_data; // Data should strictly follow the format below
								// Key1:value1;Key2:value2;Key3:value3

	@Column(name = "STATUS")
	private BigDecimal status;

	@Column(name = "CREATE_DATE")
	private Date create_date;

	@Column(name = "CREATED_BY")
	private String created_by;

	public BigDecimal getEvent_notification_id() {
		return event_notification_id;
	}

	public void setEvent_notification_id(BigDecimal event_notification_id) {
		this.event_notification_id = event_notification_id;
	}

	public BigDecimal getEvent_master_id() {
		return event_master_id;
	}

	public void setEvent_master_id(BigDecimal event_master_id) {
		this.event_master_id = event_master_id;
	}

	public String getEvent_data() {
		return event_data;
	}

	public void setEvent_data(String event_data) {
		this.event_data = event_data;
	}

	public BigDecimal getStatus() {
		return status;
	}

	public void setStatus(BigDecimal status) {
		this.status = status;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	@Override
	public String toString() {
		return "EventNotificationEntity [event_notification_id=" + event_notification_id + ", event_master_id="
				+ event_master_id + ", event_data=" + event_data + ", status=" + status + ", create_date=" + create_date
				+ ", created_by=" + created_by + "]";
	}
}
