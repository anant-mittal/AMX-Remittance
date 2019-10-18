
package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.response.JaxTransactionResponse;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.bene.BeneBranchClient;
import com.amx.jax.client.bene.BeneficaryStatusDto;
import com.amx.jax.client.bene.BeneficiaryConstant.BeneStatus;
import com.amx.jax.client.bene.IBeneficiaryService;
import com.amx.jax.client.branch.IBranchBeneService;
import com.amx.jax.client.remittance.RemittanceClient;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.request.benebranch.AddNewBankBranchRequest;
import com.amx.jax.model.request.benebranch.BankBranchListRequest;
import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.model.request.benebranch.BenePersonalDetailModel;
import com.amx.jax.model.request.benebranch.BeneficiaryTrnxModel;
import com.amx.jax.model.request.benebranch.ListBeneBankOrCashRequest;
import com.amx.jax.model.request.remittance.GetServiceApplicabilityRequest;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.model.response.benebranch.BeneStatusDto;
import com.amx.jax.model.response.remittance.GetServiceApplicabilityResponse;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.AuthDataInterface.AuthRequestOTP;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.ResponseWrapperM;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.session.Transactions;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The Class BeneController.
 */
@RestController
@Api(value = "Beneficiary APIs")
@ApiStatusService({ IBranchBeneService.class, IBeneficiaryService.class })
public class BeneController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BeneController.class);
	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	/** The transactions. */
	@Autowired
	private Transactions transactions;

	@Autowired
	BeneClient beneClient;

	/**
	 * Bene list.
	 *
	 * @return the response wrapper
	 */
	@ApiOperation(value = "List of All bnfcry")
	@RequestMapping(value = "/api/user/bnfcry/list", method = { RequestMethod.POST })
	public ResponseWrapper<List<BeneficiaryListDTO>> beneList(
			@RequestParam(required = false, defaultValue = "false") boolean excludePackages) {
		ResponseWrapper<List<BeneficiaryListDTO>> wrapper = new ResponseWrapper<>();
		wrapper.setData(jaxService.setDefaults().getBeneClient().getBeneficiaryList(new BigDecimal(0), excludePackages)
				.getResults());
		return wrapper;
	}

	/**
	 * Bene details.
	 *
	 * @param beneficiaryId the beneficiary id
	 * @return the response wrapper
	 */
	@ApiOperation(value = "Get Beneficiary Details")
	@RequestMapping(value = "/api/user/bnfcry/details", method = { RequestMethod.POST })
	public ResponseWrapper<BeneCountryDTO> beneDetails(BigDecimal beneficiaryId) {
		return new ResponseWrapper<>();
	}

	/**
	 * Bene details.
	 *
	 * @param beneficiary the beneficiary
	 * @return the response wrapper
	 * @deprecated - not used any more
	 */
	@Deprecated
	@ApiOperation(value = "Update Beneficiary Details")
	@RequestMapping(value = "/api/user/bnfcry/update", method = { RequestMethod.POST })
	public ResponseWrapper<BeneCountryDTO> beneDetails(@RequestBody BeneCountryDTO beneficiary) {
		return new ResponseWrapper<>();
	}

	/**
	 * Bene disable.
	 *
	 * @param mOtpHeader            the m otp header
	 * @param eOtpHeader            the e otp header
	 * @param mOtp                  the m otp
	 * @param eOtp                  the e otp
	 * @param beneficaryMasterSeqId the beneficary master seq id
	 * @param remarks               the remarks
	 * @param status                the status
	 * @return the response wrapper M
	 */
	@ApiOperation(value = "Disable Beneficiary")
	@RequestMapping(value = "/api/user/bnfcry/disable", method = { RequestMethod.POST })
	public ResponseWrapperM<Object, AuthResponseOTPprefix> beneDisable(
			@RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@RequestHeader(value = "eOtp", required = false) String eOtpHeader,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp,
			@RequestParam BigDecimal beneficaryMasterSeqId, @RequestParam(required = false) String remarks,
			@RequestParam BeneStatus status) {
		ResponseWrapperM<Object, AuthResponseOTPprefix> wrapper = new ResponseWrapperM<>();
		// Disable Beneficiary
		mOtp = JaxAuthContext.mOtp(ArgUtil.ifNotEmpty(mOtp, mOtpHeader));
		eOtp = JaxAuthContext.eOtp(ArgUtil.ifNotEmpty(eOtp, eOtpHeader));

		if (mOtp == null && eOtp == null) {
			wrapper.setMeta(new AuthData());
			CivilIdOtpModel model = jaxService.setDefaults().getBeneClient().sendOtp().getResult();
			wrapper.getMeta().setmOtpPrefix(model.getmOtpPrefix());
			wrapper.getMeta().seteOtpPrefix(model.geteOtpPrefix());
			wrapper.setStatusEnum(OWAStatusStatusCodes.DOTP_REQUIRED);
		} else {
			wrapper.setData(jaxService.setDefaults().getBeneClient()
					.updateStatus(beneficaryMasterSeqId, remarks, status, mOtp, eOtp).getResult());
			wrapper.setStatusEnum(OWAStatusStatusCodes.VERIFY_SUCCESS);
		}

		return wrapper;
	}

	/**
	 * Bene fav.
	 *
	 * @param beneficaryMasterSeqId the beneficary master seq id
	 * @return the response wrapper
	 */
	@ApiOperation(value = "Set Beneficiary As Favorite")
	@RequestMapping(value = "/api/user/bnfcry/fav", method = { RequestMethod.POST })
	public ResponseWrapper<BeneficiaryListDTO> beneFav(@RequestParam BigDecimal beneficaryMasterSeqId) {
		ResponseWrapper<BeneficiaryListDTO> wrapper = new ResponseWrapper<>();
		wrapper.setData(jaxService.setDefaults().getBeneClient().beneFavoriteUpdate(beneficaryMasterSeqId).getResult());
		return wrapper;
	}

	/**
	 * Bene fav get.
	 *
	 * @return the response wrapper
	 */
	@ApiOperation(value = "get List Of Favorite Beneficiary ")
	@RequestMapping(value = "/api/user/bnfcry/fav", method = { RequestMethod.GET })
	public ResponseWrapper<List<BeneficiaryListDTO>> beneFavGet() {
		return new ResponseWrapper<>(jaxService.setDefaults().getBeneClient().beneFavoriteList().getResults());
	}

	/**
	 * Save bene account in trnx.
	 *
	 * @param beneAccountModel the bene account model
	 * @return the response wrapper
	 */
	@ApiOperation(value = "Save Bene Account Info")
	@RequestMapping(value = "/api/user/bnfcry/account", method = { RequestMethod.POST })
	public ResponseWrapper<JaxTransactionResponse> saveBeneAccountInTrnx(
			@RequestBody BeneAccountModel beneAccountModel) {
		return transactions.start(new ResponseWrapper<JaxTransactionResponse>(
				jaxService.setDefaults().getBeneClient().saveBeneAccountInTrnx(beneAccountModel).getResult()));
	}

	/**
	 * Save bene personal detail in trnx.
	 *
	 * @param benePersonalDetailModel the bene personal detail model
	 * @return the response wrapper
	 */
	@ApiOperation(value = "Save Bene Personal Detail")
	@RequestMapping(value = "/api/user/bnfcry/personal", method = { RequestMethod.POST })
	public ResponseWrapper<JaxTransactionResponse> saveBenePersonalDetailInTrnx(
			@RequestBody BenePersonalDetailModel benePersonalDetailModel) {
		LOGGER.info("saveBenePersonalDetailInTrnx request: " + JsonUtil.toJson(benePersonalDetailModel));
		transactions.track();
		return new ResponseWrapper<>(jaxService.setDefaults().getBeneClient()
				.saveBenePersonalDetailInTrnx(benePersonalDetailModel).getResult());
	}

	/**
	 * Send OTP.
	 *
	 * @return the response wrapper
	 */
	@ApiOperation(value = "Sends OTP for Beneficiary Add")
	@RequestMapping(value = "/api/user/bnfcry/otp", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponseOTPprefix> sendOTP() {
		transactions.track();
		ResponseWrapper<AuthResponseOTPprefix> wrapper = new ResponseWrapper<>(new AuthData());
		CivilIdOtpModel model = jaxService.setDefaults().getBeneClient().sendOtp().getResult();
		wrapper.getData().setmOtpPrefix(model.getmOtpPrefix());
		wrapper.getData().seteOtpPrefix(model.geteOtpPrefix());
		return wrapper;
	}

	/**
	 * Commit add bene trnx.
	 *
	 * @param req the req
	 * @return the response wrapper
	 */
	@ApiOperation(value = "Save the current beneficary in progress")
	@RequestMapping(value = "/api/user/bnfcry/commit", method = { RequestMethod.POST })
	public ResponseWrapper<BeneficiaryTrnxModel> commitAddBeneTrnx(@RequestBody AuthRequestOTP req) {
		transactions.track();
		return new ResponseWrapper<>(
				jaxService.setDefaults().getBeneClient().commitAddBeneTrnx(req.getmOtp(), req.geteOtp()).getResult());
	}

	@Autowired
	BeneBranchClient beneBranchClient;

	@ApiOperation(value = "Get Lists Of Statuses for bene Filtering")
	@RequestMapping(value = "/api/bnfcry/meta/status", method = { RequestMethod.GET })
	public ResponseWrapperM<List<BeneStatusDto>, Object> getBeneListStatuses() {
		return ResponseWrapperM.fromAsList(beneBranchClient.getBeneListStatuses());
	}

	@ApiOperation(value = "Get Lists Of Types for bene")
	@RequestMapping(value = "/api/bnfcry/meta/types", method = { RequestMethod.GET })
	public ResponseWrapperM<List<BeneficaryStatusDto>, Object> getBeneStatusMaster() {
		return ResponseWrapperM.fromAsList(beneClient.getBeneStatusMaster());
	}

	@ApiOperation(value = "Add new branch request")
	@RequestMapping(value = "/api/bnfcry/meta/bank-branch/add", method = { RequestMethod.POST })
	public ResponseWrapperM<List<BoolRespModel>, Object> addNewBankBranchRequest(
			@RequestBody AddNewBankBranchRequest request) {
		return ResponseWrapperM.fromAsList(beneBranchClient.addNewBankBranchRequest(request));
	}

	@ApiOperation(value = "List of banks for currency and country")
	@RequestMapping(value = "/api/bnfcry/meta/bank/list", method = { RequestMethod.POST })
	public ResponseWrapperM<List<BankMasterDTO>, Object> addNewBankBranchRequest(
			@RequestBody ListBeneBankOrCashRequest request2) {
		return ResponseWrapperM.fromAsList(beneBranchClient.listBeneBank(request2));
	}

	@ApiOperation(value = "List of bank-branches")
	@RequestMapping(value = "/api/bnfcry/meta/bank-branch/list", method = { RequestMethod.POST })
	public ResponseWrapperM<List<BankBranchDto>, Object> addNewBankBranchRequest(
			@RequestBody BankBranchListRequest request) {
		return ResponseWrapperM.fromAsList(beneBranchClient.listBankBranch(request));
	}

	@Autowired
	RemittanceClient remittanceClient;

	@ApiOperation(value = "List of get-service-applicability")
	@RequestMapping(value = "/api/bnfcry/meta/fields/list", method = { RequestMethod.POST })
	public ResponseWrapperM<List<GetServiceApplicabilityResponse>, Object> getServiceApplicability(
			@RequestBody GetServiceApplicabilityRequest request) {
		return ResponseWrapperM.fromAsList(remittanceClient.getServiceApplicability(request));
	}
}
