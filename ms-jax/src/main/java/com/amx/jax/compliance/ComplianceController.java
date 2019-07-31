package com.amx.jax.compliance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.compliance.ComplianceBlockedTrnxType;
import com.amx.jax.client.compliance.HighValueTrnxDto;
import com.amx.jax.client.compliance.IComplianceService;

@RestController
public class ComplianceController implements IComplianceService {

	@Autowired
	ComplianceTransactionManager complianceTransactionManager;

	@RequestMapping(path = Path.LIST_HVT, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<HighValueTrnxDto, Object> listHighValueTransaction(
			@RequestParam(name = Params.TRANSACTION_BLOCK_TYPE) ComplianceBlockedTrnxType trnxType) {
		List<HighValueTrnxDto> hvtList = complianceTransactionManager.listHighValueTransaction(trnxType);
		return AmxApiResponse.buildList(hvtList);
	}
}
