package com.amx.jax.compliance;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.client.compliance.ApproveDocRequest;
import com.amx.jax.client.compliance.ComplianceBlockedTrnxType;
import com.amx.jax.client.compliance.ComplianceTrnxdDocStatus;
import com.amx.jax.client.compliance.DeactivateCustomerRequest;
import com.amx.jax.client.compliance.HighValueTrnxDto;
import com.amx.jax.client.compliance.RejectDocRequest;
import com.amx.jax.client.task.CustomerDocUploadNotificationTaskData;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.document.manager.CustomerDocumentManager;
import com.amx.jax.customer.document.validate.DocumentScanValidator;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.compliance.ComplianceBlockedTrnxDocMap;
import com.amx.jax.dbmodel.compliance.HighValueComplianceAuth;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.ComplianceTrnxDocumentInfo;
import com.amx.jax.model.customer.CustomerDocumentInfo;
import com.amx.jax.model.customer.document.CustomerDocInfoDto;
import com.amx.jax.repository.compliance.ComplianceTrnxDocMapRepo;
import com.amx.jax.repository.compliance.HighValueComplianceAuthRepo;
import com.amx.jax.services.NotificationTaskService;
import com.amx.jax.services.RemittanceTransactionService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.service.UserService;
import com.amx.utils.JsonUtil;

@Component
public class ComplianceTransactionManager {

	@Autowired
	HighValueComplianceAuthRepo highValueComplianceAuthRepo;
	@Autowired
	ComplianceTrnxDocMapRepo complianceTrnxDocMapRepo;
	@Autowired
	CustomerDocumentManager customerDocumentManager;
	@Autowired
	DocumentScanValidator documentScanValidator;
	@Autowired
	RemittanceTransactionService remittanceTransactionService;
	@Autowired
	NotificationTaskService notificationTaskService;
	@Autowired
	CustomerDao customerDao;
	@Autowired
	MetaData metaData;
	@Autowired
	UserService userService;

	private static final Logger log = LoggerFactory.getLogger(ComplianceTransactionManager.class);

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
		dto.setTransactionId(i.getIdNo());
		RemittanceTransaction remittanceTransaction = remittanceTransactionService.getRemittanceTransactionById(i.getIdNo());
		dto.setCollectionDocNo(remittanceTransaction.getCollectionDocumentNo());
		dto.setCollectionDocCode(remittanceTransaction.getCollectionDocCode());
		dto.setCollectionDocYear(remittanceTransaction.getCollectionDocFinanceYear());
		return dto;
	}

	@Transactional
	public List<ComplianceTrnxDocumentInfo> getTransactionDocuments(BigDecimal trnxId) {

		List<ComplianceBlockedTrnxDocMap> docs = complianceTrnxDocMapRepo.findByRemittanceTransaction(trnxId);
		List<ComplianceTrnxDocumentInfo> complianceBlockedDocList = docs.stream().map(i -> {
			CustomerDocumentInfo customerDocInfo = customerDocumentManager.convertToCustomerDocumentInfo(i);
			ComplianceTrnxDocumentInfo complianceTrnxDocInfo = new ComplianceTrnxDocumentInfo();
			try {
				BeanUtils.copyProperties(complianceTrnxDocInfo, customerDocInfo);
			} catch (Exception e) {
			}
			complianceTrnxDocInfo.setStatus(i.getStatus());
			return complianceTrnxDocInfo;
		}).collect(Collectors.toList());

		return complianceBlockedDocList;
	}

	@Transactional
	public void approveTrnxDoc(ApproveDocRequest request) {
		log.info("approving hvt request: {}", JsonUtil.toJson(request));
		CustomerDocumentTypeMaster docTypemaster = documentScanValidator.validateDocCatAndDocType(request.getDocumentCategory(),
				request.getDocumentType());
		List<ComplianceBlockedTrnxDocMap> allDocs = complianceTrnxDocMapRepo.findByRemittanceTransaction(request.getRemittanceTransactionId());
		boolean allDocsApproved = true;
		for (ComplianceBlockedTrnxDocMap i : allDocs) {
			CustomerDocumentTypeMaster trnxDocTypeMaster = i.getDocTypeMaster();
			if (trnxDocTypeMaster.equals(docTypemaster)) {
				i.setStatus(ComplianceTrnxdDocStatus.APPROVED);
				complianceTrnxDocMapRepo.save(i);
			}
			if (!i.getStatus().equals(ComplianceTrnxdDocStatus.APPROVED)) {
				allDocsApproved = false;
			}
		}
		if (allDocsApproved) {
			log.info("clearing transaction as all docs are approved");
			remittanceTransactionService.clearHighValueTransaction(request.getRemittanceTransactionId(), request.getComplianceBlockedTrnxType());
			notificationTaskService.removeTaskForTransaction(request.getRemittanceTransactionId());
		}

	}

	public void updateTrnxDocMap(List<CustomerDocumentUploadReference> customerUploadRefs, BigDecimal customerId) {
		for (CustomerDocumentUploadReference customerUploadRef : customerUploadRefs) {
			List<ComplianceBlockedTrnxDocMap> trnxDocMapList = complianceTrnxDocMapRepo.findByDocTypeMasterAndCustomerIdAndStatus(
					customerUploadRef.getCustomerDocumentTypeMaster(), customerId, ComplianceTrnxdDocStatus.REQUESTED);
			trnxDocMapList.forEach(i -> {
				i.setCustomerDocumentUploadReference(customerUploadRef);
				i.setStatus(ComplianceTrnxdDocStatus.UPLOADED);
			});
			complianceTrnxDocMapRepo.save(trnxDocMapList);
		}

	}

	public void rejectTrnxDoc(RejectDocRequest request) {
		log.info("rejecting hvt doc request: {}", JsonUtil.toJson(request));
		CustomerDocumentTypeMaster docTypemaster = documentScanValidator.validateDocCatAndDocType(request.getDocumentCategory(),
				request.getDocumentType());
		List<ComplianceBlockedTrnxDocMap> allDocs = complianceTrnxDocMapRepo.findByRemittanceTransaction(request.getRemittanceTransactionId());
		for (ComplianceBlockedTrnxDocMap i : allDocs) {
			CustomerDocumentTypeMaster trnxDocTypeMaster = i.getCustomerDocumentUploadReference().getCustomerDocumentTypeMaster();
			if (trnxDocTypeMaster.equals(docTypemaster)) {
				i.setStatus(ComplianceTrnxdDocStatus.REJECTED);
				complianceTrnxDocMapRepo.save(i);
			}
			CustomerDocInfoDto customerDocInfoDto = new CustomerDocInfoDto(request.getDocumentCategory(), request.getNewDocumentType());
			CustomerDocUploadNotificationTaskData data = new CustomerDocUploadNotificationTaskData();
			data.setCustomerDocInfo(Arrays.asList(customerDocInfoDto));
			data.setRemittanceTransactionId(request.getRemittanceTransactionId());
			notificationTaskService.notifyBranchUserForDocumentUpload(data);
		}
		notificationTaskService.removeTaskForTransaction(request.getRemittanceTransactionId());
	}

	@Transactional
	public void deactivateCustomer(DeactivateCustomerRequest request) {
		log.info("deactivating customer by compliance");
		userService.deactivateCustomer(metaData.getCustomerId());
		Customer customer = customerDao.getCustById(metaData.getCustomerId());
		customer.setRemarks(request.getRemark());
		customerDao.saveCustomer(customer);
	}
}
