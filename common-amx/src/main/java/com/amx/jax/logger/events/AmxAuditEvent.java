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
public abstract class AmxAuditEvent<T> extends AuditEvent<AmxAuditEvent<T>> {

	private static final long serialVersionUID = 2140018418307073180L;

	public interface EventStep extends EnumType {
	}

	protected EventStep step;
	protected String target = null;
	protected BigDecimal targetId = null;
	protected String field = null;
	protected String fromValue = null;
	protected String toValue = null;
	protected BigDecimal customerId = null;
	protected String customer = null;
	protected AuditActorInfo actor;
	protected RemitInfo trxn = null;
	protected CustInfo cust = null;
	protected ContactType contactType;

	public AmxAuditEvent(EventType type, Object target) {
		super(type);
		this.target = ArgUtil.parseAsString(target);
	}

	public AmxAuditEvent(EventType type, BigDecimal targetId, Object target) {
		super(type);
		this.targetId = targetId;
		this.target = ArgUtil.parseAsString(target);
	}

	public AmxAuditEvent(EventType type, BigDecimal targetId) {
		super(type);
		this.targetId = targetId;
	}

	public AmxAuditEvent(EventType type) {
		super(type);
	}

	private CustInfo cust() {
		if (this.cust == null) {
			this.cust = new CustInfo();
		}
		return this.cust;
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

	@SuppressWarnings("unchecked")
	public T step(EventStep step) {
		this.setStep(step);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T field(Object field) {
		this.field = getMergedString(this.field, ArgUtil.parseAsString(field, Constants.BLANK).toUpperCase());
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T from(Object value) {
		this.fromValue = getMergedString(this.fromValue, ArgUtil.parseAsString(value, Constants.BLANK));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T to(Object value) {
		this.toValue = getMergedString(this.toValue, ArgUtil.parseAsString(value, Constants.BLANK));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T customer(String customer) {
		this.setCustomer(customer);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T customerId(BigDecimal customerId) {
		this.setCustomerId(customerId);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T target(Object target) {
		this.target = getMergedString(this.target, ArgUtil.parseAsString(target, Constants.BLANK));
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T set(RemitInfo remit) {
		this.trxn = remit;
		return (T) this;
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

	public EventStep getStep() {
		return step;
	}

	public void setStep(EventStep step) {
		this.step = step;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

}
