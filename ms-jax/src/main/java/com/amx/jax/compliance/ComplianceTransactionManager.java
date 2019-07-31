package com.amx.jax.compliance;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.client.compliance.ComplianceBlockedTrnxType;
import com.amx.jax.client.compliance.HighValueTrnxDto;
import com.amx.jax.dbmodel.compliance.HighValueAMLAuthViewLocal;
import com.amx.jax.error.JaxError;
import com.amx.jax.repository.compliance.HighValueAMLAuthViewLocalRepo;

@Component
public class ComplianceTransactionManager {

	@Autowired
	HighValueAMLAuthViewLocalRepo highValueAMLAuthViewLocalRepo;

	public List<HighValueTrnxDto> listHighValueTransaction(ComplianceBlockedTrnxType trnxType) {
		if (trnxType == null) {
			throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Compliance transaction type can not be null");
		}
		List<HighValueTrnxDto> output = null;
		switch (trnxType) {
		case HVT_FC:
			List<HighValueAMLAuthViewLocal> hvtLocal = highValueAMLAuthViewLocalRepo.findAll();
			output = hvtLocal.stream().map(i -> convertHvtLocal(i)).collect(Collectors.toList());
			break;
		case HVT_KD:
			break;
		case SUSPICIOUS:
			break;
		}
		return output;
	}

	private HighValueTrnxDto convertHvtLocal(HighValueAMLAuthViewLocal i) {
		HighValueTrnxDto dto = new HighValueTrnxDto();
		dto.setBankBranch(i.getBranchName());
		dto.setBankCode(i.getBankCode());
		dto.setCustomerFullName(i.getCustomerName());
		dto.setCustomerReference(i.getCustomerReference().toString());
		dto.setForeignCurrencyQuote(i.getForeignCurrencyDesc());
		dto.setForeignTransactionAmount(i.getForeignTrnxAmount());
		dto.setLocalTransactionAmount(i.getLocalTransactionAmount());
		dto.setDocumentFinancialYear(i.getDocumentFinanceYear());
		dto.setDocumentNo(i.getDocumentNo());
		return null;
	}

}
