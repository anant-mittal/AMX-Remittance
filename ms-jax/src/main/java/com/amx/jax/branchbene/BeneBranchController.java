package com.amx.jax.branchbene;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.branch.IBranchBeneService;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.meta.MetaData;
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
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.model.response.benebranch.BeneStatusDto;
import com.amx.utils.JsonUtil;

import io.swagger.annotations.ApiOperation;

@RestController
public class BeneBranchController implements IBranchBeneService {

	private Logger logger = LoggerFactory.getLogger(BeneBranchController.class);

	@Autowired
	MetaData metaData;
	@Autowired
	BeneBranchService beneBranchService;
	@Autowired
	BeneBranchValidation beneBranchValidation;

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

	@RequestMapping(value = Path.LIST_BANK_BRANCH, method = RequestMethod.POST)
	@Override
	@ApiOperation("List bank branches")
	public AmxApiResponse<BankBranchDto, Object> listBankBranch(@RequestBody @Valid BankBranchListRequest request) {
		logger.debug("request listBankBranch: {} ", JsonUtil.toJson(request));
		List<BankBranchDto> output = beneBranchService.listBankBranch(request);
		return AmxApiResponse.buildList(output);
	}

	@RequestMapping(value = Path.ADD_BENE_BANK, method = RequestMethod.POST)
	@Override
	@ApiOperation("add bene bank")
	public AmxApiResponse<BoolRespModel, Object> addBeneBank(@RequestBody @Valid AddBeneBankRequest request) {
		logger.debug("request addBeneBank: {} ", JsonUtil.toJson(request));
		beneBranchValidation.validateaddBeneBank(request);
		beneBranchService.addBeneBankorCash(request);
		return AmxApiResponse.build(new BoolRespModel(true));
	}

	@RequestMapping(value = Path.ADD_BENE_CASH, method = RequestMethod.POST)
	@Override
	@ApiOperation("add bene cash")
	public AmxApiResponse<BoolRespModel, Object> addBenecash(@RequestBody @Valid AddBeneCashRequest request) {
		logger.debug("request addBenecash: {} ", JsonUtil.toJson(request));
		beneBranchValidation.validateaddBenecash(request);
		beneBranchService.addBeneBankorCash(request);
		return AmxApiResponse.build(new BoolRespModel(true));
	}

	@RequestMapping(value = Path.ADD_NEW_BRANCH_REQUEST, method = RequestMethod.POST)
	@Override
	@ApiOperation("add new branch req")
	public AmxApiResponse<BoolRespModel, Object> addNewBankBranchRequest(@RequestBody @Valid AddNewBankBranchRequest request) {
		logger.debug("request addNewBankBranchRequest: {} ", JsonUtil.toJson(request));
		beneBranchValidation.validateAddNewBankBranchRequest(request);
		beneBranchService.addNewBankBranchRequest(request);
		return AmxApiResponse.build(new BoolRespModel(true));
	}

	@RequestMapping(value = Path.GET_BENE_LIST_STATUSES, method = RequestMethod.GET)
	@Override
	@ApiOperation("get bene list statuss")
	public AmxApiResponse<BeneStatusDto, Object> getBeneListStatuses() {
		List<BeneStatusDto> list = beneBranchService.getBeneListStatuses();
		return AmxApiResponse.buildList(list);
	}

	@RequestMapping(value = Path.LIST_BENE, method = RequestMethod.POST)
	@Override
	@ApiOperation("get bene list statuss")
	public AmxApiResponse<BeneficiaryListDTO, Object> listBene(ListBeneRequest request) {
		List<BeneficiaryListDTO> list = beneBranchService.listBene(request);
		return AmxApiResponse.buildList(list);
	}

	@RequestMapping(value = Path.UPDATE_BENE_STATUS, method = RequestMethod.POST)
	@Override
	@ApiOperation("update bene status")
	public AmxApiResponse<BoolRespModel, Object> updateBeneStatus(UpdateBeneStatusRequest request) {
		logger.debug("updateBeneStatus request: {}", JsonUtil.toJson(request));
		beneBranchService.updateBeneStatus(request);
		return AmxApiResponse.build(new BoolRespModel(true));
	}

	@RequestMapping(value = Path.UPDATE_BENE_BANK, method = RequestMethod.POST)
	@Override
	@ApiOperation("add bene bank")
	public AmxApiResponse<BoolRespModel, Object> updateBeneBank(@RequestBody @Valid UpdateBeneBankRequest request) {
		logger.debug("request updateBeneBank: {} ", JsonUtil.toJson(request));
		beneBranchValidation.validateUpdateBeneBank(request);
		beneBranchService.updateBeneBankorCash(request);
		return AmxApiResponse.build(new BoolRespModel(true));
	}

	@RequestMapping(value = Path.UPDATE_BENE_CASH, method = RequestMethod.POST)
	@Override
	@ApiOperation("update bene bank")
	public AmxApiResponse<BoolRespModel, Object> updateBeneCash(@RequestBody @Valid UpdateBeneCashRequest request) {
		logger.debug("request updateBeneBank: {} ", JsonUtil.toJson(request));
		beneBranchValidation.validateUpdateBeneCash(request);
		beneBranchService.updateBeneBankorCash(request);
		return AmxApiResponse.build(new BoolRespModel(true));
	}
	
	@RequestMapping(value = Path.GET_BENE_BY_IDNO, method = RequestMethod.GET)
	@Override
	@ApiOperation("get bene by id")
	public AmxApiResponse<BeneficiaryListDTO, Object> getBeneByIdNo(@RequestParam(name = Params.ID_NO) Integer idNo) {
		List<BeneficiaryListDTO> list = beneBranchService.getBeneByIdNo(idNo);
		return AmxApiResponse.buildList(list);
	}
}
