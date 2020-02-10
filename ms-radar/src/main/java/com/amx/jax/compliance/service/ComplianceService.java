
package com.amx.jax.compliance.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.UserFinancialYearDTO;
import com.amx.jax.AppContextUtil;
import com.amx.jax.client.AbstractJaxServiceClient;
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
import com.amx.jax.radar.RadarConfig;
import com.amx.jax.repository.webservice.ExOwsLoginCredentialsRepository;
import com.amx.jax.rest.RestService;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ComplianceService extends AbstractJaxServiceClient{

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
		
			String response;
			
			Map<String, Object> jsonMap = new HashMap<String, Object>();

			jsonMap.put("username", userNAme);
			jsonMap.put("password", password);
			jsonMap.put("tokenlifetime", tokenLifeTime);
		
			JSONObject obj =new JSONObject(jsonMap);
			String content = obj.toString();
			
			HttpEntity<Object> requestEntity = new HttpEntity<Object>(getHeader());
			
			response = restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Authenticate/GetToken")
					.field("charset", "UTF-8").field("ContentType", "application/octet-stream").post(requestEntity).postJson(content).as(new ParameterizedTypeReference<String>() {
					});

			return response;
		

	}


	public List<ExCbkStrReportLogDto> uploadComplainceReportFile(@RequestParam BigDecimal docFyr,
			@RequestParam BigDecimal documnetNo, @RequestParam String reason, @RequestParam String action, @RequestParam BigDecimal employeeId)
					throws IOException, SQLException {

		List<ExCbkStrReportLogDto> response = null;
		String token = null;
		
		String fileName;
		 boolean fileExists = false; 


		jaxCbkReport cbk = complainceRepository.getCBKReportByDocNoAndDocFyr(documnetNo, docFyr);
		LOGGER.debug("xml  value" + cbk.getTranxNo());
		

		if(cbk!=null) {
			
			List<ExCbkStrReportLOG> reportDetails = exCbkReportLogRepo.getComplainceData(cbk.getTranxNo());

			if(reportDetails.isEmpty()) {		

				generateXMLFile(docFyr, documnetNo, employeeId,reason,action);

				List<ExCbkStrReportLOG> ex = exCbkReportLogRepo.getComplainceData(cbk.getTranxNo());
				
				LOGGER.error("xml  value" + ex.get(0).getReqXml());

				ExOwsLoginCredentials bankCode = exOwsLoginCredentialsRepository.findByBankCode(Paramss.COMPLAINCE_BANK_CODE);

				try {
					token = tokenGenaration(bankCode.getWsUserName(), bankCode.getWsPassword(), bankCode.getWsPin());

					token = token.replaceAll("^\"|\"$", "");
					
				} catch (Exception e) {

					LOGGER.error("error in token generation" + e.getMessage());
				}
				
				Clob fileformat = ex.get(0).getReqXml();
				
				Reader reader = fileformat.getCharacterStream();
				
				StringWriter writer = new StringWriter();
				IOUtils.copy(reader, writer);
				String clobContent = writer.toString();

				File file = reportJaxB.MakeZipfile(clobContent);
				LOGGER.debug("File name123 "+file.getName());

				FileInputStream input = new FileInputStream(file);
				MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "zipfile",
						IOUtils.toByteArray(input));

				response = uploadComplaince(multipartFile, token, cbk,reason,action );
				
				fileName = file.getAbsolutePath();
				String fileUploadLocation =RadarConfig.getJobFIUzipLocationEnabled()+"/"+multipartFile.getOriginalFilename().toString();
				File newFile = new File(fileUploadLocation);

				LOGGER.debug("File name "+newFile);
							
				fileExists= newFile.exists();
				
				LOGGER.debug("File exists"+fileExists);
							
				if (fileExists) {
					
					input.close();
					newFile.delete();
					
					LOGGER.debug("File deleted ");
				}
								
				
			}else {
				throw new GlobalException(JaxError.DUPLICATE_TRNX_DETAILS, "Duplicate Transction Id");
			}
		}else {
			throw new GlobalException(JaxError.INVALID_TRNX_DETAILS, "Invalid Document Number or Document Financial Year");
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
			dto.setIscustActive(i.getCustIsActive());
			if(i.getCustIsActive() != null && i.getCustIsActive().equals("Y")) {
				dto.setCustomerStatusDesc("ACTIVE");
			}else {
				dto.setCustomerStatusDesc("DEACTIVE");
			}
			
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
