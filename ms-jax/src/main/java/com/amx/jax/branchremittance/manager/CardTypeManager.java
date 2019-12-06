package com.amx.jax.branchremittance.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dao.CardTypeDao;
import com.amx.jax.dbmodel.CardTypeViewModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.remittance.CardTypeDto;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class CardTypeManager extends AbstractModel {

	Logger logger = LoggerFactory.getLogger(getClass());
	private static final long serialVersionUID = 1L;

	@Autowired
	CardTypeDao CardTypeDao;

	public List<CardTypeDto> fetchCardTypeList(BigDecimal languageId) {
		List<CardTypeDto> cardTypes = new ArrayList<>();
		List<CardTypeViewModel> cardTypeList = CardTypeDao.fetchCartList(languageId);

		if (cardTypeList != null && cardTypeList.size() != 0) {
			for (CardTypeViewModel cardTypeVal : cardTypeList) {
				CardTypeDto cardTypeDto = new CardTypeDto();

				cardTypeDto.setCardId(cardTypeVal.getCardId());
				cardTypeDto.setCardType(cardTypeVal.getCardType());
				cardTypeDto.setCardDesc(cardTypeVal.getCardDesc());
				cardTypes.add(cardTypeDto);
			}
		} else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No records found for Customer Card Type");
		}

		logger.info("cardType list size is : " + cardTypeList.size());
		return cardTypes;
	}

}
