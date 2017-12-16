package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.BENE_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.constant.JaxChannel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.service.AccountTypeService;
import com.amx.jax.service.BeneficiaryOnlineService;

/**
 * 
 * @author   :Rabil
 * Purpose   :Beneficiary related function  
 *
 */
@RestController
@RequestMapping(BENE_API_ENDPOINT)
public class BeneficiaryController {
	final static Logger logger = Logger.getLogger(BeneficiaryController.class);
	
	@Autowired
	BeneficiaryOnlineService beneOnlineService;
	
	@Autowired
	AccountTypeService accountTypeService;
	
	@Autowired
	MetaData metaData;

	
	@RequestMapping(value = "/beneList/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryListResponse(@RequestParam("beneCountryId") BigDecimal beneCountryId) {
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		JaxChannel channel = metaData.getChannel();
		logger.info("userType :"+channel.name()+"\t customerId :"+customerId+"\t applicationCountryId :"+applicationCountryId+"\t beneCountryId :"+beneCountryId);
		ApiResponse response =null;
		if(channel!=null && channel.equals(JaxChannel.BRANCH)) {
			response = beneOnlineService.getBeneficiaryListForBranch(customerId, applicationCountryId,beneCountryId);
		}else {
			response = beneOnlineService.getBeneficiaryListForOnline(customerId, applicationCountryId,beneCountryId);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/benecountry/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryCountryListResponse(@RequestParam("userType") String userType,
			@RequestParam("customerId") BigDecimal customerId) {
		logger.info("userType :"+userType+"\t customerId :"+customerId);
		ApiResponse response ;
		if(userType!=null && userType.equalsIgnoreCase(JaxChannel.BRANCH.toString())) {
		 response = beneOnlineService.getBeneficiaryCountryListForBranch(customerId);
		}else {
			 response = beneOnlineService.getBeneficiaryCountryListForOnline(customerId);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/accounttype/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryAccountType(@RequestParam("countryId") BigDecimal countryId) {
		logger.info("getBeneficiaryAccountType countryId :"+countryId);
		ApiResponse response ;
		 response = accountTypeService.getAccountTypeFromView(countryId);
		return response;
	}
	
	
	/*@RequestMapping(value = "/benecheck/", method = RequestMethod.POST)
	public ApiResponse getRemittanceDetailForReport(@RequestBody String jsonBeneDTO) {
		logger.info("getRemittanceDetailForReport Trnx Report:");
		BeneficiaryListDTO beneficiaryDto = (BeneficiaryListDTO) converterUtil.unmarshall(jsonBeneDTO, BeneficiaryListDTO.class);
		logger.info("Bene Master Id :" + beneficiaryDto.getBeneficaryMasterSeqId());
		logger.info("Bene Rela Seq Id :" + beneficiaryDto.getBeneficiaryRelationShipSeqId());
		logger.info("Bene Account Length :" + beneficiaryDto.getBeneficiaryAccountSeqId());
		logger.info("Customer Id :" + beneficiaryDto.getCustomerId() + "\t Reference :"
				+ beneficiaryDto.getServiceGroupId());
		logger.info("Country Id :" + beneficiaryDto.getApplicationCountryId() + "\t Currency Id :"
				+ beneficiaryDto.getCurrencyId());

		//ApiResponse response = reportManagerService.generatePersonalRemittanceReceiptReportDetails(transactionHistroyDTO);
		//return response;
	}

	*/
	
	
}
