package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class CustomerBankRelationNameDto {
BigDecimal relationId;
public BigDecimal getRelationId() {
	return relationId;
}
public void setRelationId(BigDecimal relationId) {
	this.relationId = relationId;
}
public String getCutomerBankNBame() {
	return cutomerBankNBame;
}
public void setCutomerBankNBame(String cutomerBankNBame) {
	this.cutomerBankNBame = cutomerBankNBame;
}
String cutomerBankNBame;

BigDecimal cardTypeId;
BigDecimal customerBankId;

public BigDecimal getCardTypeId() {
	return cardTypeId;
}
public void setCardTypeId(BigDecimal cardTypeId) {
	this.cardTypeId = cardTypeId;
}
public BigDecimal getCustomerBankId() {
	return customerBankId;
}
public void setCustomerBankId(BigDecimal customerBankId) {
	this.customerBankId = customerBankId;
}
	
}
