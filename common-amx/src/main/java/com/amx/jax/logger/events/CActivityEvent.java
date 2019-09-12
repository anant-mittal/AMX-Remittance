package com.amx.jax.logger.events;

import java.math.BigDecimal;

import com.amx.jax.dict.ContactType;
import com.amx.jax.logger.AuditEvent;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.EnumType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CActivityEvent extends AuditEvent {

	private static final long serialVersionUID = -3189696554945071766L;

	public static enum Type implements EventType {

		LOGIN,

		VALIDATION,

		PROFILE_UPDATE, ACCOUNT_LOCKED, ACCOUNT_UNLOCKED,

		BENE_ADD, BENE_UPDATE,

		APPLICATION_CREATED, APPLICATION_UPDATE,

		FC_UPDATE, TRANSACTION_CREATED,

		CONTACT_VERF,

		TP_REDIRECT;

		@Override
		public EventMarker marker() {
			return EventMarker.AUDIT;
		}
	}

	public static enum Step implements EnumType {
		CREATE, SEND, RESEND, INIT, COMPLETE, VERIFY
	}

	public CActivityEvent(Type type, Object target) {
		super(type);
		this.target = ArgUtil.parseAsString(target);
	}

	public CActivityEvent(Type type, BigDecimal targetId, Object target) {
		super(type);
		this.targetId = targetId;
		this.target = ArgUtil.parseAsString(target);
	}

	public CActivityEvent(Type type, BigDecimal targetId) {
		super(type);
		this.targetId = targetId;
	}

	public CActivityEvent(Type type) {
		super(type);
	}

	private Step step;
	private String target = null;
	private BigDecimal targetId = null;
	private String field = null;
	private String fromValue = null;
	private String toValue = null;
	private BigDecimal customerId = null;
	private String customer = null;
	private AuditActorInfo actor;
	private RemitInfo trxn = null;
	private CustInfo cust = null;
	private ContactType contactType;

	private CustInfo cust() {
		if (this.cust == null) {
			this.cust = new CustInfo();
		}
		return this.cust;
	}

	@Override
	public String getDescription() {
		return this.type + (ArgUtil.isEmpty(step) ? Constants.BLANK : ("_" + step)) + ":" + this.result;
	}

	public String getFromValue() {
		return fromValue;
	}

	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}

	public String getToValue() {
		return toValue;
	}

	public void setToValue(String toValue) {
		this.toValue = toValue;
	}

	public AuditActorInfo getActor() {
		return actor;
	}

	public void setActor(AuditActorInfo actor) {
		this.actor = actor;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
		this.cust().setId(customerId);
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public CActivityEvent step(Step step) {
		this.setStep(step);
		return this;
	}

	public CActivityEvent field(Object field) {
		this.field = getMergedString(this.field, ArgUtil.parseAsString(field, Constants.BLANK).toUpperCase());
		return this;
	}

	public CActivityEvent from(Object value) {
		this.fromValue = getMergedString(this.fromValue, ArgUtil.parseAsString(value, Constants.BLANK));
		return this;
	}

	public CActivityEvent to(Object value) {
		this.toValue = getMergedString(this.toValue, ArgUtil.parseAsString(value, Constants.BLANK));
		return this;
	}

	public CActivityEvent customer(String customer) {
		this.setCustomer(customer);
		return this;
	}

	public CActivityEvent customerId(BigDecimal customerId) {
		this.setCustomerId(customerId);
		return this;
	}

	public CActivityEvent target(Object target) {
		this.target = getMergedString(this.target, ArgUtil.parseAsString(field, Constants.BLANK));
		return this;
	}

	public CActivityEvent set(RemitInfo remit) {
		this.trxn = remit;
		return this;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public BigDecimal getTargetId() {
		return targetId;
	}

	public void setTargetId(BigDecimal targetId) {
		this.targetId = targetId;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
		this.cust().setIdentity(customer);
	}

	public static String getMergedString(String oldStr, String newStr) {
		if (!ArgUtil.isEmpty(newStr)) {
			oldStr = ArgUtil.isEmpty(oldStr) ? newStr
					: String.format("%s;%s", oldStr, newStr);
		}
		return oldStr;
	}

	public RemitInfo getTrxn() {
		return trxn;
	}

	public void setTrxn(RemitInfo trxn) {
		this.trxn = trxn;
	}

	public CustInfo getCust() {
		return cust;
	}

	public void setCust(CustInfo cust) {
		this.cust = cust;
	}

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

}
