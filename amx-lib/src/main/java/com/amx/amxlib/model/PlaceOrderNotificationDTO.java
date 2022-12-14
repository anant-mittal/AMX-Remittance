package com.amx.amxlib.model;

import java.math.BigDecimal;

import com.amx.jax.model.AbstractModel;

public class PlaceOrderNotificationDTO extends AbstractModel implements Cloneable {

	private static final long serialVersionUID = -6610524516718235211L;

	BigDecimal onlinePlaceOrderId;
	BigDecimal customerId;
	BigDecimal rate;
	String firstName;
	String middleName;
	String lastName;
	String email;
	String inputCur;
	String outputCur;
	String inputAmount;
	String outputAmount;
	String date;
	String time;

	public BigDecimal getOnlinePlaceOrderId() {
		return onlinePlaceOrderId;
	}

	public void setOnlinePlaceOrderId(BigDecimal onlinePlaceOrderId) {
		this.onlinePlaceOrderId = onlinePlaceOrderId;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInputCur() {
		return inputCur;
	}

	public void setInputCur(String inputCur) {
		this.inputCur = inputCur;
	}

	public String getOutputCur() {
		return outputCur;
	}

	public void setOutputCur(String outputCur) {
		this.outputCur = outputCur;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date2) {
		this.date = date2;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
	public String getInputAmount() {
		return inputAmount;
	}

	public void setInputAmount(String inputAmount) {
		this.inputAmount = inputAmount;
	}

	public String getOutputAmount() {
		return outputAmount;
	}

	public void setOutputAmount(String outputAmount) {
		this.outputAmount = outputAmount;
	}

	@Override
	public String getModelType() {
		return "place-order-not-dto";
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlaceOrderNotificationDTO [onlinePlaceOrderId=");
		builder.append(onlinePlaceOrderId);
		builder.append(", customerId=");
		builder.append(customerId);
		builder.append(", rate=");
		builder.append(rate);
		builder.append(", firstName=");
		builder.append(firstName);
		builder.append(", middleName=");
		builder.append(middleName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", email=");
		builder.append(email);
		builder.append(", inputCur=");
		builder.append(inputCur);
		builder.append(", outputCur=");
		builder.append(outputCur);
		builder.append(", inputAmount=");
		builder.append(inputAmount);
		builder.append(", outputAmount=");
		builder.append(outputAmount);
		builder.append(", date=");
		builder.append(date);
		builder.append(", time=");
		builder.append(time);
		builder.append("]");
		return builder.toString();
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

}
