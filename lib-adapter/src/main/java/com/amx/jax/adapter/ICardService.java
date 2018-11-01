package com.amx.jax.adapter;

import com.amx.jax.device.CardData;

public interface ICardService {
	public CardData getCardDetailsByTerminal(String terminalId, Boolean wait, Boolean flush)
			throws InterruptedException;

	public void saveCardDetailsByTerminal(String terminalId, CardData data);

}
