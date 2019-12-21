package com.amx.jax.compliance.client;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.amx.amxlib.exception.IncorrectInputException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.AbstractJaxServiceClient;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.complaince.CBK_Report;
import com.amx.jax.complaince.ExCbkStrReportLogDto;
import com.amx.jax.complaince.LoginDeatils;
import com.amx.jax.complaince.controller.IComplainceService.ComplainceApiEndpoints;
import com.amx.jax.complaince.controller.IComplainceService.ComplainceApiEndpoints.Paramss;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.response.remittance.ParameterDetailsDto;
import com.amx.jax.rest.RestService;

@Component
public class ComplainceClient extends AbstractJaxServiceClient {
	private static final Logger LOGGER = Logger.getLogger(ComplainceClient.class);

	@Autowired
	RestService restService;

	
	  public AmxApiResponse<CBK_Report, Object>
	  uploadComplainceReportFile(MultipartFile file,String token, BigDecimal
	  docFyr, BigDecimal docNo) throws Exception {
	  
	  try {
	  
	  HttpHeaders headers = new HttpHeaders();
	  headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	  
	  MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	  body.add("file", RestService.getByteArrayFile(file));
	  
	  HttpEntity<MultiValueMap<String, Object>> requestEntity = new
	  HttpEntity<>(body, headers);
	  
	  return
	  restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport"
	  ).meta(new JaxMetaInfo())
	  .path(ComplainceApiEndpoints.COMPLAINCE_REPORT_UPLOAD)
	  .queryParam(Paramss.TOKEN_VALUE, token).post(requestEntity)
	  .queryParam("docFyr", docFyr) .queryParam("docNo", docNo) .as(new
	  ParameterizedTypeReference<AmxApiResponse<CBK_Report, Object>>() { }); }
	  catch (Exception e) { LOGGER.error("exception in upload complaince report: ",
	  e); return JaxSystemError.evaluate(e); } }
	  
	 
	public AmxApiResponse<LoginDeatils, Object> tokenGenaration(String userName, String password, BigDecimal tokenLife)
			throws IncorrectInputException {
		LoginDeatils lmodel = new LoginDeatils();
		lmodel.setUserName(userName);
		lmodel.setPassword(password);
		lmodel.setTokenLife(tokenLife);

		return restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Authenticate/GetToken").meta(new JaxMetaInfo())
				.path(ComplainceApiEndpoints.COMPLAINCE_TOKEN_GENERATION).post(new HttpEntity<LoginDeatils>(lmodel))
				.as(new ParameterizedTypeReference<AmxApiResponse<LoginDeatils, Object>>() {
				});

	}

	public AmxApiResponse<CBK_Report, Object> xmlGenaration(BigDecimal docFyr, BigDecimal docNo)
			throws IncorrectInputException {
		CBK_Report lmodel = new CBK_Report();
		lmodel.setDocFyr(docFyr);
		lmodel.setDocNo(docNo);

		return restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport").meta(new JaxMetaInfo())
				.path(ComplainceApiEndpoints.COMPLAINCE_DETAILS_TXN_REF).post(new HttpEntity<CBK_Report>(lmodel))
				.as(new ParameterizedTypeReference<AmxApiResponse<CBK_Report, Object>>() {
				});

	}

	public AmxApiResponse<ExCbkStrReportLogDto, Object> getComplainceInqDetails(String fromDate, String toDate) {
		StringBuilder sb = new StringBuilder();
		sb.append("&fromDate=").append(fromDate).append("&toDate=").append(toDate);
		LOGGER.info("Input String :" + sb.toString());
		String url = (ComplainceApiEndpoints.COMPLAINCE_DETAILS_INQUIRY) + sb.toString();
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
		return restService.ajax(url).get(requestEntity)
				.as(new ParameterizedTypeReference<AmxApiResponse<ExCbkStrReportLogDto, Object>>() {
				});

	}

	public AmxApiResponse<ParameterDetailsDto, Object> complainceReasonDetails() {

		String url = (ComplainceApiEndpoints.COMPLAINCE_DETAILS_REASON);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
		return restService.ajax(url).get(requestEntity)
				.as(new ParameterizedTypeReference<AmxApiResponse<ParameterDetailsDto, Object>>() {
				});

	}

	public AmxApiResponse<ParameterDetailsDto, Object> complainceActionDetails() {

		String url = (ComplainceApiEndpoints.COMPLAINCE_DETAILS_ACTION);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
		return restService.ajax(url).get(requestEntity)
				.as(new ParameterizedTypeReference<AmxApiResponse<ParameterDetailsDto, Object>>() {
				});

	}

	public AmxApiResponse<ExCbkStrReportLogDto, Object> uploadComplainceReportFile1(@RequestParam BigDecimal docFyr,
			@RequestParam BigDecimal documnetNo) throws Exception {

		try {

			return restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport")
					.meta(new JaxMetaInfo()).path(ComplainceApiEndpoints.COMPLAINCE_REPORT_UPLOAD1)
					.queryParam("docFyr", docFyr).queryParam("docNo", documnetNo)
					.as(new ParameterizedTypeReference<AmxApiResponse<ExCbkStrReportLogDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in upload complaince report: ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	public AmxApiResponse<ParameterDetailsDto, Object> complainceDocYearDetails() {

		String url = (ComplainceApiEndpoints.COMPLAINCE_DETAILS_DOCFYR);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
		return restService.ajax(url).get(requestEntity)
				.as(new ParameterizedTypeReference<AmxApiResponse<ParameterDetailsDto, Object>>() {
				});

	}

}
