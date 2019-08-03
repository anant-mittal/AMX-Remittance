package com.amx.jax.compliance;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.client.compliance.ComplianceBlockedTrnxType;
import com.amx.jax.client.compliance.HighValueTrnxDto;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.document.manager.CustomerDocumentManager;
import com.amx.jax.dbmodel.compliance.ComplianceTrnxDocMap;
import com.amx.jax.dbmodel.compliance.HighValueComplianceAuth;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.customer.ComplianceTrnxDocumentInfo;
import com.amx.jax.repository.compliance.ComplianceTrnxDocMapRepo;
import com.amx.jax.repository.compliance.HighValueComplianceAuthRepo;

@Component
public class ComplianceTransactionManager {

	@Autowired
	HighValueComplianceAuthRepo highValueComplianceAuthRepo;
	@Autowired
	ComplianceTrnxDocMapRepo complianceTrnxDocMapRepo;
	@Autowired
	CustomerDocumentManager customerDocumentManager;

	public List<HighValueTrnxDto> listHighValueTransaction(ComplianceBlockedTrnxType trnxType) {
		if (trnxType == null) {
			throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Compliance transaction type can not be null");
		}
		List<HighValueTrnxDto> output = null;
		switch (trnxType) {
		case HVT_LOCAL:
			List<HighValueComplianceAuth> hvtLocal = highValueComplianceAuthRepo.findByhvtLocal(ConstantDocument.Yes);
			output = hvtLocal.stream().map(i -> convertHvtCompliance(i)).collect(Collectors.toList());
			break;
		case HVT_FC:
			List<HighValueComplianceAuth> hvtFc = highValueComplianceAuthRepo.findByHvtFc(ConstantDocument.Yes);
			output = hvtFc.stream().map(i -> convertHvtCompliance(i)).collect(Collectors.toList());
			break;
		case SUSPICIOUS:
			List<HighValueComplianceAuth> hvtSusp = highValueComplianceAuthRepo.findBySuspiciousTrnx(ConstantDocument.Yes);
			output = hvtSusp.stream().map(i -> convertHvtCompliance(i)).collect(Collectors.toList());
			break;
		}
		return output;
	}

	private HighValueTrnxDto convertHvtCompliance(HighValueComplianceAuth i) {
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
		return dto;
	}

	public List<ComplianceTrnxDocumentInfo> getTransactionDocuments(BigDecimal trnxId) {

		List<ComplianceTrnxDocMap> docs = complianceTrnxDocMapRepo.findById(trnxId);
		return docs.stream().map(i -> customerDocumentManager.convertToCustomerDocumentInfo(i.getCustomerDocumentUploadReference())).map(j -> {
			ComplianceTrnxDocumentInfo trnxDocInfo = new ComplianceTrnxDocumentInfo();
			try {
				BeanUtils.copyProperties(trnxDocInfo, j);
			} catch (Exception e) {
			}
			return trnxDocInfo;
		}).collect(Collectors.toList());
	}
}
