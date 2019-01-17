package com.amx.jax.client.remittance;

import org.apache.log4j.Logger;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.fx.FcSaleOrderClient;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;

public class RemittanceClient  implements IRemittanceService{
	private static final Logger LOGGER = Logger.getLogger(FcSaleOrderClient.class);

	@Override
	public AmxApiResponse<UserwiseTransactionDto, Object> getTotalCount(String transactiondate) {
		// TODO Auto-generated method stub
		return null;
	}
}
