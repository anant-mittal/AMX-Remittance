package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

public class CardTypeDto implements java.io.Serializable {

	private static final long serialVersionUID = 2381126548661383520L;
	private BigDecimal cardTypeId;
	private String cardType;
	private String cardDesc;

	public BigDecimal getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(BigDecimal cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardDesc() {
		return cardDesc;
	}

	public void setCardDesc(String cardDesc) {
		this.cardDesc = cardDesc;
	}

}
