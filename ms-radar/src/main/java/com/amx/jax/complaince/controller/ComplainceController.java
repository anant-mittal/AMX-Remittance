package com.amx.jax.complaince.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.amx.jax.complaince.LoginDeatils;
import com.amx.jax.compliance.service.ComplianceService;

@RestController
@RequestMapping("/complaince")
public class ComplainceController implements IComplainceService {
	
	@Autowired
	ComplianceService complianceService;
		
	
	@RequestMapping(value = "/complaince-report/upload", method = RequestMethod.POST ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Map<String, Object> uploadComplainceReportFile(@RequestParam MultipartFile file,
			@RequestParam String token) throws Exception {
		return complianceService.uploadComplainceReportFile(file, token);
	}
	
	@RequestMapping(value = "/complaince-token/generation", method = { RequestMethod.POST })
	public String tokenGenaration(@RequestBody LoginDeatils loginDeatils) throws Exception {
		String response = complianceService.tokenGenaration(loginDeatils);
		return response;
				
	}
	
	
	
	

}
