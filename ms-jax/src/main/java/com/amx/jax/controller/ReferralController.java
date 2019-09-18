package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.REFER_API_ENDPOINT;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.LinkDTO;
import com.amx.amxlib.model.LinkResponseModel;
import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.ReferralDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.LinkService;
import com.amx.jax.services.ReferralService;

@RestController
@RequestMapping(REFER_API_ENDPOINT)
public class ReferralController {

	@Autowired
	MetaData metaData;

	@Autowired
	ReferralService referralService;

	@Autowired
	LinkService linkService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public AmxApiResponse<ReferralDTO, Object> handleUrlSave() {
		ReferralDTO dto = new ReferralDTO();
		BigDecimal customerId = metaData.getCustomerId();
		dto.setCustomerID(customerId);
		referralService.validateReferralDto(dto);
		return referralService.saveReferral(dto);
	}

	@RequestMapping(value = "/link/make", method = RequestMethod.POST)
	public AmxApiResponse<LinkResponseModel, Object> handleMakeLink(@RequestBody @Valid LinkDTO linkDto) {
		BigDecimal customerId = metaData.getCustomerId();
		linkDto.setCustomerId(customerId);
		linkService.validateLinkDto(linkDto);
		referralService.validateContactDetails(linkDto);
		return linkService.makeLink(linkDto);
	}

	@RequestMapping(value = "/link/open", method = RequestMethod.POST)
	public BoolRespModel handleOpenLink(@RequestBody @Valid LinkDTO linkDto) {				
		referralService.validateLinkDetails(linkDto);
		return linkService.openLink(linkDto);
	}
}
