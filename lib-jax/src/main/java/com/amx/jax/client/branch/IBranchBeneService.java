package com.amx.jax.client.branch;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.request.benebranch.AddBeneBankRequest;
import com.amx.jax.model.request.benebranch.AddBeneCashRequest;
import com.amx.jax.model.request.benebranch.AddNewBankBranchRequest;
import com.amx.jax.model.request.benebranch.ListBankBranchRequest;
import com.amx.jax.model.request.benebranch.ListBeneBankOrCashRequest;
import com.amx.jax.model.request.benebranch.ListBeneRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneBankRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneCashRequest;
import com.amx.jax.model.request.benebranch.UpdateBeneStatusRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.model.response.benebranch.BeneStatusDto;

public interface IBranchBeneService {

	public static class Path {
		public static final String PREFIX = "/branch/bene/";
		public static final String LIST_BANK_BY_COUNTRY_CURRENCY = PREFIX + "/list-bank-country-currency/";
		public static final String LIST_CASH_AGENTS_BY_COUNTRY_CURRENCY = PREFIX + "/list-cash-agent-country-currency/";
		public static final String LIST_BANK_BRANCH = PREFIX + "/list-bank-branch/";
		public static final String ADD_BENE_BANK = PREFIX + "/add-bene-bank/";
		public static final String ADD_BENE_CASH = PREFIX + "/add-bene-cash/";
		public static final String ADD_NEW_BRANCH_REQUEST = PREFIX + "/add-new-branch-request/";
		public static final String GET_BENE_LIST_STATUSES = PREFIX + "/get-bene-list-statuses/";
		public static final String LIST_BENE = PREFIX + "/list-bene/";
		public static final String UPDATE_BENE_STATUS = PREFIX + "/update-bene-status/";
		public static final String UPDATE_BENE_BANK = PREFIX + "/update-bene-bank/";
		public static final String UPDATE_BENE_CASH = PREFIX + "/update-bene-cash/";
	}

	public static class Params {
		public static final String TRNX_DATE = "transactiondate";
	}

	AmxApiResponse<BankMasterDTO, Object> listBeneBank(ListBeneBankOrCashRequest request);

	AmxApiResponse<RoutingBankMasterDTO, Object> listBeneCashAgents(ListBeneBankOrCashRequest request);

	AmxApiResponse<BankBranchDto, Object> listBankBranch(ListBankBranchRequest request);

	AmxApiResponse<BoolRespModel, Object> addBeneBank(AddBeneBankRequest request);

	AmxApiResponse<BoolRespModel, Object> addBenecash(AddBeneCashRequest request);

	AmxApiResponse<BoolRespModel, Object> addNewBankBranchRequest(AddNewBankBranchRequest request);

	AmxApiResponse<BeneStatusDto, Object> getBeneListStatuses();

	AmxApiResponse<BeneficiaryListDTO, Object> listBene(ListBeneRequest request);

	AmxApiResponse<BoolRespModel, Object> updateBeneStatus(UpdateBeneStatusRequest request);

	AmxApiResponse<BoolRespModel, Object> updateBeneBank(UpdateBeneBankRequest request);

	AmxApiResponse<BoolRespModel, Object> updateBeneCash(UpdateBeneCashRequest request);

}
