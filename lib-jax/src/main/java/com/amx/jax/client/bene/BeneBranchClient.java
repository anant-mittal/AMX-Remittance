package com.amx.jax.client.bene;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.branch.IBranchBeneService;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.request.benebranch.AddBeneBankRequest;
import com.amx.jax.model.request.benebranch.AddBeneCashRequest;
import com.amx.jax.model.request.benebranch.AddNewBankBranchRequest;
import com.amx.jax.model.request.benebranch.BankBranchListRequest;
import com.amx.jax.model.request.benebranch.ListBeneBankOrCashRequest;
import com.amx.jax.model.request.benebranch.ListBeneRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneBankRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneCashRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneStatusRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.benebranch.AddBeneResponse;
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.model.response.benebranch.BeneStatusDto;
import com.amx.jax.rest.RestService;

@Component
public class BeneBranchClient implements IBranchBeneService {

	private static final Logger LOGGER = Logger.getLogger(BeneBranchClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<BankMasterDTO, Object> listBeneBank(ListBeneBankOrCashRequest request) {
			LOGGER.debug("in listBank :");
			return restService.ajax(appConfig.getJaxURL() + Path.LIST_BANK_BY_COUNTRY_CURRENCY).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BankMasterDTO, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<RoutingBankMasterDTO, Object> listBeneCashAgents(ListBeneBankOrCashRequest request) {
		
			LOGGER.debug("in listBeneCashAgents :");
			return restService.ajax(appConfig.getJaxURL() + Path.LIST_CASH_AGENTS_BY_COUNTRY_CURRENCY).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<RoutingBankMasterDTO, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BankBranchDto, Object> listBankBranch(BankBranchListRequest request) {
		
			LOGGER.debug("in listBankBranch :");
			return restService.ajax(appConfig.getJaxURL() + Path.LIST_BANK_BRANCH).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BankBranchDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<AddBeneResponse, Object> addBeneBank(AddBeneBankRequest request) {
					
		LOGGER.debug("in addBeneBank :");
			return restService.ajax(appConfig.getJaxURL() + Path.ADD_BENE_BANK).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<AddBeneResponse, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<AddBeneResponse, Object> addBenecash(AddBeneCashRequest request) {
		
			LOGGER.debug("in addBenecash :");
			return restService.ajax(appConfig.getJaxURL() + Path.ADD_BENE_CASH).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<AddBeneResponse, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> addNewBankBranchRequest(AddNewBankBranchRequest request) {
		
			LOGGER.debug("in addBenecash :");
			return restService.ajax(appConfig.getJaxURL() + Path.ADD_NEW_BRANCH_REQUEST).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BeneStatusDto, Object> getBeneListStatuses() {
		
			LOGGER.debug("in getBeneListStatuses :");
			return restService.ajax(appConfig.getJaxURL() + Path.GET_BENE_LIST_STATUSES).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BeneStatusDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BeneficiaryListDTO, Object> listBene(ListBeneRequest request) {
		
			LOGGER.debug("in listBene :");
			return restService.ajax(appConfig.getJaxURL() + Path.LIST_BENE).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BeneficiaryListDTO, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateBeneStatus(UpdateBeneStatusRequest request) {
		
			LOGGER.debug("in updateBeneStatus :");
			return restService.ajax(appConfig.getJaxURL() + Path.UPDATE_BENE_STATUS).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateBeneBank(UpdateBeneBankRequest request) {
		
			LOGGER.debug("in updateBeneBank :");
			return restService.ajax(appConfig.getJaxURL() + Path.UPDATE_BENE_BANK).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> updateBeneCash(UpdateBeneCashRequest request) {
		
			LOGGER.debug("in updateBeneCash :");
			return restService.ajax(appConfig.getJaxURL() + Path.UPDATE_BENE_CASH).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<BeneficiaryListDTO, Object> getBeneByIdNo(Integer idNo) {
		LOGGER.debug("in getBeneByIdNo :");
		return restService.ajax(appConfig.getJaxURL() + Path.GET_BENE_BY_IDNO).meta(new JaxMetaInfo()).queryParam(Params.ID_NO, idNo).get()
				.as(new ParameterizedTypeReference<AmxApiResponse<BeneficiaryListDTO, Object>>() {
				});
	}
}
