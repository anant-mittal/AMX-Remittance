
package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.JaxService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Beneficiary APIs")
public class BeneController {

	@Autowired
	private JaxService jaxService;

	@ApiOperation(value = "List of All bnfcry")
	@RequestMapping(value = "/api/user/bnfcry/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<BeneCountryDTO>> beneList() {
		ResponseWrapper<List<BeneCountryDTO>> wrapper = new ResponseWrapper<List<BeneCountryDTO>>();

		wrapper.setData(jaxService.setDefaults().getBeneClient().getBeneficiaryList(new BigDecimal(0)).getResults());

		return wrapper;
	}

	@ApiOperation(value = "Get Beneficiary Details")
	@RequestMapping(value = "/api/user/bnfcry/details", method = { RequestMethod.POST })
	public ResponseWrapper<BeneCountryDTO> beneDetails(BigDecimal beneficiaryId) {
		ResponseWrapper<BeneCountryDTO> wrapper = new ResponseWrapper<BeneCountryDTO>();

		// Disable Beneficiary
		jaxService.setDefaults().getBeneClient();

		return wrapper;
	}

	@ApiOperation(value = "Update Beneficiary Details")
	@RequestMapping(value = "/api/user/bnfcry/update", method = { RequestMethod.POST })
	public ResponseWrapper<BeneCountryDTO> beneDetails(@RequestBody BeneCountryDTO beneficiary) {
		ResponseWrapper<BeneCountryDTO> wrapper = new ResponseWrapper<BeneCountryDTO>();

		// Disable Beneficiary

		return wrapper;
	}

	@ApiOperation(value = "Disable Beneficiary")
	@RequestMapping(value = "/api/user/bnfcry/disable", method = { RequestMethod.POST })
	public ResponseWrapper<Object> beneDisable(BigDecimal beneficiaryId) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();

		// Disable Beneficiary
		jaxService.setDefaults().getBeneClient();

		return wrapper;
	}

}
