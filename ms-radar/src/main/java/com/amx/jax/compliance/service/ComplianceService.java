
package com.amx.jax.compliance.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;
import org.apache.commons.io.IOUtils;
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
//import org.springframework.mock.web.MockMultipartFile;
import com.amx.amxlib.meta.model.UserFinancialYearDTO;
import com.amx.jax.complaince.ActionParamDto;
import com.amx.jax.complaince.ActionRepo;
import com.amx.jax.complaince.CBK_Report;
import com.amx.jax.complaince.ComplainceRepository;
import com.amx.jax.complaince.ExCbkReportLogRepo;
import com.amx.jax.complaince.ExCbkStrReportLOG;
import com.amx.jax.complaince.ExCbkStrReportLogDto;
import com.amx.jax.complaince.IndicatorParamDto;
import com.amx.jax.complaince.IndicatorRepo;
import com.amx.jax.complaince.LoginDeatils;
import com.amx.jax.complaince.ReasonParamDto;
import com.amx.jax.complaince.ReasonRepo;
import com.amx.jax.complaince.RemittanceTransactionRepository;
import com.amx.jax.complaince.ReportJaxB;
import com.amx.jax.complaince.UserFinancialYearRepo;
import com.amx.jax.complaince.controller.IComplainceService.ComplainceApiEndpoints.Paramss;
import com.amx.jax.dbmodel.webservice.ExOwsLoginCredentials;
import com.amx.jax.repository.webservice.ExOwsLoginCredentialsRepository;
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
	ActionRepo actionRepo;

	@Autowired
	ReasonRepo reasonRepo;

	@Autowired
	IndicatorRepo indicatorRepo;

	@Autowired
	ReportJaxB reportJaxB;

	@Autowired
	RemittanceTransactionRepository remittanceTransactionRepository;

	@Autowired
	ExOwsLoginCredentialsRepository exOwsLoginCredentialsRepository;

	@Autowired
	UserFinancialYearRepo financeYearRespository;

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

	public String tokenGenaration1(String userNAme, String password, String tokenLifeTime) throws Exception {
		{
			String response;

			JsonObject loginDeatilss = new JsonObject();
			loginDeatilss.addProperty("username", userNAme);
			loginDeatilss.addProperty("password", password);
			loginDeatilss.addProperty("tokenlifetime", tokenLifeTime);

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

	public List<ExCbkStrReportLOG> uploadComplainceReportFile(MultipartFile file, @RequestParam String token,
			@RequestParam BigDecimal docFyr, @RequestParam BigDecimal documnetNo) throws IOException {

		String resp = restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport")

				.field("charset", "UTF-8").field("ContentType", "application/octet-stream").field("zipfile", file)
				.cookie(new Cookie("SqlAuthCookie", token)).postForm().as(new ParameterizedTypeReference<String>() {
				});

		CBK_Report cbk = complainceRepository.getCBKReportByDocNoAndDocFyr(documnetNo, docFyr);

		List<ExCbkStrReportLOG> ex = exCbkReportLogRepo.updateCbkStrId(resp, cbk.getRemitTrnxId());

		System.out.println("STR Id " + resp);// remove after testing

		return ex;
	}

	/*
	 * public List<ExCbkStrReportLogDto> uploadComplainceReportFile1(@RequestParam
	 * BigDecimal docFyr, @RequestParam BigDecimal documnetNo) throws IOException {
	 * 
	 * //generating xml file String abc = generateXMLFile(docFyr, documnetNo);
	 * 
	 * CBK_Report cbk =
	 * complainceRepository.getCBKReportByDocNoAndDocFyr(documnetNo, docFyr);
	 * List<ExCbkStrReportLOG> ex =
	 * exCbkReportLogRepo.getComplainceData(cbk.getRemitTrnxId());
	 * 
	 * ExOwsLoginCredentials bankCode =
	 * exOwsLoginCredentialsRepository.findByBankCode(Paramss.COMPLAINCE_BANK_CODE);
	 * // generating token String token = null; try { token =
	 * tokenGenaration1(bankCode.getWsUserName(), bankCode.getWsPassword(),
	 * bankCode.getWsPin()); token = token.replaceAll("^\"|\"$", ""); } catch
	 * (Exception e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * String fileformat = ex.get(0).getReqXml().toString();
	 * 
	 * File file = reportJaxB.MakeZipfile1(fileformat);
	 * 
	 * FileInputStream input = new FileInputStream(file); MultipartFile
	 * multipartFile = new MockMultipartFile("file", file.getName(), "text/plain",
	 * IOUtils.toByteArray(input)); // MultipartFile multipartFile =
	 * (MultipartFile)file1; // File file = new
	 * File("src/test/resources/validation.txt"); // DiskFileItem fileItem = new
	 * DiskFileItem("file1", "text/plain", false, file1.getName(), (int)
	 * file1.length() , file1.getParentFile()); // fileItem.getOutputStream(); //
	 * MultipartFile multipartFile = // FileInputStream input = new
	 * FileInputStream(file1);
	 * 
	 * HttpHeaders headers = new HttpHeaders();
	 * headers.setContentType(MediaType.MULTIPART_FORM_DATA);
	 * 
	 * 
	 * 
	 * MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	 * body.add("file", input);
	 * 
	 * HttpEntity<MultiValueMap<String, Object>> requestEntity = new
	 * HttpEntity<>(body, headers);
	 * 
	 * 
	 * String serverUrl =
	 * "https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport";
	 * 
	 * 
	 * List<ExCbkStrReportLogDto> response =uploadComplaince(multipartFile, token,
	 * cbk);
	 * 
	 * return response;
	 * 
	 * 
	 * }
	 * 
	 */ public String generateXMLFile(@RequestParam BigDecimal docFyr, @RequestParam BigDecimal docNo) {

		String issuccess = "File generated successfully";
		CBK_Report cbk = complainceRepository.getCBKReportByDocNoAndDocFyr(docNo, docFyr);
		reportJaxB.Marshilling(cbk);

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
			return dto;
		}).collect(Collectors.toList());
	}

	public List<ReasonParamDto> complainceReasonData() {

		return reasonRepo.getComplainceReasonData(Paramss.COMPLAINCE_REASON_CODE).stream().map(j -> {
			ReasonParamDto paramdto = new ReasonParamDto();
			paramdto.setRecordId(Paramss.COMPLAINCE_REASON_CODE);
			paramdto.setReasonCode(j.getReasonCode());
			paramdto.setReasonDesc(j.getReasonDesc());
			return paramdto;
		}).collect(Collectors.toList());

	}

	public List<ActionParamDto> complainceActionData() {

		return actionRepo.getComplainceActionData(Paramss.COMPLAINCE_ACTN_CODE).stream().map(k -> {
			ActionParamDto actiondto = new ActionParamDto();
			actiondto.setRecordId(Paramss.COMPLAINCE_ACTN_CODE);
			actiondto.setActionCode(k.getActionCode());
			actiondto.setActionDesc(k.getActionDesc());
			;
			return actiondto;
		}).collect(Collectors.toList());
	}

	public List<UserFinancialYearDTO> complainceDocYearDetails() {

		return financeYearRespository.getFinancialYear().stream().map(k -> {
			UserFinancialYearDTO reporDto = new UserFinancialYearDTO();
			reporDto.setFinancialYear(k.getFinancialYear());
			reporDto.setFinancialYearID(k.getFinancialYearID());

			return reporDto;
		}).collect(Collectors.toList());
	}

	public List<IndicatorParamDto> complainceIndicatorData() {

		return indicatorRepo.getComplainceIndicatorData(Paramss.COMPLAINCE_INDICATOR).stream().map(j -> {
			IndicatorParamDto indicatordto = new IndicatorParamDto();
			indicatordto.setRecordId(Paramss.COMPLAINCE_INDICATOR);
			indicatordto.setIndicatorCode(j.getIndicatorCode());
			indicatordto.setIndicatorDesc(j.getIndicatorDesc());
			return indicatordto;
		}).collect(Collectors.toList());
	}

	public List<ExCbkStrReportLogDto> uploadComplaince(@RequestParam("File") MultipartFile file1,
			@RequestParam String token, CBK_Report cbk) throws IOException {

		String resp = restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport")

				.field("charset", "UTF-8").field("ContentType", "application/octet-stream").field("zipfile", file1)
				.cookie(new Cookie("SqlAuthCookie", token)).postForm().as(new ParameterizedTypeReference<String>() {
				});

		List<ExCbkStrReportLOG> ex = exCbkReportLogRepo.updateCbkStrId(resp, cbk.getRemitTrnxId());

		System.out.println("STR Id " + resp);// remove after testing

		resp = resp.replaceAll("^\"|\"$", "");

		List<ExCbkStrReportLOG> repro = exCbkReportLogRepo.updateCbkStrId(resp, cbk.getRemitTrnxId());

		System.out.println("STR Id " + resp);// remove after testing

		return exCbkReportLogRepo.getComplainceData(cbk.getRemitTrnxId()).stream().map(l -> {
			ExCbkStrReportLogDto reportDto = new ExCbkStrReportLogDto();
			reportDto.setCbkStrId(l.getCbkStrId());
			return reportDto;
		}).collect(Collectors.toList());
	}

}
