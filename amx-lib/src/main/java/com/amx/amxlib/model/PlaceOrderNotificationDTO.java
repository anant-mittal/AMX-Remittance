package com.amx.amxlib.model;

import java.math.BigDecimal;
import java.util.Date;

public class PlaceOrderNotificationDTO extends AbstractModel implements Cloneable{

	/**
	 * 
	 */
    BigDecimal onlinePlaceOrderId;
	BigDecimal rate;
	String firstName;
	String middleName;
	String lastName;
	String email;
	String inputCur;
	String outputCur;
	BigDecimal inputAmount;
	BigDecimal outputAmount;
	String date;

	

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

	public BigDecimal getInputAmount() {
		return inputAmount;
	}

	public void setInputAmount(BigDecimal inputAmount) {
		this.inputAmount = inputAmount;
	}

	public BigDecimal getOutputAmount() {
		return outputAmount;
	}

	public void setOutputAmount(BigDecimal outputAmount) {
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
		builder.append("]");
		return builder.toString();
	}
	
}
