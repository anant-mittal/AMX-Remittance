package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.client.compliance.ComplianceTrnxdDocStatus;
import com.amx.jax.client.task.CustomerDocUploadNotificationTaskData;
import com.amx.jax.client.task.NotificationTaskDto;
import com.amx.jax.client.task.NotificationTaskPermission;
import com.amx.jax.compliance.ComplianceTransactionManager;
import com.amx.jax.customer.dao.EmployeeDao;
import com.amx.jax.customer.document.manager.CustomerDocumentUploadManager;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.compliance.ComplianceTrnxDocMap;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReference;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dbmodel.task.JaxNotificationTask;
import com.amx.jax.dbmodel.task.JaxNotificationTaskAssign;
import com.amx.jax.dbmodel.task.JaxNotificationTaskType;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.compliance.ComplianceTrnxDocMapRepo;
import com.amx.jax.repository.customer.CustomerDocumentTypeMasterRepo;
import com.amx.jax.repository.task.JaxNotificationTaskAssignRepo;
import com.amx.jax.repository.task.JaxNotificationTaskRepo;
import com.amx.jax.userservice.service.UserService;
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

	private static final Logger log = LoggerFactory.getLogger(NotificationTaskService.class);

	@Transactional
	public void notifyBranchUserForDocumentUpload(CustomerDocUploadNotificationTaskData data) {
		log.debug("notifyBranchUserForDocumentUpload service data: {} ", JsonUtil.toJson(data));
		RemittanceTransaction trnx = remittanceTransactionService.getRemittanceTransactionById(data.getRemittanceTransactionId());
		String docCategory = data.getDocumentCategory();
		JaxNotificationTask task = new JaxNotificationTask();
		task.setCreatedAt(new Date());
		task.setDocumentCategory(data.getDocumentCategory());
		task.setDocumentTypes(serializeDocumentTypes(data.getDocumentTypes()));
		task.setMessage(getNotificationMessageForDocUpload(data));
		task.setRemittanceTransactionid(data.getRemittanceTransactionId());
		task.setCustomerId(trnx.getCustomerId().getCustomerId());
		task.setTaskType(JaxNotificationTaskType.DOCUMENT_UPLOAD);
		JaxNotificationTaskAssign taskAssign = new JaxNotificationTaskAssign();
		taskAssign.setTask(task);
		taskAssign.setPermissions(NotificationTaskPermission.BRANCH_STAFF_VIEW_TASK.toString());
		taskAssign.setCountryBranchId(trnx.getBranchId().getCountryBranchId());
		jaxNotificationTaskAssignRepo.save(taskAssign);

		for (String docType : data.getDocumentTypes()) {
			ComplianceTrnxDocMap complianceTrnxDocMap = new ComplianceTrnxDocMap();
			complianceTrnxDocMap.setCustomerId(trnx.getCustomerId().getCustomerId());
			complianceTrnxDocMap.setDocTypeMaster(customerDocumentTypeMasterRepo.findByDocumentCategoryAndDocumentType(docCategory, docType));
			complianceTrnxDocMap.setRemittanceTransaction(data.getRemittanceTransactionId());
			complianceTrnxDocMap.setStatus(ComplianceTrnxdDocStatus.REQUESTED);
			complianceTrnxDocMapRepo.save(complianceTrnxDocMap);
		}
	}

	private String serializeDocumentTypes(List<String> documentTypes) {
		return String.join(",", documentTypes);
	}

	private String getNotificationMessageForDocUpload(CustomerDocUploadNotificationTaskData data) {
		RemittanceTransaction trnx = remittanceTransactionService.getRemittanceTransactionById(data.getRemittanceTransactionId());
		Customer customer = trnx.getCustomerId();
		StringBuilder sBuild = new StringBuilder();
		sBuild.append("Please request ");
		sBuild.append(customer.getFirstName()).append(" ").append(customer.getLastName());
		sBuild.append("-").append(customer.getIdentityInt());
		sBuild.append(" to submit ").append(String.join(",", data.getDocumentTypes()));
		sBuild.append(" for Trnx ID ").append(trnx.getDocumentFinanceYear()).append("/").append(trnx.getDocumentNo());
		return sBuild.toString();
	}

	public List<NotificationTaskDto> listUserNotificationTasks() {
		List<JaxNotificationTaskAssign> allNotifications = jaxNotificationTaskAssignRepo.findByCountryBranchId(metaData.getCountryBranchId());
		List<String> empViewPermissions = null; // TODO call rbaac api to fetch logged in emplyee's permissions
		List<JaxNotificationTaskAssign> allowedNotifications = allNotifications.stream().filter(i -> {
			String[] taskViewPermission = i.getPermissions().split(",");
			List<String> taskViewPermissionList = Arrays.asList(taskViewPermission);
			if (empViewPermissions != null) {
				for (String perm : empViewPermissions) {
					if (taskViewPermissionList.contains(perm)) {
						return true;
					}
				}
			}
			return true;
		}).collect(Collectors.toList());
		return convert(allowedNotifications);
	}

	private List<NotificationTaskDto> convert(List<JaxNotificationTaskAssign> allowedNotifications) {
		return allowedNotifications.stream().map(i -> {
			NotificationTaskDto dto = new NotificationTaskDto();
			JaxNotificationTask task = i.getTask();
			dto.setMessage(task.getMessage());
			dto.setCreationDate(task.getCreatedAt());
			CustomerDocUploadNotificationTaskData data = new CustomerDocUploadNotificationTaskData();
			data.setDocumentCategory(task.getDocumentCategory());
			data.setDocumentTypes(Arrays.asList(task.getDocumentTypes().split(",")));
			dto.setData(data);
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
				List<JaxNotificationTask> tasks = jaxNotificationTaskRepo.findByCustomerIdAndTaskTypeAndDocumentCategory(metaData.getCustomerId(),
						JaxNotificationTaskType.DOCUMENT_UPLOAD, docTypeMaster.getDocumentCategory());
				String uploadDocType = docTypeMaster.getDocumentType();
				for (JaxNotificationTask task : tasks) {
					String[] docTypes = task.getDocumentTypes().split(",");
					List<String> docTypeList = new LinkedList<>(Arrays.asList(docTypes));
					boolean containDocType = docTypeList.stream().anyMatch(uploadDocType::equals);
					if (containDocType) {
						docTypeList.remove(uploadDocType);
						if (docTypeList.size() == 0) {
							// re assign notfic to compliance
							task.getTaskAssign().forEach(i -> i.setPermissions(NotificationTaskPermission.COMPLIANCE_VIEW_TASK.toString()));

						} else {
							task.setDocumentTypes(String.join(",", docTypeList));
						}
						jaxNotificationTaskRepo.save(task);
					}
				}
			}
			// update trnx doc map
			complianceTransactionManager.updateTrnxDocMap(custUploadReferences, customerId);
		}
	}

}
