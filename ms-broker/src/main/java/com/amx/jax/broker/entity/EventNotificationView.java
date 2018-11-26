package com.amx.jax.broker.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.annotation.Id;

import com.amx.jax.broker.ProjectConfig;

@Entity
@Table(name = ProjectConfig.VIEW_EVENT_NOTIFICATION) // This view created from the table EX_EVENT_NOTIFICATION and
														// EX_EVENT_MASTER
public class EventNotificationView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EVENT_NOTIFICATION_ID")
	private BigDecimal event_notification_id;

	@Column(name = "EVENT_MASTER_ID")
	private BigDecimal event_master_id; // Foreign key from the table EX_EVENT_MASTER

	@Column(name = "EVENT_CODE")
	private String event_code; // Alpha numeric event code for the event. Will be the key value to subscribe to
								// Message Queue

	@Column(name = "EVENT_DESC")
	private String event_desc;

	@Column(name = "EVENT_PRIORITY")
	private String event_priority; // L: Low, M: Moderate, H: High

	@Column(name = "EVENT_DATA")
	private String event_data; // Data should strictly follow the format below
								// Key1:value1;Key2:value2;Key3:value3

	@Column(name = "STATUS")
	private BigDecimal status; // Record status. 1: Success, 0: Failed, NULL: Newly created

	@Column(name = "CREATE_DATE")
	private Date create_date;

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

	public String getEvent_code() {
		return event_code;
	}

	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}

	public String getEvent_desc() {
		return event_desc;
	}

	public void setEvent_desc(String event_desc) {
		this.event_desc = event_desc;
	}

	public String getEvent_priority() {
		return event_priority;
	}

	public void setEvent_priority(String event_priority) {
		this.event_priority = event_priority;
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

	@Override
	public String toString() {
		return "EventNotificationView [event_notification_id=" + event_notification_id + ", event_master_id="
				+ event_master_id + ", event_code=" + event_code + ", event_desc=" + event_desc + ", event_priority="
				+ event_priority + ", event_data=" + event_data + ", status=" + status + ", create_date=" + create_date
				+ "]";
	}
}
