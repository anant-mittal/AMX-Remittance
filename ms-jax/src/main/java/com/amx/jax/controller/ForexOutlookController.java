package com.amx.jax.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.meta.IForexOutlookService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.fx.ForexOutLookRequest;
import com.amx.jax.model.response.fx.CurrencyPairDTO;
import com.amx.jax.model.response.fx.ForexOutLookResponseDTO;
import com.amx.jax.service.ForexOutlookService;

@RestController
public class ForexOutlookController implements IForexOutlookService {

	@Autowired
	ForexOutlookService forexOutlookService;

	@Autowired
	MetaData metaData;

	private static final Logger LOGGER = LoggerService.getLogger(ForexOutlookController.class);

	@RequestMapping(value = Path.CURRENCY_PAIR_GET, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<CurrencyPairDTO, Object> getCurrencyPairList() {
		List<CurrencyPairDTO> resultList = forexOutlookService.getCurrencyPairList();
		return AmxApiResponse.buildList(resultList);
	}

	@RequestMapping(value = Path.CURRENCY_PAIR_HISTORY_GET, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<ForexOutLookResponseDTO, Object> getCurpairHistory() {
		List<ForexOutLookResponseDTO> resultList = forexOutlookService.getCurpairHistory();
		return AmxApiResponse.buildList(resultList);
	}

	@RequestMapping(value = Path.CURRENCY_PAIR_SAVE_UPDATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> saveUpdateCurrencyPair(@RequestBody @Valid ForexOutLookRequest dto) {
		LOGGER.debug("saveUpdateCurrencyPair controller" + dto.toString());
		forexOutlookService.validateForexOutlookDto(dto);
		BigDecimal appCountryId = metaData.getCountryId();
		BigDecimal langId = metaData.getLanguageId();
		BigDecimal custId = metaData.getCustomerId();
		BoolRespModel response = forexOutlookService.saveUpdateCurrencyPair(dto, appCountryId, langId, custId);
		return AmxApiResponse.build(response);
	}

	@RequestMapping(value = Path.CURRENCY_PAIR_DELETE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> deleteCurrencyPair(
			@RequestParam(name = Params.PAIR_ID) BigDecimal pairId) {
		BoolRespModel response = forexOutlookService.deleteCurrencyPair(pairId);
		return AmxApiResponse.build(response);

	}

}
