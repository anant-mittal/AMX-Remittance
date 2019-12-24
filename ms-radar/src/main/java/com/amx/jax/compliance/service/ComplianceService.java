
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.UserFinancialYearDTO;
import com.amx.jax.AppContextUtil;
import com.amx.jax.complaince.ActionParamDto;
import com.amx.jax.complaince.ActionRepo;
import com.amx.jax.complaince.ComplainceRepository;
import com.amx.jax.complaince.ExCbkReportLogRepo;
import com.amx.jax.complaince.ExCbkStrReportLOG;
import com.amx.jax.complaince.IndicatorParamDto;
import com.amx.jax.complaince.IndicatorRepo;
import com.amx.jax.complaince.ReasonParamDto;
import com.amx.jax.complaince.ReasonRepo;
import com.amx.jax.complaince.RemittanceTransactionRepository;
import com.amx.jax.complaince.ReportJaxB;
import com.amx.jax.complaince.UserFinancialYearRepo;
import com.amx.jax.complaince.jaxCbkReport;
import com.amx.jax.complaince.controller.IComplainceService.ComplainceApiEndpoints.Paramss;
import com.amx.jax.dbmodel.webservice.ExOwsLoginCredentials;
import com.amx.jax.error.JaxError;
import com.amx.jax.radaar.ExCbkStrReportLogDto;
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

	public String tokenGenaration(String userNAme, String password, String tokenLifeTime) throws Exception {
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

	public List<ExCbkStrReportLogDto> uploadComplainceReportFile(@RequestParam BigDecimal docFyr,
			@RequestParam BigDecimal documnetNo, @RequestParam String reason, @RequestParam String action, @RequestParam BigDecimal employeeId)
			throws IOException {
		
		List<ExCbkStrReportLogDto> response = null;
		String token = null;
		
		jaxCbkReport cbk = complainceRepository.getCBKReportByDocNoAndDocFyr(documnetNo, docFyr);
		
			List<ExCbkStrReportLOG> reportDetails = exCbkReportLogRepo.getComplainceData(cbk.getTranxNo());
			
			if(reportDetails.isEmpty()) {		
		
		generateXMLFile(docFyr, documnetNo, employeeId,reason,action);

	    List<ExCbkStrReportLOG> ex = exCbkReportLogRepo.getComplainceData(cbk.getTranxNo());

		ExOwsLoginCredentials bankCode = exOwsLoginCredentialsRepository.findByBankCode(Paramss.COMPLAINCE_BANK_CODE);
		
		try {
			token = tokenGenaration(bankCode.getWsUserName(), bankCode.getWsPassword(), bankCode.getWsPin());
			System.out.println("tokenbefore:"+token);
			token = token.replaceAll("^\"|\"$", "");
			System.out.println("tokenafter:"+token);
		} catch (Exception e) {

			e.printStackTrace();
		}

		String fileformat = ex.get(0).getReqXml().toString();

		File file = reportJaxB.MakeZipfile(fileformat);
		

		FileInputStream input = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "zipfile",
				IOUtils.toByteArray(input));

		response = uploadComplaince(multipartFile, token, cbk,reason,action );
					
			}else {
				throw new GlobalException(JaxError.DUPLICATE_TRNX_DETAILS, "Duplicate Transction Id");
			}
				
		return response;
	
	}

	public String generateXMLFile(BigDecimal docFyr, BigDecimal docNo, BigDecimal employeeId, String reasonCode, String actionCode ) {

		String issuccess = "File generated successfully";
		jaxCbkReport cbk = complainceRepository.getCBKReportByDocNoAndDocFyr(docNo, docFyr);
		reportJaxB.Marshilling(cbk,employeeId,reasonCode,actionCode);

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
			dto.setCustIsActive(i.getCustIsActive());
			dto.setReportType(i.getReportType());
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

	public List<ExCbkStrReportLogDto> uploadComplaince(MultipartFile file,
			String token, jaxCbkReport cbk, String reason , String action) throws IOException {

		String resp = restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport")

				.field("charset", "UTF-8").field("ContentType", "application/octet-stream").field("zipfile", file)
				.cookie(new Cookie("SqlAuthCookie", token)).postForm().as(new ParameterizedTypeReference<String>() {
				});

		resp = resp.replaceAll("^\"|\"$", "");

		exCbkReportLogRepo.updateCbkStrId(resp, cbk.getTranxNo());
		
		return exCbkReportLogRepo.getComplainceData(cbk.getTranxNo()).stream().map(l -> {
			ExCbkStrReportLogDto reportDto = new ExCbkStrReportLogDto();
			reportDto.setCbkStrId(l.getCbkStrId());
			reportDto.setActionCode(action);
			reportDto.setCreatedBy(l.getCreatedBy());
			reportDto.setCreatedDate(l.getCreatedDate());
			reportDto.setCustomerId(l.getCustomerId());
			reportDto.setCustomerName(l.getCustomerName());
			reportDto.setCbkStrRepLogId(l.getCbkStrRepLogId());
			reportDto.setCustomerrRef(l.getCustomerrRef());
			reportDto.setIpAddress(AppContextUtil.getUserClient().getIp().toString());
			reportDto.setReasonCode(reason);
			reportDto.setRemittanceTranxId(cbk.getTranxNo());
			return reportDto;
		}).collect(Collectors.toList());
	}

}
