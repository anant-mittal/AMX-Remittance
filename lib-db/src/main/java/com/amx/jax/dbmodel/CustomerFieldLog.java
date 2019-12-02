package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.constant.DeviceState;

@Entity
@Table(name = "CUSTOMER_CH_LOG_ID")
public class CustomerFieldLog {

	@Id
	@Column(name = "CUSTOMER_ID")
	BigDecimal customerId;

	@Column(name = "FROM_TABLE_NAME")
	String fromTableName;

	@Column(name = "OLD_VALUE")
	String oldValue;

	@Column(name = "NEW_VALUE")
	String newValue;

	@Column(name = "UPDATED_DATE")
	Date modifiedDate;

	@Column(name = "CREATED_BY")
	String createdBy;

	@Column(name = "BRANCH_SYSTEM_INVENTORY_ID")
	BigDecimal branchSystemInventoryId;
	
	@Column(name = "STATE")
	@Enumerated(value = EnumType.STRING)
	DeviceState state;
	
	@Column(name = "PAIR_TOKEN")
	String pairToken;

	@Column(name = "SESSION_TOKEN")
	String sessionToken;

	@Column(name = "OTP_TOKEN")
	String otpToken;
	
	@Column(name = "CLIENT_SECRET")
	String clientSecret;
	
	@Column(name = "OTP_TOKEN_CREATED_DATE")
	Date otpTokenCreatedDate;
	
	@Column(name="EMPLOYEE_ID")
	BigDecimal employeeId;

}
