package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.radaar.ActionParamDto;
import com.amx.jax.radaar.ExCbkStrReportLogDto;
import com.amx.jax.radaar.ReasonParamDto;
import com.amx.jax.sso.SSOUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ReportFiuController {

    @Autowired
    com.amx.jax.radaar.SnapComplainceClient snapComplainceClient;
    
    @Autowired
	private SSOUser ssoUser;

     @RequestMapping(value = "/api/fiu/doc_year/list", method = { RequestMethod.GET })
     public AmxApiResponse<com.amx.jax.radaar.UserFinancialYearDTO, Object> complainceDocYearDetails() {
        return snapComplainceClient.complainceDocYearDetails();
     }
    
    @RequestMapping(value = "/api/fiu/reason/list", method = { RequestMethod.GET })
	public AmxApiResponse<ReasonParamDto, Object> complainceReasonDetails() throws Exception {
        return snapComplainceClient.complainceReasonDetails();
    }

    @RequestMapping(value = "/api/fiu/action/list", method = { RequestMethod.GET })
	public AmxApiResponse<ActionParamDto, Object> complainceActionDetails()  {
        return snapComplainceClient.complainceActionDetails();
    } 
     
    @RequestMapping(value = "/api/fiu/trnx_details/save", method = { RequestMethod.POST })
    public AmxApiResponse<ExCbkStrReportLogDto, Object> uploadComplainceReportFile(@RequestParam BigDecimal docFyr,
    @RequestParam BigDecimal documentNo, @RequestParam String reasonCode, @RequestParam String actionCode) throws Exception {
         BigDecimal employeeId=ssoUser.getUserDetails().getEmployeeId();
        return snapComplainceClient.uploadComplainceReportFile(docFyr,documentNo,reasonCode,actionCode,employeeId);
    } 

    @RequestMapping(value = "/api/fiu/trnx_history/list", method = { RequestMethod.GET })
     public AmxApiResponse<ExCbkStrReportLogDto, Object> getComplainceInqDetails(String fromDate, String toDate) {
        return snapComplainceClient.getComplainceInqDetails(fromDate,toDate);
    }


}
