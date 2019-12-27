package com.amx.jax.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.constant.ApiEndpoint.MetaApi;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.bene.InstitutionCategoryDto;
import com.amx.jax.client.bene.ReasoncodeCategory;
import com.amx.jax.client.meta.IMetaControllerExtn;
import com.amx.jax.model.meta.ReasonsDTO;
import com.amx.jax.service.MetaExtnService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(MetaApi.PREFIX)
public class MetaControllerExtn implements IMetaControllerExtn {

	@Autowired
	MetaExtnService metaExtnService;

	private static final Logger log = LoggerFactory.getLogger(MetaControllerExtn.class);

	@RequestMapping(value = Path.LIST_INSTITUTION_CATEGORY_MASTER, method = RequestMethod.GET)
	@Override
	@ApiOperation("List institution cat master")
	public AmxApiResponse<InstitutionCategoryDto, Object> listInstitutionCategoryMaster() {
		List<InstitutionCategoryDto> output = metaExtnService.listInstitutionCategoryMaster();
		return AmxApiResponse.buildList(output);
	}

	@RequestMapping(value = Path.API_REASON_CODE, method = RequestMethod.POST)
	@ApiOperation("List reason codes")
	@Override
	public AmxApiResponse<ReasonsDTO, Object> getReason(@RequestParam(name = Params.REASON_CATEGORY) ReasoncodeCategory reasonCategory) {
		return metaExtnService.getReasonList(reasonCategory);
	}
}
