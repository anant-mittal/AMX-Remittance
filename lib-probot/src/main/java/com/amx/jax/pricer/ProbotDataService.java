package com.amx.jax.pricer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.amx.jax.pricer.dto.HolidayResponseDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.DiscountMgmtRespDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;

public interface ProbotDataService extends AbstractProbotInterface {
	public AmxApiResponse<DiscountMgmtRespDTO, Object> getDiscountManagemet(DiscountMgmtReqDTO discountMgmtReqDTO);

	public List<HolidayResponseDTO> fetchHolidayList(BigDecimal Id, Date fromDate, Date toDate);

	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbanksAndServices(BigDecimal countryId,
			BigDecimal currencyId);

}
