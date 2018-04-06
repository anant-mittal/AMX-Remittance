
package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.constant.BeneficiaryConstant.BeneStatus;
import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.BenePersonalDetailModel;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.response.JaxTransactionResponse;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.AuthDataInterface.AuthRequestOTP;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix;
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
			@RequestParam(required = false) BigDecimal beneRelSeqId, @RequestParam String remarks,
			@RequestParam BeneStatus status) {
		ResponseWrapper<Object> wrapper = new ResponseWrapper<Object>();
		// Disable Beneficiary
		wrapper.setData(jaxService.setDefaults().getBeneClient().updateStatus(beneficaryMasterSeqId, remarks, status)
				.getResult());
		return wrapper;
	}

	@ApiOperation(value = "Set Beneficiary As Favorite")
	@RequestMapping(value = "/api/user/bnfcry/fav", method = { RequestMethod.POST })
	public ResponseWrapper<BeneficiaryListDTO> beneFav(@RequestParam BigDecimal beneficaryMasterSeqId) {
		ResponseWrapper<BeneficiaryListDTO> wrapper = new ResponseWrapper<BeneficiaryListDTO>();
		wrapper.setData(jaxService.setDefaults().getBeneClient().beneFavoriteUpdate(beneficaryMasterSeqId).getResult());
		return wrapper;
	}

	@ApiOperation(value = "get List Of Favorite Beneficiary ")
	@RequestMapping(value = "/api/user/bnfcry/fav", method = { RequestMethod.GET })
	public ResponseWrapper<List<BeneficiaryListDTO>> beneFavGet() {
		return new ResponseWrapper<List<BeneficiaryListDTO>>(
				jaxService.setDefaults().getBeneClient().beneFavoriteList().getResults());
	}

	@ApiOperation(value = "Save Bene Account Info")
	@RequestMapping(value = "/api/user/bnfcry/account", method = { RequestMethod.POST })
	public ResponseWrapper<JaxTransactionResponse> saveBeneAccountInTrnx(
			@RequestBody BeneAccountModel beneAccountModel) {
		return new ResponseWrapper<JaxTransactionResponse>(
				jaxService.setDefaults().getBeneClient().saveBeneAccountInTrnx(beneAccountModel).getResult());
	}

	@ApiOperation(value = "Save Bene Personal Detail")
	@RequestMapping(value = "/api/user/bnfcry/personal", method = { RequestMethod.POST })
	public ResponseWrapper<JaxTransactionResponse> saveBenePersonalDetailInTrnx(
			@RequestBody BenePersonalDetailModel benePersonalDetailModel) {
		return new ResponseWrapper<JaxTransactionResponse>(jaxService.setDefaults().getBeneClient()
				.saveBenePersonalDetailInTrnx(benePersonalDetailModel).getResult());
	}

	@ApiOperation(value = "Sends OTP for Beneficiary Add")
	@RequestMapping(value = "/api/user/bnfcry/otp", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponseOTPprefix> sendOTP() {
		ResponseWrapper<AuthResponseOTPprefix> wrapper = new ResponseWrapper<AuthResponseOTPprefix>(new AuthData());
		CivilIdOtpModel model = jaxService.setDefaults().getBeneClient().sendOtp().getResult();
		wrapper.getData().setmOtpPrefix(model.getmOtpPrefix());
		wrapper.getData().seteOtpPrefix(model.geteOtpPrefix());
		return wrapper;
	}

	@ApiOperation(value = "Save the current beneficary in progress")
	@RequestMapping(value = "/api/user/bnfcry/commit", method = { RequestMethod.POST })
	public ResponseWrapper<BeneficiaryTrnxModel> commitAddBeneTrnx(@RequestBody AuthRequestOTP req) {
		return new ResponseWrapper<BeneficiaryTrnxModel>(
				jaxService.setDefaults().getBeneClient().commitAddBeneTrnx(req.getmOtp(), req.geteOtp()).getResult());
	}

}
