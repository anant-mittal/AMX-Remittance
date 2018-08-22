package com.amx.amxlib.model.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = RemittanceTransactionRequestModel.class)
public interface IRemitTransReqPurpose {

	BigDecimal getBeneId();

	void setBeneId(BigDecimal beneId);

	BigDecimal getLocalAmount();

	void setLocalAmount(BigDecimal localAmount);

	BigDecimal getForeignAmount();

	void setForeignAmount(BigDecimal foreignAmount);
	
}
