package com.amx.jax.repository.customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.customer.CustomerNotifyHubRecord;

public interface IJaxPushNotificationDao extends JpaRepository<CustomerNotifyHubRecord, Serializable> {

	@Query("select n from PushNotificationRecord n where (n.customerId=:customerId "
			+ "or (n.customerId is null and (n.nationalityId=:nationalityId or "
			+ "(n.nationalityId is null and n.countryId=:countryId)))) and  n.notificationDate >= trunc(:notificationDate) ")
	public List<CustomerNotifyHubRecord> getJaxNotification(@Param("customerId") BigDecimal customerId,
			@Param("nationalityId") BigDecimal nationalityId, @Param("countryId") BigDecimal countryId,
			@Param("notificationDate") Date notificationDate);
}
