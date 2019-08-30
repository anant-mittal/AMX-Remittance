package com.amx.jax.repository.task;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.task.JaxNotificationTask;
import com.amx.jax.dbmodel.task.JaxNotificationTaskType;

public interface JaxNotificationTaskRepo extends CrudRepository<JaxNotificationTask, Serializable> {

	List<JaxNotificationTask> findByCustomerIdAndTaskTypeAndDocumentCategoryAndDocumentTypes(BigDecimal customerId, JaxNotificationTaskType taskType,
			String documentCat, String documentType);
	
	List<JaxNotificationTask> findByRemittanceTransactionid(BigDecimal remittanceTransactionId);
}
