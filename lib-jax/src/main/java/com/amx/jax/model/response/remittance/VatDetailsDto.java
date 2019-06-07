package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class VatDetailsDto {


	private BigDecimal vatPercentage=BigDecimal.ZERO;
	private String vatType;
	private BigDecimal vatAmount=BigDecimal.ZERO;
	private String vatApplicable;
	private String calculatuonType;
	private BigDecimal roudingOff=BigDecimal.ZERO;
	private BigDecimal commission=BigDecimal.ZERO;
	public BigDecimal getVatPercentage() {
		return vatPercentage;
	}
	public void setVatPercentage(BigDecimal vatPercentage) {
		this.vatPercentage = vatPercentage;
	}
	public String getVatType() {
		return vatType;
	}
	public void setVatType(String vatType) {
		this.vatType = vatType;
	}
	public BigDecimal getVatAmount() {
		return vatAmount;
	}
	public void setVatAmount(BigDecimal vatAmount) {
		this.vatAmount = vatAmount;
	}
	public String getVatApplicable() {
		return vatApplicable;
	}
	public void setVatApplicable(String vatApplicable) {
		this.vatApplicable = vatApplicable;
	}
	public String getCalculatuonType() {
		return calculatuonType;
	}
	public void setCalculatuonType(String calculatuonType) {
		this.calculatuonType = calculatuonType;
	}
	public BigDecimal getRoudingOff() {
		return roudingOff;
	}
	public void setRoudingOff(BigDecimal roudingOff) {
		this.roudingOff = roudingOff;
	}
	public BigDecimal getCommission() {
		return commission;
	}
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}
	
	
	
}

   