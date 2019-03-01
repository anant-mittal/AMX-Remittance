package com.amx.jax.client.meta;

import java.math.BigDecimal;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.model.request.fx.ForexOutLookRequest;
import com.amx.jax.model.response.fx.CurrencyPairDTO;
import com.amx.jax.model.response.fx.ForexOutLookResponseDTO;

public interface IForexOutlookService {
	public static class Path {

		public static final String PREFIX = "/outlook";
		
		public static final String CURRENCY_PAIR_GET = PREFIX + "/currencyPairlist";
		public static final String CURRENCY_PAIR_SAVE_UPDATE = PREFIX + "/saveorupdate";
		public static final String CURRENCY_PAIR_HISTORY_GET = PREFIX + "/currencyPairHistory";
		public static final String CURRENCY_PAIR_DELETE = PREFIX + "/delete";

	}

	public static class Params {

		public static final String PAIR_ID = "pairId";

	}
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	public AmxApiResponse<CurrencyPairDTO, Object> getCurrencyPairList();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	public AmxApiResponse<ForexOutLookResponseDTO, Object> getCurpairHistory();

	@ApiJaxStatus({ JaxError.INVALID_PAIR_ID })
	public AmxApiResponse<BoolRespModel, Object> saveUpdateCurrencyPair(ForexOutLookRequest dto);

	@ApiJaxStatus({ JaxError.INVALID_PAIR_ID })
	public AmxApiResponse<BoolRespModel, Object> deleteCurrencyPair(BigDecimal pairId);

}
