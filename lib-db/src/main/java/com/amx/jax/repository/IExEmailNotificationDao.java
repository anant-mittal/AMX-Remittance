package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.ExEmailNotification;

public interface IExEmailNotificationDao extends JpaRepository<ExEmailNotification, Serializable> {

	@Query(value = "select * from EX_EMAIL_NOTIFICATION where send_IND='PE'", nativeQuery = true)
	public List<ExEmailNotification> getEmailNotification();

	@Query(value = "select * from EX_EMAIL_NOTIFICATION where send_IND='BCE'", nativeQuery = true)
	public List<ExEmailNotification> getBeneCreationErrorEmailNotification();

	@Query(value = "select * from EX_EMAIL_NOTIFICATION where send_IND='SPE'", nativeQuery = true)
	public List<ExEmailNotification> getServiceProviderEmailNotification();

	@Query(value = "select * from EX_EMAIL_NOTIFICATION where send_IND='FCS'", nativeQuery = true)
	public List<ExEmailNotification> getFCSupportTeamEmailNotification();

	@Query(value = "select * from EX_EMAIL_NOTIFICATION where SEND_IND='RPS' and ISACTIVE='Y'", nativeQuery = true)
	public List<ExEmailNotification> getSalesAdminSupportEmailNotification();

}
