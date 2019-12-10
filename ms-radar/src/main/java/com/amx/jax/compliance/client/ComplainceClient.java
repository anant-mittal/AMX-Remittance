package com.amx.jax.compliance.client;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.complaince.LoginDeatils;
import com.amx.jax.complaince.ReportDto;
import com.amx.jax.complaince.controller.IComplainceService.ComplainceApiEndpoints;
import com.amx.jax.complaince.controller.IComplainceService.ComplainceApiEndpoints.Paramss;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.rest.RestService;

public class ComplainceClient {
	private static final Logger LOGGER = Logger.getLogger(ComplainceClient.class);
	
	@Autowired
	RestService restService;
	
	public AmxApiResponse<ReportDto, Object> uploadComplainceReportFile(MultipartFile file,String token) throws Exception {

		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", RestService.getByteArrayFile(file));

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

			return restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport").meta(new JaxMetaInfo())
					.path(ComplainceApiEndpoints.COMPLAINCE_REPORT_UPLOAD)
					.queryParam(Paramss.TOKEN_VALUE, token).post(requestEntity)
					.as(new ParameterizedTypeReference<AmxApiResponse<ReportDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in upload complaince report: ", e);
			return JaxSystemError.evaluate(e);
		}
	}


	public ApiResponse<LoginDeatils> tokenGenaration(String userName, String password, BigDecimal tokenLife)
			throws IncorrectInputException {
		LoginDeatils lmodel = new LoginDeatils();
		lmodel.setUserName(userName);
		lmodel.setPassword(password);
		lmodel.setTokenLife(tokenLife);
		
		return restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Authenticate/GetToken").meta(new JaxMetaInfo())
				.path(ComplainceApiEndpoints.COMPLAINCE_TOKEN_GENERATION)
				.post(new HttpEntity<LoginDeatils>(lmodel))
				.as(new ParameterizedTypeReference<ApiResponse<LoginDeatils>>() {
				});
	
	
}
}
