package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_VW_PAYMENT_CARDTYPE")
public class CardTypeViewModel implements Serializable {

	private static final long serialVersionUID = 2381126548661383520L;

	@Id
	@Column(name = "UUID")
	private String uid;

	@Column(name = "CARD_ID")
	private BigDecimal cardId;

	@Column(name = "CARD_TYPE_CD")
	private String cardType;

	@Column(name = "CARDTYPE_DESCRIPTION")
	private String cardDesc;

	@Column(name = "LANGUAGE_ID")
	private BigDecimal languageId;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public BigDecimal getCardId() {
		return cardId;
	}

	public void setCardId(BigDecimal cardId) {
		this.cardId = cardId;
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

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

}
