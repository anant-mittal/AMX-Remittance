package com.amx.jax.branchbene;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.branch.IBranchBeneService;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.benebranch.ListBeneBankOrCashRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.utils.JsonUtil;

import io.swagger.annotations.ApiOperation;

@RestController
public class BeneBranchController implements IBranchBeneService {

	private Logger logger = LoggerFactory.getLogger(BeneBranchController.class);

	@Autowired
	MetaData metaData;
	@Autowired
	BeneBranchService beneBranchService;

	@RequestMapping(value = Path.LIST_BANK_BY_COUNTRY_CURRENCY, method = RequestMethod.POST)
	@Override
	@ApiOperation("List bank by country and currency")
	public AmxApiResponse<BankMasterDTO, Object> listBeneBank(@RequestBody @Valid ListBeneBankOrCashRequest request) {
		logger.debug("request listBank: {} ", JsonUtil.toJson(request));
		List<BankMasterDTO> output = beneBranchService.getBankByCountryAndCurrency(request);
		return AmxApiResponse.buildList(output);
	}

	@RequestMapping(value = Path.LIST_CASH_AGENTS_BY_COUNTRY_CURRENCY, method = RequestMethod.POST)
	@Override
	@ApiOperation("List cash agents by country and currency")
	public AmxApiResponse<RoutingBankMasterDTO, Object> listBeneCashAgents(@RequestBody @Valid ListBeneBankOrCashRequest request) {
		logger.debug("request listBeneCashAgents: {} ", JsonUtil.toJson(request));
		List<RoutingBankMasterDTO> output = beneBranchService.getServiceProviderList(request);
		return AmxApiResponse.buildList(output);
	}

}
