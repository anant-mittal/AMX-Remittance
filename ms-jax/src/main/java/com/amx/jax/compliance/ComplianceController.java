package com.amx.jax.compliance;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.compliance.ApproveDocRequest;
import com.amx.jax.client.compliance.ComplianceBlockedTrnxType;
import com.amx.jax.client.compliance.DeactivateCustomerRequest;
import com.amx.jax.client.compliance.HighValueTrnxDto;
import com.amx.jax.client.compliance.IComplianceService;
import com.amx.jax.client.compliance.RejectDocRequest;
import com.amx.jax.model.customer.ComplianceTrnxDocumentInfo;

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

	@Override
	@RequestMapping(path = Path.GET_TRANSACTION_DOCUMENT, method = RequestMethod.GET)
	public AmxApiResponse<ComplianceTrnxDocumentInfo, Object> getTransactionDocuments(@RequestParam(name = Params.TRANSACTION_ID) Long trnxId) {
		List<ComplianceTrnxDocumentInfo> trnxDocs = complianceTransactionManager.getTransactionDocuments(BigDecimal.valueOf(trnxId));
		return AmxApiResponse.buildList(trnxDocs);
	}

	@Override
	@RequestMapping(path = Path.APPROVE_TRANSACTOIN_DOCUMENT, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> approveTrnxDoc(@RequestBody @Valid ApproveDocRequest request) {
		complianceTransactionManager.approveTrnxDoc(request);
		return AmxApiResponse.build();
	}
	
	@Override
	@RequestMapping(path = Path.REJECT_TRANSACTOIN_DOCUMENT, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> rejectTrnxDoc(@RequestBody @Valid RejectDocRequest request) {
		complianceTransactionManager.rejectTrnxDoc(request);
		return AmxApiResponse.build();
	}
	
	@Override
	@RequestMapping(path = Path.DEACTIVATE_CUSTOMER, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> deactivateCustomer(@RequestBody @Valid DeactivateCustomerRequest request) {
		complianceTransactionManager.deactivateCustomer(request);
		return AmxApiResponse.build();
	}
}
