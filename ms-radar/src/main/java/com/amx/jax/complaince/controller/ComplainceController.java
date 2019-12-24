
package com.amx.jax.complaince.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.UserFinancialYearDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.complaince.ActionParamDto;
import com.amx.jax.complaince.ReasonParamDto;
import com.amx.jax.compliance.service.ComplianceService;
import com.amx.jax.radaar.ExCbkStrReportLogDto;

@RestController
@SuppressWarnings("rawtypes")
public class ComplainceController implements IComplainceService {
	
	@Autowired
	ComplianceService complianceService;

	@RequestMapping(value = "/complaince-deatils/inquiry", method = { RequestMethod.GET })
	public AmxApiResponse<ExCbkStrReportLogDto, Object> complainceInqDetails(@RequestParam String fromDate,@RequestParam String toDate ) throws Exception {
		List<ExCbkStrReportLogDto> exReportLogList = complianceService.complainceInqData(fromDate,toDate);
		return AmxApiResponse.buildList(exReportLogList);
				
	}
	
	
	@RequestMapping(value = "/complaince-deatils/reason", method = { RequestMethod.GET })
	public AmxApiResponse<ReasonParamDto, Object> complainceReasonDetails() throws Exception {
		List<ReasonParamDto> reasonCodeList = complianceService.complainceReasonData();
		return AmxApiResponse.buildList(reasonCodeList);
				
	}
		
	
	@RequestMapping(value = ComplainceApiEndpoints.COMPLAINCE_DETAILS_ACTION, method = { RequestMethod.GET })
	public AmxApiResponse<ActionParamDto, Object> complainceActionDetails() throws Exception {
		List<ActionParamDto> actionCodeList = complianceService.complainceActionData();
		return AmxApiResponse.buildList(actionCodeList);
				
	}
		
	  @RequestMapping(value = "/complaince-report/upload", method =  RequestMethod.POST ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	  public  AmxApiResponse<ExCbkStrReportLogDto, Object>uploadComplainceReportFile( @RequestParam BigDecimal docFyr, @RequestParam
	  BigDecimal documnetNo,@RequestParam String reason, @RequestParam String action, @RequestParam BigDecimal employeeId) throws IOException { 
		  List<ExCbkStrReportLogDto>  uploadDetailsList = complianceService.uploadComplainceReportFile(docFyr, documnetNo, reason , action, employeeId); 
		  return AmxApiResponse.buildList(uploadDetailsList); 
		  
	  }
	 	
	  @RequestMapping(value = "/complaince-deatils/docfyr", method = {
	  RequestMethod.GET }) public AmxApiResponse<UserFinancialYearDTO, Object>complainceDoFyrDetails() throws Exception { 
		  List<UserFinancialYearDTO> finYearList = complianceService.complainceDocYearDetails();
	  return  AmxApiResponse.buildList(finYearList);
	  
	  }
	 

}
