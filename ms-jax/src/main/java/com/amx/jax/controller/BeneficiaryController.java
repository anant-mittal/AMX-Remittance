package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.ACCOUNT_TYPE_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.BENE_API_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.GET_AGENT_BRANCH_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.GET_AGENT_MASTER_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.GET_SERVICE_PROVIDER_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.SEND_OTP_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.UPDAE_STATUS_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.VALIDATE_OTP_ENDPOINT;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.amx.amxlib.constant.BeneficiaryConstant.BeneStatus;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.BenePersonalDetailModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.RoutingBankMasterParam;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dict.ContactType;
import com.amx.jax.meta.MetaData;
import com.amx.jax.service.AccountTypeService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.trnx.BeneficiaryTrnxManager;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.ConverterUtil;

/**
 * 
 * @author :Rabil Purpose :Beneficiary related function
 *
 */
@RestController
@RequestMapping(BENE_API_ENDPOINT)
@SuppressWarnings("rawtypes")
public class BeneficiaryController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BeneficiaryController.class);
	private static final String RELATIONSHIP_ID = "Relationship Id :";
	private static final String CUSTOMER_ID = "Customer Id :";

	@Autowired
	BeneficiaryService beneService;

	@Autowired
	AccountTypeService accountTypeService;

	@Autowired
	private ConverterUtil converterUtil;

	@Autowired
	MetaData metaData;

	@Autowired
	BeneficiaryTrnxManager beneficiaryTrnxManager;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/beneList/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryListResponse(@RequestParam("beneCountryId") BigDecimal beneCountryId) {
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		JaxChannel channel = metaData.getChannel();
		/*
		 * LOGGER.debug("userType :" + channel.name() + "\t customerId :" + customerId +
		 * "\t applicationCountryId :" + applicationCountryId + "\t beneCountryId :" +
		 * beneCountryId);
		 */

		if (channel != null && channel.equals(JaxChannel.BRANCH)) {
			return beneService.getBeneficiaryListForBranch(customerId, applicationCountryId, beneCountryId);
		} else {
			return beneService.getBeneficiaryListForOnline(customerId, applicationCountryId, beneCountryId);
		}
	}

	@RequestMapping(value = "/benecountry/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryCountryListResponse() {
		BigDecimal customerId = metaData.getCustomerId();
		JaxChannel channel = metaData.getChannel();

		LOGGER.debug("userType :" + channel + "\t customerId :" + customerId);

		if (channel != null && channel.equals(JaxChannel.BRANCH)) {
			return beneService.getBeneficiaryCountryListForBranch(customerId);
		} else {
			return beneService.getBeneficiaryCountryListForOnline(customerId);
		}
	}

	/**
	 * to get default beneficiary bene id and tran id is optional.
	 * 
	 * @param beneocountryList
	 * @return From Transaction list need to pass transaction history : idno And
	 *         From beneficiary list need to pass Beneficiary :idNo
	 */

	@RequestMapping(value = "/defaultbene/", method = RequestMethod.POST)
	public ApiResponse defaultBeneficiary(
			@RequestParam(required = false, value = "beneRelationId") BigDecimal beneRelationId,
			@RequestParam(required = false, value = "transactionId") BigDecimal transactionId) {
		LOGGER.debug("Bene disable method Trnx Report:");
		ApiResponse response = null;
		try {
			BigDecimal customerId = metaData.getCustomerId();
			BigDecimal applicationCountryId = metaData.getCountryId();
			LOGGER.debug(RELATIONSHIP_ID + beneRelationId);
			LOGGER.debug(CUSTOMER_ID + customerId);
			LOGGER.debug("applicationCountryId  :" + applicationCountryId);
			response = beneService.getDefaultBeneficiary(customerId, applicationCountryId, beneRelationId,
					transactionId);
		} catch (Exception e) {
			LOGGER.error("exception in defaultBeneficiary : ", e);
		}
		return response;
	}

	/**
	 * Author : FavouriteBeneficiaryList
	 * 
	 * @return : Customer Id,application country Id
	 */

	@RequestMapping(value = "/favouritebenelist/", method = RequestMethod.GET)
	public ApiResponse favouriteBeneficiary() {
		LOGGER.info("getFavouriteBeneficiaryList controller :");
		ApiResponse response = null;

		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		LOGGER.info("favouritebene customerId Id :" + customerId);
		LOGGER.info("favouritebene applicationCountryId  :" + applicationCountryId);

		response = beneService.getFavouriteBeneficiaryList(customerId, applicationCountryId);

		return response;
	}

	@RequestMapping(value = "/disable/", method = RequestMethod.POST)
	public ApiResponse beneDisable(@RequestParam("beneMasSeqId") BigDecimal beneMasterSeqId,
			@RequestParam("remarks") String remarks) {
		BigDecimal customerId = metaData.getCustomerId();
		BeneficiaryListDTO beneDetails = new BeneficiaryListDTO();
		beneDetails.setCustomerId(customerId);
		beneDetails.setBeneficaryMasterSeqId(beneMasterSeqId);
		beneDetails.setRemarks(remarks);
		return beneService.disableBeneficiary(beneDetails);
	}

	@RequestMapping(value = "/favoritebeneupdate/", method = RequestMethod.POST)
	public ApiResponse favoriteBeneUpdate(@RequestParam("beneMasSeqId") BigDecimal beneMasterSeqId) {
		LOGGER.debug("Bene disable method Trnx Report:");
		BigDecimal customerId = metaData.getCustomerId();
		BeneficiaryListDTO beneDetails = new BeneficiaryListDTO();
		beneDetails.setCustomerId(customerId);
		beneDetails.setBeneficaryMasterSeqId(beneMasterSeqId);
		LOGGER.debug(CUSTOMER_ID + beneDetails.getCustomerId());
		LOGGER.debug(RELATIONSHIP_ID + beneDetails.getBeneficiaryRelationShipSeqId());
		LOGGER.debug("Bene Master Id  :" + beneDetails.getBeneficaryMasterSeqId());
		LOGGER.debug("Bene Acccount Id :" + beneDetails.getBeneficiaryAccountSeqId());

		return beneService.updateFavoriteBeneficiary(beneDetails);
	}

	@RequestMapping(value = "/beneupdate/", method = RequestMethod.POST)
	public ApiResponse getBeneUpdate(@RequestBody String jsonBeneDTO) {
		LOGGER.debug("getRemittanceDetailForReport Trnx Report:");
		BeneficiaryListDTO beneficiaryDto = (BeneficiaryListDTO) converterUtil.unmarshall(jsonBeneDTO,
				BeneficiaryListDTO.class);
		LOGGER.debug("Bene Master Id :" + beneficiaryDto.getBeneficaryMasterSeqId());
		LOGGER.debug("Bene Rela Seq Id :" + beneficiaryDto.getBeneficiaryRelationShipSeqId());
		LOGGER.debug("Bene Account Length :" + beneficiaryDto.getBeneficiaryAccountSeqId());
		LOGGER.debug(
				CUSTOMER_ID + beneficiaryDto.getCustomerId() + "\t Reference :" + beneficiaryDto.getServiceGroupId());
		LOGGER.debug("Country Id :" + beneficiaryDto.getApplicationCountryId() + "\t Currency Id :"
				+ beneficiaryDto.getCurrencyId());

		return beneService.beneficiaryUpdate(beneficiaryDto);
	}

	@RequestMapping(value = ACCOUNT_TYPE_ENDPOINT, method = RequestMethod.GET)
	public ApiResponse getBeneficiaryAccountType(@RequestParam("countryId") BigDecimal countryId) {
		LOGGER.debug("getBeneficiaryAccountType countryId :" + countryId);

		return accountTypeService.getAccountTypeFromView(countryId);

	}

	/**
	 * @param beneAccountModel
	 * @return apirepsone
	 * 
	 */
	@RequestMapping(value = "/trnx/bene/bene-account/", method = RequestMethod.POST)
	public ApiResponse saveBeneAccountInTrnx(@RequestBody @Valid BeneAccountModel beneAccountModel) {
		LOGGER.debug("saveBeneAccountInTrnx request: " + beneAccountModel.toString());
		return beneficiaryTrnxManager.saveBeneAccountTrnx(beneAccountModel);
	}

	/**
	 * @param benePersonalDetailModel
	 * @return apiresponse
	 * 
	 */
	@RequestMapping(value = "/trnx/bene/bene-details/", method = RequestMethod.POST)
	public ApiResponse saveBenePersonalDetailInTrnx(
			@RequestBody @Valid BenePersonalDetailModel benePersonalDetailModel) {
		LOGGER.debug("saveBenePersonalDetailInTrnx request: " + benePersonalDetailModel.toString());
		return beneficiaryTrnxManager.savePersonalDetailTrnx(benePersonalDetailModel);
	}

	/**
	 * @param mOtp
	 * @param eOtp
	 * @return apiresponse
	 * 
	 */
	@RequestMapping(value = "/trnx/addbene/commit/", method = RequestMethod.POST)
	public ApiResponse commitAddBeneTrnx(@RequestParam("mOtp") String mOtp,
			@RequestParam(name = "eOtp") String eOtp) {
		LOGGER.debug("in commit bene request with param , eOtp: " + eOtp + " motp: " + mOtp);
		return beneficiaryTrnxManager.commitTransaction(mOtp, eOtp);
	}

	@RequestMapping(value = "/relations/", method = RequestMethod.GET)
	public ApiResponse getAllRelations() {
		LOGGER.debug("In getAllRelations controller");
		return beneService.getBeneRelations();
	}

	/**
	 * send otp for add bene
	 * 
	 * @return apiresponse
	 * 
	 */
	@RequestMapping(value = SEND_OTP_ENDPOINT, method = RequestMethod.GET)
	public ApiResponse sendOtp() {
		List<ContactType> channel = new ArrayList<>();
		channel.add(ContactType.EMAIL);
		channel.add(ContactType.SMS);
		ApiResponse response = beneService.sendOtp(channel);
		return response;
	}

	/**
	 * validates otp for add bene
	 * 
	 * @param mOtp
	 * @param eOtp
	 * @return
	 * 
	 */
	@RequestMapping(value = VALIDATE_OTP_ENDPOINT, method = RequestMethod.GET)
	public ApiResponse validateOtp(@RequestParam("mOtp") String mOtp,
			@RequestParam(name = "eOtp", required = false) String eOtp) {
		ApiResponse response = userService.validateOtp(null, mOtp, eOtp);
		return response;
	}

	@RequestMapping(value = UPDAE_STATUS_ENDPOINT, method = RequestMethod.POST)
	public ApiResponse updateStatus(@RequestParam("beneMasSeqId") BigDecimal beneMasterSeqId,
			@RequestParam("remarks") String remarks,
			@RequestParam("status") BeneStatus status,
			@RequestParam("mOtp") String mOtp,
			@RequestParam("eOtp") String eOtp) {
		BigDecimal customerId = metaData.getCustomerId();
		BeneficiaryListDTO beneDetails = new BeneficiaryListDTO();
		beneDetails.setCustomerId(customerId);
		beneDetails.setBeneficaryMasterSeqId(beneMasterSeqId);
		beneDetails.setRemarks(remarks);
		ApiResponse resp = beneService.updateStatus(beneDetails, status, mOtp, eOtp);
		LOGGER.debug("######## Values #######################   -- " + resp.getResult());
		return resp;
	}

	@RequestMapping(value = GET_SERVICE_PROVIDER_ENDPOINT, method = RequestMethod.GET)
	public ApiResponse getServiceProviderListResponse(@RequestParam("beneCountryId") BigDecimal beneCountryId,
			@RequestParam("serviceGroupId") BigDecimal serviceGroupId) {
		BigDecimal applicationCountryId = metaData.getCountryId();
		return beneService.getServiceProviderList(new RoutingBankMasterParam.RoutingBankMasterServiceImpl(
				applicationCountryId, beneCountryId, serviceGroupId));
	}

	@RequestMapping(value = GET_AGENT_MASTER_ENDPOINT, method = RequestMethod.GET)
	public ApiResponse getAgentMasterListResponse(@RequestParam("beneCountryId") BigDecimal beneCountryId,
			@RequestParam("routingBankId") BigDecimal routingBankId,
			@RequestParam("currencyId") BigDecimal currencyId,
			@RequestParam("serviceGroupId") BigDecimal serviceGroupId) {
		BigDecimal applicationCountryId = metaData.getCountryId();
		return beneService.getAgentMasterList(new RoutingBankMasterParam.RoutingBankMasterServiceImpl(
				applicationCountryId, beneCountryId, serviceGroupId, routingBankId, currencyId));
	}

	@RequestMapping(value = GET_AGENT_BRANCH_ENDPOINT, method = RequestMethod.GET)
	public ApiResponse getAgentBranchListResponse(@RequestParam("beneCountryId") BigDecimal beneCountryId,
			@RequestParam("routingBankId") BigDecimal routingBankId,
			@RequestParam("currencyId") BigDecimal currencyId,
			@RequestParam("agentBankId") BigDecimal agentBankId,
			@RequestParam("serviceGroupId") BigDecimal serviceGroupId) {

		BigDecimal applicationCountryId = metaData.getCountryId();
		return beneService.getAgentLocationList(new RoutingBankMasterParam.RoutingBankMasterServiceImpl(
				applicationCountryId, beneCountryId, serviceGroupId, routingBankId, currencyId, agentBankId));
	}

	// Added by chetan 03-05-2018 for country with channeling
	@RequestMapping(value = "/bene/country/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryCountryListWithChannelingResponse() {
		BigDecimal customerId = metaData.getCustomerId();
		JaxChannel channel = metaData.getChannel();
		LOGGER.debug("userType :" + channel + "\t customerId :" + customerId);
		return beneService.getBeneficiaryCountryListWithChannelingForOnline(customerId);

	}

	@RequestMapping(value = "/pobene/", method = RequestMethod.GET)
	public ApiResponse poBeneficiary(@RequestParam(required = false, value = "placeOrderId") BigDecimal placeOrderId) {
		ApiResponse response = null;

		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();

		LOGGER.debug("Place Order Id : " + placeOrderId);
		LOGGER.debug(CUSTOMER_ID + customerId);
		LOGGER.debug("applicationCountryId  :" + applicationCountryId);

		response = beneService.getPlaceOrderBeneficiary(customerId, applicationCountryId, placeOrderId);

		return response;
	}

}
