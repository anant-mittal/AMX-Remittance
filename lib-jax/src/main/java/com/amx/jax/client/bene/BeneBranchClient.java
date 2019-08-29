package com.amx.jax.client.bene;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.branch.IBranchBeneService;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.request.benebranch.AddBeneBankRequest;
import com.amx.jax.model.request.benebranch.AddBeneCashRequest;
import com.amx.jax.model.request.benebranch.ListBankBranchRequest;
import com.amx.jax.model.request.benebranch.ListBeneBankOrCashRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.rest.RestService;

public class BeneBranchClient implements IBranchBeneService {

	private static final Logger LOGGER = Logger.getLogger(BeneBranchClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<BankMasterDTO, Object> listBeneBank(ListBeneBankOrCashRequest request) {
		try {
			LOGGER.debug("in listBank :");
			return restService.ajax(appConfig.getJaxURL() + Path.LIST_BANK_BY_COUNTRY_CURRENCY).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BankMasterDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in listBank : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<RoutingBankMasterDTO, Object> listBeneCashAgents(ListBeneBankOrCashRequest request) {
		try {
			LOGGER.debug("in listBeneCashAgents :");
			return restService.ajax(appConfig.getJaxURL() + Path.LIST_CASH_AGENTS_BY_COUNTRY_CURRENCY).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<RoutingBankMasterDTO, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in listBeneCashAgents : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BankBranchDto, Object> listBankBranch(ListBankBranchRequest request) {
		try {
			LOGGER.debug("in listBankBranch :");
			return restService.ajax(appConfig.getJaxURL() + Path.LIST_BANK_BRANCH).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BankBranchDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in listBankBranch : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> addBeneBank(AddBeneBankRequest request) {
		try {
			LOGGER.debug("in addBeneBank :");
			return restService.ajax(appConfig.getJaxURL() + Path.ADD_BENE_BANK).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in addBeneBank : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> addBenecash(AddBeneCashRequest request) {
		try {
			LOGGER.debug("in addBenecash :");
			return restService.ajax(appConfig.getJaxURL() + Path.ADD_BENE_CASH).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in addBenecash : ", e);
			return JaxSystemError.evaluate(e);
		}
	}
}
