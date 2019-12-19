
package com.amx.jax.compliance.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.amx.jax.complaince.CBK_Report;
import com.amx.jax.complaince.ComplainceRepository;
import com.amx.jax.complaince.ExCbkReportLogRepo;
import com.amx.jax.complaince.ExCbkStrReportLogDto;
import com.amx.jax.complaince.LoginDeatils;
import com.amx.jax.complaince.RemittanceTransactionRepository;
import com.amx.jax.complaince.ReportJaxB;
import com.amx.jax.complaince.controller.IComplainceService.ComplainceApiEndpoints.Paramss;
import com.amx.jax.model.response.remittance.ParameterDetailsDto;
import com.amx.jax.repository.ParameterDetailsRespository;
import com.amx.jax.rest.RestService;
import com.google.gson.JsonObject;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ComplianceService {

	private final Logger LOGGER = Logger.getLogger(ComplianceService.class);

	@Autowired
	RestService restService;

	@Autowired
	ComplainceRepository complainceRepository;

	@Autowired
	ExCbkReportLogRepo exCbkReportLogRepo;

	@Autowired
	ParameterDetailsRespository parameterDetailsRespository;

	@Autowired
	ReportJaxB reportJaxB;
	
	@Autowired
	RemittanceTransactionRepository remittanceTransactionRepository;

	public String tokenGenaration(@RequestBody LoginDeatils loginDeatils) throws Exception {
		{
			String response;

			JsonObject loginDeatilss = new JsonObject();
			loginDeatilss.addProperty("username", loginDeatils.getUserName());
			loginDeatilss.addProperty("password", loginDeatils.getPassword());
			loginDeatilss.addProperty("tokenlifetime", loginDeatils.getTokenLife());

			String content = loginDeatilss.toString();

			DataOutputStream out = null;
			try {

				URL url = new URL("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Authenticate/GetToken");
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("charset", "UTF-8");

				// sending request
				OutputStream wr = connection.getOutputStream();
				wr.write(content.getBytes());
				wr.flush();

				// reading response
				BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuffer response_buffer = new StringBuffer();
				while ((line = rd.readLine()) != null) {
					response_buffer.append(line);
				}

				rd.close();
				response = response_buffer.toString();

			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						LOGGER.info("" + e.getMessage());
					}
				}
			}

			return response;
		}

	}

	public String uploadComplainceReportFile(MultipartFile file, @RequestParam String token, @RequestParam BigDecimal docFyr,@RequestParam BigDecimal documnetNo ) throws IOException {		
		String resp = restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport")

				.field("charset", "UTF-8").field("ContentType", "application/octet-stream").field("zipfile", file)
				.cookie(new Cookie("SqlAuthCookie", token))
				.postForm().as(new ParameterizedTypeReference<String>() {
				});
		
		CBK_Report cbk = complainceRepository.getCBKReportByDocNoAndDocFyr(documnetNo,docFyr);
	
		exCbkReportLogRepo.updatePlaceOrdersForRateAlert(resp, cbk.getRemitTrnxId());
		
		return resp;
	}

	public String generateXMLFile(@RequestParam BigDecimal docFyr,@RequestParam BigDecimal docNo) {

		String issuccess = "File generated successfully";
		CBK_Report cbk = complainceRepository.getCBKReportByDocNoAndDocFyr(docNo,docFyr);
		reportJaxB.Marshilling(cbk);

		try {
			reportJaxB.MakeZipfile();
		} catch (IOException e) {

			e.printStackTrace();

		}
		return issuccess;

	}

	public List<ExCbkStrReportLogDto> complainceInqData(String fromDate, String toDate) {
		
		
		return exCbkReportLogRepo.getComplainceInqData(fromDate, toDate).stream().map(i -> {
			ExCbkStrReportLogDto dto = new ExCbkStrReportLogDto();
			dto.setActionCode(i.getActionCode());
			dto.setCbkStrId(i.getCbkStrId());
			dto.setCbkStrRepLogId(i.getCbkStrRepLogId());
			dto.setCreatedBy(i.getCreatedBy());
			dto.setCreatedDate(i.getCreatedDate());
			dto.setCustomerId(i.getCustomerId());
			dto.setCustomerName(i.getCustomerName());
			dto.setCustomerrRef(i.getCustomerrRef());
			dto.setReasonCode(i.getReasonCode());
			dto.setRemittanceTranxId(i.getRemittanceTranxId());
			dto.setIpAddress(i.getIpAddress());
			return dto;
		}).collect(Collectors.toList());
	}

	public List<ParameterDetailsDto> complainceReasonData() {

		return parameterDetailsRespository.fetchCodeDetails(Paramss.COMPLAINCE_REASON_CODE).stream().map(j -> {
			ParameterDetailsDto paramdto = new ParameterDetailsDto();
			paramdto.setRecordId(Paramss.COMPLAINCE_REASON_CODE);
			paramdto.setCharUdf1(j.getCharField1());
			return paramdto;
		}).collect(Collectors.toList());
		
		
	}

	public List<ParameterDetailsDto> complainceActionData() {

		return parameterDetailsRespository.fetchCodeDetails(Paramss.COMPLAINCE_ACTN_CODE).stream().map(k -> {
			ParameterDetailsDto actiondto = new ParameterDetailsDto();
			actiondto.setRecordId(Paramss.COMPLAINCE_ACTN_CODE);
			actiondto.setCharUdf1(k.getCharField1());
			return actiondto;
		}).collect(Collectors.toList());
	}

	public List<ParameterDetailsDto> complainceIndicatorData() {

		return parameterDetailsRespository.fetchCodeDetails(Paramss.COMPLAINCE_INDICATOR).stream().map(j -> {
			ParameterDetailsDto paramdto = new ParameterDetailsDto();
			paramdto.setRecordId(Paramss.COMPLAINCE_INDICATOR);
			paramdto.setCharUdf1(j.getCharField1());
			return paramdto;
		}).collect(Collectors.toList());
	}

}
