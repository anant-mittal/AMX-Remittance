package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.BENE_API_ENDPOINT;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.constant.JaxChannel;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.service.AccountTypeService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.util.ConverterUtil;

/**
 * 
 * @author   :Rabil
 * Purpose   :Beneficiary related function  
 *
 */
@RestController
@RequestMapping(BENE_API_ENDPOINT)
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
	
	@RequestMapping(value = "/beneList/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryListResponse(@RequestParam("beneCountryId") BigDecimal beneCountryId) {
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		JaxChannel channel = metaData.getChannel();
		LOGGER.info("userType :"+channel.name()+"\t customerId :"+customerId+"\t applicationCountryId :"+applicationCountryId+"\t beneCountryId :"+beneCountryId);

		if(channel!=null && channel.equals(JaxChannel.BRANCH)) {
			return beneService.getBeneficiaryListForBranch(customerId, applicationCountryId,beneCountryId);
		}else {
			return beneService.getBeneficiaryListForOnline(customerId, applicationCountryId,beneCountryId);
		}
	}
	
	@RequestMapping(value = "/benecountry/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryCountryListResponse() {
		BigDecimal customerId = metaData.getCustomerId();
		JaxChannel channel = metaData.getChannel();
		
		LOGGER.info("userType :"+channel+"\t customerId :"+customerId);

		if(channel!=null && channel.equals(JaxChannel.BRANCH)) {
			return beneService.getBeneficiaryCountryListForBranch(customerId);
		}else {
			return beneService.getBeneficiaryCountryListForOnline(customerId);
		}
	}
	
	/**
	 * to get default beneficiary bene id and tran id is optional.
	 * @param beneocountryList
	 * @return
	 * From Transaction list need to pass transaction history : idno
	 * And From beneficiary list need to pass Beneficiary :idNo
	 */
	
	@RequestMapping(value = "/defaultbene/", method = RequestMethod.POST)
	public ApiResponse defaultBeneficiary(@RequestParam(required=false,value="beneRelationId") BigDecimal beneRelationId,
			@RequestParam(required=false,value="transactionId") BigDecimal transactionId) {
		LOGGER.info("Bene disable method Trnx Report:");
		ApiResponse response=null;
		try {
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		LOGGER.info( RELATIONSHIP_ID + beneRelationId);
		LOGGER.info(CUSTOMER_ID + customerId);
		LOGGER.info("applicationCountryId  :" + applicationCountryId);
		response= beneService.getDefaultBeneficiary(customerId, applicationCountryId, beneRelationId,transactionId);
		}catch(Exception e) {
			LOGGER.error("exception in defaultBeneficiary : ",e);
		}
		return response;
	}
	
	/**
	 * Author : FavouriteBeneficiaryList
	 * @return : Customer Id,application country Id
	 */
	
	@RequestMapping(value = "/favouritebenelist/", method = RequestMethod.GET)
	public ApiResponse favouriteBeneficiary() {
		LOGGER.info("getFavouriteBeneficiaryList controller :");
		ApiResponse response=null;
		
		try {
			BigDecimal customerId = metaData.getCustomerId();
			BigDecimal applicationCountryId = metaData.getCountryId();
			LOGGER.info("favouritebene customerId Id :" + customerId);
			LOGGER.info("favouritebene applicationCountryId  :" + applicationCountryId);
			
			response = beneService.getFavouriteBeneficiaryList(customerId, applicationCountryId);
		}catch(Exception e) {
			LOGGER.error("Error while fetching favourite BeneficiaryList : ",e);
		}
		return response;
	}
	
	@RequestMapping(value = "/disable/", method = RequestMethod.POST)
	public ApiResponse beneDisable(@RequestParam("beneMasSeqId") BigDecimal beneMasterSeqId,@RequestParam("remarks") String remarks) {
		LOGGER.info("Bene disable method Trnx Report:");
		BigDecimal customerId = metaData.getCustomerId();
		BeneficiaryListDTO beneDetails = new BeneficiaryListDTO();
		beneDetails.setCustomerId(customerId);
		beneDetails.setBeneficaryMasterSeqId(beneMasterSeqId);
		beneDetails.setRemarks(remarks);
		LOGGER.info(CUSTOMER_ID + beneDetails.getCustomerId());
		LOGGER.info(RELATIONSHIP_ID + beneDetails.getBeneficiaryRelationShipSeqId());
		LOGGER.info("Bene Master Id  :" + beneDetails.getBeneficaryMasterSeqId());
		LOGGER.info("Bene Acccount Id :" + beneDetails.getBeneficiaryAccountSeqId());

		return beneService.disableBeneficiary(beneDetails);
	}

	@RequestMapping(value = "/favoritebeneupdate/", method = RequestMethod.POST)
	public ApiResponse favoriteBeneUpdate(@RequestParam("beneMasSeqId") BigDecimal beneMasterSeqId) {
		LOGGER.info("Bene disable method Trnx Report:");
		BigDecimal customerId = metaData.getCustomerId();
		BeneficiaryListDTO beneDetails = new BeneficiaryListDTO();
		beneDetails.setCustomerId(customerId);
		beneDetails.setBeneficaryMasterSeqId(beneMasterSeqId);
		LOGGER.info(CUSTOMER_ID + beneDetails.getCustomerId());
		LOGGER.info(RELATIONSHIP_ID + beneDetails.getBeneficiaryRelationShipSeqId());
		LOGGER.info("Bene Master Id  :" + beneDetails.getBeneficaryMasterSeqId());
		LOGGER.info("Bene Acccount Id :" + beneDetails.getBeneficiaryAccountSeqId());
		
		return beneService.updateFavoriteBeneficiary(beneDetails);
	}
	
	@RequestMapping(value = "/beneupdate/", method = RequestMethod.POST)
	public ApiResponse getBeneUpdate(@RequestBody String jsonBeneDTO) {
		LOGGER.info("getRemittanceDetailForReport Trnx Report:");
		BeneficiaryListDTO beneficiaryDto = (BeneficiaryListDTO) converterUtil.unmarshall(jsonBeneDTO, BeneficiaryListDTO.class);
		LOGGER.info("Bene Master Id :" + beneficiaryDto.getBeneficaryMasterSeqId());
		LOGGER.info("Bene Rela Seq Id :" + beneficiaryDto.getBeneficiaryRelationShipSeqId());
		LOGGER.info("Bene Account Length :" + beneficiaryDto.getBeneficiaryAccountSeqId());
		LOGGER.info(CUSTOMER_ID + beneficiaryDto.getCustomerId() + "\t Reference :"+ beneficiaryDto.getServiceGroupId());
		LOGGER.info("Country Id :" + beneficiaryDto.getApplicationCountryId() + "\t Currency Id :"+ beneficiaryDto.getCurrencyId());
		
		return beneService.beneficiaryUpdate(beneficiaryDto);
	}
	
	@RequestMapping(value = "/accounttype/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryAccountType(@RequestParam("countryId") BigDecimal countryId) {
		LOGGER.info("getBeneficiaryAccountType countryId :"+countryId);
		
		return accountTypeService.getAccountTypeFromView(countryId);

	}
	
}
