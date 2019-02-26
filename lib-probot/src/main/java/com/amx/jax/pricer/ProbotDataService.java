package com.amx.jax.pricer;

import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.DiscountMgmtRespDTO;
import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;

public interface ProbotDataService extends AbstractProbotInterface {
	public AmxApiResponse<DiscountMgmtRespDTO, Object> getDiscountManagemet(DiscountMgmtReqDTO discountMgmtReqDTO);

	public AmxApiResponse<HolidayResponseDTO, Object> fetchHolidayList(BigDecimal Id, Date fromDate, Date toDate);

	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbanksAndServices(BigDecimal countryId,
			BigDecimal currencyId);

}
