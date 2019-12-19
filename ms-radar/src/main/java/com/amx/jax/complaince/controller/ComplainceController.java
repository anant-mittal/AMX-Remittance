package com.amx.jax.complaince.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.complaince.ExCbkStrReportLogDto;
import com.amx.jax.complaince.LoginDeatils;
import com.amx.jax.compliance.service.ComplianceService;
import com.amx.jax.model.response.remittance.ParameterDetailsDto;

@RestController
@RequestMapping("/complaince")
@SuppressWarnings("rawtypes")
public class ComplainceController implements IComplainceService {
	
	@Autowired
	ComplianceService complianceService;
		
	
	@RequestMapping(value = "/complaince-report/upload", method = RequestMethod.POST ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadComplainceReportFile(@RequestParam MultipartFile file,
			@RequestParam String token, @RequestParam BigDecimal docFyr, @RequestParam BigDecimal documnetNo) throws Exception {
		return complianceService.uploadComplainceReportFile(file, token, docFyr, documnetNo);
	}
	
	@RequestMapping(value = "/complaince-token/generation", method = { RequestMethod.POST })
	public String tokenGenaration(@RequestBody LoginDeatils loginDeatils) throws Exception {
		String response = complianceService.tokenGenaration(loginDeatils);
		return response;
				
	}
	
	
	@RequestMapping(value = "/complaince-deatils/txnRefNo", method = { RequestMethod.POST })
	public String complainceDetails(@RequestParam BigDecimal docFyr,@RequestParam BigDecimal docNo) throws Exception {
		String response = complianceService.generateXMLFile(docFyr,docNo);
		return response;
				
	}
	
	
	
	@RequestMapping(value = "/complaince-deatils/inquiry", method = { RequestMethod.GET })
	public AmxApiResponse<ExCbkStrReportLogDto, Object> complainceInqDetails(@RequestParam String fromDate,@RequestParam String toDate ) throws Exception {
		List<ExCbkStrReportLogDto> exReportLogList = complianceService.complainceInqData(fromDate,toDate);
		return AmxApiResponse.buildList(exReportLogList);
				
	}
	
	
	
	@RequestMapping(value = "/complaince-deatils/reason", method = { RequestMethod.GET })
	public AmxApiResponse<ParameterDetailsDto, Object> complainceReasonDetails() throws Exception {
		List<ParameterDetailsDto> reasonCodeList = complianceService.complainceReasonData();
		return AmxApiResponse.buildList(reasonCodeList);
				
	}
	
	
	
	@RequestMapping(value = "/complaince-deatils/action", method = { RequestMethod.GET })
	public AmxApiResponse<ParameterDetailsDto, Object> complainceActionDetails() throws Exception {
		List<ParameterDetailsDto> actionCodeList = complianceService.complainceActionData();
		return AmxApiResponse.buildList(actionCodeList);
				
	}
	
	

}
