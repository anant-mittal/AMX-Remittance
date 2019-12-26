package com.amx.jax.radaar;

import java.math.BigDecimal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.radaar.IComplainceService.ComplainceApiEndpoints;
import com.amx.jax.rest.RestService;

@Component
public class SnapComplainceClient {
	private static final Logger LOGGER = Logger.getLogger(SnapComplainceClient.class);

	@Autowired
	RestService restService;
	@Autowired
	AppConfig appConfig;

	public AmxApiResponse<ExCbkStrReportLogDto, Object> getComplainceInqDetails(String fromDate, String toDate) {

		return restService.ajax(appConfig.getRadarURL()).path(ComplainceApiEndpoints.COMPLAINCE_DETAILS_INQUIRY).get()
				.queryParam("fromDate", fromDate).queryParam("toDate", toDate)
				.as(new ParameterizedTypeReference<AmxApiResponse<ExCbkStrReportLogDto, Object>>() {
				});

	}

	public AmxApiResponse<ReasonParamDto, Object> complainceReasonDetails() {

		return restService.ajax(appConfig.getRadarURL()).path(ComplainceApiEndpoints.COMPLAINCE_DETAILS_REASON).get()
				.as(new ParameterizedTypeReference<AmxApiResponse<ReasonParamDto, Object>>() {
				});

	}

	public AmxApiResponse<ActionParamDto, Object> complainceActionDetails() {

		return restService.ajax(appConfig.getRadarURL()).path(ComplainceApiEndpoints.COMPLAINCE_DETAILS_ACTION).get()
				.as(new ParameterizedTypeReference<AmxApiResponse<ActionParamDto, Object>>() {
				});

	}

	public AmxApiResponse<ExCbkStrReportLogDto, Object> uploadComplainceReportFile(@RequestParam BigDecimal docFyr,
			@RequestParam BigDecimal documentNo, @RequestParam String reasonCode, @RequestParam String actionCode,
			@RequestParam BigDecimal employeeId) throws Exception {

		return restService.ajax(appConfig.getRadarURL()).path(ComplainceApiEndpoints.COMPLAINCE_REPORT_UPLOAD)
				.queryParam("docFyr", docFyr).queryParam("documnetNo", documentNo).queryParam("reason", reasonCode)
				.queryParam("action", actionCode).queryParam("employeeId", employeeId).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<ExCbkStrReportLogDto, Object>>() {
				});
	}

public AmxApiResponse<UserFinancialYearDTO, Object> complainceDocYearDetails() {

		return restService.ajax(appConfig.getRadarURL()).path(ComplainceApiEndpoints.COMPLAINCE_DETAILS_DOCFYR).get()
				.as(new ParameterizedTypeReference<AmxApiResponse<UserFinancialYearDTO, Object>>() {
				});

	}

}
