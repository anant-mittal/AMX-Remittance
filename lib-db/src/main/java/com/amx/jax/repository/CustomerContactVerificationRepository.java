package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CustomerContactVerification;
import com.amx.jax.dict.ContactType;

@Transactional
public interface CustomerContactVerificationRepository extends CrudRepository<CustomerContactVerification, BigDecimal> {

	// @Query("select c from CustomerContactVerification c where id=?1")
	CustomerContactVerification findById(BigDecimal id);

	@Query("select cv from CustomerContactVerification cv where cv.customerId=?1 and cv.contactType=?2 and cv.contactValue=?3  and cv.isActive='Y' and (cv.createdDate > ?4 or cv.sendDate > ?4)")
	public List<CustomerContactVerification> getByContact(BigDecimal customerId, ContactType contactType,
			String contactValue, java.util.Date date);
	
	@Query("select cv from CustomerContactVerification cv where cv.customerId=?1 and cv.contactType=?2 and cv.isActive='Y' and (cv.createdDate > ?3 or cv.sendDate > ?3)")
	public List<CustomerContactVerification> getByContact(BigDecimal customerId, ContactType contactType, java.util.Date date);

	@Query("select cv from CustomerContactVerification cv where cv.contactType=?1 and cv.isActive='Y' and cv.sendDate < ?2")
	public List<CustomerContactVerification> getExpiredLinks(ContactType contactType, java.util.Date date);

	@Query("select cv from CustomerContactVerification cv where cv.isActive='Y' and cv.sendDate > ?1 and cv.createdByType='E'")
	public List<CustomerContactVerification> getByContactsByEmployee(java.util.Date date);

	@Query("select COUNT(cv) as total, cv.isActive as isActive from CustomerContactVerification cv where cv.customerId=?1 and cv.contactType=?2 and (cv.createdDate > ?3 or cv.sendDate > ?3) GROUP BY cv.isActive")
	public List<Object[]> getCountsByCustomer(BigDecimal customerId, ContactType contactType, java.util.Date date);
}
