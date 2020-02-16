package com.amx.jax.radar.testClient;

import static org.junit.Assert.assertNotNull;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.radaar.ActionParamDto;
import com.amx.jax.radaar.ExCbkStrReportLogDto;
import com.amx.jax.radaar.ReasonParamDto;
import com.amx.jax.radaar.SnapComplainceClient;
import com.amx.jax.radaar.UserFinancialYearDTO;
import ch.qos.logback.classic.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SnapComplainceClientTest  {
	private static final Logger LOGGER = (Logger) LoggerService.getLogger(SnapComplainceClientTest.class);

	@Autowired
	SnapComplainceClient snapComplainceClient;
	
	@Test
	public void testForComplainceInqDetails() {
		AmxApiResponse<ExCbkStrReportLogDto, Object> response = null;
		LOGGER.info("Response not set");
		String fromDate = "21/12/2019";
		String toDate = "23/12/2019";
		response = snapComplainceClient.getComplainceInqDetails(fromDate,toDate);
		LOGGER.info("Response is set");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	@Test
	public void testForUploadComplainceReportFile() throws Exception{
		AmxApiResponse<ExCbkStrReportLogDto, Object> response = null;
		LOGGER.info("Response not set");
		BigDecimal docFyr = new BigDecimal(2019);
		BigDecimal documentNo = new BigDecimal(27000081);
		String reasonCode ="102";
		String actionCode = "102";
		BigDecimal	employeeId = new BigDecimal(1344);
		response = snapComplainceClient.uploadComplainceReportFile(docFyr,documentNo,reasonCode,actionCode,employeeId);
		LOGGER.info("Response is set");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	@Test
	public void testForcomplainceActionDetails() {
		AmxApiResponse<ActionParamDto, Object> response = null;
		LOGGER.info("Response not set");
		response = snapComplainceClient.complainceActionDetails();
		LOGGER.info("Response is set");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	
	@Test
	public void testForcomplainceReasonDetails() {
		AmxApiResponse<ReasonParamDto, Object> response = null;
		LOGGER.info("Response not set");
		response = snapComplainceClient.complainceReasonDetails();
		LOGGER.info("Response is set");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
		
	
	@Test
	public void testForcomplainceDocYearDetails() {
		AmxApiResponse<UserFinancialYearDTO, Object> response = null;
		LOGGER.info("Response not set");
		response = snapComplainceClient.complainceDocYearDetails();
		LOGGER.info("Response is set");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
		
}
