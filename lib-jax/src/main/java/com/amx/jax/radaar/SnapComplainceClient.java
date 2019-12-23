package com.amx.jax.radaar;

import java.math.BigDecimal;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.response.remittance.ParameterDetailsDto;
import com.amx.jax.radaar.IComplainceService.ComplainceApiEndpoints;
import com.amx.jax.rest.RestService;

@Component
public class SnapComplainceClient {
	private static final Logger LOGGER = Logger.getLogger(SnapComplainceClient.class);

	@Autowired
	RestService restService;  

	public AmxApiResponse<ExCbkStrReportLogDto, Object> getComplainceInqDetails(String fromDate, String toDate) {
		StringBuilder sb = new StringBuilder();
		sb.append("&fromDate=").append(fromDate).append("&toDate=").append(toDate);
		LOGGER.info("Input String :" + sb.toString());
		String url = (ComplainceApiEndpoints.COMPLAINCE_DETAILS_INQUIRY) + sb.toString();
		return restService.ajax(url)
				.as(new ParameterizedTypeReference<AmxApiResponse<ExCbkStrReportLogDto, Object>>() {
				});

	}

	public AmxApiResponse<ParameterDetailsDto, Object> complainceReasonDetails() {

		String url = (ComplainceApiEndpoints.COMPLAINCE_DETAILS_REASON);
		return restService.ajax(url)
				.as(new ParameterizedTypeReference<AmxApiResponse<ParameterDetailsDto, Object>>() {
				});
		
	}

	public AmxApiResponse<ParameterDetailsDto, Object> complainceActionDetails() {

		String url = (ComplainceApiEndpoints.COMPLAINCE_DETAILS_ACTION);
		return restService.ajax(url)
				.as(new ParameterizedTypeReference<AmxApiResponse<ParameterDetailsDto, Object>>() {
				});

	}

	public AmxApiResponse<ExCbkStrReportLogDto, Object> uploadComplainceReportFile(@RequestParam BigDecimal docFyr,
			@RequestParam BigDecimal documentNo, @RequestParam String reasonCode, @RequestParam String actionCode, @RequestParam BigDecimal employeeId) throws Exception {

		try {

			return restService.ajax("https://goaml.kwfiu.gov.kw/goAMLWeb/api/Reports/PostReport")
					.meta(new JaxMetaInfo()).path(ComplainceApiEndpoints.COMPLAINCE_REPORT_UPLOAD)
					.queryParam("docFyr", docFyr).queryParam("documentNo", documentNo)
					.queryParam("reasonCode", reasonCode).queryParam("actionCode", actionCode)
					.queryParam("employeeId", employeeId)
					.as(new ParameterizedTypeReference<AmxApiResponse<ExCbkStrReportLogDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in upload complaince report: ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	public AmxApiResponse<ParameterDetailsDto, Object> complainceDocYearDetails() {

		String url = (ComplainceApiEndpoints.COMPLAINCE_DETAILS_DOCFYR);
		return restService.ajax(url)
				.as(new ParameterizedTypeReference<AmxApiResponse<ParameterDetailsDto, Object>>() {
				});

	}

}
