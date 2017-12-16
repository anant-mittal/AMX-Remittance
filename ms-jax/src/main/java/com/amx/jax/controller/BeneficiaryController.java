package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.BENE_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
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
	final static Logger logger = Logger.getLogger(BeneficiaryController.class);
	
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
		logger.info("userType :"+channel.name()+"\t customerId :"+customerId+"\t applicationCountryId :"+applicationCountryId+"\t beneCountryId :"+beneCountryId);
		ApiResponse response =null;
		if(channel!=null && channel.equals(JaxChannel.BRANCH)) {
			response = beneService.getBeneficiaryListForBranch(customerId, applicationCountryId,beneCountryId);
		}else {
			response = beneService.getBeneficiaryListForOnline(customerId, applicationCountryId,beneCountryId);
		}
		return response;
	}


	

	
	
	
	@RequestMapping(value = "/benecountry/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryCountryListResponse() {
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal applicationCountryId = metaData.getCountryId();
		JaxChannel channel = metaData.getChannel();
		
		logger.info("userType :"+channel+"\t customerId :"+customerId);
		ApiResponse response ;
		if(channel!=null && channel.equals(JaxChannel.BRANCH)) {
		 response = beneService.getBeneficiaryCountryListForBranch(customerId);
		}else {
			 response = beneService.getBeneficiaryCountryListForOnline(customerId);
		}
		return response;
	}
	
	
	

	
	@RequestMapping(value = "/disable/", method = RequestMethod.POST)
	public ApiResponse beneDisable(@RequestParam("beneMasSeqId") BigDecimal beneMasterSeqId,@RequestParam("remarks") String remarks) {
		logger.info("Bene disable method Trnx Report:");
		
		ApiResponse response = null;
		
		BigDecimal customerId = metaData.getCustomerId();
		BeneficiaryListDTO beneDetails = new BeneficiaryListDTO();
		//beneDetails.setBeneficiaryRelationShipSeqId(beneRelSeqId);
		beneDetails.setCustomerId(customerId);
		beneDetails.setBeneficaryMasterSeqId(beneMasterSeqId);
		beneDetails.setRemarks(remarks);
		logger.info("Relation ship Id :" + beneDetails.getCustomerId());
		logger.info("Relation ship Id :" + beneDetails.getBeneficiaryRelationShipSeqId());
		logger.info("Bene Master Id  :" + beneDetails.getBeneficaryMasterSeqId());
		logger.info("Bene Acccount Id :" + beneDetails.getBeneficiaryAccountSeqId());
		response= beneService.disableBeneficiary(beneDetails);
		return response;
	}

	
	
	
	
	
	
	@RequestMapping(value = "/beneupdate/", method = RequestMethod.POST)
	public ApiResponse getBeneUpdate(@RequestBody String jsonBeneDTO) {
		logger.info("getRemittanceDetailForReport Trnx Report:");
		BeneficiaryListDTO beneficiaryDto = (BeneficiaryListDTO) converterUtil.unmarshall(jsonBeneDTO, BeneficiaryListDTO.class);
		logger.info("Bene Master Id :" + beneficiaryDto.getBeneficaryMasterSeqId());
		logger.info("Bene Rela Seq Id :" + beneficiaryDto.getBeneficiaryRelationShipSeqId());
		logger.info("Bene Account Length :" + beneficiaryDto.getBeneficiaryAccountSeqId());
		logger.info("Customer Id :" + beneficiaryDto.getCustomerId() + "\t Reference :"+ beneficiaryDto.getServiceGroupId());
		logger.info("Country Id :" + beneficiaryDto.getApplicationCountryId() + "\t Currency Id :"+ beneficiaryDto.getCurrencyId());
		ApiResponse response = beneService.beneficiaryUpdate(beneficiaryDto);
		return response;
	}

	
	
	
	@RequestMapping(value = "/accounttype/", method = RequestMethod.GET)
	public ApiResponse getBeneficiaryAccountType(@RequestParam("countryId") BigDecimal countryId) {
		logger.info("getBeneficiaryAccountType countryId :"+countryId);
		ApiResponse response =null;
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
