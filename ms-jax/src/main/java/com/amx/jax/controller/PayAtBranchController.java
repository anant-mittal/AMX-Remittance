package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.PAYATBRANCH_ENDPOINT;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.constant.ApiEndpoint.PayAtBranch;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.response.payatbranch.PayAtBranchTrnxListDTO;
import com.amx.jax.services.PayAtBranchService;

@RestController
@RequestMapping(PAYATBRANCH_ENDPOINT)
@SuppressWarnings("rawtypes")
public class PayAtBranchController {
	@Autowired
	PayAtBranchService payAtBranchService;

	@RequestMapping(value = PayAtBranch.PAYMENT_MODES, method = RequestMethod.POST)
	public AmxApiResponse<ResourceDTO, Object> getPaymentModes() {
		List<ResourceDTO> resourceDTO = payAtBranchService.getPaymentModes();
		return AmxApiResponse.buildList(resourceDTO);
	}

	@RequestMapping(value = PayAtBranch.PB_TRNX_LIST, method = RequestMethod.POST)
	public AmxApiResponse<PayAtBranchTrnxListDTO, Object> getPbTrnxList() {
		List<PayAtBranchTrnxListDTO> bankList = payAtBranchService.getPbTrnxList();
		return AmxApiResponse.buildList(bankList);
	}

	@RequestMapping(value = PayAtBranch.PB_TRNX_LIST_BRANCH, method = RequestMethod.POST)
	public AmxApiResponse<PayAtBranchTrnxListDTO, Object> getPbTrnxListBranch() {
		List<PayAtBranchTrnxListDTO> bankList = payAtBranchService.getPbTrnxListBranch();
		return AmxApiResponse.buildList(bankList);
	}

}
