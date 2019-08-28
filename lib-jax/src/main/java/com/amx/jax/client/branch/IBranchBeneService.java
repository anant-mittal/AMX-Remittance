package com.amx.jax.client.branch;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.model.request.benebranch.ListBankBranchRequest;
import com.amx.jax.model.request.benebranch.ListBeneBankOrCashRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.benebranch.BankBranchDto;

public interface IBranchBeneService {

	public static class Path {
		public static final String PREFIX = "/branch/bene/";
		public static final String LIST_BANK_BY_COUNTRY_CURRENCY = PREFIX + "/list-bank-country-currency/";
		public static final String LIST_CASH_AGENTS_BY_COUNTRY_CURRENCY = PREFIX + "/list-cash-agent-country-currency/";
		public static final String LIST_BANK_BRANCH = PREFIX + "/list-bank-branch/";
	}

	public static class Params {
		public static final String TRNX_DATE = "transactiondate";
	}

	AmxApiResponse<BankMasterDTO, Object> listBeneBank(ListBeneBankOrCashRequest request);

	AmxApiResponse<RoutingBankMasterDTO, Object> listBeneCashAgents(ListBeneBankOrCashRequest request);

	AmxApiResponse<BankBranchDto, Object> listBankBranch(ListBankBranchRequest request);

}
