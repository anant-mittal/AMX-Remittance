package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.AppContextUtil;
import com.amx.jax.apiwrapper.JaxRbaacServiceWrapper;
import com.amx.jax.client.compliance.ComplianceTrnxdDocStatus;
import com.amx.jax.client.task.AbstractNotificationTaskMessageDto;
import com.amx.jax.client.task.BranchNotificationTaskMessageDto;
import com.amx.jax.client.task.ComplianceNotificationTaskMessageDto;
import com.amx.jax.client.task.CustomerDocUploadNotificationTaskData;
import com.amx.jax.client.task.JaxNotificationTaskType;
import com.amx.jax.client.task.ListCustomerDocInfoTaskDto;
import com.amx.jax.client.task.NotificationTaskDto;
import com.amx.jax.client.task.NotificationTaskPermission;
import com.amx.jax.compliance.ComplianceTransactionManager;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.customer.dao.EmployeeDao;
import com.amx.jax.customer.document.manager.CustomerDocumentUploadManager;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.compliance.ComplianceBlockedTrnxDocMap;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dbmodel.task.JaxNotificationTask;
import com.amx.jax.dbmodel.task.JaxNotificationTaskAssign;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.document.CustomerDocInfoDto;
import com.amx.jax.repository.compliance.ComplianceTrnxDocMapRepo;
import com.amx.jax.repository.customer.CustomerDocumentTypeMasterRepo;
import com.amx.jax.repository.task.JaxNotificationTaskAssignRepo;
import com.amx.jax.repository.task.JaxNotificationTaskRepo;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.validation.NotificationTaskServiceValidator;
import com.amx.utils.JsonUtil;

@Service
public class NotificationTaskService {

	@Autowired
	JaxNotificationTaskAssignRepo jaxNotificationTaskAssignRepo;
	@Autowired
	JaxNotificationTaskRepo jaxNotificationTaskRepo;
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	MetaData metaData;
	@Autowired
	RemittanceTransactionService remittanceTransactionService;
	@Autowired
	UserService userSerivce;
	@Autowired
	CustomerDocumentTypeMasterRepo customerDocumentTypeMasterRepo;
	@Autowired
	ComplianceTrnxDocMapRepo complianceTrnxDocMapRepo;
	@Autowired
	CustomerDocumentUploadManager customerDocumentUploadManager;
	@Autowired
	ComplianceTransactionManager complianceTransactionManager;
	@Autowired
	JaxRbaacServiceWrapper jaxRbaacServiceWrapper;
	@Autowired
	NotificationTaskServiceValidator notificationTaskServiceValidator;

	private static final Logger log = LoggerFactory.getLogger(NotificationTaskService.class);

	@Transactional
	public void notifyBranchUserForDocumentUpload(CustomerDocUploadNotificationTaskData data) {
		log.debug("notifyBranchUserForDocumentUpload service data: {} ", JsonUtil.toJson(data));
		notificationTaskServiceValidator.validateNotifyBranchUserForDocumentUpload(data);
		RemittanceTransaction trnx = remittanceTransactionService.getRemittanceTransactionById(data.getRemittanceTransactionId());
		List<CustomerDocInfoDto> customerDocInfos = data.getCustomerDocInfo();
		for (CustomerDocInfoDto customerDocInfo : customerDocInfos) {
			String docCategory = customerDocInfo.getDocumentCategory();
			String docType = customerDocInfo.getDocumentType();
			JaxNotificationTask task = new JaxNotificationTask();
			task.setIsActive(ConstantDocument.Yes);
			task.setCreatedAt(new Date());
			task.setDocumentCategory(docCategory);
			task.setDocumentTypes(docType);
			task.setRemittanceTransactionid(data.getRemittanceTransactionId());
			task.setCustomerId(trnx.getCustomerId().getCustomerId());
			task.setTaskType(JaxNotificationTaskType.DOCUMENT_VERIFY);
			BranchNotificationTaskMessageDto messageDto = getDocUploadNotificationMessageForBranchStaff(task);
			task.setMessage(messageDto.getMessageText());
			task.setPermission(NotificationTaskPermission.COMPLIANCE_DOC_UPLOAD_RESOLVE.name());
			task.setRequestId(AppContextUtil.getTraceId());
			JaxNotificationTaskAssign taskAssign = new JaxNotificationTaskAssign();
			taskAssign.setTask(task);
			taskAssign.setCountryBranchId(trnx.getBranchId().getCountryBranchId());
			jaxNotificationTaskAssignRepo.save(taskAssign);

			ComplianceBlockedTrnxDocMap complianceTrnxDocMap = new ComplianceBlockedTrnxDocMap();
			complianceTrnxDocMap.setCustomerId(trnx.getCustomerId().getCustomerId());
			complianceTrnxDocMap.setDocTypeMaster(customerDocumentTypeMasterRepo.findByDocumentCategoryAndDocumentType(docCategory, docType));
			complianceTrnxDocMap.setRemittanceTransaction(data.getRemittanceTransactionId());
			complianceTrnxDocMap.setStatus(ComplianceTrnxdDocStatus.REQUESTED);
			complianceTrnxDocMapRepo.save(complianceTrnxDocMap);
		}
	}

	private BranchNotificationTaskMessageDto getDocUploadNotificationMessageForBranchStaff(JaxNotificationTask task) {
		RemittanceTransaction trnx = remittanceTransactionService.getRemittanceTransactionById(task.getRemittanceTransactionid());
		Customer customer = trnx.getCustomerId();
		BranchNotificationTaskMessageDto dto = new BranchNotificationTaskMessageDto();
		StringBuilder sBuild = new StringBuilder();
		sBuild.append("Please request ");
		sBuild.append(customer.getFirstName()).append(" ").append(customer.getLastName());
		dto.setFirstName(customer.getFirstName());
		dto.setLastName(customer.getLastName());
		sBuild.append("-").append(customer.getIdentityInt());
		dto.setIdentityInt(customer.getIdentityInt());
		sBuild.append(" to submit ").append(task.getDocumentTypes());
		dto.setDocumentTypes(task.getDocumentTypes());
		String trnxNo = trnx.getDocumentFinanceYear() + "/" + trnx.getDocumentNo();
		sBuild.append(" for Trnx ID ").append(trnxNo);
		dto.setTransactionNumber(trnxNo);
		dto.setMessageText(sBuild.toString());
		return dto;
	}

	private ComplianceNotificationTaskMessageDto getDocUploadNotificationMessageForCompliance(JaxNotificationTask task) {
		RemittanceTransaction trnx = remittanceTransactionService.getRemittanceTransactionById(task.getRemittanceTransactionid());
		ComplianceNotificationTaskMessageDto dto = new ComplianceNotificationTaskMessageDto();
		Customer customer = trnx.getCustomerId();
		StringBuilder sBuild = new StringBuilder();
		sBuild.append(customer.getFirstName()).append(" ").append(customer.getLastName());
		dto.setFirstName(customer.getFirstName());
		dto.setLastName(customer.getLastName());
		sBuild.append("-").append(customer.getIdentityInt());
		dto.setIdentityInt(customer.getIdentityInt());
		sBuild.append(" has submitted ").append(task.getDocumentTypes());
		dto.setDocumentTypes(task.getDocumentTypes());
		String trnxNo = trnx.getDocumentFinanceYear() + "/" + trnx.getDocumentNo();
		sBuild.append(" for Trnx ID ").append(trnxNo);
		dto.setTransactionNumber(trnxNo);
		dto.setMessageText(sBuild.toString());
		return dto;
	}

	public List<NotificationTaskDto> listUserNotificationTasks() {
		List<JaxNotificationTaskAssign> allNotifications = jaxNotificationTaskAssignRepo.findAll();
		Map<String, Map<String, String>> empPermissions = jaxRbaacServiceWrapper.getEmployeePermissions();
		List<JaxNotificationTaskAssign> allowedNotifications = allNotifications.stream().filter(i -> {
			if (!ConstantDocument.Yes.equals(i.getTask().getIsActive())) {
				return false;
			}
			if (i.getCountryBranchId() != null && !metaData.getCountryBranchId().equals(i.getCountryBranchId())) {
				return false;
			}
			return empPermissions.keySet().stream().anyMatch(j -> j.contains(i.getTask().getPermission()));
		}).collect(Collectors.toList());
		return convert(allowedNotifications);
	}

	private List<NotificationTaskDto> convert(List<JaxNotificationTaskAssign> allowedNotifications) {
		return allowedNotifications.stream().map(i -> {
			NotificationTaskDto dto = new NotificationTaskDto();
			JaxNotificationTask task = i.getTask();
			dto.setMessage(task.getMessage());
			dto.setCreationDate(task.getCreatedAt());
			ListCustomerDocInfoTaskDto data = new ListCustomerDocInfoTaskDto();
			data.setDocumentCategory(task.getDocumentCategory());
			data.setDocumentType(task.getDocumentTypes());
			data.setCustomerId(task.getCustomerId());
			dto.setRequestId(task.getRequestId());
			dto.setData(data);
			dto.setTaskType(task.getTaskType());
			dto.setMessageData(getNotificationMessageDto(task));
			data.setRemittanceTransactionId(task.getRemittanceTransactionid());
			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public void updateDocUploadNotificationTask(List<CustomerDocumentTypeMaster> customerTempUploads) {
		BigDecimal customerId = metaData.getCustomerId();
		List<CustomerDocumentUploadReference> custUploadReferences = customerDocumentUploadManager.fetchCustomerUploadedDocRef(customerTempUploads,
				customerId);
		if (CollectionUtils.isNotEmpty(custUploadReferences)) {
			log.info("sending notification to compliance user");
			for (CustomerDocumentUploadReference uploadDocMaster : custUploadReferences) {
				CustomerDocumentTypeMaster docTypeMaster = uploadDocMaster.getCustomerDocumentTypeMaster();
				List<ComplianceBlockedTrnxDocMap> complianceBlockedTrnxDocMap = complianceTrnxDocMapRepo
						.findByDocTypeMasterAndCustomerIdAndStatus(docTypeMaster, customerId, ComplianceTrnxdDocStatus.REQUESTED);
				// delete or update earlier task of branch mananger
				removeCurrentTaskNofication(docTypeMaster, customerId, JaxNotificationTaskType.DOCUMENT_UPLOAD);
				for (ComplianceBlockedTrnxDocMap complianceBlockedTrnxDoc : complianceBlockedTrnxDocMap) {
					// notify compliance of upload
					JaxNotificationTask task = new JaxNotificationTask();
					task.setIsActive(ConstantDocument.Yes);
					task.setRequestId(AppContextUtil.getTraceId());
					task.setCreatedAt(new Date());
					task.setDocumentCategory(docTypeMaster.getDocumentCategory());
					task.setDocumentTypes(docTypeMaster.getDocumentType());
					task.setRemittanceTransactionid(complianceBlockedTrnxDoc.getRemittanceTransaction());
					task.setCustomerId(customerId);
					task.setTaskType(JaxNotificationTaskType.DOCUMENT_UPLOAD);
					AbstractNotificationTaskMessageDto taskMessageDto = getNotificationMessageDto(task);
					task.setMessage(taskMessageDto.getMessageText());
					task.setPermission(NotificationTaskPermission.COMPLIANCE_DOC_UPLOAD_VERIFY.name());
					JaxNotificationTaskAssign taskAssign = new JaxNotificationTaskAssign();
					taskAssign.setTask(task);
					jaxNotificationTaskAssignRepo.save(taskAssign);
					// change status of trnx to uploaded
					complianceBlockedTrnxDoc.setStatus(ComplianceTrnxdDocStatus.UPLOADED);
					complianceBlockedTrnxDoc.setCustomerDocumentUploadReference(uploadDocMaster);
					complianceTrnxDocMapRepo.save(complianceBlockedTrnxDoc);
				}

			}
			// update trnx doc map
			complianceTransactionManager.updateTrnxDocMap(custUploadReferences, customerId);
		}
	}

	private AbstractNotificationTaskMessageDto getNotificationMessageDto(JaxNotificationTask task) {
		switch (task.getTaskType()) {
		case DOCUMENT_UPLOAD:
			return getDocUploadNotificationMessageForBranchStaff(task);
		case DOCUMENT_VERIFY:
			return getDocUploadNotificationMessageForCompliance(task);
		default:
			return null;
		}
	}

	public void removeCurrentTaskNofication(CustomerDocumentTypeMaster docTypeMaster, BigDecimal customerId,
			JaxNotificationTaskType jaxNotificationTaskType) {

		List<JaxNotificationTask> branchTaskNotifications = jaxNotificationTaskRepo.findByCustomerIdAndTaskTypeAndDocumentCategoryAndDocumentTypes(
				customerId, jaxNotificationTaskType, docTypeMaster.getDocumentCategory(), docTypeMaster.getDocumentType());
		for (JaxNotificationTask task : branchTaskNotifications) {
			task.setIsActive(ConstantDocument.Deleted);
			task.setResponseId(AppContextUtil.getTraceId());
			jaxNotificationTaskRepo.save(task);
		}
	}

}
