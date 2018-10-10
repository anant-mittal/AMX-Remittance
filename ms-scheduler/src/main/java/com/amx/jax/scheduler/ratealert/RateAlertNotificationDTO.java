package com.amx.jax.scheduler.ratealert;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.amx.amxlib.model.RateAlertDTO;

public class RateAlertNotificationDTO {

	BigDecimal rate;
	String firstName;
	String middleName;
	String lastName;
	String email;
	String inputCur;
	String outputCur;
	BigDecimal inputAmount;
	BigDecimal outputAmount;
	Date date;

	public RateAlertNotificationDTO(RateAlertDTO rateAlert, BigDecimal rate) {
		this.setEmail(rateAlert.getAlertEmail());
		this.setFirstName(rateAlert.getCustomerFirstName());
		this.setMiddleName(rateAlert.getCustomerMiddleName());
		this.setLastName(rateAlert.getCustomerLastName());
		this.setInputCur(rateAlert.getBaseCurrencyQuote());
		this.setOutputCur(rateAlert.getForeignCurrencyQuote());
		this.setInputAmount(rateAlert.getPayAmount());
		this.setOutputAmount(rateAlert.getReceiveAmount());
		this.setRate(rate);
		this.setDate(Calendar.getInstance().getTime());
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RateAlertNotificationDTO [rate=");
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
