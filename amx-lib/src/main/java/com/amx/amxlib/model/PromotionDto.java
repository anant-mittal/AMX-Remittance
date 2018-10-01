package com.amx.amxlib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PromotionDto {

	String prize;
	String prizeMessage;
	String transactionReference;

	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}

	public String getPrizeMessage() {
		return prizeMessage;
	}

	public void setPrizeMessage(String prizeMessage) {
		this.prizeMessage = prizeMessage;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}

	@JsonIgnore
	public boolean isChichenVoucher() {
		if ("CHICKEN KING SAGAR VOUCHER".equals(this.prize)) {
			return true;
		}
		return false;
	}
}
