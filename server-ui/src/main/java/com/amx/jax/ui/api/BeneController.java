
package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
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
	public ResponseWrapper<List<BeneficiaryListDTO>> beneList() {
		ResponseWrapper<List<BeneficiaryListDTO>> wrapper = new ResponseWrapper<List<BeneficiaryListDTO>>();

		wrapper.setData(jaxService.setDefaults().getBeneClient().getBeneficiaryList(new BigDecimal(0)).getResults());

		return wrapper;
	}

	@ApiOperation(value = "Get Beneficiary Details")
	@RequestMapping(value = "/api/user/bnfcry/details", method = { RequestMethod.POST })
	public ResponseWrapper<BeneCountryDTO> beneDetails(BigDecimal beneficiaryId) {
		ResponseWrapper<BeneCountryDTO> wrapper = new ResponseWrapper<BeneCountryDTO>();

		// Disable Beneficiary
		// jaxService.setDefaults().getBeneClient().beneDisable(beneRelSeqId, remarks);

		return wrapper;
	}

	@ApiOperation(value = "Update Beneficiary Details")
	@RequestMapping(value = "/api/user/bnfcry/update", method = { RequestMethod.POST })
	public ResponseWrapper<BeneCountryDTO> beneDetails(@RequestBody BeneCountryDTO beneficiary) {
		ResponseWrapper<BeneCountryDTO> wrapper = new ResponseWrapper<BeneCountryDTO>();

		// wrapper.setData(jaxService.setDefaults().getBeneClient().);

		return wrapper;
	}

	@ApiOperation(value = "Disable Beneficiary")
	@RequestMapping(value = "/api/user/bnfcry/disable", method = { RequestMethod.POST })
	public ResponseWrapper<Object> beneDisable(@RequestParam BigDecimal beneficaryMasterSeqId,
			@RequestParam(required = false) BigDecimal beneRelSeqId, @RequestParam String remarks) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();
		// Disable Beneficiary
		wrapper.setData(
				jaxService.setDefaults().getBeneClient().beneDisable(beneficaryMasterSeqId, remarks).getResult());
		return wrapper;
	}

	@ApiOperation(value = "Set Beneficiary As Favorite")
	@RequestMapping(value = "/api/user/bnfcry/fav", method = { RequestMethod.POST })
	public ResponseWrapper<BeneficiaryListDTO> beneFav(@RequestParam BigDecimal beneficaryMasterSeqId) {
		ResponseWrapper<BeneficiaryListDTO> wrapper = new ResponseWrapper<BeneficiaryListDTO>();
		wrapper.setData(jaxService.setDefaults().getBeneClient().beneFav(beneficaryMasterSeqId).getResult());
		return wrapper;
	}

	@ApiOperation(value = "get List Of Favorite Beneficiary ")
	@RequestMapping(value = "/api/user/bnfcry/fav", method = { RequestMethod.GET })
	public ResponseWrapper<BeneficiaryListDTO> beneFavGet() {
		ResponseWrapper<BeneficiaryListDTO> wrapper = new ResponseWrapper<BeneficiaryListDTO>();
		wrapper.setData(jaxService.setDefaults().getBeneClient().beneFav().getResult());
		return wrapper;
	}

}
